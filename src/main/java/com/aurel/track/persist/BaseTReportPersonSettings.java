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



import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TExportTemplate;
import com.aurel.track.persist.TExportTemplatePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TReportPersonSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TExportTemplateBean;

import com.aurel.track.beans.TReportParameterBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TReportPersonSettings
 */
public abstract class BaseTReportPersonSettings extends TpBaseObject
{
    /** The Peer class */
    private static final TReportPersonSettingsPeer peer =
        new TReportPersonSettingsPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the person field */
    private Integer person;

    /** The value for the reportTemplate field */
    private Integer reportTemplate;

    /** The value for the paramSettings field */
    private String paramSettings;

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



        // update associated TReportParameter
        if (collTReportParameters != null)
        {
            for (int i = 0; i < collTReportParameters.size(); i++)
            {
                ((TReportParameter) collTReportParameters.get(i))
                        .setReportPersonSettings(v);
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
     * Get the ParamSettings
     *
     * @return String
     */
    public String getParamSettings()
    {
        return paramSettings;
    }


    /**
     * Set the value of ParamSettings
     *
     * @param v new value
     */
    public void setParamSettings(String v) 
    {

        if (!ObjectUtils.equals(this.paramSettings, v))
        {
            this.paramSettings = v;
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
   


    /**
     * Collection to store aggregation of collTReportParameters
     */
    protected List<TReportParameter> collTReportParameters;

    /**
     * Temporary storage of collTReportParameters to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTReportParameters()
    {
        if (collTReportParameters == null)
        {
            collTReportParameters = new ArrayList<TReportParameter>();
        }
    }


    /**
     * Method called to associate a TReportParameter object to this object
     * through the TReportParameter foreign key attribute
     *
     * @param l TReportParameter
     * @throws TorqueException
     */
    public void addTReportParameter(TReportParameter l) throws TorqueException
    {
        getTReportParameters().add(l);
        l.setTReportPersonSettings((TReportPersonSettings) this);
    }

    /**
     * Method called to associate a TReportParameter object to this object
     * through the TReportParameter foreign key attribute using connection.
     *
     * @param l TReportParameter
     * @throws TorqueException
     */
    public void addTReportParameter(TReportParameter l, Connection con) throws TorqueException
    {
        getTReportParameters(con).add(l);
        l.setTReportPersonSettings((TReportPersonSettings) this);
    }

    /**
     * The criteria used to select the current contents of collTReportParameters
     */
    private Criteria lastTReportParametersCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportParameters(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TReportParameter> getTReportParameters()
        throws TorqueException
    {
        if (collTReportParameters == null)
        {
            collTReportParameters = getTReportParameters(new Criteria(10));
        }
        return collTReportParameters;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TReportPersonSettings has previously
     * been saved, it will retrieve related TReportParameters from storage.
     * If this TReportPersonSettings is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TReportParameter> getTReportParameters(Criteria criteria) throws TorqueException
    {
        if (collTReportParameters == null)
        {
            if (isNew())
            {
               collTReportParameters = new ArrayList<TReportParameter>();
            }
            else
            {
                criteria.add(TReportParameterPeer.REPORTPERSONSETTINGS, getObjectID() );
                collTReportParameters = TReportParameterPeer.doSelect(criteria);
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
                criteria.add(TReportParameterPeer.REPORTPERSONSETTINGS, getObjectID());
                if (!lastTReportParametersCriteria.equals(criteria))
                {
                    collTReportParameters = TReportParameterPeer.doSelect(criteria);
                }
            }
        }
        lastTReportParametersCriteria = criteria;

        return collTReportParameters;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportParameters(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportParameter> getTReportParameters(Connection con) throws TorqueException
    {
        if (collTReportParameters == null)
        {
            collTReportParameters = getTReportParameters(new Criteria(10), con);
        }
        return collTReportParameters;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TReportPersonSettings has previously
     * been saved, it will retrieve related TReportParameters from storage.
     * If this TReportPersonSettings is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportParameter> getTReportParameters(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTReportParameters == null)
        {
            if (isNew())
            {
               collTReportParameters = new ArrayList<TReportParameter>();
            }
            else
            {
                 criteria.add(TReportParameterPeer.REPORTPERSONSETTINGS, getObjectID());
                 collTReportParameters = TReportParameterPeer.doSelect(criteria, con);
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
                 criteria.add(TReportParameterPeer.REPORTPERSONSETTINGS, getObjectID());
                 if (!lastTReportParametersCriteria.equals(criteria))
                 {
                     collTReportParameters = TReportParameterPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTReportParametersCriteria = criteria;

         return collTReportParameters;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TReportPersonSettings is new, it will return
     * an empty collection; or if this TReportPersonSettings has previously
     * been saved, it will retrieve related TReportParameters from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TReportPersonSettings.
     */
    protected List<TReportParameter> getTReportParametersJoinTReportPersonSettings(Criteria criteria)
        throws TorqueException
    {
        if (collTReportParameters == null)
        {
            if (isNew())
            {
               collTReportParameters = new ArrayList<TReportParameter>();
            }
            else
            {
                criteria.add(TReportParameterPeer.REPORTPERSONSETTINGS, getObjectID());
                collTReportParameters = TReportParameterPeer.doSelectJoinTReportPersonSettings(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportParameterPeer.REPORTPERSONSETTINGS, getObjectID());
            if (!lastTReportParametersCriteria.equals(criteria))
            {
                collTReportParameters = TReportParameterPeer.doSelectJoinTReportPersonSettings(criteria);
            }
        }
        lastTReportParametersCriteria = criteria;

        return collTReportParameters;
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
            fieldNames.add("ReportTemplate");
            fieldNames.add("ParamSettings");
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
        if (name.equals("ReportTemplate"))
        {
            return getReportTemplate();
        }
        if (name.equals("ParamSettings"))
        {
            return getParamSettings();
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
        if (name.equals("ParamSettings"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParamSettings((String) value);
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
        if (name.equals(TReportPersonSettingsPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TReportPersonSettingsPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TReportPersonSettingsPeer.REPORTTEMPLATE))
        {
            return getReportTemplate();
        }
        if (name.equals(TReportPersonSettingsPeer.PARAMSETTINGS))
        {
            return getParamSettings();
        }
        if (name.equals(TReportPersonSettingsPeer.TPUUID))
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
      if (TReportPersonSettingsPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TReportPersonSettingsPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TReportPersonSettingsPeer.REPORTTEMPLATE.equals(name))
        {
            return setByName("ReportTemplate", value);
        }
      if (TReportPersonSettingsPeer.PARAMSETTINGS.equals(name))
        {
            return setByName("ParamSettings", value);
        }
      if (TReportPersonSettingsPeer.TPUUID.equals(name))
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
            return getReportTemplate();
        }
        if (pos == 3)
        {
            return getParamSettings();
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
            return setByName("ReportTemplate", value);
        }
    if (position == 3)
        {
            return setByName("ParamSettings", value);
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
        save(TReportPersonSettingsPeer.DATABASE_NAME);
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
                    TReportPersonSettingsPeer.doInsert((TReportPersonSettings) this, con);
                    setNew(false);
                }
                else
                {
                    TReportPersonSettingsPeer.doUpdate((TReportPersonSettings) this, con);
                }
            }


            if (collTReportParameters != null)
            {
                for (int i = 0; i < collTReportParameters.size(); i++)
                {
                    ((TReportParameter) collTReportParameters.get(i)).save(con);
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
    public TReportPersonSettings copy() throws TorqueException
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
    public TReportPersonSettings copy(Connection con) throws TorqueException
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
    public TReportPersonSettings copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TReportPersonSettings(), deepcopy);
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
    public TReportPersonSettings copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TReportPersonSettings(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TReportPersonSettings copyInto(TReportPersonSettings copyObj) throws TorqueException
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
    protected TReportPersonSettings copyInto(TReportPersonSettings copyObj, Connection con) throws TorqueException
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
    protected TReportPersonSettings copyInto(TReportPersonSettings copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setReportTemplate(reportTemplate);
        copyObj.setParamSettings(paramSettings);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TReportParameter> vTReportParameters = getTReportParameters();
        if (vTReportParameters != null)
        {
            for (int i = 0; i < vTReportParameters.size(); i++)
            {
                TReportParameter obj =  vTReportParameters.get(i);
                copyObj.addTReportParameter(obj.copy());
            }
        }
        else
        {
            copyObj.collTReportParameters = null;
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
    protected TReportPersonSettings copyInto(TReportPersonSettings copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setReportTemplate(reportTemplate);
        copyObj.setParamSettings(paramSettings);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TReportParameter> vTReportParameters = getTReportParameters(con);
        if (vTReportParameters != null)
        {
            for (int i = 0; i < vTReportParameters.size(); i++)
            {
                TReportParameter obj =  vTReportParameters.get(i);
                copyObj.addTReportParameter(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTReportParameters = null;
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
    public TReportPersonSettingsPeer getPeer()
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
        return TReportPersonSettingsPeer.getTableMap();
    }

  
    /**
     * Creates a TReportPersonSettingsBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TReportPersonSettingsBean with the contents of this object
     */
    public TReportPersonSettingsBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TReportPersonSettingsBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TReportPersonSettingsBean with the contents of this object
     */
    public TReportPersonSettingsBean getBean(IdentityMap createdBeans)
    {
        TReportPersonSettingsBean result = (TReportPersonSettingsBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TReportPersonSettingsBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setPerson(getPerson());
        result.setReportTemplate(getReportTemplate());
        result.setParamSettings(getParamSettings());
        result.setUuid(getUuid());



        if (collTReportParameters != null)
        {
            List<TReportParameterBean> relatedBeans = new ArrayList<TReportParameterBean>(collTReportParameters.size());
            for (Iterator<TReportParameter> collTReportParametersIt = collTReportParameters.iterator(); collTReportParametersIt.hasNext(); )
            {
                TReportParameter related = (TReportParameter) collTReportParametersIt.next();
                TReportParameterBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTReportParameterBeans(relatedBeans);
        }




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
     * Creates an instance of TReportPersonSettings with the contents
     * of a TReportPersonSettingsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TReportPersonSettingsBean which contents are used to create
     *        the resulting class
     * @return an instance of TReportPersonSettings with the contents of bean
     */
    public static TReportPersonSettings createTReportPersonSettings(TReportPersonSettingsBean bean)
        throws TorqueException
    {
        return createTReportPersonSettings(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TReportPersonSettings with the contents
     * of a TReportPersonSettingsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TReportPersonSettingsBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TReportPersonSettings with the contents of bean
     */

    public static TReportPersonSettings createTReportPersonSettings(TReportPersonSettingsBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TReportPersonSettings result = (TReportPersonSettings) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TReportPersonSettings();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setPerson(bean.getPerson());
        result.setReportTemplate(bean.getReportTemplate());
        result.setParamSettings(bean.getParamSettings());
        result.setUuid(bean.getUuid());



        {
            List<TReportParameterBean> relatedBeans = bean.getTReportParameterBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TReportParameterBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TReportParameterBean relatedBean =  relatedBeansIt.next();
                    TReportParameter related = TReportParameter.createTReportParameter(relatedBean, createdObjects);
                    result.addTReportParameterFromBean(related);
                }
            }
        }




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



    /**
     * Method called to associate a TReportParameter object to this object.
     * through the TReportParameter foreign key attribute
     *
     * @param toAdd TReportParameter
     */
    protected void addTReportParameterFromBean(TReportParameter toAdd)
    {
        initTReportParameters();
        collTReportParameters.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TReportPersonSettings:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("ReportTemplate = ")
           .append(getReportTemplate())
           .append("\n");
        str.append("ParamSettings = ")
           .append(getParamSettings())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
