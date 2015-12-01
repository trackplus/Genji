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



import com.aurel.track.persist.TAccount;
import com.aurel.track.persist.TAccountPeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TProjectAccountBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TAccountBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TProjectAccount
 */
public abstract class BaseTProjectAccount extends TpBaseObject
{
    /** The Peer class */
    private static final TProjectAccountPeer peer =
        new TProjectAccountPeer();


    /** The value for the account field */
    private Integer account;

    /** The value for the project field */
    private Integer project;

    /** The value for the uuid field */
    private String uuid;


    /**
     * Get the Account
     *
     * @return Integer
     */
    public Integer getAccount()
    {
        return account;
    }


    /**
     * Set the value of Account
     *
     * @param v new value
     */
    public void setAccount(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.account, v))
        {
            this.account = v;
            setModified(true);
        }


        if (aTAccount != null && !ObjectUtils.equals(aTAccount.getObjectID(), v))
        {
            aTAccount = null;
        }

    }

    /**
     * Get the Project
     *
     * @return Integer
     */
    public Integer getProject()
    {
        return project;
    }


    /**
     * Set the value of Project
     *
     * @param v new value
     */
    public void setProject(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.project, v))
        {
            this.project = v;
            setModified(true);
        }


        if (aTProject != null && !ObjectUtils.equals(aTProject.getObjectID(), v))
        {
            aTProject = null;
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

    



    private TProject aTProject;

    /**
     * Declares an association between this object and a TProject object
     *
     * @param v TProject
     * @throws TorqueException
     */
    public void setTProject(TProject v) throws TorqueException
    {
        if (v == null)
        {
            setProject((Integer) null);
        }
        else
        {
            setProject(v.getObjectID());
        }
        aTProject = v;
    }


    /**
     * Returns the associated TProject object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProject object
     * @throws TorqueException
     */
    public TProject getTProject()
        throws TorqueException
    {
        if (aTProject == null && (!ObjectUtils.equals(this.project, null)))
        {
            aTProject = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.project));
        }
        return aTProject;
    }

    /**
     * Return the associated TProject object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProject object
     * @throws TorqueException
     */
    public TProject getTProject(Connection connection)
        throws TorqueException
    {
        if (aTProject == null && (!ObjectUtils.equals(this.project, null)))
        {
            aTProject = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.project), connection);
        }
        return aTProject;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectKey(ObjectKey key) throws TorqueException
    {

        setProject(new Integer(((NumberKey) key).intValue()));
    }




    private TAccount aTAccount;

    /**
     * Declares an association between this object and a TAccount object
     *
     * @param v TAccount
     * @throws TorqueException
     */
    public void setTAccount(TAccount v) throws TorqueException
    {
        if (v == null)
        {
            setAccount((Integer) null);
        }
        else
        {
            setAccount(v.getObjectID());
        }
        aTAccount = v;
    }


    /**
     * Returns the associated TAccount object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TAccount object
     * @throws TorqueException
     */
    public TAccount getTAccount()
        throws TorqueException
    {
        if (aTAccount == null && (!ObjectUtils.equals(this.account, null)))
        {
            aTAccount = TAccountPeer.retrieveByPK(SimpleKey.keyFor(this.account));
        }
        return aTAccount;
    }

    /**
     * Return the associated TAccount object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TAccount object
     * @throws TorqueException
     */
    public TAccount getTAccount(Connection connection)
        throws TorqueException
    {
        if (aTAccount == null && (!ObjectUtils.equals(this.account, null)))
        {
            aTAccount = TAccountPeer.retrieveByPK(SimpleKey.keyFor(this.account), connection);
        }
        return aTAccount;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTAccountKey(ObjectKey key) throws TorqueException
    {

        setAccount(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Account");
            fieldNames.add("Project");
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
        if (name.equals("Account"))
        {
            return getAccount();
        }
        if (name.equals("Project"))
        {
            return getProject();
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
        if (name.equals("Account"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAccount((Integer) value);
            return true;
        }
        if (name.equals("Project"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProject((Integer) value);
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
        if (name.equals(TProjectAccountPeer.ACCOUNT))
        {
            return getAccount();
        }
        if (name.equals(TProjectAccountPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TProjectAccountPeer.TPUUID))
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
      if (TProjectAccountPeer.ACCOUNT.equals(name))
        {
            return setByName("Account", value);
        }
      if (TProjectAccountPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TProjectAccountPeer.TPUUID.equals(name))
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
            return getAccount();
        }
        if (pos == 1)
        {
            return getProject();
        }
        if (pos == 2)
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
            return setByName("Account", value);
        }
    if (position == 1)
        {
            return setByName("Project", value);
        }
    if (position == 2)
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
        save(TProjectAccountPeer.DATABASE_NAME);
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
                    TProjectAccountPeer.doInsert((TProjectAccount) this, con);
                    setNew(false);
                }
                else
                {
                    TProjectAccountPeer.doUpdate((TProjectAccount) this, con);
                }
            }

            alreadyInSave = false;
        }
    }



    private final SimpleKey[] pks = new SimpleKey[2];
    private final ComboKey comboPK = new ComboKey(pks);

    /**
     * Set the PrimaryKey with an ObjectKey
     *
     * @param key
     */
    public void setPrimaryKey(ObjectKey key) throws TorqueException
    {
        SimpleKey[] keys = (SimpleKey[]) key.getValue();
        setAccount(new Integer(((NumberKey)keys[0]).intValue()));
        setProject(new Integer(((NumberKey)keys[1]).intValue()));
    }

    /**
     * Set the PrimaryKey using SimpleKeys.
     *
     * @param account Integer
     * @param project Integer
     */
    public void setPrimaryKey( Integer account, Integer project)
        throws TorqueException
    {
        setAccount(account);
        setProject(project);
    }

    /**
     * Set the PrimaryKey using a String.
     */
    public void setPrimaryKey(String key) throws TorqueException
    {
        setPrimaryKey(new ComboKey(key));
    }

    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
        pks[0] = SimpleKey.keyFor(getAccount());
        pks[1] = SimpleKey.keyFor(getProject());
        return comboPK;
    }
 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     */
    public TProjectAccount copy() throws TorqueException
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
    public TProjectAccount copy(Connection con) throws TorqueException
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
    public TProjectAccount copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TProjectAccount(), deepcopy);
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
    public TProjectAccount copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TProjectAccount(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TProjectAccount copyInto(TProjectAccount copyObj) throws TorqueException
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
    protected TProjectAccount copyInto(TProjectAccount copyObj, Connection con) throws TorqueException
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
    protected TProjectAccount copyInto(TProjectAccount copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setAccount(account);
        copyObj.setProject(project);
        copyObj.setUuid(uuid);

        copyObj.setAccount((Integer)null);
        copyObj.setProject((Integer)null);

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
    protected TProjectAccount copyInto(TProjectAccount copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setAccount(account);
        copyObj.setProject(project);
        copyObj.setUuid(uuid);

        copyObj.setAccount((Integer)null);
        copyObj.setProject((Integer)null);

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
    public TProjectAccountPeer getPeer()
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
        return TProjectAccountPeer.getTableMap();
    }

  
    /**
     * Creates a TProjectAccountBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TProjectAccountBean with the contents of this object
     */
    public TProjectAccountBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TProjectAccountBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TProjectAccountBean with the contents of this object
     */
    public TProjectAccountBean getBean(IdentityMap createdBeans)
    {
        TProjectAccountBean result = (TProjectAccountBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TProjectAccountBean();
        createdBeans.put(this, result);

        result.setAccount(getAccount());
        result.setProject(getProject());
        result.setUuid(getUuid());





        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }



        if (aTAccount != null)
        {
            TAccountBean relatedBean = aTAccount.getBean(createdBeans);
            result.setTAccountBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TProjectAccount with the contents
     * of a TProjectAccountBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TProjectAccountBean which contents are used to create
     *        the resulting class
     * @return an instance of TProjectAccount with the contents of bean
     */
    public static TProjectAccount createTProjectAccount(TProjectAccountBean bean)
        throws TorqueException
    {
        return createTProjectAccount(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TProjectAccount with the contents
     * of a TProjectAccountBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TProjectAccountBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TProjectAccount with the contents of bean
     */

    public static TProjectAccount createTProjectAccount(TProjectAccountBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TProjectAccount result = (TProjectAccount) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TProjectAccount();
        createdObjects.put(bean, result);

        result.setAccount(bean.getAccount());
        result.setProject(bean.getProject());
        result.setUuid(bean.getUuid());





        {
            TProjectBean relatedBean = bean.getTProjectBean();
            if (relatedBean != null)
            {
                TProject relatedObject = TProject.createTProject(relatedBean, createdObjects);
                result.setTProject(relatedObject);
            }
        }



        {
            TAccountBean relatedBean = bean.getTAccountBean();
            if (relatedBean != null)
            {
                TAccount relatedObject = TAccount.createTAccount(relatedBean, createdObjects);
                result.setTAccount(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TProjectAccount:\n");
        str.append("Account = ")
           .append(getAccount())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
