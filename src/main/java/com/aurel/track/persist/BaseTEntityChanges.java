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



import com.aurel.track.persist.TClusterNode;
import com.aurel.track.persist.TClusterNodePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TEntityChangesBean;
import com.aurel.track.beans.TClusterNodeBean;



/**
 * This table holds the changed entities for updating the full text search index in each clusternode
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TEntityChanges
 */
public abstract class BaseTEntityChanges extends TpBaseObject
{
    /** The Peer class */
    private static final TEntityChangesPeer peer =
        new TEntityChangesPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the entityKey field */
    private Integer entityKey;

    /** The value for the entityType field */
    private Integer entityType;

    /** The value for the clusterNode field */
    private Integer clusterNode;

    /** The value for the list field */
    private Integer list;

    /** The value for the changeType field */
    private Integer changeType;


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
     * Get the EntityKey
     *
     * @return Integer
     */
    public Integer getEntityKey()
    {
        return entityKey;
    }


    /**
     * Set the value of EntityKey
     *
     * @param v new value
     */
    public void setEntityKey(Integer v) 
    {

        if (!ObjectUtils.equals(this.entityKey, v))
        {
            this.entityKey = v;
            setModified(true);
        }


    }

    /**
     * Get the EntityType
     *
     * @return Integer
     */
    public Integer getEntityType()
    {
        return entityType;
    }


    /**
     * Set the value of EntityType
     *
     * @param v new value
     */
    public void setEntityType(Integer v) 
    {

        if (!ObjectUtils.equals(this.entityType, v))
        {
            this.entityType = v;
            setModified(true);
        }


    }

    /**
     * Get the ClusterNode
     *
     * @return Integer
     */
    public Integer getClusterNode()
    {
        return clusterNode;
    }


    /**
     * Set the value of ClusterNode
     *
     * @param v new value
     */
    public void setClusterNode(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.clusterNode, v))
        {
            this.clusterNode = v;
            setModified(true);
        }


        if (aTClusterNode != null && !ObjectUtils.equals(aTClusterNode.getObjectID(), v))
        {
            aTClusterNode = null;
        }

    }

    /**
     * Get the List
     *
     * @return Integer
     */
    public Integer getList()
    {
        return list;
    }


    /**
     * Set the value of List
     *
     * @param v new value
     */
    public void setList(Integer v) 
    {

        if (!ObjectUtils.equals(this.list, v))
        {
            this.list = v;
            setModified(true);
        }


    }

    /**
     * Get the ChangeType
     *
     * @return Integer
     */
    public Integer getChangeType()
    {
        return changeType;
    }


    /**
     * Set the value of ChangeType
     *
     * @param v new value
     */
    public void setChangeType(Integer v) 
    {

        if (!ObjectUtils.equals(this.changeType, v))
        {
            this.changeType = v;
            setModified(true);
        }


    }

    



    private TClusterNode aTClusterNode;

    /**
     * Declares an association between this object and a TClusterNode object
     *
     * @param v TClusterNode
     * @throws TorqueException
     */
    public void setTClusterNode(TClusterNode v) throws TorqueException
    {
        if (v == null)
        {
            setClusterNode((Integer) null);
        }
        else
        {
            setClusterNode(v.getObjectID());
        }
        aTClusterNode = v;
    }


    /**
     * Returns the associated TClusterNode object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TClusterNode object
     * @throws TorqueException
     */
    public TClusterNode getTClusterNode()
        throws TorqueException
    {
        if (aTClusterNode == null && (!ObjectUtils.equals(this.clusterNode, null)))
        {
            aTClusterNode = TClusterNodePeer.retrieveByPK(SimpleKey.keyFor(this.clusterNode));
        }
        return aTClusterNode;
    }

    /**
     * Return the associated TClusterNode object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TClusterNode object
     * @throws TorqueException
     */
    public TClusterNode getTClusterNode(Connection connection)
        throws TorqueException
    {
        if (aTClusterNode == null && (!ObjectUtils.equals(this.clusterNode, null)))
        {
            aTClusterNode = TClusterNodePeer.retrieveByPK(SimpleKey.keyFor(this.clusterNode), connection);
        }
        return aTClusterNode;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTClusterNodeKey(ObjectKey key) throws TorqueException
    {

        setClusterNode(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("EntityKey");
            fieldNames.add("EntityType");
            fieldNames.add("ClusterNode");
            fieldNames.add("List");
            fieldNames.add("ChangeType");
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
        if (name.equals("EntityKey"))
        {
            return getEntityKey();
        }
        if (name.equals("EntityType"))
        {
            return getEntityType();
        }
        if (name.equals("ClusterNode"))
        {
            return getClusterNode();
        }
        if (name.equals("List"))
        {
            return getList();
        }
        if (name.equals("ChangeType"))
        {
            return getChangeType();
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
        if (name.equals("EntityKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEntityKey((Integer) value);
            return true;
        }
        if (name.equals("EntityType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEntityType((Integer) value);
            return true;
        }
        if (name.equals("ClusterNode"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setClusterNode((Integer) value);
            return true;
        }
        if (name.equals("List"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setList((Integer) value);
            return true;
        }
        if (name.equals("ChangeType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setChangeType((Integer) value);
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
        if (name.equals(TEntityChangesPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TEntityChangesPeer.ENTITYKEY))
        {
            return getEntityKey();
        }
        if (name.equals(TEntityChangesPeer.ENTITYTYPE))
        {
            return getEntityType();
        }
        if (name.equals(TEntityChangesPeer.CLUSTERNODE))
        {
            return getClusterNode();
        }
        if (name.equals(TEntityChangesPeer.LIST))
        {
            return getList();
        }
        if (name.equals(TEntityChangesPeer.CHANGETYPE))
        {
            return getChangeType();
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
      if (TEntityChangesPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TEntityChangesPeer.ENTITYKEY.equals(name))
        {
            return setByName("EntityKey", value);
        }
      if (TEntityChangesPeer.ENTITYTYPE.equals(name))
        {
            return setByName("EntityType", value);
        }
      if (TEntityChangesPeer.CLUSTERNODE.equals(name))
        {
            return setByName("ClusterNode", value);
        }
      if (TEntityChangesPeer.LIST.equals(name))
        {
            return setByName("List", value);
        }
      if (TEntityChangesPeer.CHANGETYPE.equals(name))
        {
            return setByName("ChangeType", value);
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
            return getEntityKey();
        }
        if (pos == 2)
        {
            return getEntityType();
        }
        if (pos == 3)
        {
            return getClusterNode();
        }
        if (pos == 4)
        {
            return getList();
        }
        if (pos == 5)
        {
            return getChangeType();
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
            return setByName("EntityKey", value);
        }
    if (position == 2)
        {
            return setByName("EntityType", value);
        }
    if (position == 3)
        {
            return setByName("ClusterNode", value);
        }
    if (position == 4)
        {
            return setByName("List", value);
        }
    if (position == 5)
        {
            return setByName("ChangeType", value);
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
        save(TEntityChangesPeer.DATABASE_NAME);
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
                    TEntityChangesPeer.doInsert((TEntityChanges) this, con);
                    setNew(false);
                }
                else
                {
                    TEntityChangesPeer.doUpdate((TEntityChanges) this, con);
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
    public TEntityChanges copy() throws TorqueException
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
    public TEntityChanges copy(Connection con) throws TorqueException
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
    public TEntityChanges copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TEntityChanges(), deepcopy);
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
    public TEntityChanges copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TEntityChanges(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TEntityChanges copyInto(TEntityChanges copyObj) throws TorqueException
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
    protected TEntityChanges copyInto(TEntityChanges copyObj, Connection con) throws TorqueException
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
    protected TEntityChanges copyInto(TEntityChanges copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setEntityKey(entityKey);
        copyObj.setEntityType(entityType);
        copyObj.setClusterNode(clusterNode);
        copyObj.setList(list);
        copyObj.setChangeType(changeType);

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
    protected TEntityChanges copyInto(TEntityChanges copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setEntityKey(entityKey);
        copyObj.setEntityType(entityType);
        copyObj.setClusterNode(clusterNode);
        copyObj.setList(list);
        copyObj.setChangeType(changeType);

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
    public TEntityChangesPeer getPeer()
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
        return TEntityChangesPeer.getTableMap();
    }

  
    /**
     * Creates a TEntityChangesBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TEntityChangesBean with the contents of this object
     */
    public TEntityChangesBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TEntityChangesBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TEntityChangesBean with the contents of this object
     */
    public TEntityChangesBean getBean(IdentityMap createdBeans)
    {
        TEntityChangesBean result = (TEntityChangesBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TEntityChangesBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setEntityKey(getEntityKey());
        result.setEntityType(getEntityType());
        result.setClusterNode(getClusterNode());
        result.setList(getList());
        result.setChangeType(getChangeType());





        if (aTClusterNode != null)
        {
            TClusterNodeBean relatedBean = aTClusterNode.getBean(createdBeans);
            result.setTClusterNodeBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TEntityChanges with the contents
     * of a TEntityChangesBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TEntityChangesBean which contents are used to create
     *        the resulting class
     * @return an instance of TEntityChanges with the contents of bean
     */
    public static TEntityChanges createTEntityChanges(TEntityChangesBean bean)
        throws TorqueException
    {
        return createTEntityChanges(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TEntityChanges with the contents
     * of a TEntityChangesBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TEntityChangesBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TEntityChanges with the contents of bean
     */

    public static TEntityChanges createTEntityChanges(TEntityChangesBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TEntityChanges result = (TEntityChanges) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TEntityChanges();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setEntityKey(bean.getEntityKey());
        result.setEntityType(bean.getEntityType());
        result.setClusterNode(bean.getClusterNode());
        result.setList(bean.getList());
        result.setChangeType(bean.getChangeType());





        {
            TClusterNodeBean relatedBean = bean.getTClusterNodeBean();
            if (relatedBean != null)
            {
                TClusterNode relatedObject = TClusterNode.createTClusterNode(relatedBean, createdObjects);
                result.setTClusterNode(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TEntityChanges:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("EntityKey = ")
           .append(getEntityKey())
           .append("\n");
        str.append("EntityType = ")
           .append(getEntityType())
           .append("\n");
        str.append("ClusterNode = ")
           .append(getClusterNode())
           .append("\n");
        str.append("List = ")
           .append(getList())
           .append("\n");
        str.append("ChangeType = ")
           .append(getChangeType())
           .append("\n");
        return(str.toString());
    }
}
