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



import com.aurel.track.persist.TOutlineTemplate;
import com.aurel.track.persist.TOutlineTemplatePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TOutlineTemplateDefBean;
import com.aurel.track.beans.TOutlineTemplateBean;



/**
 * defines the parts of an outline code
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TOutlineTemplateDef
 */
public abstract class BaseTOutlineTemplateDef extends TpBaseObject
{
    /** The Peer class */
    private static final TOutlineTemplateDefPeer peer =
        new TOutlineTemplateDefPeer();


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
     * Get the LevelNo
     *
     * @return Integer
     */
    public Integer getLevelNo()
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

        if (!ObjectUtils.equals(this.levelNo, v))
        {
            this.levelNo = v;
            setModified(true);
        }


    }

    /**
     * Get the SequenceType
     *
     * @return Integer
     */
    public Integer getSequenceType()
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

        if (!ObjectUtils.equals(this.sequenceType, v))
        {
            this.sequenceType = v;
            setModified(true);
        }


    }

    /**
     * Get the ListID
     *
     * @return Integer
     */
    public Integer getListID()
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

        if (!ObjectUtils.equals(this.listID, v))
        {
            this.listID = v;
            setModified(true);
        }


    }

    /**
     * Get the SequenceLength
     *
     * @return Integer
     */
    public Integer getSequenceLength()
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

        if (!ObjectUtils.equals(this.sequenceLength, v))
        {
            this.sequenceLength = v;
            setModified(true);
        }


    }

    /**
     * Get the SeparatorChar
     *
     * @return String
     */
    public String getSeparatorChar()
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

        if (!ObjectUtils.equals(this.separatorChar, v))
        {
            this.separatorChar = v;
            setModified(true);
        }


    }

    /**
     * Get the OutlineTemplate
     *
     * @return Integer
     */
    public Integer getOutlineTemplate()
    {
        return outlineTemplate;
    }


    /**
     * Set the value of OutlineTemplate
     *
     * @param v new value
     */
    public void setOutlineTemplate(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.outlineTemplate, v))
        {
            this.outlineTemplate = v;
            setModified(true);
        }


        if (aTOutlineTemplate != null && !ObjectUtils.equals(aTOutlineTemplate.getObjectID(), v))
        {
            aTOutlineTemplate = null;
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

    



    private TOutlineTemplate aTOutlineTemplate;

    /**
     * Declares an association between this object and a TOutlineTemplate object
     *
     * @param v TOutlineTemplate
     * @throws TorqueException
     */
    public void setTOutlineTemplate(TOutlineTemplate v) throws TorqueException
    {
        if (v == null)
        {
            setOutlineTemplate((Integer) null);
        }
        else
        {
            setOutlineTemplate(v.getObjectID());
        }
        aTOutlineTemplate = v;
    }


    /**
     * Returns the associated TOutlineTemplate object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TOutlineTemplate object
     * @throws TorqueException
     */
    public TOutlineTemplate getTOutlineTemplate()
        throws TorqueException
    {
        if (aTOutlineTemplate == null && (!ObjectUtils.equals(this.outlineTemplate, null)))
        {
            aTOutlineTemplate = TOutlineTemplatePeer.retrieveByPK(SimpleKey.keyFor(this.outlineTemplate));
        }
        return aTOutlineTemplate;
    }

    /**
     * Return the associated TOutlineTemplate object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TOutlineTemplate object
     * @throws TorqueException
     */
    public TOutlineTemplate getTOutlineTemplate(Connection connection)
        throws TorqueException
    {
        if (aTOutlineTemplate == null && (!ObjectUtils.equals(this.outlineTemplate, null)))
        {
            aTOutlineTemplate = TOutlineTemplatePeer.retrieveByPK(SimpleKey.keyFor(this.outlineTemplate), connection);
        }
        return aTOutlineTemplate;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTOutlineTemplateKey(ObjectKey key) throws TorqueException
    {

        setOutlineTemplate(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("LevelNo");
            fieldNames.add("SequenceType");
            fieldNames.add("ListID");
            fieldNames.add("SequenceLength");
            fieldNames.add("SeparatorChar");
            fieldNames.add("OutlineTemplate");
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
        if (name.equals("LevelNo"))
        {
            return getLevelNo();
        }
        if (name.equals("SequenceType"))
        {
            return getSequenceType();
        }
        if (name.equals("ListID"))
        {
            return getListID();
        }
        if (name.equals("SequenceLength"))
        {
            return getSequenceLength();
        }
        if (name.equals("SeparatorChar"))
        {
            return getSeparatorChar();
        }
        if (name.equals("OutlineTemplate"))
        {
            return getOutlineTemplate();
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
        if (name.equals("LevelNo"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLevelNo((Integer) value);
            return true;
        }
        if (name.equals("SequenceType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSequenceType((Integer) value);
            return true;
        }
        if (name.equals("ListID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setListID((Integer) value);
            return true;
        }
        if (name.equals("SequenceLength"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSequenceLength((Integer) value);
            return true;
        }
        if (name.equals("SeparatorChar"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSeparatorChar((String) value);
            return true;
        }
        if (name.equals("OutlineTemplate"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOutlineTemplate((Integer) value);
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
        if (name.equals(TOutlineTemplateDefPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TOutlineTemplateDefPeer.LEVELNO))
        {
            return getLevelNo();
        }
        if (name.equals(TOutlineTemplateDefPeer.SEQUENCETYPE))
        {
            return getSequenceType();
        }
        if (name.equals(TOutlineTemplateDefPeer.LISTID))
        {
            return getListID();
        }
        if (name.equals(TOutlineTemplateDefPeer.SEQUENCELENGTH))
        {
            return getSequenceLength();
        }
        if (name.equals(TOutlineTemplateDefPeer.SEPARATORCHAR))
        {
            return getSeparatorChar();
        }
        if (name.equals(TOutlineTemplateDefPeer.OUTLINETEMPLATE))
        {
            return getOutlineTemplate();
        }
        if (name.equals(TOutlineTemplateDefPeer.TPUUID))
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
      if (TOutlineTemplateDefPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TOutlineTemplateDefPeer.LEVELNO.equals(name))
        {
            return setByName("LevelNo", value);
        }
      if (TOutlineTemplateDefPeer.SEQUENCETYPE.equals(name))
        {
            return setByName("SequenceType", value);
        }
      if (TOutlineTemplateDefPeer.LISTID.equals(name))
        {
            return setByName("ListID", value);
        }
      if (TOutlineTemplateDefPeer.SEQUENCELENGTH.equals(name))
        {
            return setByName("SequenceLength", value);
        }
      if (TOutlineTemplateDefPeer.SEPARATORCHAR.equals(name))
        {
            return setByName("SeparatorChar", value);
        }
      if (TOutlineTemplateDefPeer.OUTLINETEMPLATE.equals(name))
        {
            return setByName("OutlineTemplate", value);
        }
      if (TOutlineTemplateDefPeer.TPUUID.equals(name))
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
            return getLevelNo();
        }
        if (pos == 2)
        {
            return getSequenceType();
        }
        if (pos == 3)
        {
            return getListID();
        }
        if (pos == 4)
        {
            return getSequenceLength();
        }
        if (pos == 5)
        {
            return getSeparatorChar();
        }
        if (pos == 6)
        {
            return getOutlineTemplate();
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
            return setByName("LevelNo", value);
        }
    if (position == 2)
        {
            return setByName("SequenceType", value);
        }
    if (position == 3)
        {
            return setByName("ListID", value);
        }
    if (position == 4)
        {
            return setByName("SequenceLength", value);
        }
    if (position == 5)
        {
            return setByName("SeparatorChar", value);
        }
    if (position == 6)
        {
            return setByName("OutlineTemplate", value);
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
        save(TOutlineTemplateDefPeer.DATABASE_NAME);
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
                    TOutlineTemplateDefPeer.doInsert((TOutlineTemplateDef) this, con);
                    setNew(false);
                }
                else
                {
                    TOutlineTemplateDefPeer.doUpdate((TOutlineTemplateDef) this, con);
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
    public TOutlineTemplateDef copy() throws TorqueException
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
    public TOutlineTemplateDef copy(Connection con) throws TorqueException
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
    public TOutlineTemplateDef copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TOutlineTemplateDef(), deepcopy);
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
    public TOutlineTemplateDef copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TOutlineTemplateDef(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TOutlineTemplateDef copyInto(TOutlineTemplateDef copyObj) throws TorqueException
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
    protected TOutlineTemplateDef copyInto(TOutlineTemplateDef copyObj, Connection con) throws TorqueException
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
    protected TOutlineTemplateDef copyInto(TOutlineTemplateDef copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLevelNo(levelNo);
        copyObj.setSequenceType(sequenceType);
        copyObj.setListID(listID);
        copyObj.setSequenceLength(sequenceLength);
        copyObj.setSeparatorChar(separatorChar);
        copyObj.setOutlineTemplate(outlineTemplate);
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
    protected TOutlineTemplateDef copyInto(TOutlineTemplateDef copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLevelNo(levelNo);
        copyObj.setSequenceType(sequenceType);
        copyObj.setListID(listID);
        copyObj.setSequenceLength(sequenceLength);
        copyObj.setSeparatorChar(separatorChar);
        copyObj.setOutlineTemplate(outlineTemplate);
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
    public TOutlineTemplateDefPeer getPeer()
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
        return TOutlineTemplateDefPeer.getTableMap();
    }

  
    /**
     * Creates a TOutlineTemplateDefBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TOutlineTemplateDefBean with the contents of this object
     */
    public TOutlineTemplateDefBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TOutlineTemplateDefBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TOutlineTemplateDefBean with the contents of this object
     */
    public TOutlineTemplateDefBean getBean(IdentityMap createdBeans)
    {
        TOutlineTemplateDefBean result = (TOutlineTemplateDefBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TOutlineTemplateDefBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLevelNo(getLevelNo());
        result.setSequenceType(getSequenceType());
        result.setListID(getListID());
        result.setSequenceLength(getSequenceLength());
        result.setSeparatorChar(getSeparatorChar());
        result.setOutlineTemplate(getOutlineTemplate());
        result.setUuid(getUuid());





        if (aTOutlineTemplate != null)
        {
            TOutlineTemplateBean relatedBean = aTOutlineTemplate.getBean(createdBeans);
            result.setTOutlineTemplateBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TOutlineTemplateDef with the contents
     * of a TOutlineTemplateDefBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TOutlineTemplateDefBean which contents are used to create
     *        the resulting class
     * @return an instance of TOutlineTemplateDef with the contents of bean
     */
    public static TOutlineTemplateDef createTOutlineTemplateDef(TOutlineTemplateDefBean bean)
        throws TorqueException
    {
        return createTOutlineTemplateDef(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TOutlineTemplateDef with the contents
     * of a TOutlineTemplateDefBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TOutlineTemplateDefBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TOutlineTemplateDef with the contents of bean
     */

    public static TOutlineTemplateDef createTOutlineTemplateDef(TOutlineTemplateDefBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TOutlineTemplateDef result = (TOutlineTemplateDef) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TOutlineTemplateDef();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLevelNo(bean.getLevelNo());
        result.setSequenceType(bean.getSequenceType());
        result.setListID(bean.getListID());
        result.setSequenceLength(bean.getSequenceLength());
        result.setSeparatorChar(bean.getSeparatorChar());
        result.setOutlineTemplate(bean.getOutlineTemplate());
        result.setUuid(bean.getUuid());





        {
            TOutlineTemplateBean relatedBean = bean.getTOutlineTemplateBean();
            if (relatedBean != null)
            {
                TOutlineTemplate relatedObject = TOutlineTemplate.createTOutlineTemplate(relatedBean, createdObjects);
                result.setTOutlineTemplate(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TOutlineTemplateDef:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("LevelNo = ")
           .append(getLevelNo())
           .append("\n");
        str.append("SequenceType = ")
           .append(getSequenceType())
           .append("\n");
        str.append("ListID = ")
           .append(getListID())
           .append("\n");
        str.append("SequenceLength = ")
           .append(getSequenceLength())
           .append("\n");
        str.append("SeparatorChar = ")
           .append(getSeparatorChar())
           .append("\n");
        str.append("OutlineTemplate = ")
           .append(getOutlineTemplate())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
