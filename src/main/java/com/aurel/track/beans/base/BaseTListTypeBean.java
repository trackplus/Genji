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
 * Holds all known issue types.
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TListTypeBean
 */
public abstract class BaseTListTypeBean
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

    /** The value for the tooltip field */
    private String tooltip;

    /** The value for the typeflag field */
    private Integer typeflag;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the symbol field */
    private String symbol;

    /** The value for the iconKey field */
    private Integer iconKey;

    /** The value for the iconChanged field */
    private String iconChanged = "N";

    /** The value for the cSSStyle field */
    private String cSSStyle;

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
     * Get the Tooltip
     *
     * @return String
     */
    public String getTooltip ()
    {
        return tooltip;
    }

    /**
     * Set the value of Tooltip
     *
     * @param v new value
     */
    public void setTooltip(String v)
    {

        this.tooltip = v;
        setModified(true);

    }

    /**
     * Get the Typeflag
     *
     * @return Integer
     */
    public Integer getTypeflag ()
    {
        return typeflag;
    }

    /**
     * Set the value of Typeflag
     *
     * @param v new value
     */
    public void setTypeflag(Integer v)
    {

        this.typeflag = v;
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
     * Get the Symbol
     *
     * @return String
     */
    public String getSymbol ()
    {
        return symbol;
    }

    /**
     * Set the value of Symbol
     *
     * @param v new value
     */
    public void setSymbol(String v)
    {

        this.symbol = v;
        setModified(true);

    }

    /**
     * Get the IconKey
     *
     * @return Integer
     */
    public Integer getIconKey ()
    {
        return iconKey;
    }

    /**
     * Set the value of IconKey
     *
     * @param v new value
     */
    public void setIconKey(Integer v)
    {

        this.iconKey = v;
        setModified(true);

    }

    /**
     * Get the IconChanged
     *
     * @return String
     */
    public String getIconChanged ()
    {
        return iconChanged;
    }

    /**
     * Set the value of IconChanged
     *
     * @param v new value
     */
    public void setIconChanged(String v)
    {

        this.iconChanged = v;
        setModified(true);

    }

    /**
     * Get the CSSStyle
     *
     * @return String
     */
    public String getCSSStyle ()
    {
        return cSSStyle;
    }

    /**
     * Set the value of CSSStyle
     *
     * @param v new value
     */
    public void setCSSStyle(String v)
    {

        this.cSSStyle = v;
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

    



    private TBLOBBean aTBLOBBean;

    /**
     * sets an associated TBLOBBean object
     *
     * @param v TBLOBBean
     */
    public void setTBLOBBean(TBLOBBean v)
    {
        if (v == null)
        {
            setIconKey((Integer) null);
        }
        else
        {
            setIconKey(v.getObjectID());
        }
        aTBLOBBean = v;
    }


    /**
     * Get the associated TBLOBBean object
     *
     * @return the associated TBLOBBean object
     */
    public TBLOBBean getTBLOBBean()
    {
        return aTBLOBBean;
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
     * Collection to store aggregation of collTWorkFlowCategoryBeans
     */
    protected List<TWorkFlowCategoryBean> collTWorkFlowCategoryBeans;

    /**
     * Returns the collection of TWorkFlowCategoryBeans
     */
    public List<TWorkFlowCategoryBean> getTWorkFlowCategoryBeans()
    {
        return collTWorkFlowCategoryBeans;
    }

    /**
     * Sets the collection of TWorkFlowCategoryBeans to the specified value
     */
    public void setTWorkFlowCategoryBeans(List<TWorkFlowCategoryBean> list)
    {
        if (list == null)
        {
            collTWorkFlowCategoryBeans = null;
        }
        else
        {
            collTWorkFlowCategoryBeans = new ArrayList<TWorkFlowCategoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTRoleListTypeBeans
     */
    protected List<TRoleListTypeBean> collTRoleListTypeBeans;

    /**
     * Returns the collection of TRoleListTypeBeans
     */
    public List<TRoleListTypeBean> getTRoleListTypeBeans()
    {
        return collTRoleListTypeBeans;
    }

    /**
     * Sets the collection of TRoleListTypeBeans to the specified value
     */
    public void setTRoleListTypeBeans(List<TRoleListTypeBean> list)
    {
        if (list == null)
        {
            collTRoleListTypeBeans = null;
        }
        else
        {
            collTRoleListTypeBeans = new ArrayList<TRoleListTypeBean>(list);
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
     * Collection to store aggregation of collTChildIssueTypeBeansRelatedByIssueTypeParent
     */
    protected List<TChildIssueTypeBean> collTChildIssueTypeBeansRelatedByIssueTypeParent;

    /**
     * Returns the collection of TChildIssueTypeBeansRelatedByIssueTypeParent
     */
    public List<TChildIssueTypeBean> getTChildIssueTypeBeansRelatedByIssueTypeParent()
    {
        return collTChildIssueTypeBeansRelatedByIssueTypeParent;
    }

    /**
     * Sets the collection of TChildIssueTypeBeansRelatedByIssueTypeParent to the specified value
     */
    public void setTChildIssueTypeBeansRelatedByIssueTypeParent(List<TChildIssueTypeBean> list)
    {
        if (list == null)
        {
            collTChildIssueTypeBeansRelatedByIssueTypeParent = null;
        }
        else
        {
            collTChildIssueTypeBeansRelatedByIssueTypeParent = new ArrayList<TChildIssueTypeBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTChildIssueTypeBeansRelatedByIssueTypeChild
     */
    protected List<TChildIssueTypeBean> collTChildIssueTypeBeansRelatedByIssueTypeChild;

    /**
     * Returns the collection of TChildIssueTypeBeansRelatedByIssueTypeChild
     */
    public List<TChildIssueTypeBean> getTChildIssueTypeBeansRelatedByIssueTypeChild()
    {
        return collTChildIssueTypeBeansRelatedByIssueTypeChild;
    }

    /**
     * Sets the collection of TChildIssueTypeBeansRelatedByIssueTypeChild to the specified value
     */
    public void setTChildIssueTypeBeansRelatedByIssueTypeChild(List<TChildIssueTypeBean> list)
    {
        if (list == null)
        {
            collTChildIssueTypeBeansRelatedByIssueTypeChild = null;
        }
        else
        {
            collTChildIssueTypeBeansRelatedByIssueTypeChild = new ArrayList<TChildIssueTypeBean>(list);
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

}
