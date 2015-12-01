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

package com.aurel.track.persist;


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.torque.TorqueException;
import org.apache.torque.map.TableMap;
import org.apache.torque.om.BaseObject;
import org.apache.torque.om.ComboKey;
import org.apache.torque.om.DateKey;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.om.StringKey;
import org.apache.torque.om.Persistent;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;



import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TDocState;
import com.aurel.track.persist.TDocStatePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TDocStateBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TAttachment
 */
public abstract class BaseTAttachment extends TpBaseObject
{
    /** The Peer class */
    private static final TAttachmentPeer peer =
        new TAttachmentPeer();


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
     * Get the ObjectID
     *
     * @return Integer
     */
    public Integer getObjectID()
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

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }


    }

    /**
     * Get the WorkItem
     *
     * @return Integer
     */
    public Integer getWorkItem()
    {
        return workItem;
    }


    /**
     * Set the value of WorkItem
     *
     * @param v new value
     */
    public void setWorkItem(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workItem, v))
        {
            this.workItem = v;
            setModified(true);
        }


        if (aTWorkItem != null && !ObjectUtils.equals(aTWorkItem.getObjectID(), v))
        {
            aTWorkItem = null;
        }

    }

    /**
     * Get the ChangedBy
     *
     * @return Integer
     */
    public Integer getChangedBy()
    {
        return changedBy;
    }


    /**
     * Set the value of ChangedBy
     *
     * @param v new value
     */
    public void setChangedBy(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.changedBy, v))
        {
            this.changedBy = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the DocumentState
     *
     * @return Integer
     */
    public Integer getDocumentState()
    {
        return documentState;
    }


    /**
     * Set the value of DocumentState
     *
     * @param v new value
     */
    public void setDocumentState(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.documentState, v))
        {
            this.documentState = v;
            setModified(true);
        }


        if (aTDocState != null && !ObjectUtils.equals(aTDocState.getObjectID(), v))
        {
            aTDocState = null;
        }

    }

    /**
     * Get the FileName
     *
     * @return String
     */
    public String getFileName()
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

        if (!ObjectUtils.equals(this.fileName, v))
        {
            this.fileName = v;
            setModified(true);
        }


    }

    /**
     * Get the IsUrl
     *
     * @return String
     */
    public String getIsUrl()
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

        if (!ObjectUtils.equals(this.isUrl, v))
        {
            this.isUrl = v;
            setModified(true);
        }


    }

    /**
     * Get the FileSize
     *
     * @return String
     */
    public String getFileSize()
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

        if (!ObjectUtils.equals(this.fileSize, v))
        {
            this.fileSize = v;
            setModified(true);
        }


    }

    /**
     * Get the MimeType
     *
     * @return String
     */
    public String getMimeType()
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

        if (!ObjectUtils.equals(this.mimeType, v))
        {
            this.mimeType = v;
            setModified(true);
        }


    }

    /**
     * Get the LastEdit
     *
     * @return Date
     */
    public Date getLastEdit()
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

        if (!ObjectUtils.equals(this.lastEdit, v))
        {
            this.lastEdit = v;
            setModified(true);
        }


    }

    /**
     * Get the Version
     *
     * @return String
     */
    public String getVersion()
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

        if (!ObjectUtils.equals(this.version, v))
        {
            this.version = v;
            setModified(true);
        }


    }

    /**
     * Get the Description
     *
     * @return String
     */
    public String getDescription()
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

        if (!ObjectUtils.equals(this.description, v))
        {
            this.description = v;
            setModified(true);
        }


    }

    /**
     * Get the CryptKey
     *
     * @return String
     */
    public String getCryptKey()
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

        if (!ObjectUtils.equals(this.cryptKey, v))
        {
            this.cryptKey = v;
            setModified(true);
        }


    }

    /**
     * Get the IsEncrypted
     *
     * @return String
     */
    public String getIsEncrypted()
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

        if (!ObjectUtils.equals(this.isEncrypted, v))
        {
            this.isEncrypted = v;
            setModified(true);
        }


    }

    /**
     * Get the IsDeleted
     *
     * @return String
     */
    public String getIsDeleted()
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

        if (!ObjectUtils.equals(this.isDeleted, v))
        {
            this.isDeleted = v;
            setModified(true);
        }


    }

    /**
     * Get the Uuid
     *
     * @return String
     */
    public String getUuid()
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

        if (!ObjectUtils.equals(this.uuid, v))
        {
            this.uuid = v;
            setModified(true);
        }


    }

    



    private TWorkItem aTWorkItem;

    /**
     * Declares an association between this object and a TWorkItem object
     *
     * @param v TWorkItem
     * @throws TorqueException
     */
    public void setTWorkItem(TWorkItem v) throws TorqueException
    {
        if (v == null)
        {
            setWorkItem((Integer) null);
        }
        else
        {
            setWorkItem(v.getObjectID());
        }
        aTWorkItem = v;
    }


    /**
     * Returns the associated TWorkItem object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem()
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItem));
        }
        return aTWorkItem;
    }

    /**
     * Return the associated TWorkItem object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem(Connection connection)
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItem), connection);
        }
        return aTWorkItem;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkItemKey(ObjectKey key) throws TorqueException
    {

        setWorkItem(new Integer(((NumberKey) key).intValue()));
    }




    private TPerson aTPerson;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPerson(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setChangedBy((Integer) null);
        }
        else
        {
            setChangedBy(v.getObjectID());
        }
        aTPerson = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson()
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.changedBy, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.changedBy));
        }
        return aTPerson;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson(Connection connection)
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.changedBy, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.changedBy), connection);
        }
        return aTPerson;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonKey(ObjectKey key) throws TorqueException
    {

        setChangedBy(new Integer(((NumberKey) key).intValue()));
    }




    private TDocState aTDocState;

    /**
     * Declares an association between this object and a TDocState object
     *
     * @param v TDocState
     * @throws TorqueException
     */
    public void setTDocState(TDocState v) throws TorqueException
    {
        if (v == null)
        {
            setDocumentState((Integer) null);
        }
        else
        {
            setDocumentState(v.getObjectID());
        }
        aTDocState = v;
    }


    /**
     * Returns the associated TDocState object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TDocState object
     * @throws TorqueException
     */
    public TDocState getTDocState()
        throws TorqueException
    {
        if (aTDocState == null && (!ObjectUtils.equals(this.documentState, null)))
        {
            aTDocState = TDocStatePeer.retrieveByPK(SimpleKey.keyFor(this.documentState));
        }
        return aTDocState;
    }

    /**
     * Return the associated TDocState object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TDocState object
     * @throws TorqueException
     */
    public TDocState getTDocState(Connection connection)
        throws TorqueException
    {
        if (aTDocState == null && (!ObjectUtils.equals(this.documentState, null)))
        {
            aTDocState = TDocStatePeer.retrieveByPK(SimpleKey.keyFor(this.documentState), connection);
        }
        return aTDocState;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTDocStateKey(ObjectKey key) throws TorqueException
    {

        setDocumentState(new Integer(((NumberKey) key).intValue()));
    }
   
        
    private static List<String> fieldNames = null;

    /**
     * Generate a list of field names.
     *
     * @return a list of field names
     */
    public static synchronized List<String> getFieldNames()
    {
        if (fieldNames == null)
        {
            fieldNames = new ArrayList<String>();
            fieldNames.add("ObjectID");
            fieldNames.add("WorkItem");
            fieldNames.add("ChangedBy");
            fieldNames.add("DocumentState");
            fieldNames.add("FileName");
            fieldNames.add("IsUrl");
            fieldNames.add("FileSize");
            fieldNames.add("MimeType");
            fieldNames.add("LastEdit");
            fieldNames.add("Version");
            fieldNames.add("Description");
            fieldNames.add("CryptKey");
            fieldNames.add("IsEncrypted");
            fieldNames.add("IsDeleted");
            fieldNames.add("Uuid");
            fieldNames = Collections.unmodifiableList(fieldNames);
        }
        return fieldNames;
    }

    /**
     * Retrieves a field from the object by field (Java) name passed in as a String.
     *
     * @param name field name
     * @return value
     */
    public Object getByName(String name)
    {
        if (name.equals("ObjectID"))
        {
            return getObjectID();
        }
        if (name.equals("WorkItem"))
        {
            return getWorkItem();
        }
        if (name.equals("ChangedBy"))
        {
            return getChangedBy();
        }
        if (name.equals("DocumentState"))
        {
            return getDocumentState();
        }
        if (name.equals("FileName"))
        {
            return getFileName();
        }
        if (name.equals("IsUrl"))
        {
            return getIsUrl();
        }
        if (name.equals("FileSize"))
        {
            return getFileSize();
        }
        if (name.equals("MimeType"))
        {
            return getMimeType();
        }
        if (name.equals("LastEdit"))
        {
            return getLastEdit();
        }
        if (name.equals("Version"))
        {
            return getVersion();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("CryptKey"))
        {
            return getCryptKey();
        }
        if (name.equals("IsEncrypted"))
        {
            return getIsEncrypted();
        }
        if (name.equals("IsDeleted"))
        {
            return getIsDeleted();
        }
        if (name.equals("Uuid"))
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set a field in the object by field (Java) name.
     *
     * @param name field name
     * @param value field value
     * @return True if value was set, false if not (invalid name / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByName(String name, Object value )
        throws TorqueException, IllegalArgumentException
    {
        if (name.equals("ObjectID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setObjectID((Integer) value);
            return true;
        }
        if (name.equals("WorkItem"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkItem((Integer) value);
            return true;
        }
        if (name.equals("ChangedBy"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setChangedBy((Integer) value);
            return true;
        }
        if (name.equals("DocumentState"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDocumentState((Integer) value);
            return true;
        }
        if (name.equals("FileName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFileName((String) value);
            return true;
        }
        if (name.equals("IsUrl"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsUrl((String) value);
            return true;
        }
        if (name.equals("FileSize"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFileSize((String) value);
            return true;
        }
        if (name.equals("MimeType"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMimeType((String) value);
            return true;
        }
        if (name.equals("LastEdit"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLastEdit((Date) value);
            return true;
        }
        if (name.equals("Version"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setVersion((String) value);
            return true;
        }
        if (name.equals("Description"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDescription((String) value);
            return true;
        }
        if (name.equals("CryptKey"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCryptKey((String) value);
            return true;
        }
        if (name.equals("IsEncrypted"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsEncrypted((String) value);
            return true;
        }
        if (name.equals("IsDeleted"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsDeleted((String) value);
            return true;
        }
        if (name.equals("Uuid"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setUuid((String) value);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     *
     * @param name peer name
     * @return value
     */
    public Object getByPeerName(String name)
    {
        if (name.equals(TAttachmentPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TAttachmentPeer.WORKITEM))
        {
            return getWorkItem();
        }
        if (name.equals(TAttachmentPeer.CHANGEDBY))
        {
            return getChangedBy();
        }
        if (name.equals(TAttachmentPeer.DOCUMENTSTATE))
        {
            return getDocumentState();
        }
        if (name.equals(TAttachmentPeer.FILENAME))
        {
            return getFileName();
        }
        if (name.equals(TAttachmentPeer.ISURL))
        {
            return getIsUrl();
        }
        if (name.equals(TAttachmentPeer.FILESIZE))
        {
            return getFileSize();
        }
        if (name.equals(TAttachmentPeer.MIMETYPE))
        {
            return getMimeType();
        }
        if (name.equals(TAttachmentPeer.LASTEDIT))
        {
            return getLastEdit();
        }
        if (name.equals(TAttachmentPeer.VERSION))
        {
            return getVersion();
        }
        if (name.equals(TAttachmentPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TAttachmentPeer.CRYPTKEY))
        {
            return getCryptKey();
        }
        if (name.equals(TAttachmentPeer.ISENCRYPTED))
        {
            return getIsEncrypted();
        }
        if (name.equals(TAttachmentPeer.ISDELETED))
        {
            return getIsDeleted();
        }
        if (name.equals(TAttachmentPeer.TPUUID))
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set field values by Peer Field Name
     *
     * @param name field name
     * @param value field value
     * @return True if value was set, false if not (invalid name / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByPeerName(String name, Object value)
        throws TorqueException, IllegalArgumentException
    {
      if (TAttachmentPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TAttachmentPeer.WORKITEM.equals(name))
        {
            return setByName("WorkItem", value);
        }
      if (TAttachmentPeer.CHANGEDBY.equals(name))
        {
            return setByName("ChangedBy", value);
        }
      if (TAttachmentPeer.DOCUMENTSTATE.equals(name))
        {
            return setByName("DocumentState", value);
        }
      if (TAttachmentPeer.FILENAME.equals(name))
        {
            return setByName("FileName", value);
        }
      if (TAttachmentPeer.ISURL.equals(name))
        {
            return setByName("IsUrl", value);
        }
      if (TAttachmentPeer.FILESIZE.equals(name))
        {
            return setByName("FileSize", value);
        }
      if (TAttachmentPeer.MIMETYPE.equals(name))
        {
            return setByName("MimeType", value);
        }
      if (TAttachmentPeer.LASTEDIT.equals(name))
        {
            return setByName("LastEdit", value);
        }
      if (TAttachmentPeer.VERSION.equals(name))
        {
            return setByName("Version", value);
        }
      if (TAttachmentPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TAttachmentPeer.CRYPTKEY.equals(name))
        {
            return setByName("CryptKey", value);
        }
      if (TAttachmentPeer.ISENCRYPTED.equals(name))
        {
            return setByName("IsEncrypted", value);
        }
      if (TAttachmentPeer.ISDELETED.equals(name))
        {
            return setByName("IsDeleted", value);
        }
      if (TAttachmentPeer.TPUUID.equals(name))
        {
            return setByName("Uuid", value);
        }
        return false;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     *
     * @param pos position in xml schema
     * @return value
     */
    public Object getByPosition(int pos)
    {
        if (pos == 0)
        {
            return getObjectID();
        }
        if (pos == 1)
        {
            return getWorkItem();
        }
        if (pos == 2)
        {
            return getChangedBy();
        }
        if (pos == 3)
        {
            return getDocumentState();
        }
        if (pos == 4)
        {
            return getFileName();
        }
        if (pos == 5)
        {
            return getIsUrl();
        }
        if (pos == 6)
        {
            return getFileSize();
        }
        if (pos == 7)
        {
            return getMimeType();
        }
        if (pos == 8)
        {
            return getLastEdit();
        }
        if (pos == 9)
        {
            return getVersion();
        }
        if (pos == 10)
        {
            return getDescription();
        }
        if (pos == 11)
        {
            return getCryptKey();
        }
        if (pos == 12)
        {
            return getIsEncrypted();
        }
        if (pos == 13)
        {
            return getIsDeleted();
        }
        if (pos == 14)
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set field values by its position (zero based) in the XML schema.
     *
     * @param position The field position
     * @param value field value
     * @return True if value was set, false if not (invalid position / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByPosition(int position, Object value)
        throws TorqueException, IllegalArgumentException
    {
    if (position == 0)
        {
            return setByName("ObjectID", value);
        }
    if (position == 1)
        {
            return setByName("WorkItem", value);
        }
    if (position == 2)
        {
            return setByName("ChangedBy", value);
        }
    if (position == 3)
        {
            return setByName("DocumentState", value);
        }
    if (position == 4)
        {
            return setByName("FileName", value);
        }
    if (position == 5)
        {
            return setByName("IsUrl", value);
        }
    if (position == 6)
        {
            return setByName("FileSize", value);
        }
    if (position == 7)
        {
            return setByName("MimeType", value);
        }
    if (position == 8)
        {
            return setByName("LastEdit", value);
        }
    if (position == 9)
        {
            return setByName("Version", value);
        }
    if (position == 10)
        {
            return setByName("Description", value);
        }
    if (position == 11)
        {
            return setByName("CryptKey", value);
        }
    if (position == 12)
        {
            return setByName("IsEncrypted", value);
        }
    if (position == 13)
        {
            return setByName("IsDeleted", value);
        }
    if (position == 14)
        {
            return setByName("Uuid", value);
        }
        return false;
    }
     
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     *
     * @throws Exception
     */
    public void save() throws Exception
    {
        save(TAttachmentPeer.DATABASE_NAME);
    }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     * Note: this code is here because the method body is
     * auto-generated conditionally and therefore needs to be
     * in this file instead of in the super class, BaseObject.
     *
     * @param dbName
     * @throws TorqueException
     */
    public void save(String dbName) throws TorqueException
    {
        Connection con = null;
        try
        {
            con = Transaction.begin(dbName);
            save(con);
            Transaction.commit(con);
        }
        catch(TorqueException e)
        {
            Transaction.safeRollback(con);
            throw e;
        }
    }

    /** flag to prevent endless save loop, if this object is referenced
        by another object which falls in this transaction. */
    private boolean alreadyInSave = false;
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.  This method
     * is meant to be used as part of a transaction, otherwise use
     * the save() method and the connection details will be handled
     * internally
     *
     * @param con
     * @throws TorqueException
     */
    public void save(Connection con) throws TorqueException
    {
        if (!alreadyInSave)
        {
            alreadyInSave = true;



            // If this object has been modified, then save it to the database.
            if (isModified())
            {
                if (isNew())
                {
                    TAttachmentPeer.doInsert((TAttachment) this, con);
                    setNew(false);
                }
                else
                {
                    TAttachmentPeer.doUpdate((TAttachment) this, con);
                }
            }

            alreadyInSave = false;
        }
    }


    /**
     * Set the PrimaryKey using ObjectKey.
     *
     * @param key objectID ObjectKey
     */
    public void setPrimaryKey(ObjectKey key)
        
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) 
    {
        setObjectID(new Integer(key));
    }


    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
        return SimpleKey.keyFor(getObjectID());
    }
 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     */
    public TAttachment copy() throws TorqueException
    {
        return copy(true);
    }

    /**
     * Makes a copy of this object using connection.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     *
     * @param con the database connection to read associated objects.
     */
    public TAttachment copy(Connection con) throws TorqueException
    {
        return copy(true, con);
    }

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * If the parameter deepcopy is true, it then fills all the
     * association collections and sets the related objects to
     * isNew=true.
     *
     * @param deepcopy whether to copy the associated objects.
     */
    public TAttachment copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TAttachment(), deepcopy);
    }

    /**
     * Makes a copy of this object using connection.
     * It creates a new object filling in the simple attributes.
     * If the parameter deepcopy is true, it then fills all the
     * association collections and sets the related objects to
     * isNew=true.
     *
     * @param deepcopy whether to copy the associated objects.
     * @param con the database connection to read associated objects.
     */
    public TAttachment copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TAttachment(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TAttachment copyInto(TAttachment copyObj) throws TorqueException
    {
        return copyInto(copyObj, true);
    }

  
    /**
     * Fills the copyObj with the contents of this object using connection.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param con the database connection to read associated objects.
     */
    protected TAttachment copyInto(TAttachment copyObj, Connection con) throws TorqueException
    {
        return copyInto(copyObj, true, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * If deepcopy is true, The associated objects are also copied
     * and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param deepcopy whether the associated objects should be copied.
     */
    protected TAttachment copyInto(TAttachment copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkItem(workItem);
        copyObj.setChangedBy(changedBy);
        copyObj.setDocumentState(documentState);
        copyObj.setFileName(fileName);
        copyObj.setIsUrl(isUrl);
        copyObj.setFileSize(fileSize);
        copyObj.setMimeType(mimeType);
        copyObj.setLastEdit(lastEdit);
        copyObj.setVersion(version);
        copyObj.setDescription(description);
        copyObj.setCryptKey(cryptKey);
        copyObj.setIsEncrypted(isEncrypted);
        copyObj.setIsDeleted(isDeleted);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {
        }
        return copyObj;
    }
        
    
    /**
     * Fills the copyObj with the contents of this object using connection.
     * If deepcopy is true, The associated objects are also copied
     * and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param deepcopy whether the associated objects should be copied.
     * @param con the database connection to read associated objects.
     */
    protected TAttachment copyInto(TAttachment copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkItem(workItem);
        copyObj.setChangedBy(changedBy);
        copyObj.setDocumentState(documentState);
        copyObj.setFileName(fileName);
        copyObj.setIsUrl(isUrl);
        copyObj.setFileSize(fileSize);
        copyObj.setMimeType(mimeType);
        copyObj.setLastEdit(lastEdit);
        copyObj.setVersion(version);
        copyObj.setDescription(description);
        copyObj.setCryptKey(cryptKey);
        copyObj.setIsEncrypted(isEncrypted);
        copyObj.setIsDeleted(isDeleted);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TAttachmentPeer getPeer()
    {
        return peer;
    }

    /**
     * Retrieves the TableMap object related to this Table data without
     * compiler warnings of using getPeer().getTableMap().
     *
     * @return The associated TableMap object.
     */
    public TableMap getTableMap() throws TorqueException
    {
        return TAttachmentPeer.getTableMap();
    }

  
    /**
     * Creates a TAttachmentBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TAttachmentBean with the contents of this object
     */
    public TAttachmentBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TAttachmentBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TAttachmentBean with the contents of this object
     */
    public TAttachmentBean getBean(IdentityMap createdBeans)
    {
        TAttachmentBean result = (TAttachmentBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TAttachmentBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setWorkItem(getWorkItem());
        result.setChangedBy(getChangedBy());
        result.setDocumentState(getDocumentState());
        result.setFileName(getFileName());
        result.setIsUrl(getIsUrl());
        result.setFileSize(getFileSize());
        result.setMimeType(getMimeType());
        result.setLastEdit(getLastEdit());
        result.setVersion(getVersion());
        result.setDescription(getDescription());
        result.setCryptKey(getCryptKey());
        result.setIsEncrypted(getIsEncrypted());
        result.setIsDeleted(getIsDeleted());
        result.setUuid(getUuid());





        if (aTWorkItem != null)
        {
            TWorkItemBean relatedBean = aTWorkItem.getBean(createdBeans);
            result.setTWorkItemBean(relatedBean);
        }



        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTDocState != null)
        {
            TDocStateBean relatedBean = aTDocState.getBean(createdBeans);
            result.setTDocStateBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TAttachment with the contents
     * of a TAttachmentBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TAttachmentBean which contents are used to create
     *        the resulting class
     * @return an instance of TAttachment with the contents of bean
     */
    public static TAttachment createTAttachment(TAttachmentBean bean)
        throws TorqueException
    {
        return createTAttachment(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TAttachment with the contents
     * of a TAttachmentBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TAttachmentBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TAttachment with the contents of bean
     */

    public static TAttachment createTAttachment(TAttachmentBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TAttachment result = (TAttachment) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TAttachment();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setWorkItem(bean.getWorkItem());
        result.setChangedBy(bean.getChangedBy());
        result.setDocumentState(bean.getDocumentState());
        result.setFileName(bean.getFileName());
        result.setIsUrl(bean.getIsUrl());
        result.setFileSize(bean.getFileSize());
        result.setMimeType(bean.getMimeType());
        result.setLastEdit(bean.getLastEdit());
        result.setVersion(bean.getVersion());
        result.setDescription(bean.getDescription());
        result.setCryptKey(bean.getCryptKey());
        result.setIsEncrypted(bean.getIsEncrypted());
        result.setIsDeleted(bean.getIsDeleted());
        result.setUuid(bean.getUuid());





        {
            TWorkItemBean relatedBean = bean.getTWorkItemBean();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItem(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBean();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPerson(relatedObject);
            }
        }



        {
            TDocStateBean relatedBean = bean.getTDocStateBean();
            if (relatedBean != null)
            {
                TDocState relatedObject = TDocState.createTDocState(relatedBean, createdObjects);
                result.setTDocState(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TAttachment:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("WorkItem = ")
           .append(getWorkItem())
           .append("\n");
        str.append("ChangedBy = ")
           .append(getChangedBy())
           .append("\n");
        str.append("DocumentState = ")
           .append(getDocumentState())
           .append("\n");
        str.append("FileName = ")
           .append(getFileName())
           .append("\n");
        str.append("IsUrl = ")
           .append(getIsUrl())
           .append("\n");
        str.append("FileSize = ")
           .append(getFileSize())
           .append("\n");
        str.append("MimeType = ")
           .append(getMimeType())
           .append("\n");
        str.append("LastEdit = ")
           .append(getLastEdit())
           .append("\n");
        str.append("Version = ")
           .append(getVersion())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("CryptKey = ")
           .append(getCryptKey())
           .append("\n");
        str.append("IsEncrypted = ")
           .append(getIsEncrypted())
           .append("\n");
        str.append("IsDeleted = ")
           .append(getIsDeleted())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
