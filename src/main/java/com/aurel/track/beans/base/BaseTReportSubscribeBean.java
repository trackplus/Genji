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
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TReportSubscribeBean
 */
public abstract class BaseTReportSubscribeBean
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

    /** The value for the person field */
    private Integer person;

    /** The value for the recurrencePattern field */
    private Integer recurrencePattern;

    /** The value for the reportTemplate field */
    private Integer reportTemplate;

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
     * Get the Person
     *
     * @return Integer
     */
    public Integer getPerson ()
    {
        return person;
    }

    /**
     * Set the value of Person
     *
     * @param v new value
     */
    public void setPerson(Integer v)
    {

        this.person = v;
        setModified(true);

    }

    /**
     * Get the RecurrencePattern
     *
     * @return Integer
     */
    public Integer getRecurrencePattern ()
    {
        return recurrencePattern;
    }

    /**
     * Set the value of RecurrencePattern
     *
     * @param v new value
     */
    public void setRecurrencePattern(Integer v)
    {

        this.recurrencePattern = v;
        setModified(true);

    }

    /**
     * Get the ReportTemplate
     *
     * @return Integer
     */
    public Integer getReportTemplate ()
    {
        return reportTemplate;
    }

    /**
     * Set the value of ReportTemplate
     *
     * @param v new value
     */
    public void setReportTemplate(Integer v)
    {

        this.reportTemplate = v;
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
            setPerson((Integer) null);
        }
        else
        {
            setPerson(v.getObjectID());
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





    private TRecurrencePatternBean aTRecurrencePatternBean;

    /**
     * sets an associated TRecurrencePatternBean object
     *
     * @param v TRecurrencePatternBean
     */
    public void setTRecurrencePatternBean(TRecurrencePatternBean v)
    {
        if (v == null)
        {
            setRecurrencePattern((Integer) null);
        }
        else
        {
            setRecurrencePattern(v.getObjectID());
        }
        aTRecurrencePatternBean = v;
    }


    /**
     * Get the associated TRecurrencePatternBean object
     *
     * @return the associated TRecurrencePatternBean object
     */
    public TRecurrencePatternBean getTRecurrencePatternBean()
    {
        return aTRecurrencePatternBean;
    }





    private TExportTemplateBean aTExportTemplateBean;

    /**
     * sets an associated TExportTemplateBean object
     *
     * @param v TExportTemplateBean
     */
    public void setTExportTemplateBean(TExportTemplateBean v)
    {
        if (v == null)
        {
            setReportTemplate((Integer) null);
        }
        else
        {
            setReportTemplate(v.getObjectID());
        }
        aTExportTemplateBean = v;
    }


    /**
     * Get the associated TExportTemplateBean object
     *
     * @return the associated TExportTemplateBean object
     */
    public TExportTemplateBean getTExportTemplateBean()
    {
        return aTExportTemplateBean;
    }



}
