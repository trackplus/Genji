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



import com.aurel.track.persist.TExportTemplate;
import com.aurel.track.persist.TExportTemplatePeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TTemplatePersonBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TExportTemplateBean;



/**
 * The persons who can or can not run a report template
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TTemplatePerson
 */
public abstract class BaseTTemplatePerson extends TpBaseObject
{
    /** The Peer class */
    private static final TTemplatePersonPeer peer =
        new TTemplatePersonPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the reportTemplate field */
    private Integer reportTemplate;

    /** The value for the person field */
    private Integer person;

    /** The value for the rightFlag field */
    private Integer rightFlag;

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
     * Get the ReportTemplate
     *
     * @return Integer
     */
    public Integer getReportTemplate()
    {
        return reportTemplate;
    }


    /**
     * Set the value of ReportTemplate
     *
     * @param v new value
     */
    public void setReportTemplate(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.reportTemplate, v))
        {
            this.reportTemplate = v;
            setModified(true);
        }


        if (aTExportTemplate != null && !ObjectUtils.equals(aTExportTemplate.getObjectID(), v))
        {
            aTExportTemplate = null;
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
    public void setPerson(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.person, v))
        {
            this.person = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the RightFlag
     *
     * @return Integer
     */
    public Integer getRightFlag()
    {
        return rightFlag;
    }


    /**
     * Set the value of RightFlag
     *
     * @param v new value
     */
    public void setRightFlag(Integer v) 
    {

        if (!ObjectUtils.equals(this.rightFlag, v))
        {
            this.rightFlag = v;
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

    



    private TPerson aTPerson;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPerson(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setPerson((Integer) null);
        }
        else
        {
            setPerson(v.getObjectID());
        }
        aTPerson = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson()
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.person, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.person));
        }
        return aTPerson;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson(Connection connection)
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.person, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.person), connection);
        }
        return aTPerson;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonKey(ObjectKey key) throws TorqueException
    {

        setPerson(new Integer(((NumberKey) key).intValue()));
    }




    private TExportTemplate aTExportTemplate;

    /**
     * Declares an association between this object and a TExportTemplate object
     *
     * @param v TExportTemplate
     * @throws TorqueException
     */
    public void setTExportTemplate(TExportTemplate v) throws TorqueException
    {
        if (v == null)
        {
            setReportTemplate((Integer) null);
        }
        else
        {
            setReportTemplate(v.getObjectID());
        }
        aTExportTemplate = v;
    }


    /**
     * Returns the associated TExportTemplate object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TExportTemplate object
     * @throws TorqueException
     */
    public TExportTemplate getTExportTemplate()
        throws TorqueException
    {
        if (aTExportTemplate == null && (!ObjectUtils.equals(this.reportTemplate, null)))
        {
            aTExportTemplate = TExportTemplatePeer.retrieveByPK(SimpleKey.keyFor(this.reportTemplate));
        }
        return aTExportTemplate;
    }

    /**
     * Return the associated TExportTemplate object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TExportTemplate object
     * @throws TorqueException
     */
    public TExportTemplate getTExportTemplate(Connection connection)
        throws TorqueException
    {
        if (aTExportTemplate == null && (!ObjectUtils.equals(this.reportTemplate, null)))
        {
            aTExportTemplate = TExportTemplatePeer.retrieveByPK(SimpleKey.keyFor(this.reportTemplate), connection);
        }
        return aTExportTemplate;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTExportTemplateKey(ObjectKey key) throws TorqueException
    {

        setReportTemplate(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("ReportTemplate");
            fieldNames.add("Person");
            fieldNames.add("RightFlag");
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
        if (name.equals("ReportTemplate"))
        {
            return getReportTemplate();
        }
        if (name.equals("Person"))
        {
            return getPerson();
        }
        if (name.equals("RightFlag"))
        {
            return getRightFlag();
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
        if (name.equals("ReportTemplate"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReportTemplate((Integer) value);
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
        if (name.equals("RightFlag"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRightFlag((Integer) value);
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
        if (name.equals(TTemplatePersonPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TTemplatePersonPeer.REPORTTEMPLATE))
        {
            return getReportTemplate();
        }
        if (name.equals(TTemplatePersonPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TTemplatePersonPeer.RIGHTFLAG))
        {
            return getRightFlag();
        }
        if (name.equals(TTemplatePersonPeer.TPUUID))
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
      if (TTemplatePersonPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TTemplatePersonPeer.REPORTTEMPLATE.equals(name))
        {
            return setByName("ReportTemplate", value);
        }
      if (TTemplatePersonPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TTemplatePersonPeer.RIGHTFLAG.equals(name))
        {
            return setByName("RightFlag", value);
        }
      if (TTemplatePersonPeer.TPUUID.equals(name))
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
            return getReportTemplate();
        }
        if (pos == 2)
        {
            return getPerson();
        }
        if (pos == 3)
        {
            return getRightFlag();
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
            return setByName("ReportTemplate", value);
        }
    if (position == 2)
        {
            return setByName("Person", value);
        }
    if (position == 3)
        {
            return setByName("RightFlag", value);
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
        save(TTemplatePersonPeer.DATABASE_NAME);
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
                    TTemplatePersonPeer.doInsert((TTemplatePerson) this, con);
                    setNew(false);
                }
                else
                {
                    TTemplatePersonPeer.doUpdate((TTemplatePerson) this, con);
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
    public TTemplatePerson copy() throws TorqueException
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
    public TTemplatePerson copy(Connection con) throws TorqueException
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
    public TTemplatePerson copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TTemplatePerson(), deepcopy);
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
    public TTemplatePerson copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TTemplatePerson(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TTemplatePerson copyInto(TTemplatePerson copyObj) throws TorqueException
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
    protected TTemplatePerson copyInto(TTemplatePerson copyObj, Connection con) throws TorqueException
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
    protected TTemplatePerson copyInto(TTemplatePerson copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setReportTemplate(reportTemplate);
        copyObj.setPerson(person);
        copyObj.setRightFlag(rightFlag);
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
    protected TTemplatePerson copyInto(TTemplatePerson copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setReportTemplate(reportTemplate);
        copyObj.setPerson(person);
        copyObj.setRightFlag(rightFlag);
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
    public TTemplatePersonPeer getPeer()
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
        return TTemplatePersonPeer.getTableMap();
    }

  
    /**
     * Creates a TTemplatePersonBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TTemplatePersonBean with the contents of this object
     */
    public TTemplatePersonBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TTemplatePersonBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TTemplatePersonBean with the contents of this object
     */
    public TTemplatePersonBean getBean(IdentityMap createdBeans)
    {
        TTemplatePersonBean result = (TTemplatePersonBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TTemplatePersonBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setReportTemplate(getReportTemplate());
        result.setPerson(getPerson());
        result.setRightFlag(getRightFlag());
        result.setUuid(getUuid());





        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTExportTemplate != null)
        {
            TExportTemplateBean relatedBean = aTExportTemplate.getBean(createdBeans);
            result.setTExportTemplateBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TTemplatePerson with the contents
     * of a TTemplatePersonBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TTemplatePersonBean which contents are used to create
     *        the resulting class
     * @return an instance of TTemplatePerson with the contents of bean
     */
    public static TTemplatePerson createTTemplatePerson(TTemplatePersonBean bean)
        throws TorqueException
    {
        return createTTemplatePerson(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TTemplatePerson with the contents
     * of a TTemplatePersonBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TTemplatePersonBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TTemplatePerson with the contents of bean
     */

    public static TTemplatePerson createTTemplatePerson(TTemplatePersonBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TTemplatePerson result = (TTemplatePerson) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TTemplatePerson();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setReportTemplate(bean.getReportTemplate());
        result.setPerson(bean.getPerson());
        result.setRightFlag(bean.getRightFlag());
        result.setUuid(bean.getUuid());





        {
            TPersonBean relatedBean = bean.getTPersonBean();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPerson(relatedObject);
            }
        }



        {
            TExportTemplateBean relatedBean = bean.getTExportTemplateBean();
            if (relatedBean != null)
            {
                TExportTemplate relatedObject = TExportTemplate.createTExportTemplate(relatedBean, createdObjects);
                result.setTExportTemplate(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TTemplatePerson:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("ReportTemplate = ")
           .append(getReportTemplate())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("RightFlag = ")
           .append(getRightFlag())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
