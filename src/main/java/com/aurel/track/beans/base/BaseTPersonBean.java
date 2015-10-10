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
 * This table holds all user related information, like user name, e-mail, etc.
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TPersonBean
 */
public abstract class BaseTPersonBean
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

    /** The value for the firstName field */
    private String firstName;

    /** The value for the lastName field */
    private String lastName;

    /** The value for the loginName field */
    private String loginName;

    /** The value for the email field */
    private String email;

    /** The value for the passwd field */
    private String passwd;

    /** The value for the salt field */
    private String salt;

    /** The value for the forgotPasswordKey field */
    private String forgotPasswordKey;

    /** The value for the phone field */
    private String phone;

    /** The value for the departmentID field */
    private Integer departmentID;

    /** The value for the validUntil field */
    private Date validUntil;

    /** The value for the preferences field */
    private String preferences;

    /** The value for the lastEdit field */
    private Date lastEdit;

    /** The value for the created field */
    private Date created;

    /** The value for the deleted field */
    private String deleted = "N";

    /** The value for the tokenPasswd field */
    private String tokenPasswd;

    /** The value for the tokenExpDate field */
    private Date tokenExpDate;

    /** The value for the emailFrequency field */
    private Integer emailFrequency;

    /** The value for the emailLead field */
    private Integer emailLead;

    /** The value for the emailLastReminded field */
    private Date emailLastReminded;

    /** The value for the emailRemindMe field */
    private String emailRemindMe = "N";

    /** The value for the prefEmailType field */
    private String prefEmailType = "Plain";

    /** The value for the prefLocale field */
    private String prefLocale;

    /** The value for the myDefaultReport field */
    private Integer myDefaultReport;

    /** The value for the noEmailPlease field */
    private Integer noEmailPlease;

    /** The value for the remindMeAsOriginator field */
    private String remindMeAsOriginator = "N";

    /** The value for the remindMeAsManager field */
    private String remindMeAsManager = "Y";

    /** The value for the remindMeAsResponsible field */
    private String remindMeAsResponsible = "Y";

    /** The value for the emailRemindPriorityLevel field */
    private Integer emailRemindPriorityLevel;

    /** The value for the emailRemindSeverityLevel field */
    private Integer emailRemindSeverityLevel;

    /** The value for the hoursPerWorkDay field */
    private Double hoursPerWorkDay;

    /** The value for the hourlyWage field */
    private Double hourlyWage;

    /** The value for the extraHourWage field */
    private Double extraHourWage;

    /** The value for the employeeID field */
    private String employeeID;

    /** The value for the isgroup field */
    private String isgroup = "N";

    /** The value for the userLevel field */
    private Integer userLevel;

    /** The value for the maxAssignedItems field */
    private Integer maxAssignedItems;

    /** The value for the messengerURL field */
    private String messengerURL;

    /** The value for the cALLURL field */
    private String cALLURL;

    /** The value for the symbol field */
    private String symbol;

    /** The value for the iconKey field */
    private Integer iconKey;

    /** The value for the substituteID field */
    private Integer substituteID;

    /** The value for the substituteActive field */
    private String substituteActive = "N";

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
     * Get the FirstName
     *
     * @return String
     */
    public String getFirstName ()
    {
        return firstName;
    }

    /**
     * Set the value of FirstName
     *
     * @param v new value
     */
    public void setFirstName(String v)
    {

        this.firstName = v;
        setModified(true);

    }

    /**
     * Get the LastName
     *
     * @return String
     */
    public String getLastName ()
    {
        return lastName;
    }

    /**
     * Set the value of LastName
     *
     * @param v new value
     */
    public void setLastName(String v)
    {

        this.lastName = v;
        setModified(true);

    }

    /**
     * Get the LoginName
     *
     * @return String
     */
    public String getLoginName ()
    {
        return loginName;
    }

    /**
     * Set the value of LoginName
     *
     * @param v new value
     */
    public void setLoginName(String v)
    {

        this.loginName = v;
        setModified(true);

    }

    /**
     * Get the Email
     *
     * @return String
     */
    public String getEmail ()
    {
        return email;
    }

    /**
     * Set the value of Email
     *
     * @param v new value
     */
    public void setEmail(String v)
    {

        this.email = v;
        setModified(true);

    }

    /**
     * Get the Passwd
     *
     * @return String
     */
    public String getPasswd ()
    {
        return passwd;
    }

    /**
     * Set the value of Passwd
     *
     * @param v new value
     */
    public void setPasswd(String v)
    {

        this.passwd = v;
        setModified(true);

    }

    /**
     * Get the Salt
     *
     * @return String
     */
    public String getSalt ()
    {
        return salt;
    }

    /**
     * Set the value of Salt
     *
     * @param v new value
     */
    public void setSalt(String v)
    {

        this.salt = v;
        setModified(true);

    }

    /**
     * Get the ForgotPasswordKey
     *
     * @return String
     */
    public String getForgotPasswordKey ()
    {
        return forgotPasswordKey;
    }

    /**
     * Set the value of ForgotPasswordKey
     *
     * @param v new value
     */
    public void setForgotPasswordKey(String v)
    {

        this.forgotPasswordKey = v;
        setModified(true);

    }

    /**
     * Get the Phone
     *
     * @return String
     */
    public String getPhone ()
    {
        return phone;
    }

    /**
     * Set the value of Phone
     *
     * @param v new value
     */
    public void setPhone(String v)
    {

        this.phone = v;
        setModified(true);

    }

    /**
     * Get the DepartmentID
     *
     * @return Integer
     */
    public Integer getDepartmentID ()
    {
        return departmentID;
    }

    /**
     * Set the value of DepartmentID
     *
     * @param v new value
     */
    public void setDepartmentID(Integer v)
    {

        this.departmentID = v;
        setModified(true);

    }

    /**
     * Get the ValidUntil
     *
     * @return Date
     */
    public Date getValidUntil ()
    {
        return validUntil;
    }

    /**
     * Set the value of ValidUntil
     *
     * @param v new value
     */
    public void setValidUntil(Date v)
    {

        this.validUntil = v;
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
     * Get the Created
     *
     * @return Date
     */
    public Date getCreated ()
    {
        return created;
    }

    /**
     * Set the value of Created
     *
     * @param v new value
     */
    public void setCreated(Date v)
    {

        this.created = v;
        setModified(true);

    }

    /**
     * Get the Deleted
     *
     * @return String
     */
    public String getDeleted ()
    {
        return deleted;
    }

    /**
     * Set the value of Deleted
     *
     * @param v new value
     */
    public void setDeleted(String v)
    {

        this.deleted = v;
        setModified(true);

    }

    /**
     * Get the TokenPasswd
     *
     * @return String
     */
    public String getTokenPasswd ()
    {
        return tokenPasswd;
    }

    /**
     * Set the value of TokenPasswd
     *
     * @param v new value
     */
    public void setTokenPasswd(String v)
    {

        this.tokenPasswd = v;
        setModified(true);

    }

    /**
     * Get the TokenExpDate
     *
     * @return Date
     */
    public Date getTokenExpDate ()
    {
        return tokenExpDate;
    }

    /**
     * Set the value of TokenExpDate
     *
     * @param v new value
     */
    public void setTokenExpDate(Date v)
    {

        this.tokenExpDate = v;
        setModified(true);

    }

    /**
     * Get the EmailFrequency
     *
     * @return Integer
     */
    public Integer getEmailFrequency ()
    {
        return emailFrequency;
    }

    /**
     * Set the value of EmailFrequency
     *
     * @param v new value
     */
    public void setEmailFrequency(Integer v)
    {

        this.emailFrequency = v;
        setModified(true);

    }

    /**
     * Get the EmailLead
     *
     * @return Integer
     */
    public Integer getEmailLead ()
    {
        return emailLead;
    }

    /**
     * Set the value of EmailLead
     *
     * @param v new value
     */
    public void setEmailLead(Integer v)
    {

        this.emailLead = v;
        setModified(true);

    }

    /**
     * Get the EmailLastReminded
     *
     * @return Date
     */
    public Date getEmailLastReminded ()
    {
        return emailLastReminded;
    }

    /**
     * Set the value of EmailLastReminded
     *
     * @param v new value
     */
    public void setEmailLastReminded(Date v)
    {

        this.emailLastReminded = v;
        setModified(true);

    }

    /**
     * Get the EmailRemindMe
     *
     * @return String
     */
    public String getEmailRemindMe ()
    {
        return emailRemindMe;
    }

    /**
     * Set the value of EmailRemindMe
     *
     * @param v new value
     */
    public void setEmailRemindMe(String v)
    {

        this.emailRemindMe = v;
        setModified(true);

    }

    /**
     * Get the PrefEmailType
     *
     * @return String
     */
    public String getPrefEmailType ()
    {
        return prefEmailType;
    }

    /**
     * Set the value of PrefEmailType
     *
     * @param v new value
     */
    public void setPrefEmailType(String v)
    {

        this.prefEmailType = v;
        setModified(true);

    }

    /**
     * Get the PrefLocale
     *
     * @return String
     */
    public String getPrefLocale ()
    {
        return prefLocale;
    }

    /**
     * Set the value of PrefLocale
     *
     * @param v new value
     */
    public void setPrefLocale(String v)
    {

        this.prefLocale = v;
        setModified(true);

    }

    /**
     * Get the MyDefaultReport
     *
     * @return Integer
     */
    public Integer getMyDefaultReport ()
    {
        return myDefaultReport;
    }

    /**
     * Set the value of MyDefaultReport
     *
     * @param v new value
     */
    public void setMyDefaultReport(Integer v)
    {

        this.myDefaultReport = v;
        setModified(true);

    }

    /**
     * Get the NoEmailPlease
     *
     * @return Integer
     */
    public Integer getNoEmailPlease ()
    {
        return noEmailPlease;
    }

    /**
     * Set the value of NoEmailPlease
     *
     * @param v new value
     */
    public void setNoEmailPlease(Integer v)
    {

        this.noEmailPlease = v;
        setModified(true);

    }

    /**
     * Get the RemindMeAsOriginator
     *
     * @return String
     */
    public String getRemindMeAsOriginator ()
    {
        return remindMeAsOriginator;
    }

    /**
     * Set the value of RemindMeAsOriginator
     *
     * @param v new value
     */
    public void setRemindMeAsOriginator(String v)
    {

        this.remindMeAsOriginator = v;
        setModified(true);

    }

    /**
     * Get the RemindMeAsManager
     *
     * @return String
     */
    public String getRemindMeAsManager ()
    {
        return remindMeAsManager;
    }

    /**
     * Set the value of RemindMeAsManager
     *
     * @param v new value
     */
    public void setRemindMeAsManager(String v)
    {

        this.remindMeAsManager = v;
        setModified(true);

    }

    /**
     * Get the RemindMeAsResponsible
     *
     * @return String
     */
    public String getRemindMeAsResponsible ()
    {
        return remindMeAsResponsible;
    }

    /**
     * Set the value of RemindMeAsResponsible
     *
     * @param v new value
     */
    public void setRemindMeAsResponsible(String v)
    {

        this.remindMeAsResponsible = v;
        setModified(true);

    }

    /**
     * Get the EmailRemindPriorityLevel
     *
     * @return Integer
     */
    public Integer getEmailRemindPriorityLevel ()
    {
        return emailRemindPriorityLevel;
    }

    /**
     * Set the value of EmailRemindPriorityLevel
     *
     * @param v new value
     */
    public void setEmailRemindPriorityLevel(Integer v)
    {

        this.emailRemindPriorityLevel = v;
        setModified(true);

    }

    /**
     * Get the EmailRemindSeverityLevel
     *
     * @return Integer
     */
    public Integer getEmailRemindSeverityLevel ()
    {
        return emailRemindSeverityLevel;
    }

    /**
     * Set the value of EmailRemindSeverityLevel
     *
     * @param v new value
     */
    public void setEmailRemindSeverityLevel(Integer v)
    {

        this.emailRemindSeverityLevel = v;
        setModified(true);

    }

    /**
     * Get the HoursPerWorkDay
     *
     * @return Double
     */
    public Double getHoursPerWorkDay ()
    {
        return hoursPerWorkDay;
    }

    /**
     * Set the value of HoursPerWorkDay
     *
     * @param v new value
     */
    public void setHoursPerWorkDay(Double v)
    {

        this.hoursPerWorkDay = v;
        setModified(true);

    }

    /**
     * Get the HourlyWage
     *
     * @return Double
     */
    public Double getHourlyWage ()
    {
        return hourlyWage;
    }

    /**
     * Set the value of HourlyWage
     *
     * @param v new value
     */
    public void setHourlyWage(Double v)
    {

        this.hourlyWage = v;
        setModified(true);

    }

    /**
     * Get the ExtraHourWage
     *
     * @return Double
     */
    public Double getExtraHourWage ()
    {
        return extraHourWage;
    }

    /**
     * Set the value of ExtraHourWage
     *
     * @param v new value
     */
    public void setExtraHourWage(Double v)
    {

        this.extraHourWage = v;
        setModified(true);

    }

    /**
     * Get the EmployeeID
     *
     * @return String
     */
    public String getEmployeeID ()
    {
        return employeeID;
    }

    /**
     * Set the value of EmployeeID
     *
     * @param v new value
     */
    public void setEmployeeID(String v)
    {

        this.employeeID = v;
        setModified(true);

    }

    /**
     * Get the Isgroup
     *
     * @return String
     */
    public String getIsgroup ()
    {
        return isgroup;
    }

    /**
     * Set the value of Isgroup
     *
     * @param v new value
     */
    public void setIsgroup(String v)
    {

        this.isgroup = v;
        setModified(true);

    }

    /**
     * Get the UserLevel
     *
     * @return Integer
     */
    public Integer getUserLevel ()
    {
        return userLevel;
    }

    /**
     * Set the value of UserLevel
     *
     * @param v new value
     */
    public void setUserLevel(Integer v)
    {

        this.userLevel = v;
        setModified(true);

    }

    /**
     * Get the MaxAssignedItems
     *
     * @return Integer
     */
    public Integer getMaxAssignedItems ()
    {
        return maxAssignedItems;
    }

    /**
     * Set the value of MaxAssignedItems
     *
     * @param v new value
     */
    public void setMaxAssignedItems(Integer v)
    {

        this.maxAssignedItems = v;
        setModified(true);

    }

    /**
     * Get the MessengerURL
     *
     * @return String
     */
    public String getMessengerURL ()
    {
        return messengerURL;
    }

    /**
     * Set the value of MessengerURL
     *
     * @param v new value
     */
    public void setMessengerURL(String v)
    {

        this.messengerURL = v;
        setModified(true);

    }

    /**
     * Get the CALLURL
     *
     * @return String
     */
    public String getCALLURL ()
    {
        return cALLURL;
    }

    /**
     * Set the value of CALLURL
     *
     * @param v new value
     */
    public void setCALLURL(String v)
    {

        this.cALLURL = v;
        setModified(true);

    }

    /**
     * Get the Symbol
     *
     * @return String
     */
    public String getSymbol ()
    {
        return symbol;
    }

    /**
     * Set the value of Symbol
     *
     * @param v new value
     */
    public void setSymbol(String v)
    {

        this.symbol = v;
        setModified(true);

    }

    /**
     * Get the IconKey
     *
     * @return Integer
     */
    public Integer getIconKey ()
    {
        return iconKey;
    }

    /**
     * Set the value of IconKey
     *
     * @param v new value
     */
    public void setIconKey(Integer v)
    {

        this.iconKey = v;
        setModified(true);

    }

    /**
     * Get the SubstituteID
     *
     * @return Integer
     */
    public Integer getSubstituteID ()
    {
        return substituteID;
    }

    /**
     * Set the value of SubstituteID
     *
     * @param v new value
     */
    public void setSubstituteID(Integer v)
    {

        this.substituteID = v;
        setModified(true);

    }

    /**
     * Get the SubstituteActive
     *
     * @return String
     */
    public String getSubstituteActive ()
    {
        return substituteActive;
    }

    /**
     * Set the value of SubstituteActive
     *
     * @param v new value
     */
    public void setSubstituteActive(String v)
    {

        this.substituteActive = v;
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

    



    private TDepartmentBean aTDepartmentBean;

    /**
     * sets an associated TDepartmentBean object
     *
     * @param v TDepartmentBean
     */
    public void setTDepartmentBean(TDepartmentBean v)
    {
        if (v == null)
        {
            setDepartmentID((Integer) null);
        }
        else
        {
            setDepartmentID(v.getObjectID());
        }
        aTDepartmentBean = v;
    }


    /**
     * Get the associated TDepartmentBean object
     *
     * @return the associated TDepartmentBean object
     */
    public TDepartmentBean getTDepartmentBean()
    {
        return aTDepartmentBean;
    }





    private TPrivateReportRepositoryBean aTPrivateReportRepositoryBean;

    /**
     * sets an associated TPrivateReportRepositoryBean object
     *
     * @param v TPrivateReportRepositoryBean
     */
    public void setTPrivateReportRepositoryBean(TPrivateReportRepositoryBean v)
    {
        if (v == null)
        {
            setMyDefaultReport((Integer) null);
        }
        else
        {
            setMyDefaultReport(v.getObjectID());
        }
        aTPrivateReportRepositoryBean = v;
    }


    /**
     * Get the associated TPrivateReportRepositoryBean object
     *
     * @return the associated TPrivateReportRepositoryBean object
     */
    public TPrivateReportRepositoryBean getTPrivateReportRepositoryBean()
    {
        return aTPrivateReportRepositoryBean;
    }





    private TBLOBBean aTBLOBBean;

    /**
     * sets an associated TBLOBBean object
     *
     * @param v TBLOBBean
     */
    public void setTBLOBBean(TBLOBBean v)
    {
        if (v == null)
        {
            setIconKey((Integer) null);
        }
        else
        {
            setIconKey(v.getObjectID());
        }
        aTBLOBBean = v;
    }


    /**
     * Get the associated TBLOBBean object
     *
     * @return the associated TBLOBBean object
     */
    public TBLOBBean getTBLOBBean()
    {
        return aTBLOBBean;
    }





    private TPersonBean aTPersonBeanRelatedBySubstituteID;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedBySubstituteID(TPersonBean v)
    {
        if (v == null)
        {
            setSubstituteID((Integer) null);
        }
        else
        {
            setSubstituteID(v.getObjectID());
        }
        aTPersonBeanRelatedBySubstituteID = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedBySubstituteID()
    {
        return aTPersonBeanRelatedBySubstituteID;
    }





    /**
     * Collection to store aggregation of collTAccessControlListBeans
     */
    protected List<TAccessControlListBean> collTAccessControlListBeans;

    /**
     * Returns the collection of TAccessControlListBeans
     */
    public List<TAccessControlListBean> getTAccessControlListBeans()
    {
        return collTAccessControlListBeans;
    }

    /**
     * Sets the collection of TAccessControlListBeans to the specified value
     */
    public void setTAccessControlListBeans(List<TAccessControlListBean> list)
    {
        if (list == null)
        {
            collTAccessControlListBeans = null;
        }
        else
        {
            collTAccessControlListBeans = new ArrayList<TAccessControlListBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTBaseLineBeans
     */
    protected List<TBaseLineBean> collTBaseLineBeans;

    /**
     * Returns the collection of TBaseLineBeans
     */
    public List<TBaseLineBean> getTBaseLineBeans()
    {
        return collTBaseLineBeans;
    }

    /**
     * Sets the collection of TBaseLineBeans to the specified value
     */
    public void setTBaseLineBeans(List<TBaseLineBean> list)
    {
        if (list == null)
        {
            collTBaseLineBeans = null;
        }
        else
        {
            collTBaseLineBeans = new ArrayList<TBaseLineBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTNotifyBeans
     */
    protected List<TNotifyBean> collTNotifyBeans;

    /**
     * Returns the collection of TNotifyBeans
     */
    public List<TNotifyBean> getTNotifyBeans()
    {
        return collTNotifyBeans;
    }

    /**
     * Sets the collection of TNotifyBeans to the specified value
     */
    public void setTNotifyBeans(List<TNotifyBean> list)
    {
        if (list == null)
        {
            collTNotifyBeans = null;
        }
        else
        {
            collTNotifyBeans = new ArrayList<TNotifyBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTProjectBeansRelatedByDefaultOwnerID
     */
    protected List<TProjectBean> collTProjectBeansRelatedByDefaultOwnerID;

    /**
     * Returns the collection of TProjectBeansRelatedByDefaultOwnerID
     */
    public List<TProjectBean> getTProjectBeansRelatedByDefaultOwnerID()
    {
        return collTProjectBeansRelatedByDefaultOwnerID;
    }

    /**
     * Sets the collection of TProjectBeansRelatedByDefaultOwnerID to the specified value
     */
    public void setTProjectBeansRelatedByDefaultOwnerID(List<TProjectBean> list)
    {
        if (list == null)
        {
            collTProjectBeansRelatedByDefaultOwnerID = null;
        }
        else
        {
            collTProjectBeansRelatedByDefaultOwnerID = new ArrayList<TProjectBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTProjectBeansRelatedByDefaultManagerID
     */
    protected List<TProjectBean> collTProjectBeansRelatedByDefaultManagerID;

    /**
     * Returns the collection of TProjectBeansRelatedByDefaultManagerID
     */
    public List<TProjectBean> getTProjectBeansRelatedByDefaultManagerID()
    {
        return collTProjectBeansRelatedByDefaultManagerID;
    }

    /**
     * Sets the collection of TProjectBeansRelatedByDefaultManagerID to the specified value
     */
    public void setTProjectBeansRelatedByDefaultManagerID(List<TProjectBean> list)
    {
        if (list == null)
        {
            collTProjectBeansRelatedByDefaultManagerID = null;
        }
        else
        {
            collTProjectBeansRelatedByDefaultManagerID = new ArrayList<TProjectBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTStateChangeBeans
     */
    protected List<TStateChangeBean> collTStateChangeBeans;

    /**
     * Returns the collection of TStateChangeBeans
     */
    public List<TStateChangeBean> getTStateChangeBeans()
    {
        return collTStateChangeBeans;
    }

    /**
     * Sets the collection of TStateChangeBeans to the specified value
     */
    public void setTStateChangeBeans(List<TStateChangeBean> list)
    {
        if (list == null)
        {
            collTStateChangeBeans = null;
        }
        else
        {
            collTStateChangeBeans = new ArrayList<TStateChangeBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTTrailBeans
     */
    protected List<TTrailBean> collTTrailBeans;

    /**
     * Returns the collection of TTrailBeans
     */
    public List<TTrailBean> getTTrailBeans()
    {
        return collTTrailBeans;
    }

    /**
     * Sets the collection of TTrailBeans to the specified value
     */
    public void setTTrailBeans(List<TTrailBean> list)
    {
        if (list == null)
        {
            collTTrailBeans = null;
        }
        else
        {
            collTTrailBeans = new ArrayList<TTrailBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemBeansRelatedByOwnerID
     */
    protected List<TWorkItemBean> collTWorkItemBeansRelatedByOwnerID;

    /**
     * Returns the collection of TWorkItemBeansRelatedByOwnerID
     */
    public List<TWorkItemBean> getTWorkItemBeansRelatedByOwnerID()
    {
        return collTWorkItemBeansRelatedByOwnerID;
    }

    /**
     * Sets the collection of TWorkItemBeansRelatedByOwnerID to the specified value
     */
    public void setTWorkItemBeansRelatedByOwnerID(List<TWorkItemBean> list)
    {
        if (list == null)
        {
            collTWorkItemBeansRelatedByOwnerID = null;
        }
        else
        {
            collTWorkItemBeansRelatedByOwnerID = new ArrayList<TWorkItemBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemBeansRelatedByChangedByID
     */
    protected List<TWorkItemBean> collTWorkItemBeansRelatedByChangedByID;

    /**
     * Returns the collection of TWorkItemBeansRelatedByChangedByID
     */
    public List<TWorkItemBean> getTWorkItemBeansRelatedByChangedByID()
    {
        return collTWorkItemBeansRelatedByChangedByID;
    }

    /**
     * Sets the collection of TWorkItemBeansRelatedByChangedByID to the specified value
     */
    public void setTWorkItemBeansRelatedByChangedByID(List<TWorkItemBean> list)
    {
        if (list == null)
        {
            collTWorkItemBeansRelatedByChangedByID = null;
        }
        else
        {
            collTWorkItemBeansRelatedByChangedByID = new ArrayList<TWorkItemBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemBeansRelatedByOriginatorID
     */
    protected List<TWorkItemBean> collTWorkItemBeansRelatedByOriginatorID;

    /**
     * Returns the collection of TWorkItemBeansRelatedByOriginatorID
     */
    public List<TWorkItemBean> getTWorkItemBeansRelatedByOriginatorID()
    {
        return collTWorkItemBeansRelatedByOriginatorID;
    }

    /**
     * Sets the collection of TWorkItemBeansRelatedByOriginatorID to the specified value
     */
    public void setTWorkItemBeansRelatedByOriginatorID(List<TWorkItemBean> list)
    {
        if (list == null)
        {
            collTWorkItemBeansRelatedByOriginatorID = null;
        }
        else
        {
            collTWorkItemBeansRelatedByOriginatorID = new ArrayList<TWorkItemBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemBeansRelatedByResponsibleID
     */
    protected List<TWorkItemBean> collTWorkItemBeansRelatedByResponsibleID;

    /**
     * Returns the collection of TWorkItemBeansRelatedByResponsibleID
     */
    public List<TWorkItemBean> getTWorkItemBeansRelatedByResponsibleID()
    {
        return collTWorkItemBeansRelatedByResponsibleID;
    }

    /**
     * Sets the collection of TWorkItemBeansRelatedByResponsibleID to the specified value
     */
    public void setTWorkItemBeansRelatedByResponsibleID(List<TWorkItemBean> list)
    {
        if (list == null)
        {
            collTWorkItemBeansRelatedByResponsibleID = null;
        }
        else
        {
            collTWorkItemBeansRelatedByResponsibleID = new ArrayList<TWorkItemBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTComputedValuesBeans
     */
    protected List<TComputedValuesBean> collTComputedValuesBeans;

    /**
     * Returns the collection of TComputedValuesBeans
     */
    public List<TComputedValuesBean> getTComputedValuesBeans()
    {
        return collTComputedValuesBeans;
    }

    /**
     * Sets the collection of TComputedValuesBeans to the specified value
     */
    public void setTComputedValuesBeans(List<TComputedValuesBean> list)
    {
        if (list == null)
        {
            collTComputedValuesBeans = null;
        }
        else
        {
            collTComputedValuesBeans = new ArrayList<TComputedValuesBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTPrivateReportRepositoryBeans
     */
    protected List<TPrivateReportRepositoryBean> collTPrivateReportRepositoryBeans;

    /**
     * Returns the collection of TPrivateReportRepositoryBeans
     */
    public List<TPrivateReportRepositoryBean> getTPrivateReportRepositoryBeans()
    {
        return collTPrivateReportRepositoryBeans;
    }

    /**
     * Sets the collection of TPrivateReportRepositoryBeans to the specified value
     */
    public void setTPrivateReportRepositoryBeans(List<TPrivateReportRepositoryBean> list)
    {
        if (list == null)
        {
            collTPrivateReportRepositoryBeans = null;
        }
        else
        {
            collTPrivateReportRepositoryBeans = new ArrayList<TPrivateReportRepositoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTPublicReportRepositoryBeans
     */
    protected List<TPublicReportRepositoryBean> collTPublicReportRepositoryBeans;

    /**
     * Returns the collection of TPublicReportRepositoryBeans
     */
    public List<TPublicReportRepositoryBean> getTPublicReportRepositoryBeans()
    {
        return collTPublicReportRepositoryBeans;
    }

    /**
     * Sets the collection of TPublicReportRepositoryBeans to the specified value
     */
    public void setTPublicReportRepositoryBeans(List<TPublicReportRepositoryBean> list)
    {
        if (list == null)
        {
            collTPublicReportRepositoryBeans = null;
        }
        else
        {
            collTPublicReportRepositoryBeans = new ArrayList<TPublicReportRepositoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTAttachmentBeans
     */
    protected List<TAttachmentBean> collTAttachmentBeans;

    /**
     * Returns the collection of TAttachmentBeans
     */
    public List<TAttachmentBean> getTAttachmentBeans()
    {
        return collTAttachmentBeans;
    }

    /**
     * Sets the collection of TAttachmentBeans to the specified value
     */
    public void setTAttachmentBeans(List<TAttachmentBean> list)
    {
        if (list == null)
        {
            collTAttachmentBeans = null;
        }
        else
        {
            collTAttachmentBeans = new ArrayList<TAttachmentBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTCostBeans
     */
    protected List<TCostBean> collTCostBeans;

    /**
     * Returns the collection of TCostBeans
     */
    public List<TCostBean> getTCostBeans()
    {
        return collTCostBeans;
    }

    /**
     * Sets the collection of TCostBeans to the specified value
     */
    public void setTCostBeans(List<TCostBean> list)
    {
        if (list == null)
        {
            collTCostBeans = null;
        }
        else
        {
            collTCostBeans = new ArrayList<TCostBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkFlowBeans
     */
    protected List<TWorkFlowBean> collTWorkFlowBeans;

    /**
     * Returns the collection of TWorkFlowBeans
     */
    public List<TWorkFlowBean> getTWorkFlowBeans()
    {
        return collTWorkFlowBeans;
    }

    /**
     * Sets the collection of TWorkFlowBeans to the specified value
     */
    public void setTWorkFlowBeans(List<TWorkFlowBean> list)
    {
        if (list == null)
        {
            collTWorkFlowBeans = null;
        }
        else
        {
            collTWorkFlowBeans = new ArrayList<TWorkFlowBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTIssueAttributeValueBeans
     */
    protected List<TIssueAttributeValueBean> collTIssueAttributeValueBeans;

    /**
     * Returns the collection of TIssueAttributeValueBeans
     */
    public List<TIssueAttributeValueBean> getTIssueAttributeValueBeans()
    {
        return collTIssueAttributeValueBeans;
    }

    /**
     * Sets the collection of TIssueAttributeValueBeans to the specified value
     */
    public void setTIssueAttributeValueBeans(List<TIssueAttributeValueBean> list)
    {
        if (list == null)
        {
            collTIssueAttributeValueBeans = null;
        }
        else
        {
            collTIssueAttributeValueBeans = new ArrayList<TIssueAttributeValueBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReportLayoutBeans
     */
    protected List<TReportLayoutBean> collTReportLayoutBeans;

    /**
     * Returns the collection of TReportLayoutBeans
     */
    public List<TReportLayoutBean> getTReportLayoutBeans()
    {
        return collTReportLayoutBeans;
    }

    /**
     * Sets the collection of TReportLayoutBeans to the specified value
     */
    public void setTReportLayoutBeans(List<TReportLayoutBean> list)
    {
        if (list == null)
        {
            collTReportLayoutBeans = null;
        }
        else
        {
            collTReportLayoutBeans = new ArrayList<TReportLayoutBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTSchedulerBeans
     */
    protected List<TSchedulerBean> collTSchedulerBeans;

    /**
     * Returns the collection of TSchedulerBeans
     */
    public List<TSchedulerBean> getTSchedulerBeans()
    {
        return collTSchedulerBeans;
    }

    /**
     * Sets the collection of TSchedulerBeans to the specified value
     */
    public void setTSchedulerBeans(List<TSchedulerBean> list)
    {
        if (list == null)
        {
            collTSchedulerBeans = null;
        }
        else
        {
            collTSchedulerBeans = new ArrayList<TSchedulerBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTGroupMemberBeansRelatedByTheGroup
     */
    protected List<TGroupMemberBean> collTGroupMemberBeansRelatedByTheGroup;

    /**
     * Returns the collection of TGroupMemberBeansRelatedByTheGroup
     */
    public List<TGroupMemberBean> getTGroupMemberBeansRelatedByTheGroup()
    {
        return collTGroupMemberBeansRelatedByTheGroup;
    }

    /**
     * Sets the collection of TGroupMemberBeansRelatedByTheGroup to the specified value
     */
    public void setTGroupMemberBeansRelatedByTheGroup(List<TGroupMemberBean> list)
    {
        if (list == null)
        {
            collTGroupMemberBeansRelatedByTheGroup = null;
        }
        else
        {
            collTGroupMemberBeansRelatedByTheGroup = new ArrayList<TGroupMemberBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTGroupMemberBeansRelatedByPerson
     */
    protected List<TGroupMemberBean> collTGroupMemberBeansRelatedByPerson;

    /**
     * Returns the collection of TGroupMemberBeansRelatedByPerson
     */
    public List<TGroupMemberBean> getTGroupMemberBeansRelatedByPerson()
    {
        return collTGroupMemberBeansRelatedByPerson;
    }

    /**
     * Sets the collection of TGroupMemberBeansRelatedByPerson to the specified value
     */
    public void setTGroupMemberBeansRelatedByPerson(List<TGroupMemberBean> list)
    {
        if (list == null)
        {
            collTGroupMemberBeansRelatedByPerson = null;
        }
        else
        {
            collTGroupMemberBeansRelatedByPerson = new ArrayList<TGroupMemberBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTBudgetBeans
     */
    protected List<TBudgetBean> collTBudgetBeans;

    /**
     * Returns the collection of TBudgetBeans
     */
    public List<TBudgetBean> getTBudgetBeans()
    {
        return collTBudgetBeans;
    }

    /**
     * Sets the collection of TBudgetBeans to the specified value
     */
    public void setTBudgetBeans(List<TBudgetBean> list)
    {
        if (list == null)
        {
            collTBudgetBeans = null;
        }
        else
        {
            collTBudgetBeans = new ArrayList<TBudgetBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTActualEstimatedBudgetBeans
     */
    protected List<TActualEstimatedBudgetBean> collTActualEstimatedBudgetBeans;

    /**
     * Returns the collection of TActualEstimatedBudgetBeans
     */
    public List<TActualEstimatedBudgetBean> getTActualEstimatedBudgetBeans()
    {
        return collTActualEstimatedBudgetBeans;
    }

    /**
     * Sets the collection of TActualEstimatedBudgetBeans to the specified value
     */
    public void setTActualEstimatedBudgetBeans(List<TActualEstimatedBudgetBean> list)
    {
        if (list == null)
        {
            collTActualEstimatedBudgetBeans = null;
        }
        else
        {
            collTActualEstimatedBudgetBeans = new ArrayList<TActualEstimatedBudgetBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTDashboardScreenBeansRelatedByPerson
     */
    protected List<TDashboardScreenBean> collTDashboardScreenBeansRelatedByPerson;

    /**
     * Returns the collection of TDashboardScreenBeansRelatedByPerson
     */
    public List<TDashboardScreenBean> getTDashboardScreenBeansRelatedByPerson()
    {
        return collTDashboardScreenBeansRelatedByPerson;
    }

    /**
     * Sets the collection of TDashboardScreenBeansRelatedByPerson to the specified value
     */
    public void setTDashboardScreenBeansRelatedByPerson(List<TDashboardScreenBean> list)
    {
        if (list == null)
        {
            collTDashboardScreenBeansRelatedByPerson = null;
        }
        else
        {
            collTDashboardScreenBeansRelatedByPerson = new ArrayList<TDashboardScreenBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTDashboardScreenBeansRelatedByOwner
     */
    protected List<TDashboardScreenBean> collTDashboardScreenBeansRelatedByOwner;

    /**
     * Returns the collection of TDashboardScreenBeansRelatedByOwner
     */
    public List<TDashboardScreenBean> getTDashboardScreenBeansRelatedByOwner()
    {
        return collTDashboardScreenBeansRelatedByOwner;
    }

    /**
     * Sets the collection of TDashboardScreenBeansRelatedByOwner to the specified value
     */
    public void setTDashboardScreenBeansRelatedByOwner(List<TDashboardScreenBean> list)
    {
        if (list == null)
        {
            collTDashboardScreenBeansRelatedByOwner = null;
        }
        else
        {
            collTDashboardScreenBeansRelatedByOwner = new ArrayList<TDashboardScreenBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTFieldBeans
     */
    protected List<TFieldBean> collTFieldBeans;

    /**
     * Returns the collection of TFieldBeans
     */
    public List<TFieldBean> getTFieldBeans()
    {
        return collTFieldBeans;
    }

    /**
     * Sets the collection of TFieldBeans to the specified value
     */
    public void setTFieldBeans(List<TFieldBean> list)
    {
        if (list == null)
        {
            collTFieldBeans = null;
        }
        else
        {
            collTFieldBeans = new ArrayList<TFieldBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTListBeans
     */
    protected List<TListBean> collTListBeans;

    /**
     * Returns the collection of TListBeans
     */
    public List<TListBean> getTListBeans()
    {
        return collTListBeans;
    }

    /**
     * Sets the collection of TListBeans to the specified value
     */
    public void setTListBeans(List<TListBean> list)
    {
        if (list == null)
        {
            collTListBeans = null;
        }
        else
        {
            collTListBeans = new ArrayList<TListBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTScreenBeans
     */
    protected List<TScreenBean> collTScreenBeans;

    /**
     * Returns the collection of TScreenBeans
     */
    public List<TScreenBean> getTScreenBeans()
    {
        return collTScreenBeans;
    }

    /**
     * Sets the collection of TScreenBeans to the specified value
     */
    public void setTScreenBeans(List<TScreenBean> list)
    {
        if (list == null)
        {
            collTScreenBeans = null;
        }
        else
        {
            collTScreenBeans = new ArrayList<TScreenBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTNotifyTriggerBeans
     */
    protected List<TNotifyTriggerBean> collTNotifyTriggerBeans;

    /**
     * Returns the collection of TNotifyTriggerBeans
     */
    public List<TNotifyTriggerBean> getTNotifyTriggerBeans()
    {
        return collTNotifyTriggerBeans;
    }

    /**
     * Sets the collection of TNotifyTriggerBeans to the specified value
     */
    public void setTNotifyTriggerBeans(List<TNotifyTriggerBean> list)
    {
        if (list == null)
        {
            collTNotifyTriggerBeans = null;
        }
        else
        {
            collTNotifyTriggerBeans = new ArrayList<TNotifyTriggerBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTNotifySettingsBeans
     */
    protected List<TNotifySettingsBean> collTNotifySettingsBeans;

    /**
     * Returns the collection of TNotifySettingsBeans
     */
    public List<TNotifySettingsBean> getTNotifySettingsBeans()
    {
        return collTNotifySettingsBeans;
    }

    /**
     * Sets the collection of TNotifySettingsBeans to the specified value
     */
    public void setTNotifySettingsBeans(List<TNotifySettingsBean> list)
    {
        if (list == null)
        {
            collTNotifySettingsBeans = null;
        }
        else
        {
            collTNotifySettingsBeans = new ArrayList<TNotifySettingsBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTQueryRepositoryBeans
     */
    protected List<TQueryRepositoryBean> collTQueryRepositoryBeans;

    /**
     * Returns the collection of TQueryRepositoryBeans
     */
    public List<TQueryRepositoryBean> getTQueryRepositoryBeans()
    {
        return collTQueryRepositoryBeans;
    }

    /**
     * Sets the collection of TQueryRepositoryBeans to the specified value
     */
    public void setTQueryRepositoryBeans(List<TQueryRepositoryBean> list)
    {
        if (list == null)
        {
            collTQueryRepositoryBeans = null;
        }
        else
        {
            collTQueryRepositoryBeans = new ArrayList<TQueryRepositoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemLinkBeans
     */
    protected List<TWorkItemLinkBean> collTWorkItemLinkBeans;

    /**
     * Returns the collection of TWorkItemLinkBeans
     */
    public List<TWorkItemLinkBean> getTWorkItemLinkBeans()
    {
        return collTWorkItemLinkBeans;
    }

    /**
     * Sets the collection of TWorkItemLinkBeans to the specified value
     */
    public void setTWorkItemLinkBeans(List<TWorkItemLinkBean> list)
    {
        if (list == null)
        {
            collTWorkItemLinkBeans = null;
        }
        else
        {
            collTWorkItemLinkBeans = new ArrayList<TWorkItemLinkBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemLockBeans
     */
    protected List<TWorkItemLockBean> collTWorkItemLockBeans;

    /**
     * Returns the collection of TWorkItemLockBeans
     */
    public List<TWorkItemLockBean> getTWorkItemLockBeans()
    {
        return collTWorkItemLockBeans;
    }

    /**
     * Sets the collection of TWorkItemLockBeans to the specified value
     */
    public void setTWorkItemLockBeans(List<TWorkItemLockBean> list)
    {
        if (list == null)
        {
            collTWorkItemLockBeans = null;
        }
        else
        {
            collTWorkItemLockBeans = new ArrayList<TWorkItemLockBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTExportTemplateBeans
     */
    protected List<TExportTemplateBean> collTExportTemplateBeans;

    /**
     * Returns the collection of TExportTemplateBeans
     */
    public List<TExportTemplateBean> getTExportTemplateBeans()
    {
        return collTExportTemplateBeans;
    }

    /**
     * Sets the collection of TExportTemplateBeans to the specified value
     */
    public void setTExportTemplateBeans(List<TExportTemplateBean> list)
    {
        if (list == null)
        {
            collTExportTemplateBeans = null;
        }
        else
        {
            collTExportTemplateBeans = new ArrayList<TExportTemplateBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTLoggedInUsersBeans
     */
    protected List<TLoggedInUsersBean> collTLoggedInUsersBeans;

    /**
     * Returns the collection of TLoggedInUsersBeans
     */
    public List<TLoggedInUsersBean> getTLoggedInUsersBeans()
    {
        return collTLoggedInUsersBeans;
    }

    /**
     * Sets the collection of TLoggedInUsersBeans to the specified value
     */
    public void setTLoggedInUsersBeans(List<TLoggedInUsersBean> list)
    {
        if (list == null)
        {
            collTLoggedInUsersBeans = null;
        }
        else
        {
            collTLoggedInUsersBeans = new ArrayList<TLoggedInUsersBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTSummaryMailBeansRelatedByPERSONFROM
     */
    protected List<TSummaryMailBean> collTSummaryMailBeansRelatedByPERSONFROM;

    /**
     * Returns the collection of TSummaryMailBeansRelatedByPERSONFROM
     */
    public List<TSummaryMailBean> getTSummaryMailBeansRelatedByPERSONFROM()
    {
        return collTSummaryMailBeansRelatedByPERSONFROM;
    }

    /**
     * Sets the collection of TSummaryMailBeansRelatedByPERSONFROM to the specified value
     */
    public void setTSummaryMailBeansRelatedByPERSONFROM(List<TSummaryMailBean> list)
    {
        if (list == null)
        {
            collTSummaryMailBeansRelatedByPERSONFROM = null;
        }
        else
        {
            collTSummaryMailBeansRelatedByPERSONFROM = new ArrayList<TSummaryMailBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTSummaryMailBeansRelatedByPERSONTO
     */
    protected List<TSummaryMailBean> collTSummaryMailBeansRelatedByPERSONTO;

    /**
     * Returns the collection of TSummaryMailBeansRelatedByPERSONTO
     */
    public List<TSummaryMailBean> getTSummaryMailBeansRelatedByPERSONTO()
    {
        return collTSummaryMailBeansRelatedByPERSONTO;
    }

    /**
     * Sets the collection of TSummaryMailBeansRelatedByPERSONTO to the specified value
     */
    public void setTSummaryMailBeansRelatedByPERSONTO(List<TSummaryMailBean> list)
    {
        if (list == null)
        {
            collTSummaryMailBeansRelatedByPERSONTO = null;
        }
        else
        {
            collTSummaryMailBeansRelatedByPERSONTO = new ArrayList<TSummaryMailBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTHistoryTransactionBeans
     */
    protected List<THistoryTransactionBean> collTHistoryTransactionBeans;

    /**
     * Returns the collection of THistoryTransactionBeans
     */
    public List<THistoryTransactionBean> getTHistoryTransactionBeans()
    {
        return collTHistoryTransactionBeans;
    }

    /**
     * Sets the collection of THistoryTransactionBeans to the specified value
     */
    public void setTHistoryTransactionBeans(List<THistoryTransactionBean> list)
    {
        if (list == null)
        {
            collTHistoryTransactionBeans = null;
        }
        else
        {
            collTHistoryTransactionBeans = new ArrayList<THistoryTransactionBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTScriptsBeans
     */
    protected List<TScriptsBean> collTScriptsBeans;

    /**
     * Returns the collection of TScriptsBeans
     */
    public List<TScriptsBean> getTScriptsBeans()
    {
        return collTScriptsBeans;
    }

    /**
     * Sets the collection of TScriptsBeans to the specified value
     */
    public void setTScriptsBeans(List<TScriptsBean> list)
    {
        if (list == null)
        {
            collTScriptsBeans = null;
        }
        else
        {
            collTScriptsBeans = new ArrayList<TScriptsBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTTemplatePersonBeans
     */
    protected List<TTemplatePersonBean> collTTemplatePersonBeans;

    /**
     * Returns the collection of TTemplatePersonBeans
     */
    public List<TTemplatePersonBean> getTTemplatePersonBeans()
    {
        return collTTemplatePersonBeans;
    }

    /**
     * Sets the collection of TTemplatePersonBeans to the specified value
     */
    public void setTTemplatePersonBeans(List<TTemplatePersonBean> list)
    {
        if (list == null)
        {
            collTTemplatePersonBeans = null;
        }
        else
        {
            collTTemplatePersonBeans = new ArrayList<TTemplatePersonBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReportPersonSettingsBeans
     */
    protected List<TReportPersonSettingsBean> collTReportPersonSettingsBeans;

    /**
     * Returns the collection of TReportPersonSettingsBeans
     */
    public List<TReportPersonSettingsBean> getTReportPersonSettingsBeans()
    {
        return collTReportPersonSettingsBeans;
    }

    /**
     * Sets the collection of TReportPersonSettingsBeans to the specified value
     */
    public void setTReportPersonSettingsBeans(List<TReportPersonSettingsBean> list)
    {
        if (list == null)
        {
            collTReportPersonSettingsBeans = null;
        }
        else
        {
            collTReportPersonSettingsBeans = new ArrayList<TReportPersonSettingsBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTMSProjectExchangeBeans
     */
    protected List<TMSProjectExchangeBean> collTMSProjectExchangeBeans;

    /**
     * Returns the collection of TMSProjectExchangeBeans
     */
    public List<TMSProjectExchangeBean> getTMSProjectExchangeBeans()
    {
        return collTMSProjectExchangeBeans;
    }

    /**
     * Sets the collection of TMSProjectExchangeBeans to the specified value
     */
    public void setTMSProjectExchangeBeans(List<TMSProjectExchangeBean> list)
    {
        if (list == null)
        {
            collTMSProjectExchangeBeans = null;
        }
        else
        {
            collTMSProjectExchangeBeans = new ArrayList<TMSProjectExchangeBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTFilterCategoryBeans
     */
    protected List<TFilterCategoryBean> collTFilterCategoryBeans;

    /**
     * Returns the collection of TFilterCategoryBeans
     */
    public List<TFilterCategoryBean> getTFilterCategoryBeans()
    {
        return collTFilterCategoryBeans;
    }

    /**
     * Sets the collection of TFilterCategoryBeans to the specified value
     */
    public void setTFilterCategoryBeans(List<TFilterCategoryBean> list)
    {
        if (list == null)
        {
            collTFilterCategoryBeans = null;
        }
        else
        {
            collTFilterCategoryBeans = new ArrayList<TFilterCategoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReportCategoryBeans
     */
    protected List<TReportCategoryBean> collTReportCategoryBeans;

    /**
     * Returns the collection of TReportCategoryBeans
     */
    public List<TReportCategoryBean> getTReportCategoryBeans()
    {
        return collTReportCategoryBeans;
    }

    /**
     * Sets the collection of TReportCategoryBeans to the specified value
     */
    public void setTReportCategoryBeans(List<TReportCategoryBean> list)
    {
        if (list == null)
        {
            collTReportCategoryBeans = null;
        }
        else
        {
            collTReportCategoryBeans = new ArrayList<TReportCategoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTMenuitemQueryBeans
     */
    protected List<TMenuitemQueryBean> collTMenuitemQueryBeans;

    /**
     * Returns the collection of TMenuitemQueryBeans
     */
    public List<TMenuitemQueryBean> getTMenuitemQueryBeans()
    {
        return collTMenuitemQueryBeans;
    }

    /**
     * Sets the collection of TMenuitemQueryBeans to the specified value
     */
    public void setTMenuitemQueryBeans(List<TMenuitemQueryBean> list)
    {
        if (list == null)
        {
            collTMenuitemQueryBeans = null;
        }
        else
        {
            collTMenuitemQueryBeans = new ArrayList<TMenuitemQueryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTPersonBasketBeans
     */
    protected List<TPersonBasketBean> collTPersonBasketBeans;

    /**
     * Returns the collection of TPersonBasketBeans
     */
    public List<TPersonBasketBean> getTPersonBasketBeans()
    {
        return collTPersonBasketBeans;
    }

    /**
     * Sets the collection of TPersonBasketBeans to the specified value
     */
    public void setTPersonBasketBeans(List<TPersonBasketBean> list)
    {
        if (list == null)
        {
            collTPersonBasketBeans = null;
        }
        else
        {
            collTPersonBasketBeans = new ArrayList<TPersonBasketBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTBasketBeans
     */
    protected List<TBasketBean> collTBasketBeans;

    /**
     * Returns the collection of TBasketBeans
     */
    public List<TBasketBean> getTBasketBeans()
    {
        return collTBasketBeans;
    }

    /**
     * Sets the collection of TBasketBeans to the specified value
     */
    public void setTBasketBeans(List<TBasketBean> list)
    {
        if (list == null)
        {
            collTBasketBeans = null;
        }
        else
        {
            collTBasketBeans = new ArrayList<TBasketBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTLastVisitedItemBeans
     */
    protected List<TLastVisitedItemBean> collTLastVisitedItemBeans;

    /**
     * Returns the collection of TLastVisitedItemBeans
     */
    public List<TLastVisitedItemBean> getTLastVisitedItemBeans()
    {
        return collTLastVisitedItemBeans;
    }

    /**
     * Sets the collection of TLastVisitedItemBeans to the specified value
     */
    public void setTLastVisitedItemBeans(List<TLastVisitedItemBean> list)
    {
        if (list == null)
        {
            collTLastVisitedItemBeans = null;
        }
        else
        {
            collTLastVisitedItemBeans = new ArrayList<TLastVisitedItemBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowDefBeans
     */
    protected List<TWorkflowDefBean> collTWorkflowDefBeans;

    /**
     * Returns the collection of TWorkflowDefBeans
     */
    public List<TWorkflowDefBean> getTWorkflowDefBeans()
    {
        return collTWorkflowDefBeans;
    }

    /**
     * Sets the collection of TWorkflowDefBeans to the specified value
     */
    public void setTWorkflowDefBeans(List<TWorkflowDefBean> list)
    {
        if (list == null)
        {
            collTWorkflowDefBeans = null;
        }
        else
        {
            collTWorkflowDefBeans = new ArrayList<TWorkflowDefBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowActivityBeansRelatedByNewMan
     */
    protected List<TWorkflowActivityBean> collTWorkflowActivityBeansRelatedByNewMan;

    /**
     * Returns the collection of TWorkflowActivityBeansRelatedByNewMan
     */
    public List<TWorkflowActivityBean> getTWorkflowActivityBeansRelatedByNewMan()
    {
        return collTWorkflowActivityBeansRelatedByNewMan;
    }

    /**
     * Sets the collection of TWorkflowActivityBeansRelatedByNewMan to the specified value
     */
    public void setTWorkflowActivityBeansRelatedByNewMan(List<TWorkflowActivityBean> list)
    {
        if (list == null)
        {
            collTWorkflowActivityBeansRelatedByNewMan = null;
        }
        else
        {
            collTWorkflowActivityBeansRelatedByNewMan = new ArrayList<TWorkflowActivityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowActivityBeansRelatedByNewResp
     */
    protected List<TWorkflowActivityBean> collTWorkflowActivityBeansRelatedByNewResp;

    /**
     * Returns the collection of TWorkflowActivityBeansRelatedByNewResp
     */
    public List<TWorkflowActivityBean> getTWorkflowActivityBeansRelatedByNewResp()
    {
        return collTWorkflowActivityBeansRelatedByNewResp;
    }

    /**
     * Sets the collection of TWorkflowActivityBeansRelatedByNewResp to the specified value
     */
    public void setTWorkflowActivityBeansRelatedByNewResp(List<TWorkflowActivityBean> list)
    {
        if (list == null)
        {
            collTWorkflowActivityBeansRelatedByNewResp = null;
        }
        else
        {
            collTWorkflowActivityBeansRelatedByNewResp = new ArrayList<TWorkflowActivityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowGuardBeans
     */
    protected List<TWorkflowGuardBean> collTWorkflowGuardBeans;

    /**
     * Returns the collection of TWorkflowGuardBeans
     */
    public List<TWorkflowGuardBean> getTWorkflowGuardBeans()
    {
        return collTWorkflowGuardBeans;
    }

    /**
     * Sets the collection of TWorkflowGuardBeans to the specified value
     */
    public void setTWorkflowGuardBeans(List<TWorkflowGuardBean> list)
    {
        if (list == null)
        {
            collTWorkflowGuardBeans = null;
        }
        else
        {
            collTWorkflowGuardBeans = new ArrayList<TWorkflowGuardBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTEscalationEntryBeans
     */
    protected List<TEscalationEntryBean> collTEscalationEntryBeans;

    /**
     * Returns the collection of TEscalationEntryBeans
     */
    public List<TEscalationEntryBean> getTEscalationEntryBeans()
    {
        return collTEscalationEntryBeans;
    }

    /**
     * Sets the collection of TEscalationEntryBeans to the specified value
     */
    public void setTEscalationEntryBeans(List<TEscalationEntryBean> list)
    {
        if (list == null)
        {
            collTEscalationEntryBeans = null;
        }
        else
        {
            collTEscalationEntryBeans = new ArrayList<TEscalationEntryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReadIssueBeans
     */
    protected List<TReadIssueBean> collTReadIssueBeans;

    /**
     * Returns the collection of TReadIssueBeans
     */
    public List<TReadIssueBean> getTReadIssueBeans()
    {
        return collTReadIssueBeans;
    }

    /**
     * Sets the collection of TReadIssueBeans to the specified value
     */
    public void setTReadIssueBeans(List<TReadIssueBean> list)
    {
        if (list == null)
        {
            collTReadIssueBeans = null;
        }
        else
        {
            collTReadIssueBeans = new ArrayList<TReadIssueBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTLastExecutedQueryBeans
     */
    protected List<TLastExecutedQueryBean> collTLastExecutedQueryBeans;

    /**
     * Returns the collection of TLastExecutedQueryBeans
     */
    public List<TLastExecutedQueryBean> getTLastExecutedQueryBeans()
    {
        return collTLastExecutedQueryBeans;
    }

    /**
     * Sets the collection of TLastExecutedQueryBeans to the specified value
     */
    public void setTLastExecutedQueryBeans(List<TLastExecutedQueryBean> list)
    {
        if (list == null)
        {
            collTLastExecutedQueryBeans = null;
        }
        else
        {
            collTLastExecutedQueryBeans = new ArrayList<TLastExecutedQueryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReportSubscribeBeans
     */
    protected List<TReportSubscribeBean> collTReportSubscribeBeans;

    /**
     * Returns the collection of TReportSubscribeBeans
     */
    public List<TReportSubscribeBean> getTReportSubscribeBeans()
    {
        return collTReportSubscribeBeans;
    }

    /**
     * Sets the collection of TReportSubscribeBeans to the specified value
     */
    public void setTReportSubscribeBeans(List<TReportSubscribeBean> list)
    {
        if (list == null)
        {
            collTReportSubscribeBeans = null;
        }
        else
        {
            collTReportSubscribeBeans = new ArrayList<TReportSubscribeBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTGridLayoutBeans
     */
    protected List<TGridLayoutBean> collTGridLayoutBeans;

    /**
     * Returns the collection of TGridLayoutBeans
     */
    public List<TGridLayoutBean> getTGridLayoutBeans()
    {
        return collTGridLayoutBeans;
    }

    /**
     * Sets the collection of TGridLayoutBeans to the specified value
     */
    public void setTGridLayoutBeans(List<TGridLayoutBean> list)
    {
        if (list == null)
        {
            collTGridLayoutBeans = null;
        }
        else
        {
            collTGridLayoutBeans = new ArrayList<TGridLayoutBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTNavigatorLayoutBeans
     */
    protected List<TNavigatorLayoutBean> collTNavigatorLayoutBeans;

    /**
     * Returns the collection of TNavigatorLayoutBeans
     */
    public List<TNavigatorLayoutBean> getTNavigatorLayoutBeans()
    {
        return collTNavigatorLayoutBeans;
    }

    /**
     * Sets the collection of TNavigatorLayoutBeans to the specified value
     */
    public void setTNavigatorLayoutBeans(List<TNavigatorLayoutBean> list)
    {
        if (list == null)
        {
            collTNavigatorLayoutBeans = null;
        }
        else
        {
            collTNavigatorLayoutBeans = new ArrayList<TNavigatorLayoutBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTMailTextBlockBeans
     */
    protected List<TMailTextBlockBean> collTMailTextBlockBeans;

    /**
     * Returns the collection of TMailTextBlockBeans
     */
    public List<TMailTextBlockBean> getTMailTextBlockBeans()
    {
        return collTMailTextBlockBeans;
    }

    /**
     * Sets the collection of TMailTextBlockBeans to the specified value
     */
    public void setTMailTextBlockBeans(List<TMailTextBlockBean> list)
    {
        if (list == null)
        {
            collTMailTextBlockBeans = null;
        }
        else
        {
            collTMailTextBlockBeans = new ArrayList<TMailTextBlockBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTPersonInDomainBeans
     */
    protected List<TPersonInDomainBean> collTPersonInDomainBeans;

    /**
     * Returns the collection of TPersonInDomainBeans
     */
    public List<TPersonInDomainBean> getTPersonInDomainBeans()
    {
        return collTPersonInDomainBeans;
    }

    /**
     * Sets the collection of TPersonInDomainBeans to the specified value
     */
    public void setTPersonInDomainBeans(List<TPersonInDomainBean> list)
    {
        if (list == null)
        {
            collTPersonInDomainBeans = null;
        }
        else
        {
            collTPersonInDomainBeans = new ArrayList<TPersonInDomainBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTAttachmentVersionBeans
     */
    protected List<TAttachmentVersionBean> collTAttachmentVersionBeans;

    /**
     * Returns the collection of TAttachmentVersionBeans
     */
    public List<TAttachmentVersionBean> getTAttachmentVersionBeans()
    {
        return collTAttachmentVersionBeans;
    }

    /**
     * Sets the collection of TAttachmentVersionBeans to the specified value
     */
    public void setTAttachmentVersionBeans(List<TAttachmentVersionBean> list)
    {
        if (list == null)
        {
            collTAttachmentVersionBeans = null;
        }
        else
        {
            collTAttachmentVersionBeans = new ArrayList<TAttachmentVersionBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTUserFeatureBeans
     */
    protected List<TUserFeatureBean> collTUserFeatureBeans;

    /**
     * Returns the collection of TUserFeatureBeans
     */
    public List<TUserFeatureBean> getTUserFeatureBeans()
    {
        return collTUserFeatureBeans;
    }

    /**
     * Sets the collection of TUserFeatureBeans to the specified value
     */
    public void setTUserFeatureBeans(List<TUserFeatureBean> list)
    {
        if (list == null)
        {
            collTUserFeatureBeans = null;
        }
        else
        {
            collTUserFeatureBeans = new ArrayList<TUserFeatureBean>(list);
        }
    }

}
