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
 * extended; all references should be to TWorkflowGuardBean
 */
public abstract class BaseTWorkflowGuardBean
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

    /** The value for the guardType field */
    private Integer guardType;

    /** The value for the guardParams field */
    private String guardParams;

    /** The value for the workflowTransition field */
    private Integer workflowTransition;

    /** The value for the role field */
    private Integer role;

    /** The value for the groovyScript field */
    private Integer groovyScript;

    /** The value for the person field */
    private Integer person;

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
     * Get the GuardType
     *
     * @return Integer
     */
    public Integer getGuardType ()
    {
        return guardType;
    }

    /**
     * Set the value of GuardType
     *
     * @param v new value
     */
    public void setGuardType(Integer v)
    {

        this.guardType = v;
        setModified(true);

    }

    /**
     * Get the GuardParams
     *
     * @return String
     */
    public String getGuardParams ()
    {
        return guardParams;
    }

    /**
     * Set the value of GuardParams
     *
     * @param v new value
     */
    public void setGuardParams(String v)
    {

        this.guardParams = v;
        setModified(true);

    }

    /**
     * Get the WorkflowTransition
     *
     * @return Integer
     */
    public Integer getWorkflowTransition ()
    {
        return workflowTransition;
    }

    /**
     * Set the value of WorkflowTransition
     *
     * @param v new value
     */
    public void setWorkflowTransition(Integer v)
    {

        this.workflowTransition = v;
        setModified(true);

    }

    /**
     * Get the Role
     *
     * @return Integer
     */
    public Integer getRole ()
    {
        return role;
    }

    /**
     * Set the value of Role
     *
     * @param v new value
     */
    public void setRole(Integer v)
    {

        this.role = v;
        setModified(true);

    }

    /**
     * Get the GroovyScript
     *
     * @return Integer
     */
    public Integer getGroovyScript ()
    {
        return groovyScript;
    }

    /**
     * Set the value of GroovyScript
     *
     * @param v new value
     */
    public void setGroovyScript(Integer v)
    {

        this.groovyScript = v;
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

    



    private TWorkflowTransitionBean aTWorkflowTransitionBean;

    /**
     * sets an associated TWorkflowTransitionBean object
     *
     * @param v TWorkflowTransitionBean
     */
    public void setTWorkflowTransitionBean(TWorkflowTransitionBean v)
    {
        if (v == null)
        {
            setWorkflowTransition((Integer) null);
        }
        else
        {
            setWorkflowTransition(v.getObjectID());
        }
        aTWorkflowTransitionBean = v;
    }


    /**
     * Get the associated TWorkflowTransitionBean object
     *
     * @return the associated TWorkflowTransitionBean object
     */
    public TWorkflowTransitionBean getTWorkflowTransitionBean()
    {
        return aTWorkflowTransitionBean;
    }





    private TRoleBean aTRoleBean;

    /**
     * sets an associated TRoleBean object
     *
     * @param v TRoleBean
     */
    public void setTRoleBean(TRoleBean v)
    {
        if (v == null)
        {
            setRole((Integer) null);
        }
        else
        {
            setRole(v.getObjectID());
        }
        aTRoleBean = v;
    }


    /**
     * Get the associated TRoleBean object
     *
     * @return the associated TRoleBean object
     */
    public TRoleBean getTRoleBean()
    {
        return aTRoleBean;
    }





    private TScriptsBean aTScriptsBean;

    /**
     * sets an associated TScriptsBean object
     *
     * @param v TScriptsBean
     */
    public void setTScriptsBean(TScriptsBean v)
    {
        if (v == null)
        {
            setGroovyScript((Integer) null);
        }
        else
        {
            setGroovyScript(v.getObjectID());
        }
        aTScriptsBean = v;
    }


    /**
     * Get the associated TScriptsBean object
     *
     * @return the associated TScriptsBean object
     */
    public TScriptsBean getTScriptsBean()
    {
        return aTScriptsBean;
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
            setPerson((Integer) null);
        }
        else
        {
            setPerson(v.getObjectID());
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



}
