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
 * extended; all references should be to TScreenFieldBean
 */
public abstract class BaseTScreenFieldBean
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

    /** The value for the name field */
    private String name;

    /** The value for the description field */
    private String description;

    /** The value for the index field */
    private Integer index;

    /** The value for the colIndex field */
    private Integer colIndex;

    /** The value for the rowIndex field */
    private Integer rowIndex;

    /** The value for the colSpan field */
    private Integer colSpan;

    /** The value for the rowSpan field */
    private Integer rowSpan;

    /** The value for the labelHAlign field */
    private Integer labelHAlign;

    /** The value for the labelVAlign field */
    private Integer labelVAlign;

    /** The value for the valueHAlign field */
    private Integer valueHAlign;

    /** The value for the valueVAlign field */
    private Integer valueVAlign;

    /** The value for the isEmpty field */
    private String isEmpty = "N";

    /** The value for the parent field */
    private Integer parent;

    /** The value for the field field */
    private Integer field;

    /** The value for the hideLabel field */
    private String hideLabel = "N";

    /** The value for the iconRendering field */
    private Integer iconRendering;

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
     * Get the Name
     *
     * @return String
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Set the value of Name
     *
     * @param v new value
     */
    public void setName(String v)
    {

        this.name = v;
        setModified(true);

    }

    /**
     * Get the Description
     *
     * @return String
     */
    public String getDescription ()
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

        this.description = v;
        setModified(true);

    }

    /**
     * Get the Index
     *
     * @return Integer
     */
    public Integer getIndex ()
    {
        return index;
    }

    /**
     * Set the value of Index
     *
     * @param v new value
     */
    public void setIndex(Integer v)
    {

        this.index = v;
        setModified(true);

    }

    /**
     * Get the ColIndex
     *
     * @return Integer
     */
    public Integer getColIndex ()
    {
        return colIndex;
    }

    /**
     * Set the value of ColIndex
     *
     * @param v new value
     */
    public void setColIndex(Integer v)
    {

        this.colIndex = v;
        setModified(true);

    }

    /**
     * Get the RowIndex
     *
     * @return Integer
     */
    public Integer getRowIndex ()
    {
        return rowIndex;
    }

    /**
     * Set the value of RowIndex
     *
     * @param v new value
     */
    public void setRowIndex(Integer v)
    {

        this.rowIndex = v;
        setModified(true);

    }

    /**
     * Get the ColSpan
     *
     * @return Integer
     */
    public Integer getColSpan ()
    {
        return colSpan;
    }

    /**
     * Set the value of ColSpan
     *
     * @param v new value
     */
    public void setColSpan(Integer v)
    {

        this.colSpan = v;
        setModified(true);

    }

    /**
     * Get the RowSpan
     *
     * @return Integer
     */
    public Integer getRowSpan ()
    {
        return rowSpan;
    }

    /**
     * Set the value of RowSpan
     *
     * @param v new value
     */
    public void setRowSpan(Integer v)
    {

        this.rowSpan = v;
        setModified(true);

    }

    /**
     * Get the LabelHAlign
     *
     * @return Integer
     */
    public Integer getLabelHAlign ()
    {
        return labelHAlign;
    }

    /**
     * Set the value of LabelHAlign
     *
     * @param v new value
     */
    public void setLabelHAlign(Integer v)
    {

        this.labelHAlign = v;
        setModified(true);

    }

    /**
     * Get the LabelVAlign
     *
     * @return Integer
     */
    public Integer getLabelVAlign ()
    {
        return labelVAlign;
    }

    /**
     * Set the value of LabelVAlign
     *
     * @param v new value
     */
    public void setLabelVAlign(Integer v)
    {

        this.labelVAlign = v;
        setModified(true);

    }

    /**
     * Get the ValueHAlign
     *
     * @return Integer
     */
    public Integer getValueHAlign ()
    {
        return valueHAlign;
    }

    /**
     * Set the value of ValueHAlign
     *
     * @param v new value
     */
    public void setValueHAlign(Integer v)
    {

        this.valueHAlign = v;
        setModified(true);

    }

    /**
     * Get the ValueVAlign
     *
     * @return Integer
     */
    public Integer getValueVAlign ()
    {
        return valueVAlign;
    }

    /**
     * Set the value of ValueVAlign
     *
     * @param v new value
     */
    public void setValueVAlign(Integer v)
    {

        this.valueVAlign = v;
        setModified(true);

    }

    /**
     * Get the IsEmpty
     *
     * @return String
     */
    public String getIsEmpty ()
    {
        return isEmpty;
    }

    /**
     * Set the value of IsEmpty
     *
     * @param v new value
     */
    public void setIsEmpty(String v)
    {

        this.isEmpty = v;
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
     * Get the Field
     *
     * @return Integer
     */
    public Integer getField ()
    {
        return field;
    }

    /**
     * Set the value of Field
     *
     * @param v new value
     */
    public void setField(Integer v)
    {

        this.field = v;
        setModified(true);

    }

    /**
     * Get the HideLabel
     *
     * @return String
     */
    public String getHideLabel ()
    {
        return hideLabel;
    }

    /**
     * Set the value of HideLabel
     *
     * @param v new value
     */
    public void setHideLabel(String v)
    {

        this.hideLabel = v;
        setModified(true);

    }

    /**
     * Get the IconRendering
     *
     * @return Integer
     */
    public Integer getIconRendering ()
    {
        return iconRendering;
    }

    /**
     * Set the value of IconRendering
     *
     * @param v new value
     */
    public void setIconRendering(Integer v)
    {

        this.iconRendering = v;
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

    



    private TScreenPanelBean aTScreenPanelBean;

    /**
     * sets an associated TScreenPanelBean object
     *
     * @param v TScreenPanelBean
     */
    public void setTScreenPanelBean(TScreenPanelBean v)
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTScreenPanelBean = v;
    }


    /**
     * Get the associated TScreenPanelBean object
     *
     * @return the associated TScreenPanelBean object
     */
    public TScreenPanelBean getTScreenPanelBean()
    {
        return aTScreenPanelBean;
    }





    private TFieldBean aTFieldBean;

    /**
     * sets an associated TFieldBean object
     *
     * @param v TFieldBean
     */
    public void setTFieldBean(TFieldBean v)
    {
        if (v == null)
        {
            setField((Integer) null);
        }
        else
        {
            setField(v.getObjectID());
        }
        aTFieldBean = v;
    }


    /**
     * Get the associated TFieldBean object
     *
     * @return the associated TFieldBean object
     */
    public TFieldBean getTFieldBean()
    {
        return aTFieldBean;
    }



}
