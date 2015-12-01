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
import com.aurel.track.beans.TCardPanelBean;

import com.aurel.track.beans.TCardFieldBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TCardPanel
 */
public abstract class BaseTCardPanel extends TpBaseObject
{
    /** The Peer class */
    private static final TCardPanelPeer peer =
        new TCardPanelPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the person field */
    private Integer person;

    /** The value for the rowsNo field */
    private Integer rowsNo;

    /** The value for the colsNo field */
    private Integer colsNo;

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



        // update associated TCardField
        if (collTCardFields != null)
        {
            for (int i = 0; i < collTCardFields.size(); i++)
            {
                ((TCardField) collTCardFields.get(i))
                        .setCardPanel(v);
            }
        }
    }

    /**
     * Get the Person
     *
     * @return Integer
     */
    public Integer getPerson()
    {
        return person;
    }


    /**
     * Set the value of Person
     *
     * @param v new value
     */
    public void setPerson(Integer v) 
    {

        if (!ObjectUtils.equals(this.person, v))
        {
            this.person = v;
            setModified(true);
        }


    }

    /**
     * Get the RowsNo
     *
     * @return Integer
     */
    public Integer getRowsNo()
    {
        return rowsNo;
    }


    /**
     * Set the value of RowsNo
     *
     * @param v new value
     */
    public void setRowsNo(Integer v) 
    {

        if (!ObjectUtils.equals(this.rowsNo, v))
        {
            this.rowsNo = v;
            setModified(true);
        }


    }

    /**
     * Get the ColsNo
     *
     * @return Integer
     */
    public Integer getColsNo()
    {
        return colsNo;
    }


    /**
     * Set the value of ColsNo
     *
     * @param v new value
     */
    public void setColsNo(Integer v) 
    {

        if (!ObjectUtils.equals(this.colsNo, v))
        {
            this.colsNo = v;
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
     * Collection to store aggregation of collTCardFields
     */
    protected List<TCardField> collTCardFields;

    /**
     * Temporary storage of collTCardFields to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTCardFields()
    {
        if (collTCardFields == null)
        {
            collTCardFields = new ArrayList<TCardField>();
        }
    }


    /**
     * Method called to associate a TCardField object to this object
     * through the TCardField foreign key attribute
     *
     * @param l TCardField
     * @throws TorqueException
     */
    public void addTCardField(TCardField l) throws TorqueException
    {
        getTCardFields().add(l);
        l.setTCardPanel((TCardPanel) this);
    }

    /**
     * Method called to associate a TCardField object to this object
     * through the TCardField foreign key attribute using connection.
     *
     * @param l TCardField
     * @throws TorqueException
     */
    public void addTCardField(TCardField l, Connection con) throws TorqueException
    {
        getTCardFields(con).add(l);
        l.setTCardPanel((TCardPanel) this);
    }

    /**
     * The criteria used to select the current contents of collTCardFields
     */
    private Criteria lastTCardFieldsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCardFields(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TCardField> getTCardFields()
        throws TorqueException
    {
        if (collTCardFields == null)
        {
            collTCardFields = getTCardFields(new Criteria(10));
        }
        return collTCardFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCardPanel has previously
     * been saved, it will retrieve related TCardFields from storage.
     * If this TCardPanel is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TCardField> getTCardFields(Criteria criteria) throws TorqueException
    {
        if (collTCardFields == null)
        {
            if (isNew())
            {
               collTCardFields = new ArrayList<TCardField>();
            }
            else
            {
                criteria.add(TCardFieldPeer.CARDPANEL, getObjectID() );
                collTCardFields = TCardFieldPeer.doSelect(criteria);
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
                criteria.add(TCardFieldPeer.CARDPANEL, getObjectID());
                if (!lastTCardFieldsCriteria.equals(criteria))
                {
                    collTCardFields = TCardFieldPeer.doSelect(criteria);
                }
            }
        }
        lastTCardFieldsCriteria = criteria;

        return collTCardFields;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCardFields(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCardField> getTCardFields(Connection con) throws TorqueException
    {
        if (collTCardFields == null)
        {
            collTCardFields = getTCardFields(new Criteria(10), con);
        }
        return collTCardFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCardPanel has previously
     * been saved, it will retrieve related TCardFields from storage.
     * If this TCardPanel is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCardField> getTCardFields(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTCardFields == null)
        {
            if (isNew())
            {
               collTCardFields = new ArrayList<TCardField>();
            }
            else
            {
                 criteria.add(TCardFieldPeer.CARDPANEL, getObjectID());
                 collTCardFields = TCardFieldPeer.doSelect(criteria, con);
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
                 criteria.add(TCardFieldPeer.CARDPANEL, getObjectID());
                 if (!lastTCardFieldsCriteria.equals(criteria))
                 {
                     collTCardFields = TCardFieldPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTCardFieldsCriteria = criteria;

         return collTCardFields;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCardPanel is new, it will return
     * an empty collection; or if this TCardPanel has previously
     * been saved, it will retrieve related TCardFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCardPanel.
     */
    protected List<TCardField> getTCardFieldsJoinTCardPanel(Criteria criteria)
        throws TorqueException
    {
        if (collTCardFields == null)
        {
            if (isNew())
            {
               collTCardFields = new ArrayList<TCardField>();
            }
            else
            {
                criteria.add(TCardFieldPeer.CARDPANEL, getObjectID());
                collTCardFields = TCardFieldPeer.doSelectJoinTCardPanel(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCardFieldPeer.CARDPANEL, getObjectID());
            if (!lastTCardFieldsCriteria.equals(criteria))
            {
                collTCardFields = TCardFieldPeer.doSelectJoinTCardPanel(criteria);
            }
        }
        lastTCardFieldsCriteria = criteria;

        return collTCardFields;
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
            fieldNames.add("Person");
            fieldNames.add("RowsNo");
            fieldNames.add("ColsNo");
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
        if (name.equals("Person"))
        {
            return getPerson();
        }
        if (name.equals("RowsNo"))
        {
            return getRowsNo();
        }
        if (name.equals("ColsNo"))
        {
            return getColsNo();
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
        if (name.equals("Person"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPerson((Integer) value);
            return true;
        }
        if (name.equals("RowsNo"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRowsNo((Integer) value);
            return true;
        }
        if (name.equals("ColsNo"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setColsNo((Integer) value);
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
        if (name.equals(TCardPanelPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TCardPanelPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TCardPanelPeer.ROWSNO))
        {
            return getRowsNo();
        }
        if (name.equals(TCardPanelPeer.COLSNO))
        {
            return getColsNo();
        }
        if (name.equals(TCardPanelPeer.TPUUID))
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
      if (TCardPanelPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TCardPanelPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TCardPanelPeer.ROWSNO.equals(name))
        {
            return setByName("RowsNo", value);
        }
      if (TCardPanelPeer.COLSNO.equals(name))
        {
            return setByName("ColsNo", value);
        }
      if (TCardPanelPeer.TPUUID.equals(name))
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
            return getPerson();
        }
        if (pos == 2)
        {
            return getRowsNo();
        }
        if (pos == 3)
        {
            return getColsNo();
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
            return setByName("Person", value);
        }
    if (position == 2)
        {
            return setByName("RowsNo", value);
        }
    if (position == 3)
        {
            return setByName("ColsNo", value);
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
        save(TCardPanelPeer.DATABASE_NAME);
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
                    TCardPanelPeer.doInsert((TCardPanel) this, con);
                    setNew(false);
                }
                else
                {
                    TCardPanelPeer.doUpdate((TCardPanel) this, con);
                }
            }


            if (collTCardFields != null)
            {
                for (int i = 0; i < collTCardFields.size(); i++)
                {
                    ((TCardField) collTCardFields.get(i)).save(con);
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
    public TCardPanel copy() throws TorqueException
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
    public TCardPanel copy(Connection con) throws TorqueException
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
    public TCardPanel copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TCardPanel(), deepcopy);
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
    public TCardPanel copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TCardPanel(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TCardPanel copyInto(TCardPanel copyObj) throws TorqueException
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
    protected TCardPanel copyInto(TCardPanel copyObj, Connection con) throws TorqueException
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
    protected TCardPanel copyInto(TCardPanel copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setRowsNo(rowsNo);
        copyObj.setColsNo(colsNo);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TCardField> vTCardFields = getTCardFields();
        if (vTCardFields != null)
        {
            for (int i = 0; i < vTCardFields.size(); i++)
            {
                TCardField obj =  vTCardFields.get(i);
                copyObj.addTCardField(obj.copy());
            }
        }
        else
        {
            copyObj.collTCardFields = null;
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
    protected TCardPanel copyInto(TCardPanel copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setRowsNo(rowsNo);
        copyObj.setColsNo(colsNo);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TCardField> vTCardFields = getTCardFields(con);
        if (vTCardFields != null)
        {
            for (int i = 0; i < vTCardFields.size(); i++)
            {
                TCardField obj =  vTCardFields.get(i);
                copyObj.addTCardField(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTCardFields = null;
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
    public TCardPanelPeer getPeer()
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
        return TCardPanelPeer.getTableMap();
    }

  
    /**
     * Creates a TCardPanelBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TCardPanelBean with the contents of this object
     */
    public TCardPanelBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TCardPanelBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TCardPanelBean with the contents of this object
     */
    public TCardPanelBean getBean(IdentityMap createdBeans)
    {
        TCardPanelBean result = (TCardPanelBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TCardPanelBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setPerson(getPerson());
        result.setRowsNo(getRowsNo());
        result.setColsNo(getColsNo());
        result.setUuid(getUuid());



        if (collTCardFields != null)
        {
            List<TCardFieldBean> relatedBeans = new ArrayList<TCardFieldBean>(collTCardFields.size());
            for (Iterator<TCardField> collTCardFieldsIt = collTCardFields.iterator(); collTCardFieldsIt.hasNext(); )
            {
                TCardField related = (TCardField) collTCardFieldsIt.next();
                TCardFieldBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTCardFieldBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TCardPanel with the contents
     * of a TCardPanelBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TCardPanelBean which contents are used to create
     *        the resulting class
     * @return an instance of TCardPanel with the contents of bean
     */
    public static TCardPanel createTCardPanel(TCardPanelBean bean)
        throws TorqueException
    {
        return createTCardPanel(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TCardPanel with the contents
     * of a TCardPanelBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TCardPanelBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TCardPanel with the contents of bean
     */

    public static TCardPanel createTCardPanel(TCardPanelBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TCardPanel result = (TCardPanel) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TCardPanel();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setPerson(bean.getPerson());
        result.setRowsNo(bean.getRowsNo());
        result.setColsNo(bean.getColsNo());
        result.setUuid(bean.getUuid());



        {
            List<TCardFieldBean> relatedBeans = bean.getTCardFieldBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TCardFieldBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TCardFieldBean relatedBean =  relatedBeansIt.next();
                    TCardField related = TCardField.createTCardField(relatedBean, createdObjects);
                    result.addTCardFieldFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TCardField object to this object.
     * through the TCardField foreign key attribute
     *
     * @param toAdd TCardField
     */
    protected void addTCardFieldFromBean(TCardField toAdd)
    {
        initTCardFields();
        collTCardFields.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TCardPanel:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("RowsNo = ")
           .append(getRowsNo())
           .append("\n");
        str.append("ColsNo = ")
           .append(getColsNo())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
