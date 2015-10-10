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
 * Describes that a parent item issue type which issue types can have as child
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TChildIssueTypeBean
 */
public abstract class BaseTChildIssueTypeBean
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

    /** The value for the issueTypeParent field */
    private Integer issueTypeParent;

    /** The value for the issueTypeChild field */
    private Integer issueTypeChild;

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
     * Get the IssueTypeParent
     *
     * @return Integer
     */
    public Integer getIssueTypeParent ()
    {
        return issueTypeParent;
    }

    /**
     * Set the value of IssueTypeParent
     *
     * @param v new value
     */
    public void setIssueTypeParent(Integer v)
    {

        this.issueTypeParent = v;
        setModified(true);

    }

    /**
     * Get the IssueTypeChild
     *
     * @return Integer
     */
    public Integer getIssueTypeChild ()
    {
        return issueTypeChild;
    }

    /**
     * Set the value of IssueTypeChild
     *
     * @param v new value
     */
    public void setIssueTypeChild(Integer v)
    {

        this.issueTypeChild = v;
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

    



    private TListTypeBean aTListTypeBeanRelatedByIssueTypeParent;

    /**
     * sets an associated TListTypeBean object
     *
     * @param v TListTypeBean
     */
    public void setTListTypeBeanRelatedByIssueTypeParent(TListTypeBean v)
    {
        if (v == null)
        {
            setIssueTypeParent((Integer) null);
        }
        else
        {
            setIssueTypeParent(v.getObjectID());
        }
        aTListTypeBeanRelatedByIssueTypeParent = v;
    }


    /**
     * Get the associated TListTypeBean object
     *
     * @return the associated TListTypeBean object
     */
    public TListTypeBean getTListTypeBeanRelatedByIssueTypeParent()
    {
        return aTListTypeBeanRelatedByIssueTypeParent;
    }





    private TListTypeBean aTListTypeBeanRelatedByIssueTypeChild;

    /**
     * sets an associated TListTypeBean object
     *
     * @param v TListTypeBean
     */
    public void setTListTypeBeanRelatedByIssueTypeChild(TListTypeBean v)
    {
        if (v == null)
        {
            setIssueTypeChild((Integer) null);
        }
        else
        {
            setIssueTypeChild(v.getObjectID());
        }
        aTListTypeBeanRelatedByIssueTypeChild = v;
    }


    /**
     * Get the associated TListTypeBean object
     *
     * @return the associated TListTypeBean object
     */
    public TListTypeBean getTListTypeBeanRelatedByIssueTypeChild()
    {
        return aTListTypeBeanRelatedByIssueTypeChild;
    }



}
