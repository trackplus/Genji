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
 * defines the parts of an outline code
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TOutlineTemplateDefBean
 */
public abstract class BaseTOutlineTemplateDefBean
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

    /** The value for the levelNo field */
    private Integer levelNo;

    /** The value for the sequenceType field */
    private Integer sequenceType;

    /** The value for the listID field */
    private Integer listID;

    /** The value for the sequenceLength field */
    private Integer sequenceLength;

    /** The value for the separatorChar field */
    private String separatorChar;

    /** The value for the outlineTemplate field */
    private Integer outlineTemplate;

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
     * Get the LevelNo
     *
     * @return Integer
     */
    public Integer getLevelNo ()
    {
        return levelNo;
    }

    /**
     * Set the value of LevelNo
     *
     * @param v new value
     */
    public void setLevelNo(Integer v)
    {

        this.levelNo = v;
        setModified(true);

    }

    /**
     * Get the SequenceType
     *
     * @return Integer
     */
    public Integer getSequenceType ()
    {
        return sequenceType;
    }

    /**
     * Set the value of SequenceType
     *
     * @param v new value
     */
    public void setSequenceType(Integer v)
    {

        this.sequenceType = v;
        setModified(true);

    }

    /**
     * Get the ListID
     *
     * @return Integer
     */
    public Integer getListID ()
    {
        return listID;
    }

    /**
     * Set the value of ListID
     *
     * @param v new value
     */
    public void setListID(Integer v)
    {

        this.listID = v;
        setModified(true);

    }

    /**
     * Get the SequenceLength
     *
     * @return Integer
     */
    public Integer getSequenceLength ()
    {
        return sequenceLength;
    }

    /**
     * Set the value of SequenceLength
     *
     * @param v new value
     */
    public void setSequenceLength(Integer v)
    {

        this.sequenceLength = v;
        setModified(true);

    }

    /**
     * Get the SeparatorChar
     *
     * @return String
     */
    public String getSeparatorChar ()
    {
        return separatorChar;
    }

    /**
     * Set the value of SeparatorChar
     *
     * @param v new value
     */
    public void setSeparatorChar(String v)
    {

        this.separatorChar = v;
        setModified(true);

    }

    /**
     * Get the OutlineTemplate
     *
     * @return Integer
     */
    public Integer getOutlineTemplate ()
    {
        return outlineTemplate;
    }

    /**
     * Set the value of OutlineTemplate
     *
     * @param v new value
     */
    public void setOutlineTemplate(Integer v)
    {

        this.outlineTemplate = v;
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

    



    private TOutlineTemplateBean aTOutlineTemplateBean;

    /**
     * sets an associated TOutlineTemplateBean object
     *
     * @param v TOutlineTemplateBean
     */
    public void setTOutlineTemplateBean(TOutlineTemplateBean v)
    {
        if (v == null)
        {
            setOutlineTemplate((Integer) null);
        }
        else
        {
            setOutlineTemplate(v.getObjectID());
        }
        aTOutlineTemplateBean = v;
    }


    /**
     * Get the associated TOutlineTemplateBean object
     *
     * @return the associated TOutlineTemplateBean object
     */
    public TOutlineTemplateBean getTOutlineTemplateBean()
    {
        return aTOutlineTemplateBean;
    }



}
