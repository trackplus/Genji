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
 * Comments from the version control system
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TRevisionBean
 */
public abstract class BaseTRevisionBean
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

    /** The value for the fileName field */
    private String fileName;

    /** The value for the authorName field */
    private String authorName;

    /** The value for the changeDescription field */
    private String changeDescription;

    /** The value for the revisionDate field */
    private Date revisionDate;

    /** The value for the revisionNumber field */
    private String revisionNumber;

    /** The value for the repositoryKey field */
    private Integer repositoryKey;

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
     * Get the FileName
     *
     * @return String
     */
    public String getFileName ()
    {
        return fileName;
    }

    /**
     * Set the value of FileName
     *
     * @param v new value
     */
    public void setFileName(String v)
    {

        this.fileName = v;
        setModified(true);

    }

    /**
     * Get the AuthorName
     *
     * @return String
     */
    public String getAuthorName ()
    {
        return authorName;
    }

    /**
     * Set the value of AuthorName
     *
     * @param v new value
     */
    public void setAuthorName(String v)
    {

        this.authorName = v;
        setModified(true);

    }

    /**
     * Get the ChangeDescription
     *
     * @return String
     */
    public String getChangeDescription ()
    {
        return changeDescription;
    }

    /**
     * Set the value of ChangeDescription
     *
     * @param v new value
     */
    public void setChangeDescription(String v)
    {

        this.changeDescription = v;
        setModified(true);

    }

    /**
     * Get the RevisionDate
     *
     * @return Date
     */
    public Date getRevisionDate ()
    {
        return revisionDate;
    }

    /**
     * Set the value of RevisionDate
     *
     * @param v new value
     */
    public void setRevisionDate(Date v)
    {

        this.revisionDate = v;
        setModified(true);

    }

    /**
     * Get the RevisionNumber
     *
     * @return String
     */
    public String getRevisionNumber ()
    {
        return revisionNumber;
    }

    /**
     * Set the value of RevisionNumber
     *
     * @param v new value
     */
    public void setRevisionNumber(String v)
    {

        this.revisionNumber = v;
        setModified(true);

    }

    /**
     * Get the RepositoryKey
     *
     * @return Integer
     */
    public Integer getRepositoryKey ()
    {
        return repositoryKey;
    }

    /**
     * Set the value of RepositoryKey
     *
     * @param v new value
     */
    public void setRepositoryKey(Integer v)
    {

        this.repositoryKey = v;
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

    



    private TRepositoryBean aTRepositoryBean;

    /**
     * sets an associated TRepositoryBean object
     *
     * @param v TRepositoryBean
     */
    public void setTRepositoryBean(TRepositoryBean v)
    {
        if (v == null)
        {
            setRepositoryKey((Integer) null);
        }
        else
        {
            setRepositoryKey(v.getObjectID());
        }
        aTRepositoryBean = v;
    }


    /**
     * Get the associated TRepositoryBean object
     *
     * @return the associated TRepositoryBean object
     */
    public TRepositoryBean getTRepositoryBean()
    {
        return aTRepositoryBean;
    }





    /**
     * Collection to store aggregation of collTRevisionWorkitemsBeans
     */
    protected List<TRevisionWorkitemsBean> collTRevisionWorkitemsBeans;

    /**
     * Returns the collection of TRevisionWorkitemsBeans
     */
    public List<TRevisionWorkitemsBean> getTRevisionWorkitemsBeans()
    {
        return collTRevisionWorkitemsBeans;
    }

    /**
     * Sets the collection of TRevisionWorkitemsBeans to the specified value
     */
    public void setTRevisionWorkitemsBeans(List<TRevisionWorkitemsBean> list)
    {
        if (list == null)
        {
            collTRevisionWorkitemsBeans = null;
        }
        else
        {
            collTRevisionWorkitemsBeans = new ArrayList<TRevisionWorkitemsBean>(list);
        }
    }

}
