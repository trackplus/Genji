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
 * extended; all references should be to TDashboardScreenBean
 */
public abstract class BaseTDashboardScreenBean
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

    /** The value for the label field */
    private String label;

    /** The value for the person field */
    private Integer person;

    /** The value for the project field */
    private Integer project;

    /** The value for the entityType field */
    private Integer entityType;

    /** The value for the owner field */
    private Integer owner;

    /** The value for the description field */
    private String description;

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
     * Get the Person
     *
     * @return Integer
     */
    public Integer getPerson ()
    {
        return person;
    }

    /**
     * Set the value of Person
     *
     * @param v new value
     */
    public void setPerson(Integer v)
    {

        this.person = v;
        setModified(true);

    }

    /**
     * Get the Project
     *
     * @return Integer
     */
    public Integer getProject ()
    {
        return project;
    }

    /**
     * Set the value of Project
     *
     * @param v new value
     */
    public void setProject(Integer v)
    {

        this.project = v;
        setModified(true);

    }

    /**
     * Get the EntityType
     *
     * @return Integer
     */
    public Integer getEntityType ()
    {
        return entityType;
    }

    /**
     * Set the value of EntityType
     *
     * @param v new value
     */
    public void setEntityType(Integer v)
    {

        this.entityType = v;
        setModified(true);

    }

    /**
     * Get the Owner
     *
     * @return Integer
     */
    public Integer getOwner ()
    {
        return owner;
    }

    /**
     * Set the value of Owner
     *
     * @param v new value
     */
    public void setOwner(Integer v)
    {

        this.owner = v;
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

    



    private TPersonBean aTPersonBeanRelatedByPerson;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByPerson(TPersonBean v)
    {
        if (v == null)
        {
            setPerson((Integer) null);
        }
        else
        {
            setPerson(v.getObjectID());
        }
        aTPersonBeanRelatedByPerson = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByPerson()
    {
        return aTPersonBeanRelatedByPerson;
    }





    private TProjectBean aTProjectBean;

    /**
     * sets an associated TProjectBean object
     *
     * @param v TProjectBean
     */
    public void setTProjectBean(TProjectBean v)
    {
        if (v == null)
        {
            setProject((Integer) null);
        }
        else
        {
            setProject(v.getObjectID());
        }
        aTProjectBean = v;
    }


    /**
     * Get the associated TProjectBean object
     *
     * @return the associated TProjectBean object
     */
    public TProjectBean getTProjectBean()
    {
        return aTProjectBean;
    }





    private TPersonBean aTPersonBeanRelatedByOwner;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByOwner(TPersonBean v)
    {
        if (v == null)
        {
            setOwner((Integer) null);
        }
        else
        {
            setOwner(v.getObjectID());
        }
        aTPersonBeanRelatedByOwner = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByOwner()
    {
        return aTPersonBeanRelatedByOwner;
    }





    /**
     * Collection to store aggregation of collTDashboardTabBeans
     */
    protected List<TDashboardTabBean> collTDashboardTabBeans;

    /**
     * Returns the collection of TDashboardTabBeans
     */
    public List<TDashboardTabBean> getTDashboardTabBeans()
    {
        return collTDashboardTabBeans;
    }

    /**
     * Sets the collection of TDashboardTabBeans to the specified value
     */
    public void setTDashboardTabBeans(List<TDashboardTabBean> list)
    {
        if (list == null)
        {
            collTDashboardTabBeans = null;
        }
        else
        {
            collTDashboardTabBeans = new ArrayList<TDashboardTabBean>(list);
        }
    }

}
