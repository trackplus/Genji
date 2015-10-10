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



import com.aurel.track.persist.TWorkflowDef;
import com.aurel.track.persist.TWorkflowDefPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TWorkflowCommentBean;
import com.aurel.track.beans.TWorkflowDefBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TWorkflowComment
 */
public abstract class BaseTWorkflowComment extends TpBaseObject
{
    /** The Peer class */
    private static final TWorkflowCommentPeer peer =
        new TWorkflowCommentPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the description field */
    private String description;

    /** The value for the workflow field */
    private Integer workflow;

    /** The value for the nodeX field */
    private Integer nodeX;

    /** The value for the nodeY field */
    private Integer nodeY;

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
     * Get the Workflow
     *
     * @return Integer
     */
    public Integer getWorkflow()
    {
        return workflow;
    }


    /**
     * Set the value of Workflow
     *
     * @param v new value
     */
    public void setWorkflow(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workflow, v))
        {
            this.workflow = v;
            setModified(true);
        }


        if (aTWorkflowDef != null && !ObjectUtils.equals(aTWorkflowDef.getObjectID(), v))
        {
            aTWorkflowDef = null;
        }

    }

    /**
     * Get the NodeX
     *
     * @return Integer
     */
    public Integer getNodeX()
    {
        return nodeX;
    }


    /**
     * Set the value of NodeX
     *
     * @param v new value
     */
    public void setNodeX(Integer v) 
    {

        if (!ObjectUtils.equals(this.nodeX, v))
        {
            this.nodeX = v;
            setModified(true);
        }


    }

    /**
     * Get the NodeY
     *
     * @return Integer
     */
    public Integer getNodeY()
    {
        return nodeY;
    }


    /**
     * Set the value of NodeY
     *
     * @param v new value
     */
    public void setNodeY(Integer v) 
    {

        if (!ObjectUtils.equals(this.nodeY, v))
        {
            this.nodeY = v;
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

    



    private TWorkflowDef aTWorkflowDef;

    /**
     * Declares an association between this object and a TWorkflowDef object
     *
     * @param v TWorkflowDef
     * @throws TorqueException
     */
    public void setTWorkflowDef(TWorkflowDef v) throws TorqueException
    {
        if (v == null)
        {
            setWorkflow((Integer) null);
        }
        else
        {
            setWorkflow(v.getObjectID());
        }
        aTWorkflowDef = v;
    }


    /**
     * Returns the associated TWorkflowDef object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowDef object
     * @throws TorqueException
     */
    public TWorkflowDef getTWorkflowDef()
        throws TorqueException
    {
        if (aTWorkflowDef == null && (!ObjectUtils.equals(this.workflow, null)))
        {
            aTWorkflowDef = TWorkflowDefPeer.retrieveByPK(SimpleKey.keyFor(this.workflow));
        }
        return aTWorkflowDef;
    }

    /**
     * Return the associated TWorkflowDef object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowDef object
     * @throws TorqueException
     */
    public TWorkflowDef getTWorkflowDef(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowDef == null && (!ObjectUtils.equals(this.workflow, null)))
        {
            aTWorkflowDef = TWorkflowDefPeer.retrieveByPK(SimpleKey.keyFor(this.workflow), connection);
        }
        return aTWorkflowDef;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowDefKey(ObjectKey key) throws TorqueException
    {

        setWorkflow(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Description");
            fieldNames.add("Workflow");
            fieldNames.add("NodeX");
            fieldNames.add("NodeY");
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
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("Workflow"))
        {
            return getWorkflow();
        }
        if (name.equals("NodeX"))
        {
            return getNodeX();
        }
        if (name.equals("NodeY"))
        {
            return getNodeY();
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
        if (name.equals("Workflow"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkflow((Integer) value);
            return true;
        }
        if (name.equals("NodeX"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNodeX((Integer) value);
            return true;
        }
        if (name.equals("NodeY"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNodeY((Integer) value);
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
        if (name.equals(TWorkflowCommentPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TWorkflowCommentPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TWorkflowCommentPeer.WORKFLOW))
        {
            return getWorkflow();
        }
        if (name.equals(TWorkflowCommentPeer.NODEX))
        {
            return getNodeX();
        }
        if (name.equals(TWorkflowCommentPeer.NODEY))
        {
            return getNodeY();
        }
        if (name.equals(TWorkflowCommentPeer.TPUUID))
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
      if (TWorkflowCommentPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TWorkflowCommentPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TWorkflowCommentPeer.WORKFLOW.equals(name))
        {
            return setByName("Workflow", value);
        }
      if (TWorkflowCommentPeer.NODEX.equals(name))
        {
            return setByName("NodeX", value);
        }
      if (TWorkflowCommentPeer.NODEY.equals(name))
        {
            return setByName("NodeY", value);
        }
      if (TWorkflowCommentPeer.TPUUID.equals(name))
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
            return getDescription();
        }
        if (pos == 2)
        {
            return getWorkflow();
        }
        if (pos == 3)
        {
            return getNodeX();
        }
        if (pos == 4)
        {
            return getNodeY();
        }
        if (pos == 5)
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
            return setByName("Description", value);
        }
    if (position == 2)
        {
            return setByName("Workflow", value);
        }
    if (position == 3)
        {
            return setByName("NodeX", value);
        }
    if (position == 4)
        {
            return setByName("NodeY", value);
        }
    if (position == 5)
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
        save(TWorkflowCommentPeer.DATABASE_NAME);
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
                    TWorkflowCommentPeer.doInsert((TWorkflowComment) this, con);
                    setNew(false);
                }
                else
                {
                    TWorkflowCommentPeer.doUpdate((TWorkflowComment) this, con);
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
    public TWorkflowComment copy() throws TorqueException
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
    public TWorkflowComment copy(Connection con) throws TorqueException
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
    public TWorkflowComment copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TWorkflowComment(), deepcopy);
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
    public TWorkflowComment copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TWorkflowComment(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TWorkflowComment copyInto(TWorkflowComment copyObj) throws TorqueException
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
    protected TWorkflowComment copyInto(TWorkflowComment copyObj, Connection con) throws TorqueException
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
    protected TWorkflowComment copyInto(TWorkflowComment copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setDescription(description);
        copyObj.setWorkflow(workflow);
        copyObj.setNodeX(nodeX);
        copyObj.setNodeY(nodeY);
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
    protected TWorkflowComment copyInto(TWorkflowComment copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setDescription(description);
        copyObj.setWorkflow(workflow);
        copyObj.setNodeX(nodeX);
        copyObj.setNodeY(nodeY);
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
    public TWorkflowCommentPeer getPeer()
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
        return TWorkflowCommentPeer.getTableMap();
    }

  
    /**
     * Creates a TWorkflowCommentBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TWorkflowCommentBean with the contents of this object
     */
    public TWorkflowCommentBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TWorkflowCommentBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TWorkflowCommentBean with the contents of this object
     */
    public TWorkflowCommentBean getBean(IdentityMap createdBeans)
    {
        TWorkflowCommentBean result = (TWorkflowCommentBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TWorkflowCommentBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setDescription(getDescription());
        result.setWorkflow(getWorkflow());
        result.setNodeX(getNodeX());
        result.setNodeY(getNodeY());
        result.setUuid(getUuid());





        if (aTWorkflowDef != null)
        {
            TWorkflowDefBean relatedBean = aTWorkflowDef.getBean(createdBeans);
            result.setTWorkflowDefBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TWorkflowComment with the contents
     * of a TWorkflowCommentBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TWorkflowCommentBean which contents are used to create
     *        the resulting class
     * @return an instance of TWorkflowComment with the contents of bean
     */
    public static TWorkflowComment createTWorkflowComment(TWorkflowCommentBean bean)
        throws TorqueException
    {
        return createTWorkflowComment(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TWorkflowComment with the contents
     * of a TWorkflowCommentBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TWorkflowCommentBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TWorkflowComment with the contents of bean
     */

    public static TWorkflowComment createTWorkflowComment(TWorkflowCommentBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TWorkflowComment result = (TWorkflowComment) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TWorkflowComment();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setDescription(bean.getDescription());
        result.setWorkflow(bean.getWorkflow());
        result.setNodeX(bean.getNodeX());
        result.setNodeY(bean.getNodeY());
        result.setUuid(bean.getUuid());





        {
            TWorkflowDefBean relatedBean = bean.getTWorkflowDefBean();
            if (relatedBean != null)
            {
                TWorkflowDef relatedObject = TWorkflowDef.createTWorkflowDef(relatedBean, createdObjects);
                result.setTWorkflowDef(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TWorkflowComment:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Workflow = ")
           .append(getWorkflow())
           .append("\n");
        str.append("NodeX = ")
           .append(getNodeX())
           .append("\n");
        str.append("NodeY = ")
           .append(getNodeY())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
