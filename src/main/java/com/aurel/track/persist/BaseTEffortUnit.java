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
import com.aurel.track.beans.TEffortUnitBean;

import com.aurel.track.beans.TEffortTypeBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TEffortUnit
 */
public abstract class BaseTEffortUnit extends TpBaseObject
{
    /** The Peer class */
    private static final TEffortUnitPeer peer =
        new TEffortUnitPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the description field */
    private String description;

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



        // update associated TEffortType
        if (collTEffortTypes != null)
        {
            for (int i = 0; i < collTEffortTypes.size(); i++)
            {
                ((TEffortType) collTEffortTypes.get(i))
                        .setEffortUnit(v);
            }
        }
    }

    /**
     * Get the Label
     *
     * @return String
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set the value of Label
     *
     * @param v new value
     */
    public void setLabel(String v) 
    {

        if (!ObjectUtils.equals(this.label, v))
        {
            this.label = v;
            setModified(true);
        }


    }

    /**
     * Get the Description
     *
     * @return String
     */
    public String getDescription()
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

        if (!ObjectUtils.equals(this.description, v))
        {
            this.description = v;
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
     * Collection to store aggregation of collTEffortTypes
     */
    protected List<TEffortType> collTEffortTypes;

    /**
     * Temporary storage of collTEffortTypes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTEffortTypes()
    {
        if (collTEffortTypes == null)
        {
            collTEffortTypes = new ArrayList<TEffortType>();
        }
    }


    /**
     * Method called to associate a TEffortType object to this object
     * through the TEffortType foreign key attribute
     *
     * @param l TEffortType
     * @throws TorqueException
     */
    public void addTEffortType(TEffortType l) throws TorqueException
    {
        getTEffortTypes().add(l);
        l.setTEffortUnit((TEffortUnit) this);
    }

    /**
     * Method called to associate a TEffortType object to this object
     * through the TEffortType foreign key attribute using connection.
     *
     * @param l TEffortType
     * @throws TorqueException
     */
    public void addTEffortType(TEffortType l, Connection con) throws TorqueException
    {
        getTEffortTypes(con).add(l);
        l.setTEffortUnit((TEffortUnit) this);
    }

    /**
     * The criteria used to select the current contents of collTEffortTypes
     */
    private Criteria lastTEffortTypesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEffortTypes(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TEffortType> getTEffortTypes()
        throws TorqueException
    {
        if (collTEffortTypes == null)
        {
            collTEffortTypes = getTEffortTypes(new Criteria(10));
        }
        return collTEffortTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEffortUnit has previously
     * been saved, it will retrieve related TEffortTypes from storage.
     * If this TEffortUnit is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TEffortType> getTEffortTypes(Criteria criteria) throws TorqueException
    {
        if (collTEffortTypes == null)
        {
            if (isNew())
            {
               collTEffortTypes = new ArrayList<TEffortType>();
            }
            else
            {
                criteria.add(TEffortTypePeer.EFFORTUNIT, getObjectID() );
                collTEffortTypes = TEffortTypePeer.doSelect(criteria);
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
                criteria.add(TEffortTypePeer.EFFORTUNIT, getObjectID());
                if (!lastTEffortTypesCriteria.equals(criteria))
                {
                    collTEffortTypes = TEffortTypePeer.doSelect(criteria);
                }
            }
        }
        lastTEffortTypesCriteria = criteria;

        return collTEffortTypes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEffortTypes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEffortType> getTEffortTypes(Connection con) throws TorqueException
    {
        if (collTEffortTypes == null)
        {
            collTEffortTypes = getTEffortTypes(new Criteria(10), con);
        }
        return collTEffortTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEffortUnit has previously
     * been saved, it will retrieve related TEffortTypes from storage.
     * If this TEffortUnit is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEffortType> getTEffortTypes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTEffortTypes == null)
        {
            if (isNew())
            {
               collTEffortTypes = new ArrayList<TEffortType>();
            }
            else
            {
                 criteria.add(TEffortTypePeer.EFFORTUNIT, getObjectID());
                 collTEffortTypes = TEffortTypePeer.doSelect(criteria, con);
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
                 criteria.add(TEffortTypePeer.EFFORTUNIT, getObjectID());
                 if (!lastTEffortTypesCriteria.equals(criteria))
                 {
                     collTEffortTypes = TEffortTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTEffortTypesCriteria = criteria;

         return collTEffortTypes;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEffortUnit is new, it will return
     * an empty collection; or if this TEffortUnit has previously
     * been saved, it will retrieve related TEffortTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TEffortUnit.
     */
    protected List<TEffortType> getTEffortTypesJoinTEffortUnit(Criteria criteria)
        throws TorqueException
    {
        if (collTEffortTypes == null)
        {
            if (isNew())
            {
               collTEffortTypes = new ArrayList<TEffortType>();
            }
            else
            {
                criteria.add(TEffortTypePeer.EFFORTUNIT, getObjectID());
                collTEffortTypes = TEffortTypePeer.doSelectJoinTEffortUnit(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEffortTypePeer.EFFORTUNIT, getObjectID());
            if (!lastTEffortTypesCriteria.equals(criteria))
            {
                collTEffortTypes = TEffortTypePeer.doSelectJoinTEffortUnit(criteria);
            }
        }
        lastTEffortTypesCriteria = criteria;

        return collTEffortTypes;
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
            fieldNames.add("Label");
            fieldNames.add("Description");
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
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("Description"))
        {
            return getDescription();
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
        if (name.equals("Label"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLabel((String) value);
            return true;
        }
        if (name.equals("Description"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDescription((String) value);
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
        if (name.equals(TEffortUnitPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TEffortUnitPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TEffortUnitPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TEffortUnitPeer.TPUUID))
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
      if (TEffortUnitPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TEffortUnitPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TEffortUnitPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TEffortUnitPeer.TPUUID.equals(name))
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
            return getLabel();
        }
        if (pos == 2)
        {
            return getDescription();
        }
        if (pos == 3)
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
            return setByName("Label", value);
        }
    if (position == 2)
        {
            return setByName("Description", value);
        }
    if (position == 3)
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
        save(TEffortUnitPeer.DATABASE_NAME);
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
                    TEffortUnitPeer.doInsert((TEffortUnit) this, con);
                    setNew(false);
                }
                else
                {
                    TEffortUnitPeer.doUpdate((TEffortUnit) this, con);
                }
            }


            if (collTEffortTypes != null)
            {
                for (int i = 0; i < collTEffortTypes.size(); i++)
                {
                    ((TEffortType) collTEffortTypes.get(i)).save(con);
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
    public TEffortUnit copy() throws TorqueException
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
    public TEffortUnit copy(Connection con) throws TorqueException
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
    public TEffortUnit copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TEffortUnit(), deepcopy);
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
    public TEffortUnit copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TEffortUnit(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TEffortUnit copyInto(TEffortUnit copyObj) throws TorqueException
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
    protected TEffortUnit copyInto(TEffortUnit copyObj, Connection con) throws TorqueException
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
    protected TEffortUnit copyInto(TEffortUnit copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setDescription(description);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TEffortType> vTEffortTypes = getTEffortTypes();
        if (vTEffortTypes != null)
        {
            for (int i = 0; i < vTEffortTypes.size(); i++)
            {
                TEffortType obj =  vTEffortTypes.get(i);
                copyObj.addTEffortType(obj.copy());
            }
        }
        else
        {
            copyObj.collTEffortTypes = null;
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
    protected TEffortUnit copyInto(TEffortUnit copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setDescription(description);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TEffortType> vTEffortTypes = getTEffortTypes(con);
        if (vTEffortTypes != null)
        {
            for (int i = 0; i < vTEffortTypes.size(); i++)
            {
                TEffortType obj =  vTEffortTypes.get(i);
                copyObj.addTEffortType(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTEffortTypes = null;
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
    public TEffortUnitPeer getPeer()
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
        return TEffortUnitPeer.getTableMap();
    }

  
    /**
     * Creates a TEffortUnitBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TEffortUnitBean with the contents of this object
     */
    public TEffortUnitBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TEffortUnitBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TEffortUnitBean with the contents of this object
     */
    public TEffortUnitBean getBean(IdentityMap createdBeans)
    {
        TEffortUnitBean result = (TEffortUnitBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TEffortUnitBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setDescription(getDescription());
        result.setUuid(getUuid());



        if (collTEffortTypes != null)
        {
            List<TEffortTypeBean> relatedBeans = new ArrayList<TEffortTypeBean>(collTEffortTypes.size());
            for (Iterator<TEffortType> collTEffortTypesIt = collTEffortTypes.iterator(); collTEffortTypesIt.hasNext(); )
            {
                TEffortType related = (TEffortType) collTEffortTypesIt.next();
                TEffortTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTEffortTypeBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TEffortUnit with the contents
     * of a TEffortUnitBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TEffortUnitBean which contents are used to create
     *        the resulting class
     * @return an instance of TEffortUnit with the contents of bean
     */
    public static TEffortUnit createTEffortUnit(TEffortUnitBean bean)
        throws TorqueException
    {
        return createTEffortUnit(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TEffortUnit with the contents
     * of a TEffortUnitBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TEffortUnitBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TEffortUnit with the contents of bean
     */

    public static TEffortUnit createTEffortUnit(TEffortUnitBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TEffortUnit result = (TEffortUnit) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TEffortUnit();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setDescription(bean.getDescription());
        result.setUuid(bean.getUuid());



        {
            List<TEffortTypeBean> relatedBeans = bean.getTEffortTypeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TEffortTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TEffortTypeBean relatedBean =  relatedBeansIt.next();
                    TEffortType related = TEffortType.createTEffortType(relatedBean, createdObjects);
                    result.addTEffortTypeFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TEffortType object to this object.
     * through the TEffortType foreign key attribute
     *
     * @param toAdd TEffortType
     */
    protected void addTEffortTypeFromBean(TEffortType toAdd)
    {
        initTEffortTypes();
        collTEffortTypes.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TEffortUnit:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
