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
 * shortcut  definitions for menu items
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TShortcutBean
 */
public abstract class BaseTShortcutBean
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

    /** The value for the shortcutKey field */
    private Integer shortcutKey;

    /** The value for the ctrlKey field */
    private String ctrlKey = "N";

    /** The value for the shiftKey field */
    private String shiftKey = "N";

    /** The value for the altKey field */
    private String altKey = "N";

    /** The value for the menuItemKey field */
    private Integer menuItemKey;

    /** The value for the menuContext field */
    private Integer menuContext;

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
     * Get the ShortcutKey
     *
     * @return Integer
     */
    public Integer getShortcutKey ()
    {
        return shortcutKey;
    }

    /**
     * Set the value of ShortcutKey
     *
     * @param v new value
     */
    public void setShortcutKey(Integer v)
    {

        this.shortcutKey = v;
        setModified(true);

    }

    /**
     * Get the CtrlKey
     *
     * @return String
     */
    public String getCtrlKey ()
    {
        return ctrlKey;
    }

    /**
     * Set the value of CtrlKey
     *
     * @param v new value
     */
    public void setCtrlKey(String v)
    {

        this.ctrlKey = v;
        setModified(true);

    }

    /**
     * Get the ShiftKey
     *
     * @return String
     */
    public String getShiftKey ()
    {
        return shiftKey;
    }

    /**
     * Set the value of ShiftKey
     *
     * @param v new value
     */
    public void setShiftKey(String v)
    {

        this.shiftKey = v;
        setModified(true);

    }

    /**
     * Get the AltKey
     *
     * @return String
     */
    public String getAltKey ()
    {
        return altKey;
    }

    /**
     * Set the value of AltKey
     *
     * @param v new value
     */
    public void setAltKey(String v)
    {

        this.altKey = v;
        setModified(true);

    }

    /**
     * Get the MenuItemKey
     *
     * @return Integer
     */
    public Integer getMenuItemKey ()
    {
        return menuItemKey;
    }

    /**
     * Set the value of MenuItemKey
     *
     * @param v new value
     */
    public void setMenuItemKey(Integer v)
    {

        this.menuItemKey = v;
        setModified(true);

    }

    /**
     * Get the MenuContext
     *
     * @return Integer
     */
    public Integer getMenuContext ()
    {
        return menuContext;
    }

    /**
     * Set the value of MenuContext
     *
     * @param v new value
     */
    public void setMenuContext(Integer v)
    {

        this.menuContext = v;
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
