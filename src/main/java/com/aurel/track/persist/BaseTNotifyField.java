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



import com.aurel.track.persist.TNotifyTrigger;
import com.aurel.track.persist.TNotifyTriggerPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TNotifyFieldBean;
import com.aurel.track.beans.TNotifyTriggerBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TNotifyField
 */
public abstract class BaseTNotifyField extends TpBaseObject
{
    /** The Peer class */
    private static final TNotifyFieldPeer peer =
        new TNotifyFieldPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the field field */
    private Integer field;

    /** The value for the actionType field */
    private Integer actionType;

    /** The value for the fieldType field */
    private Integer fieldType;

    /** The value for the notifyTrigger field */
    private Integer notifyTrigger;

    /** The value for the originator field */
    private String originator = "N";

    /** The value for the manager field */
    private String manager = "N";

    /** The value for the responsible field */
    private String responsible = "N";

    /** The value for the consultant field */
    private String consultant = "N";

    /** The value for the informant field */
    private String informant = "N";

    /** The value for the observer field */
    private String observer = "N";

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
     * Get the Field
     *
     * @return Integer
     */
    public Integer getField()
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

        if (!ObjectUtils.equals(this.field, v))
        {
            this.field = v;
            setModified(true);
        }


    }

    /**
     * Get the ActionType
     *
     * @return Integer
     */
    public Integer getActionType()
    {
        return actionType;
    }


    /**
     * Set the value of ActionType
     *
     * @param v new value
     */
    public void setActionType(Integer v) 
    {

        if (!ObjectUtils.equals(this.actionType, v))
        {
            this.actionType = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldType
     *
     * @return Integer
     */
    public Integer getFieldType()
    {
        return fieldType;
    }


    /**
     * Set the value of FieldType
     *
     * @param v new value
     */
    public void setFieldType(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldType, v))
        {
            this.fieldType = v;
            setModified(true);
        }


    }

    /**
     * Get the NotifyTrigger
     *
     * @return Integer
     */
    public Integer getNotifyTrigger()
    {
        return notifyTrigger;
    }


    /**
     * Set the value of NotifyTrigger
     *
     * @param v new value
     */
    public void setNotifyTrigger(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.notifyTrigger, v))
        {
            this.notifyTrigger = v;
            setModified(true);
        }


        if (aTNotifyTrigger != null && !ObjectUtils.equals(aTNotifyTrigger.getObjectID(), v))
        {
            aTNotifyTrigger = null;
        }

    }

    /**
     * Get the Originator
     *
     * @return String
     */
    public String getOriginator()
    {
        return originator;
    }


    /**
     * Set the value of Originator
     *
     * @param v new value
     */
    public void setOriginator(String v) 
    {

        if (!ObjectUtils.equals(this.originator, v))
        {
            this.originator = v;
            setModified(true);
        }


    }

    /**
     * Get the Manager
     *
     * @return String
     */
    public String getManager()
    {
        return manager;
    }


    /**
     * Set the value of Manager
     *
     * @param v new value
     */
    public void setManager(String v) 
    {

        if (!ObjectUtils.equals(this.manager, v))
        {
            this.manager = v;
            setModified(true);
        }


    }

    /**
     * Get the Responsible
     *
     * @return String
     */
    public String getResponsible()
    {
        return responsible;
    }


    /**
     * Set the value of Responsible
     *
     * @param v new value
     */
    public void setResponsible(String v) 
    {

        if (!ObjectUtils.equals(this.responsible, v))
        {
            this.responsible = v;
            setModified(true);
        }


    }

    /**
     * Get the Consultant
     *
     * @return String
     */
    public String getConsultant()
    {
        return consultant;
    }


    /**
     * Set the value of Consultant
     *
     * @param v new value
     */
    public void setConsultant(String v) 
    {

        if (!ObjectUtils.equals(this.consultant, v))
        {
            this.consultant = v;
            setModified(true);
        }


    }

    /**
     * Get the Informant
     *
     * @return String
     */
    public String getInformant()
    {
        return informant;
    }


    /**
     * Set the value of Informant
     *
     * @param v new value
     */
    public void setInformant(String v) 
    {

        if (!ObjectUtils.equals(this.informant, v))
        {
            this.informant = v;
            setModified(true);
        }


    }

    /**
     * Get the Observer
     *
     * @return String
     */
    public String getObserver()
    {
        return observer;
    }


    /**
     * Set the value of Observer
     *
     * @param v new value
     */
    public void setObserver(String v) 
    {

        if (!ObjectUtils.equals(this.observer, v))
        {
            this.observer = v;
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

    



    private TNotifyTrigger aTNotifyTrigger;

    /**
     * Declares an association between this object and a TNotifyTrigger object
     *
     * @param v TNotifyTrigger
     * @throws TorqueException
     */
    public void setTNotifyTrigger(TNotifyTrigger v) throws TorqueException
    {
        if (v == null)
        {
            setNotifyTrigger((Integer) null);
        }
        else
        {
            setNotifyTrigger(v.getObjectID());
        }
        aTNotifyTrigger = v;
    }


    /**
     * Returns the associated TNotifyTrigger object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TNotifyTrigger object
     * @throws TorqueException
     */
    public TNotifyTrigger getTNotifyTrigger()
        throws TorqueException
    {
        if (aTNotifyTrigger == null && (!ObjectUtils.equals(this.notifyTrigger, null)))
        {
            aTNotifyTrigger = TNotifyTriggerPeer.retrieveByPK(SimpleKey.keyFor(this.notifyTrigger));
        }
        return aTNotifyTrigger;
    }

    /**
     * Return the associated TNotifyTrigger object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TNotifyTrigger object
     * @throws TorqueException
     */
    public TNotifyTrigger getTNotifyTrigger(Connection connection)
        throws TorqueException
    {
        if (aTNotifyTrigger == null && (!ObjectUtils.equals(this.notifyTrigger, null)))
        {
            aTNotifyTrigger = TNotifyTriggerPeer.retrieveByPK(SimpleKey.keyFor(this.notifyTrigger), connection);
        }
        return aTNotifyTrigger;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTNotifyTriggerKey(ObjectKey key) throws TorqueException
    {

        setNotifyTrigger(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Field");
            fieldNames.add("ActionType");
            fieldNames.add("FieldType");
            fieldNames.add("NotifyTrigger");
            fieldNames.add("Originator");
            fieldNames.add("Manager");
            fieldNames.add("Responsible");
            fieldNames.add("Consultant");
            fieldNames.add("Informant");
            fieldNames.add("Observer");
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
        if (name.equals("Field"))
        {
            return getField();
        }
        if (name.equals("ActionType"))
        {
            return getActionType();
        }
        if (name.equals("FieldType"))
        {
            return getFieldType();
        }
        if (name.equals("NotifyTrigger"))
        {
            return getNotifyTrigger();
        }
        if (name.equals("Originator"))
        {
            return getOriginator();
        }
        if (name.equals("Manager"))
        {
            return getManager();
        }
        if (name.equals("Responsible"))
        {
            return getResponsible();
        }
        if (name.equals("Consultant"))
        {
            return getConsultant();
        }
        if (name.equals("Informant"))
        {
            return getInformant();
        }
        if (name.equals("Observer"))
        {
            return getObserver();
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
        if (name.equals("Field"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setField((Integer) value);
            return true;
        }
        if (name.equals("ActionType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setActionType((Integer) value);
            return true;
        }
        if (name.equals("FieldType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldType((Integer) value);
            return true;
        }
        if (name.equals("NotifyTrigger"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNotifyTrigger((Integer) value);
            return true;
        }
        if (name.equals("Originator"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOriginator((String) value);
            return true;
        }
        if (name.equals("Manager"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setManager((String) value);
            return true;
        }
        if (name.equals("Responsible"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setResponsible((String) value);
            return true;
        }
        if (name.equals("Consultant"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setConsultant((String) value);
            return true;
        }
        if (name.equals("Informant"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setInformant((String) value);
            return true;
        }
        if (name.equals("Observer"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setObserver((String) value);
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
        if (name.equals(TNotifyFieldPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TNotifyFieldPeer.FIELD))
        {
            return getField();
        }
        if (name.equals(TNotifyFieldPeer.ACTIONTYPE))
        {
            return getActionType();
        }
        if (name.equals(TNotifyFieldPeer.FIELDTYPE))
        {
            return getFieldType();
        }
        if (name.equals(TNotifyFieldPeer.NOTIFYTRIGGER))
        {
            return getNotifyTrigger();
        }
        if (name.equals(TNotifyFieldPeer.ORIGINATOR))
        {
            return getOriginator();
        }
        if (name.equals(TNotifyFieldPeer.MANAGER))
        {
            return getManager();
        }
        if (name.equals(TNotifyFieldPeer.RESPONSIBLE))
        {
            return getResponsible();
        }
        if (name.equals(TNotifyFieldPeer.CONSULTANT))
        {
            return getConsultant();
        }
        if (name.equals(TNotifyFieldPeer.INFORMANT))
        {
            return getInformant();
        }
        if (name.equals(TNotifyFieldPeer.OBSERVER))
        {
            return getObserver();
        }
        if (name.equals(TNotifyFieldPeer.TPUUID))
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
      if (TNotifyFieldPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TNotifyFieldPeer.FIELD.equals(name))
        {
            return setByName("Field", value);
        }
      if (TNotifyFieldPeer.ACTIONTYPE.equals(name))
        {
            return setByName("ActionType", value);
        }
      if (TNotifyFieldPeer.FIELDTYPE.equals(name))
        {
            return setByName("FieldType", value);
        }
      if (TNotifyFieldPeer.NOTIFYTRIGGER.equals(name))
        {
            return setByName("NotifyTrigger", value);
        }
      if (TNotifyFieldPeer.ORIGINATOR.equals(name))
        {
            return setByName("Originator", value);
        }
      if (TNotifyFieldPeer.MANAGER.equals(name))
        {
            return setByName("Manager", value);
        }
      if (TNotifyFieldPeer.RESPONSIBLE.equals(name))
        {
            return setByName("Responsible", value);
        }
      if (TNotifyFieldPeer.CONSULTANT.equals(name))
        {
            return setByName("Consultant", value);
        }
      if (TNotifyFieldPeer.INFORMANT.equals(name))
        {
            return setByName("Informant", value);
        }
      if (TNotifyFieldPeer.OBSERVER.equals(name))
        {
            return setByName("Observer", value);
        }
      if (TNotifyFieldPeer.TPUUID.equals(name))
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
            return getField();
        }
        if (pos == 2)
        {
            return getActionType();
        }
        if (pos == 3)
        {
            return getFieldType();
        }
        if (pos == 4)
        {
            return getNotifyTrigger();
        }
        if (pos == 5)
        {
            return getOriginator();
        }
        if (pos == 6)
        {
            return getManager();
        }
        if (pos == 7)
        {
            return getResponsible();
        }
        if (pos == 8)
        {
            return getConsultant();
        }
        if (pos == 9)
        {
            return getInformant();
        }
        if (pos == 10)
        {
            return getObserver();
        }
        if (pos == 11)
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
            return setByName("Field", value);
        }
    if (position == 2)
        {
            return setByName("ActionType", value);
        }
    if (position == 3)
        {
            return setByName("FieldType", value);
        }
    if (position == 4)
        {
            return setByName("NotifyTrigger", value);
        }
    if (position == 5)
        {
            return setByName("Originator", value);
        }
    if (position == 6)
        {
            return setByName("Manager", value);
        }
    if (position == 7)
        {
            return setByName("Responsible", value);
        }
    if (position == 8)
        {
            return setByName("Consultant", value);
        }
    if (position == 9)
        {
            return setByName("Informant", value);
        }
    if (position == 10)
        {
            return setByName("Observer", value);
        }
    if (position == 11)
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
        save(TNotifyFieldPeer.DATABASE_NAME);
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
                    TNotifyFieldPeer.doInsert((TNotifyField) this, con);
                    setNew(false);
                }
                else
                {
                    TNotifyFieldPeer.doUpdate((TNotifyField) this, con);
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
    public TNotifyField copy() throws TorqueException
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
    public TNotifyField copy(Connection con) throws TorqueException
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
    public TNotifyField copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TNotifyField(), deepcopy);
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
    public TNotifyField copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TNotifyField(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TNotifyField copyInto(TNotifyField copyObj) throws TorqueException
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
    protected TNotifyField copyInto(TNotifyField copyObj, Connection con) throws TorqueException
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
    protected TNotifyField copyInto(TNotifyField copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setField(field);
        copyObj.setActionType(actionType);
        copyObj.setFieldType(fieldType);
        copyObj.setNotifyTrigger(notifyTrigger);
        copyObj.setOriginator(originator);
        copyObj.setManager(manager);
        copyObj.setResponsible(responsible);
        copyObj.setConsultant(consultant);
        copyObj.setInformant(informant);
        copyObj.setObserver(observer);
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
    protected TNotifyField copyInto(TNotifyField copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setField(field);
        copyObj.setActionType(actionType);
        copyObj.setFieldType(fieldType);
        copyObj.setNotifyTrigger(notifyTrigger);
        copyObj.setOriginator(originator);
        copyObj.setManager(manager);
        copyObj.setResponsible(responsible);
        copyObj.setConsultant(consultant);
        copyObj.setInformant(informant);
        copyObj.setObserver(observer);
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
    public TNotifyFieldPeer getPeer()
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
        return TNotifyFieldPeer.getTableMap();
    }

  
    /**
     * Creates a TNotifyFieldBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TNotifyFieldBean with the contents of this object
     */
    public TNotifyFieldBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TNotifyFieldBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TNotifyFieldBean with the contents of this object
     */
    public TNotifyFieldBean getBean(IdentityMap createdBeans)
    {
        TNotifyFieldBean result = (TNotifyFieldBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TNotifyFieldBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setField(getField());
        result.setActionType(getActionType());
        result.setFieldType(getFieldType());
        result.setNotifyTrigger(getNotifyTrigger());
        result.setOriginator(getOriginator());
        result.setManager(getManager());
        result.setResponsible(getResponsible());
        result.setConsultant(getConsultant());
        result.setInformant(getInformant());
        result.setObserver(getObserver());
        result.setUuid(getUuid());





        if (aTNotifyTrigger != null)
        {
            TNotifyTriggerBean relatedBean = aTNotifyTrigger.getBean(createdBeans);
            result.setTNotifyTriggerBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TNotifyField with the contents
     * of a TNotifyFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TNotifyFieldBean which contents are used to create
     *        the resulting class
     * @return an instance of TNotifyField with the contents of bean
     */
    public static TNotifyField createTNotifyField(TNotifyFieldBean bean)
        throws TorqueException
    {
        return createTNotifyField(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TNotifyField with the contents
     * of a TNotifyFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TNotifyFieldBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TNotifyField with the contents of bean
     */

    public static TNotifyField createTNotifyField(TNotifyFieldBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TNotifyField result = (TNotifyField) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TNotifyField();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setField(bean.getField());
        result.setActionType(bean.getActionType());
        result.setFieldType(bean.getFieldType());
        result.setNotifyTrigger(bean.getNotifyTrigger());
        result.setOriginator(bean.getOriginator());
        result.setManager(bean.getManager());
        result.setResponsible(bean.getResponsible());
        result.setConsultant(bean.getConsultant());
        result.setInformant(bean.getInformant());
        result.setObserver(bean.getObserver());
        result.setUuid(bean.getUuid());





        {
            TNotifyTriggerBean relatedBean = bean.getTNotifyTriggerBean();
            if (relatedBean != null)
            {
                TNotifyTrigger relatedObject = TNotifyTrigger.createTNotifyTrigger(relatedBean, createdObjects);
                result.setTNotifyTrigger(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TNotifyField:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Field = ")
           .append(getField())
           .append("\n");
        str.append("ActionType = ")
           .append(getActionType())
           .append("\n");
        str.append("FieldType = ")
           .append(getFieldType())
           .append("\n");
        str.append("NotifyTrigger = ")
           .append(getNotifyTrigger())
           .append("\n");
        str.append("Originator = ")
           .append(getOriginator())
           .append("\n");
        str.append("Manager = ")
           .append(getManager())
           .append("\n");
        str.append("Responsible = ")
           .append(getResponsible())
           .append("\n");
        str.append("Consultant = ")
           .append(getConsultant())
           .append("\n");
        str.append("Informant = ")
           .append(getInformant())
           .append("\n");
        str.append("Observer = ")
           .append(getObserver())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
