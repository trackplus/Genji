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
 * extended; all references should be to TSiteBean
 */
public abstract class BaseTSiteBean
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

    /** The value for the trackVersion field */
    private String trackVersion;

    /** The value for the dbVersion field */
    private String dbVersion;

    /** The value for the licenseExtension field */
    private String licenseExtension;

    /** The value for the expDate field */
    private Date expDate;

    /** The value for the numberOfUsers field */
    private Integer numberOfUsers;

    /** The value for the trackEmail field */
    private String trackEmail;

    /** The value for the smtpServerName field */
    private String smtpServerName;

    /** The value for the smtpPort field */
    private Integer smtpPort;

    /** The value for the mailEncoding field */
    private String mailEncoding;

    /** The value for the smtpUser field */
    private String smtpUser;

    /** The value for the smtpPassWord field */
    private String smtpPassWord;

    /** The value for the mailReceivingServerName field */
    private String mailReceivingServerName;

    /** The value for the mailReceivingPort field */
    private Integer mailReceivingPort;

    /** The value for the mailReceivingUser field */
    private String mailReceivingUser;

    /** The value for the mailReceivingPassword field */
    private String mailReceivingPassword;

    /** The value for the mailReceivingProtocol field */
    private String mailReceivingProtocol;

    /** The value for the allowedEmailPattern field */
    private String allowedEmailPattern;

    /** The value for the isLDAPOn field */
    private String isLDAPOn;

    /** The value for the ldapServerURL field */
    private String ldapServerURL;

    /** The value for the ldapAttributeLoginName field */
    private String ldapAttributeLoginName;

    /** The value for the attachmentRootDir field */
    private String attachmentRootDir;

    /** The value for the serverURL field */
    private String serverURL;

    /** The value for the descriptionLength field */
    private Integer descriptionLength;

    /** The value for the isSelfRegisterAllowed field */
    private String isSelfRegisterAllowed = "Y";

    /** The value for the isDemoSite field */
    private String isDemoSite = "N";

    /** The value for the useTrackFromAddress field */
    private String useTrackFromAddress = "Y";

    /** The value for the reservedUse field */
    private Integer reservedUse;

    /** The value for the ldapBindDN field */
    private String ldapBindDN;

    /** The value for the ldapBindPassword field */
    private String ldapBindPassword;

    /** The value for the preferences field */
    private String preferences;

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
     * Get the TrackVersion
     *
     * @return String
     */
    public String getTrackVersion ()
    {
        return trackVersion;
    }

    /**
     * Set the value of TrackVersion
     *
     * @param v new value
     */
    public void setTrackVersion(String v)
    {

        this.trackVersion = v;
        setModified(true);

    }

    /**
     * Get the DbVersion
     *
     * @return String
     */
    public String getDbVersion ()
    {
        return dbVersion;
    }

    /**
     * Set the value of DbVersion
     *
     * @param v new value
     */
    public void setDbVersion(String v)
    {

        this.dbVersion = v;
        setModified(true);

    }

    /**
     * Get the LicenseExtension
     *
     * @return String
     */
    public String getLicenseExtension ()
    {
        return licenseExtension;
    }

    /**
     * Set the value of LicenseExtension
     *
     * @param v new value
     */
    public void setLicenseExtension(String v)
    {

        this.licenseExtension = v;
        setModified(true);

    }

    /**
     * Get the ExpDate
     *
     * @return Date
     */
    public Date getExpDate ()
    {
        return expDate;
    }

    /**
     * Set the value of ExpDate
     *
     * @param v new value
     */
    public void setExpDate(Date v)
    {

        this.expDate = v;
        setModified(true);

    }

    /**
     * Get the NumberOfUsers
     *
     * @return Integer
     */
    public Integer getNumberOfUsers ()
    {
        return numberOfUsers;
    }

    /**
     * Set the value of NumberOfUsers
     *
     * @param v new value
     */
    public void setNumberOfUsers(Integer v)
    {

        this.numberOfUsers = v;
        setModified(true);

    }

    /**
     * Get the TrackEmail
     *
     * @return String
     */
    public String getTrackEmail ()
    {
        return trackEmail;
    }

    /**
     * Set the value of TrackEmail
     *
     * @param v new value
     */
    public void setTrackEmail(String v)
    {

        this.trackEmail = v;
        setModified(true);

    }

    /**
     * Get the SmtpServerName
     *
     * @return String
     */
    public String getSmtpServerName ()
    {
        return smtpServerName;
    }

    /**
     * Set the value of SmtpServerName
     *
     * @param v new value
     */
    public void setSmtpServerName(String v)
    {

        this.smtpServerName = v;
        setModified(true);

    }

    /**
     * Get the SmtpPort
     *
     * @return Integer
     */
    public Integer getSmtpPort ()
    {
        return smtpPort;
    }

    /**
     * Set the value of SmtpPort
     *
     * @param v new value
     */
    public void setSmtpPort(Integer v)
    {

        this.smtpPort = v;
        setModified(true);

    }

    /**
     * Get the MailEncoding
     *
     * @return String
     */
    public String getMailEncoding ()
    {
        return mailEncoding;
    }

    /**
     * Set the value of MailEncoding
     *
     * @param v new value
     */
    public void setMailEncoding(String v)
    {

        this.mailEncoding = v;
        setModified(true);

    }

    /**
     * Get the SmtpUser
     *
     * @return String
     */
    public String getSmtpUser ()
    {
        return smtpUser;
    }

    /**
     * Set the value of SmtpUser
     *
     * @param v new value
     */
    public void setSmtpUser(String v)
    {

        this.smtpUser = v;
        setModified(true);

    }

    /**
     * Get the SmtpPassWord
     *
     * @return String
     */
    public String getSmtpPassWord ()
    {
        return smtpPassWord;
    }

    /**
     * Set the value of SmtpPassWord
     *
     * @param v new value
     */
    public void setSmtpPassWord(String v)
    {

        this.smtpPassWord = v;
        setModified(true);

    }

    /**
     * Get the MailReceivingServerName
     *
     * @return String
     */
    public String getMailReceivingServerName ()
    {
        return mailReceivingServerName;
    }

    /**
     * Set the value of MailReceivingServerName
     *
     * @param v new value
     */
    public void setMailReceivingServerName(String v)
    {

        this.mailReceivingServerName = v;
        setModified(true);

    }

    /**
     * Get the MailReceivingPort
     *
     * @return Integer
     */
    public Integer getMailReceivingPort ()
    {
        return mailReceivingPort;
    }

    /**
     * Set the value of MailReceivingPort
     *
     * @param v new value
     */
    public void setMailReceivingPort(Integer v)
    {

        this.mailReceivingPort = v;
        setModified(true);

    }

    /**
     * Get the MailReceivingUser
     *
     * @return String
     */
    public String getMailReceivingUser ()
    {
        return mailReceivingUser;
    }

    /**
     * Set the value of MailReceivingUser
     *
     * @param v new value
     */
    public void setMailReceivingUser(String v)
    {

        this.mailReceivingUser = v;
        setModified(true);

    }

    /**
     * Get the MailReceivingPassword
     *
     * @return String
     */
    public String getMailReceivingPassword ()
    {
        return mailReceivingPassword;
    }

    /**
     * Set the value of MailReceivingPassword
     *
     * @param v new value
     */
    public void setMailReceivingPassword(String v)
    {

        this.mailReceivingPassword = v;
        setModified(true);

    }

    /**
     * Get the MailReceivingProtocol
     *
     * @return String
     */
    public String getMailReceivingProtocol ()
    {
        return mailReceivingProtocol;
    }

    /**
     * Set the value of MailReceivingProtocol
     *
     * @param v new value
     */
    public void setMailReceivingProtocol(String v)
    {

        this.mailReceivingProtocol = v;
        setModified(true);

    }

    /**
     * Get the AllowedEmailPattern
     *
     * @return String
     */
    public String getAllowedEmailPattern ()
    {
        return allowedEmailPattern;
    }

    /**
     * Set the value of AllowedEmailPattern
     *
     * @param v new value
     */
    public void setAllowedEmailPattern(String v)
    {

        this.allowedEmailPattern = v;
        setModified(true);

    }

    /**
     * Get the IsLDAPOn
     *
     * @return String
     */
    public String getIsLDAPOn ()
    {
        return isLDAPOn;
    }

    /**
     * Set the value of IsLDAPOn
     *
     * @param v new value
     */
    public void setIsLDAPOn(String v)
    {

        this.isLDAPOn = v;
        setModified(true);

    }

    /**
     * Get the LdapServerURL
     *
     * @return String
     */
    public String getLdapServerURL ()
    {
        return ldapServerURL;
    }

    /**
     * Set the value of LdapServerURL
     *
     * @param v new value
     */
    public void setLdapServerURL(String v)
    {

        this.ldapServerURL = v;
        setModified(true);

    }

    /**
     * Get the LdapAttributeLoginName
     *
     * @return String
     */
    public String getLdapAttributeLoginName ()
    {
        return ldapAttributeLoginName;
    }

    /**
     * Set the value of LdapAttributeLoginName
     *
     * @param v new value
     */
    public void setLdapAttributeLoginName(String v)
    {

        this.ldapAttributeLoginName = v;
        setModified(true);

    }

    /**
     * Get the AttachmentRootDir
     *
     * @return String
     */
    public String getAttachmentRootDir ()
    {
        return attachmentRootDir;
    }

    /**
     * Set the value of AttachmentRootDir
     *
     * @param v new value
     */
    public void setAttachmentRootDir(String v)
    {

        this.attachmentRootDir = v;
        setModified(true);

    }

    /**
     * Get the ServerURL
     *
     * @return String
     */
    public String getServerURL ()
    {
        return serverURL;
    }

    /**
     * Set the value of ServerURL
     *
     * @param v new value
     */
    public void setServerURL(String v)
    {

        this.serverURL = v;
        setModified(true);

    }

    /**
     * Get the DescriptionLength
     *
     * @return Integer
     */
    public Integer getDescriptionLength ()
    {
        return descriptionLength;
    }

    /**
     * Set the value of DescriptionLength
     *
     * @param v new value
     */
    public void setDescriptionLength(Integer v)
    {

        this.descriptionLength = v;
        setModified(true);

    }

    /**
     * Get the IsSelfRegisterAllowed
     *
     * @return String
     */
    public String getIsSelfRegisterAllowed ()
    {
        return isSelfRegisterAllowed;
    }

    /**
     * Set the value of IsSelfRegisterAllowed
     *
     * @param v new value
     */
    public void setIsSelfRegisterAllowed(String v)
    {

        this.isSelfRegisterAllowed = v;
        setModified(true);

    }

    /**
     * Get the IsDemoSite
     *
     * @return String
     */
    public String getIsDemoSite ()
    {
        return isDemoSite;
    }

    /**
     * Set the value of IsDemoSite
     *
     * @param v new value
     */
    public void setIsDemoSite(String v)
    {

        this.isDemoSite = v;
        setModified(true);

    }

    /**
     * Get the UseTrackFromAddress
     *
     * @return String
     */
    public String getUseTrackFromAddress ()
    {
        return useTrackFromAddress;
    }

    /**
     * Set the value of UseTrackFromAddress
     *
     * @param v new value
     */
    public void setUseTrackFromAddress(String v)
    {

        this.useTrackFromAddress = v;
        setModified(true);

    }

    /**
     * Get the ReservedUse
     *
     * @return Integer
     */
    public Integer getReservedUse ()
    {
        return reservedUse;
    }

    /**
     * Set the value of ReservedUse
     *
     * @param v new value
     */
    public void setReservedUse(Integer v)
    {

        this.reservedUse = v;
        setModified(true);

    }

    /**
     * Get the LdapBindDN
     *
     * @return String
     */
    public String getLdapBindDN ()
    {
        return ldapBindDN;
    }

    /**
     * Set the value of LdapBindDN
     *
     * @param v new value
     */
    public void setLdapBindDN(String v)
    {

        this.ldapBindDN = v;
        setModified(true);

    }

    /**
     * Get the LdapBindPassword
     *
     * @return String
     */
    public String getLdapBindPassword ()
    {
        return ldapBindPassword;
    }

    /**
     * Set the value of LdapBindPassword
     *
     * @param v new value
     */
    public void setLdapBindPassword(String v)
    {

        this.ldapBindPassword = v;
        setModified(true);

    }

    /**
     * Get the Preferences
     *
     * @return String
     */
    public String getPreferences ()
    {
        return preferences;
    }

    /**
     * Set the value of Preferences
     *
     * @param v new value
     */
    public void setPreferences(String v)
    {

        this.preferences = v;
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

    

}
