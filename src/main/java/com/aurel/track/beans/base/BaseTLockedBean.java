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
 * extended; all references should be to TLockedBean
 */
public abstract class BaseTLockedBean
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
  
    /** The value for the workItem field */
    private Integer workItem;
  
    /** The value for the person field */
    private Integer person;
  
    /** The value for the httpSession field */
    private String httpSession;


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
     * Get the WorkItem
     *
     * @return Integer
     */
    public Integer getWorkItem ()
    {
        return workItem;
    }

    /**
     * Set the value of WorkItem
     *
     * @param v new value
     */
    public void setWorkItem(Integer v)
    {
  
        this.workItem = v;
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
     * Get the HttpSession
     *
     * @return String
     */
    public String getHttpSession ()
    {
        return httpSession;
    }

    /**
     * Set the value of HttpSession
     *
     * @param v new value
     */
    public void setHttpSession(String v)
    {
  
        this.httpSession = v;
        setModified(true);

    }

      

        }
