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
 * extended; all references should be to TProjectTypeBean
 */
public abstract class BaseTProjectTypeBean
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

    /** The value for the notifyOwnerLevel field */
    private Integer notifyOwnerLevel;

    /** The value for the notifyManagerLevel field */
    private Integer notifyManagerLevel;

    /** The value for the hoursPerWorkDay field */
    private Double hoursPerWorkDay;

    /** The value for the moreProps field */
    private String moreProps;

    /** The value for the defaultForPrivate field */
    private String defaultForPrivate = "N";

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
     * Get the NotifyOwnerLevel
     *
     * @return Integer
     */
    public Integer getNotifyOwnerLevel ()
    {
        return notifyOwnerLevel;
    }

    /**
     * Set the value of NotifyOwnerLevel
     *
     * @param v new value
     */
    public void setNotifyOwnerLevel(Integer v)
    {

        this.notifyOwnerLevel = v;
        setModified(true);

    }

    /**
     * Get the NotifyManagerLevel
     *
     * @return Integer
     */
    public Integer getNotifyManagerLevel ()
    {
        return notifyManagerLevel;
    }

    /**
     * Set the value of NotifyManagerLevel
     *
     * @param v new value
     */
    public void setNotifyManagerLevel(Integer v)
    {

        this.notifyManagerLevel = v;
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
     * Get the DefaultForPrivate
     *
     * @return String
     */
    public String getDefaultForPrivate ()
    {
        return defaultForPrivate;
    }

    /**
     * Set the value of DefaultForPrivate
     *
     * @param v new value
     */
    public void setDefaultForPrivate(String v)
    {

        this.defaultForPrivate = v;
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
     * Collection to store aggregation of collTPpriorityBeans
     */
    protected List<TPpriorityBean> collTPpriorityBeans;

    /**
     * Returns the collection of TPpriorityBeans
     */
    public List<TPpriorityBean> getTPpriorityBeans()
    {
        return collTPpriorityBeans;
    }

    /**
     * Sets the collection of TPpriorityBeans to the specified value
     */
    public void setTPpriorityBeans(List<TPpriorityBean> list)
    {
        if (list == null)
        {
            collTPpriorityBeans = null;
        }
        else
        {
            collTPpriorityBeans = new ArrayList<TPpriorityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTProjectBeans
     */
    protected List<TProjectBean> collTProjectBeans;

    /**
     * Returns the collection of TProjectBeans
     */
    public List<TProjectBean> getTProjectBeans()
    {
        return collTProjectBeans;
    }

    /**
     * Sets the collection of TProjectBeans to the specified value
     */
    public void setTProjectBeans(List<TProjectBean> list)
    {
        if (list == null)
        {
            collTProjectBeans = null;
        }
        else
        {
            collTProjectBeans = new ArrayList<TProjectBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTRoleBeans
     */
    protected List<TRoleBean> collTRoleBeans;

    /**
     * Returns the collection of TRoleBeans
     */
    public List<TRoleBean> getTRoleBeans()
    {
        return collTRoleBeans;
    }

    /**
     * Sets the collection of TRoleBeans to the specified value
     */
    public void setTRoleBeans(List<TRoleBean> list)
    {
        if (list == null)
        {
            collTRoleBeans = null;
        }
        else
        {
            collTRoleBeans = new ArrayList<TRoleBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTPseverityBeans
     */
    protected List<TPseverityBean> collTPseverityBeans;

    /**
     * Returns the collection of TPseverityBeans
     */
    public List<TPseverityBean> getTPseverityBeans()
    {
        return collTPseverityBeans;
    }

    /**
     * Sets the collection of TPseverityBeans to the specified value
     */
    public void setTPseverityBeans(List<TPseverityBean> list)
    {
        if (list == null)
        {
            collTPseverityBeans = null;
        }
        else
        {
            collTPseverityBeans = new ArrayList<TPseverityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTDocStateBeans
     */
    protected List<TDocStateBean> collTDocStateBeans;

    /**
     * Returns the collection of TDocStateBeans
     */
    public List<TDocStateBean> getTDocStateBeans()
    {
        return collTDocStateBeans;
    }

    /**
     * Sets the collection of TDocStateBeans to the specified value
     */
    public void setTDocStateBeans(List<TDocStateBean> list)
    {
        if (list == null)
        {
            collTDocStateBeans = null;
        }
        else
        {
            collTDocStateBeans = new ArrayList<TDocStateBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTDisableFieldBeans
     */
    protected List<TDisableFieldBean> collTDisableFieldBeans;

    /**
     * Returns the collection of TDisableFieldBeans
     */
    public List<TDisableFieldBean> getTDisableFieldBeans()
    {
        return collTDisableFieldBeans;
    }

    /**
     * Sets the collection of TDisableFieldBeans to the specified value
     */
    public void setTDisableFieldBeans(List<TDisableFieldBean> list)
    {
        if (list == null)
        {
            collTDisableFieldBeans = null;
        }
        else
        {
            collTDisableFieldBeans = new ArrayList<TDisableFieldBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTPlistTypeBeans
     */
    protected List<TPlistTypeBean> collTPlistTypeBeans;

    /**
     * Returns the collection of TPlistTypeBeans
     */
    public List<TPlistTypeBean> getTPlistTypeBeans()
    {
        return collTPlistTypeBeans;
    }

    /**
     * Sets the collection of TPlistTypeBeans to the specified value
     */
    public void setTPlistTypeBeans(List<TPlistTypeBean> list)
    {
        if (list == null)
        {
            collTPlistTypeBeans = null;
        }
        else
        {
            collTPlistTypeBeans = new ArrayList<TPlistTypeBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTPstateBeans
     */
    protected List<TPstateBean> collTPstateBeans;

    /**
     * Returns the collection of TPstateBeans
     */
    public List<TPstateBean> getTPstateBeans()
    {
        return collTPstateBeans;
    }

    /**
     * Sets the collection of TPstateBeans to the specified value
     */
    public void setTPstateBeans(List<TPstateBean> list)
    {
        if (list == null)
        {
            collTPstateBeans = null;
        }
        else
        {
            collTPstateBeans = new ArrayList<TPstateBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkFlowBeans
     */
    protected List<TWorkFlowBean> collTWorkFlowBeans;

    /**
     * Returns the collection of TWorkFlowBeans
     */
    public List<TWorkFlowBean> getTWorkFlowBeans()
    {
        return collTWorkFlowBeans;
    }

    /**
     * Sets the collection of TWorkFlowBeans to the specified value
     */
    public void setTWorkFlowBeans(List<TWorkFlowBean> list)
    {
        if (list == null)
        {
            collTWorkFlowBeans = null;
        }
        else
        {
            collTWorkFlowBeans = new ArrayList<TWorkFlowBean>(list);
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
     * Collection to store aggregation of collTPRoleBeans
     */
    protected List<TPRoleBean> collTPRoleBeans;

    /**
     * Returns the collection of TPRoleBeans
     */
    public List<TPRoleBean> getTPRoleBeans()
    {
        return collTPRoleBeans;
    }

    /**
     * Sets the collection of TPRoleBeans to the specified value
     */
    public void setTPRoleBeans(List<TPRoleBean> list)
    {
        if (list == null)
        {
            collTPRoleBeans = null;
        }
        else
        {
            collTPRoleBeans = new ArrayList<TPRoleBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTChildProjectTypeBeansRelatedByProjectTypeParent
     */
    protected List<TChildProjectTypeBean> collTChildProjectTypeBeansRelatedByProjectTypeParent;

    /**
     * Returns the collection of TChildProjectTypeBeansRelatedByProjectTypeParent
     */
    public List<TChildProjectTypeBean> getTChildProjectTypeBeansRelatedByProjectTypeParent()
    {
        return collTChildProjectTypeBeansRelatedByProjectTypeParent;
    }

    /**
     * Sets the collection of TChildProjectTypeBeansRelatedByProjectTypeParent to the specified value
     */
    public void setTChildProjectTypeBeansRelatedByProjectTypeParent(List<TChildProjectTypeBean> list)
    {
        if (list == null)
        {
            collTChildProjectTypeBeansRelatedByProjectTypeParent = null;
        }
        else
        {
            collTChildProjectTypeBeansRelatedByProjectTypeParent = new ArrayList<TChildProjectTypeBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTChildProjectTypeBeansRelatedByProjectTypeChild
     */
    protected List<TChildProjectTypeBean> collTChildProjectTypeBeansRelatedByProjectTypeChild;

    /**
     * Returns the collection of TChildProjectTypeBeansRelatedByProjectTypeChild
     */
    public List<TChildProjectTypeBean> getTChildProjectTypeBeansRelatedByProjectTypeChild()
    {
        return collTChildProjectTypeBeansRelatedByProjectTypeChild;
    }

    /**
     * Sets the collection of TChildProjectTypeBeansRelatedByProjectTypeChild to the specified value
     */
    public void setTChildProjectTypeBeansRelatedByProjectTypeChild(List<TChildProjectTypeBean> list)
    {
        if (list == null)
        {
            collTChildProjectTypeBeansRelatedByProjectTypeChild = null;
        }
        else
        {
            collTChildProjectTypeBeansRelatedByProjectTypeChild = new ArrayList<TChildProjectTypeBean>(list);
        }
    }

}
