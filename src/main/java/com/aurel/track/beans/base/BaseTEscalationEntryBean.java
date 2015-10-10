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
 * extended; all references should be to TEscalationEntryBean
 */
public abstract class BaseTEscalationEntryBean
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

    /** The value for the sla field */
    private Integer sla;

    /** The value for the priority field */
    private Integer priority;

    /** The value for the escalateTo field */
    private Integer escalateTo;

    /** The value for the sparameters field */
    private String sparameters;

    /** The value for the nlevel field */
    private Integer nlevel;

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
     * Get the Sla
     *
     * @return Integer
     */
    public Integer getSla ()
    {
        return sla;
    }

    /**
     * Set the value of Sla
     *
     * @param v new value
     */
    public void setSla(Integer v)
    {

        this.sla = v;
        setModified(true);

    }

    /**
     * Get the Priority
     *
     * @return Integer
     */
    public Integer getPriority ()
    {
        return priority;
    }

    /**
     * Set the value of Priority
     *
     * @param v new value
     */
    public void setPriority(Integer v)
    {

        this.priority = v;
        setModified(true);

    }

    /**
     * Get the EscalateTo
     *
     * @return Integer
     */
    public Integer getEscalateTo ()
    {
        return escalateTo;
    }

    /**
     * Set the value of EscalateTo
     *
     * @param v new value
     */
    public void setEscalateTo(Integer v)
    {

        this.escalateTo = v;
        setModified(true);

    }

    /**
     * Get the Sparameters
     *
     * @return String
     */
    public String getSparameters ()
    {
        return sparameters;
    }

    /**
     * Set the value of Sparameters
     *
     * @param v new value
     */
    public void setSparameters(String v)
    {

        this.sparameters = v;
        setModified(true);

    }

    /**
     * Get the Nlevel
     *
     * @return Integer
     */
    public Integer getNlevel ()
    {
        return nlevel;
    }

    /**
     * Set the value of Nlevel
     *
     * @param v new value
     */
    public void setNlevel(Integer v)
    {

        this.nlevel = v;
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

    



    private TSLABean aTSLABean;

    /**
     * sets an associated TSLABean object
     *
     * @param v TSLABean
     */
    public void setTSLABean(TSLABean v)
    {
        if (v == null)
        {
            setSla((Integer) null);
        }
        else
        {
            setSla(v.getObjectID());
        }
        aTSLABean = v;
    }


    /**
     * Get the associated TSLABean object
     *
     * @return the associated TSLABean object
     */
    public TSLABean getTSLABean()
    {
        return aTSLABean;
    }





    private TPriorityBean aTPriorityBean;

    /**
     * sets an associated TPriorityBean object
     *
     * @param v TPriorityBean
     */
    public void setTPriorityBean(TPriorityBean v)
    {
        if (v == null)
        {
            setPriority((Integer) null);
        }
        else
        {
            setPriority(v.getObjectID());
        }
        aTPriorityBean = v;
    }


    /**
     * Get the associated TPriorityBean object
     *
     * @return the associated TPriorityBean object
     */
    public TPriorityBean getTPriorityBean()
    {
        return aTPriorityBean;
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
            setEscalateTo((Integer) null);
        }
        else
        {
            setEscalateTo(v.getObjectID());
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
     * Collection to store aggregation of collTEscalationStateBeans
     */
    protected List<TEscalationStateBean> collTEscalationStateBeans;

    /**
     * Returns the collection of TEscalationStateBeans
     */
    public List<TEscalationStateBean> getTEscalationStateBeans()
    {
        return collTEscalationStateBeans;
    }

    /**
     * Sets the collection of TEscalationStateBeans to the specified value
     */
    public void setTEscalationStateBeans(List<TEscalationStateBean> list)
    {
        if (list == null)
        {
            collTEscalationStateBeans = null;
        }
        else
        {
            collTEscalationStateBeans = new ArrayList<TEscalationStateBean>(list);
        }
    }

}
