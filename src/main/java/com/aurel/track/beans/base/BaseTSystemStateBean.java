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
 * extended; all references should be to TSystemStateBean
 */
public abstract class BaseTSystemStateBean
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

    /** The value for the stateflag field */
    private Integer stateflag;

    /** The value for the symbol field */
    private String symbol;

    /** The value for the entityflag field */
    private Integer entityflag;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the iconKey field */
    private Integer iconKey;

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
     * Get the Stateflag
     *
     * @return Integer
     */
    public Integer getStateflag ()
    {
        return stateflag;
    }

    /**
     * Set the value of Stateflag
     *
     * @param v new value
     */
    public void setStateflag(Integer v)
    {

        this.stateflag = v;
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
     * Get the Entityflag
     *
     * @return Integer
     */
    public Integer getEntityflag ()
    {
        return entityflag;
    }

    /**
     * Set the value of Entityflag
     *
     * @param v new value
     */
    public void setEntityflag(Integer v)
    {

        this.entityflag = v;
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
     * Collection to store aggregation of collTAccountBeans
     */
    protected List<TAccountBean> collTAccountBeans;

    /**
     * Returns the collection of TAccountBeans
     */
    public List<TAccountBean> getTAccountBeans()
    {
        return collTAccountBeans;
    }

    /**
     * Sets the collection of TAccountBeans to the specified value
     */
    public void setTAccountBeans(List<TAccountBean> list)
    {
        if (list == null)
        {
            collTAccountBeans = null;
        }
        else
        {
            collTAccountBeans = new ArrayList<TAccountBean>(list);
        }
    }

}
