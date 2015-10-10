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



import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.persist.TWorkflowTransition;
import com.aurel.track.persist.TWorkflowTransitionPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TItemTransitionBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkflowTransitionBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TItemTransition
 */
public abstract class BaseTItemTransition extends TpBaseObject
{
    /** The Peer class */
    private static final TItemTransitionPeer peer =
        new TItemTransitionPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the transition field */
    private Integer transition;

    /** The value for the transitionTime field */
    private Date transitionTime;

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
     * Get the WorkItem
     *
     * @return Integer
     */
    public Integer getWorkItem()
    {
        return workItem;
    }


    /**
     * Set the value of WorkItem
     *
     * @param v new value
     */
    public void setWorkItem(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workItem, v))
        {
            this.workItem = v;
            setModified(true);
        }


        if (aTWorkItem != null && !ObjectUtils.equals(aTWorkItem.getObjectID(), v))
        {
            aTWorkItem = null;
        }

    }

    /**
     * Get the Transition
     *
     * @return Integer
     */
    public Integer getTransition()
    {
        return transition;
    }


    /**
     * Set the value of Transition
     *
     * @param v new value
     */
    public void setTransition(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.transition, v))
        {
            this.transition = v;
            setModified(true);
        }


        if (aTWorkflowTransition != null && !ObjectUtils.equals(aTWorkflowTransition.getObjectID(), v))
        {
            aTWorkflowTransition = null;
        }

    }

    /**
     * Get the TransitionTime
     *
     * @return Date
     */
    public Date getTransitionTime()
    {
        return transitionTime;
    }


    /**
     * Set the value of TransitionTime
     *
     * @param v new value
     */
    public void setTransitionTime(Date v) 
    {

        if (!ObjectUtils.equals(this.transitionTime, v))
        {
            this.transitionTime = v;
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

    



    private TWorkItem aTWorkItem;

    /**
     * Declares an association between this object and a TWorkItem object
     *
     * @param v TWorkItem
     * @throws TorqueException
     */
    public void setTWorkItem(TWorkItem v) throws TorqueException
    {
        if (v == null)
        {
            setWorkItem((Integer) null);
        }
        else
        {
            setWorkItem(v.getObjectID());
        }
        aTWorkItem = v;
    }


    /**
     * Returns the associated TWorkItem object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem()
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItem));
        }
        return aTWorkItem;
    }

    /**
     * Return the associated TWorkItem object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem(Connection connection)
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItem), connection);
        }
        return aTWorkItem;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkItemKey(ObjectKey key) throws TorqueException
    {

        setWorkItem(new Integer(((NumberKey) key).intValue()));
    }




    private TWorkflowTransition aTWorkflowTransition;

    /**
     * Declares an association between this object and a TWorkflowTransition object
     *
     * @param v TWorkflowTransition
     * @throws TorqueException
     */
    public void setTWorkflowTransition(TWorkflowTransition v) throws TorqueException
    {
        if (v == null)
        {
            setTransition((Integer) null);
        }
        else
        {
            setTransition(v.getObjectID());
        }
        aTWorkflowTransition = v;
    }


    /**
     * Returns the associated TWorkflowTransition object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowTransition object
     * @throws TorqueException
     */
    public TWorkflowTransition getTWorkflowTransition()
        throws TorqueException
    {
        if (aTWorkflowTransition == null && (!ObjectUtils.equals(this.transition, null)))
        {
            aTWorkflowTransition = TWorkflowTransitionPeer.retrieveByPK(SimpleKey.keyFor(this.transition));
        }
        return aTWorkflowTransition;
    }

    /**
     * Return the associated TWorkflowTransition object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowTransition object
     * @throws TorqueException
     */
    public TWorkflowTransition getTWorkflowTransition(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowTransition == null && (!ObjectUtils.equals(this.transition, null)))
        {
            aTWorkflowTransition = TWorkflowTransitionPeer.retrieveByPK(SimpleKey.keyFor(this.transition), connection);
        }
        return aTWorkflowTransition;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowTransitionKey(ObjectKey key) throws TorqueException
    {

        setTransition(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("WorkItem");
            fieldNames.add("Transition");
            fieldNames.add("TransitionTime");
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
        if (name.equals("WorkItem"))
        {
            return getWorkItem();
        }
        if (name.equals("Transition"))
        {
            return getTransition();
        }
        if (name.equals("TransitionTime"))
        {
            return getTransitionTime();
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
        if (name.equals("WorkItem"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkItem((Integer) value);
            return true;
        }
        if (name.equals("Transition"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTransition((Integer) value);
            return true;
        }
        if (name.equals("TransitionTime"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTransitionTime((Date) value);
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
        if (name.equals(TItemTransitionPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TItemTransitionPeer.WORKITEM))
        {
            return getWorkItem();
        }
        if (name.equals(TItemTransitionPeer.TRANSITION))
        {
            return getTransition();
        }
        if (name.equals(TItemTransitionPeer.TRANSITIONTIME))
        {
            return getTransitionTime();
        }
        if (name.equals(TItemTransitionPeer.TPUUID))
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
      if (TItemTransitionPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TItemTransitionPeer.WORKITEM.equals(name))
        {
            return setByName("WorkItem", value);
        }
      if (TItemTransitionPeer.TRANSITION.equals(name))
        {
            return setByName("Transition", value);
        }
      if (TItemTransitionPeer.TRANSITIONTIME.equals(name))
        {
            return setByName("TransitionTime", value);
        }
      if (TItemTransitionPeer.TPUUID.equals(name))
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
            return getWorkItem();
        }
        if (pos == 2)
        {
            return getTransition();
        }
        if (pos == 3)
        {
            return getTransitionTime();
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
            return setByName("WorkItem", value);
        }
    if (position == 2)
        {
            return setByName("Transition", value);
        }
    if (position == 3)
        {
            return setByName("TransitionTime", value);
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
        save(TItemTransitionPeer.DATABASE_NAME);
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
                    TItemTransitionPeer.doInsert((TItemTransition) this, con);
                    setNew(false);
                }
                else
                {
                    TItemTransitionPeer.doUpdate((TItemTransition) this, con);
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
    public TItemTransition copy() throws TorqueException
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
    public TItemTransition copy(Connection con) throws TorqueException
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
    public TItemTransition copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TItemTransition(), deepcopy);
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
    public TItemTransition copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TItemTransition(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TItemTransition copyInto(TItemTransition copyObj) throws TorqueException
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
    protected TItemTransition copyInto(TItemTransition copyObj, Connection con) throws TorqueException
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
    protected TItemTransition copyInto(TItemTransition copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkItem(workItem);
        copyObj.setTransition(transition);
        copyObj.setTransitionTime(transitionTime);
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
    protected TItemTransition copyInto(TItemTransition copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkItem(workItem);
        copyObj.setTransition(transition);
        copyObj.setTransitionTime(transitionTime);
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
    public TItemTransitionPeer getPeer()
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
        return TItemTransitionPeer.getTableMap();
    }

  
    /**
     * Creates a TItemTransitionBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TItemTransitionBean with the contents of this object
     */
    public TItemTransitionBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TItemTransitionBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TItemTransitionBean with the contents of this object
     */
    public TItemTransitionBean getBean(IdentityMap createdBeans)
    {
        TItemTransitionBean result = (TItemTransitionBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TItemTransitionBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setWorkItem(getWorkItem());
        result.setTransition(getTransition());
        result.setTransitionTime(getTransitionTime());
        result.setUuid(getUuid());





        if (aTWorkItem != null)
        {
            TWorkItemBean relatedBean = aTWorkItem.getBean(createdBeans);
            result.setTWorkItemBean(relatedBean);
        }



        if (aTWorkflowTransition != null)
        {
            TWorkflowTransitionBean relatedBean = aTWorkflowTransition.getBean(createdBeans);
            result.setTWorkflowTransitionBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TItemTransition with the contents
     * of a TItemTransitionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TItemTransitionBean which contents are used to create
     *        the resulting class
     * @return an instance of TItemTransition with the contents of bean
     */
    public static TItemTransition createTItemTransition(TItemTransitionBean bean)
        throws TorqueException
    {
        return createTItemTransition(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TItemTransition with the contents
     * of a TItemTransitionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TItemTransitionBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TItemTransition with the contents of bean
     */

    public static TItemTransition createTItemTransition(TItemTransitionBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TItemTransition result = (TItemTransition) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TItemTransition();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setWorkItem(bean.getWorkItem());
        result.setTransition(bean.getTransition());
        result.setTransitionTime(bean.getTransitionTime());
        result.setUuid(bean.getUuid());





        {
            TWorkItemBean relatedBean = bean.getTWorkItemBean();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItem(relatedObject);
            }
        }



        {
            TWorkflowTransitionBean relatedBean = bean.getTWorkflowTransitionBean();
            if (relatedBean != null)
            {
                TWorkflowTransition relatedObject = TWorkflowTransition.createTWorkflowTransition(relatedBean, createdObjects);
                result.setTWorkflowTransition(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TItemTransition:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("WorkItem = ")
           .append(getWorkItem())
           .append("\n");
        str.append("Transition = ")
           .append(getTransition())
           .append("\n");
        str.append("TransitionTime = ")
           .append(getTransitionTime())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
