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
 * fields for card
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TCardFieldOptionBean
 */
public abstract class BaseTCardFieldOptionBean
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

    /** The value for the groupingField field */
    private Integer groupingField;

    /** The value for the optionID field */
    private Integer optionID;

    /** The value for the optionPosition field */
    private Integer optionPosition;

    /** The value for the optionWidth field */
    private Integer optionWidth;

    /** The value for the maxNumber field */
    private Integer maxNumber;

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
     * Get the GroupingField
     *
     * @return Integer
     */
    public Integer getGroupingField ()
    {
        return groupingField;
    }

    /**
     * Set the value of GroupingField
     *
     * @param v new value
     */
    public void setGroupingField(Integer v)
    {

        this.groupingField = v;
        setModified(true);

    }

    /**
     * Get the OptionID
     *
     * @return Integer
     */
    public Integer getOptionID ()
    {
        return optionID;
    }

    /**
     * Set the value of OptionID
     *
     * @param v new value
     */
    public void setOptionID(Integer v)
    {

        this.optionID = v;
        setModified(true);

    }

    /**
     * Get the OptionPosition
     *
     * @return Integer
     */
    public Integer getOptionPosition ()
    {
        return optionPosition;
    }

    /**
     * Set the value of OptionPosition
     *
     * @param v new value
     */
    public void setOptionPosition(Integer v)
    {

        this.optionPosition = v;
        setModified(true);

    }

    /**
     * Get the OptionWidth
     *
     * @return Integer
     */
    public Integer getOptionWidth ()
    {
        return optionWidth;
    }

    /**
     * Set the value of OptionWidth
     *
     * @param v new value
     */
    public void setOptionWidth(Integer v)
    {

        this.optionWidth = v;
        setModified(true);

    }

    /**
     * Get the MaxNumber
     *
     * @return Integer
     */
    public Integer getMaxNumber ()
    {
        return maxNumber;
    }

    /**
     * Set the value of MaxNumber
     *
     * @param v new value
     */
    public void setMaxNumber(Integer v)
    {

        this.maxNumber = v;
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

    



    private TCardGroupingFieldBean aTCardGroupingFieldBean;

    /**
     * sets an associated TCardGroupingFieldBean object
     *
     * @param v TCardGroupingFieldBean
     */
    public void setTCardGroupingFieldBean(TCardGroupingFieldBean v)
    {
        if (v == null)
        {
            setGroupingField((Integer) null);
        }
        else
        {
            setGroupingField(v.getObjectID());
        }
        aTCardGroupingFieldBean = v;
    }


    /**
     * Get the associated TCardGroupingFieldBean object
     *
     * @return the associated TCardGroupingFieldBean object
     */
    public TCardGroupingFieldBean getTCardGroupingFieldBean()
    {
        return aTCardGroupingFieldBean;
    }



}
