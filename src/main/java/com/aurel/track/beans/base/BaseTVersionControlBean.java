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
 * configuration for version control server
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TVersionControlBean
 */
public abstract class BaseTVersionControlBean
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
     * Get the VCType
     *
     * @return Integer
     */
    public Integer getVCType ()
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

        this.vCType = v;
        setModified(true);

    }

    /**
     * Get the RepositoryBrowser
     *
     * @return Integer
     */
    public Integer getRepositoryBrowser ()
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

        this.repositoryBrowser = v;
        setModified(true);

    }

    /**
     * Get the ChangeSetLink
     *
     * @return String
     */
    public String getChangeSetLink ()
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

        this.changeSetLink = v;
        setModified(true);

    }

    /**
     * Get the AddedFilesLink
     *
     * @return String
     */
    public String getAddedFilesLink ()
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

        this.addedFilesLink = v;
        setModified(true);

    }

    /**
     * Get the ModifiedFilesLink
     *
     * @return String
     */
    public String getModifiedFilesLink ()
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

        this.modifiedFilesLink = v;
        setModified(true);

    }

    /**
     * Get the ReplacedFilesLink
     *
     * @return String
     */
    public String getReplacedFilesLink ()
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

        this.replacedFilesLink = v;
        setModified(true);

    }

    /**
     * Get the DeletedFilesLink
     *
     * @return String
     */
    public String getDeletedFilesLink ()
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

        this.deletedFilesLink = v;
        setModified(true);

    }

    /**
     * Get the ConnectionType
     *
     * @return Integer
     */
    public Integer getConnectionType ()
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

        this.connectionType = v;
        setModified(true);

    }

    /**
     * Get the ServerName
     *
     * @return String
     */
    public String getServerName ()
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

        this.serverName = v;
        setModified(true);

    }

    /**
     * Get the RepositoryPath
     *
     * @return String
     */
    public String getRepositoryPath ()
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

        this.repositoryPath = v;
        setModified(true);

    }

    /**
     * Get the DefaultServerPort
     *
     * @return String
     */
    public String getDefaultServerPort ()
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

        this.defaultServerPort = v;
        setModified(true);

    }

    /**
     * Get the ServerPort
     *
     * @return Integer
     */
    public Integer getServerPort ()
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

        this.serverPort = v;
        setModified(true);

    }

    /**
     * Get the AuthenticationMode
     *
     * @return Integer
     */
    public Integer getAuthenticationMode ()
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

        this.authenticationMode = v;
        setModified(true);

    }

    /**
     * Get the UserName
     *
     * @return String
     */
    public String getUserName ()
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

        this.userName = v;
        setModified(true);

    }

    /**
     * Get the Password
     *
     * @return String
     */
    public String getPassword ()
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

        this.password = v;
        setModified(true);

    }

    /**
     * Get the PrivateKey
     *
     * @return String
     */
    public String getPrivateKey ()
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

        this.privateKey = v;
        setModified(true);

    }

    /**
     * Get the Passphrase
     *
     * @return String
     */
    public String getPassphrase ()
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

        this.passphrase = v;
        setModified(true);

    }

    /**
     * Get the Parent
     *
     * @return Integer
     */
    public Integer getParent ()
    {
        return parent;
    }

    /**
     * Set the value of Parent
     *
     * @param v new value
     */
    public void setParent(Integer v)
    {

        this.parent = v;
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

    



    private TVersionControlBean aTVersionControlBeanRelatedByParent;

    /**
     * sets an associated TVersionControlBean object
     *
     * @param v TVersionControlBean
     */
    public void setTVersionControlBeanRelatedByParent(TVersionControlBean v)
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTVersionControlBeanRelatedByParent = v;
    }


    /**
     * Get the associated TVersionControlBean object
     *
     * @return the associated TVersionControlBean object
     */
    public TVersionControlBean getTVersionControlBeanRelatedByParent()
    {
        return aTVersionControlBeanRelatedByParent;
    }



}
