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
import com.aurel.track.beans.TAttributeClassBean;

import com.aurel.track.beans.TAttributeTypeBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TAttributeClass
 */
public abstract class BaseTAttributeClass extends TpBaseObject
{
    /** The Peer class */
    private static final TAttributeClassPeer peer =
        new TAttributeClassPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the attributeClassName field */
    private String attributeClassName;

    /** The value for the attributeClassDescription field */
    private String attributeClassDescription;

    /** The value for the javaClassName field */
    private String javaClassName;

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
    public void setObjectID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }



        // update associated TAttributeType
        if (collTAttributeTypes != null)
        {
            for (int i = 0; i < collTAttributeTypes.size(); i++)
            {
                ((TAttributeType) collTAttributeTypes.get(i))
                        .setAttributeClass(v);
            }
        }
    }

    /**
     * Get the AttributeClassName
     *
     * @return String
     */
    public String getAttributeClassName()
    {
        return attributeClassName;
    }


    /**
     * Set the value of AttributeClassName
     *
     * @param v new value
     */
    public void setAttributeClassName(String v) 
    {

        if (!ObjectUtils.equals(this.attributeClassName, v))
        {
            this.attributeClassName = v;
            setModified(true);
        }


    }

    /**
     * Get the AttributeClassDescription
     *
     * @return String
     */
    public String getAttributeClassDescription()
    {
        return attributeClassDescription;
    }


    /**
     * Set the value of AttributeClassDescription
     *
     * @param v new value
     */
    public void setAttributeClassDescription(String v) 
    {

        if (!ObjectUtils.equals(this.attributeClassDescription, v))
        {
            this.attributeClassDescription = v;
            setModified(true);
        }


    }

    /**
     * Get the JavaClassName
     *
     * @return String
     */
    public String getJavaClassName()
    {
        return javaClassName;
    }


    /**
     * Set the value of JavaClassName
     *
     * @param v new value
     */
    public void setJavaClassName(String v) 
    {

        if (!ObjectUtils.equals(this.javaClassName, v))
        {
            this.javaClassName = v;
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

       


    /**
     * Collection to store aggregation of collTAttributeTypes
     */
    protected List<TAttributeType> collTAttributeTypes;

    /**
     * Temporary storage of collTAttributeTypes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAttributeTypes()
    {
        if (collTAttributeTypes == null)
        {
            collTAttributeTypes = new ArrayList<TAttributeType>();
        }
    }


    /**
     * Method called to associate a TAttributeType object to this object
     * through the TAttributeType foreign key attribute
     *
     * @param l TAttributeType
     * @throws TorqueException
     */
    public void addTAttributeType(TAttributeType l) throws TorqueException
    {
        getTAttributeTypes().add(l);
        l.setTAttributeClass((TAttributeClass) this);
    }

    /**
     * Method called to associate a TAttributeType object to this object
     * through the TAttributeType foreign key attribute using connection.
     *
     * @param l TAttributeType
     * @throws TorqueException
     */
    public void addTAttributeType(TAttributeType l, Connection con) throws TorqueException
    {
        getTAttributeTypes(con).add(l);
        l.setTAttributeClass((TAttributeClass) this);
    }

    /**
     * The criteria used to select the current contents of collTAttributeTypes
     */
    private Criteria lastTAttributeTypesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttributeTypes(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAttributeType> getTAttributeTypes()
        throws TorqueException
    {
        if (collTAttributeTypes == null)
        {
            collTAttributeTypes = getTAttributeTypes(new Criteria(10));
        }
        return collTAttributeTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttributeClass has previously
     * been saved, it will retrieve related TAttributeTypes from storage.
     * If this TAttributeClass is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAttributeType> getTAttributeTypes(Criteria criteria) throws TorqueException
    {
        if (collTAttributeTypes == null)
        {
            if (isNew())
            {
               collTAttributeTypes = new ArrayList<TAttributeType>();
            }
            else
            {
                criteria.add(TAttributeTypePeer.ATTRIBUTECLASS, getObjectID() );
                collTAttributeTypes = TAttributeTypePeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TAttributeTypePeer.ATTRIBUTECLASS, getObjectID());
                if (!lastTAttributeTypesCriteria.equals(criteria))
                {
                    collTAttributeTypes = TAttributeTypePeer.doSelect(criteria);
                }
            }
        }
        lastTAttributeTypesCriteria = criteria;

        return collTAttributeTypes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttributeTypes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttributeType> getTAttributeTypes(Connection con) throws TorqueException
    {
        if (collTAttributeTypes == null)
        {
            collTAttributeTypes = getTAttributeTypes(new Criteria(10), con);
        }
        return collTAttributeTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttributeClass has previously
     * been saved, it will retrieve related TAttributeTypes from storage.
     * If this TAttributeClass is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttributeType> getTAttributeTypes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAttributeTypes == null)
        {
            if (isNew())
            {
               collTAttributeTypes = new ArrayList<TAttributeType>();
            }
            else
            {
                 criteria.add(TAttributeTypePeer.ATTRIBUTECLASS, getObjectID());
                 collTAttributeTypes = TAttributeTypePeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TAttributeTypePeer.ATTRIBUTECLASS, getObjectID());
                 if (!lastTAttributeTypesCriteria.equals(criteria))
                 {
                     collTAttributeTypes = TAttributeTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAttributeTypesCriteria = criteria;

         return collTAttributeTypes;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttributeClass is new, it will return
     * an empty collection; or if this TAttributeClass has previously
     * been saved, it will retrieve related TAttributeTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttributeClass.
     */
    protected List<TAttributeType> getTAttributeTypesJoinTAttributeClass(Criteria criteria)
        throws TorqueException
    {
        if (collTAttributeTypes == null)
        {
            if (isNew())
            {
               collTAttributeTypes = new ArrayList<TAttributeType>();
            }
            else
            {
                criteria.add(TAttributeTypePeer.ATTRIBUTECLASS, getObjectID());
                collTAttributeTypes = TAttributeTypePeer.doSelectJoinTAttributeClass(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttributeTypePeer.ATTRIBUTECLASS, getObjectID());
            if (!lastTAttributeTypesCriteria.equals(criteria))
            {
                collTAttributeTypes = TAttributeTypePeer.doSelectJoinTAttributeClass(criteria);
            }
        }
        lastTAttributeTypesCriteria = criteria;

        return collTAttributeTypes;
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
            fieldNames.add("AttributeClassName");
            fieldNames.add("AttributeClassDescription");
            fieldNames.add("JavaClassName");
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
        if (name.equals("AttributeClassName"))
        {
            return getAttributeClassName();
        }
        if (name.equals("AttributeClassDescription"))
        {
            return getAttributeClassDescription();
        }
        if (name.equals("JavaClassName"))
        {
            return getJavaClassName();
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
        if (name.equals("AttributeClassName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAttributeClassName((String) value);
            return true;
        }
        if (name.equals("AttributeClassDescription"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAttributeClassDescription((String) value);
            return true;
        }
        if (name.equals("JavaClassName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setJavaClassName((String) value);
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
        if (name.equals(TAttributeClassPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TAttributeClassPeer.ATTRIBUTECLASSNAME))
        {
            return getAttributeClassName();
        }
        if (name.equals(TAttributeClassPeer.ATTRIBUTECLASSDESC))
        {
            return getAttributeClassDescription();
        }
        if (name.equals(TAttributeClassPeer.JAVACLASSNAME))
        {
            return getJavaClassName();
        }
        if (name.equals(TAttributeClassPeer.TPUUID))
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
      if (TAttributeClassPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TAttributeClassPeer.ATTRIBUTECLASSNAME.equals(name))
        {
            return setByName("AttributeClassName", value);
        }
      if (TAttributeClassPeer.ATTRIBUTECLASSDESC.equals(name))
        {
            return setByName("AttributeClassDescription", value);
        }
      if (TAttributeClassPeer.JAVACLASSNAME.equals(name))
        {
            return setByName("JavaClassName", value);
        }
      if (TAttributeClassPeer.TPUUID.equals(name))
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
            return getAttributeClassName();
        }
        if (pos == 2)
        {
            return getAttributeClassDescription();
        }
        if (pos == 3)
        {
            return getJavaClassName();
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
            return setByName("AttributeClassName", value);
        }
    if (position == 2)
        {
            return setByName("AttributeClassDescription", value);
        }
    if (position == 3)
        {
            return setByName("JavaClassName", value);
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
        save(TAttributeClassPeer.DATABASE_NAME);
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
                    TAttributeClassPeer.doInsert((TAttributeClass) this, con);
                    setNew(false);
                }
                else
                {
                    TAttributeClassPeer.doUpdate((TAttributeClass) this, con);
                }
            }


            if (collTAttributeTypes != null)
            {
                for (int i = 0; i < collTAttributeTypes.size(); i++)
                {
                    ((TAttributeType) collTAttributeTypes.get(i)).save(con);
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
        throws TorqueException
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
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
    public TAttributeClass copy() throws TorqueException
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
    public TAttributeClass copy(Connection con) throws TorqueException
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
    public TAttributeClass copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TAttributeClass(), deepcopy);
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
    public TAttributeClass copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TAttributeClass(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TAttributeClass copyInto(TAttributeClass copyObj) throws TorqueException
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
    protected TAttributeClass copyInto(TAttributeClass copyObj, Connection con) throws TorqueException
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
    protected TAttributeClass copyInto(TAttributeClass copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAttributeClassName(attributeClassName);
        copyObj.setAttributeClassDescription(attributeClassDescription);
        copyObj.setJavaClassName(javaClassName);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TAttributeType> vTAttributeTypes = getTAttributeTypes();
        if (vTAttributeTypes != null)
        {
            for (int i = 0; i < vTAttributeTypes.size(); i++)
            {
                TAttributeType obj =  vTAttributeTypes.get(i);
                copyObj.addTAttributeType(obj.copy());
            }
        }
        else
        {
            copyObj.collTAttributeTypes = null;
        }
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
    protected TAttributeClass copyInto(TAttributeClass copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAttributeClassName(attributeClassName);
        copyObj.setAttributeClassDescription(attributeClassDescription);
        copyObj.setJavaClassName(javaClassName);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TAttributeType> vTAttributeTypes = getTAttributeTypes(con);
        if (vTAttributeTypes != null)
        {
            for (int i = 0; i < vTAttributeTypes.size(); i++)
            {
                TAttributeType obj =  vTAttributeTypes.get(i);
                copyObj.addTAttributeType(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAttributeTypes = null;
        }
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TAttributeClassPeer getPeer()
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
        return TAttributeClassPeer.getTableMap();
    }

  
    /**
     * Creates a TAttributeClassBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TAttributeClassBean with the contents of this object
     */
    public TAttributeClassBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TAttributeClassBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TAttributeClassBean with the contents of this object
     */
    public TAttributeClassBean getBean(IdentityMap createdBeans)
    {
        TAttributeClassBean result = (TAttributeClassBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TAttributeClassBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setAttributeClassName(getAttributeClassName());
        result.setAttributeClassDescription(getAttributeClassDescription());
        result.setJavaClassName(getJavaClassName());
        result.setUuid(getUuid());



        if (collTAttributeTypes != null)
        {
            List<TAttributeTypeBean> relatedBeans = new ArrayList<TAttributeTypeBean>(collTAttributeTypes.size());
            for (Iterator<TAttributeType> collTAttributeTypesIt = collTAttributeTypes.iterator(); collTAttributeTypesIt.hasNext(); )
            {
                TAttributeType related = (TAttributeType) collTAttributeTypesIt.next();
                TAttributeTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAttributeTypeBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TAttributeClass with the contents
     * of a TAttributeClassBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TAttributeClassBean which contents are used to create
     *        the resulting class
     * @return an instance of TAttributeClass with the contents of bean
     */
    public static TAttributeClass createTAttributeClass(TAttributeClassBean bean)
        throws TorqueException
    {
        return createTAttributeClass(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TAttributeClass with the contents
     * of a TAttributeClassBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TAttributeClassBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TAttributeClass with the contents of bean
     */

    public static TAttributeClass createTAttributeClass(TAttributeClassBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TAttributeClass result = (TAttributeClass) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TAttributeClass();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setAttributeClassName(bean.getAttributeClassName());
        result.setAttributeClassDescription(bean.getAttributeClassDescription());
        result.setJavaClassName(bean.getJavaClassName());
        result.setUuid(bean.getUuid());



        {
            List<TAttributeTypeBean> relatedBeans = bean.getTAttributeTypeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAttributeTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAttributeTypeBean relatedBean =  relatedBeansIt.next();
                    TAttributeType related = TAttributeType.createTAttributeType(relatedBean, createdObjects);
                    result.addTAttributeTypeFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TAttributeType object to this object.
     * through the TAttributeType foreign key attribute
     *
     * @param toAdd TAttributeType
     */
    protected void addTAttributeTypeFromBean(TAttributeType toAdd)
    {
        initTAttributeTypes();
        collTAttributeTypes.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TAttributeClass:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("AttributeClassName = ")
           .append(getAttributeClassName())
           .append("\n");
        str.append("AttributeClassDescription = ")
           .append(getAttributeClassDescription())
           .append("\n");
        str.append("JavaClassName = ")
           .append(getJavaClassName())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
