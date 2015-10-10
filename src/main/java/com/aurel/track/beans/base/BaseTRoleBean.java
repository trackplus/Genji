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
 * extended; all references should be to TRoleBean
 */
public abstract class BaseTRoleBean
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

    /** The value for the accesskey field */
    private Integer accesskey;

    /** The value for the extendedaccesskey field */
    private String extendedaccesskey;

    /** The value for the projecttype field */
    private Integer projecttype;

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
     * Get the Accesskey
     *
     * @return Integer
     */
    public Integer getAccesskey ()
    {
        return accesskey;
    }

    /**
     * Set the value of Accesskey
     *
     * @param v new value
     */
    public void setAccesskey(Integer v)
    {

        this.accesskey = v;
        setModified(true);

    }

    /**
     * Get the Extendedaccesskey
     *
     * @return String
     */
    public String getExtendedaccesskey ()
    {
        return extendedaccesskey;
    }

    /**
     * Set the value of Extendedaccesskey
     *
     * @param v new value
     */
    public void setExtendedaccesskey(String v)
    {

        this.extendedaccesskey = v;
        setModified(true);

    }

    /**
     * Get the Projecttype
     *
     * @return Integer
     */
    public Integer getProjecttype ()
    {
        return projecttype;
    }

    /**
     * Set the value of Projecttype
     *
     * @param v new value
     */
    public void setProjecttype(Integer v)
    {

        this.projecttype = v;
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
            setProjecttype((Integer) null);
        }
        else
        {
            setProjecttype(v.getObjectID());
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
     * Collection to store aggregation of collTWorkFlowRoleBeans
     */
    protected List<TWorkFlowRoleBean> collTWorkFlowRoleBeans;

    /**
     * Returns the collection of TWorkFlowRoleBeans
     */
    public List<TWorkFlowRoleBean> getTWorkFlowRoleBeans()
    {
        return collTWorkFlowRoleBeans;
    }

    /**
     * Sets the collection of TWorkFlowRoleBeans to the specified value
     */
    public void setTWorkFlowRoleBeans(List<TWorkFlowRoleBean> list)
    {
        if (list == null)
        {
            collTWorkFlowRoleBeans = null;
        }
        else
        {
            collTWorkFlowRoleBeans = new ArrayList<TWorkFlowRoleBean>(list);
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
     * Collection to store aggregation of collTRoleFieldBeans
     */
    protected List<TRoleFieldBean> collTRoleFieldBeans;

    /**
     * Returns the collection of TRoleFieldBeans
     */
    public List<TRoleFieldBean> getTRoleFieldBeans()
    {
        return collTRoleFieldBeans;
    }

    /**
     * Sets the collection of TRoleFieldBeans to the specified value
     */
    public void setTRoleFieldBeans(List<TRoleFieldBean> list)
    {
        if (list == null)
        {
            collTRoleFieldBeans = null;
        }
        else
        {
            collTRoleFieldBeans = new ArrayList<TRoleFieldBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTConfigOptionsRoleBeans
     */
    protected List<TConfigOptionsRoleBean> collTConfigOptionsRoleBeans;

    /**
     * Returns the collection of TConfigOptionsRoleBeans
     */
    public List<TConfigOptionsRoleBean> getTConfigOptionsRoleBeans()
    {
        return collTConfigOptionsRoleBeans;
    }

    /**
     * Sets the collection of TConfigOptionsRoleBeans to the specified value
     */
    public void setTConfigOptionsRoleBeans(List<TConfigOptionsRoleBean> list)
    {
        if (list == null)
        {
            collTConfigOptionsRoleBeans = null;
        }
        else
        {
            collTConfigOptionsRoleBeans = new ArrayList<TConfigOptionsRoleBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowGuardBeans
     */
    protected List<TWorkflowGuardBean> collTWorkflowGuardBeans;

    /**
     * Returns the collection of TWorkflowGuardBeans
     */
    public List<TWorkflowGuardBean> getTWorkflowGuardBeans()
    {
        return collTWorkflowGuardBeans;
    }

    /**
     * Sets the collection of TWorkflowGuardBeans to the specified value
     */
    public void setTWorkflowGuardBeans(List<TWorkflowGuardBean> list)
    {
        if (list == null)
        {
            collTWorkflowGuardBeans = null;
        }
        else
        {
            collTWorkflowGuardBeans = new ArrayList<TWorkflowGuardBean>(list);
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

}
