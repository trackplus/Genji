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



import com.aurel.track.persist.TVersionControl;
import com.aurel.track.persist.TVersionControlPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TVersionControlBean;
import com.aurel.track.beans.TVersionControlBean;



/**
 * configuration for version control server
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TVersionControl
 */
public abstract class BaseTVersionControl extends TpBaseObject
{
    /** The Peer class */
    private static final TVersionControlPeer peer =
        new TVersionControlPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the vCType field */
    private Integer vCType;

    /** The value for the repositoryBrowser field */
    private Integer repositoryBrowser;

    /** The value for the changeSetLink field */
    private String changeSetLink;

    /** The value for the addedFilesLink field */
    private String addedFilesLink;

    /** The value for the modifiedFilesLink field */
    private String modifiedFilesLink;

    /** The value for the replacedFilesLink field */
    private String replacedFilesLink;

    /** The value for the deletedFilesLink field */
    private String deletedFilesLink;

    /** The value for the connectionType field */
    private Integer connectionType;

    /** The value for the serverName field */
    private String serverName;

    /** The value for the repositoryPath field */
    private String repositoryPath;

    /** The value for the defaultServerPort field */
    private String defaultServerPort = "Y";

    /** The value for the serverPort field */
    private Integer serverPort;

    /** The value for the authenticationMode field */
    private Integer authenticationMode;

    /** The value for the userName field */
    private String userName;

    /** The value for the password field */
    private String password;

    /** The value for the privateKey field */
    private String privateKey;

    /** The value for the passphrase field */
    private String passphrase;

    /** The value for the parent field */
    private Integer parent;

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
    public void setObjectID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }


    }

    /**
     * Get the VCType
     *
     * @return Integer
     */
    public Integer getVCType()
    {
        return vCType;
    }


    /**
     * Set the value of VCType
     *
     * @param v new value
     */
    public void setVCType(Integer v) 
    {

        if (!ObjectUtils.equals(this.vCType, v))
        {
            this.vCType = v;
            setModified(true);
        }


    }

    /**
     * Get the RepositoryBrowser
     *
     * @return Integer
     */
    public Integer getRepositoryBrowser()
    {
        return repositoryBrowser;
    }


    /**
     * Set the value of RepositoryBrowser
     *
     * @param v new value
     */
    public void setRepositoryBrowser(Integer v) 
    {

        if (!ObjectUtils.equals(this.repositoryBrowser, v))
        {
            this.repositoryBrowser = v;
            setModified(true);
        }


    }

    /**
     * Get the ChangeSetLink
     *
     * @return String
     */
    public String getChangeSetLink()
    {
        return changeSetLink;
    }


    /**
     * Set the value of ChangeSetLink
     *
     * @param v new value
     */
    public void setChangeSetLink(String v) 
    {

        if (!ObjectUtils.equals(this.changeSetLink, v))
        {
            this.changeSetLink = v;
            setModified(true);
        }


    }

    /**
     * Get the AddedFilesLink
     *
     * @return String
     */
    public String getAddedFilesLink()
    {
        return addedFilesLink;
    }


    /**
     * Set the value of AddedFilesLink
     *
     * @param v new value
     */
    public void setAddedFilesLink(String v) 
    {

        if (!ObjectUtils.equals(this.addedFilesLink, v))
        {
            this.addedFilesLink = v;
            setModified(true);
        }


    }

    /**
     * Get the ModifiedFilesLink
     *
     * @return String
     */
    public String getModifiedFilesLink()
    {
        return modifiedFilesLink;
    }


    /**
     * Set the value of ModifiedFilesLink
     *
     * @param v new value
     */
    public void setModifiedFilesLink(String v) 
    {

        if (!ObjectUtils.equals(this.modifiedFilesLink, v))
        {
            this.modifiedFilesLink = v;
            setModified(true);
        }


    }

    /**
     * Get the ReplacedFilesLink
     *
     * @return String
     */
    public String getReplacedFilesLink()
    {
        return replacedFilesLink;
    }


    /**
     * Set the value of ReplacedFilesLink
     *
     * @param v new value
     */
    public void setReplacedFilesLink(String v) 
    {

        if (!ObjectUtils.equals(this.replacedFilesLink, v))
        {
            this.replacedFilesLink = v;
            setModified(true);
        }


    }

    /**
     * Get the DeletedFilesLink
     *
     * @return String
     */
    public String getDeletedFilesLink()
    {
        return deletedFilesLink;
    }


    /**
     * Set the value of DeletedFilesLink
     *
     * @param v new value
     */
    public void setDeletedFilesLink(String v) 
    {

        if (!ObjectUtils.equals(this.deletedFilesLink, v))
        {
            this.deletedFilesLink = v;
            setModified(true);
        }


    }

    /**
     * Get the ConnectionType
     *
     * @return Integer
     */
    public Integer getConnectionType()
    {
        return connectionType;
    }


    /**
     * Set the value of ConnectionType
     *
     * @param v new value
     */
    public void setConnectionType(Integer v) 
    {

        if (!ObjectUtils.equals(this.connectionType, v))
        {
            this.connectionType = v;
            setModified(true);
        }


    }

    /**
     * Get the ServerName
     *
     * @return String
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Set the value of ServerName
     *
     * @param v new value
     */
    public void setServerName(String v) 
    {

        if (!ObjectUtils.equals(this.serverName, v))
        {
            this.serverName = v;
            setModified(true);
        }


    }

    /**
     * Get the RepositoryPath
     *
     * @return String
     */
    public String getRepositoryPath()
    {
        return repositoryPath;
    }


    /**
     * Set the value of RepositoryPath
     *
     * @param v new value
     */
    public void setRepositoryPath(String v) 
    {

        if (!ObjectUtils.equals(this.repositoryPath, v))
        {
            this.repositoryPath = v;
            setModified(true);
        }


    }

    /**
     * Get the DefaultServerPort
     *
     * @return String
     */
    public String getDefaultServerPort()
    {
        return defaultServerPort;
    }


    /**
     * Set the value of DefaultServerPort
     *
     * @param v new value
     */
    public void setDefaultServerPort(String v) 
    {

        if (!ObjectUtils.equals(this.defaultServerPort, v))
        {
            this.defaultServerPort = v;
            setModified(true);
        }


    }

    /**
     * Get the ServerPort
     *
     * @return Integer
     */
    public Integer getServerPort()
    {
        return serverPort;
    }


    /**
     * Set the value of ServerPort
     *
     * @param v new value
     */
    public void setServerPort(Integer v) 
    {

        if (!ObjectUtils.equals(this.serverPort, v))
        {
            this.serverPort = v;
            setModified(true);
        }


    }

    /**
     * Get the AuthenticationMode
     *
     * @return Integer
     */
    public Integer getAuthenticationMode()
    {
        return authenticationMode;
    }


    /**
     * Set the value of AuthenticationMode
     *
     * @param v new value
     */
    public void setAuthenticationMode(Integer v) 
    {

        if (!ObjectUtils.equals(this.authenticationMode, v))
        {
            this.authenticationMode = v;
            setModified(true);
        }


    }

    /**
     * Get the UserName
     *
     * @return String
     */
    public String getUserName()
    {
        return userName;
    }


    /**
     * Set the value of UserName
     *
     * @param v new value
     */
    public void setUserName(String v) 
    {

        if (!ObjectUtils.equals(this.userName, v))
        {
            this.userName = v;
            setModified(true);
        }


    }

    /**
     * Get the Password
     *
     * @return String
     */
    public String getPassword()
    {
        return password;
    }


    /**
     * Set the value of Password
     *
     * @param v new value
     */
    public void setPassword(String v) 
    {

        if (!ObjectUtils.equals(this.password, v))
        {
            this.password = v;
            setModified(true);
        }


    }

    /**
     * Get the PrivateKey
     *
     * @return String
     */
    public String getPrivateKey()
    {
        return privateKey;
    }


    /**
     * Set the value of PrivateKey
     *
     * @param v new value
     */
    public void setPrivateKey(String v) 
    {

        if (!ObjectUtils.equals(this.privateKey, v))
        {
            this.privateKey = v;
            setModified(true);
        }


    }

    /**
     * Get the Passphrase
     *
     * @return String
     */
    public String getPassphrase()
    {
        return passphrase;
    }


    /**
     * Set the value of Passphrase
     *
     * @param v new value
     */
    public void setPassphrase(String v) 
    {

        if (!ObjectUtils.equals(this.passphrase, v))
        {
            this.passphrase = v;
            setModified(true);
        }


    }

    /**
     * Get the Parent
     *
     * @return Integer
     */
    public Integer getParent()
    {
        return parent;
    }


    /**
     * Set the value of Parent
     *
     * @param v new value
     */
    public void setParent(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.parent, v))
        {
            this.parent = v;
            setModified(true);
        }


        if (aTVersionControlRelatedByParent != null && !ObjectUtils.equals(aTVersionControlRelatedByParent.getObjectID(), v))
        {
            aTVersionControlRelatedByParent = null;
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

    



    private TVersionControl aTVersionControlRelatedByParent;

    /**
     * Declares an association between this object and a TVersionControl object
     *
     * @param v TVersionControl
     * @throws TorqueException
     */
    public void setTVersionControlRelatedByParent(TVersionControl v) throws TorqueException
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTVersionControlRelatedByParent = v;
    }


    /**
     * Returns the associated TVersionControl object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TVersionControl object
     * @throws TorqueException
     */
    public TVersionControl getTVersionControlRelatedByParent()
        throws TorqueException
    {
        if (aTVersionControlRelatedByParent == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTVersionControlRelatedByParent = TVersionControlPeer.retrieveByPK(SimpleKey.keyFor(this.parent));
        }
        return aTVersionControlRelatedByParent;
    }

    /**
     * Return the associated TVersionControl object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TVersionControl object
     * @throws TorqueException
     */
    public TVersionControl getTVersionControlRelatedByParent(Connection connection)
        throws TorqueException
    {
        if (aTVersionControlRelatedByParent == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTVersionControlRelatedByParent = TVersionControlPeer.retrieveByPK(SimpleKey.keyFor(this.parent), connection);
        }
        return aTVersionControlRelatedByParent;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTVersionControlRelatedByParentKey(ObjectKey key) throws TorqueException
    {

        setParent(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("VCType");
            fieldNames.add("RepositoryBrowser");
            fieldNames.add("ChangeSetLink");
            fieldNames.add("AddedFilesLink");
            fieldNames.add("ModifiedFilesLink");
            fieldNames.add("ReplacedFilesLink");
            fieldNames.add("DeletedFilesLink");
            fieldNames.add("ConnectionType");
            fieldNames.add("ServerName");
            fieldNames.add("RepositoryPath");
            fieldNames.add("DefaultServerPort");
            fieldNames.add("ServerPort");
            fieldNames.add("AuthenticationMode");
            fieldNames.add("UserName");
            fieldNames.add("Password");
            fieldNames.add("PrivateKey");
            fieldNames.add("Passphrase");
            fieldNames.add("Parent");
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
        if (name.equals("VCType"))
        {
            return getVCType();
        }
        if (name.equals("RepositoryBrowser"))
        {
            return getRepositoryBrowser();
        }
        if (name.equals("ChangeSetLink"))
        {
            return getChangeSetLink();
        }
        if (name.equals("AddedFilesLink"))
        {
            return getAddedFilesLink();
        }
        if (name.equals("ModifiedFilesLink"))
        {
            return getModifiedFilesLink();
        }
        if (name.equals("ReplacedFilesLink"))
        {
            return getReplacedFilesLink();
        }
        if (name.equals("DeletedFilesLink"))
        {
            return getDeletedFilesLink();
        }
        if (name.equals("ConnectionType"))
        {
            return getConnectionType();
        }
        if (name.equals("ServerName"))
        {
            return getServerName();
        }
        if (name.equals("RepositoryPath"))
        {
            return getRepositoryPath();
        }
        if (name.equals("DefaultServerPort"))
        {
            return getDefaultServerPort();
        }
        if (name.equals("ServerPort"))
        {
            return getServerPort();
        }
        if (name.equals("AuthenticationMode"))
        {
            return getAuthenticationMode();
        }
        if (name.equals("UserName"))
        {
            return getUserName();
        }
        if (name.equals("Password"))
        {
            return getPassword();
        }
        if (name.equals("PrivateKey"))
        {
            return getPrivateKey();
        }
        if (name.equals("Passphrase"))
        {
            return getPassphrase();
        }
        if (name.equals("Parent"))
        {
            return getParent();
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
        if (name.equals("VCType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setVCType((Integer) value);
            return true;
        }
        if (name.equals("RepositoryBrowser"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRepositoryBrowser((Integer) value);
            return true;
        }
        if (name.equals("ChangeSetLink"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setChangeSetLink((String) value);
            return true;
        }
        if (name.equals("AddedFilesLink"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAddedFilesLink((String) value);
            return true;
        }
        if (name.equals("ModifiedFilesLink"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setModifiedFilesLink((String) value);
            return true;
        }
        if (name.equals("ReplacedFilesLink"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReplacedFilesLink((String) value);
            return true;
        }
        if (name.equals("DeletedFilesLink"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDeletedFilesLink((String) value);
            return true;
        }
        if (name.equals("ConnectionType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setConnectionType((Integer) value);
            return true;
        }
        if (name.equals("ServerName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setServerName((String) value);
            return true;
        }
        if (name.equals("RepositoryPath"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRepositoryPath((String) value);
            return true;
        }
        if (name.equals("DefaultServerPort"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultServerPort((String) value);
            return true;
        }
        if (name.equals("ServerPort"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setServerPort((Integer) value);
            return true;
        }
        if (name.equals("AuthenticationMode"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAuthenticationMode((Integer) value);
            return true;
        }
        if (name.equals("UserName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setUserName((String) value);
            return true;
        }
        if (name.equals("Password"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPassword((String) value);
            return true;
        }
        if (name.equals("PrivateKey"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPrivateKey((String) value);
            return true;
        }
        if (name.equals("Passphrase"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPassphrase((String) value);
            return true;
        }
        if (name.equals("Parent"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParent((Integer) value);
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
        if (name.equals(TVersionControlPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TVersionControlPeer.VCTYPE))
        {
            return getVCType();
        }
        if (name.equals(TVersionControlPeer.REPOSITORYBROWSER))
        {
            return getRepositoryBrowser();
        }
        if (name.equals(TVersionControlPeer.CHANGESETLINK))
        {
            return getChangeSetLink();
        }
        if (name.equals(TVersionControlPeer.ADDEDFILESLINK))
        {
            return getAddedFilesLink();
        }
        if (name.equals(TVersionControlPeer.MODIFIEDEDFILESLINK))
        {
            return getModifiedFilesLink();
        }
        if (name.equals(TVersionControlPeer.REPLACEDFILESLINK))
        {
            return getReplacedFilesLink();
        }
        if (name.equals(TVersionControlPeer.DELETEDFILESLINK))
        {
            return getDeletedFilesLink();
        }
        if (name.equals(TVersionControlPeer.CONNECTIONTYPE))
        {
            return getConnectionType();
        }
        if (name.equals(TVersionControlPeer.SERVERNAME))
        {
            return getServerName();
        }
        if (name.equals(TVersionControlPeer.REPOSITORYPATH))
        {
            return getRepositoryPath();
        }
        if (name.equals(TVersionControlPeer.DEFAULTSEVERPORT))
        {
            return getDefaultServerPort();
        }
        if (name.equals(TVersionControlPeer.SEVERPORT))
        {
            return getServerPort();
        }
        if (name.equals(TVersionControlPeer.AUTHENTICATIONMODE))
        {
            return getAuthenticationMode();
        }
        if (name.equals(TVersionControlPeer.USERNAME))
        {
            return getUserName();
        }
        if (name.equals(TVersionControlPeer.PASSWORD))
        {
            return getPassword();
        }
        if (name.equals(TVersionControlPeer.PRIVATEKEY))
        {
            return getPrivateKey();
        }
        if (name.equals(TVersionControlPeer.PASSPHRASE))
        {
            return getPassphrase();
        }
        if (name.equals(TVersionControlPeer.PARENT))
        {
            return getParent();
        }
        if (name.equals(TVersionControlPeer.TPUUID))
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
      if (TVersionControlPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TVersionControlPeer.VCTYPE.equals(name))
        {
            return setByName("VCType", value);
        }
      if (TVersionControlPeer.REPOSITORYBROWSER.equals(name))
        {
            return setByName("RepositoryBrowser", value);
        }
      if (TVersionControlPeer.CHANGESETLINK.equals(name))
        {
            return setByName("ChangeSetLink", value);
        }
      if (TVersionControlPeer.ADDEDFILESLINK.equals(name))
        {
            return setByName("AddedFilesLink", value);
        }
      if (TVersionControlPeer.MODIFIEDEDFILESLINK.equals(name))
        {
            return setByName("ModifiedFilesLink", value);
        }
      if (TVersionControlPeer.REPLACEDFILESLINK.equals(name))
        {
            return setByName("ReplacedFilesLink", value);
        }
      if (TVersionControlPeer.DELETEDFILESLINK.equals(name))
        {
            return setByName("DeletedFilesLink", value);
        }
      if (TVersionControlPeer.CONNECTIONTYPE.equals(name))
        {
            return setByName("ConnectionType", value);
        }
      if (TVersionControlPeer.SERVERNAME.equals(name))
        {
            return setByName("ServerName", value);
        }
      if (TVersionControlPeer.REPOSITORYPATH.equals(name))
        {
            return setByName("RepositoryPath", value);
        }
      if (TVersionControlPeer.DEFAULTSEVERPORT.equals(name))
        {
            return setByName("DefaultServerPort", value);
        }
      if (TVersionControlPeer.SEVERPORT.equals(name))
        {
            return setByName("ServerPort", value);
        }
      if (TVersionControlPeer.AUTHENTICATIONMODE.equals(name))
        {
            return setByName("AuthenticationMode", value);
        }
      if (TVersionControlPeer.USERNAME.equals(name))
        {
            return setByName("UserName", value);
        }
      if (TVersionControlPeer.PASSWORD.equals(name))
        {
            return setByName("Password", value);
        }
      if (TVersionControlPeer.PRIVATEKEY.equals(name))
        {
            return setByName("PrivateKey", value);
        }
      if (TVersionControlPeer.PASSPHRASE.equals(name))
        {
            return setByName("Passphrase", value);
        }
      if (TVersionControlPeer.PARENT.equals(name))
        {
            return setByName("Parent", value);
        }
      if (TVersionControlPeer.TPUUID.equals(name))
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
            return getVCType();
        }
        if (pos == 2)
        {
            return getRepositoryBrowser();
        }
        if (pos == 3)
        {
            return getChangeSetLink();
        }
        if (pos == 4)
        {
            return getAddedFilesLink();
        }
        if (pos == 5)
        {
            return getModifiedFilesLink();
        }
        if (pos == 6)
        {
            return getReplacedFilesLink();
        }
        if (pos == 7)
        {
            return getDeletedFilesLink();
        }
        if (pos == 8)
        {
            return getConnectionType();
        }
        if (pos == 9)
        {
            return getServerName();
        }
        if (pos == 10)
        {
            return getRepositoryPath();
        }
        if (pos == 11)
        {
            return getDefaultServerPort();
        }
        if (pos == 12)
        {
            return getServerPort();
        }
        if (pos == 13)
        {
            return getAuthenticationMode();
        }
        if (pos == 14)
        {
            return getUserName();
        }
        if (pos == 15)
        {
            return getPassword();
        }
        if (pos == 16)
        {
            return getPrivateKey();
        }
        if (pos == 17)
        {
            return getPassphrase();
        }
        if (pos == 18)
        {
            return getParent();
        }
        if (pos == 19)
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
            return setByName("VCType", value);
        }
    if (position == 2)
        {
            return setByName("RepositoryBrowser", value);
        }
    if (position == 3)
        {
            return setByName("ChangeSetLink", value);
        }
    if (position == 4)
        {
            return setByName("AddedFilesLink", value);
        }
    if (position == 5)
        {
            return setByName("ModifiedFilesLink", value);
        }
    if (position == 6)
        {
            return setByName("ReplacedFilesLink", value);
        }
    if (position == 7)
        {
            return setByName("DeletedFilesLink", value);
        }
    if (position == 8)
        {
            return setByName("ConnectionType", value);
        }
    if (position == 9)
        {
            return setByName("ServerName", value);
        }
    if (position == 10)
        {
            return setByName("RepositoryPath", value);
        }
    if (position == 11)
        {
            return setByName("DefaultServerPort", value);
        }
    if (position == 12)
        {
            return setByName("ServerPort", value);
        }
    if (position == 13)
        {
            return setByName("AuthenticationMode", value);
        }
    if (position == 14)
        {
            return setByName("UserName", value);
        }
    if (position == 15)
        {
            return setByName("Password", value);
        }
    if (position == 16)
        {
            return setByName("PrivateKey", value);
        }
    if (position == 17)
        {
            return setByName("Passphrase", value);
        }
    if (position == 18)
        {
            return setByName("Parent", value);
        }
    if (position == 19)
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
        save(TVersionControlPeer.DATABASE_NAME);
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
                    TVersionControlPeer.doInsert((TVersionControl) this, con);
                    setNew(false);
                }
                else
                {
                    TVersionControlPeer.doUpdate((TVersionControl) this, con);
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
        throws TorqueException
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
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
    public TVersionControl copy() throws TorqueException
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
    public TVersionControl copy(Connection con) throws TorqueException
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
    public TVersionControl copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TVersionControl(), deepcopy);
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
    public TVersionControl copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TVersionControl(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TVersionControl copyInto(TVersionControl copyObj) throws TorqueException
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
    protected TVersionControl copyInto(TVersionControl copyObj, Connection con) throws TorqueException
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
    protected TVersionControl copyInto(TVersionControl copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setVCType(vCType);
        copyObj.setRepositoryBrowser(repositoryBrowser);
        copyObj.setChangeSetLink(changeSetLink);
        copyObj.setAddedFilesLink(addedFilesLink);
        copyObj.setModifiedFilesLink(modifiedFilesLink);
        copyObj.setReplacedFilesLink(replacedFilesLink);
        copyObj.setDeletedFilesLink(deletedFilesLink);
        copyObj.setConnectionType(connectionType);
        copyObj.setServerName(serverName);
        copyObj.setRepositoryPath(repositoryPath);
        copyObj.setDefaultServerPort(defaultServerPort);
        copyObj.setServerPort(serverPort);
        copyObj.setAuthenticationMode(authenticationMode);
        copyObj.setUserName(userName);
        copyObj.setPassword(password);
        copyObj.setPrivateKey(privateKey);
        copyObj.setPassphrase(passphrase);
        copyObj.setParent(parent);
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
    protected TVersionControl copyInto(TVersionControl copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setVCType(vCType);
        copyObj.setRepositoryBrowser(repositoryBrowser);
        copyObj.setChangeSetLink(changeSetLink);
        copyObj.setAddedFilesLink(addedFilesLink);
        copyObj.setModifiedFilesLink(modifiedFilesLink);
        copyObj.setReplacedFilesLink(replacedFilesLink);
        copyObj.setDeletedFilesLink(deletedFilesLink);
        copyObj.setConnectionType(connectionType);
        copyObj.setServerName(serverName);
        copyObj.setRepositoryPath(repositoryPath);
        copyObj.setDefaultServerPort(defaultServerPort);
        copyObj.setServerPort(serverPort);
        copyObj.setAuthenticationMode(authenticationMode);
        copyObj.setUserName(userName);
        copyObj.setPassword(password);
        copyObj.setPrivateKey(privateKey);
        copyObj.setPassphrase(passphrase);
        copyObj.setParent(parent);
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
    public TVersionControlPeer getPeer()
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
        return TVersionControlPeer.getTableMap();
    }

  
    /**
     * Creates a TVersionControlBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TVersionControlBean with the contents of this object
     */
    public TVersionControlBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TVersionControlBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TVersionControlBean with the contents of this object
     */
    public TVersionControlBean getBean(IdentityMap createdBeans)
    {
        TVersionControlBean result = (TVersionControlBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TVersionControlBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setVCType(getVCType());
        result.setRepositoryBrowser(getRepositoryBrowser());
        result.setChangeSetLink(getChangeSetLink());
        result.setAddedFilesLink(getAddedFilesLink());
        result.setModifiedFilesLink(getModifiedFilesLink());
        result.setReplacedFilesLink(getReplacedFilesLink());
        result.setDeletedFilesLink(getDeletedFilesLink());
        result.setConnectionType(getConnectionType());
        result.setServerName(getServerName());
        result.setRepositoryPath(getRepositoryPath());
        result.setDefaultServerPort(getDefaultServerPort());
        result.setServerPort(getServerPort());
        result.setAuthenticationMode(getAuthenticationMode());
        result.setUserName(getUserName());
        result.setPassword(getPassword());
        result.setPrivateKey(getPrivateKey());
        result.setPassphrase(getPassphrase());
        result.setParent(getParent());
        result.setUuid(getUuid());





        if (aTVersionControlRelatedByParent != null)
        {
            TVersionControlBean relatedBean = aTVersionControlRelatedByParent.getBean(createdBeans);
            result.setTVersionControlBeanRelatedByParent(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TVersionControl with the contents
     * of a TVersionControlBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TVersionControlBean which contents are used to create
     *        the resulting class
     * @return an instance of TVersionControl with the contents of bean
     */
    public static TVersionControl createTVersionControl(TVersionControlBean bean)
        throws TorqueException
    {
        return createTVersionControl(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TVersionControl with the contents
     * of a TVersionControlBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TVersionControlBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TVersionControl with the contents of bean
     */

    public static TVersionControl createTVersionControl(TVersionControlBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TVersionControl result = (TVersionControl) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TVersionControl();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setVCType(bean.getVCType());
        result.setRepositoryBrowser(bean.getRepositoryBrowser());
        result.setChangeSetLink(bean.getChangeSetLink());
        result.setAddedFilesLink(bean.getAddedFilesLink());
        result.setModifiedFilesLink(bean.getModifiedFilesLink());
        result.setReplacedFilesLink(bean.getReplacedFilesLink());
        result.setDeletedFilesLink(bean.getDeletedFilesLink());
        result.setConnectionType(bean.getConnectionType());
        result.setServerName(bean.getServerName());
        result.setRepositoryPath(bean.getRepositoryPath());
        result.setDefaultServerPort(bean.getDefaultServerPort());
        result.setServerPort(bean.getServerPort());
        result.setAuthenticationMode(bean.getAuthenticationMode());
        result.setUserName(bean.getUserName());
        result.setPassword(bean.getPassword());
        result.setPrivateKey(bean.getPrivateKey());
        result.setPassphrase(bean.getPassphrase());
        result.setParent(bean.getParent());
        result.setUuid(bean.getUuid());





        {
            TVersionControlBean relatedBean = bean.getTVersionControlBeanRelatedByParent();
            if (relatedBean != null)
            {
                TVersionControl relatedObject = TVersionControl.createTVersionControl(relatedBean, createdObjects);
                result.setTVersionControlRelatedByParent(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TVersionControl:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("VCType = ")
           .append(getVCType())
           .append("\n");
        str.append("RepositoryBrowser = ")
           .append(getRepositoryBrowser())
           .append("\n");
        str.append("ChangeSetLink = ")
           .append(getChangeSetLink())
           .append("\n");
        str.append("AddedFilesLink = ")
           .append(getAddedFilesLink())
           .append("\n");
        str.append("ModifiedFilesLink = ")
           .append(getModifiedFilesLink())
           .append("\n");
        str.append("ReplacedFilesLink = ")
           .append(getReplacedFilesLink())
           .append("\n");
        str.append("DeletedFilesLink = ")
           .append(getDeletedFilesLink())
           .append("\n");
        str.append("ConnectionType = ")
           .append(getConnectionType())
           .append("\n");
        str.append("ServerName = ")
           .append(getServerName())
           .append("\n");
        str.append("RepositoryPath = ")
           .append(getRepositoryPath())
           .append("\n");
        str.append("DefaultServerPort = ")
           .append(getDefaultServerPort())
           .append("\n");
        str.append("ServerPort = ")
           .append(getServerPort())
           .append("\n");
        str.append("AuthenticationMode = ")
           .append(getAuthenticationMode())
           .append("\n");
        str.append("UserName = ")
           .append(getUserName())
           .append("\n");
        str.append("Password = ")
           .append(getPassword())
           .append("\n");
        str.append("PrivateKey = ")
           .append(getPrivateKey())
           .append("\n");
        str.append("Passphrase = ")
           .append(getPassphrase())
           .append("\n");
        str.append("Parent = ")
           .append(getParent())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
