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
import com.aurel.track.beans.TMotdBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TMotd
 */
public abstract class BaseTMotd extends TpBaseObject
{
    /** The Peer class */
    private static final TMotdPeer peer =
        new TMotdPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the theLocale field */
    private String theLocale;

    /** The value for the theMessage field */
    private String theMessage;

    /** The value for the teaserText field */
    private String teaserText;

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
     * Get the TheLocale
     *
     * @return String
     */
    public String getTheLocale()
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

        if (!ObjectUtils.equals(this.theLocale, v))
        {
            this.theLocale = v;
            setModified(true);
        }


    }

    /**
     * Get the TheMessage
     *
     * @return String
     */
    public String getTheMessage()
    {
        return theMessage;
    }


    /**
     * Set the value of TheMessage
     *
     * @param v new value
     */
    public void setTheMessage(String v) 
    {

        if (!ObjectUtils.equals(this.theMessage, v))
        {
            this.theMessage = v;
            setModified(true);
        }


    }

    /**
     * Get the TeaserText
     *
     * @return String
     */
    public String getTeaserText()
    {
        return teaserText;
    }


    /**
     * Set the value of TeaserText
     *
     * @param v new value
     */
    public void setTeaserText(String v) 
    {

        if (!ObjectUtils.equals(this.teaserText, v))
        {
            this.teaserText = v;
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
            fieldNames.add("TheLocale");
            fieldNames.add("TheMessage");
            fieldNames.add("TeaserText");
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
        if (name.equals("TheLocale"))
        {
            return getTheLocale();
        }
        if (name.equals("TheMessage"))
        {
            return getTheMessage();
        }
        if (name.equals("TeaserText"))
        {
            return getTeaserText();
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
        if (name.equals("TheLocale"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTheLocale((String) value);
            return true;
        }
        if (name.equals("TheMessage"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTheMessage((String) value);
            return true;
        }
        if (name.equals("TeaserText"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTeaserText((String) value);
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
        if (name.equals(TMotdPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TMotdPeer.THELOCALE))
        {
            return getTheLocale();
        }
        if (name.equals(TMotdPeer.THEMESSAGE))
        {
            return getTheMessage();
        }
        if (name.equals(TMotdPeer.TEASERTEXT))
        {
            return getTeaserText();
        }
        if (name.equals(TMotdPeer.TPUUID))
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
      if (TMotdPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TMotdPeer.THELOCALE.equals(name))
        {
            return setByName("TheLocale", value);
        }
      if (TMotdPeer.THEMESSAGE.equals(name))
        {
            return setByName("TheMessage", value);
        }
      if (TMotdPeer.TEASERTEXT.equals(name))
        {
            return setByName("TeaserText", value);
        }
      if (TMotdPeer.TPUUID.equals(name))
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
            return getTheLocale();
        }
        if (pos == 2)
        {
            return getTheMessage();
        }
        if (pos == 3)
        {
            return getTeaserText();
        }
        if (pos == 4)
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
            return setByName("TheLocale", value);
        }
    if (position == 2)
        {
            return setByName("TheMessage", value);
        }
    if (position == 3)
        {
            return setByName("TeaserText", value);
        }
    if (position == 4)
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
        save(TMotdPeer.DATABASE_NAME);
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
                    TMotdPeer.doInsert((TMotd) this, con);
                    setNew(false);
                }
                else
                {
                    TMotdPeer.doUpdate((TMotd) this, con);
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
    public TMotd copy() throws TorqueException
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
    public TMotd copy(Connection con) throws TorqueException
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
    public TMotd copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TMotd(), deepcopy);
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
    public TMotd copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TMotd(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TMotd copyInto(TMotd copyObj) throws TorqueException
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
    protected TMotd copyInto(TMotd copyObj, Connection con) throws TorqueException
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
    protected TMotd copyInto(TMotd copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setTheLocale(theLocale);
        copyObj.setTheMessage(theMessage);
        copyObj.setTeaserText(teaserText);
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
    protected TMotd copyInto(TMotd copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setTheLocale(theLocale);
        copyObj.setTheMessage(theMessage);
        copyObj.setTeaserText(teaserText);
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
    public TMotdPeer getPeer()
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
        return TMotdPeer.getTableMap();
    }

  
    /**
     * Creates a TMotdBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TMotdBean with the contents of this object
     */
    public TMotdBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TMotdBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TMotdBean with the contents of this object
     */
    public TMotdBean getBean(IdentityMap createdBeans)
    {
        TMotdBean result = (TMotdBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TMotdBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setTheLocale(getTheLocale());
        result.setTheMessage(getTheMessage());
        result.setTeaserText(getTeaserText());
        result.setUuid(getUuid());


        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TMotd with the contents
     * of a TMotdBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TMotdBean which contents are used to create
     *        the resulting class
     * @return an instance of TMotd with the contents of bean
     */
    public static TMotd createTMotd(TMotdBean bean)
        throws TorqueException
    {
        return createTMotd(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TMotd with the contents
     * of a TMotdBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TMotdBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TMotd with the contents of bean
     */

    public static TMotd createTMotd(TMotdBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TMotd result = (TMotd) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TMotd();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setTheLocale(bean.getTheLocale());
        result.setTheMessage(bean.getTheMessage());
        result.setTeaserText(bean.getTeaserText());
        result.setUuid(bean.getUuid());


    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TMotd:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("TheLocale = ")
           .append(getTheLocale())
           .append("\n");
        str.append("TheMessage = ")
           .append(getTheMessage())
           .append("\n");
        str.append("TeaserText = ")
           .append(getTeaserText())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
