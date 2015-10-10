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
import com.aurel.track.beans.TShortcutBean;



/**
 * shortcut  definitions for menu items
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TShortcut
 */
public abstract class BaseTShortcut extends TpBaseObject
{
    /** The Peer class */
    private static final TShortcutPeer peer =
        new TShortcutPeer();


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
     * Get the ShortcutKey
     *
     * @return Integer
     */
    public Integer getShortcutKey()
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

        if (!ObjectUtils.equals(this.shortcutKey, v))
        {
            this.shortcutKey = v;
            setModified(true);
        }


    }

    /**
     * Get the CtrlKey
     *
     * @return String
     */
    public String getCtrlKey()
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

        if (!ObjectUtils.equals(this.ctrlKey, v))
        {
            this.ctrlKey = v;
            setModified(true);
        }


    }

    /**
     * Get the ShiftKey
     *
     * @return String
     */
    public String getShiftKey()
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

        if (!ObjectUtils.equals(this.shiftKey, v))
        {
            this.shiftKey = v;
            setModified(true);
        }


    }

    /**
     * Get the AltKey
     *
     * @return String
     */
    public String getAltKey()
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

        if (!ObjectUtils.equals(this.altKey, v))
        {
            this.altKey = v;
            setModified(true);
        }


    }

    /**
     * Get the MenuItemKey
     *
     * @return Integer
     */
    public Integer getMenuItemKey()
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

        if (!ObjectUtils.equals(this.menuItemKey, v))
        {
            this.menuItemKey = v;
            setModified(true);
        }


    }

    /**
     * Get the MenuContext
     *
     * @return Integer
     */
    public Integer getMenuContext()
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

        if (!ObjectUtils.equals(this.menuContext, v))
        {
            this.menuContext = v;
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
            fieldNames.add("ShortcutKey");
            fieldNames.add("CtrlKey");
            fieldNames.add("ShiftKey");
            fieldNames.add("AltKey");
            fieldNames.add("MenuItemKey");
            fieldNames.add("MenuContext");
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
        if (name.equals("ShortcutKey"))
        {
            return getShortcutKey();
        }
        if (name.equals("CtrlKey"))
        {
            return getCtrlKey();
        }
        if (name.equals("ShiftKey"))
        {
            return getShiftKey();
        }
        if (name.equals("AltKey"))
        {
            return getAltKey();
        }
        if (name.equals("MenuItemKey"))
        {
            return getMenuItemKey();
        }
        if (name.equals("MenuContext"))
        {
            return getMenuContext();
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
        if (name.equals("ShortcutKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setShortcutKey((Integer) value);
            return true;
        }
        if (name.equals("CtrlKey"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCtrlKey((String) value);
            return true;
        }
        if (name.equals("ShiftKey"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setShiftKey((String) value);
            return true;
        }
        if (name.equals("AltKey"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAltKey((String) value);
            return true;
        }
        if (name.equals("MenuItemKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMenuItemKey((Integer) value);
            return true;
        }
        if (name.equals("MenuContext"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMenuContext((Integer) value);
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
        if (name.equals(TShortcutPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TShortcutPeer.SHORTCUTKEY))
        {
            return getShortcutKey();
        }
        if (name.equals(TShortcutPeer.CTRLKEY))
        {
            return getCtrlKey();
        }
        if (name.equals(TShortcutPeer.SHIFTKEY))
        {
            return getShiftKey();
        }
        if (name.equals(TShortcutPeer.ALTKEY))
        {
            return getAltKey();
        }
        if (name.equals(TShortcutPeer.MENUITEMKEY))
        {
            return getMenuItemKey();
        }
        if (name.equals(TShortcutPeer.MENUCONTEXT))
        {
            return getMenuContext();
        }
        if (name.equals(TShortcutPeer.TPUUID))
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
      if (TShortcutPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TShortcutPeer.SHORTCUTKEY.equals(name))
        {
            return setByName("ShortcutKey", value);
        }
      if (TShortcutPeer.CTRLKEY.equals(name))
        {
            return setByName("CtrlKey", value);
        }
      if (TShortcutPeer.SHIFTKEY.equals(name))
        {
            return setByName("ShiftKey", value);
        }
      if (TShortcutPeer.ALTKEY.equals(name))
        {
            return setByName("AltKey", value);
        }
      if (TShortcutPeer.MENUITEMKEY.equals(name))
        {
            return setByName("MenuItemKey", value);
        }
      if (TShortcutPeer.MENUCONTEXT.equals(name))
        {
            return setByName("MenuContext", value);
        }
      if (TShortcutPeer.TPUUID.equals(name))
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
            return getShortcutKey();
        }
        if (pos == 2)
        {
            return getCtrlKey();
        }
        if (pos == 3)
        {
            return getShiftKey();
        }
        if (pos == 4)
        {
            return getAltKey();
        }
        if (pos == 5)
        {
            return getMenuItemKey();
        }
        if (pos == 6)
        {
            return getMenuContext();
        }
        if (pos == 7)
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
            return setByName("ShortcutKey", value);
        }
    if (position == 2)
        {
            return setByName("CtrlKey", value);
        }
    if (position == 3)
        {
            return setByName("ShiftKey", value);
        }
    if (position == 4)
        {
            return setByName("AltKey", value);
        }
    if (position == 5)
        {
            return setByName("MenuItemKey", value);
        }
    if (position == 6)
        {
            return setByName("MenuContext", value);
        }
    if (position == 7)
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
        save(TShortcutPeer.DATABASE_NAME);
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
                    TShortcutPeer.doInsert((TShortcut) this, con);
                    setNew(false);
                }
                else
                {
                    TShortcutPeer.doUpdate((TShortcut) this, con);
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
    public TShortcut copy() throws TorqueException
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
    public TShortcut copy(Connection con) throws TorqueException
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
    public TShortcut copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TShortcut(), deepcopy);
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
    public TShortcut copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TShortcut(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TShortcut copyInto(TShortcut copyObj) throws TorqueException
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
    protected TShortcut copyInto(TShortcut copyObj, Connection con) throws TorqueException
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
    protected TShortcut copyInto(TShortcut copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setShortcutKey(shortcutKey);
        copyObj.setCtrlKey(ctrlKey);
        copyObj.setShiftKey(shiftKey);
        copyObj.setAltKey(altKey);
        copyObj.setMenuItemKey(menuItemKey);
        copyObj.setMenuContext(menuContext);
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
    protected TShortcut copyInto(TShortcut copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setShortcutKey(shortcutKey);
        copyObj.setCtrlKey(ctrlKey);
        copyObj.setShiftKey(shiftKey);
        copyObj.setAltKey(altKey);
        copyObj.setMenuItemKey(menuItemKey);
        copyObj.setMenuContext(menuContext);
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
    public TShortcutPeer getPeer()
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
        return TShortcutPeer.getTableMap();
    }

  
    /**
     * Creates a TShortcutBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TShortcutBean with the contents of this object
     */
    public TShortcutBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TShortcutBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TShortcutBean with the contents of this object
     */
    public TShortcutBean getBean(IdentityMap createdBeans)
    {
        TShortcutBean result = (TShortcutBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TShortcutBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setShortcutKey(getShortcutKey());
        result.setCtrlKey(getCtrlKey());
        result.setShiftKey(getShiftKey());
        result.setAltKey(getAltKey());
        result.setMenuItemKey(getMenuItemKey());
        result.setMenuContext(getMenuContext());
        result.setUuid(getUuid());


        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TShortcut with the contents
     * of a TShortcutBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TShortcutBean which contents are used to create
     *        the resulting class
     * @return an instance of TShortcut with the contents of bean
     */
    public static TShortcut createTShortcut(TShortcutBean bean)
        throws TorqueException
    {
        return createTShortcut(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TShortcut with the contents
     * of a TShortcutBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TShortcutBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TShortcut with the contents of bean
     */

    public static TShortcut createTShortcut(TShortcutBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TShortcut result = (TShortcut) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TShortcut();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setShortcutKey(bean.getShortcutKey());
        result.setCtrlKey(bean.getCtrlKey());
        result.setShiftKey(bean.getShiftKey());
        result.setAltKey(bean.getAltKey());
        result.setMenuItemKey(bean.getMenuItemKey());
        result.setMenuContext(bean.getMenuContext());
        result.setUuid(bean.getUuid());


    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TShortcut:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("ShortcutKey = ")
           .append(getShortcutKey())
           .append("\n");
        str.append("CtrlKey = ")
           .append(getCtrlKey())
           .append("\n");
        str.append("ShiftKey = ")
           .append(getShiftKey())
           .append("\n");
        str.append("AltKey = ")
           .append(getAltKey())
           .append("\n");
        str.append("MenuItemKey = ")
           .append(getMenuItemKey())
           .append("\n");
        str.append("MenuContext = ")
           .append(getMenuContext())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
