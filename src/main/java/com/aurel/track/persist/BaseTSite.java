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




  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TSiteBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TSite
 */
public abstract class BaseTSite extends TpBaseObject
{
    /** The Peer class */
    private static final TSitePeer peer =
        new TSitePeer();


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
     * Get the TrackVersion
     *
     * @return String
     */
    public String getTrackVersion()
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

        if (!ObjectUtils.equals(this.trackVersion, v))
        {
            this.trackVersion = v;
            setModified(true);
        }


    }

    /**
     * Get the DbVersion
     *
     * @return String
     */
    public String getDbVersion()
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

        if (!ObjectUtils.equals(this.dbVersion, v))
        {
            this.dbVersion = v;
            setModified(true);
        }


    }

    /**
     * Get the LicenseExtension
     *
     * @return String
     */
    public String getLicenseExtension()
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

        if (!ObjectUtils.equals(this.licenseExtension, v))
        {
            this.licenseExtension = v;
            setModified(true);
        }


    }

    /**
     * Get the ExpDate
     *
     * @return Date
     */
    public Date getExpDate()
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

        if (!ObjectUtils.equals(this.expDate, v))
        {
            this.expDate = v;
            setModified(true);
        }


    }

    /**
     * Get the NumberOfUsers
     *
     * @return Integer
     */
    public Integer getNumberOfUsers()
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

        if (!ObjectUtils.equals(this.numberOfUsers, v))
        {
            this.numberOfUsers = v;
            setModified(true);
        }


    }

    /**
     * Get the TrackEmail
     *
     * @return String
     */
    public String getTrackEmail()
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

        if (!ObjectUtils.equals(this.trackEmail, v))
        {
            this.trackEmail = v;
            setModified(true);
        }


    }

    /**
     * Get the SmtpServerName
     *
     * @return String
     */
    public String getSmtpServerName()
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

        if (!ObjectUtils.equals(this.smtpServerName, v))
        {
            this.smtpServerName = v;
            setModified(true);
        }


    }

    /**
     * Get the SmtpPort
     *
     * @return Integer
     */
    public Integer getSmtpPort()
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

        if (!ObjectUtils.equals(this.smtpPort, v))
        {
            this.smtpPort = v;
            setModified(true);
        }


    }

    /**
     * Get the MailEncoding
     *
     * @return String
     */
    public String getMailEncoding()
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

        if (!ObjectUtils.equals(this.mailEncoding, v))
        {
            this.mailEncoding = v;
            setModified(true);
        }


    }

    /**
     * Get the SmtpUser
     *
     * @return String
     */
    public String getSmtpUser()
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

        if (!ObjectUtils.equals(this.smtpUser, v))
        {
            this.smtpUser = v;
            setModified(true);
        }


    }

    /**
     * Get the SmtpPassWord
     *
     * @return String
     */
    public String getSmtpPassWord()
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

        if (!ObjectUtils.equals(this.smtpPassWord, v))
        {
            this.smtpPassWord = v;
            setModified(true);
        }


    }

    /**
     * Get the MailReceivingServerName
     *
     * @return String
     */
    public String getMailReceivingServerName()
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

        if (!ObjectUtils.equals(this.mailReceivingServerName, v))
        {
            this.mailReceivingServerName = v;
            setModified(true);
        }


    }

    /**
     * Get the MailReceivingPort
     *
     * @return Integer
     */
    public Integer getMailReceivingPort()
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

        if (!ObjectUtils.equals(this.mailReceivingPort, v))
        {
            this.mailReceivingPort = v;
            setModified(true);
        }


    }

    /**
     * Get the MailReceivingUser
     *
     * @return String
     */
    public String getMailReceivingUser()
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

        if (!ObjectUtils.equals(this.mailReceivingUser, v))
        {
            this.mailReceivingUser = v;
            setModified(true);
        }


    }

    /**
     * Get the MailReceivingPassword
     *
     * @return String
     */
    public String getMailReceivingPassword()
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

        if (!ObjectUtils.equals(this.mailReceivingPassword, v))
        {
            this.mailReceivingPassword = v;
            setModified(true);
        }


    }

    /**
     * Get the MailReceivingProtocol
     *
     * @return String
     */
    public String getMailReceivingProtocol()
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

        if (!ObjectUtils.equals(this.mailReceivingProtocol, v))
        {
            this.mailReceivingProtocol = v;
            setModified(true);
        }


    }

    /**
     * Get the AllowedEmailPattern
     *
     * @return String
     */
    public String getAllowedEmailPattern()
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

        if (!ObjectUtils.equals(this.allowedEmailPattern, v))
        {
            this.allowedEmailPattern = v;
            setModified(true);
        }


    }

    /**
     * Get the IsLDAPOn
     *
     * @return String
     */
    public String getIsLDAPOn()
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

        if (!ObjectUtils.equals(this.isLDAPOn, v))
        {
            this.isLDAPOn = v;
            setModified(true);
        }


    }

    /**
     * Get the LdapServerURL
     *
     * @return String
     */
    public String getLdapServerURL()
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

        if (!ObjectUtils.equals(this.ldapServerURL, v))
        {
            this.ldapServerURL = v;
            setModified(true);
        }


    }

    /**
     * Get the LdapAttributeLoginName
     *
     * @return String
     */
    public String getLdapAttributeLoginName()
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

        if (!ObjectUtils.equals(this.ldapAttributeLoginName, v))
        {
            this.ldapAttributeLoginName = v;
            setModified(true);
        }


    }

    /**
     * Get the AttachmentRootDir
     *
     * @return String
     */
    public String getAttachmentRootDir()
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

        if (!ObjectUtils.equals(this.attachmentRootDir, v))
        {
            this.attachmentRootDir = v;
            setModified(true);
        }


    }

    /**
     * Get the ServerURL
     *
     * @return String
     */
    public String getServerURL()
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

        if (!ObjectUtils.equals(this.serverURL, v))
        {
            this.serverURL = v;
            setModified(true);
        }


    }

    /**
     * Get the DescriptionLength
     *
     * @return Integer
     */
    public Integer getDescriptionLength()
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

        if (!ObjectUtils.equals(this.descriptionLength, v))
        {
            this.descriptionLength = v;
            setModified(true);
        }


    }

    /**
     * Get the IsSelfRegisterAllowed
     *
     * @return String
     */
    public String getIsSelfRegisterAllowed()
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

        if (!ObjectUtils.equals(this.isSelfRegisterAllowed, v))
        {
            this.isSelfRegisterAllowed = v;
            setModified(true);
        }


    }

    /**
     * Get the IsDemoSite
     *
     * @return String
     */
    public String getIsDemoSite()
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

        if (!ObjectUtils.equals(this.isDemoSite, v))
        {
            this.isDemoSite = v;
            setModified(true);
        }


    }

    /**
     * Get the UseTrackFromAddress
     *
     * @return String
     */
    public String getUseTrackFromAddress()
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

        if (!ObjectUtils.equals(this.useTrackFromAddress, v))
        {
            this.useTrackFromAddress = v;
            setModified(true);
        }


    }

    /**
     * Get the ReservedUse
     *
     * @return Integer
     */
    public Integer getReservedUse()
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

        if (!ObjectUtils.equals(this.reservedUse, v))
        {
            this.reservedUse = v;
            setModified(true);
        }


    }

    /**
     * Get the LdapBindDN
     *
     * @return String
     */
    public String getLdapBindDN()
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

        if (!ObjectUtils.equals(this.ldapBindDN, v))
        {
            this.ldapBindDN = v;
            setModified(true);
        }


    }

    /**
     * Get the LdapBindPassword
     *
     * @return String
     */
    public String getLdapBindPassword()
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

        if (!ObjectUtils.equals(this.ldapBindPassword, v))
        {
            this.ldapBindPassword = v;
            setModified(true);
        }


    }

    /**
     * Get the Preferences
     *
     * @return String
     */
    public String getPreferences()
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

        if (!ObjectUtils.equals(this.preferences, v))
        {
            this.preferences = v;
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
            fieldNames.add("TrackVersion");
            fieldNames.add("DbVersion");
            fieldNames.add("LicenseExtension");
            fieldNames.add("ExpDate");
            fieldNames.add("NumberOfUsers");
            fieldNames.add("TrackEmail");
            fieldNames.add("SmtpServerName");
            fieldNames.add("SmtpPort");
            fieldNames.add("MailEncoding");
            fieldNames.add("SmtpUser");
            fieldNames.add("SmtpPassWord");
            fieldNames.add("MailReceivingServerName");
            fieldNames.add("MailReceivingPort");
            fieldNames.add("MailReceivingUser");
            fieldNames.add("MailReceivingPassword");
            fieldNames.add("MailReceivingProtocol");
            fieldNames.add("AllowedEmailPattern");
            fieldNames.add("IsLDAPOn");
            fieldNames.add("LdapServerURL");
            fieldNames.add("LdapAttributeLoginName");
            fieldNames.add("AttachmentRootDir");
            fieldNames.add("ServerURL");
            fieldNames.add("DescriptionLength");
            fieldNames.add("IsSelfRegisterAllowed");
            fieldNames.add("IsDemoSite");
            fieldNames.add("UseTrackFromAddress");
            fieldNames.add("ReservedUse");
            fieldNames.add("LdapBindDN");
            fieldNames.add("LdapBindPassword");
            fieldNames.add("Preferences");
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
        if (name.equals("TrackVersion"))
        {
            return getTrackVersion();
        }
        if (name.equals("DbVersion"))
        {
            return getDbVersion();
        }
        if (name.equals("LicenseExtension"))
        {
            return getLicenseExtension();
        }
        if (name.equals("ExpDate"))
        {
            return getExpDate();
        }
        if (name.equals("NumberOfUsers"))
        {
            return getNumberOfUsers();
        }
        if (name.equals("TrackEmail"))
        {
            return getTrackEmail();
        }
        if (name.equals("SmtpServerName"))
        {
            return getSmtpServerName();
        }
        if (name.equals("SmtpPort"))
        {
            return getSmtpPort();
        }
        if (name.equals("MailEncoding"))
        {
            return getMailEncoding();
        }
        if (name.equals("SmtpUser"))
        {
            return getSmtpUser();
        }
        if (name.equals("SmtpPassWord"))
        {
            return getSmtpPassWord();
        }
        if (name.equals("MailReceivingServerName"))
        {
            return getMailReceivingServerName();
        }
        if (name.equals("MailReceivingPort"))
        {
            return getMailReceivingPort();
        }
        if (name.equals("MailReceivingUser"))
        {
            return getMailReceivingUser();
        }
        if (name.equals("MailReceivingPassword"))
        {
            return getMailReceivingPassword();
        }
        if (name.equals("MailReceivingProtocol"))
        {
            return getMailReceivingProtocol();
        }
        if (name.equals("AllowedEmailPattern"))
        {
            return getAllowedEmailPattern();
        }
        if (name.equals("IsLDAPOn"))
        {
            return getIsLDAPOn();
        }
        if (name.equals("LdapServerURL"))
        {
            return getLdapServerURL();
        }
        if (name.equals("LdapAttributeLoginName"))
        {
            return getLdapAttributeLoginName();
        }
        if (name.equals("AttachmentRootDir"))
        {
            return getAttachmentRootDir();
        }
        if (name.equals("ServerURL"))
        {
            return getServerURL();
        }
        if (name.equals("DescriptionLength"))
        {
            return getDescriptionLength();
        }
        if (name.equals("IsSelfRegisterAllowed"))
        {
            return getIsSelfRegisterAllowed();
        }
        if (name.equals("IsDemoSite"))
        {
            return getIsDemoSite();
        }
        if (name.equals("UseTrackFromAddress"))
        {
            return getUseTrackFromAddress();
        }
        if (name.equals("ReservedUse"))
        {
            return getReservedUse();
        }
        if (name.equals("LdapBindDN"))
        {
            return getLdapBindDN();
        }
        if (name.equals("LdapBindPassword"))
        {
            return getLdapBindPassword();
        }
        if (name.equals("Preferences"))
        {
            return getPreferences();
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
        if (name.equals("TrackVersion"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTrackVersion((String) value);
            return true;
        }
        if (name.equals("DbVersion"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDbVersion((String) value);
            return true;
        }
        if (name.equals("LicenseExtension"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLicenseExtension((String) value);
            return true;
        }
        if (name.equals("ExpDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setExpDate((Date) value);
            return true;
        }
        if (name.equals("NumberOfUsers"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNumberOfUsers((Integer) value);
            return true;
        }
        if (name.equals("TrackEmail"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTrackEmail((String) value);
            return true;
        }
        if (name.equals("SmtpServerName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSmtpServerName((String) value);
            return true;
        }
        if (name.equals("SmtpPort"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSmtpPort((Integer) value);
            return true;
        }
        if (name.equals("MailEncoding"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMailEncoding((String) value);
            return true;
        }
        if (name.equals("SmtpUser"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSmtpUser((String) value);
            return true;
        }
        if (name.equals("SmtpPassWord"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSmtpPassWord((String) value);
            return true;
        }
        if (name.equals("MailReceivingServerName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMailReceivingServerName((String) value);
            return true;
        }
        if (name.equals("MailReceivingPort"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMailReceivingPort((Integer) value);
            return true;
        }
        if (name.equals("MailReceivingUser"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMailReceivingUser((String) value);
            return true;
        }
        if (name.equals("MailReceivingPassword"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMailReceivingPassword((String) value);
            return true;
        }
        if (name.equals("MailReceivingProtocol"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMailReceivingProtocol((String) value);
            return true;
        }
        if (name.equals("AllowedEmailPattern"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAllowedEmailPattern((String) value);
            return true;
        }
        if (name.equals("IsLDAPOn"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsLDAPOn((String) value);
            return true;
        }
        if (name.equals("LdapServerURL"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLdapServerURL((String) value);
            return true;
        }
        if (name.equals("LdapAttributeLoginName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLdapAttributeLoginName((String) value);
            return true;
        }
        if (name.equals("AttachmentRootDir"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAttachmentRootDir((String) value);
            return true;
        }
        if (name.equals("ServerURL"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setServerURL((String) value);
            return true;
        }
        if (name.equals("DescriptionLength"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDescriptionLength((Integer) value);
            return true;
        }
        if (name.equals("IsSelfRegisterAllowed"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsSelfRegisterAllowed((String) value);
            return true;
        }
        if (name.equals("IsDemoSite"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsDemoSite((String) value);
            return true;
        }
        if (name.equals("UseTrackFromAddress"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setUseTrackFromAddress((String) value);
            return true;
        }
        if (name.equals("ReservedUse"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReservedUse((Integer) value);
            return true;
        }
        if (name.equals("LdapBindDN"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLdapBindDN((String) value);
            return true;
        }
        if (name.equals("LdapBindPassword"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLdapBindPassword((String) value);
            return true;
        }
        if (name.equals("Preferences"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPreferences((String) value);
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
        if (name.equals(TSitePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TSitePeer.TRACKVERSION))
        {
            return getTrackVersion();
        }
        if (name.equals(TSitePeer.DBVERSION))
        {
            return getDbVersion();
        }
        if (name.equals(TSitePeer.LICENSEEXT))
        {
            return getLicenseExtension();
        }
        if (name.equals(TSitePeer.EXPDATE))
        {
            return getExpDate();
        }
        if (name.equals(TSitePeer.NUMBEROFUSERS))
        {
            return getNumberOfUsers();
        }
        if (name.equals(TSitePeer.TRACKEMAIL))
        {
            return getTrackEmail();
        }
        if (name.equals(TSitePeer.SMTPSERVERNAME))
        {
            return getSmtpServerName();
        }
        if (name.equals(TSitePeer.SMTPPORT))
        {
            return getSmtpPort();
        }
        if (name.equals(TSitePeer.MAILENCODING))
        {
            return getMailEncoding();
        }
        if (name.equals(TSitePeer.SMTPUSER))
        {
            return getSmtpUser();
        }
        if (name.equals(TSitePeer.SMTPPASSWORD))
        {
            return getSmtpPassWord();
        }
        if (name.equals(TSitePeer.POPSERVERNAME))
        {
            return getMailReceivingServerName();
        }
        if (name.equals(TSitePeer.POPPORT))
        {
            return getMailReceivingPort();
        }
        if (name.equals(TSitePeer.POPUSER))
        {
            return getMailReceivingUser();
        }
        if (name.equals(TSitePeer.POPPASSWORD))
        {
            return getMailReceivingPassword();
        }
        if (name.equals(TSitePeer.RECEIVINGPROTOCOL))
        {
            return getMailReceivingProtocol();
        }
        if (name.equals(TSitePeer.ALLOWEDEMAILPATTERN))
        {
            return getAllowedEmailPattern();
        }
        if (name.equals(TSitePeer.ISLDAPON))
        {
            return getIsLDAPOn();
        }
        if (name.equals(TSitePeer.LDAPSERVERURL))
        {
            return getLdapServerURL();
        }
        if (name.equals(TSitePeer.LDAPATTRIBUTELOGINNAME))
        {
            return getLdapAttributeLoginName();
        }
        if (name.equals(TSitePeer.ATTACHMENTROOTDIR))
        {
            return getAttachmentRootDir();
        }
        if (name.equals(TSitePeer.SERVERURL))
        {
            return getServerURL();
        }
        if (name.equals(TSitePeer.DESCRIPTIONLENGTH))
        {
            return getDescriptionLength();
        }
        if (name.equals(TSitePeer.ISSELFREGISTERALLOWED))
        {
            return getIsSelfRegisterAllowed();
        }
        if (name.equals(TSitePeer.ISDEMOSITE))
        {
            return getIsDemoSite();
        }
        if (name.equals(TSitePeer.USETRACKFROMADDRESS))
        {
            return getUseTrackFromAddress();
        }
        if (name.equals(TSitePeer.RESERVEDUSE))
        {
            return getReservedUse();
        }
        if (name.equals(TSitePeer.LDAPBINDDN))
        {
            return getLdapBindDN();
        }
        if (name.equals(TSitePeer.LDAPBINDPASSW))
        {
            return getLdapBindPassword();
        }
        if (name.equals(TSitePeer.PREFERENCES))
        {
            return getPreferences();
        }
        if (name.equals(TSitePeer.TPUUID))
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
      if (TSitePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TSitePeer.TRACKVERSION.equals(name))
        {
            return setByName("TrackVersion", value);
        }
      if (TSitePeer.DBVERSION.equals(name))
        {
            return setByName("DbVersion", value);
        }
      if (TSitePeer.LICENSEEXT.equals(name))
        {
            return setByName("LicenseExtension", value);
        }
      if (TSitePeer.EXPDATE.equals(name))
        {
            return setByName("ExpDate", value);
        }
      if (TSitePeer.NUMBEROFUSERS.equals(name))
        {
            return setByName("NumberOfUsers", value);
        }
      if (TSitePeer.TRACKEMAIL.equals(name))
        {
            return setByName("TrackEmail", value);
        }
      if (TSitePeer.SMTPSERVERNAME.equals(name))
        {
            return setByName("SmtpServerName", value);
        }
      if (TSitePeer.SMTPPORT.equals(name))
        {
            return setByName("SmtpPort", value);
        }
      if (TSitePeer.MAILENCODING.equals(name))
        {
            return setByName("MailEncoding", value);
        }
      if (TSitePeer.SMTPUSER.equals(name))
        {
            return setByName("SmtpUser", value);
        }
      if (TSitePeer.SMTPPASSWORD.equals(name))
        {
            return setByName("SmtpPassWord", value);
        }
      if (TSitePeer.POPSERVERNAME.equals(name))
        {
            return setByName("MailReceivingServerName", value);
        }
      if (TSitePeer.POPPORT.equals(name))
        {
            return setByName("MailReceivingPort", value);
        }
      if (TSitePeer.POPUSER.equals(name))
        {
            return setByName("MailReceivingUser", value);
        }
      if (TSitePeer.POPPASSWORD.equals(name))
        {
            return setByName("MailReceivingPassword", value);
        }
      if (TSitePeer.RECEIVINGPROTOCOL.equals(name))
        {
            return setByName("MailReceivingProtocol", value);
        }
      if (TSitePeer.ALLOWEDEMAILPATTERN.equals(name))
        {
            return setByName("AllowedEmailPattern", value);
        }
      if (TSitePeer.ISLDAPON.equals(name))
        {
            return setByName("IsLDAPOn", value);
        }
      if (TSitePeer.LDAPSERVERURL.equals(name))
        {
            return setByName("LdapServerURL", value);
        }
      if (TSitePeer.LDAPATTRIBUTELOGINNAME.equals(name))
        {
            return setByName("LdapAttributeLoginName", value);
        }
      if (TSitePeer.ATTACHMENTROOTDIR.equals(name))
        {
            return setByName("AttachmentRootDir", value);
        }
      if (TSitePeer.SERVERURL.equals(name))
        {
            return setByName("ServerURL", value);
        }
      if (TSitePeer.DESCRIPTIONLENGTH.equals(name))
        {
            return setByName("DescriptionLength", value);
        }
      if (TSitePeer.ISSELFREGISTERALLOWED.equals(name))
        {
            return setByName("IsSelfRegisterAllowed", value);
        }
      if (TSitePeer.ISDEMOSITE.equals(name))
        {
            return setByName("IsDemoSite", value);
        }
      if (TSitePeer.USETRACKFROMADDRESS.equals(name))
        {
            return setByName("UseTrackFromAddress", value);
        }
      if (TSitePeer.RESERVEDUSE.equals(name))
        {
            return setByName("ReservedUse", value);
        }
      if (TSitePeer.LDAPBINDDN.equals(name))
        {
            return setByName("LdapBindDN", value);
        }
      if (TSitePeer.LDAPBINDPASSW.equals(name))
        {
            return setByName("LdapBindPassword", value);
        }
      if (TSitePeer.PREFERENCES.equals(name))
        {
            return setByName("Preferences", value);
        }
      if (TSitePeer.TPUUID.equals(name))
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
            return getTrackVersion();
        }
        if (pos == 2)
        {
            return getDbVersion();
        }
        if (pos == 3)
        {
            return getLicenseExtension();
        }
        if (pos == 4)
        {
            return getExpDate();
        }
        if (pos == 5)
        {
            return getNumberOfUsers();
        }
        if (pos == 6)
        {
            return getTrackEmail();
        }
        if (pos == 7)
        {
            return getSmtpServerName();
        }
        if (pos == 8)
        {
            return getSmtpPort();
        }
        if (pos == 9)
        {
            return getMailEncoding();
        }
        if (pos == 10)
        {
            return getSmtpUser();
        }
        if (pos == 11)
        {
            return getSmtpPassWord();
        }
        if (pos == 12)
        {
            return getMailReceivingServerName();
        }
        if (pos == 13)
        {
            return getMailReceivingPort();
        }
        if (pos == 14)
        {
            return getMailReceivingUser();
        }
        if (pos == 15)
        {
            return getMailReceivingPassword();
        }
        if (pos == 16)
        {
            return getMailReceivingProtocol();
        }
        if (pos == 17)
        {
            return getAllowedEmailPattern();
        }
        if (pos == 18)
        {
            return getIsLDAPOn();
        }
        if (pos == 19)
        {
            return getLdapServerURL();
        }
        if (pos == 20)
        {
            return getLdapAttributeLoginName();
        }
        if (pos == 21)
        {
            return getAttachmentRootDir();
        }
        if (pos == 22)
        {
            return getServerURL();
        }
        if (pos == 23)
        {
            return getDescriptionLength();
        }
        if (pos == 24)
        {
            return getIsSelfRegisterAllowed();
        }
        if (pos == 25)
        {
            return getIsDemoSite();
        }
        if (pos == 26)
        {
            return getUseTrackFromAddress();
        }
        if (pos == 27)
        {
            return getReservedUse();
        }
        if (pos == 28)
        {
            return getLdapBindDN();
        }
        if (pos == 29)
        {
            return getLdapBindPassword();
        }
        if (pos == 30)
        {
            return getPreferences();
        }
        if (pos == 31)
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
            return setByName("TrackVersion", value);
        }
    if (position == 2)
        {
            return setByName("DbVersion", value);
        }
    if (position == 3)
        {
            return setByName("LicenseExtension", value);
        }
    if (position == 4)
        {
            return setByName("ExpDate", value);
        }
    if (position == 5)
        {
            return setByName("NumberOfUsers", value);
        }
    if (position == 6)
        {
            return setByName("TrackEmail", value);
        }
    if (position == 7)
        {
            return setByName("SmtpServerName", value);
        }
    if (position == 8)
        {
            return setByName("SmtpPort", value);
        }
    if (position == 9)
        {
            return setByName("MailEncoding", value);
        }
    if (position == 10)
        {
            return setByName("SmtpUser", value);
        }
    if (position == 11)
        {
            return setByName("SmtpPassWord", value);
        }
    if (position == 12)
        {
            return setByName("MailReceivingServerName", value);
        }
    if (position == 13)
        {
            return setByName("MailReceivingPort", value);
        }
    if (position == 14)
        {
            return setByName("MailReceivingUser", value);
        }
    if (position == 15)
        {
            return setByName("MailReceivingPassword", value);
        }
    if (position == 16)
        {
            return setByName("MailReceivingProtocol", value);
        }
    if (position == 17)
        {
            return setByName("AllowedEmailPattern", value);
        }
    if (position == 18)
        {
            return setByName("IsLDAPOn", value);
        }
    if (position == 19)
        {
            return setByName("LdapServerURL", value);
        }
    if (position == 20)
        {
            return setByName("LdapAttributeLoginName", value);
        }
    if (position == 21)
        {
            return setByName("AttachmentRootDir", value);
        }
    if (position == 22)
        {
            return setByName("ServerURL", value);
        }
    if (position == 23)
        {
            return setByName("DescriptionLength", value);
        }
    if (position == 24)
        {
            return setByName("IsSelfRegisterAllowed", value);
        }
    if (position == 25)
        {
            return setByName("IsDemoSite", value);
        }
    if (position == 26)
        {
            return setByName("UseTrackFromAddress", value);
        }
    if (position == 27)
        {
            return setByName("ReservedUse", value);
        }
    if (position == 28)
        {
            return setByName("LdapBindDN", value);
        }
    if (position == 29)
        {
            return setByName("LdapBindPassword", value);
        }
    if (position == 30)
        {
            return setByName("Preferences", value);
        }
    if (position == 31)
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
        save(TSitePeer.DATABASE_NAME);
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
                    TSitePeer.doInsert((TSite) this, con);
                    setNew(false);
                }
                else
                {
                    TSitePeer.doUpdate((TSite) this, con);
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
    public TSite copy() throws TorqueException
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
    public TSite copy(Connection con) throws TorqueException
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
    public TSite copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TSite(), deepcopy);
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
    public TSite copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TSite(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TSite copyInto(TSite copyObj) throws TorqueException
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
    protected TSite copyInto(TSite copyObj, Connection con) throws TorqueException
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
    protected TSite copyInto(TSite copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setTrackVersion(trackVersion);
        copyObj.setDbVersion(dbVersion);
        copyObj.setLicenseExtension(licenseExtension);
        copyObj.setExpDate(expDate);
        copyObj.setNumberOfUsers(numberOfUsers);
        copyObj.setTrackEmail(trackEmail);
        copyObj.setSmtpServerName(smtpServerName);
        copyObj.setSmtpPort(smtpPort);
        copyObj.setMailEncoding(mailEncoding);
        copyObj.setSmtpUser(smtpUser);
        copyObj.setSmtpPassWord(smtpPassWord);
        copyObj.setMailReceivingServerName(mailReceivingServerName);
        copyObj.setMailReceivingPort(mailReceivingPort);
        copyObj.setMailReceivingUser(mailReceivingUser);
        copyObj.setMailReceivingPassword(mailReceivingPassword);
        copyObj.setMailReceivingProtocol(mailReceivingProtocol);
        copyObj.setAllowedEmailPattern(allowedEmailPattern);
        copyObj.setIsLDAPOn(isLDAPOn);
        copyObj.setLdapServerURL(ldapServerURL);
        copyObj.setLdapAttributeLoginName(ldapAttributeLoginName);
        copyObj.setAttachmentRootDir(attachmentRootDir);
        copyObj.setServerURL(serverURL);
        copyObj.setDescriptionLength(descriptionLength);
        copyObj.setIsSelfRegisterAllowed(isSelfRegisterAllowed);
        copyObj.setIsDemoSite(isDemoSite);
        copyObj.setUseTrackFromAddress(useTrackFromAddress);
        copyObj.setReservedUse(reservedUse);
        copyObj.setLdapBindDN(ldapBindDN);
        copyObj.setLdapBindPassword(ldapBindPassword);
        copyObj.setPreferences(preferences);
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
    protected TSite copyInto(TSite copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setTrackVersion(trackVersion);
        copyObj.setDbVersion(dbVersion);
        copyObj.setLicenseExtension(licenseExtension);
        copyObj.setExpDate(expDate);
        copyObj.setNumberOfUsers(numberOfUsers);
        copyObj.setTrackEmail(trackEmail);
        copyObj.setSmtpServerName(smtpServerName);
        copyObj.setSmtpPort(smtpPort);
        copyObj.setMailEncoding(mailEncoding);
        copyObj.setSmtpUser(smtpUser);
        copyObj.setSmtpPassWord(smtpPassWord);
        copyObj.setMailReceivingServerName(mailReceivingServerName);
        copyObj.setMailReceivingPort(mailReceivingPort);
        copyObj.setMailReceivingUser(mailReceivingUser);
        copyObj.setMailReceivingPassword(mailReceivingPassword);
        copyObj.setMailReceivingProtocol(mailReceivingProtocol);
        copyObj.setAllowedEmailPattern(allowedEmailPattern);
        copyObj.setIsLDAPOn(isLDAPOn);
        copyObj.setLdapServerURL(ldapServerURL);
        copyObj.setLdapAttributeLoginName(ldapAttributeLoginName);
        copyObj.setAttachmentRootDir(attachmentRootDir);
        copyObj.setServerURL(serverURL);
        copyObj.setDescriptionLength(descriptionLength);
        copyObj.setIsSelfRegisterAllowed(isSelfRegisterAllowed);
        copyObj.setIsDemoSite(isDemoSite);
        copyObj.setUseTrackFromAddress(useTrackFromAddress);
        copyObj.setReservedUse(reservedUse);
        copyObj.setLdapBindDN(ldapBindDN);
        copyObj.setLdapBindPassword(ldapBindPassword);
        copyObj.setPreferences(preferences);
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
    public TSitePeer getPeer()
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
        return TSitePeer.getTableMap();
    }

  
    /**
     * Creates a TSiteBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TSiteBean with the contents of this object
     */
    public TSiteBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TSiteBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TSiteBean with the contents of this object
     */
    public TSiteBean getBean(IdentityMap createdBeans)
    {
        TSiteBean result = (TSiteBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TSiteBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setTrackVersion(getTrackVersion());
        result.setDbVersion(getDbVersion());
        result.setLicenseExtension(getLicenseExtension());
        result.setExpDate(getExpDate());
        result.setNumberOfUsers(getNumberOfUsers());
        result.setTrackEmail(getTrackEmail());
        result.setSmtpServerName(getSmtpServerName());
        result.setSmtpPort(getSmtpPort());
        result.setMailEncoding(getMailEncoding());
        result.setSmtpUser(getSmtpUser());
        result.setSmtpPassWord(getSmtpPassWord());
        result.setMailReceivingServerName(getMailReceivingServerName());
        result.setMailReceivingPort(getMailReceivingPort());
        result.setMailReceivingUser(getMailReceivingUser());
        result.setMailReceivingPassword(getMailReceivingPassword());
        result.setMailReceivingProtocol(getMailReceivingProtocol());
        result.setAllowedEmailPattern(getAllowedEmailPattern());
        result.setIsLDAPOn(getIsLDAPOn());
        result.setLdapServerURL(getLdapServerURL());
        result.setLdapAttributeLoginName(getLdapAttributeLoginName());
        result.setAttachmentRootDir(getAttachmentRootDir());
        result.setServerURL(getServerURL());
        result.setDescriptionLength(getDescriptionLength());
        result.setIsSelfRegisterAllowed(getIsSelfRegisterAllowed());
        result.setIsDemoSite(getIsDemoSite());
        result.setUseTrackFromAddress(getUseTrackFromAddress());
        result.setReservedUse(getReservedUse());
        result.setLdapBindDN(getLdapBindDN());
        result.setLdapBindPassword(getLdapBindPassword());
        result.setPreferences(getPreferences());
        result.setUuid(getUuid());


        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TSite with the contents
     * of a TSiteBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TSiteBean which contents are used to create
     *        the resulting class
     * @return an instance of TSite with the contents of bean
     */
    public static TSite createTSite(TSiteBean bean)
        throws TorqueException
    {
        return createTSite(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TSite with the contents
     * of a TSiteBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TSiteBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TSite with the contents of bean
     */

    public static TSite createTSite(TSiteBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TSite result = (TSite) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TSite();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setTrackVersion(bean.getTrackVersion());
        result.setDbVersion(bean.getDbVersion());
        result.setLicenseExtension(bean.getLicenseExtension());
        result.setExpDate(bean.getExpDate());
        result.setNumberOfUsers(bean.getNumberOfUsers());
        result.setTrackEmail(bean.getTrackEmail());
        result.setSmtpServerName(bean.getSmtpServerName());
        result.setSmtpPort(bean.getSmtpPort());
        result.setMailEncoding(bean.getMailEncoding());
        result.setSmtpUser(bean.getSmtpUser());
        result.setSmtpPassWord(bean.getSmtpPassWord());
        result.setMailReceivingServerName(bean.getMailReceivingServerName());
        result.setMailReceivingPort(bean.getMailReceivingPort());
        result.setMailReceivingUser(bean.getMailReceivingUser());
        result.setMailReceivingPassword(bean.getMailReceivingPassword());
        result.setMailReceivingProtocol(bean.getMailReceivingProtocol());
        result.setAllowedEmailPattern(bean.getAllowedEmailPattern());
        result.setIsLDAPOn(bean.getIsLDAPOn());
        result.setLdapServerURL(bean.getLdapServerURL());
        result.setLdapAttributeLoginName(bean.getLdapAttributeLoginName());
        result.setAttachmentRootDir(bean.getAttachmentRootDir());
        result.setServerURL(bean.getServerURL());
        result.setDescriptionLength(bean.getDescriptionLength());
        result.setIsSelfRegisterAllowed(bean.getIsSelfRegisterAllowed());
        result.setIsDemoSite(bean.getIsDemoSite());
        result.setUseTrackFromAddress(bean.getUseTrackFromAddress());
        result.setReservedUse(bean.getReservedUse());
        result.setLdapBindDN(bean.getLdapBindDN());
        result.setLdapBindPassword(bean.getLdapBindPassword());
        result.setPreferences(bean.getPreferences());
        result.setUuid(bean.getUuid());


    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TSite:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("TrackVersion = ")
           .append(getTrackVersion())
           .append("\n");
        str.append("DbVersion = ")
           .append(getDbVersion())
           .append("\n");
        str.append("LicenseExtension = ")
           .append(getLicenseExtension())
           .append("\n");
        str.append("ExpDate = ")
           .append(getExpDate())
           .append("\n");
        str.append("NumberOfUsers = ")
           .append(getNumberOfUsers())
           .append("\n");
        str.append("TrackEmail = ")
           .append(getTrackEmail())
           .append("\n");
        str.append("SmtpServerName = ")
           .append(getSmtpServerName())
           .append("\n");
        str.append("SmtpPort = ")
           .append(getSmtpPort())
           .append("\n");
        str.append("MailEncoding = ")
           .append(getMailEncoding())
           .append("\n");
        str.append("SmtpUser = ")
           .append(getSmtpUser())
           .append("\n");
        str.append("SmtpPassWord = ")
           .append(getSmtpPassWord())
           .append("\n");
        str.append("MailReceivingServerName = ")
           .append(getMailReceivingServerName())
           .append("\n");
        str.append("MailReceivingPort = ")
           .append(getMailReceivingPort())
           .append("\n");
        str.append("MailReceivingUser = ")
           .append(getMailReceivingUser())
           .append("\n");
        str.append("MailReceivingPassword = ")
           .append(getMailReceivingPassword())
           .append("\n");
        str.append("MailReceivingProtocol = ")
           .append(getMailReceivingProtocol())
           .append("\n");
        str.append("AllowedEmailPattern = ")
           .append(getAllowedEmailPattern())
           .append("\n");
        str.append("IsLDAPOn = ")
           .append(getIsLDAPOn())
           .append("\n");
        str.append("LdapServerURL = ")
           .append(getLdapServerURL())
           .append("\n");
        str.append("LdapAttributeLoginName = ")
           .append(getLdapAttributeLoginName())
           .append("\n");
        str.append("AttachmentRootDir = ")
           .append(getAttachmentRootDir())
           .append("\n");
        str.append("ServerURL = ")
           .append(getServerURL())
           .append("\n");
        str.append("DescriptionLength = ")
           .append(getDescriptionLength())
           .append("\n");
        str.append("IsSelfRegisterAllowed = ")
           .append(getIsSelfRegisterAllowed())
           .append("\n");
        str.append("IsDemoSite = ")
           .append(getIsDemoSite())
           .append("\n");
        str.append("UseTrackFromAddress = ")
           .append(getUseTrackFromAddress())
           .append("\n");
        str.append("ReservedUse = ")
           .append(getReservedUse())
           .append("\n");
        str.append("LdapBindDN = ")
           .append(getLdapBindDN())
           .append("\n");
        str.append("LdapBindPassword = ")
           .append(getLdapBindPassword())
           .append("\n");
        str.append("Preferences = ")
           .append(getPreferences())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
