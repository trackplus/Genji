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
 * extended; all references should be to TWorkFlowBean
 */
public abstract class BaseTWorkFlowBean
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

    /** The value for the stateFrom field */
    private Integer stateFrom;

    /** The value for the stateTo field */
    private Integer stateTo;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the responsible field */
    private Integer responsible;

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
     * Get the StateFrom
     *
     * @return Integer
     */
    public Integer getStateFrom ()
    {
        return stateFrom;
    }

    /**
     * Set the value of StateFrom
     *
     * @param v new value
     */
    public void setStateFrom(Integer v)
    {

        this.stateFrom = v;
        setModified(true);

    }

    /**
     * Get the StateTo
     *
     * @return Integer
     */
    public Integer getStateTo ()
    {
        return stateTo;
    }

    /**
     * Set the value of StateTo
     *
     * @param v new value
     */
    public void setStateTo(Integer v)
    {

        this.stateTo = v;
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
     * Get the Responsible
     *
     * @return Integer
     */
    public Integer getResponsible ()
    {
        return responsible;
    }

    /**
     * Set the value of Responsible
     *
     * @param v new value
     */
    public void setResponsible(Integer v)
    {

        this.responsible = v;
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

    



    private TStateBean aTStateBeanRelatedByStateFrom;

    /**
     * sets an associated TStateBean object
     *
     * @param v TStateBean
     */
    public void setTStateBeanRelatedByStateFrom(TStateBean v)
    {
        if (v == null)
        {
            setStateFrom((Integer) null);
        }
        else
        {
            setStateFrom(v.getObjectID());
        }
        aTStateBeanRelatedByStateFrom = v;
    }


    /**
     * Get the associated TStateBean object
     *
     * @return the associated TStateBean object
     */
    public TStateBean getTStateBeanRelatedByStateFrom()
    {
        return aTStateBeanRelatedByStateFrom;
    }





    private TStateBean aTStateBeanRelatedByStateTo;

    /**
     * sets an associated TStateBean object
     *
     * @param v TStateBean
     */
    public void setTStateBeanRelatedByStateTo(TStateBean v)
    {
        if (v == null)
        {
            setStateTo((Integer) null);
        }
        else
        {
            setStateTo(v.getObjectID());
        }
        aTStateBeanRelatedByStateTo = v;
    }


    /**
     * Get the associated TStateBean object
     *
     * @return the associated TStateBean object
     */
    public TStateBean getTStateBeanRelatedByStateTo()
    {
        return aTStateBeanRelatedByStateTo;
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
            setResponsible((Integer) null);
        }
        else
        {
            setResponsible(v.getObjectID());
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

}
