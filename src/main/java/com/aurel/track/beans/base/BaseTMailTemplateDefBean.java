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
 * Locale and e-mail type specific template definitions for notification emails
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TMailTemplateDefBean
 */
public abstract class BaseTMailTemplateDefBean
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

    /** The value for the mailTemplate field */
    private Integer mailTemplate;

    /** The value for the mailSubject field */
    private String mailSubject;

    /** The value for the mailBody field */
    private String mailBody;

    /** The value for the theLocale field */
    private String theLocale;

    /** The value for the plainEmail field */
    private String plainEmail = "N";

    /** The value for the templateChanged field */
    private String templateChanged = "N";

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
     * Get the MailTemplate
     *
     * @return Integer
     */
    public Integer getMailTemplate ()
    {
        return mailTemplate;
    }

    /**
     * Set the value of MailTemplate
     *
     * @param v new value
     */
    public void setMailTemplate(Integer v)
    {

        this.mailTemplate = v;
        setModified(true);

    }

    /**
     * Get the MailSubject
     *
     * @return String
     */
    public String getMailSubject ()
    {
        return mailSubject;
    }

    /**
     * Set the value of MailSubject
     *
     * @param v new value
     */
    public void setMailSubject(String v)
    {

        this.mailSubject = v;
        setModified(true);

    }

    /**
     * Get the MailBody
     *
     * @return String
     */
    public String getMailBody ()
    {
        return mailBody;
    }

    /**
     * Set the value of MailBody
     *
     * @param v new value
     */
    public void setMailBody(String v)
    {

        this.mailBody = v;
        setModified(true);

    }

    /**
     * Get the TheLocale
     *
     * @return String
     */
    public String getTheLocale ()
    {
        return theLocale;
    }

    /**
     * Set the value of TheLocale
     *
     * @param v new value
     */
    public void setTheLocale(String v)
    {

        this.theLocale = v;
        setModified(true);

    }

    /**
     * Get the PlainEmail
     *
     * @return String
     */
    public String getPlainEmail ()
    {
        return plainEmail;
    }

    /**
     * Set the value of PlainEmail
     *
     * @param v new value
     */
    public void setPlainEmail(String v)
    {

        this.plainEmail = v;
        setModified(true);

    }

    /**
     * Get the TemplateChanged
     *
     * @return String
     */
    public String getTemplateChanged ()
    {
        return templateChanged;
    }

    /**
     * Set the value of TemplateChanged
     *
     * @param v new value
     */
    public void setTemplateChanged(String v)
    {

        this.templateChanged = v;
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

    



    private TMailTemplateBean aTMailTemplateBean;

    /**
     * sets an associated TMailTemplateBean object
     *
     * @param v TMailTemplateBean
     */
    public void setTMailTemplateBean(TMailTemplateBean v)
    {
        if (v == null)
        {
            setMailTemplate((Integer) null);
        }
        else
        {
            setMailTemplate(v.getObjectID());
        }
        aTMailTemplateBean = v;
    }


    /**
     * Get the associated TMailTemplateBean object
     *
     * @return the associated TMailTemplateBean object
     */
    public TMailTemplateBean getTMailTemplateBean()
    {
        return aTMailTemplateBean;
    }



}
