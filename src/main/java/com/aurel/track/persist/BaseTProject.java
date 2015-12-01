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

package com.aurel.track.persist;


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.torque.TorqueException;
import org.apache.torque.map.TableMap;
import org.apache.torque.om.BaseObject;
import org.apache.torque.om.ComboKey;
import org.apache.torque.om.DateKey;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.om.StringKey;
import org.apache.torque.om.Persistent;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;



import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TState;
import com.aurel.track.persist.TStatePeer;
import com.aurel.track.persist.TProjectType;
import com.aurel.track.persist.TProjectTypePeer;
import com.aurel.track.persist.TSystemState;
import com.aurel.track.persist.TSystemStatePeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TDomain;
import com.aurel.track.persist.TDomainPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TDomainBean;

import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TClassBean;
import com.aurel.track.beans.TProjectCategoryBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TProjectReportRepositoryBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TProjectAccountBean;
import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TVersionControlParameterBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.beans.TInitStateBean;
import com.aurel.track.beans.TEventBean;
import com.aurel.track.beans.TNotifySettingsBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.beans.TFilterCategoryBean;
import com.aurel.track.beans.TReportCategoryBean;
import com.aurel.track.beans.TMailTemplateConfigBean;
import com.aurel.track.beans.TWfActivityContextParamsBean;
import com.aurel.track.beans.TWorkflowConnectBean;
import com.aurel.track.beans.TOrgProjectSLABean;
import com.aurel.track.beans.TMailTextBlockBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TProject
 */
public abstract class BaseTProject extends TpBaseObject
{
    /** The Peer class */
    private static final TProjectPeer peer =
        new TProjectPeer();


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
     * Get the ObjectID
     *
     * @return Integer
     */
    public Integer getObjectID()
    {
        return objectID;
    }


    /**
     * Set the value of ObjectID
     *
     * @param v new value
     */
    public void setObjectID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }



        // update associated TAccessControlList
        if (collTAccessControlLists != null)
        {
            for (int i = 0; i < collTAccessControlLists.size(); i++)
            {
                ((TAccessControlList) collTAccessControlLists.get(i))
                        .setProjectID(v);
            }
        }

        // update associated TClass
        if (collTClasss != null)
        {
            for (int i = 0; i < collTClasss.size(); i++)
            {
                ((TClass) collTClasss.get(i))
                        .setProjectID(v);
            }
        }

        // update associated TProjectCategory
        if (collTProjectCategorys != null)
        {
            for (int i = 0; i < collTProjectCategorys.size(); i++)
            {
                ((TProjectCategory) collTProjectCategorys.get(i))
                        .setProjectID(v);
            }
        }

        // update associated TRelease
        if (collTReleases != null)
        {
            for (int i = 0; i < collTReleases.size(); i++)
            {
                ((TRelease) collTReleases.get(i))
                        .setProjectID(v);
            }
        }

        // update associated TWorkItem
        if (collTWorkItems != null)
        {
            for (int i = 0; i < collTWorkItems.size(); i++)
            {
                ((TWorkItem) collTWorkItems.get(i))
                        .setProjectID(v);
            }
        }

        // update associated TProjectReportRepository
        if (collTProjectReportRepositorys != null)
        {
            for (int i = 0; i < collTProjectReportRepositorys.size(); i++)
            {
                ((TProjectReportRepository) collTProjectReportRepositorys.get(i))
                        .setProjectID(v);
            }
        }

        // update associated TReportLayout
        if (collTReportLayouts != null)
        {
            for (int i = 0; i < collTReportLayouts.size(); i++)
            {
                ((TReportLayout) collTReportLayouts.get(i))
                        .setProject(v);
            }
        }

        // update associated TProjectAccount
        if (collTProjectAccounts != null)
        {
            for (int i = 0; i < collTProjectAccounts.size(); i++)
            {
                ((TProjectAccount) collTProjectAccounts.get(i))
                        .setProject(v);
            }
        }

        // update associated TDashboardScreen
        if (collTDashboardScreens != null)
        {
            for (int i = 0; i < collTDashboardScreens.size(); i++)
            {
                ((TDashboardScreen) collTDashboardScreens.get(i))
                        .setProject(v);
            }
        }

        // update associated TVersionControlParameter
        if (collTVersionControlParameters != null)
        {
            for (int i = 0; i < collTVersionControlParameters.size(); i++)
            {
                ((TVersionControlParameter) collTVersionControlParameters.get(i))
                        .setProject(v);
            }
        }

        // update associated TField
        if (collTFields != null)
        {
            for (int i = 0; i < collTFields.size(); i++)
            {
                ((TField) collTFields.get(i))
                        .setProject(v);
            }
        }

        // update associated TFieldConfig
        if (collTFieldConfigs != null)
        {
            for (int i = 0; i < collTFieldConfigs.size(); i++)
            {
                ((TFieldConfig) collTFieldConfigs.get(i))
                        .setProject(v);
            }
        }

        // update associated TList
        if (collTLists != null)
        {
            for (int i = 0; i < collTLists.size(); i++)
            {
                ((TList) collTLists.get(i))
                        .setProject(v);
            }
        }

        // update associated TScreen
        if (collTScreens != null)
        {
            for (int i = 0; i < collTScreens.size(); i++)
            {
                ((TScreen) collTScreens.get(i))
                        .setProject(v);
            }
        }

        // update associated TScreenConfig
        if (collTScreenConfigs != null)
        {
            for (int i = 0; i < collTScreenConfigs.size(); i++)
            {
                ((TScreenConfig) collTScreenConfigs.get(i))
                        .setProject(v);
            }
        }

        // update associated TInitState
        if (collTInitStates != null)
        {
            for (int i = 0; i < collTInitStates.size(); i++)
            {
                ((TInitState) collTInitStates.get(i))
                        .setProject(v);
            }
        }

        // update associated TEvent
        if (collTEvents != null)
        {
            for (int i = 0; i < collTEvents.size(); i++)
            {
                ((TEvent) collTEvents.get(i))
                        .setProject(v);
            }
        }

        // update associated TNotifySettings
        if (collTNotifySettingss != null)
        {
            for (int i = 0; i < collTNotifySettingss.size(); i++)
            {
                ((TNotifySettings) collTNotifySettingss.get(i))
                        .setProject(v);
            }
        }

        // update associated TQueryRepository
        if (collTQueryRepositorys != null)
        {
            for (int i = 0; i < collTQueryRepositorys.size(); i++)
            {
                ((TQueryRepository) collTQueryRepositorys.get(i))
                        .setProject(v);
            }
        }

        // update associated TExportTemplate
        if (collTExportTemplates != null)
        {
            for (int i = 0; i < collTExportTemplates.size(); i++)
            {
                ((TExportTemplate) collTExportTemplates.get(i))
                        .setProject(v);
            }
        }

        // update associated TScripts
        if (collTScriptss != null)
        {
            for (int i = 0; i < collTScriptss.size(); i++)
            {
                ((TScripts) collTScriptss.get(i))
                        .setProject(v);
            }
        }

        // update associated TFilterCategory
        if (collTFilterCategorys != null)
        {
            for (int i = 0; i < collTFilterCategorys.size(); i++)
            {
                ((TFilterCategory) collTFilterCategorys.get(i))
                        .setProject(v);
            }
        }

        // update associated TReportCategory
        if (collTReportCategorys != null)
        {
            for (int i = 0; i < collTReportCategorys.size(); i++)
            {
                ((TReportCategory) collTReportCategorys.get(i))
                        .setProject(v);
            }
        }

        // update associated TMailTemplateConfig
        if (collTMailTemplateConfigs != null)
        {
            for (int i = 0; i < collTMailTemplateConfigs.size(); i++)
            {
                ((TMailTemplateConfig) collTMailTemplateConfigs.get(i))
                        .setProject(v);
            }
        }

        // update associated TWfActivityContextParams
        if (collTWfActivityContextParamss != null)
        {
            for (int i = 0; i < collTWfActivityContextParamss.size(); i++)
            {
                ((TWfActivityContextParams) collTWfActivityContextParamss.get(i))
                        .setProject(v);
            }
        }

        // update associated TWorkflowConnect
        if (collTWorkflowConnects != null)
        {
            for (int i = 0; i < collTWorkflowConnects.size(); i++)
            {
                ((TWorkflowConnect) collTWorkflowConnects.get(i))
                        .setProject(v);
            }
        }

        // update associated TOrgProjectSLA
        if (collTOrgProjectSLAs != null)
        {
            for (int i = 0; i < collTOrgProjectSLAs.size(); i++)
            {
                ((TOrgProjectSLA) collTOrgProjectSLAs.get(i))
                        .setProject(v);
            }
        }

        // update associated TMailTextBlock
        if (collTMailTextBlocks != null)
        {
            for (int i = 0; i < collTMailTextBlocks.size(); i++)
            {
                ((TMailTextBlock) collTMailTextBlocks.get(i))
                        .setProject(v);
            }
        }
    }

    /**
     * Get the Label
     *
     * @return String
     */
    public String getLabel()
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

        if (!ObjectUtils.equals(this.label, v))
        {
            this.label = v;
            setModified(true);
        }


    }

    /**
     * Get the DefaultOwnerID
     *
     * @return Integer
     */
    public Integer getDefaultOwnerID()
    {
        return defaultOwnerID;
    }


    /**
     * Set the value of DefaultOwnerID
     *
     * @param v new value
     */
    public void setDefaultOwnerID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.defaultOwnerID, v))
        {
            this.defaultOwnerID = v;
            setModified(true);
        }


        if (aTPersonRelatedByDefaultOwnerID != null && !ObjectUtils.equals(aTPersonRelatedByDefaultOwnerID.getObjectID(), v))
        {
            aTPersonRelatedByDefaultOwnerID = null;
        }

    }

    /**
     * Get the DefaultManagerID
     *
     * @return Integer
     */
    public Integer getDefaultManagerID()
    {
        return defaultManagerID;
    }


    /**
     * Set the value of DefaultManagerID
     *
     * @param v new value
     */
    public void setDefaultManagerID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.defaultManagerID, v))
        {
            this.defaultManagerID = v;
            setModified(true);
        }


        if (aTPersonRelatedByDefaultManagerID != null && !ObjectUtils.equals(aTPersonRelatedByDefaultManagerID.getObjectID(), v))
        {
            aTPersonRelatedByDefaultManagerID = null;
        }

    }

    /**
     * Get the DefaultInitStateID
     *
     * @return Integer
     */
    public Integer getDefaultInitStateID()
    {
        return defaultInitStateID;
    }


    /**
     * Set the value of DefaultInitStateID
     *
     * @param v new value
     */
    public void setDefaultInitStateID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.defaultInitStateID, v))
        {
            this.defaultInitStateID = v;
            setModified(true);
        }


        if (aTState != null && !ObjectUtils.equals(aTState.getObjectID(), v))
        {
            aTState = null;
        }

    }

    /**
     * Get the ProjectType
     *
     * @return Integer
     */
    public Integer getProjectType()
    {
        return projectType;
    }


    /**
     * Set the value of ProjectType
     *
     * @param v new value
     */
    public void setProjectType(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.projectType, v))
        {
            this.projectType = v;
            setModified(true);
        }


        if (aTProjectType != null && !ObjectUtils.equals(aTProjectType.getObjectID(), v))
        {
            aTProjectType = null;
        }

    }

    /**
     * Get the VersionSystemField0
     *
     * @return String
     */
    public String getVersionSystemField0()
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

        if (!ObjectUtils.equals(this.versionSystemField0, v))
        {
            this.versionSystemField0 = v;
            setModified(true);
        }


    }

    /**
     * Get the VersionSystemField1
     *
     * @return String
     */
    public String getVersionSystemField1()
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

        if (!ObjectUtils.equals(this.versionSystemField1, v))
        {
            this.versionSystemField1 = v;
            setModified(true);
        }


    }

    /**
     * Get the VersionSystemField2
     *
     * @return String
     */
    public String getVersionSystemField2()
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

        if (!ObjectUtils.equals(this.versionSystemField2, v))
        {
            this.versionSystemField2 = v;
            setModified(true);
        }


    }

    /**
     * Get the VersionSystemField3
     *
     * @return String
     */
    public String getVersionSystemField3()
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

        if (!ObjectUtils.equals(this.versionSystemField3, v))
        {
            this.versionSystemField3 = v;
            setModified(true);
        }


    }

    /**
     * Get the Deleted
     *
     * @return String
     */
    public String getDeleted()
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

        if (!ObjectUtils.equals(this.deleted, v))
        {
            this.deleted = v;
            setModified(true);
        }


    }

    /**
     * Get the Status
     *
     * @return Integer
     */
    public Integer getStatus()
    {
        return status;
    }


    /**
     * Set the value of Status
     *
     * @param v new value
     */
    public void setStatus(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.status, v))
        {
            this.status = v;
            setModified(true);
        }


        if (aTSystemState != null && !ObjectUtils.equals(aTSystemState.getObjectID(), v))
        {
            aTSystemState = null;
        }

    }

    /**
     * Get the CurrencyName
     *
     * @return String
     */
    public String getCurrencyName()
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

        if (!ObjectUtils.equals(this.currencyName, v))
        {
            this.currencyName = v;
            setModified(true);
        }


    }

    /**
     * Get the CurrencySymbol
     *
     * @return String
     */
    public String getCurrencySymbol()
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

        if (!ObjectUtils.equals(this.currencySymbol, v))
        {
            this.currencySymbol = v;
            setModified(true);
        }


    }

    /**
     * Get the HoursPerWorkDay
     *
     * @return Double
     */
    public Double getHoursPerWorkDay()
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

        if (!ObjectUtils.equals(this.hoursPerWorkDay, v))
        {
            this.hoursPerWorkDay = v;
            setModified(true);
        }


    }

    /**
     * Get the MoreProps
     *
     * @return String
     */
    public String getMoreProps()
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

        if (!ObjectUtils.equals(this.moreProps, v))
        {
            this.moreProps = v;
            setModified(true);
        }


    }

    /**
     * Get the TagLabel
     *
     * @return String
     */
    public String getTagLabel()
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

        if (!ObjectUtils.equals(this.tagLabel, v))
        {
            this.tagLabel = v;
            setModified(true);
        }


    }

    /**
     * Get the Description
     *
     * @return String
     */
    public String getDescription()
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

        if (!ObjectUtils.equals(this.description, v))
        {
            this.description = v;
            setModified(true);
        }


    }

    /**
     * Get the Prefix
     *
     * @return String
     */
    public String getPrefix()
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

        if (!ObjectUtils.equals(this.prefix, v))
        {
            this.prefix = v;
            setModified(true);
        }


    }

    /**
     * Get the NextItemID
     *
     * @return Integer
     */
    public Integer getNextItemID()
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

        if (!ObjectUtils.equals(this.nextItemID, v))
        {
            this.nextItemID = v;
            setModified(true);
        }


    }

    /**
     * Get the LastID
     *
     * @return Integer
     */
    public Integer getLastID()
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

        if (!ObjectUtils.equals(this.lastID, v))
        {
            this.lastID = v;
            setModified(true);
        }


    }

    /**
     * Get the Parent
     *
     * @return Integer
     */
    public Integer getParent()
    {
        return parent;
    }


    /**
     * Set the value of Parent
     *
     * @param v new value
     */
    public void setParent(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.parent, v))
        {
            this.parent = v;
            setModified(true);
        }


        if (aTProjectRelatedByParent != null && !ObjectUtils.equals(aTProjectRelatedByParent.getObjectID(), v))
        {
            aTProjectRelatedByParent = null;
        }

    }

    /**
     * Get the Sortorder
     *
     * @return Integer
     */
    public Integer getSortorder()
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

        if (!ObjectUtils.equals(this.sortorder, v))
        {
            this.sortorder = v;
            setModified(true);
        }


    }

    /**
     * Get the IsPrivate
     *
     * @return String
     */
    public String getIsPrivate()
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

        if (!ObjectUtils.equals(this.isPrivate, v))
        {
            this.isPrivate = v;
            setModified(true);
        }


    }

    /**
     * Get the IsTemplate
     *
     * @return String
     */
    public String getIsTemplate()
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

        if (!ObjectUtils.equals(this.isTemplate, v))
        {
            this.isTemplate = v;
            setModified(true);
        }


    }

    /**
     * Get the Domain
     *
     * @return Integer
     */
    public Integer getDomain()
    {
        return domain;
    }


    /**
     * Set the value of Domain
     *
     * @param v new value
     */
    public void setDomain(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.domain, v))
        {
            this.domain = v;
            setModified(true);
        }


        if (aTDomain != null && !ObjectUtils.equals(aTDomain.getObjectID(), v))
        {
            aTDomain = null;
        }

    }

    /**
     * Get the Uuid
     *
     * @return String
     */
    public String getUuid()
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

        if (!ObjectUtils.equals(this.uuid, v))
        {
            this.uuid = v;
            setModified(true);
        }


    }

    



    private TPerson aTPersonRelatedByDefaultOwnerID;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByDefaultOwnerID(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setDefaultOwnerID((Integer) null);
        }
        else
        {
            setDefaultOwnerID(v.getObjectID());
        }
        aTPersonRelatedByDefaultOwnerID = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByDefaultOwnerID()
        throws TorqueException
    {
        if (aTPersonRelatedByDefaultOwnerID == null && (!ObjectUtils.equals(this.defaultOwnerID, null)))
        {
            aTPersonRelatedByDefaultOwnerID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.defaultOwnerID));
        }
        return aTPersonRelatedByDefaultOwnerID;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByDefaultOwnerID(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByDefaultOwnerID == null && (!ObjectUtils.equals(this.defaultOwnerID, null)))
        {
            aTPersonRelatedByDefaultOwnerID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.defaultOwnerID), connection);
        }
        return aTPersonRelatedByDefaultOwnerID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByDefaultOwnerIDKey(ObjectKey key) throws TorqueException
    {

        setDefaultOwnerID(new Integer(((NumberKey) key).intValue()));
    }




    private TPerson aTPersonRelatedByDefaultManagerID;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByDefaultManagerID(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setDefaultManagerID((Integer) null);
        }
        else
        {
            setDefaultManagerID(v.getObjectID());
        }
        aTPersonRelatedByDefaultManagerID = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByDefaultManagerID()
        throws TorqueException
    {
        if (aTPersonRelatedByDefaultManagerID == null && (!ObjectUtils.equals(this.defaultManagerID, null)))
        {
            aTPersonRelatedByDefaultManagerID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.defaultManagerID));
        }
        return aTPersonRelatedByDefaultManagerID;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByDefaultManagerID(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByDefaultManagerID == null && (!ObjectUtils.equals(this.defaultManagerID, null)))
        {
            aTPersonRelatedByDefaultManagerID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.defaultManagerID), connection);
        }
        return aTPersonRelatedByDefaultManagerID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByDefaultManagerIDKey(ObjectKey key) throws TorqueException
    {

        setDefaultManagerID(new Integer(((NumberKey) key).intValue()));
    }




    private TState aTState;

    /**
     * Declares an association between this object and a TState object
     *
     * @param v TState
     * @throws TorqueException
     */
    public void setTState(TState v) throws TorqueException
    {
        if (v == null)
        {
            setDefaultInitStateID((Integer) null);
        }
        else
        {
            setDefaultInitStateID(v.getObjectID());
        }
        aTState = v;
    }


    /**
     * Returns the associated TState object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TState object
     * @throws TorqueException
     */
    public TState getTState()
        throws TorqueException
    {
        if (aTState == null && (!ObjectUtils.equals(this.defaultInitStateID, null)))
        {
            aTState = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.defaultInitStateID));
        }
        return aTState;
    }

    /**
     * Return the associated TState object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TState object
     * @throws TorqueException
     */
    public TState getTState(Connection connection)
        throws TorqueException
    {
        if (aTState == null && (!ObjectUtils.equals(this.defaultInitStateID, null)))
        {
            aTState = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.defaultInitStateID), connection);
        }
        return aTState;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTStateKey(ObjectKey key) throws TorqueException
    {

        setDefaultInitStateID(new Integer(((NumberKey) key).intValue()));
    }




    private TProjectType aTProjectType;

    /**
     * Declares an association between this object and a TProjectType object
     *
     * @param v TProjectType
     * @throws TorqueException
     */
    public void setTProjectType(TProjectType v) throws TorqueException
    {
        if (v == null)
        {
            setProjectType((Integer) null);
        }
        else
        {
            setProjectType(v.getObjectID());
        }
        aTProjectType = v;
    }


    /**
     * Returns the associated TProjectType object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProjectType object
     * @throws TorqueException
     */
    public TProjectType getTProjectType()
        throws TorqueException
    {
        if (aTProjectType == null && (!ObjectUtils.equals(this.projectType, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projectType));
        }
        return aTProjectType;
    }

    /**
     * Return the associated TProjectType object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProjectType object
     * @throws TorqueException
     */
    public TProjectType getTProjectType(Connection connection)
        throws TorqueException
    {
        if (aTProjectType == null && (!ObjectUtils.equals(this.projectType, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projectType), connection);
        }
        return aTProjectType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectTypeKey(ObjectKey key) throws TorqueException
    {

        setProjectType(new Integer(((NumberKey) key).intValue()));
    }




    private TSystemState aTSystemState;

    /**
     * Declares an association between this object and a TSystemState object
     *
     * @param v TSystemState
     * @throws TorqueException
     */
    public void setTSystemState(TSystemState v) throws TorqueException
    {
        if (v == null)
        {
            setStatus((Integer) null);
        }
        else
        {
            setStatus(v.getObjectID());
        }
        aTSystemState = v;
    }


    /**
     * Returns the associated TSystemState object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TSystemState object
     * @throws TorqueException
     */
    public TSystemState getTSystemState()
        throws TorqueException
    {
        if (aTSystemState == null && (!ObjectUtils.equals(this.status, null)))
        {
            aTSystemState = TSystemStatePeer.retrieveByPK(SimpleKey.keyFor(this.status));
        }
        return aTSystemState;
    }

    /**
     * Return the associated TSystemState object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TSystemState object
     * @throws TorqueException
     */
    public TSystemState getTSystemState(Connection connection)
        throws TorqueException
    {
        if (aTSystemState == null && (!ObjectUtils.equals(this.status, null)))
        {
            aTSystemState = TSystemStatePeer.retrieveByPK(SimpleKey.keyFor(this.status), connection);
        }
        return aTSystemState;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTSystemStateKey(ObjectKey key) throws TorqueException
    {

        setStatus(new Integer(((NumberKey) key).intValue()));
    }




    private TProject aTProjectRelatedByParent;

    /**
     * Declares an association between this object and a TProject object
     *
     * @param v TProject
     * @throws TorqueException
     */
    public void setTProjectRelatedByParent(TProject v) throws TorqueException
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTProjectRelatedByParent = v;
    }


    /**
     * Returns the associated TProject object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProject object
     * @throws TorqueException
     */
    public TProject getTProjectRelatedByParent()
        throws TorqueException
    {
        if (aTProjectRelatedByParent == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTProjectRelatedByParent = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.parent));
        }
        return aTProjectRelatedByParent;
    }

    /**
     * Return the associated TProject object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProject object
     * @throws TorqueException
     */
    public TProject getTProjectRelatedByParent(Connection connection)
        throws TorqueException
    {
        if (aTProjectRelatedByParent == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTProjectRelatedByParent = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.parent), connection);
        }
        return aTProjectRelatedByParent;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectRelatedByParentKey(ObjectKey key) throws TorqueException
    {

        setParent(new Integer(((NumberKey) key).intValue()));
    }




    private TDomain aTDomain;

    /**
     * Declares an association between this object and a TDomain object
     *
     * @param v TDomain
     * @throws TorqueException
     */
    public void setTDomain(TDomain v) throws TorqueException
    {
        if (v == null)
        {
            setDomain((Integer) null);
        }
        else
        {
            setDomain(v.getObjectID());
        }
        aTDomain = v;
    }


    /**
     * Returns the associated TDomain object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TDomain object
     * @throws TorqueException
     */
    public TDomain getTDomain()
        throws TorqueException
    {
        if (aTDomain == null && (!ObjectUtils.equals(this.domain, null)))
        {
            aTDomain = TDomainPeer.retrieveByPK(SimpleKey.keyFor(this.domain));
        }
        return aTDomain;
    }

    /**
     * Return the associated TDomain object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TDomain object
     * @throws TorqueException
     */
    public TDomain getTDomain(Connection connection)
        throws TorqueException
    {
        if (aTDomain == null && (!ObjectUtils.equals(this.domain, null)))
        {
            aTDomain = TDomainPeer.retrieveByPK(SimpleKey.keyFor(this.domain), connection);
        }
        return aTDomain;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTDomainKey(ObjectKey key) throws TorqueException
    {

        setDomain(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTAccessControlLists
     */
    protected List<TAccessControlList> collTAccessControlLists;

    /**
     * Temporary storage of collTAccessControlLists to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAccessControlLists()
    {
        if (collTAccessControlLists == null)
        {
            collTAccessControlLists = new ArrayList<TAccessControlList>();
        }
    }


    /**
     * Method called to associate a TAccessControlList object to this object
     * through the TAccessControlList foreign key attribute
     *
     * @param l TAccessControlList
     * @throws TorqueException
     */
    public void addTAccessControlList(TAccessControlList l) throws TorqueException
    {
        getTAccessControlLists().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TAccessControlList object to this object
     * through the TAccessControlList foreign key attribute using connection.
     *
     * @param l TAccessControlList
     * @throws TorqueException
     */
    public void addTAccessControlList(TAccessControlList l, Connection con) throws TorqueException
    {
        getTAccessControlLists(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTAccessControlLists
     */
    private Criteria lastTAccessControlListsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAccessControlLists(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAccessControlList> getTAccessControlLists()
        throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            collTAccessControlLists = getTAccessControlLists(new Criteria(10));
        }
        return collTAccessControlLists;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TAccessControlLists from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAccessControlList> getTAccessControlLists(Criteria criteria) throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            if (isNew())
            {
               collTAccessControlLists = new ArrayList<TAccessControlList>();
            }
            else
            {
                criteria.add(TAccessControlListPeer.PROJKEY, getObjectID() );
                collTAccessControlLists = TAccessControlListPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TAccessControlListPeer.PROJKEY, getObjectID());
                if (!lastTAccessControlListsCriteria.equals(criteria))
                {
                    collTAccessControlLists = TAccessControlListPeer.doSelect(criteria);
                }
            }
        }
        lastTAccessControlListsCriteria = criteria;

        return collTAccessControlLists;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAccessControlLists(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAccessControlList> getTAccessControlLists(Connection con) throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            collTAccessControlLists = getTAccessControlLists(new Criteria(10), con);
        }
        return collTAccessControlLists;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TAccessControlLists from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAccessControlList> getTAccessControlLists(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            if (isNew())
            {
               collTAccessControlLists = new ArrayList<TAccessControlList>();
            }
            else
            {
                 criteria.add(TAccessControlListPeer.PROJKEY, getObjectID());
                 collTAccessControlLists = TAccessControlListPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TAccessControlListPeer.PROJKEY, getObjectID());
                 if (!lastTAccessControlListsCriteria.equals(criteria))
                 {
                     collTAccessControlLists = TAccessControlListPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAccessControlListsCriteria = criteria;

         return collTAccessControlLists;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TAccessControlLists from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TAccessControlList> getTAccessControlListsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            if (isNew())
            {
               collTAccessControlLists = new ArrayList<TAccessControlList>();
            }
            else
            {
                criteria.add(TAccessControlListPeer.PROJKEY, getObjectID());
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAccessControlListPeer.PROJKEY, getObjectID());
            if (!lastTAccessControlListsCriteria.equals(criteria))
            {
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTAccessControlListsCriteria = criteria;

        return collTAccessControlLists;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TAccessControlLists from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TAccessControlList> getTAccessControlListsJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            if (isNew())
            {
               collTAccessControlLists = new ArrayList<TAccessControlList>();
            }
            else
            {
                criteria.add(TAccessControlListPeer.PROJKEY, getObjectID());
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAccessControlListPeer.PROJKEY, getObjectID());
            if (!lastTAccessControlListsCriteria.equals(criteria))
            {
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTRole(criteria);
            }
        }
        lastTAccessControlListsCriteria = criteria;

        return collTAccessControlLists;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TAccessControlLists from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TAccessControlList> getTAccessControlListsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            if (isNew())
            {
               collTAccessControlLists = new ArrayList<TAccessControlList>();
            }
            else
            {
                criteria.add(TAccessControlListPeer.PROJKEY, getObjectID());
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAccessControlListPeer.PROJKEY, getObjectID());
            if (!lastTAccessControlListsCriteria.equals(criteria))
            {
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTAccessControlListsCriteria = criteria;

        return collTAccessControlLists;
    }





    /**
     * Collection to store aggregation of collTClasss
     */
    protected List<TClass> collTClasss;

    /**
     * Temporary storage of collTClasss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTClasss()
    {
        if (collTClasss == null)
        {
            collTClasss = new ArrayList<TClass>();
        }
    }


    /**
     * Method called to associate a TClass object to this object
     * through the TClass foreign key attribute
     *
     * @param l TClass
     * @throws TorqueException
     */
    public void addTClass(TClass l) throws TorqueException
    {
        getTClasss().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TClass object to this object
     * through the TClass foreign key attribute using connection.
     *
     * @param l TClass
     * @throws TorqueException
     */
    public void addTClass(TClass l, Connection con) throws TorqueException
    {
        getTClasss(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTClasss
     */
    private Criteria lastTClasssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTClasss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TClass> getTClasss()
        throws TorqueException
    {
        if (collTClasss == null)
        {
            collTClasss = getTClasss(new Criteria(10));
        }
        return collTClasss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TClasss from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TClass> getTClasss(Criteria criteria) throws TorqueException
    {
        if (collTClasss == null)
        {
            if (isNew())
            {
               collTClasss = new ArrayList<TClass>();
            }
            else
            {
                criteria.add(TClassPeer.PROJKEY, getObjectID() );
                collTClasss = TClassPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TClassPeer.PROJKEY, getObjectID());
                if (!lastTClasssCriteria.equals(criteria))
                {
                    collTClasss = TClassPeer.doSelect(criteria);
                }
            }
        }
        lastTClasssCriteria = criteria;

        return collTClasss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTClasss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TClass> getTClasss(Connection con) throws TorqueException
    {
        if (collTClasss == null)
        {
            collTClasss = getTClasss(new Criteria(10), con);
        }
        return collTClasss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TClasss from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TClass> getTClasss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTClasss == null)
        {
            if (isNew())
            {
               collTClasss = new ArrayList<TClass>();
            }
            else
            {
                 criteria.add(TClassPeer.PROJKEY, getObjectID());
                 collTClasss = TClassPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TClassPeer.PROJKEY, getObjectID());
                 if (!lastTClasssCriteria.equals(criteria))
                 {
                     collTClasss = TClassPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTClasssCriteria = criteria;

         return collTClasss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TClasss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TClass> getTClasssJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTClasss == null)
        {
            if (isNew())
            {
               collTClasss = new ArrayList<TClass>();
            }
            else
            {
                criteria.add(TClassPeer.PROJKEY, getObjectID());
                collTClasss = TClassPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TClassPeer.PROJKEY, getObjectID());
            if (!lastTClasssCriteria.equals(criteria))
            {
                collTClasss = TClassPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTClasssCriteria = criteria;

        return collTClasss;
    }





    /**
     * Collection to store aggregation of collTProjectCategorys
     */
    protected List<TProjectCategory> collTProjectCategorys;

    /**
     * Temporary storage of collTProjectCategorys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTProjectCategorys()
    {
        if (collTProjectCategorys == null)
        {
            collTProjectCategorys = new ArrayList<TProjectCategory>();
        }
    }


    /**
     * Method called to associate a TProjectCategory object to this object
     * through the TProjectCategory foreign key attribute
     *
     * @param l TProjectCategory
     * @throws TorqueException
     */
    public void addTProjectCategory(TProjectCategory l) throws TorqueException
    {
        getTProjectCategorys().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TProjectCategory object to this object
     * through the TProjectCategory foreign key attribute using connection.
     *
     * @param l TProjectCategory
     * @throws TorqueException
     */
    public void addTProjectCategory(TProjectCategory l, Connection con) throws TorqueException
    {
        getTProjectCategorys(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTProjectCategorys
     */
    private Criteria lastTProjectCategorysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjectCategorys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TProjectCategory> getTProjectCategorys()
        throws TorqueException
    {
        if (collTProjectCategorys == null)
        {
            collTProjectCategorys = getTProjectCategorys(new Criteria(10));
        }
        return collTProjectCategorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TProjectCategorys from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TProjectCategory> getTProjectCategorys(Criteria criteria) throws TorqueException
    {
        if (collTProjectCategorys == null)
        {
            if (isNew())
            {
               collTProjectCategorys = new ArrayList<TProjectCategory>();
            }
            else
            {
                criteria.add(TProjectCategoryPeer.PROJKEY, getObjectID() );
                collTProjectCategorys = TProjectCategoryPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TProjectCategoryPeer.PROJKEY, getObjectID());
                if (!lastTProjectCategorysCriteria.equals(criteria))
                {
                    collTProjectCategorys = TProjectCategoryPeer.doSelect(criteria);
                }
            }
        }
        lastTProjectCategorysCriteria = criteria;

        return collTProjectCategorys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjectCategorys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProjectCategory> getTProjectCategorys(Connection con) throws TorqueException
    {
        if (collTProjectCategorys == null)
        {
            collTProjectCategorys = getTProjectCategorys(new Criteria(10), con);
        }
        return collTProjectCategorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TProjectCategorys from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProjectCategory> getTProjectCategorys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTProjectCategorys == null)
        {
            if (isNew())
            {
               collTProjectCategorys = new ArrayList<TProjectCategory>();
            }
            else
            {
                 criteria.add(TProjectCategoryPeer.PROJKEY, getObjectID());
                 collTProjectCategorys = TProjectCategoryPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TProjectCategoryPeer.PROJKEY, getObjectID());
                 if (!lastTProjectCategorysCriteria.equals(criteria))
                 {
                     collTProjectCategorys = TProjectCategoryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTProjectCategorysCriteria = criteria;

         return collTProjectCategorys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TProjectCategorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TProjectCategory> getTProjectCategorysJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTProjectCategorys == null)
        {
            if (isNew())
            {
               collTProjectCategorys = new ArrayList<TProjectCategory>();
            }
            else
            {
                criteria.add(TProjectCategoryPeer.PROJKEY, getObjectID());
                collTProjectCategorys = TProjectCategoryPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectCategoryPeer.PROJKEY, getObjectID());
            if (!lastTProjectCategorysCriteria.equals(criteria))
            {
                collTProjectCategorys = TProjectCategoryPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTProjectCategorysCriteria = criteria;

        return collTProjectCategorys;
    }








    /**
     * Collection to store aggregation of collTReleases
     */
    protected List<TRelease> collTReleases;

    /**
     * Temporary storage of collTReleases to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTReleases()
    {
        if (collTReleases == null)
        {
            collTReleases = new ArrayList<TRelease>();
        }
    }


    /**
     * Method called to associate a TRelease object to this object
     * through the TRelease foreign key attribute
     *
     * @param l TRelease
     * @throws TorqueException
     */
    public void addTRelease(TRelease l) throws TorqueException
    {
        getTReleases().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TRelease object to this object
     * through the TRelease foreign key attribute using connection.
     *
     * @param l TRelease
     * @throws TorqueException
     */
    public void addTRelease(TRelease l, Connection con) throws TorqueException
    {
        getTReleases(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTReleases
     */
    private Criteria lastTReleasesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReleases(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TRelease> getTReleases()
        throws TorqueException
    {
        if (collTReleases == null)
        {
            collTReleases = getTReleases(new Criteria(10));
        }
        return collTReleases;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TReleases from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TRelease> getTReleases(Criteria criteria) throws TorqueException
    {
        if (collTReleases == null)
        {
            if (isNew())
            {
               collTReleases = new ArrayList<TRelease>();
            }
            else
            {
                criteria.add(TReleasePeer.PROJKEY, getObjectID() );
                collTReleases = TReleasePeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TReleasePeer.PROJKEY, getObjectID());
                if (!lastTReleasesCriteria.equals(criteria))
                {
                    collTReleases = TReleasePeer.doSelect(criteria);
                }
            }
        }
        lastTReleasesCriteria = criteria;

        return collTReleases;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReleases(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRelease> getTReleases(Connection con) throws TorqueException
    {
        if (collTReleases == null)
        {
            collTReleases = getTReleases(new Criteria(10), con);
        }
        return collTReleases;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TReleases from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRelease> getTReleases(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTReleases == null)
        {
            if (isNew())
            {
               collTReleases = new ArrayList<TRelease>();
            }
            else
            {
                 criteria.add(TReleasePeer.PROJKEY, getObjectID());
                 collTReleases = TReleasePeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TReleasePeer.PROJKEY, getObjectID());
                 if (!lastTReleasesCriteria.equals(criteria))
                 {
                     collTReleases = TReleasePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTReleasesCriteria = criteria;

         return collTReleases;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TReleases from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TRelease> getTReleasesJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTReleases == null)
        {
            if (isNew())
            {
               collTReleases = new ArrayList<TRelease>();
            }
            else
            {
                criteria.add(TReleasePeer.PROJKEY, getObjectID());
                collTReleases = TReleasePeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReleasePeer.PROJKEY, getObjectID());
            if (!lastTReleasesCriteria.equals(criteria))
            {
                collTReleases = TReleasePeer.doSelectJoinTProject(criteria);
            }
        }
        lastTReleasesCriteria = criteria;

        return collTReleases;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TReleases from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TRelease> getTReleasesJoinTSystemState(Criteria criteria)
        throws TorqueException
    {
        if (collTReleases == null)
        {
            if (isNew())
            {
               collTReleases = new ArrayList<TRelease>();
            }
            else
            {
                criteria.add(TReleasePeer.PROJKEY, getObjectID());
                collTReleases = TReleasePeer.doSelectJoinTSystemState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReleasePeer.PROJKEY, getObjectID());
            if (!lastTReleasesCriteria.equals(criteria))
            {
                collTReleases = TReleasePeer.doSelectJoinTSystemState(criteria);
            }
        }
        lastTReleasesCriteria = criteria;

        return collTReleases;
    }













    /**
     * Collection to store aggregation of collTWorkItems
     */
    protected List<TWorkItem> collTWorkItems;

    /**
     * Temporary storage of collTWorkItems to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkItems()
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = new ArrayList<TWorkItem>();
        }
    }


    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItem(TWorkItem l) throws TorqueException
    {
        getTWorkItems().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute using connection.
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItem(TWorkItem l, Connection con) throws TorqueException
    {
        getTWorkItems(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkItems
     */
    private Criteria lastTWorkItemsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItems(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItems()
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = getTWorkItems(new Criteria(10));
        }
        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItems(Criteria criteria) throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID() );
                collTWorkItems = TWorkItemPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                if (!lastTWorkItemsCriteria.equals(criteria))
                {
                    collTWorkItems = TWorkItemPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItems(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItems(Connection con) throws TorqueException
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = getTWorkItems(new Criteria(10), con);
        }
        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItems(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                 criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                 collTWorkItems = TWorkItemPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                 if (!lastTWorkItemsCriteria.equals(criteria))
                 {
                     collTWorkItems = TWorkItemPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkItemsCriteria = criteria;

         return collTWorkItems;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByOwnerID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByChangedByID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByOriginatorID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByResponsibleID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTProjectCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTClass(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTSeverity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTReleaseRelatedByReleaseNoticedID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTReleaseRelatedByReleaseScheduledID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkItem> getTWorkItemsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJECTKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }













    /**
     * Collection to store aggregation of collTProjectReportRepositorys
     */
    protected List<TProjectReportRepository> collTProjectReportRepositorys;

    /**
     * Temporary storage of collTProjectReportRepositorys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTProjectReportRepositorys()
    {
        if (collTProjectReportRepositorys == null)
        {
            collTProjectReportRepositorys = new ArrayList<TProjectReportRepository>();
        }
    }


    /**
     * Method called to associate a TProjectReportRepository object to this object
     * through the TProjectReportRepository foreign key attribute
     *
     * @param l TProjectReportRepository
     * @throws TorqueException
     */
    public void addTProjectReportRepository(TProjectReportRepository l) throws TorqueException
    {
        getTProjectReportRepositorys().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TProjectReportRepository object to this object
     * through the TProjectReportRepository foreign key attribute using connection.
     *
     * @param l TProjectReportRepository
     * @throws TorqueException
     */
    public void addTProjectReportRepository(TProjectReportRepository l, Connection con) throws TorqueException
    {
        getTProjectReportRepositorys(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTProjectReportRepositorys
     */
    private Criteria lastTProjectReportRepositorysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjectReportRepositorys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TProjectReportRepository> getTProjectReportRepositorys()
        throws TorqueException
    {
        if (collTProjectReportRepositorys == null)
        {
            collTProjectReportRepositorys = getTProjectReportRepositorys(new Criteria(10));
        }
        return collTProjectReportRepositorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TProjectReportRepositorys from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TProjectReportRepository> getTProjectReportRepositorys(Criteria criteria) throws TorqueException
    {
        if (collTProjectReportRepositorys == null)
        {
            if (isNew())
            {
               collTProjectReportRepositorys = new ArrayList<TProjectReportRepository>();
            }
            else
            {
                criteria.add(TProjectReportRepositoryPeer.PROJECT, getObjectID() );
                collTProjectReportRepositorys = TProjectReportRepositoryPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TProjectReportRepositoryPeer.PROJECT, getObjectID());
                if (!lastTProjectReportRepositorysCriteria.equals(criteria))
                {
                    collTProjectReportRepositorys = TProjectReportRepositoryPeer.doSelect(criteria);
                }
            }
        }
        lastTProjectReportRepositorysCriteria = criteria;

        return collTProjectReportRepositorys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjectReportRepositorys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProjectReportRepository> getTProjectReportRepositorys(Connection con) throws TorqueException
    {
        if (collTProjectReportRepositorys == null)
        {
            collTProjectReportRepositorys = getTProjectReportRepositorys(new Criteria(10), con);
        }
        return collTProjectReportRepositorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TProjectReportRepositorys from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProjectReportRepository> getTProjectReportRepositorys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTProjectReportRepositorys == null)
        {
            if (isNew())
            {
               collTProjectReportRepositorys = new ArrayList<TProjectReportRepository>();
            }
            else
            {
                 criteria.add(TProjectReportRepositoryPeer.PROJECT, getObjectID());
                 collTProjectReportRepositorys = TProjectReportRepositoryPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TProjectReportRepositoryPeer.PROJECT, getObjectID());
                 if (!lastTProjectReportRepositorysCriteria.equals(criteria))
                 {
                     collTProjectReportRepositorys = TProjectReportRepositoryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTProjectReportRepositorysCriteria = criteria;

         return collTProjectReportRepositorys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TProjectReportRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TProjectReportRepository> getTProjectReportRepositorysJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTProjectReportRepositorys == null)
        {
            if (isNew())
            {
               collTProjectReportRepositorys = new ArrayList<TProjectReportRepository>();
            }
            else
            {
                criteria.add(TProjectReportRepositoryPeer.PROJECT, getObjectID());
                collTProjectReportRepositorys = TProjectReportRepositoryPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectReportRepositoryPeer.PROJECT, getObjectID());
            if (!lastTProjectReportRepositorysCriteria.equals(criteria))
            {
                collTProjectReportRepositorys = TProjectReportRepositoryPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTProjectReportRepositorysCriteria = criteria;

        return collTProjectReportRepositorys;
    }





    /**
     * Collection to store aggregation of collTReportLayouts
     */
    protected List<TReportLayout> collTReportLayouts;

    /**
     * Temporary storage of collTReportLayouts to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTReportLayouts()
    {
        if (collTReportLayouts == null)
        {
            collTReportLayouts = new ArrayList<TReportLayout>();
        }
    }


    /**
     * Method called to associate a TReportLayout object to this object
     * through the TReportLayout foreign key attribute
     *
     * @param l TReportLayout
     * @throws TorqueException
     */
    public void addTReportLayout(TReportLayout l) throws TorqueException
    {
        getTReportLayouts().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TReportLayout object to this object
     * through the TReportLayout foreign key attribute using connection.
     *
     * @param l TReportLayout
     * @throws TorqueException
     */
    public void addTReportLayout(TReportLayout l, Connection con) throws TorqueException
    {
        getTReportLayouts(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTReportLayouts
     */
    private Criteria lastTReportLayoutsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportLayouts(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TReportLayout> getTReportLayouts()
        throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            collTReportLayouts = getTReportLayouts(new Criteria(10));
        }
        return collTReportLayouts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TReportLayouts from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TReportLayout> getTReportLayouts(Criteria criteria) throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            if (isNew())
            {
               collTReportLayouts = new ArrayList<TReportLayout>();
            }
            else
            {
                criteria.add(TReportLayoutPeer.PROJECT, getObjectID() );
                collTReportLayouts = TReportLayoutPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TReportLayoutPeer.PROJECT, getObjectID());
                if (!lastTReportLayoutsCriteria.equals(criteria))
                {
                    collTReportLayouts = TReportLayoutPeer.doSelect(criteria);
                }
            }
        }
        lastTReportLayoutsCriteria = criteria;

        return collTReportLayouts;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportLayouts(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportLayout> getTReportLayouts(Connection con) throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            collTReportLayouts = getTReportLayouts(new Criteria(10), con);
        }
        return collTReportLayouts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TReportLayouts from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportLayout> getTReportLayouts(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            if (isNew())
            {
               collTReportLayouts = new ArrayList<TReportLayout>();
            }
            else
            {
                 criteria.add(TReportLayoutPeer.PROJECT, getObjectID());
                 collTReportLayouts = TReportLayoutPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TReportLayoutPeer.PROJECT, getObjectID());
                 if (!lastTReportLayoutsCriteria.equals(criteria))
                 {
                     collTReportLayouts = TReportLayoutPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTReportLayoutsCriteria = criteria;

         return collTReportLayouts;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TReportLayouts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TReportLayout> getTReportLayoutsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            if (isNew())
            {
               collTReportLayouts = new ArrayList<TReportLayout>();
            }
            else
            {
                criteria.add(TReportLayoutPeer.PROJECT, getObjectID());
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportLayoutPeer.PROJECT, getObjectID());
            if (!lastTReportLayoutsCriteria.equals(criteria))
            {
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTReportLayoutsCriteria = criteria;

        return collTReportLayouts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TReportLayouts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TReportLayout> getTReportLayoutsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            if (isNew())
            {
               collTReportLayouts = new ArrayList<TReportLayout>();
            }
            else
            {
                criteria.add(TReportLayoutPeer.PROJECT, getObjectID());
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportLayoutPeer.PROJECT, getObjectID());
            if (!lastTReportLayoutsCriteria.equals(criteria))
            {
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTReportLayoutsCriteria = criteria;

        return collTReportLayouts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TReportLayouts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TReportLayout> getTReportLayoutsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            if (isNew())
            {
               collTReportLayouts = new ArrayList<TReportLayout>();
            }
            else
            {
                criteria.add(TReportLayoutPeer.PROJECT, getObjectID());
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportLayoutPeer.PROJECT, getObjectID());
            if (!lastTReportLayoutsCriteria.equals(criteria))
            {
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTReportLayoutsCriteria = criteria;

        return collTReportLayouts;
    }





    /**
     * Collection to store aggregation of collTProjectAccounts
     */
    protected List<TProjectAccount> collTProjectAccounts;

    /**
     * Temporary storage of collTProjectAccounts to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTProjectAccounts()
    {
        if (collTProjectAccounts == null)
        {
            collTProjectAccounts = new ArrayList<TProjectAccount>();
        }
    }


    /**
     * Method called to associate a TProjectAccount object to this object
     * through the TProjectAccount foreign key attribute
     *
     * @param l TProjectAccount
     * @throws TorqueException
     */
    public void addTProjectAccount(TProjectAccount l) throws TorqueException
    {
        getTProjectAccounts().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TProjectAccount object to this object
     * through the TProjectAccount foreign key attribute using connection.
     *
     * @param l TProjectAccount
     * @throws TorqueException
     */
    public void addTProjectAccount(TProjectAccount l, Connection con) throws TorqueException
    {
        getTProjectAccounts(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTProjectAccounts
     */
    private Criteria lastTProjectAccountsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjectAccounts(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TProjectAccount> getTProjectAccounts()
        throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            collTProjectAccounts = getTProjectAccounts(new Criteria(10));
        }
        return collTProjectAccounts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TProjectAccounts from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TProjectAccount> getTProjectAccounts(Criteria criteria) throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            if (isNew())
            {
               collTProjectAccounts = new ArrayList<TProjectAccount>();
            }
            else
            {
                criteria.add(TProjectAccountPeer.PROJECT, getObjectID() );
                collTProjectAccounts = TProjectAccountPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TProjectAccountPeer.PROJECT, getObjectID());
                if (!lastTProjectAccountsCriteria.equals(criteria))
                {
                    collTProjectAccounts = TProjectAccountPeer.doSelect(criteria);
                }
            }
        }
        lastTProjectAccountsCriteria = criteria;

        return collTProjectAccounts;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjectAccounts(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProjectAccount> getTProjectAccounts(Connection con) throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            collTProjectAccounts = getTProjectAccounts(new Criteria(10), con);
        }
        return collTProjectAccounts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TProjectAccounts from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProjectAccount> getTProjectAccounts(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            if (isNew())
            {
               collTProjectAccounts = new ArrayList<TProjectAccount>();
            }
            else
            {
                 criteria.add(TProjectAccountPeer.PROJECT, getObjectID());
                 collTProjectAccounts = TProjectAccountPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TProjectAccountPeer.PROJECT, getObjectID());
                 if (!lastTProjectAccountsCriteria.equals(criteria))
                 {
                     collTProjectAccounts = TProjectAccountPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTProjectAccountsCriteria = criteria;

         return collTProjectAccounts;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TProjectAccounts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TProjectAccount> getTProjectAccountsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            if (isNew())
            {
               collTProjectAccounts = new ArrayList<TProjectAccount>();
            }
            else
            {
                criteria.add(TProjectAccountPeer.PROJECT, getObjectID());
                collTProjectAccounts = TProjectAccountPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectAccountPeer.PROJECT, getObjectID());
            if (!lastTProjectAccountsCriteria.equals(criteria))
            {
                collTProjectAccounts = TProjectAccountPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTProjectAccountsCriteria = criteria;

        return collTProjectAccounts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TProjectAccounts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TProjectAccount> getTProjectAccountsJoinTAccount(Criteria criteria)
        throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            if (isNew())
            {
               collTProjectAccounts = new ArrayList<TProjectAccount>();
            }
            else
            {
                criteria.add(TProjectAccountPeer.PROJECT, getObjectID());
                collTProjectAccounts = TProjectAccountPeer.doSelectJoinTAccount(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectAccountPeer.PROJECT, getObjectID());
            if (!lastTProjectAccountsCriteria.equals(criteria))
            {
                collTProjectAccounts = TProjectAccountPeer.doSelectJoinTAccount(criteria);
            }
        }
        lastTProjectAccountsCriteria = criteria;

        return collTProjectAccounts;
    }





    /**
     * Collection to store aggregation of collTDashboardScreens
     */
    protected List<TDashboardScreen> collTDashboardScreens;

    /**
     * Temporary storage of collTDashboardScreens to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTDashboardScreens()
    {
        if (collTDashboardScreens == null)
        {
            collTDashboardScreens = new ArrayList<TDashboardScreen>();
        }
    }


    /**
     * Method called to associate a TDashboardScreen object to this object
     * through the TDashboardScreen foreign key attribute
     *
     * @param l TDashboardScreen
     * @throws TorqueException
     */
    public void addTDashboardScreen(TDashboardScreen l) throws TorqueException
    {
        getTDashboardScreens().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TDashboardScreen object to this object
     * through the TDashboardScreen foreign key attribute using connection.
     *
     * @param l TDashboardScreen
     * @throws TorqueException
     */
    public void addTDashboardScreen(TDashboardScreen l, Connection con) throws TorqueException
    {
        getTDashboardScreens(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTDashboardScreens
     */
    private Criteria lastTDashboardScreensCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDashboardScreens(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TDashboardScreen> getTDashboardScreens()
        throws TorqueException
    {
        if (collTDashboardScreens == null)
        {
            collTDashboardScreens = getTDashboardScreens(new Criteria(10));
        }
        return collTDashboardScreens;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TDashboardScreens from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TDashboardScreen> getTDashboardScreens(Criteria criteria) throws TorqueException
    {
        if (collTDashboardScreens == null)
        {
            if (isNew())
            {
               collTDashboardScreens = new ArrayList<TDashboardScreen>();
            }
            else
            {
                criteria.add(TDashboardScreenPeer.PROJECT, getObjectID() );
                collTDashboardScreens = TDashboardScreenPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TDashboardScreenPeer.PROJECT, getObjectID());
                if (!lastTDashboardScreensCriteria.equals(criteria))
                {
                    collTDashboardScreens = TDashboardScreenPeer.doSelect(criteria);
                }
            }
        }
        lastTDashboardScreensCriteria = criteria;

        return collTDashboardScreens;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDashboardScreens(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDashboardScreen> getTDashboardScreens(Connection con) throws TorqueException
    {
        if (collTDashboardScreens == null)
        {
            collTDashboardScreens = getTDashboardScreens(new Criteria(10), con);
        }
        return collTDashboardScreens;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TDashboardScreens from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDashboardScreen> getTDashboardScreens(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTDashboardScreens == null)
        {
            if (isNew())
            {
               collTDashboardScreens = new ArrayList<TDashboardScreen>();
            }
            else
            {
                 criteria.add(TDashboardScreenPeer.PROJECT, getObjectID());
                 collTDashboardScreens = TDashboardScreenPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TDashboardScreenPeer.PROJECT, getObjectID());
                 if (!lastTDashboardScreensCriteria.equals(criteria))
                 {
                     collTDashboardScreens = TDashboardScreenPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTDashboardScreensCriteria = criteria;

         return collTDashboardScreens;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TDashboardScreens from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TDashboardScreen> getTDashboardScreensJoinTPersonRelatedByPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTDashboardScreens == null)
        {
            if (isNew())
            {
               collTDashboardScreens = new ArrayList<TDashboardScreen>();
            }
            else
            {
                criteria.add(TDashboardScreenPeer.PROJECT, getObjectID());
                collTDashboardScreens = TDashboardScreenPeer.doSelectJoinTPersonRelatedByPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDashboardScreenPeer.PROJECT, getObjectID());
            if (!lastTDashboardScreensCriteria.equals(criteria))
            {
                collTDashboardScreens = TDashboardScreenPeer.doSelectJoinTPersonRelatedByPerson(criteria);
            }
        }
        lastTDashboardScreensCriteria = criteria;

        return collTDashboardScreens;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TDashboardScreens from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TDashboardScreen> getTDashboardScreensJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTDashboardScreens == null)
        {
            if (isNew())
            {
               collTDashboardScreens = new ArrayList<TDashboardScreen>();
            }
            else
            {
                criteria.add(TDashboardScreenPeer.PROJECT, getObjectID());
                collTDashboardScreens = TDashboardScreenPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDashboardScreenPeer.PROJECT, getObjectID());
            if (!lastTDashboardScreensCriteria.equals(criteria))
            {
                collTDashboardScreens = TDashboardScreenPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTDashboardScreensCriteria = criteria;

        return collTDashboardScreens;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TDashboardScreens from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TDashboardScreen> getTDashboardScreensJoinTPersonRelatedByOwner(Criteria criteria)
        throws TorqueException
    {
        if (collTDashboardScreens == null)
        {
            if (isNew())
            {
               collTDashboardScreens = new ArrayList<TDashboardScreen>();
            }
            else
            {
                criteria.add(TDashboardScreenPeer.PROJECT, getObjectID());
                collTDashboardScreens = TDashboardScreenPeer.doSelectJoinTPersonRelatedByOwner(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDashboardScreenPeer.PROJECT, getObjectID());
            if (!lastTDashboardScreensCriteria.equals(criteria))
            {
                collTDashboardScreens = TDashboardScreenPeer.doSelectJoinTPersonRelatedByOwner(criteria);
            }
        }
        lastTDashboardScreensCriteria = criteria;

        return collTDashboardScreens;
    }





    /**
     * Collection to store aggregation of collTVersionControlParameters
     */
    protected List<TVersionControlParameter> collTVersionControlParameters;

    /**
     * Temporary storage of collTVersionControlParameters to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTVersionControlParameters()
    {
        if (collTVersionControlParameters == null)
        {
            collTVersionControlParameters = new ArrayList<TVersionControlParameter>();
        }
    }


    /**
     * Method called to associate a TVersionControlParameter object to this object
     * through the TVersionControlParameter foreign key attribute
     *
     * @param l TVersionControlParameter
     * @throws TorqueException
     */
    public void addTVersionControlParameter(TVersionControlParameter l) throws TorqueException
    {
        getTVersionControlParameters().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TVersionControlParameter object to this object
     * through the TVersionControlParameter foreign key attribute using connection.
     *
     * @param l TVersionControlParameter
     * @throws TorqueException
     */
    public void addTVersionControlParameter(TVersionControlParameter l, Connection con) throws TorqueException
    {
        getTVersionControlParameters(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTVersionControlParameters
     */
    private Criteria lastTVersionControlParametersCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTVersionControlParameters(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TVersionControlParameter> getTVersionControlParameters()
        throws TorqueException
    {
        if (collTVersionControlParameters == null)
        {
            collTVersionControlParameters = getTVersionControlParameters(new Criteria(10));
        }
        return collTVersionControlParameters;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TVersionControlParameters from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TVersionControlParameter> getTVersionControlParameters(Criteria criteria) throws TorqueException
    {
        if (collTVersionControlParameters == null)
        {
            if (isNew())
            {
               collTVersionControlParameters = new ArrayList<TVersionControlParameter>();
            }
            else
            {
                criteria.add(TVersionControlParameterPeer.PROJECT, getObjectID() );
                collTVersionControlParameters = TVersionControlParameterPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TVersionControlParameterPeer.PROJECT, getObjectID());
                if (!lastTVersionControlParametersCriteria.equals(criteria))
                {
                    collTVersionControlParameters = TVersionControlParameterPeer.doSelect(criteria);
                }
            }
        }
        lastTVersionControlParametersCriteria = criteria;

        return collTVersionControlParameters;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTVersionControlParameters(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TVersionControlParameter> getTVersionControlParameters(Connection con) throws TorqueException
    {
        if (collTVersionControlParameters == null)
        {
            collTVersionControlParameters = getTVersionControlParameters(new Criteria(10), con);
        }
        return collTVersionControlParameters;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TVersionControlParameters from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TVersionControlParameter> getTVersionControlParameters(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTVersionControlParameters == null)
        {
            if (isNew())
            {
               collTVersionControlParameters = new ArrayList<TVersionControlParameter>();
            }
            else
            {
                 criteria.add(TVersionControlParameterPeer.PROJECT, getObjectID());
                 collTVersionControlParameters = TVersionControlParameterPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TVersionControlParameterPeer.PROJECT, getObjectID());
                 if (!lastTVersionControlParametersCriteria.equals(criteria))
                 {
                     collTVersionControlParameters = TVersionControlParameterPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTVersionControlParametersCriteria = criteria;

         return collTVersionControlParameters;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TVersionControlParameters from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TVersionControlParameter> getTVersionControlParametersJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTVersionControlParameters == null)
        {
            if (isNew())
            {
               collTVersionControlParameters = new ArrayList<TVersionControlParameter>();
            }
            else
            {
                criteria.add(TVersionControlParameterPeer.PROJECT, getObjectID());
                collTVersionControlParameters = TVersionControlParameterPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TVersionControlParameterPeer.PROJECT, getObjectID());
            if (!lastTVersionControlParametersCriteria.equals(criteria))
            {
                collTVersionControlParameters = TVersionControlParameterPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTVersionControlParametersCriteria = criteria;

        return collTVersionControlParameters;
    }





    /**
     * Collection to store aggregation of collTFields
     */
    protected List<TField> collTFields;

    /**
     * Temporary storage of collTFields to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTFields()
    {
        if (collTFields == null)
        {
            collTFields = new ArrayList<TField>();
        }
    }


    /**
     * Method called to associate a TField object to this object
     * through the TField foreign key attribute
     *
     * @param l TField
     * @throws TorqueException
     */
    public void addTField(TField l) throws TorqueException
    {
        getTFields().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TField object to this object
     * through the TField foreign key attribute using connection.
     *
     * @param l TField
     * @throws TorqueException
     */
    public void addTField(TField l, Connection con) throws TorqueException
    {
        getTFields(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTFields
     */
    private Criteria lastTFieldsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFields(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TField> getTFields()
        throws TorqueException
    {
        if (collTFields == null)
        {
            collTFields = getTFields(new Criteria(10));
        }
        return collTFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TFields from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TField> getTFields(Criteria criteria) throws TorqueException
    {
        if (collTFields == null)
        {
            if (isNew())
            {
               collTFields = new ArrayList<TField>();
            }
            else
            {
                criteria.add(TFieldPeer.PROJECT, getObjectID() );
                collTFields = TFieldPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TFieldPeer.PROJECT, getObjectID());
                if (!lastTFieldsCriteria.equals(criteria))
                {
                    collTFields = TFieldPeer.doSelect(criteria);
                }
            }
        }
        lastTFieldsCriteria = criteria;

        return collTFields;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFields(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TField> getTFields(Connection con) throws TorqueException
    {
        if (collTFields == null)
        {
            collTFields = getTFields(new Criteria(10), con);
        }
        return collTFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TFields from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TField> getTFields(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTFields == null)
        {
            if (isNew())
            {
               collTFields = new ArrayList<TField>();
            }
            else
            {
                 criteria.add(TFieldPeer.PROJECT, getObjectID());
                 collTFields = TFieldPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TFieldPeer.PROJECT, getObjectID());
                 if (!lastTFieldsCriteria.equals(criteria))
                 {
                     collTFields = TFieldPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTFieldsCriteria = criteria;

         return collTFields;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TField> getTFieldsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTFields == null)
        {
            if (isNew())
            {
               collTFields = new ArrayList<TField>();
            }
            else
            {
                criteria.add(TFieldPeer.PROJECT, getObjectID());
                collTFields = TFieldPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldPeer.PROJECT, getObjectID());
            if (!lastTFieldsCriteria.equals(criteria))
            {
                collTFields = TFieldPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTFieldsCriteria = criteria;

        return collTFields;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TField> getTFieldsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTFields == null)
        {
            if (isNew())
            {
               collTFields = new ArrayList<TField>();
            }
            else
            {
                criteria.add(TFieldPeer.PROJECT, getObjectID());
                collTFields = TFieldPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldPeer.PROJECT, getObjectID());
            if (!lastTFieldsCriteria.equals(criteria))
            {
                collTFields = TFieldPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTFieldsCriteria = criteria;

        return collTFields;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TField> getTFieldsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTFields == null)
        {
            if (isNew())
            {
               collTFields = new ArrayList<TField>();
            }
            else
            {
                criteria.add(TFieldPeer.PROJECT, getObjectID());
                collTFields = TFieldPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldPeer.PROJECT, getObjectID());
            if (!lastTFieldsCriteria.equals(criteria))
            {
                collTFields = TFieldPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTFieldsCriteria = criteria;

        return collTFields;
    }





    /**
     * Collection to store aggregation of collTFieldConfigs
     */
    protected List<TFieldConfig> collTFieldConfigs;

    /**
     * Temporary storage of collTFieldConfigs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTFieldConfigs()
    {
        if (collTFieldConfigs == null)
        {
            collTFieldConfigs = new ArrayList<TFieldConfig>();
        }
    }


    /**
     * Method called to associate a TFieldConfig object to this object
     * through the TFieldConfig foreign key attribute
     *
     * @param l TFieldConfig
     * @throws TorqueException
     */
    public void addTFieldConfig(TFieldConfig l) throws TorqueException
    {
        getTFieldConfigs().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TFieldConfig object to this object
     * through the TFieldConfig foreign key attribute using connection.
     *
     * @param l TFieldConfig
     * @throws TorqueException
     */
    public void addTFieldConfig(TFieldConfig l, Connection con) throws TorqueException
    {
        getTFieldConfigs(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTFieldConfigs
     */
    private Criteria lastTFieldConfigsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldConfigs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TFieldConfig> getTFieldConfigs()
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            collTFieldConfigs = getTFieldConfigs(new Criteria(10));
        }
        return collTFieldConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TFieldConfig> getTFieldConfigs(Criteria criteria) throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.PROJECT, getObjectID() );
                collTFieldConfigs = TFieldConfigPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
                if (!lastTFieldConfigsCriteria.equals(criteria))
                {
                    collTFieldConfigs = TFieldConfigPeer.doSelect(criteria);
                }
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldConfigs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldConfig> getTFieldConfigs(Connection con) throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            collTFieldConfigs = getTFieldConfigs(new Criteria(10), con);
        }
        return collTFieldConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldConfig> getTFieldConfigs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                 criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
                 collTFieldConfigs = TFieldConfigPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
                 if (!lastTFieldConfigsCriteria.equals(criteria))
                 {
                     collTFieldConfigs = TFieldConfigPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTFieldConfigsCriteria = criteria;

         return collTFieldConfigs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTField(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTField(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.PROJECT, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }





    /**
     * Collection to store aggregation of collTLists
     */
    protected List<TList> collTLists;

    /**
     * Temporary storage of collTLists to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTLists()
    {
        if (collTLists == null)
        {
            collTLists = new ArrayList<TList>();
        }
    }


    /**
     * Method called to associate a TList object to this object
     * through the TList foreign key attribute
     *
     * @param l TList
     * @throws TorqueException
     */
    public void addTList(TList l) throws TorqueException
    {
        getTLists().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TList object to this object
     * through the TList foreign key attribute using connection.
     *
     * @param l TList
     * @throws TorqueException
     */
    public void addTList(TList l, Connection con) throws TorqueException
    {
        getTLists(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTLists
     */
    private Criteria lastTListsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLists(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TList> getTLists()
        throws TorqueException
    {
        if (collTLists == null)
        {
            collTLists = getTLists(new Criteria(10));
        }
        return collTLists;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TLists from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TList> getTLists(Criteria criteria) throws TorqueException
    {
        if (collTLists == null)
        {
            if (isNew())
            {
               collTLists = new ArrayList<TList>();
            }
            else
            {
                criteria.add(TListPeer.PROJECT, getObjectID() );
                collTLists = TListPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TListPeer.PROJECT, getObjectID());
                if (!lastTListsCriteria.equals(criteria))
                {
                    collTLists = TListPeer.doSelect(criteria);
                }
            }
        }
        lastTListsCriteria = criteria;

        return collTLists;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLists(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TList> getTLists(Connection con) throws TorqueException
    {
        if (collTLists == null)
        {
            collTLists = getTLists(new Criteria(10), con);
        }
        return collTLists;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TLists from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TList> getTLists(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTLists == null)
        {
            if (isNew())
            {
               collTLists = new ArrayList<TList>();
            }
            else
            {
                 criteria.add(TListPeer.PROJECT, getObjectID());
                 collTLists = TListPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TListPeer.PROJECT, getObjectID());
                 if (!lastTListsCriteria.equals(criteria))
                 {
                     collTLists = TListPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTListsCriteria = criteria;

         return collTLists;
     }



















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TLists from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TList> getTListsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTLists == null)
        {
            if (isNew())
            {
               collTLists = new ArrayList<TList>();
            }
            else
            {
                criteria.add(TListPeer.PROJECT, getObjectID());
                collTLists = TListPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TListPeer.PROJECT, getObjectID());
            if (!lastTListsCriteria.equals(criteria))
            {
                collTLists = TListPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTListsCriteria = criteria;

        return collTLists;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TLists from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TList> getTListsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTLists == null)
        {
            if (isNew())
            {
               collTLists = new ArrayList<TList>();
            }
            else
            {
                criteria.add(TListPeer.PROJECT, getObjectID());
                collTLists = TListPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TListPeer.PROJECT, getObjectID());
            if (!lastTListsCriteria.equals(criteria))
            {
                collTLists = TListPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTListsCriteria = criteria;

        return collTLists;
    }





    /**
     * Collection to store aggregation of collTScreens
     */
    protected List<TScreen> collTScreens;

    /**
     * Temporary storage of collTScreens to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScreens()
    {
        if (collTScreens == null)
        {
            collTScreens = new ArrayList<TScreen>();
        }
    }


    /**
     * Method called to associate a TScreen object to this object
     * through the TScreen foreign key attribute
     *
     * @param l TScreen
     * @throws TorqueException
     */
    public void addTScreen(TScreen l) throws TorqueException
    {
        getTScreens().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TScreen object to this object
     * through the TScreen foreign key attribute using connection.
     *
     * @param l TScreen
     * @throws TorqueException
     */
    public void addTScreen(TScreen l, Connection con) throws TorqueException
    {
        getTScreens(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTScreens
     */
    private Criteria lastTScreensCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreens(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScreen> getTScreens()
        throws TorqueException
    {
        if (collTScreens == null)
        {
            collTScreens = getTScreens(new Criteria(10));
        }
        return collTScreens;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TScreens from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScreen> getTScreens(Criteria criteria) throws TorqueException
    {
        if (collTScreens == null)
        {
            if (isNew())
            {
               collTScreens = new ArrayList<TScreen>();
            }
            else
            {
                criteria.add(TScreenPeer.PROJECT, getObjectID() );
                collTScreens = TScreenPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TScreenPeer.PROJECT, getObjectID());
                if (!lastTScreensCriteria.equals(criteria))
                {
                    collTScreens = TScreenPeer.doSelect(criteria);
                }
            }
        }
        lastTScreensCriteria = criteria;

        return collTScreens;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreens(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreen> getTScreens(Connection con) throws TorqueException
    {
        if (collTScreens == null)
        {
            collTScreens = getTScreens(new Criteria(10), con);
        }
        return collTScreens;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TScreens from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreen> getTScreens(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScreens == null)
        {
            if (isNew())
            {
               collTScreens = new ArrayList<TScreen>();
            }
            else
            {
                 criteria.add(TScreenPeer.PROJECT, getObjectID());
                 collTScreens = TScreenPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TScreenPeer.PROJECT, getObjectID());
                 if (!lastTScreensCriteria.equals(criteria))
                 {
                     collTScreens = TScreenPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScreensCriteria = criteria;

         return collTScreens;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScreens from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScreen> getTScreensJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTScreens == null)
        {
            if (isNew())
            {
               collTScreens = new ArrayList<TScreen>();
            }
            else
            {
                criteria.add(TScreenPeer.PROJECT, getObjectID());
                collTScreens = TScreenPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenPeer.PROJECT, getObjectID());
            if (!lastTScreensCriteria.equals(criteria))
            {
                collTScreens = TScreenPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTScreensCriteria = criteria;

        return collTScreens;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScreens from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScreen> getTScreensJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTScreens == null)
        {
            if (isNew())
            {
               collTScreens = new ArrayList<TScreen>();
            }
            else
            {
                criteria.add(TScreenPeer.PROJECT, getObjectID());
                collTScreens = TScreenPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenPeer.PROJECT, getObjectID());
            if (!lastTScreensCriteria.equals(criteria))
            {
                collTScreens = TScreenPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTScreensCriteria = criteria;

        return collTScreens;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScreens from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScreen> getTScreensJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTScreens == null)
        {
            if (isNew())
            {
               collTScreens = new ArrayList<TScreen>();
            }
            else
            {
                criteria.add(TScreenPeer.PROJECT, getObjectID());
                collTScreens = TScreenPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenPeer.PROJECT, getObjectID());
            if (!lastTScreensCriteria.equals(criteria))
            {
                collTScreens = TScreenPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTScreensCriteria = criteria;

        return collTScreens;
    }





    /**
     * Collection to store aggregation of collTScreenConfigs
     */
    protected List<TScreenConfig> collTScreenConfigs;

    /**
     * Temporary storage of collTScreenConfigs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScreenConfigs()
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = new ArrayList<TScreenConfig>();
        }
    }


    /**
     * Method called to associate a TScreenConfig object to this object
     * through the TScreenConfig foreign key attribute
     *
     * @param l TScreenConfig
     * @throws TorqueException
     */
    public void addTScreenConfig(TScreenConfig l) throws TorqueException
    {
        getTScreenConfigs().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TScreenConfig object to this object
     * through the TScreenConfig foreign key attribute using connection.
     *
     * @param l TScreenConfig
     * @throws TorqueException
     */
    public void addTScreenConfig(TScreenConfig l, Connection con) throws TorqueException
    {
        getTScreenConfigs(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTScreenConfigs
     */
    private Criteria lastTScreenConfigsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenConfigs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScreenConfig> getTScreenConfigs()
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = getTScreenConfigs(new Criteria(10));
        }
        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScreenConfig> getTScreenConfigs(Criteria criteria) throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.PROJECT, getObjectID() );
                collTScreenConfigs = TScreenConfigPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
                if (!lastTScreenConfigsCriteria.equals(criteria))
                {
                    collTScreenConfigs = TScreenConfigPeer.doSelect(criteria);
                }
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenConfigs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenConfig> getTScreenConfigs(Connection con) throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = getTScreenConfigs(new Criteria(10), con);
        }
        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenConfig> getTScreenConfigs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                 criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
                 collTScreenConfigs = TScreenConfigPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
                 if (!lastTScreenConfigsCriteria.equals(criteria))
                 {
                     collTScreenConfigs = TScreenConfigPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScreenConfigsCriteria = criteria;

         return collTScreenConfigs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTAction(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTAction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.PROJECT, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTAction(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }





    /**
     * Collection to store aggregation of collTInitStates
     */
    protected List<TInitState> collTInitStates;

    /**
     * Temporary storage of collTInitStates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTInitStates()
    {
        if (collTInitStates == null)
        {
            collTInitStates = new ArrayList<TInitState>();
        }
    }


    /**
     * Method called to associate a TInitState object to this object
     * through the TInitState foreign key attribute
     *
     * @param l TInitState
     * @throws TorqueException
     */
    public void addTInitState(TInitState l) throws TorqueException
    {
        getTInitStates().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TInitState object to this object
     * through the TInitState foreign key attribute using connection.
     *
     * @param l TInitState
     * @throws TorqueException
     */
    public void addTInitState(TInitState l, Connection con) throws TorqueException
    {
        getTInitStates(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTInitStates
     */
    private Criteria lastTInitStatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTInitStates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TInitState> getTInitStates()
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            collTInitStates = getTInitStates(new Criteria(10));
        }
        return collTInitStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TInitStates from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TInitState> getTInitStates(Criteria criteria) throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.PROJECT, getObjectID() );
                collTInitStates = TInitStatePeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TInitStatePeer.PROJECT, getObjectID());
                if (!lastTInitStatesCriteria.equals(criteria))
                {
                    collTInitStates = TInitStatePeer.doSelect(criteria);
                }
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTInitStates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TInitState> getTInitStates(Connection con) throws TorqueException
    {
        if (collTInitStates == null)
        {
            collTInitStates = getTInitStates(new Criteria(10), con);
        }
        return collTInitStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TInitStates from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TInitState> getTInitStates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                 criteria.add(TInitStatePeer.PROJECT, getObjectID());
                 collTInitStates = TInitStatePeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TInitStatePeer.PROJECT, getObjectID());
                 if (!lastTInitStatesCriteria.equals(criteria))
                 {
                     collTInitStates = TInitStatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTInitStatesCriteria = criteria;

         return collTInitStates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TInitStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TInitState> getTInitStatesJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.PROJECT, getObjectID());
                collTInitStates = TInitStatePeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TInitStatePeer.PROJECT, getObjectID());
            if (!lastTInitStatesCriteria.equals(criteria))
            {
                collTInitStates = TInitStatePeer.doSelectJoinTProject(criteria);
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TInitStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TInitState> getTInitStatesJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.PROJECT, getObjectID());
                collTInitStates = TInitStatePeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TInitStatePeer.PROJECT, getObjectID());
            if (!lastTInitStatesCriteria.equals(criteria))
            {
                collTInitStates = TInitStatePeer.doSelectJoinTListType(criteria);
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TInitStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TInitState> getTInitStatesJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.PROJECT, getObjectID());
                collTInitStates = TInitStatePeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TInitStatePeer.PROJECT, getObjectID());
            if (!lastTInitStatesCriteria.equals(criteria))
            {
                collTInitStates = TInitStatePeer.doSelectJoinTState(criteria);
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }





    /**
     * Collection to store aggregation of collTEvents
     */
    protected List<TEvent> collTEvents;

    /**
     * Temporary storage of collTEvents to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTEvents()
    {
        if (collTEvents == null)
        {
            collTEvents = new ArrayList<TEvent>();
        }
    }


    /**
     * Method called to associate a TEvent object to this object
     * through the TEvent foreign key attribute
     *
     * @param l TEvent
     * @throws TorqueException
     */
    public void addTEvent(TEvent l) throws TorqueException
    {
        getTEvents().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TEvent object to this object
     * through the TEvent foreign key attribute using connection.
     *
     * @param l TEvent
     * @throws TorqueException
     */
    public void addTEvent(TEvent l, Connection con) throws TorqueException
    {
        getTEvents(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTEvents
     */
    private Criteria lastTEventsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEvents(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TEvent> getTEvents()
        throws TorqueException
    {
        if (collTEvents == null)
        {
            collTEvents = getTEvents(new Criteria(10));
        }
        return collTEvents;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TEvents from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TEvent> getTEvents(Criteria criteria) throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.PROJECT, getObjectID() );
                collTEvents = TEventPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TEventPeer.PROJECT, getObjectID());
                if (!lastTEventsCriteria.equals(criteria))
                {
                    collTEvents = TEventPeer.doSelect(criteria);
                }
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEvents(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEvent> getTEvents(Connection con) throws TorqueException
    {
        if (collTEvents == null)
        {
            collTEvents = getTEvents(new Criteria(10), con);
        }
        return collTEvents;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TEvents from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEvent> getTEvents(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                 criteria.add(TEventPeer.PROJECT, getObjectID());
                 collTEvents = TEventPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TEventPeer.PROJECT, getObjectID());
                 if (!lastTEventsCriteria.equals(criteria))
                 {
                     collTEvents = TEventPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTEventsCriteria = criteria;

         return collTEvents;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TEvents from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TEvent> getTEventsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.PROJECT, getObjectID());
                collTEvents = TEventPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEventPeer.PROJECT, getObjectID());
            if (!lastTEventsCriteria.equals(criteria))
            {
                collTEvents = TEventPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TEvents from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TEvent> getTEventsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.PROJECT, getObjectID());
                collTEvents = TEventPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEventPeer.PROJECT, getObjectID());
            if (!lastTEventsCriteria.equals(criteria))
            {
                collTEvents = TEventPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TEvents from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TEvent> getTEventsJoinTCLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.PROJECT, getObjectID());
                collTEvents = TEventPeer.doSelectJoinTCLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEventPeer.PROJECT, getObjectID());
            if (!lastTEventsCriteria.equals(criteria))
            {
                collTEvents = TEventPeer.doSelectJoinTCLOB(criteria);
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }





    /**
     * Collection to store aggregation of collTNotifySettingss
     */
    protected List<TNotifySettings> collTNotifySettingss;

    /**
     * Temporary storage of collTNotifySettingss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTNotifySettingss()
    {
        if (collTNotifySettingss == null)
        {
            collTNotifySettingss = new ArrayList<TNotifySettings>();
        }
    }


    /**
     * Method called to associate a TNotifySettings object to this object
     * through the TNotifySettings foreign key attribute
     *
     * @param l TNotifySettings
     * @throws TorqueException
     */
    public void addTNotifySettings(TNotifySettings l) throws TorqueException
    {
        getTNotifySettingss().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TNotifySettings object to this object
     * through the TNotifySettings foreign key attribute using connection.
     *
     * @param l TNotifySettings
     * @throws TorqueException
     */
    public void addTNotifySettings(TNotifySettings l, Connection con) throws TorqueException
    {
        getTNotifySettingss(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTNotifySettingss
     */
    private Criteria lastTNotifySettingssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNotifySettingss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TNotifySettings> getTNotifySettingss()
        throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            collTNotifySettingss = getTNotifySettingss(new Criteria(10));
        }
        return collTNotifySettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TNotifySettings> getTNotifySettingss(Criteria criteria) throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                criteria.add(TNotifySettingsPeer.PROJECT, getObjectID() );
                collTNotifySettingss = TNotifySettingsPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
                if (!lastTNotifySettingssCriteria.equals(criteria))
                {
                    collTNotifySettingss = TNotifySettingsPeer.doSelect(criteria);
                }
            }
        }
        lastTNotifySettingssCriteria = criteria;

        return collTNotifySettingss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNotifySettingss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNotifySettings> getTNotifySettingss(Connection con) throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            collTNotifySettingss = getTNotifySettingss(new Criteria(10), con);
        }
        return collTNotifySettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNotifySettings> getTNotifySettingss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                 criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
                 collTNotifySettingss = TNotifySettingsPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
                 if (!lastTNotifySettingssCriteria.equals(criteria))
                 {
                     collTNotifySettingss = TNotifySettingsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTNotifySettingssCriteria = criteria;

         return collTNotifySettingss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TNotifySettings> getTNotifySettingssJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
            if (!lastTNotifySettingssCriteria.equals(criteria))
            {
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTNotifySettingssCriteria = criteria;

        return collTNotifySettingss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TNotifySettings> getTNotifySettingssJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
            if (!lastTNotifySettingssCriteria.equals(criteria))
            {
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTNotifySettingssCriteria = criteria;

        return collTNotifySettingss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TNotifySettings> getTNotifySettingssJoinTNotifyTrigger(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTNotifyTrigger(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
            if (!lastTNotifySettingssCriteria.equals(criteria))
            {
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTNotifyTrigger(criteria);
            }
        }
        lastTNotifySettingssCriteria = criteria;

        return collTNotifySettingss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TNotifySettings> getTNotifySettingssJoinTQueryRepository(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTQueryRepository(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifySettingsPeer.PROJECT, getObjectID());
            if (!lastTNotifySettingssCriteria.equals(criteria))
            {
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTQueryRepository(criteria);
            }
        }
        lastTNotifySettingssCriteria = criteria;

        return collTNotifySettingss;
    }





    /**
     * Collection to store aggregation of collTQueryRepositorys
     */
    protected List<TQueryRepository> collTQueryRepositorys;

    /**
     * Temporary storage of collTQueryRepositorys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTQueryRepositorys()
    {
        if (collTQueryRepositorys == null)
        {
            collTQueryRepositorys = new ArrayList<TQueryRepository>();
        }
    }


    /**
     * Method called to associate a TQueryRepository object to this object
     * through the TQueryRepository foreign key attribute
     *
     * @param l TQueryRepository
     * @throws TorqueException
     */
    public void addTQueryRepository(TQueryRepository l) throws TorqueException
    {
        getTQueryRepositorys().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TQueryRepository object to this object
     * through the TQueryRepository foreign key attribute using connection.
     *
     * @param l TQueryRepository
     * @throws TorqueException
     */
    public void addTQueryRepository(TQueryRepository l, Connection con) throws TorqueException
    {
        getTQueryRepositorys(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTQueryRepositorys
     */
    private Criteria lastTQueryRepositorysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTQueryRepositorys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TQueryRepository> getTQueryRepositorys()
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            collTQueryRepositorys = getTQueryRepositorys(new Criteria(10));
        }
        return collTQueryRepositorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TQueryRepository> getTQueryRepositorys(Criteria criteria) throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID() );
                collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
                if (!lastTQueryRepositorysCriteria.equals(criteria))
                {
                    collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria);
                }
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTQueryRepositorys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TQueryRepository> getTQueryRepositorys(Connection con) throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            collTQueryRepositorys = getTQueryRepositorys(new Criteria(10), con);
        }
        return collTQueryRepositorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TQueryRepository> getTQueryRepositorys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                 criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
                 collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
                 if (!lastTQueryRepositorysCriteria.equals(criteria))
                 {
                     collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTQueryRepositorysCriteria = criteria;

         return collTQueryRepositorys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTCLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTCLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTCLOB(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTFilterCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTFilterCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.PROJECT, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTFilterCategory(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }





    /**
     * Collection to store aggregation of collTExportTemplates
     */
    protected List<TExportTemplate> collTExportTemplates;

    /**
     * Temporary storage of collTExportTemplates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTExportTemplates()
    {
        if (collTExportTemplates == null)
        {
            collTExportTemplates = new ArrayList<TExportTemplate>();
        }
    }


    /**
     * Method called to associate a TExportTemplate object to this object
     * through the TExportTemplate foreign key attribute
     *
     * @param l TExportTemplate
     * @throws TorqueException
     */
    public void addTExportTemplate(TExportTemplate l) throws TorqueException
    {
        getTExportTemplates().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TExportTemplate object to this object
     * through the TExportTemplate foreign key attribute using connection.
     *
     * @param l TExportTemplate
     * @throws TorqueException
     */
    public void addTExportTemplate(TExportTemplate l, Connection con) throws TorqueException
    {
        getTExportTemplates(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTExportTemplates
     */
    private Criteria lastTExportTemplatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTExportTemplates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TExportTemplate> getTExportTemplates()
        throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            collTExportTemplates = getTExportTemplates(new Criteria(10));
        }
        return collTExportTemplates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TExportTemplates from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TExportTemplate> getTExportTemplates(Criteria criteria) throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            if (isNew())
            {
               collTExportTemplates = new ArrayList<TExportTemplate>();
            }
            else
            {
                criteria.add(TExportTemplatePeer.PROJECT, getObjectID() );
                collTExportTemplates = TExportTemplatePeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TExportTemplatePeer.PROJECT, getObjectID());
                if (!lastTExportTemplatesCriteria.equals(criteria))
                {
                    collTExportTemplates = TExportTemplatePeer.doSelect(criteria);
                }
            }
        }
        lastTExportTemplatesCriteria = criteria;

        return collTExportTemplates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTExportTemplates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TExportTemplate> getTExportTemplates(Connection con) throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            collTExportTemplates = getTExportTemplates(new Criteria(10), con);
        }
        return collTExportTemplates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TExportTemplates from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TExportTemplate> getTExportTemplates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            if (isNew())
            {
               collTExportTemplates = new ArrayList<TExportTemplate>();
            }
            else
            {
                 criteria.add(TExportTemplatePeer.PROJECT, getObjectID());
                 collTExportTemplates = TExportTemplatePeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TExportTemplatePeer.PROJECT, getObjectID());
                 if (!lastTExportTemplatesCriteria.equals(criteria))
                 {
                     collTExportTemplates = TExportTemplatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTExportTemplatesCriteria = criteria;

         return collTExportTemplates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TExportTemplates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TExportTemplate> getTExportTemplatesJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            if (isNew())
            {
               collTExportTemplates = new ArrayList<TExportTemplate>();
            }
            else
            {
                criteria.add(TExportTemplatePeer.PROJECT, getObjectID());
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TExportTemplatePeer.PROJECT, getObjectID());
            if (!lastTExportTemplatesCriteria.equals(criteria))
            {
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTExportTemplatesCriteria = criteria;

        return collTExportTemplates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TExportTemplates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TExportTemplate> getTExportTemplatesJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            if (isNew())
            {
               collTExportTemplates = new ArrayList<TExportTemplate>();
            }
            else
            {
                criteria.add(TExportTemplatePeer.PROJECT, getObjectID());
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TExportTemplatePeer.PROJECT, getObjectID());
            if (!lastTExportTemplatesCriteria.equals(criteria))
            {
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTProject(criteria);
            }
        }
        lastTExportTemplatesCriteria = criteria;

        return collTExportTemplates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TExportTemplates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TExportTemplate> getTExportTemplatesJoinTReportCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            if (isNew())
            {
               collTExportTemplates = new ArrayList<TExportTemplate>();
            }
            else
            {
                criteria.add(TExportTemplatePeer.PROJECT, getObjectID());
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTReportCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TExportTemplatePeer.PROJECT, getObjectID());
            if (!lastTExportTemplatesCriteria.equals(criteria))
            {
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTReportCategory(criteria);
            }
        }
        lastTExportTemplatesCriteria = criteria;

        return collTExportTemplates;
    }













    /**
     * Collection to store aggregation of collTScriptss
     */
    protected List<TScripts> collTScriptss;

    /**
     * Temporary storage of collTScriptss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScriptss()
    {
        if (collTScriptss == null)
        {
            collTScriptss = new ArrayList<TScripts>();
        }
    }


    /**
     * Method called to associate a TScripts object to this object
     * through the TScripts foreign key attribute
     *
     * @param l TScripts
     * @throws TorqueException
     */
    public void addTScripts(TScripts l) throws TorqueException
    {
        getTScriptss().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TScripts object to this object
     * through the TScripts foreign key attribute using connection.
     *
     * @param l TScripts
     * @throws TorqueException
     */
    public void addTScripts(TScripts l, Connection con) throws TorqueException
    {
        getTScriptss(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTScriptss
     */
    private Criteria lastTScriptssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScriptss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScripts> getTScriptss()
        throws TorqueException
    {
        if (collTScriptss == null)
        {
            collTScriptss = getTScriptss(new Criteria(10));
        }
        return collTScriptss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TScriptss from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScripts> getTScriptss(Criteria criteria) throws TorqueException
    {
        if (collTScriptss == null)
        {
            if (isNew())
            {
               collTScriptss = new ArrayList<TScripts>();
            }
            else
            {
                criteria.add(TScriptsPeer.PROJECT, getObjectID() );
                collTScriptss = TScriptsPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TScriptsPeer.PROJECT, getObjectID());
                if (!lastTScriptssCriteria.equals(criteria))
                {
                    collTScriptss = TScriptsPeer.doSelect(criteria);
                }
            }
        }
        lastTScriptssCriteria = criteria;

        return collTScriptss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScriptss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScripts> getTScriptss(Connection con) throws TorqueException
    {
        if (collTScriptss == null)
        {
            collTScriptss = getTScriptss(new Criteria(10), con);
        }
        return collTScriptss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TScriptss from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScripts> getTScriptss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScriptss == null)
        {
            if (isNew())
            {
               collTScriptss = new ArrayList<TScripts>();
            }
            else
            {
                 criteria.add(TScriptsPeer.PROJECT, getObjectID());
                 collTScriptss = TScriptsPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TScriptsPeer.PROJECT, getObjectID());
                 if (!lastTScriptssCriteria.equals(criteria))
                 {
                     collTScriptss = TScriptsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScriptssCriteria = criteria;

         return collTScriptss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScriptss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScripts> getTScriptssJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTScriptss == null)
        {
            if (isNew())
            {
               collTScriptss = new ArrayList<TScripts>();
            }
            else
            {
                criteria.add(TScriptsPeer.PROJECT, getObjectID());
                collTScriptss = TScriptsPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScriptsPeer.PROJECT, getObjectID());
            if (!lastTScriptssCriteria.equals(criteria))
            {
                collTScriptss = TScriptsPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTScriptssCriteria = criteria;

        return collTScriptss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScriptss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScripts> getTScriptssJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTScriptss == null)
        {
            if (isNew())
            {
               collTScriptss = new ArrayList<TScripts>();
            }
            else
            {
                criteria.add(TScriptsPeer.PROJECT, getObjectID());
                collTScriptss = TScriptsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScriptsPeer.PROJECT, getObjectID());
            if (!lastTScriptssCriteria.equals(criteria))
            {
                collTScriptss = TScriptsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTScriptssCriteria = criteria;

        return collTScriptss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TScriptss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TScripts> getTScriptssJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTScriptss == null)
        {
            if (isNew())
            {
               collTScriptss = new ArrayList<TScripts>();
            }
            else
            {
                criteria.add(TScriptsPeer.PROJECT, getObjectID());
                collTScriptss = TScriptsPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScriptsPeer.PROJECT, getObjectID());
            if (!lastTScriptssCriteria.equals(criteria))
            {
                collTScriptss = TScriptsPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTScriptssCriteria = criteria;

        return collTScriptss;
    }





    /**
     * Collection to store aggregation of collTFilterCategorys
     */
    protected List<TFilterCategory> collTFilterCategorys;

    /**
     * Temporary storage of collTFilterCategorys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTFilterCategorys()
    {
        if (collTFilterCategorys == null)
        {
            collTFilterCategorys = new ArrayList<TFilterCategory>();
        }
    }


    /**
     * Method called to associate a TFilterCategory object to this object
     * through the TFilterCategory foreign key attribute
     *
     * @param l TFilterCategory
     * @throws TorqueException
     */
    public void addTFilterCategory(TFilterCategory l) throws TorqueException
    {
        getTFilterCategorys().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TFilterCategory object to this object
     * through the TFilterCategory foreign key attribute using connection.
     *
     * @param l TFilterCategory
     * @throws TorqueException
     */
    public void addTFilterCategory(TFilterCategory l, Connection con) throws TorqueException
    {
        getTFilterCategorys(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTFilterCategorys
     */
    private Criteria lastTFilterCategorysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFilterCategorys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TFilterCategory> getTFilterCategorys()
        throws TorqueException
    {
        if (collTFilterCategorys == null)
        {
            collTFilterCategorys = getTFilterCategorys(new Criteria(10));
        }
        return collTFilterCategorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TFilterCategorys from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TFilterCategory> getTFilterCategorys(Criteria criteria) throws TorqueException
    {
        if (collTFilterCategorys == null)
        {
            if (isNew())
            {
               collTFilterCategorys = new ArrayList<TFilterCategory>();
            }
            else
            {
                criteria.add(TFilterCategoryPeer.PROJECT, getObjectID() );
                collTFilterCategorys = TFilterCategoryPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TFilterCategoryPeer.PROJECT, getObjectID());
                if (!lastTFilterCategorysCriteria.equals(criteria))
                {
                    collTFilterCategorys = TFilterCategoryPeer.doSelect(criteria);
                }
            }
        }
        lastTFilterCategorysCriteria = criteria;

        return collTFilterCategorys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFilterCategorys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFilterCategory> getTFilterCategorys(Connection con) throws TorqueException
    {
        if (collTFilterCategorys == null)
        {
            collTFilterCategorys = getTFilterCategorys(new Criteria(10), con);
        }
        return collTFilterCategorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TFilterCategorys from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFilterCategory> getTFilterCategorys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTFilterCategorys == null)
        {
            if (isNew())
            {
               collTFilterCategorys = new ArrayList<TFilterCategory>();
            }
            else
            {
                 criteria.add(TFilterCategoryPeer.PROJECT, getObjectID());
                 collTFilterCategorys = TFilterCategoryPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TFilterCategoryPeer.PROJECT, getObjectID());
                 if (!lastTFilterCategorysCriteria.equals(criteria))
                 {
                     collTFilterCategorys = TFilterCategoryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTFilterCategorysCriteria = criteria;

         return collTFilterCategorys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TFilterCategorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TFilterCategory> getTFilterCategorysJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTFilterCategorys == null)
        {
            if (isNew())
            {
               collTFilterCategorys = new ArrayList<TFilterCategory>();
            }
            else
            {
                criteria.add(TFilterCategoryPeer.PROJECT, getObjectID());
                collTFilterCategorys = TFilterCategoryPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFilterCategoryPeer.PROJECT, getObjectID());
            if (!lastTFilterCategorysCriteria.equals(criteria))
            {
                collTFilterCategorys = TFilterCategoryPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTFilterCategorysCriteria = criteria;

        return collTFilterCategorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TFilterCategorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TFilterCategory> getTFilterCategorysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTFilterCategorys == null)
        {
            if (isNew())
            {
               collTFilterCategorys = new ArrayList<TFilterCategory>();
            }
            else
            {
                criteria.add(TFilterCategoryPeer.PROJECT, getObjectID());
                collTFilterCategorys = TFilterCategoryPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFilterCategoryPeer.PROJECT, getObjectID());
            if (!lastTFilterCategorysCriteria.equals(criteria))
            {
                collTFilterCategorys = TFilterCategoryPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTFilterCategorysCriteria = criteria;

        return collTFilterCategorys;
    }













    /**
     * Collection to store aggregation of collTReportCategorys
     */
    protected List<TReportCategory> collTReportCategorys;

    /**
     * Temporary storage of collTReportCategorys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTReportCategorys()
    {
        if (collTReportCategorys == null)
        {
            collTReportCategorys = new ArrayList<TReportCategory>();
        }
    }


    /**
     * Method called to associate a TReportCategory object to this object
     * through the TReportCategory foreign key attribute
     *
     * @param l TReportCategory
     * @throws TorqueException
     */
    public void addTReportCategory(TReportCategory l) throws TorqueException
    {
        getTReportCategorys().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TReportCategory object to this object
     * through the TReportCategory foreign key attribute using connection.
     *
     * @param l TReportCategory
     * @throws TorqueException
     */
    public void addTReportCategory(TReportCategory l, Connection con) throws TorqueException
    {
        getTReportCategorys(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTReportCategorys
     */
    private Criteria lastTReportCategorysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportCategorys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TReportCategory> getTReportCategorys()
        throws TorqueException
    {
        if (collTReportCategorys == null)
        {
            collTReportCategorys = getTReportCategorys(new Criteria(10));
        }
        return collTReportCategorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TReportCategorys from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TReportCategory> getTReportCategorys(Criteria criteria) throws TorqueException
    {
        if (collTReportCategorys == null)
        {
            if (isNew())
            {
               collTReportCategorys = new ArrayList<TReportCategory>();
            }
            else
            {
                criteria.add(TReportCategoryPeer.PROJECT, getObjectID() );
                collTReportCategorys = TReportCategoryPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TReportCategoryPeer.PROJECT, getObjectID());
                if (!lastTReportCategorysCriteria.equals(criteria))
                {
                    collTReportCategorys = TReportCategoryPeer.doSelect(criteria);
                }
            }
        }
        lastTReportCategorysCriteria = criteria;

        return collTReportCategorys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportCategorys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportCategory> getTReportCategorys(Connection con) throws TorqueException
    {
        if (collTReportCategorys == null)
        {
            collTReportCategorys = getTReportCategorys(new Criteria(10), con);
        }
        return collTReportCategorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TReportCategorys from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportCategory> getTReportCategorys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTReportCategorys == null)
        {
            if (isNew())
            {
               collTReportCategorys = new ArrayList<TReportCategory>();
            }
            else
            {
                 criteria.add(TReportCategoryPeer.PROJECT, getObjectID());
                 collTReportCategorys = TReportCategoryPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TReportCategoryPeer.PROJECT, getObjectID());
                 if (!lastTReportCategorysCriteria.equals(criteria))
                 {
                     collTReportCategorys = TReportCategoryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTReportCategorysCriteria = criteria;

         return collTReportCategorys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TReportCategorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TReportCategory> getTReportCategorysJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTReportCategorys == null)
        {
            if (isNew())
            {
               collTReportCategorys = new ArrayList<TReportCategory>();
            }
            else
            {
                criteria.add(TReportCategoryPeer.PROJECT, getObjectID());
                collTReportCategorys = TReportCategoryPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportCategoryPeer.PROJECT, getObjectID());
            if (!lastTReportCategorysCriteria.equals(criteria))
            {
                collTReportCategorys = TReportCategoryPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTReportCategorysCriteria = criteria;

        return collTReportCategorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TReportCategorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TReportCategory> getTReportCategorysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTReportCategorys == null)
        {
            if (isNew())
            {
               collTReportCategorys = new ArrayList<TReportCategory>();
            }
            else
            {
                criteria.add(TReportCategoryPeer.PROJECT, getObjectID());
                collTReportCategorys = TReportCategoryPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportCategoryPeer.PROJECT, getObjectID());
            if (!lastTReportCategorysCriteria.equals(criteria))
            {
                collTReportCategorys = TReportCategoryPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTReportCategorysCriteria = criteria;

        return collTReportCategorys;
    }













    /**
     * Collection to store aggregation of collTMailTemplateConfigs
     */
    protected List<TMailTemplateConfig> collTMailTemplateConfigs;

    /**
     * Temporary storage of collTMailTemplateConfigs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTMailTemplateConfigs()
    {
        if (collTMailTemplateConfigs == null)
        {
            collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
        }
    }


    /**
     * Method called to associate a TMailTemplateConfig object to this object
     * through the TMailTemplateConfig foreign key attribute
     *
     * @param l TMailTemplateConfig
     * @throws TorqueException
     */
    public void addTMailTemplateConfig(TMailTemplateConfig l) throws TorqueException
    {
        getTMailTemplateConfigs().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TMailTemplateConfig object to this object
     * through the TMailTemplateConfig foreign key attribute using connection.
     *
     * @param l TMailTemplateConfig
     * @throws TorqueException
     */
    public void addTMailTemplateConfig(TMailTemplateConfig l, Connection con) throws TorqueException
    {
        getTMailTemplateConfigs(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTMailTemplateConfigs
     */
    private Criteria lastTMailTemplateConfigsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMailTemplateConfigs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TMailTemplateConfig> getTMailTemplateConfigs()
        throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            collTMailTemplateConfigs = getTMailTemplateConfigs(new Criteria(10));
        }
        return collTMailTemplateConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TMailTemplateConfig> getTMailTemplateConfigs(Criteria criteria) throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID() );
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
                if (!lastTMailTemplateConfigsCriteria.equals(criteria))
                {
                    collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelect(criteria);
                }
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMailTemplateConfigs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMailTemplateConfig> getTMailTemplateConfigs(Connection con) throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            collTMailTemplateConfigs = getTMailTemplateConfigs(new Criteria(10), con);
        }
        return collTMailTemplateConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMailTemplateConfig> getTMailTemplateConfigs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                 criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
                 collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
                 if (!lastTMailTemplateConfigsCriteria.equals(criteria))
                 {
                     collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTMailTemplateConfigsCriteria = criteria;

         return collTMailTemplateConfigs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TMailTemplateConfig> getTMailTemplateConfigsJoinTMailTemplate(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTMailTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
            if (!lastTMailTemplateConfigsCriteria.equals(criteria))
            {
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTMailTemplate(criteria);
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TMailTemplateConfig> getTMailTemplateConfigsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
            if (!lastTMailTemplateConfigsCriteria.equals(criteria))
            {
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TMailTemplateConfig> getTMailTemplateConfigsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
            if (!lastTMailTemplateConfigsCriteria.equals(criteria))
            {
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TMailTemplateConfig> getTMailTemplateConfigsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.PROJECT, getObjectID());
            if (!lastTMailTemplateConfigsCriteria.equals(criteria))
            {
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }





    /**
     * Collection to store aggregation of collTWfActivityContextParamss
     */
    protected List<TWfActivityContextParams> collTWfActivityContextParamss;

    /**
     * Temporary storage of collTWfActivityContextParamss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWfActivityContextParamss()
    {
        if (collTWfActivityContextParamss == null)
        {
            collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
        }
    }


    /**
     * Method called to associate a TWfActivityContextParams object to this object
     * through the TWfActivityContextParams foreign key attribute
     *
     * @param l TWfActivityContextParams
     * @throws TorqueException
     */
    public void addTWfActivityContextParams(TWfActivityContextParams l) throws TorqueException
    {
        getTWfActivityContextParamss().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TWfActivityContextParams object to this object
     * through the TWfActivityContextParams foreign key attribute using connection.
     *
     * @param l TWfActivityContextParams
     * @throws TorqueException
     */
    public void addTWfActivityContextParams(TWfActivityContextParams l, Connection con) throws TorqueException
    {
        getTWfActivityContextParamss(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTWfActivityContextParamss
     */
    private Criteria lastTWfActivityContextParamssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWfActivityContextParamss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss()
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            collTWfActivityContextParamss = getTWfActivityContextParamss(new Criteria(10));
        }
        return collTWfActivityContextParamss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss(Criteria criteria) throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID() );
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
                if (!lastTWfActivityContextParamssCriteria.equals(criteria))
                {
                    collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria);
                }
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWfActivityContextParamss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss(Connection con) throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            collTWfActivityContextParamss = getTWfActivityContextParamss(new Criteria(10), con);
        }
        return collTWfActivityContextParamss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                 criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
                 collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
                 if (!lastTWfActivityContextParamssCriteria.equals(criteria))
                 {
                     collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWfActivityContextParamssCriteria = criteria;

         return collTWfActivityContextParamss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTWorkflowActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTWorkflowActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTWorkflowActivity(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.PROJECT, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }





    /**
     * Collection to store aggregation of collTWorkflowConnects
     */
    protected List<TWorkflowConnect> collTWorkflowConnects;

    /**
     * Temporary storage of collTWorkflowConnects to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowConnects()
    {
        if (collTWorkflowConnects == null)
        {
            collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
        }
    }


    /**
     * Method called to associate a TWorkflowConnect object to this object
     * through the TWorkflowConnect foreign key attribute
     *
     * @param l TWorkflowConnect
     * @throws TorqueException
     */
    public void addTWorkflowConnect(TWorkflowConnect l) throws TorqueException
    {
        getTWorkflowConnects().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TWorkflowConnect object to this object
     * through the TWorkflowConnect foreign key attribute using connection.
     *
     * @param l TWorkflowConnect
     * @throws TorqueException
     */
    public void addTWorkflowConnect(TWorkflowConnect l, Connection con) throws TorqueException
    {
        getTWorkflowConnects(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowConnects
     */
    private Criteria lastTWorkflowConnectsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowConnects(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowConnect> getTWorkflowConnects()
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            collTWorkflowConnects = getTWorkflowConnects(new Criteria(10));
        }
        return collTWorkflowConnects;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowConnect> getTWorkflowConnects(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID() );
                collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
                if (!lastTWorkflowConnectsCriteria.equals(criteria))
                {
                    collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowConnects(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowConnect> getTWorkflowConnects(Connection con) throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            collTWorkflowConnects = getTWorkflowConnects(new Criteria(10), con);
        }
        return collTWorkflowConnects;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowConnect> getTWorkflowConnects(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                 criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
                 collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
                 if (!lastTWorkflowConnectsCriteria.equals(criteria))
                 {
                     collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowConnectsCriteria = criteria;

         return collTWorkflowConnects;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTWorkflowDef(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.PROJECT, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }





    /**
     * Collection to store aggregation of collTOrgProjectSLAs
     */
    protected List<TOrgProjectSLA> collTOrgProjectSLAs;

    /**
     * Temporary storage of collTOrgProjectSLAs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTOrgProjectSLAs()
    {
        if (collTOrgProjectSLAs == null)
        {
            collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
        }
    }


    /**
     * Method called to associate a TOrgProjectSLA object to this object
     * through the TOrgProjectSLA foreign key attribute
     *
     * @param l TOrgProjectSLA
     * @throws TorqueException
     */
    public void addTOrgProjectSLA(TOrgProjectSLA l) throws TorqueException
    {
        getTOrgProjectSLAs().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TOrgProjectSLA object to this object
     * through the TOrgProjectSLA foreign key attribute using connection.
     *
     * @param l TOrgProjectSLA
     * @throws TorqueException
     */
    public void addTOrgProjectSLA(TOrgProjectSLA l, Connection con) throws TorqueException
    {
        getTOrgProjectSLAs(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTOrgProjectSLAs
     */
    private Criteria lastTOrgProjectSLAsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOrgProjectSLAs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TOrgProjectSLA> getTOrgProjectSLAs()
        throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            collTOrgProjectSLAs = getTOrgProjectSLAs(new Criteria(10));
        }
        return collTOrgProjectSLAs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TOrgProjectSLA> getTOrgProjectSLAs(Criteria criteria) throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            if (isNew())
            {
               collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
            }
            else
            {
                criteria.add(TOrgProjectSLAPeer.PROJECT, getObjectID() );
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TOrgProjectSLAPeer.PROJECT, getObjectID());
                if (!lastTOrgProjectSLAsCriteria.equals(criteria))
                {
                    collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelect(criteria);
                }
            }
        }
        lastTOrgProjectSLAsCriteria = criteria;

        return collTOrgProjectSLAs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOrgProjectSLAs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOrgProjectSLA> getTOrgProjectSLAs(Connection con) throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            collTOrgProjectSLAs = getTOrgProjectSLAs(new Criteria(10), con);
        }
        return collTOrgProjectSLAs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOrgProjectSLA> getTOrgProjectSLAs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            if (isNew())
            {
               collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
            }
            else
            {
                 criteria.add(TOrgProjectSLAPeer.PROJECT, getObjectID());
                 collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TOrgProjectSLAPeer.PROJECT, getObjectID());
                 if (!lastTOrgProjectSLAsCriteria.equals(criteria))
                 {
                     collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTOrgProjectSLAsCriteria = criteria;

         return collTOrgProjectSLAs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TOrgProjectSLA> getTOrgProjectSLAsJoinTDepartment(Criteria criteria)
        throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            if (isNew())
            {
               collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
            }
            else
            {
                criteria.add(TOrgProjectSLAPeer.PROJECT, getObjectID());
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTDepartment(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOrgProjectSLAPeer.PROJECT, getObjectID());
            if (!lastTOrgProjectSLAsCriteria.equals(criteria))
            {
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTDepartment(criteria);
            }
        }
        lastTOrgProjectSLAsCriteria = criteria;

        return collTOrgProjectSLAs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TOrgProjectSLA> getTOrgProjectSLAsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            if (isNew())
            {
               collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
            }
            else
            {
                criteria.add(TOrgProjectSLAPeer.PROJECT, getObjectID());
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOrgProjectSLAPeer.PROJECT, getObjectID());
            if (!lastTOrgProjectSLAsCriteria.equals(criteria))
            {
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTOrgProjectSLAsCriteria = criteria;

        return collTOrgProjectSLAs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TOrgProjectSLA> getTOrgProjectSLAsJoinTSLA(Criteria criteria)
        throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            if (isNew())
            {
               collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
            }
            else
            {
                criteria.add(TOrgProjectSLAPeer.PROJECT, getObjectID());
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOrgProjectSLAPeer.PROJECT, getObjectID());
            if (!lastTOrgProjectSLAsCriteria.equals(criteria))
            {
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTSLA(criteria);
            }
        }
        lastTOrgProjectSLAsCriteria = criteria;

        return collTOrgProjectSLAs;
    }





    /**
     * Collection to store aggregation of collTMailTextBlocks
     */
    protected List<TMailTextBlock> collTMailTextBlocks;

    /**
     * Temporary storage of collTMailTextBlocks to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTMailTextBlocks()
    {
        if (collTMailTextBlocks == null)
        {
            collTMailTextBlocks = new ArrayList<TMailTextBlock>();
        }
    }


    /**
     * Method called to associate a TMailTextBlock object to this object
     * through the TMailTextBlock foreign key attribute
     *
     * @param l TMailTextBlock
     * @throws TorqueException
     */
    public void addTMailTextBlock(TMailTextBlock l) throws TorqueException
    {
        getTMailTextBlocks().add(l);
        l.setTProject((TProject) this);
    }

    /**
     * Method called to associate a TMailTextBlock object to this object
     * through the TMailTextBlock foreign key attribute using connection.
     *
     * @param l TMailTextBlock
     * @throws TorqueException
     */
    public void addTMailTextBlock(TMailTextBlock l, Connection con) throws TorqueException
    {
        getTMailTextBlocks(con).add(l);
        l.setTProject((TProject) this);
    }

    /**
     * The criteria used to select the current contents of collTMailTextBlocks
     */
    private Criteria lastTMailTextBlocksCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMailTextBlocks(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TMailTextBlock> getTMailTextBlocks()
        throws TorqueException
    {
        if (collTMailTextBlocks == null)
        {
            collTMailTextBlocks = getTMailTextBlocks(new Criteria(10));
        }
        return collTMailTextBlocks;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TMailTextBlocks from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TMailTextBlock> getTMailTextBlocks(Criteria criteria) throws TorqueException
    {
        if (collTMailTextBlocks == null)
        {
            if (isNew())
            {
               collTMailTextBlocks = new ArrayList<TMailTextBlock>();
            }
            else
            {
                criteria.add(TMailTextBlockPeer.PROJECT, getObjectID() );
                collTMailTextBlocks = TMailTextBlockPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TMailTextBlockPeer.PROJECT, getObjectID());
                if (!lastTMailTextBlocksCriteria.equals(criteria))
                {
                    collTMailTextBlocks = TMailTextBlockPeer.doSelect(criteria);
                }
            }
        }
        lastTMailTextBlocksCriteria = criteria;

        return collTMailTextBlocks;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMailTextBlocks(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMailTextBlock> getTMailTextBlocks(Connection con) throws TorqueException
    {
        if (collTMailTextBlocks == null)
        {
            collTMailTextBlocks = getTMailTextBlocks(new Criteria(10), con);
        }
        return collTMailTextBlocks;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject has previously
     * been saved, it will retrieve related TMailTextBlocks from storage.
     * If this TProject is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMailTextBlock> getTMailTextBlocks(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTMailTextBlocks == null)
        {
            if (isNew())
            {
               collTMailTextBlocks = new ArrayList<TMailTextBlock>();
            }
            else
            {
                 criteria.add(TMailTextBlockPeer.PROJECT, getObjectID());
                 collTMailTextBlocks = TMailTextBlockPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TMailTextBlockPeer.PROJECT, getObjectID());
                 if (!lastTMailTextBlocksCriteria.equals(criteria))
                 {
                     collTMailTextBlocks = TMailTextBlockPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTMailTextBlocksCriteria = criteria;

         return collTMailTextBlocks;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TMailTextBlocks from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TMailTextBlock> getTMailTextBlocksJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTextBlocks == null)
        {
            if (isNew())
            {
               collTMailTextBlocks = new ArrayList<TMailTextBlock>();
            }
            else
            {
                criteria.add(TMailTextBlockPeer.PROJECT, getObjectID());
                collTMailTextBlocks = TMailTextBlockPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTextBlockPeer.PROJECT, getObjectID());
            if (!lastTMailTextBlocksCriteria.equals(criteria))
            {
                collTMailTextBlocks = TMailTextBlockPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTMailTextBlocksCriteria = criteria;

        return collTMailTextBlocks;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProject is new, it will return
     * an empty collection; or if this TProject has previously
     * been saved, it will retrieve related TMailTextBlocks from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProject.
     */
    protected List<TMailTextBlock> getTMailTextBlocksJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTextBlocks == null)
        {
            if (isNew())
            {
               collTMailTextBlocks = new ArrayList<TMailTextBlock>();
            }
            else
            {
                criteria.add(TMailTextBlockPeer.PROJECT, getObjectID());
                collTMailTextBlocks = TMailTextBlockPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTextBlockPeer.PROJECT, getObjectID());
            if (!lastTMailTextBlocksCriteria.equals(criteria))
            {
                collTMailTextBlocks = TMailTextBlockPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTMailTextBlocksCriteria = criteria;

        return collTMailTextBlocks;
    }



        
    private static List<String> fieldNames = null;

    /**
     * Generate a list of field names.
     *
     * @return a list of field names
     */
    public static synchronized List<String> getFieldNames()
    {
        if (fieldNames == null)
        {
            fieldNames = new ArrayList<String>();
            fieldNames.add("ObjectID");
            fieldNames.add("Label");
            fieldNames.add("DefaultOwnerID");
            fieldNames.add("DefaultManagerID");
            fieldNames.add("DefaultInitStateID");
            fieldNames.add("ProjectType");
            fieldNames.add("VersionSystemField0");
            fieldNames.add("VersionSystemField1");
            fieldNames.add("VersionSystemField2");
            fieldNames.add("VersionSystemField3");
            fieldNames.add("Deleted");
            fieldNames.add("Status");
            fieldNames.add("CurrencyName");
            fieldNames.add("CurrencySymbol");
            fieldNames.add("HoursPerWorkDay");
            fieldNames.add("MoreProps");
            fieldNames.add("TagLabel");
            fieldNames.add("Description");
            fieldNames.add("Prefix");
            fieldNames.add("NextItemID");
            fieldNames.add("LastID");
            fieldNames.add("Parent");
            fieldNames.add("Sortorder");
            fieldNames.add("IsPrivate");
            fieldNames.add("IsTemplate");
            fieldNames.add("Domain");
            fieldNames.add("Uuid");
            fieldNames = Collections.unmodifiableList(fieldNames);
        }
        return fieldNames;
    }

    /**
     * Retrieves a field from the object by field (Java) name passed in as a String.
     *
     * @param name field name
     * @return value
     */
    public Object getByName(String name)
    {
        if (name.equals("ObjectID"))
        {
            return getObjectID();
        }
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("DefaultOwnerID"))
        {
            return getDefaultOwnerID();
        }
        if (name.equals("DefaultManagerID"))
        {
            return getDefaultManagerID();
        }
        if (name.equals("DefaultInitStateID"))
        {
            return getDefaultInitStateID();
        }
        if (name.equals("ProjectType"))
        {
            return getProjectType();
        }
        if (name.equals("VersionSystemField0"))
        {
            return getVersionSystemField0();
        }
        if (name.equals("VersionSystemField1"))
        {
            return getVersionSystemField1();
        }
        if (name.equals("VersionSystemField2"))
        {
            return getVersionSystemField2();
        }
        if (name.equals("VersionSystemField3"))
        {
            return getVersionSystemField3();
        }
        if (name.equals("Deleted"))
        {
            return getDeleted();
        }
        if (name.equals("Status"))
        {
            return getStatus();
        }
        if (name.equals("CurrencyName"))
        {
            return getCurrencyName();
        }
        if (name.equals("CurrencySymbol"))
        {
            return getCurrencySymbol();
        }
        if (name.equals("HoursPerWorkDay"))
        {
            return getHoursPerWorkDay();
        }
        if (name.equals("MoreProps"))
        {
            return getMoreProps();
        }
        if (name.equals("TagLabel"))
        {
            return getTagLabel();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("Prefix"))
        {
            return getPrefix();
        }
        if (name.equals("NextItemID"))
        {
            return getNextItemID();
        }
        if (name.equals("LastID"))
        {
            return getLastID();
        }
        if (name.equals("Parent"))
        {
            return getParent();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
        }
        if (name.equals("IsPrivate"))
        {
            return getIsPrivate();
        }
        if (name.equals("IsTemplate"))
        {
            return getIsTemplate();
        }
        if (name.equals("Domain"))
        {
            return getDomain();
        }
        if (name.equals("Uuid"))
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set a field in the object by field (Java) name.
     *
     * @param name field name
     * @param value field value
     * @return True if value was set, false if not (invalid name / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByName(String name, Object value )
        throws TorqueException, IllegalArgumentException
    {
        if (name.equals("ObjectID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setObjectID((Integer) value);
            return true;
        }
        if (name.equals("Label"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLabel((String) value);
            return true;
        }
        if (name.equals("DefaultOwnerID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultOwnerID((Integer) value);
            return true;
        }
        if (name.equals("DefaultManagerID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultManagerID((Integer) value);
            return true;
        }
        if (name.equals("DefaultInitStateID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultInitStateID((Integer) value);
            return true;
        }
        if (name.equals("ProjectType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProjectType((Integer) value);
            return true;
        }
        if (name.equals("VersionSystemField0"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setVersionSystemField0((String) value);
            return true;
        }
        if (name.equals("VersionSystemField1"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setVersionSystemField1((String) value);
            return true;
        }
        if (name.equals("VersionSystemField2"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setVersionSystemField2((String) value);
            return true;
        }
        if (name.equals("VersionSystemField3"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setVersionSystemField3((String) value);
            return true;
        }
        if (name.equals("Deleted"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDeleted((String) value);
            return true;
        }
        if (name.equals("Status"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStatus((Integer) value);
            return true;
        }
        if (name.equals("CurrencyName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCurrencyName((String) value);
            return true;
        }
        if (name.equals("CurrencySymbol"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCurrencySymbol((String) value);
            return true;
        }
        if (name.equals("HoursPerWorkDay"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setHoursPerWorkDay((Double) value);
            return true;
        }
        if (name.equals("MoreProps"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMoreProps((String) value);
            return true;
        }
        if (name.equals("TagLabel"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTagLabel((String) value);
            return true;
        }
        if (name.equals("Description"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDescription((String) value);
            return true;
        }
        if (name.equals("Prefix"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPrefix((String) value);
            return true;
        }
        if (name.equals("NextItemID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNextItemID((Integer) value);
            return true;
        }
        if (name.equals("LastID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLastID((Integer) value);
            return true;
        }
        if (name.equals("Parent"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParent((Integer) value);
            return true;
        }
        if (name.equals("Sortorder"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSortorder((Integer) value);
            return true;
        }
        if (name.equals("IsPrivate"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsPrivate((String) value);
            return true;
        }
        if (name.equals("IsTemplate"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsTemplate((String) value);
            return true;
        }
        if (name.equals("Domain"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDomain((Integer) value);
            return true;
        }
        if (name.equals("Uuid"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setUuid((String) value);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     *
     * @param name peer name
     * @return value
     */
    public Object getByPeerName(String name)
    {
        if (name.equals(TProjectPeer.PKEY))
        {
            return getObjectID();
        }
        if (name.equals(TProjectPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TProjectPeer.DEFOWNER))
        {
            return getDefaultOwnerID();
        }
        if (name.equals(TProjectPeer.DEFMANAGER))
        {
            return getDefaultManagerID();
        }
        if (name.equals(TProjectPeer.DEFINITSTATE))
        {
            return getDefaultInitStateID();
        }
        if (name.equals(TProjectPeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TProjectPeer.VERSIONSYSTEMFIELD0))
        {
            return getVersionSystemField0();
        }
        if (name.equals(TProjectPeer.VERSIONSYSTEMFIELD1))
        {
            return getVersionSystemField1();
        }
        if (name.equals(TProjectPeer.VERSIONSYSTEMFIELD2))
        {
            return getVersionSystemField2();
        }
        if (name.equals(TProjectPeer.VERSIONSYSTEMFIELD3))
        {
            return getVersionSystemField3();
        }
        if (name.equals(TProjectPeer.DELETED))
        {
            return getDeleted();
        }
        if (name.equals(TProjectPeer.STATUS))
        {
            return getStatus();
        }
        if (name.equals(TProjectPeer.CURRENCYNAME))
        {
            return getCurrencyName();
        }
        if (name.equals(TProjectPeer.CURRENCYSYMBOL))
        {
            return getCurrencySymbol();
        }
        if (name.equals(TProjectPeer.HOURSPERWORKDAY))
        {
            return getHoursPerWorkDay();
        }
        if (name.equals(TProjectPeer.MOREPROPS))
        {
            return getMoreProps();
        }
        if (name.equals(TProjectPeer.TAGLABEL))
        {
            return getTagLabel();
        }
        if (name.equals(TProjectPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TProjectPeer.PREFIX))
        {
            return getPrefix();
        }
        if (name.equals(TProjectPeer.NEXTITEMID))
        {
            return getNextItemID();
        }
        if (name.equals(TProjectPeer.LASTID))
        {
            return getLastID();
        }
        if (name.equals(TProjectPeer.PARENT))
        {
            return getParent();
        }
        if (name.equals(TProjectPeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TProjectPeer.ISPRIVATE))
        {
            return getIsPrivate();
        }
        if (name.equals(TProjectPeer.ISTEMPLATE))
        {
            return getIsTemplate();
        }
        if (name.equals(TProjectPeer.DOMAINKEY))
        {
            return getDomain();
        }
        if (name.equals(TProjectPeer.TPUUID))
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set field values by Peer Field Name
     *
     * @param name field name
     * @param value field value
     * @return True if value was set, false if not (invalid name / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByPeerName(String name, Object value)
        throws TorqueException, IllegalArgumentException
    {
      if (TProjectPeer.PKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TProjectPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TProjectPeer.DEFOWNER.equals(name))
        {
            return setByName("DefaultOwnerID", value);
        }
      if (TProjectPeer.DEFMANAGER.equals(name))
        {
            return setByName("DefaultManagerID", value);
        }
      if (TProjectPeer.DEFINITSTATE.equals(name))
        {
            return setByName("DefaultInitStateID", value);
        }
      if (TProjectPeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TProjectPeer.VERSIONSYSTEMFIELD0.equals(name))
        {
            return setByName("VersionSystemField0", value);
        }
      if (TProjectPeer.VERSIONSYSTEMFIELD1.equals(name))
        {
            return setByName("VersionSystemField1", value);
        }
      if (TProjectPeer.VERSIONSYSTEMFIELD2.equals(name))
        {
            return setByName("VersionSystemField2", value);
        }
      if (TProjectPeer.VERSIONSYSTEMFIELD3.equals(name))
        {
            return setByName("VersionSystemField3", value);
        }
      if (TProjectPeer.DELETED.equals(name))
        {
            return setByName("Deleted", value);
        }
      if (TProjectPeer.STATUS.equals(name))
        {
            return setByName("Status", value);
        }
      if (TProjectPeer.CURRENCYNAME.equals(name))
        {
            return setByName("CurrencyName", value);
        }
      if (TProjectPeer.CURRENCYSYMBOL.equals(name))
        {
            return setByName("CurrencySymbol", value);
        }
      if (TProjectPeer.HOURSPERWORKDAY.equals(name))
        {
            return setByName("HoursPerWorkDay", value);
        }
      if (TProjectPeer.MOREPROPS.equals(name))
        {
            return setByName("MoreProps", value);
        }
      if (TProjectPeer.TAGLABEL.equals(name))
        {
            return setByName("TagLabel", value);
        }
      if (TProjectPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TProjectPeer.PREFIX.equals(name))
        {
            return setByName("Prefix", value);
        }
      if (TProjectPeer.NEXTITEMID.equals(name))
        {
            return setByName("NextItemID", value);
        }
      if (TProjectPeer.LASTID.equals(name))
        {
            return setByName("LastID", value);
        }
      if (TProjectPeer.PARENT.equals(name))
        {
            return setByName("Parent", value);
        }
      if (TProjectPeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TProjectPeer.ISPRIVATE.equals(name))
        {
            return setByName("IsPrivate", value);
        }
      if (TProjectPeer.ISTEMPLATE.equals(name))
        {
            return setByName("IsTemplate", value);
        }
      if (TProjectPeer.DOMAINKEY.equals(name))
        {
            return setByName("Domain", value);
        }
      if (TProjectPeer.TPUUID.equals(name))
        {
            return setByName("Uuid", value);
        }
        return false;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     *
     * @param pos position in xml schema
     * @return value
     */
    public Object getByPosition(int pos)
    {
        if (pos == 0)
        {
            return getObjectID();
        }
        if (pos == 1)
        {
            return getLabel();
        }
        if (pos == 2)
        {
            return getDefaultOwnerID();
        }
        if (pos == 3)
        {
            return getDefaultManagerID();
        }
        if (pos == 4)
        {
            return getDefaultInitStateID();
        }
        if (pos == 5)
        {
            return getProjectType();
        }
        if (pos == 6)
        {
            return getVersionSystemField0();
        }
        if (pos == 7)
        {
            return getVersionSystemField1();
        }
        if (pos == 8)
        {
            return getVersionSystemField2();
        }
        if (pos == 9)
        {
            return getVersionSystemField3();
        }
        if (pos == 10)
        {
            return getDeleted();
        }
        if (pos == 11)
        {
            return getStatus();
        }
        if (pos == 12)
        {
            return getCurrencyName();
        }
        if (pos == 13)
        {
            return getCurrencySymbol();
        }
        if (pos == 14)
        {
            return getHoursPerWorkDay();
        }
        if (pos == 15)
        {
            return getMoreProps();
        }
        if (pos == 16)
        {
            return getTagLabel();
        }
        if (pos == 17)
        {
            return getDescription();
        }
        if (pos == 18)
        {
            return getPrefix();
        }
        if (pos == 19)
        {
            return getNextItemID();
        }
        if (pos == 20)
        {
            return getLastID();
        }
        if (pos == 21)
        {
            return getParent();
        }
        if (pos == 22)
        {
            return getSortorder();
        }
        if (pos == 23)
        {
            return getIsPrivate();
        }
        if (pos == 24)
        {
            return getIsTemplate();
        }
        if (pos == 25)
        {
            return getDomain();
        }
        if (pos == 26)
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set field values by its position (zero based) in the XML schema.
     *
     * @param position The field position
     * @param value field value
     * @return True if value was set, false if not (invalid position / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByPosition(int position, Object value)
        throws TorqueException, IllegalArgumentException
    {
    if (position == 0)
        {
            return setByName("ObjectID", value);
        }
    if (position == 1)
        {
            return setByName("Label", value);
        }
    if (position == 2)
        {
            return setByName("DefaultOwnerID", value);
        }
    if (position == 3)
        {
            return setByName("DefaultManagerID", value);
        }
    if (position == 4)
        {
            return setByName("DefaultInitStateID", value);
        }
    if (position == 5)
        {
            return setByName("ProjectType", value);
        }
    if (position == 6)
        {
            return setByName("VersionSystemField0", value);
        }
    if (position == 7)
        {
            return setByName("VersionSystemField1", value);
        }
    if (position == 8)
        {
            return setByName("VersionSystemField2", value);
        }
    if (position == 9)
        {
            return setByName("VersionSystemField3", value);
        }
    if (position == 10)
        {
            return setByName("Deleted", value);
        }
    if (position == 11)
        {
            return setByName("Status", value);
        }
    if (position == 12)
        {
            return setByName("CurrencyName", value);
        }
    if (position == 13)
        {
            return setByName("CurrencySymbol", value);
        }
    if (position == 14)
        {
            return setByName("HoursPerWorkDay", value);
        }
    if (position == 15)
        {
            return setByName("MoreProps", value);
        }
    if (position == 16)
        {
            return setByName("TagLabel", value);
        }
    if (position == 17)
        {
            return setByName("Description", value);
        }
    if (position == 18)
        {
            return setByName("Prefix", value);
        }
    if (position == 19)
        {
            return setByName("NextItemID", value);
        }
    if (position == 20)
        {
            return setByName("LastID", value);
        }
    if (position == 21)
        {
            return setByName("Parent", value);
        }
    if (position == 22)
        {
            return setByName("Sortorder", value);
        }
    if (position == 23)
        {
            return setByName("IsPrivate", value);
        }
    if (position == 24)
        {
            return setByName("IsTemplate", value);
        }
    if (position == 25)
        {
            return setByName("Domain", value);
        }
    if (position == 26)
        {
            return setByName("Uuid", value);
        }
        return false;
    }
     
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     *
     * @throws Exception
     */
    public void save() throws Exception
    {
        save(TProjectPeer.DATABASE_NAME);
    }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     * Note: this code is here because the method body is
     * auto-generated conditionally and therefore needs to be
     * in this file instead of in the super class, BaseObject.
     *
     * @param dbName
     * @throws TorqueException
     */
    public void save(String dbName) throws TorqueException
    {
        Connection con = null;
        try
        {
            con = Transaction.begin(dbName);
            save(con);
            Transaction.commit(con);
        }
        catch(TorqueException e)
        {
            Transaction.safeRollback(con);
            throw e;
        }
    }

    /** flag to prevent endless save loop, if this object is referenced
        by another object which falls in this transaction. */
    private boolean alreadyInSave = false;
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.  This method
     * is meant to be used as part of a transaction, otherwise use
     * the save() method and the connection details will be handled
     * internally
     *
     * @param con
     * @throws TorqueException
     */
    public void save(Connection con) throws TorqueException
    {
        if (!alreadyInSave)
        {
            alreadyInSave = true;



            // If this object has been modified, then save it to the database.
            if (isModified())
            {
                if (isNew())
                {
                    TProjectPeer.doInsert((TProject) this, con);
                    setNew(false);
                }
                else
                {
                    TProjectPeer.doUpdate((TProject) this, con);
                }
            }


            if (collTAccessControlLists != null)
            {
                for (int i = 0; i < collTAccessControlLists.size(); i++)
                {
                    ((TAccessControlList) collTAccessControlLists.get(i)).save(con);
                }
            }

            if (collTClasss != null)
            {
                for (int i = 0; i < collTClasss.size(); i++)
                {
                    ((TClass) collTClasss.get(i)).save(con);
                }
            }

            if (collTProjectCategorys != null)
            {
                for (int i = 0; i < collTProjectCategorys.size(); i++)
                {
                    ((TProjectCategory) collTProjectCategorys.get(i)).save(con);
                }
            }

            if (collTReleases != null)
            {
                for (int i = 0; i < collTReleases.size(); i++)
                {
                    ((TRelease) collTReleases.get(i)).save(con);
                }
            }

            if (collTWorkItems != null)
            {
                for (int i = 0; i < collTWorkItems.size(); i++)
                {
                    ((TWorkItem) collTWorkItems.get(i)).save(con);
                }
            }

            if (collTProjectReportRepositorys != null)
            {
                for (int i = 0; i < collTProjectReportRepositorys.size(); i++)
                {
                    ((TProjectReportRepository) collTProjectReportRepositorys.get(i)).save(con);
                }
            }

            if (collTReportLayouts != null)
            {
                for (int i = 0; i < collTReportLayouts.size(); i++)
                {
                    ((TReportLayout) collTReportLayouts.get(i)).save(con);
                }
            }

            if (collTProjectAccounts != null)
            {
                for (int i = 0; i < collTProjectAccounts.size(); i++)
                {
                    ((TProjectAccount) collTProjectAccounts.get(i)).save(con);
                }
            }

            if (collTDashboardScreens != null)
            {
                for (int i = 0; i < collTDashboardScreens.size(); i++)
                {
                    ((TDashboardScreen) collTDashboardScreens.get(i)).save(con);
                }
            }

            if (collTVersionControlParameters != null)
            {
                for (int i = 0; i < collTVersionControlParameters.size(); i++)
                {
                    ((TVersionControlParameter) collTVersionControlParameters.get(i)).save(con);
                }
            }

            if (collTFields != null)
            {
                for (int i = 0; i < collTFields.size(); i++)
                {
                    ((TField) collTFields.get(i)).save(con);
                }
            }

            if (collTFieldConfigs != null)
            {
                for (int i = 0; i < collTFieldConfigs.size(); i++)
                {
                    ((TFieldConfig) collTFieldConfigs.get(i)).save(con);
                }
            }

            if (collTLists != null)
            {
                for (int i = 0; i < collTLists.size(); i++)
                {
                    ((TList) collTLists.get(i)).save(con);
                }
            }

            if (collTScreens != null)
            {
                for (int i = 0; i < collTScreens.size(); i++)
                {
                    ((TScreen) collTScreens.get(i)).save(con);
                }
            }

            if (collTScreenConfigs != null)
            {
                for (int i = 0; i < collTScreenConfigs.size(); i++)
                {
                    ((TScreenConfig) collTScreenConfigs.get(i)).save(con);
                }
            }

            if (collTInitStates != null)
            {
                for (int i = 0; i < collTInitStates.size(); i++)
                {
                    ((TInitState) collTInitStates.get(i)).save(con);
                }
            }

            if (collTEvents != null)
            {
                for (int i = 0; i < collTEvents.size(); i++)
                {
                    ((TEvent) collTEvents.get(i)).save(con);
                }
            }

            if (collTNotifySettingss != null)
            {
                for (int i = 0; i < collTNotifySettingss.size(); i++)
                {
                    ((TNotifySettings) collTNotifySettingss.get(i)).save(con);
                }
            }

            if (collTQueryRepositorys != null)
            {
                for (int i = 0; i < collTQueryRepositorys.size(); i++)
                {
                    ((TQueryRepository) collTQueryRepositorys.get(i)).save(con);
                }
            }

            if (collTExportTemplates != null)
            {
                for (int i = 0; i < collTExportTemplates.size(); i++)
                {
                    ((TExportTemplate) collTExportTemplates.get(i)).save(con);
                }
            }

            if (collTScriptss != null)
            {
                for (int i = 0; i < collTScriptss.size(); i++)
                {
                    ((TScripts) collTScriptss.get(i)).save(con);
                }
            }

            if (collTFilterCategorys != null)
            {
                for (int i = 0; i < collTFilterCategorys.size(); i++)
                {
                    ((TFilterCategory) collTFilterCategorys.get(i)).save(con);
                }
            }

            if (collTReportCategorys != null)
            {
                for (int i = 0; i < collTReportCategorys.size(); i++)
                {
                    ((TReportCategory) collTReportCategorys.get(i)).save(con);
                }
            }

            if (collTMailTemplateConfigs != null)
            {
                for (int i = 0; i < collTMailTemplateConfigs.size(); i++)
                {
                    ((TMailTemplateConfig) collTMailTemplateConfigs.get(i)).save(con);
                }
            }

            if (collTWfActivityContextParamss != null)
            {
                for (int i = 0; i < collTWfActivityContextParamss.size(); i++)
                {
                    ((TWfActivityContextParams) collTWfActivityContextParamss.get(i)).save(con);
                }
            }

            if (collTWorkflowConnects != null)
            {
                for (int i = 0; i < collTWorkflowConnects.size(); i++)
                {
                    ((TWorkflowConnect) collTWorkflowConnects.get(i)).save(con);
                }
            }

            if (collTOrgProjectSLAs != null)
            {
                for (int i = 0; i < collTOrgProjectSLAs.size(); i++)
                {
                    ((TOrgProjectSLA) collTOrgProjectSLAs.get(i)).save(con);
                }
            }

            if (collTMailTextBlocks != null)
            {
                for (int i = 0; i < collTMailTextBlocks.size(); i++)
                {
                    ((TMailTextBlock) collTMailTextBlocks.get(i)).save(con);
                }
            }
            alreadyInSave = false;
        }
    }


    /**
     * Set the PrimaryKey using ObjectKey.
     *
     * @param key objectID ObjectKey
     */
    public void setPrimaryKey(ObjectKey key)
        throws TorqueException
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
    {
        setObjectID(new Integer(key));
    }


    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
        return SimpleKey.keyFor(getObjectID());
    }
 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     */
    public TProject copy() throws TorqueException
    {
        return copy(true);
    }

    /**
     * Makes a copy of this object using connection.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     *
     * @param con the database connection to read associated objects.
     */
    public TProject copy(Connection con) throws TorqueException
    {
        return copy(true, con);
    }

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * If the parameter deepcopy is true, it then fills all the
     * association collections and sets the related objects to
     * isNew=true.
     *
     * @param deepcopy whether to copy the associated objects.
     */
    public TProject copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TProject(), deepcopy);
    }

    /**
     * Makes a copy of this object using connection.
     * It creates a new object filling in the simple attributes.
     * If the parameter deepcopy is true, it then fills all the
     * association collections and sets the related objects to
     * isNew=true.
     *
     * @param deepcopy whether to copy the associated objects.
     * @param con the database connection to read associated objects.
     */
    public TProject copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TProject(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TProject copyInto(TProject copyObj) throws TorqueException
    {
        return copyInto(copyObj, true);
    }

  
    /**
     * Fills the copyObj with the contents of this object using connection.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param con the database connection to read associated objects.
     */
    protected TProject copyInto(TProject copyObj, Connection con) throws TorqueException
    {
        return copyInto(copyObj, true, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * If deepcopy is true, The associated objects are also copied
     * and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param deepcopy whether the associated objects should be copied.
     */
    protected TProject copyInto(TProject copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setDefaultOwnerID(defaultOwnerID);
        copyObj.setDefaultManagerID(defaultManagerID);
        copyObj.setDefaultInitStateID(defaultInitStateID);
        copyObj.setProjectType(projectType);
        copyObj.setVersionSystemField0(versionSystemField0);
        copyObj.setVersionSystemField1(versionSystemField1);
        copyObj.setVersionSystemField2(versionSystemField2);
        copyObj.setVersionSystemField3(versionSystemField3);
        copyObj.setDeleted(deleted);
        copyObj.setStatus(status);
        copyObj.setCurrencyName(currencyName);
        copyObj.setCurrencySymbol(currencySymbol);
        copyObj.setHoursPerWorkDay(hoursPerWorkDay);
        copyObj.setMoreProps(moreProps);
        copyObj.setTagLabel(tagLabel);
        copyObj.setDescription(description);
        copyObj.setPrefix(prefix);
        copyObj.setNextItemID(nextItemID);
        copyObj.setLastID(lastID);
        copyObj.setParent(parent);
        copyObj.setSortorder(sortorder);
        copyObj.setIsPrivate(isPrivate);
        copyObj.setIsTemplate(isTemplate);
        copyObj.setDomain(domain);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TAccessControlList> vTAccessControlLists = getTAccessControlLists();
        if (vTAccessControlLists != null)
        {
            for (int i = 0; i < vTAccessControlLists.size(); i++)
            {
                TAccessControlList obj =  vTAccessControlLists.get(i);
                copyObj.addTAccessControlList(obj.copy());
            }
        }
        else
        {
            copyObj.collTAccessControlLists = null;
        }


        List<TClass> vTClasss = getTClasss();
        if (vTClasss != null)
        {
            for (int i = 0; i < vTClasss.size(); i++)
            {
                TClass obj =  vTClasss.get(i);
                copyObj.addTClass(obj.copy());
            }
        }
        else
        {
            copyObj.collTClasss = null;
        }


        List<TProjectCategory> vTProjectCategorys = getTProjectCategorys();
        if (vTProjectCategorys != null)
        {
            for (int i = 0; i < vTProjectCategorys.size(); i++)
            {
                TProjectCategory obj =  vTProjectCategorys.get(i);
                copyObj.addTProjectCategory(obj.copy());
            }
        }
        else
        {
            copyObj.collTProjectCategorys = null;
        }


        List<TRelease> vTReleases = getTReleases();
        if (vTReleases != null)
        {
            for (int i = 0; i < vTReleases.size(); i++)
            {
                TRelease obj =  vTReleases.get(i);
                copyObj.addTRelease(obj.copy());
            }
        }
        else
        {
            copyObj.collTReleases = null;
        }


        List<TWorkItem> vTWorkItems = getTWorkItems();
        if (vTWorkItems != null)
        {
            for (int i = 0; i < vTWorkItems.size(); i++)
            {
                TWorkItem obj =  vTWorkItems.get(i);
                copyObj.addTWorkItem(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkItems = null;
        }


        List<TProjectReportRepository> vTProjectReportRepositorys = getTProjectReportRepositorys();
        if (vTProjectReportRepositorys != null)
        {
            for (int i = 0; i < vTProjectReportRepositorys.size(); i++)
            {
                TProjectReportRepository obj =  vTProjectReportRepositorys.get(i);
                copyObj.addTProjectReportRepository(obj.copy());
            }
        }
        else
        {
            copyObj.collTProjectReportRepositorys = null;
        }


        List<TReportLayout> vTReportLayouts = getTReportLayouts();
        if (vTReportLayouts != null)
        {
            for (int i = 0; i < vTReportLayouts.size(); i++)
            {
                TReportLayout obj =  vTReportLayouts.get(i);
                copyObj.addTReportLayout(obj.copy());
            }
        }
        else
        {
            copyObj.collTReportLayouts = null;
        }


        List<TProjectAccount> vTProjectAccounts = getTProjectAccounts();
        if (vTProjectAccounts != null)
        {
            for (int i = 0; i < vTProjectAccounts.size(); i++)
            {
                TProjectAccount obj =  vTProjectAccounts.get(i);
                copyObj.addTProjectAccount(obj.copy());
            }
        }
        else
        {
            copyObj.collTProjectAccounts = null;
        }


        List<TDashboardScreen> vTDashboardScreens = getTDashboardScreens();
        if (vTDashboardScreens != null)
        {
            for (int i = 0; i < vTDashboardScreens.size(); i++)
            {
                TDashboardScreen obj =  vTDashboardScreens.get(i);
                copyObj.addTDashboardScreen(obj.copy());
            }
        }
        else
        {
            copyObj.collTDashboardScreens = null;
        }


        List<TVersionControlParameter> vTVersionControlParameters = getTVersionControlParameters();
        if (vTVersionControlParameters != null)
        {
            for (int i = 0; i < vTVersionControlParameters.size(); i++)
            {
                TVersionControlParameter obj =  vTVersionControlParameters.get(i);
                copyObj.addTVersionControlParameter(obj.copy());
            }
        }
        else
        {
            copyObj.collTVersionControlParameters = null;
        }


        List<TField> vTFields = getTFields();
        if (vTFields != null)
        {
            for (int i = 0; i < vTFields.size(); i++)
            {
                TField obj =  vTFields.get(i);
                copyObj.addTField(obj.copy());
            }
        }
        else
        {
            copyObj.collTFields = null;
        }


        List<TFieldConfig> vTFieldConfigs = getTFieldConfigs();
        if (vTFieldConfigs != null)
        {
            for (int i = 0; i < vTFieldConfigs.size(); i++)
            {
                TFieldConfig obj =  vTFieldConfigs.get(i);
                copyObj.addTFieldConfig(obj.copy());
            }
        }
        else
        {
            copyObj.collTFieldConfigs = null;
        }


        List<TList> vTLists = getTLists();
        if (vTLists != null)
        {
            for (int i = 0; i < vTLists.size(); i++)
            {
                TList obj =  vTLists.get(i);
                copyObj.addTList(obj.copy());
            }
        }
        else
        {
            copyObj.collTLists = null;
        }


        List<TScreen> vTScreens = getTScreens();
        if (vTScreens != null)
        {
            for (int i = 0; i < vTScreens.size(); i++)
            {
                TScreen obj =  vTScreens.get(i);
                copyObj.addTScreen(obj.copy());
            }
        }
        else
        {
            copyObj.collTScreens = null;
        }


        List<TScreenConfig> vTScreenConfigs = getTScreenConfigs();
        if (vTScreenConfigs != null)
        {
            for (int i = 0; i < vTScreenConfigs.size(); i++)
            {
                TScreenConfig obj =  vTScreenConfigs.get(i);
                copyObj.addTScreenConfig(obj.copy());
            }
        }
        else
        {
            copyObj.collTScreenConfigs = null;
        }


        List<TInitState> vTInitStates = getTInitStates();
        if (vTInitStates != null)
        {
            for (int i = 0; i < vTInitStates.size(); i++)
            {
                TInitState obj =  vTInitStates.get(i);
                copyObj.addTInitState(obj.copy());
            }
        }
        else
        {
            copyObj.collTInitStates = null;
        }


        List<TEvent> vTEvents = getTEvents();
        if (vTEvents != null)
        {
            for (int i = 0; i < vTEvents.size(); i++)
            {
                TEvent obj =  vTEvents.get(i);
                copyObj.addTEvent(obj.copy());
            }
        }
        else
        {
            copyObj.collTEvents = null;
        }


        List<TNotifySettings> vTNotifySettingss = getTNotifySettingss();
        if (vTNotifySettingss != null)
        {
            for (int i = 0; i < vTNotifySettingss.size(); i++)
            {
                TNotifySettings obj =  vTNotifySettingss.get(i);
                copyObj.addTNotifySettings(obj.copy());
            }
        }
        else
        {
            copyObj.collTNotifySettingss = null;
        }


        List<TQueryRepository> vTQueryRepositorys = getTQueryRepositorys();
        if (vTQueryRepositorys != null)
        {
            for (int i = 0; i < vTQueryRepositorys.size(); i++)
            {
                TQueryRepository obj =  vTQueryRepositorys.get(i);
                copyObj.addTQueryRepository(obj.copy());
            }
        }
        else
        {
            copyObj.collTQueryRepositorys = null;
        }


        List<TExportTemplate> vTExportTemplates = getTExportTemplates();
        if (vTExportTemplates != null)
        {
            for (int i = 0; i < vTExportTemplates.size(); i++)
            {
                TExportTemplate obj =  vTExportTemplates.get(i);
                copyObj.addTExportTemplate(obj.copy());
            }
        }
        else
        {
            copyObj.collTExportTemplates = null;
        }


        List<TScripts> vTScriptss = getTScriptss();
        if (vTScriptss != null)
        {
            for (int i = 0; i < vTScriptss.size(); i++)
            {
                TScripts obj =  vTScriptss.get(i);
                copyObj.addTScripts(obj.copy());
            }
        }
        else
        {
            copyObj.collTScriptss = null;
        }


        List<TFilterCategory> vTFilterCategorys = getTFilterCategorys();
        if (vTFilterCategorys != null)
        {
            for (int i = 0; i < vTFilterCategorys.size(); i++)
            {
                TFilterCategory obj =  vTFilterCategorys.get(i);
                copyObj.addTFilterCategory(obj.copy());
            }
        }
        else
        {
            copyObj.collTFilterCategorys = null;
        }


        List<TReportCategory> vTReportCategorys = getTReportCategorys();
        if (vTReportCategorys != null)
        {
            for (int i = 0; i < vTReportCategorys.size(); i++)
            {
                TReportCategory obj =  vTReportCategorys.get(i);
                copyObj.addTReportCategory(obj.copy());
            }
        }
        else
        {
            copyObj.collTReportCategorys = null;
        }


        List<TMailTemplateConfig> vTMailTemplateConfigs = getTMailTemplateConfigs();
        if (vTMailTemplateConfigs != null)
        {
            for (int i = 0; i < vTMailTemplateConfigs.size(); i++)
            {
                TMailTemplateConfig obj =  vTMailTemplateConfigs.get(i);
                copyObj.addTMailTemplateConfig(obj.copy());
            }
        }
        else
        {
            copyObj.collTMailTemplateConfigs = null;
        }


        List<TWfActivityContextParams> vTWfActivityContextParamss = getTWfActivityContextParamss();
        if (vTWfActivityContextParamss != null)
        {
            for (int i = 0; i < vTWfActivityContextParamss.size(); i++)
            {
                TWfActivityContextParams obj =  vTWfActivityContextParamss.get(i);
                copyObj.addTWfActivityContextParams(obj.copy());
            }
        }
        else
        {
            copyObj.collTWfActivityContextParamss = null;
        }


        List<TWorkflowConnect> vTWorkflowConnects = getTWorkflowConnects();
        if (vTWorkflowConnects != null)
        {
            for (int i = 0; i < vTWorkflowConnects.size(); i++)
            {
                TWorkflowConnect obj =  vTWorkflowConnects.get(i);
                copyObj.addTWorkflowConnect(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowConnects = null;
        }


        List<TOrgProjectSLA> vTOrgProjectSLAs = getTOrgProjectSLAs();
        if (vTOrgProjectSLAs != null)
        {
            for (int i = 0; i < vTOrgProjectSLAs.size(); i++)
            {
                TOrgProjectSLA obj =  vTOrgProjectSLAs.get(i);
                copyObj.addTOrgProjectSLA(obj.copy());
            }
        }
        else
        {
            copyObj.collTOrgProjectSLAs = null;
        }


        List<TMailTextBlock> vTMailTextBlocks = getTMailTextBlocks();
        if (vTMailTextBlocks != null)
        {
            for (int i = 0; i < vTMailTextBlocks.size(); i++)
            {
                TMailTextBlock obj =  vTMailTextBlocks.get(i);
                copyObj.addTMailTextBlock(obj.copy());
            }
        }
        else
        {
            copyObj.collTMailTextBlocks = null;
        }
        }
        return copyObj;
    }
        
    
    /**
     * Fills the copyObj with the contents of this object using connection.
     * If deepcopy is true, The associated objects are also copied
     * and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param deepcopy whether the associated objects should be copied.
     * @param con the database connection to read associated objects.
     */
    protected TProject copyInto(TProject copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setDefaultOwnerID(defaultOwnerID);
        copyObj.setDefaultManagerID(defaultManagerID);
        copyObj.setDefaultInitStateID(defaultInitStateID);
        copyObj.setProjectType(projectType);
        copyObj.setVersionSystemField0(versionSystemField0);
        copyObj.setVersionSystemField1(versionSystemField1);
        copyObj.setVersionSystemField2(versionSystemField2);
        copyObj.setVersionSystemField3(versionSystemField3);
        copyObj.setDeleted(deleted);
        copyObj.setStatus(status);
        copyObj.setCurrencyName(currencyName);
        copyObj.setCurrencySymbol(currencySymbol);
        copyObj.setHoursPerWorkDay(hoursPerWorkDay);
        copyObj.setMoreProps(moreProps);
        copyObj.setTagLabel(tagLabel);
        copyObj.setDescription(description);
        copyObj.setPrefix(prefix);
        copyObj.setNextItemID(nextItemID);
        copyObj.setLastID(lastID);
        copyObj.setParent(parent);
        copyObj.setSortorder(sortorder);
        copyObj.setIsPrivate(isPrivate);
        copyObj.setIsTemplate(isTemplate);
        copyObj.setDomain(domain);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TAccessControlList> vTAccessControlLists = getTAccessControlLists(con);
        if (vTAccessControlLists != null)
        {
            for (int i = 0; i < vTAccessControlLists.size(); i++)
            {
                TAccessControlList obj =  vTAccessControlLists.get(i);
                copyObj.addTAccessControlList(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAccessControlLists = null;
        }


        List<TClass> vTClasss = getTClasss(con);
        if (vTClasss != null)
        {
            for (int i = 0; i < vTClasss.size(); i++)
            {
                TClass obj =  vTClasss.get(i);
                copyObj.addTClass(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTClasss = null;
        }


        List<TProjectCategory> vTProjectCategorys = getTProjectCategorys(con);
        if (vTProjectCategorys != null)
        {
            for (int i = 0; i < vTProjectCategorys.size(); i++)
            {
                TProjectCategory obj =  vTProjectCategorys.get(i);
                copyObj.addTProjectCategory(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTProjectCategorys = null;
        }


        List<TRelease> vTReleases = getTReleases(con);
        if (vTReleases != null)
        {
            for (int i = 0; i < vTReleases.size(); i++)
            {
                TRelease obj =  vTReleases.get(i);
                copyObj.addTRelease(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTReleases = null;
        }


        List<TWorkItem> vTWorkItems = getTWorkItems(con);
        if (vTWorkItems != null)
        {
            for (int i = 0; i < vTWorkItems.size(); i++)
            {
                TWorkItem obj =  vTWorkItems.get(i);
                copyObj.addTWorkItem(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkItems = null;
        }


        List<TProjectReportRepository> vTProjectReportRepositorys = getTProjectReportRepositorys(con);
        if (vTProjectReportRepositorys != null)
        {
            for (int i = 0; i < vTProjectReportRepositorys.size(); i++)
            {
                TProjectReportRepository obj =  vTProjectReportRepositorys.get(i);
                copyObj.addTProjectReportRepository(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTProjectReportRepositorys = null;
        }


        List<TReportLayout> vTReportLayouts = getTReportLayouts(con);
        if (vTReportLayouts != null)
        {
            for (int i = 0; i < vTReportLayouts.size(); i++)
            {
                TReportLayout obj =  vTReportLayouts.get(i);
                copyObj.addTReportLayout(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTReportLayouts = null;
        }


        List<TProjectAccount> vTProjectAccounts = getTProjectAccounts(con);
        if (vTProjectAccounts != null)
        {
            for (int i = 0; i < vTProjectAccounts.size(); i++)
            {
                TProjectAccount obj =  vTProjectAccounts.get(i);
                copyObj.addTProjectAccount(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTProjectAccounts = null;
        }


        List<TDashboardScreen> vTDashboardScreens = getTDashboardScreens(con);
        if (vTDashboardScreens != null)
        {
            for (int i = 0; i < vTDashboardScreens.size(); i++)
            {
                TDashboardScreen obj =  vTDashboardScreens.get(i);
                copyObj.addTDashboardScreen(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTDashboardScreens = null;
        }


        List<TVersionControlParameter> vTVersionControlParameters = getTVersionControlParameters(con);
        if (vTVersionControlParameters != null)
        {
            for (int i = 0; i < vTVersionControlParameters.size(); i++)
            {
                TVersionControlParameter obj =  vTVersionControlParameters.get(i);
                copyObj.addTVersionControlParameter(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTVersionControlParameters = null;
        }


        List<TField> vTFields = getTFields(con);
        if (vTFields != null)
        {
            for (int i = 0; i < vTFields.size(); i++)
            {
                TField obj =  vTFields.get(i);
                copyObj.addTField(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTFields = null;
        }


        List<TFieldConfig> vTFieldConfigs = getTFieldConfigs(con);
        if (vTFieldConfigs != null)
        {
            for (int i = 0; i < vTFieldConfigs.size(); i++)
            {
                TFieldConfig obj =  vTFieldConfigs.get(i);
                copyObj.addTFieldConfig(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTFieldConfigs = null;
        }


        List<TList> vTLists = getTLists(con);
        if (vTLists != null)
        {
            for (int i = 0; i < vTLists.size(); i++)
            {
                TList obj =  vTLists.get(i);
                copyObj.addTList(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTLists = null;
        }


        List<TScreen> vTScreens = getTScreens(con);
        if (vTScreens != null)
        {
            for (int i = 0; i < vTScreens.size(); i++)
            {
                TScreen obj =  vTScreens.get(i);
                copyObj.addTScreen(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScreens = null;
        }


        List<TScreenConfig> vTScreenConfigs = getTScreenConfigs(con);
        if (vTScreenConfigs != null)
        {
            for (int i = 0; i < vTScreenConfigs.size(); i++)
            {
                TScreenConfig obj =  vTScreenConfigs.get(i);
                copyObj.addTScreenConfig(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScreenConfigs = null;
        }


        List<TInitState> vTInitStates = getTInitStates(con);
        if (vTInitStates != null)
        {
            for (int i = 0; i < vTInitStates.size(); i++)
            {
                TInitState obj =  vTInitStates.get(i);
                copyObj.addTInitState(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTInitStates = null;
        }


        List<TEvent> vTEvents = getTEvents(con);
        if (vTEvents != null)
        {
            for (int i = 0; i < vTEvents.size(); i++)
            {
                TEvent obj =  vTEvents.get(i);
                copyObj.addTEvent(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTEvents = null;
        }


        List<TNotifySettings> vTNotifySettingss = getTNotifySettingss(con);
        if (vTNotifySettingss != null)
        {
            for (int i = 0; i < vTNotifySettingss.size(); i++)
            {
                TNotifySettings obj =  vTNotifySettingss.get(i);
                copyObj.addTNotifySettings(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTNotifySettingss = null;
        }


        List<TQueryRepository> vTQueryRepositorys = getTQueryRepositorys(con);
        if (vTQueryRepositorys != null)
        {
            for (int i = 0; i < vTQueryRepositorys.size(); i++)
            {
                TQueryRepository obj =  vTQueryRepositorys.get(i);
                copyObj.addTQueryRepository(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTQueryRepositorys = null;
        }


        List<TExportTemplate> vTExportTemplates = getTExportTemplates(con);
        if (vTExportTemplates != null)
        {
            for (int i = 0; i < vTExportTemplates.size(); i++)
            {
                TExportTemplate obj =  vTExportTemplates.get(i);
                copyObj.addTExportTemplate(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTExportTemplates = null;
        }


        List<TScripts> vTScriptss = getTScriptss(con);
        if (vTScriptss != null)
        {
            for (int i = 0; i < vTScriptss.size(); i++)
            {
                TScripts obj =  vTScriptss.get(i);
                copyObj.addTScripts(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScriptss = null;
        }


        List<TFilterCategory> vTFilterCategorys = getTFilterCategorys(con);
        if (vTFilterCategorys != null)
        {
            for (int i = 0; i < vTFilterCategorys.size(); i++)
            {
                TFilterCategory obj =  vTFilterCategorys.get(i);
                copyObj.addTFilterCategory(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTFilterCategorys = null;
        }


        List<TReportCategory> vTReportCategorys = getTReportCategorys(con);
        if (vTReportCategorys != null)
        {
            for (int i = 0; i < vTReportCategorys.size(); i++)
            {
                TReportCategory obj =  vTReportCategorys.get(i);
                copyObj.addTReportCategory(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTReportCategorys = null;
        }


        List<TMailTemplateConfig> vTMailTemplateConfigs = getTMailTemplateConfigs(con);
        if (vTMailTemplateConfigs != null)
        {
            for (int i = 0; i < vTMailTemplateConfigs.size(); i++)
            {
                TMailTemplateConfig obj =  vTMailTemplateConfigs.get(i);
                copyObj.addTMailTemplateConfig(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTMailTemplateConfigs = null;
        }


        List<TWfActivityContextParams> vTWfActivityContextParamss = getTWfActivityContextParamss(con);
        if (vTWfActivityContextParamss != null)
        {
            for (int i = 0; i < vTWfActivityContextParamss.size(); i++)
            {
                TWfActivityContextParams obj =  vTWfActivityContextParamss.get(i);
                copyObj.addTWfActivityContextParams(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWfActivityContextParamss = null;
        }


        List<TWorkflowConnect> vTWorkflowConnects = getTWorkflowConnects(con);
        if (vTWorkflowConnects != null)
        {
            for (int i = 0; i < vTWorkflowConnects.size(); i++)
            {
                TWorkflowConnect obj =  vTWorkflowConnects.get(i);
                copyObj.addTWorkflowConnect(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowConnects = null;
        }


        List<TOrgProjectSLA> vTOrgProjectSLAs = getTOrgProjectSLAs(con);
        if (vTOrgProjectSLAs != null)
        {
            for (int i = 0; i < vTOrgProjectSLAs.size(); i++)
            {
                TOrgProjectSLA obj =  vTOrgProjectSLAs.get(i);
                copyObj.addTOrgProjectSLA(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTOrgProjectSLAs = null;
        }


        List<TMailTextBlock> vTMailTextBlocks = getTMailTextBlocks(con);
        if (vTMailTextBlocks != null)
        {
            for (int i = 0; i < vTMailTextBlocks.size(); i++)
            {
                TMailTextBlock obj =  vTMailTextBlocks.get(i);
                copyObj.addTMailTextBlock(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTMailTextBlocks = null;
        }
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TProjectPeer getPeer()
    {
        return peer;
    }

    /**
     * Retrieves the TableMap object related to this Table data without
     * compiler warnings of using getPeer().getTableMap().
     *
     * @return The associated TableMap object.
     */
    public TableMap getTableMap() throws TorqueException
    {
        return TProjectPeer.getTableMap();
    }

  
    /**
     * Creates a TProjectBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TProjectBean with the contents of this object
     */
    public TProjectBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TProjectBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TProjectBean with the contents of this object
     */
    public TProjectBean getBean(IdentityMap createdBeans)
    {
        TProjectBean result = (TProjectBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TProjectBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setDefaultOwnerID(getDefaultOwnerID());
        result.setDefaultManagerID(getDefaultManagerID());
        result.setDefaultInitStateID(getDefaultInitStateID());
        result.setProjectType(getProjectType());
        result.setVersionSystemField0(getVersionSystemField0());
        result.setVersionSystemField1(getVersionSystemField1());
        result.setVersionSystemField2(getVersionSystemField2());
        result.setVersionSystemField3(getVersionSystemField3());
        result.setDeleted(getDeleted());
        result.setStatus(getStatus());
        result.setCurrencyName(getCurrencyName());
        result.setCurrencySymbol(getCurrencySymbol());
        result.setHoursPerWorkDay(getHoursPerWorkDay());
        result.setMoreProps(getMoreProps());
        result.setTagLabel(getTagLabel());
        result.setDescription(getDescription());
        result.setPrefix(getPrefix());
        result.setNextItemID(getNextItemID());
        result.setLastID(getLastID());
        result.setParent(getParent());
        result.setSortorder(getSortorder());
        result.setIsPrivate(getIsPrivate());
        result.setIsTemplate(getIsTemplate());
        result.setDomain(getDomain());
        result.setUuid(getUuid());



        if (collTAccessControlLists != null)
        {
            List<TAccessControlListBean> relatedBeans = new ArrayList<TAccessControlListBean>(collTAccessControlLists.size());
            for (Iterator<TAccessControlList> collTAccessControlListsIt = collTAccessControlLists.iterator(); collTAccessControlListsIt.hasNext(); )
            {
                TAccessControlList related = (TAccessControlList) collTAccessControlListsIt.next();
                TAccessControlListBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAccessControlListBeans(relatedBeans);
        }


        if (collTClasss != null)
        {
            List<TClassBean> relatedBeans = new ArrayList<TClassBean>(collTClasss.size());
            for (Iterator<TClass> collTClasssIt = collTClasss.iterator(); collTClasssIt.hasNext(); )
            {
                TClass related = (TClass) collTClasssIt.next();
                TClassBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTClassBeans(relatedBeans);
        }


        if (collTProjectCategorys != null)
        {
            List<TProjectCategoryBean> relatedBeans = new ArrayList<TProjectCategoryBean>(collTProjectCategorys.size());
            for (Iterator<TProjectCategory> collTProjectCategorysIt = collTProjectCategorys.iterator(); collTProjectCategorysIt.hasNext(); )
            {
                TProjectCategory related = (TProjectCategory) collTProjectCategorysIt.next();
                TProjectCategoryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTProjectCategoryBeans(relatedBeans);
        }


        if (collTReleases != null)
        {
            List<TReleaseBean> relatedBeans = new ArrayList<TReleaseBean>(collTReleases.size());
            for (Iterator<TRelease> collTReleasesIt = collTReleases.iterator(); collTReleasesIt.hasNext(); )
            {
                TRelease related = (TRelease) collTReleasesIt.next();
                TReleaseBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTReleaseBeans(relatedBeans);
        }


        if (collTWorkItems != null)
        {
            List<TWorkItemBean> relatedBeans = new ArrayList<TWorkItemBean>(collTWorkItems.size());
            for (Iterator<TWorkItem> collTWorkItemsIt = collTWorkItems.iterator(); collTWorkItemsIt.hasNext(); )
            {
                TWorkItem related = (TWorkItem) collTWorkItemsIt.next();
                TWorkItemBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkItemBeans(relatedBeans);
        }


        if (collTProjectReportRepositorys != null)
        {
            List<TProjectReportRepositoryBean> relatedBeans = new ArrayList<TProjectReportRepositoryBean>(collTProjectReportRepositorys.size());
            for (Iterator<TProjectReportRepository> collTProjectReportRepositorysIt = collTProjectReportRepositorys.iterator(); collTProjectReportRepositorysIt.hasNext(); )
            {
                TProjectReportRepository related = (TProjectReportRepository) collTProjectReportRepositorysIt.next();
                TProjectReportRepositoryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTProjectReportRepositoryBeans(relatedBeans);
        }


        if (collTReportLayouts != null)
        {
            List<TReportLayoutBean> relatedBeans = new ArrayList<TReportLayoutBean>(collTReportLayouts.size());
            for (Iterator<TReportLayout> collTReportLayoutsIt = collTReportLayouts.iterator(); collTReportLayoutsIt.hasNext(); )
            {
                TReportLayout related = (TReportLayout) collTReportLayoutsIt.next();
                TReportLayoutBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTReportLayoutBeans(relatedBeans);
        }


        if (collTProjectAccounts != null)
        {
            List<TProjectAccountBean> relatedBeans = new ArrayList<TProjectAccountBean>(collTProjectAccounts.size());
            for (Iterator<TProjectAccount> collTProjectAccountsIt = collTProjectAccounts.iterator(); collTProjectAccountsIt.hasNext(); )
            {
                TProjectAccount related = (TProjectAccount) collTProjectAccountsIt.next();
                TProjectAccountBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTProjectAccountBeans(relatedBeans);
        }


        if (collTDashboardScreens != null)
        {
            List<TDashboardScreenBean> relatedBeans = new ArrayList<TDashboardScreenBean>(collTDashboardScreens.size());
            for (Iterator<TDashboardScreen> collTDashboardScreensIt = collTDashboardScreens.iterator(); collTDashboardScreensIt.hasNext(); )
            {
                TDashboardScreen related = (TDashboardScreen) collTDashboardScreensIt.next();
                TDashboardScreenBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTDashboardScreenBeans(relatedBeans);
        }


        if (collTVersionControlParameters != null)
        {
            List<TVersionControlParameterBean> relatedBeans = new ArrayList<TVersionControlParameterBean>(collTVersionControlParameters.size());
            for (Iterator<TVersionControlParameter> collTVersionControlParametersIt = collTVersionControlParameters.iterator(); collTVersionControlParametersIt.hasNext(); )
            {
                TVersionControlParameter related = (TVersionControlParameter) collTVersionControlParametersIt.next();
                TVersionControlParameterBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTVersionControlParameterBeans(relatedBeans);
        }


        if (collTFields != null)
        {
            List<TFieldBean> relatedBeans = new ArrayList<TFieldBean>(collTFields.size());
            for (Iterator<TField> collTFieldsIt = collTFields.iterator(); collTFieldsIt.hasNext(); )
            {
                TField related = (TField) collTFieldsIt.next();
                TFieldBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTFieldBeans(relatedBeans);
        }


        if (collTFieldConfigs != null)
        {
            List<TFieldConfigBean> relatedBeans = new ArrayList<TFieldConfigBean>(collTFieldConfigs.size());
            for (Iterator<TFieldConfig> collTFieldConfigsIt = collTFieldConfigs.iterator(); collTFieldConfigsIt.hasNext(); )
            {
                TFieldConfig related = (TFieldConfig) collTFieldConfigsIt.next();
                TFieldConfigBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTFieldConfigBeans(relatedBeans);
        }


        if (collTLists != null)
        {
            List<TListBean> relatedBeans = new ArrayList<TListBean>(collTLists.size());
            for (Iterator<TList> collTListsIt = collTLists.iterator(); collTListsIt.hasNext(); )
            {
                TList related = (TList) collTListsIt.next();
                TListBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTListBeans(relatedBeans);
        }


        if (collTScreens != null)
        {
            List<TScreenBean> relatedBeans = new ArrayList<TScreenBean>(collTScreens.size());
            for (Iterator<TScreen> collTScreensIt = collTScreens.iterator(); collTScreensIt.hasNext(); )
            {
                TScreen related = (TScreen) collTScreensIt.next();
                TScreenBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScreenBeans(relatedBeans);
        }


        if (collTScreenConfigs != null)
        {
            List<TScreenConfigBean> relatedBeans = new ArrayList<TScreenConfigBean>(collTScreenConfigs.size());
            for (Iterator<TScreenConfig> collTScreenConfigsIt = collTScreenConfigs.iterator(); collTScreenConfigsIt.hasNext(); )
            {
                TScreenConfig related = (TScreenConfig) collTScreenConfigsIt.next();
                TScreenConfigBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScreenConfigBeans(relatedBeans);
        }


        if (collTInitStates != null)
        {
            List<TInitStateBean> relatedBeans = new ArrayList<TInitStateBean>(collTInitStates.size());
            for (Iterator<TInitState> collTInitStatesIt = collTInitStates.iterator(); collTInitStatesIt.hasNext(); )
            {
                TInitState related = (TInitState) collTInitStatesIt.next();
                TInitStateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTInitStateBeans(relatedBeans);
        }


        if (collTEvents != null)
        {
            List<TEventBean> relatedBeans = new ArrayList<TEventBean>(collTEvents.size());
            for (Iterator<TEvent> collTEventsIt = collTEvents.iterator(); collTEventsIt.hasNext(); )
            {
                TEvent related = (TEvent) collTEventsIt.next();
                TEventBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTEventBeans(relatedBeans);
        }


        if (collTNotifySettingss != null)
        {
            List<TNotifySettingsBean> relatedBeans = new ArrayList<TNotifySettingsBean>(collTNotifySettingss.size());
            for (Iterator<TNotifySettings> collTNotifySettingssIt = collTNotifySettingss.iterator(); collTNotifySettingssIt.hasNext(); )
            {
                TNotifySettings related = (TNotifySettings) collTNotifySettingssIt.next();
                TNotifySettingsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTNotifySettingsBeans(relatedBeans);
        }


        if (collTQueryRepositorys != null)
        {
            List<TQueryRepositoryBean> relatedBeans = new ArrayList<TQueryRepositoryBean>(collTQueryRepositorys.size());
            for (Iterator<TQueryRepository> collTQueryRepositorysIt = collTQueryRepositorys.iterator(); collTQueryRepositorysIt.hasNext(); )
            {
                TQueryRepository related = (TQueryRepository) collTQueryRepositorysIt.next();
                TQueryRepositoryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTQueryRepositoryBeans(relatedBeans);
        }


        if (collTExportTemplates != null)
        {
            List<TExportTemplateBean> relatedBeans = new ArrayList<TExportTemplateBean>(collTExportTemplates.size());
            for (Iterator<TExportTemplate> collTExportTemplatesIt = collTExportTemplates.iterator(); collTExportTemplatesIt.hasNext(); )
            {
                TExportTemplate related = (TExportTemplate) collTExportTemplatesIt.next();
                TExportTemplateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTExportTemplateBeans(relatedBeans);
        }


        if (collTScriptss != null)
        {
            List<TScriptsBean> relatedBeans = new ArrayList<TScriptsBean>(collTScriptss.size());
            for (Iterator<TScripts> collTScriptssIt = collTScriptss.iterator(); collTScriptssIt.hasNext(); )
            {
                TScripts related = (TScripts) collTScriptssIt.next();
                TScriptsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScriptsBeans(relatedBeans);
        }


        if (collTFilterCategorys != null)
        {
            List<TFilterCategoryBean> relatedBeans = new ArrayList<TFilterCategoryBean>(collTFilterCategorys.size());
            for (Iterator<TFilterCategory> collTFilterCategorysIt = collTFilterCategorys.iterator(); collTFilterCategorysIt.hasNext(); )
            {
                TFilterCategory related = (TFilterCategory) collTFilterCategorysIt.next();
                TFilterCategoryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTFilterCategoryBeans(relatedBeans);
        }


        if (collTReportCategorys != null)
        {
            List<TReportCategoryBean> relatedBeans = new ArrayList<TReportCategoryBean>(collTReportCategorys.size());
            for (Iterator<TReportCategory> collTReportCategorysIt = collTReportCategorys.iterator(); collTReportCategorysIt.hasNext(); )
            {
                TReportCategory related = (TReportCategory) collTReportCategorysIt.next();
                TReportCategoryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTReportCategoryBeans(relatedBeans);
        }


        if (collTMailTemplateConfigs != null)
        {
            List<TMailTemplateConfigBean> relatedBeans = new ArrayList<TMailTemplateConfigBean>(collTMailTemplateConfigs.size());
            for (Iterator<TMailTemplateConfig> collTMailTemplateConfigsIt = collTMailTemplateConfigs.iterator(); collTMailTemplateConfigsIt.hasNext(); )
            {
                TMailTemplateConfig related = (TMailTemplateConfig) collTMailTemplateConfigsIt.next();
                TMailTemplateConfigBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTMailTemplateConfigBeans(relatedBeans);
        }


        if (collTWfActivityContextParamss != null)
        {
            List<TWfActivityContextParamsBean> relatedBeans = new ArrayList<TWfActivityContextParamsBean>(collTWfActivityContextParamss.size());
            for (Iterator<TWfActivityContextParams> collTWfActivityContextParamssIt = collTWfActivityContextParamss.iterator(); collTWfActivityContextParamssIt.hasNext(); )
            {
                TWfActivityContextParams related = (TWfActivityContextParams) collTWfActivityContextParamssIt.next();
                TWfActivityContextParamsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWfActivityContextParamsBeans(relatedBeans);
        }


        if (collTWorkflowConnects != null)
        {
            List<TWorkflowConnectBean> relatedBeans = new ArrayList<TWorkflowConnectBean>(collTWorkflowConnects.size());
            for (Iterator<TWorkflowConnect> collTWorkflowConnectsIt = collTWorkflowConnects.iterator(); collTWorkflowConnectsIt.hasNext(); )
            {
                TWorkflowConnect related = (TWorkflowConnect) collTWorkflowConnectsIt.next();
                TWorkflowConnectBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowConnectBeans(relatedBeans);
        }


        if (collTOrgProjectSLAs != null)
        {
            List<TOrgProjectSLABean> relatedBeans = new ArrayList<TOrgProjectSLABean>(collTOrgProjectSLAs.size());
            for (Iterator<TOrgProjectSLA> collTOrgProjectSLAsIt = collTOrgProjectSLAs.iterator(); collTOrgProjectSLAsIt.hasNext(); )
            {
                TOrgProjectSLA related = (TOrgProjectSLA) collTOrgProjectSLAsIt.next();
                TOrgProjectSLABean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTOrgProjectSLABeans(relatedBeans);
        }


        if (collTMailTextBlocks != null)
        {
            List<TMailTextBlockBean> relatedBeans = new ArrayList<TMailTextBlockBean>(collTMailTextBlocks.size());
            for (Iterator<TMailTextBlock> collTMailTextBlocksIt = collTMailTextBlocks.iterator(); collTMailTextBlocksIt.hasNext(); )
            {
                TMailTextBlock related = (TMailTextBlock) collTMailTextBlocksIt.next();
                TMailTextBlockBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTMailTextBlockBeans(relatedBeans);
        }




        if (aTPersonRelatedByDefaultOwnerID != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByDefaultOwnerID.getBean(createdBeans);
            result.setTPersonBeanRelatedByDefaultOwnerID(relatedBean);
        }



        if (aTPersonRelatedByDefaultManagerID != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByDefaultManagerID.getBean(createdBeans);
            result.setTPersonBeanRelatedByDefaultManagerID(relatedBean);
        }



        if (aTState != null)
        {
            TStateBean relatedBean = aTState.getBean(createdBeans);
            result.setTStateBean(relatedBean);
        }



        if (aTProjectType != null)
        {
            TProjectTypeBean relatedBean = aTProjectType.getBean(createdBeans);
            result.setTProjectTypeBean(relatedBean);
        }



        if (aTSystemState != null)
        {
            TSystemStateBean relatedBean = aTSystemState.getBean(createdBeans);
            result.setTSystemStateBean(relatedBean);
        }



        if (aTProjectRelatedByParent != null)
        {
            TProjectBean relatedBean = aTProjectRelatedByParent.getBean(createdBeans);
            result.setTProjectBeanRelatedByParent(relatedBean);
        }



        if (aTDomain != null)
        {
            TDomainBean relatedBean = aTDomain.getBean(createdBeans);
            result.setTDomainBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TProject with the contents
     * of a TProjectBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TProjectBean which contents are used to create
     *        the resulting class
     * @return an instance of TProject with the contents of bean
     */
    public static TProject createTProject(TProjectBean bean)
        throws TorqueException
    {
        return createTProject(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TProject with the contents
     * of a TProjectBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TProjectBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TProject with the contents of bean
     */

    public static TProject createTProject(TProjectBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TProject result = (TProject) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TProject();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setDefaultOwnerID(bean.getDefaultOwnerID());
        result.setDefaultManagerID(bean.getDefaultManagerID());
        result.setDefaultInitStateID(bean.getDefaultInitStateID());
        result.setProjectType(bean.getProjectType());
        result.setVersionSystemField0(bean.getVersionSystemField0());
        result.setVersionSystemField1(bean.getVersionSystemField1());
        result.setVersionSystemField2(bean.getVersionSystemField2());
        result.setVersionSystemField3(bean.getVersionSystemField3());
        result.setDeleted(bean.getDeleted());
        result.setStatus(bean.getStatus());
        result.setCurrencyName(bean.getCurrencyName());
        result.setCurrencySymbol(bean.getCurrencySymbol());
        result.setHoursPerWorkDay(bean.getHoursPerWorkDay());
        result.setMoreProps(bean.getMoreProps());
        result.setTagLabel(bean.getTagLabel());
        result.setDescription(bean.getDescription());
        result.setPrefix(bean.getPrefix());
        result.setNextItemID(bean.getNextItemID());
        result.setLastID(bean.getLastID());
        result.setParent(bean.getParent());
        result.setSortorder(bean.getSortorder());
        result.setIsPrivate(bean.getIsPrivate());
        result.setIsTemplate(bean.getIsTemplate());
        result.setDomain(bean.getDomain());
        result.setUuid(bean.getUuid());



        {
            List<TAccessControlListBean> relatedBeans = bean.getTAccessControlListBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAccessControlListBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAccessControlListBean relatedBean =  relatedBeansIt.next();
                    TAccessControlList related = TAccessControlList.createTAccessControlList(relatedBean, createdObjects);
                    result.addTAccessControlListFromBean(related);
                }
            }
        }


        {
            List<TClassBean> relatedBeans = bean.getTClassBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TClassBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TClassBean relatedBean =  relatedBeansIt.next();
                    TClass related = TClass.createTClass(relatedBean, createdObjects);
                    result.addTClassFromBean(related);
                }
            }
        }


        {
            List<TProjectCategoryBean> relatedBeans = bean.getTProjectCategoryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TProjectCategoryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TProjectCategoryBean relatedBean =  relatedBeansIt.next();
                    TProjectCategory related = TProjectCategory.createTProjectCategory(relatedBean, createdObjects);
                    result.addTProjectCategoryFromBean(related);
                }
            }
        }


        {
            List<TReleaseBean> relatedBeans = bean.getTReleaseBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TReleaseBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TReleaseBean relatedBean =  relatedBeansIt.next();
                    TRelease related = TRelease.createTRelease(relatedBean, createdObjects);
                    result.addTReleaseFromBean(related);
                }
            }
        }


        {
            List<TWorkItemBean> relatedBeans = bean.getTWorkItemBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkItemBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkItemBean relatedBean =  relatedBeansIt.next();
                    TWorkItem related = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                    result.addTWorkItemFromBean(related);
                }
            }
        }


        {
            List<TProjectReportRepositoryBean> relatedBeans = bean.getTProjectReportRepositoryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TProjectReportRepositoryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TProjectReportRepositoryBean relatedBean =  relatedBeansIt.next();
                    TProjectReportRepository related = TProjectReportRepository.createTProjectReportRepository(relatedBean, createdObjects);
                    result.addTProjectReportRepositoryFromBean(related);
                }
            }
        }


        {
            List<TReportLayoutBean> relatedBeans = bean.getTReportLayoutBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TReportLayoutBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TReportLayoutBean relatedBean =  relatedBeansIt.next();
                    TReportLayout related = TReportLayout.createTReportLayout(relatedBean, createdObjects);
                    result.addTReportLayoutFromBean(related);
                }
            }
        }


        {
            List<TProjectAccountBean> relatedBeans = bean.getTProjectAccountBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TProjectAccountBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TProjectAccountBean relatedBean =  relatedBeansIt.next();
                    TProjectAccount related = TProjectAccount.createTProjectAccount(relatedBean, createdObjects);
                    result.addTProjectAccountFromBean(related);
                }
            }
        }


        {
            List<TDashboardScreenBean> relatedBeans = bean.getTDashboardScreenBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TDashboardScreenBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TDashboardScreenBean relatedBean =  relatedBeansIt.next();
                    TDashboardScreen related = TDashboardScreen.createTDashboardScreen(relatedBean, createdObjects);
                    result.addTDashboardScreenFromBean(related);
                }
            }
        }


        {
            List<TVersionControlParameterBean> relatedBeans = bean.getTVersionControlParameterBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TVersionControlParameterBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TVersionControlParameterBean relatedBean =  relatedBeansIt.next();
                    TVersionControlParameter related = TVersionControlParameter.createTVersionControlParameter(relatedBean, createdObjects);
                    result.addTVersionControlParameterFromBean(related);
                }
            }
        }


        {
            List<TFieldBean> relatedBeans = bean.getTFieldBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TFieldBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TFieldBean relatedBean =  relatedBeansIt.next();
                    TField related = TField.createTField(relatedBean, createdObjects);
                    result.addTFieldFromBean(related);
                }
            }
        }


        {
            List<TFieldConfigBean> relatedBeans = bean.getTFieldConfigBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TFieldConfigBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TFieldConfigBean relatedBean =  relatedBeansIt.next();
                    TFieldConfig related = TFieldConfig.createTFieldConfig(relatedBean, createdObjects);
                    result.addTFieldConfigFromBean(related);
                }
            }
        }


        {
            List<TListBean> relatedBeans = bean.getTListBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TListBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TListBean relatedBean =  relatedBeansIt.next();
                    TList related = TList.createTList(relatedBean, createdObjects);
                    result.addTListFromBean(related);
                }
            }
        }


        {
            List<TScreenBean> relatedBeans = bean.getTScreenBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScreenBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScreenBean relatedBean =  relatedBeansIt.next();
                    TScreen related = TScreen.createTScreen(relatedBean, createdObjects);
                    result.addTScreenFromBean(related);
                }
            }
        }


        {
            List<TScreenConfigBean> relatedBeans = bean.getTScreenConfigBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScreenConfigBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScreenConfigBean relatedBean =  relatedBeansIt.next();
                    TScreenConfig related = TScreenConfig.createTScreenConfig(relatedBean, createdObjects);
                    result.addTScreenConfigFromBean(related);
                }
            }
        }


        {
            List<TInitStateBean> relatedBeans = bean.getTInitStateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TInitStateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TInitStateBean relatedBean =  relatedBeansIt.next();
                    TInitState related = TInitState.createTInitState(relatedBean, createdObjects);
                    result.addTInitStateFromBean(related);
                }
            }
        }


        {
            List<TEventBean> relatedBeans = bean.getTEventBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TEventBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TEventBean relatedBean =  relatedBeansIt.next();
                    TEvent related = TEvent.createTEvent(relatedBean, createdObjects);
                    result.addTEventFromBean(related);
                }
            }
        }


        {
            List<TNotifySettingsBean> relatedBeans = bean.getTNotifySettingsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TNotifySettingsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TNotifySettingsBean relatedBean =  relatedBeansIt.next();
                    TNotifySettings related = TNotifySettings.createTNotifySettings(relatedBean, createdObjects);
                    result.addTNotifySettingsFromBean(related);
                }
            }
        }


        {
            List<TQueryRepositoryBean> relatedBeans = bean.getTQueryRepositoryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TQueryRepositoryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TQueryRepositoryBean relatedBean =  relatedBeansIt.next();
                    TQueryRepository related = TQueryRepository.createTQueryRepository(relatedBean, createdObjects);
                    result.addTQueryRepositoryFromBean(related);
                }
            }
        }


        {
            List<TExportTemplateBean> relatedBeans = bean.getTExportTemplateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TExportTemplateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TExportTemplateBean relatedBean =  relatedBeansIt.next();
                    TExportTemplate related = TExportTemplate.createTExportTemplate(relatedBean, createdObjects);
                    result.addTExportTemplateFromBean(related);
                }
            }
        }


        {
            List<TScriptsBean> relatedBeans = bean.getTScriptsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScriptsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScriptsBean relatedBean =  relatedBeansIt.next();
                    TScripts related = TScripts.createTScripts(relatedBean, createdObjects);
                    result.addTScriptsFromBean(related);
                }
            }
        }


        {
            List<TFilterCategoryBean> relatedBeans = bean.getTFilterCategoryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TFilterCategoryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TFilterCategoryBean relatedBean =  relatedBeansIt.next();
                    TFilterCategory related = TFilterCategory.createTFilterCategory(relatedBean, createdObjects);
                    result.addTFilterCategoryFromBean(related);
                }
            }
        }


        {
            List<TReportCategoryBean> relatedBeans = bean.getTReportCategoryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TReportCategoryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TReportCategoryBean relatedBean =  relatedBeansIt.next();
                    TReportCategory related = TReportCategory.createTReportCategory(relatedBean, createdObjects);
                    result.addTReportCategoryFromBean(related);
                }
            }
        }


        {
            List<TMailTemplateConfigBean> relatedBeans = bean.getTMailTemplateConfigBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TMailTemplateConfigBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TMailTemplateConfigBean relatedBean =  relatedBeansIt.next();
                    TMailTemplateConfig related = TMailTemplateConfig.createTMailTemplateConfig(relatedBean, createdObjects);
                    result.addTMailTemplateConfigFromBean(related);
                }
            }
        }


        {
            List<TWfActivityContextParamsBean> relatedBeans = bean.getTWfActivityContextParamsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWfActivityContextParamsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWfActivityContextParamsBean relatedBean =  relatedBeansIt.next();
                    TWfActivityContextParams related = TWfActivityContextParams.createTWfActivityContextParams(relatedBean, createdObjects);
                    result.addTWfActivityContextParamsFromBean(related);
                }
            }
        }


        {
            List<TWorkflowConnectBean> relatedBeans = bean.getTWorkflowConnectBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowConnectBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowConnectBean relatedBean =  relatedBeansIt.next();
                    TWorkflowConnect related = TWorkflowConnect.createTWorkflowConnect(relatedBean, createdObjects);
                    result.addTWorkflowConnectFromBean(related);
                }
            }
        }


        {
            List<TOrgProjectSLABean> relatedBeans = bean.getTOrgProjectSLABeans();
            if (relatedBeans != null)
            {
                for (Iterator<TOrgProjectSLABean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TOrgProjectSLABean relatedBean =  relatedBeansIt.next();
                    TOrgProjectSLA related = TOrgProjectSLA.createTOrgProjectSLA(relatedBean, createdObjects);
                    result.addTOrgProjectSLAFromBean(related);
                }
            }
        }


        {
            List<TMailTextBlockBean> relatedBeans = bean.getTMailTextBlockBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TMailTextBlockBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TMailTextBlockBean relatedBean =  relatedBeansIt.next();
                    TMailTextBlock related = TMailTextBlock.createTMailTextBlock(relatedBean, createdObjects);
                    result.addTMailTextBlockFromBean(related);
                }
            }
        }




        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByDefaultOwnerID();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByDefaultOwnerID(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByDefaultManagerID();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByDefaultManagerID(relatedObject);
            }
        }



        {
            TStateBean relatedBean = bean.getTStateBean();
            if (relatedBean != null)
            {
                TState relatedObject = TState.createTState(relatedBean, createdObjects);
                result.setTState(relatedObject);
            }
        }



        {
            TProjectTypeBean relatedBean = bean.getTProjectTypeBean();
            if (relatedBean != null)
            {
                TProjectType relatedObject = TProjectType.createTProjectType(relatedBean, createdObjects);
                result.setTProjectType(relatedObject);
            }
        }



        {
            TSystemStateBean relatedBean = bean.getTSystemStateBean();
            if (relatedBean != null)
            {
                TSystemState relatedObject = TSystemState.createTSystemState(relatedBean, createdObjects);
                result.setTSystemState(relatedObject);
            }
        }



        {
            TProjectBean relatedBean = bean.getTProjectBeanRelatedByParent();
            if (relatedBean != null)
            {
                TProject relatedObject = TProject.createTProject(relatedBean, createdObjects);
                result.setTProjectRelatedByParent(relatedObject);
            }
        }



        {
            TDomainBean relatedBean = bean.getTDomainBean();
            if (relatedBean != null)
            {
                TDomain relatedObject = TDomain.createTDomain(relatedBean, createdObjects);
                result.setTDomain(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TAccessControlList object to this object.
     * through the TAccessControlList foreign key attribute
     *
     * @param toAdd TAccessControlList
     */
    protected void addTAccessControlListFromBean(TAccessControlList toAdd)
    {
        initTAccessControlLists();
        collTAccessControlLists.add(toAdd);
    }


    /**
     * Method called to associate a TClass object to this object.
     * through the TClass foreign key attribute
     *
     * @param toAdd TClass
     */
    protected void addTClassFromBean(TClass toAdd)
    {
        initTClasss();
        collTClasss.add(toAdd);
    }


    /**
     * Method called to associate a TProjectCategory object to this object.
     * through the TProjectCategory foreign key attribute
     *
     * @param toAdd TProjectCategory
     */
    protected void addTProjectCategoryFromBean(TProjectCategory toAdd)
    {
        initTProjectCategorys();
        collTProjectCategorys.add(toAdd);
    }


    /**
     * Method called to associate a TRelease object to this object.
     * through the TRelease foreign key attribute
     *
     * @param toAdd TRelease
     */
    protected void addTReleaseFromBean(TRelease toAdd)
    {
        initTReleases();
        collTReleases.add(toAdd);
    }


    /**
     * Method called to associate a TWorkItem object to this object.
     * through the TWorkItem foreign key attribute
     *
     * @param toAdd TWorkItem
     */
    protected void addTWorkItemFromBean(TWorkItem toAdd)
    {
        initTWorkItems();
        collTWorkItems.add(toAdd);
    }


    /**
     * Method called to associate a TProjectReportRepository object to this object.
     * through the TProjectReportRepository foreign key attribute
     *
     * @param toAdd TProjectReportRepository
     */
    protected void addTProjectReportRepositoryFromBean(TProjectReportRepository toAdd)
    {
        initTProjectReportRepositorys();
        collTProjectReportRepositorys.add(toAdd);
    }


    /**
     * Method called to associate a TReportLayout object to this object.
     * through the TReportLayout foreign key attribute
     *
     * @param toAdd TReportLayout
     */
    protected void addTReportLayoutFromBean(TReportLayout toAdd)
    {
        initTReportLayouts();
        collTReportLayouts.add(toAdd);
    }


    /**
     * Method called to associate a TProjectAccount object to this object.
     * through the TProjectAccount foreign key attribute
     *
     * @param toAdd TProjectAccount
     */
    protected void addTProjectAccountFromBean(TProjectAccount toAdd)
    {
        initTProjectAccounts();
        collTProjectAccounts.add(toAdd);
    }


    /**
     * Method called to associate a TDashboardScreen object to this object.
     * through the TDashboardScreen foreign key attribute
     *
     * @param toAdd TDashboardScreen
     */
    protected void addTDashboardScreenFromBean(TDashboardScreen toAdd)
    {
        initTDashboardScreens();
        collTDashboardScreens.add(toAdd);
    }


    /**
     * Method called to associate a TVersionControlParameter object to this object.
     * through the TVersionControlParameter foreign key attribute
     *
     * @param toAdd TVersionControlParameter
     */
    protected void addTVersionControlParameterFromBean(TVersionControlParameter toAdd)
    {
        initTVersionControlParameters();
        collTVersionControlParameters.add(toAdd);
    }


    /**
     * Method called to associate a TField object to this object.
     * through the TField foreign key attribute
     *
     * @param toAdd TField
     */
    protected void addTFieldFromBean(TField toAdd)
    {
        initTFields();
        collTFields.add(toAdd);
    }


    /**
     * Method called to associate a TFieldConfig object to this object.
     * through the TFieldConfig foreign key attribute
     *
     * @param toAdd TFieldConfig
     */
    protected void addTFieldConfigFromBean(TFieldConfig toAdd)
    {
        initTFieldConfigs();
        collTFieldConfigs.add(toAdd);
    }


    /**
     * Method called to associate a TList object to this object.
     * through the TList foreign key attribute
     *
     * @param toAdd TList
     */
    protected void addTListFromBean(TList toAdd)
    {
        initTLists();
        collTLists.add(toAdd);
    }


    /**
     * Method called to associate a TScreen object to this object.
     * through the TScreen foreign key attribute
     *
     * @param toAdd TScreen
     */
    protected void addTScreenFromBean(TScreen toAdd)
    {
        initTScreens();
        collTScreens.add(toAdd);
    }


    /**
     * Method called to associate a TScreenConfig object to this object.
     * through the TScreenConfig foreign key attribute
     *
     * @param toAdd TScreenConfig
     */
    protected void addTScreenConfigFromBean(TScreenConfig toAdd)
    {
        initTScreenConfigs();
        collTScreenConfigs.add(toAdd);
    }


    /**
     * Method called to associate a TInitState object to this object.
     * through the TInitState foreign key attribute
     *
     * @param toAdd TInitState
     */
    protected void addTInitStateFromBean(TInitState toAdd)
    {
        initTInitStates();
        collTInitStates.add(toAdd);
    }


    /**
     * Method called to associate a TEvent object to this object.
     * through the TEvent foreign key attribute
     *
     * @param toAdd TEvent
     */
    protected void addTEventFromBean(TEvent toAdd)
    {
        initTEvents();
        collTEvents.add(toAdd);
    }


    /**
     * Method called to associate a TNotifySettings object to this object.
     * through the TNotifySettings foreign key attribute
     *
     * @param toAdd TNotifySettings
     */
    protected void addTNotifySettingsFromBean(TNotifySettings toAdd)
    {
        initTNotifySettingss();
        collTNotifySettingss.add(toAdd);
    }


    /**
     * Method called to associate a TQueryRepository object to this object.
     * through the TQueryRepository foreign key attribute
     *
     * @param toAdd TQueryRepository
     */
    protected void addTQueryRepositoryFromBean(TQueryRepository toAdd)
    {
        initTQueryRepositorys();
        collTQueryRepositorys.add(toAdd);
    }


    /**
     * Method called to associate a TExportTemplate object to this object.
     * through the TExportTemplate foreign key attribute
     *
     * @param toAdd TExportTemplate
     */
    protected void addTExportTemplateFromBean(TExportTemplate toAdd)
    {
        initTExportTemplates();
        collTExportTemplates.add(toAdd);
    }


    /**
     * Method called to associate a TScripts object to this object.
     * through the TScripts foreign key attribute
     *
     * @param toAdd TScripts
     */
    protected void addTScriptsFromBean(TScripts toAdd)
    {
        initTScriptss();
        collTScriptss.add(toAdd);
    }


    /**
     * Method called to associate a TFilterCategory object to this object.
     * through the TFilterCategory foreign key attribute
     *
     * @param toAdd TFilterCategory
     */
    protected void addTFilterCategoryFromBean(TFilterCategory toAdd)
    {
        initTFilterCategorys();
        collTFilterCategorys.add(toAdd);
    }


    /**
     * Method called to associate a TReportCategory object to this object.
     * through the TReportCategory foreign key attribute
     *
     * @param toAdd TReportCategory
     */
    protected void addTReportCategoryFromBean(TReportCategory toAdd)
    {
        initTReportCategorys();
        collTReportCategorys.add(toAdd);
    }


    /**
     * Method called to associate a TMailTemplateConfig object to this object.
     * through the TMailTemplateConfig foreign key attribute
     *
     * @param toAdd TMailTemplateConfig
     */
    protected void addTMailTemplateConfigFromBean(TMailTemplateConfig toAdd)
    {
        initTMailTemplateConfigs();
        collTMailTemplateConfigs.add(toAdd);
    }


    /**
     * Method called to associate a TWfActivityContextParams object to this object.
     * through the TWfActivityContextParams foreign key attribute
     *
     * @param toAdd TWfActivityContextParams
     */
    protected void addTWfActivityContextParamsFromBean(TWfActivityContextParams toAdd)
    {
        initTWfActivityContextParamss();
        collTWfActivityContextParamss.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowConnect object to this object.
     * through the TWorkflowConnect foreign key attribute
     *
     * @param toAdd TWorkflowConnect
     */
    protected void addTWorkflowConnectFromBean(TWorkflowConnect toAdd)
    {
        initTWorkflowConnects();
        collTWorkflowConnects.add(toAdd);
    }


    /**
     * Method called to associate a TOrgProjectSLA object to this object.
     * through the TOrgProjectSLA foreign key attribute
     *
     * @param toAdd TOrgProjectSLA
     */
    protected void addTOrgProjectSLAFromBean(TOrgProjectSLA toAdd)
    {
        initTOrgProjectSLAs();
        collTOrgProjectSLAs.add(toAdd);
    }


    /**
     * Method called to associate a TMailTextBlock object to this object.
     * through the TMailTextBlock foreign key attribute
     *
     * @param toAdd TMailTextBlock
     */
    protected void addTMailTextBlockFromBean(TMailTextBlock toAdd)
    {
        initTMailTextBlocks();
        collTMailTextBlocks.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TProject:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("DefaultOwnerID = ")
           .append(getDefaultOwnerID())
           .append("\n");
        str.append("DefaultManagerID = ")
           .append(getDefaultManagerID())
           .append("\n");
        str.append("DefaultInitStateID = ")
           .append(getDefaultInitStateID())
           .append("\n");
        str.append("ProjectType = ")
           .append(getProjectType())
           .append("\n");
        str.append("VersionSystemField0 = ")
           .append(getVersionSystemField0())
           .append("\n");
        str.append("VersionSystemField1 = ")
           .append(getVersionSystemField1())
           .append("\n");
        str.append("VersionSystemField2 = ")
           .append(getVersionSystemField2())
           .append("\n");
        str.append("VersionSystemField3 = ")
           .append(getVersionSystemField3())
           .append("\n");
        str.append("Deleted = ")
           .append(getDeleted())
           .append("\n");
        str.append("Status = ")
           .append(getStatus())
           .append("\n");
        str.append("CurrencyName = ")
           .append(getCurrencyName())
           .append("\n");
        str.append("CurrencySymbol = ")
           .append(getCurrencySymbol())
           .append("\n");
        str.append("HoursPerWorkDay = ")
           .append(getHoursPerWorkDay())
           .append("\n");
        str.append("MoreProps = ")
           .append(getMoreProps())
           .append("\n");
        str.append("TagLabel = ")
           .append(getTagLabel())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Prefix = ")
           .append(getPrefix())
           .append("\n");
        str.append("NextItemID = ")
           .append(getNextItemID())
           .append("\n");
        str.append("LastID = ")
           .append(getLastID())
           .append("\n");
        str.append("Parent = ")
           .append(getParent())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("IsPrivate = ")
           .append(getIsPrivate())
           .append("\n");
        str.append("IsTemplate = ")
           .append(getIsTemplate())
           .append("\n");
        str.append("Domain = ")
           .append(getDomain())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
