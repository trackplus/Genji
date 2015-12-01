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
 * extended; all references should be to TAttachmentBean
 */
public abstract class BaseTAttachmentBean
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

    /** The value for the changedBy field */
    private Integer changedBy;

    /** The value for the documentState field */
    private Integer documentState;

    /** The value for the fileName field */
    private String fileName;

    /** The value for the isUrl field */
    private String isUrl = "N";

    /** The value for the fileSize field */
    private String fileSize;

    /** The value for the mimeType field */
    private String mimeType;

    /** The value for the lastEdit field */
    private Date lastEdit;

    /** The value for the version field */
    private String version;

    /** The value for the description field */
    private String description;

    /** The value for the cryptKey field */
    private String cryptKey;

    /** The value for the isEncrypted field */
    private String isEncrypted = "N";

    /** The value for the isDeleted field */
    private String isDeleted = "N";

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
     * Get the ChangedBy
     *
     * @return Integer
     */
    public Integer getChangedBy ()
    {
        return changedBy;
    }

    /**
     * Set the value of ChangedBy
     *
     * @param v new value
     */
    public void setChangedBy(Integer v)
    {

        this.changedBy = v;
        setModified(true);

    }

    /**
     * Get the DocumentState
     *
     * @return Integer
     */
    public Integer getDocumentState ()
    {
        return documentState;
    }

    /**
     * Set the value of DocumentState
     *
     * @param v new value
     */
    public void setDocumentState(Integer v)
    {

        this.documentState = v;
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
     * Get the IsUrl
     *
     * @return String
     */
    public String getIsUrl ()
    {
        return isUrl;
    }

    /**
     * Set the value of IsUrl
     *
     * @param v new value
     */
    public void setIsUrl(String v)
    {

        this.isUrl = v;
        setModified(true);

    }

    /**
     * Get the FileSize
     *
     * @return String
     */
    public String getFileSize ()
    {
        return fileSize;
    }

    /**
     * Set the value of FileSize
     *
     * @param v new value
     */
    public void setFileSize(String v)
    {

        this.fileSize = v;
        setModified(true);

    }

    /**
     * Get the MimeType
     *
     * @return String
     */
    public String getMimeType ()
    {
        return mimeType;
    }

    /**
     * Set the value of MimeType
     *
     * @param v new value
     */
    public void setMimeType(String v)
    {

        this.mimeType = v;
        setModified(true);

    }

    /**
     * Get the LastEdit
     *
     * @return Date
     */
    public Date getLastEdit ()
    {
        return lastEdit;
    }

    /**
     * Set the value of LastEdit
     *
     * @param v new value
     */
    public void setLastEdit(Date v)
    {

        this.lastEdit = v;
        setModified(true);

    }

    /**
     * Get the Version
     *
     * @return String
     */
    public String getVersion ()
    {
        return version;
    }

    /**
     * Set the value of Version
     *
     * @param v new value
     */
    public void setVersion(String v)
    {

        this.version = v;
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
     * Get the CryptKey
     *
     * @return String
     */
    public String getCryptKey ()
    {
        return cryptKey;
    }

    /**
     * Set the value of CryptKey
     *
     * @param v new value
     */
    public void setCryptKey(String v)
    {

        this.cryptKey = v;
        setModified(true);

    }

    /**
     * Get the IsEncrypted
     *
     * @return String
     */
    public String getIsEncrypted ()
    {
        return isEncrypted;
    }

    /**
     * Set the value of IsEncrypted
     *
     * @param v new value
     */
    public void setIsEncrypted(String v)
    {

        this.isEncrypted = v;
        setModified(true);

    }

    /**
     * Get the IsDeleted
     *
     * @return String
     */
    public String getIsDeleted ()
    {
        return isDeleted;
    }

    /**
     * Set the value of IsDeleted
     *
     * @param v new value
     */
    public void setIsDeleted(String v)
    {

        this.isDeleted = v;
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

    



    private TWorkItemBean aTWorkItemBean;

    /**
     * sets an associated TWorkItemBean object
     *
     * @param v TWorkItemBean
     */
    public void setTWorkItemBean(TWorkItemBean v)
    {
        if (v == null)
        {
            setWorkItem((Integer) null);
        }
        else
        {
            setWorkItem(v.getObjectID());
        }
        aTWorkItemBean = v;
    }


    /**
     * Get the associated TWorkItemBean object
     *
     * @return the associated TWorkItemBean object
     */
    public TWorkItemBean getTWorkItemBean()
    {
        return aTWorkItemBean;
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
            setChangedBy((Integer) null);
        }
        else
        {
            setChangedBy(v.getObjectID());
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





    private TDocStateBean aTDocStateBean;

    /**
     * sets an associated TDocStateBean object
     *
     * @param v TDocStateBean
     */
    public void setTDocStateBean(TDocStateBean v)
    {
        if (v == null)
        {
            setDocumentState((Integer) null);
        }
        else
        {
            setDocumentState(v.getObjectID());
        }
        aTDocStateBean = v;
    }


    /**
     * Get the associated TDocStateBean object
     *
     * @return the associated TDocStateBean object
     */
    public TDocStateBean getTDocStateBean()
    {
        return aTDocStateBean;
    }



}
