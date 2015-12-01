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
 * extended; all references should be to TNotifyFieldBean
 */
public abstract class BaseTNotifyFieldBean
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

    /** The value for the field field */
    private Integer field;

    /** The value for the actionType field */
    private Integer actionType;

    /** The value for the fieldType field */
    private Integer fieldType;

    /** The value for the notifyTrigger field */
    private Integer notifyTrigger;

    /** The value for the originator field */
    private String originator = "N";

    /** The value for the manager field */
    private String manager = "N";

    /** The value for the responsible field */
    private String responsible = "N";

    /** The value for the consultant field */
    private String consultant = "N";

    /** The value for the informant field */
    private String informant = "N";

    /** The value for the observer field */
    private String observer = "N";

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
     * Get the Field
     *
     * @return Integer
     */
    public Integer getField ()
    {
        return field;
    }

    /**
     * Set the value of Field
     *
     * @param v new value
     */
    public void setField(Integer v)
    {

        this.field = v;
        setModified(true);

    }

    /**
     * Get the ActionType
     *
     * @return Integer
     */
    public Integer getActionType ()
    {
        return actionType;
    }

    /**
     * Set the value of ActionType
     *
     * @param v new value
     */
    public void setActionType(Integer v)
    {

        this.actionType = v;
        setModified(true);

    }

    /**
     * Get the FieldType
     *
     * @return Integer
     */
    public Integer getFieldType ()
    {
        return fieldType;
    }

    /**
     * Set the value of FieldType
     *
     * @param v new value
     */
    public void setFieldType(Integer v)
    {

        this.fieldType = v;
        setModified(true);

    }

    /**
     * Get the NotifyTrigger
     *
     * @return Integer
     */
    public Integer getNotifyTrigger ()
    {
        return notifyTrigger;
    }

    /**
     * Set the value of NotifyTrigger
     *
     * @param v new value
     */
    public void setNotifyTrigger(Integer v)
    {

        this.notifyTrigger = v;
        setModified(true);

    }

    /**
     * Get the Originator
     *
     * @return String
     */
    public String getOriginator ()
    {
        return originator;
    }

    /**
     * Set the value of Originator
     *
     * @param v new value
     */
    public void setOriginator(String v)
    {

        this.originator = v;
        setModified(true);

    }

    /**
     * Get the Manager
     *
     * @return String
     */
    public String getManager ()
    {
        return manager;
    }

    /**
     * Set the value of Manager
     *
     * @param v new value
     */
    public void setManager(String v)
    {

        this.manager = v;
        setModified(true);

    }

    /**
     * Get the Responsible
     *
     * @return String
     */
    public String getResponsible ()
    {
        return responsible;
    }

    /**
     * Set the value of Responsible
     *
     * @param v new value
     */
    public void setResponsible(String v)
    {

        this.responsible = v;
        setModified(true);

    }

    /**
     * Get the Consultant
     *
     * @return String
     */
    public String getConsultant ()
    {
        return consultant;
    }

    /**
     * Set the value of Consultant
     *
     * @param v new value
     */
    public void setConsultant(String v)
    {

        this.consultant = v;
        setModified(true);

    }

    /**
     * Get the Informant
     *
     * @return String
     */
    public String getInformant ()
    {
        return informant;
    }

    /**
     * Set the value of Informant
     *
     * @param v new value
     */
    public void setInformant(String v)
    {

        this.informant = v;
        setModified(true);

    }

    /**
     * Get the Observer
     *
     * @return String
     */
    public String getObserver ()
    {
        return observer;
    }

    /**
     * Set the value of Observer
     *
     * @param v new value
     */
    public void setObserver(String v)
    {

        this.observer = v;
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

    



    private TNotifyTriggerBean aTNotifyTriggerBean;

    /**
     * sets an associated TNotifyTriggerBean object
     *
     * @param v TNotifyTriggerBean
     */
    public void setTNotifyTriggerBean(TNotifyTriggerBean v)
    {
        if (v == null)
        {
            setNotifyTrigger((Integer) null);
        }
        else
        {
            setNotifyTrigger(v.getObjectID());
        }
        aTNotifyTriggerBean = v;
    }


    /**
     * Get the associated TNotifyTriggerBean object
     *
     * @return the associated TNotifyTriggerBean object
     */
    public TNotifyTriggerBean getTNotifyTriggerBean()
    {
        return aTNotifyTriggerBean;
    }



}
