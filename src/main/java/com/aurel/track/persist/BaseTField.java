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



import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TProjectType;
import com.aurel.track.persist.TProjectTypePeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TProjectBean;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TScreenFieldBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TField
 */
public abstract class BaseTField extends TpBaseObject
{
    /** The Peer class */
    private static final TFieldPeer peer =
        new TFieldPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the fieldType field */
    private String fieldType;

    /** The value for the deprecated field */
    private String deprecated = "N";

    /** The value for the isCustom field */
    private String isCustom = "Y";

    /** The value for the required field */
    private String required = "N";

    /** The value for the filterField field */
    private String filterField = "N";

    /** The value for the description field */
    private String description;

    /** The value for the owner field */
    private Integer owner;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

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



        // update associated TFieldConfig
        if (collTFieldConfigs != null)
        {
            for (int i = 0; i < collTFieldConfigs.size(); i++)
            {
                ((TFieldConfig) collTFieldConfigs.get(i))
                        .setField(v);
            }
        }

        // update associated TAttributeValue
        if (collTAttributeValues != null)
        {
            for (int i = 0; i < collTAttributeValues.size(); i++)
            {
                ((TAttributeValue) collTAttributeValues.get(i))
                        .setField(v);
            }
        }

        // update associated TScreenField
        if (collTScreenFields != null)
        {
            for (int i = 0; i < collTScreenFields.size(); i++)
            {
                ((TScreenField) collTScreenFields.get(i))
                        .setField(v);
            }
        }
    }

    /**
     * Get the Name
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set the value of Name
     *
     * @param v new value
     */
    public void setName(String v) 
    {

        if (!ObjectUtils.equals(this.name, v))
        {
            this.name = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldType
     *
     * @return String
     */
    public String getFieldType()
    {
        return fieldType;
    }


    /**
     * Set the value of FieldType
     *
     * @param v new value
     */
    public void setFieldType(String v) 
    {

        if (!ObjectUtils.equals(this.fieldType, v))
        {
            this.fieldType = v;
            setModified(true);
        }


    }

    /**
     * Get the Deprecated
     *
     * @return String
     */
    public String getDeprecated()
    {
        return deprecated;
    }


    /**
     * Set the value of Deprecated
     *
     * @param v new value
     */
    public void setDeprecated(String v) 
    {

        if (!ObjectUtils.equals(this.deprecated, v))
        {
            this.deprecated = v;
            setModified(true);
        }


    }

    /**
     * Get the IsCustom
     *
     * @return String
     */
    public String getIsCustom()
    {
        return isCustom;
    }


    /**
     * Set the value of IsCustom
     *
     * @param v new value
     */
    public void setIsCustom(String v) 
    {

        if (!ObjectUtils.equals(this.isCustom, v))
        {
            this.isCustom = v;
            setModified(true);
        }


    }

    /**
     * Get the Required
     *
     * @return String
     */
    public String getRequired()
    {
        return required;
    }


    /**
     * Set the value of Required
     *
     * @param v new value
     */
    public void setRequired(String v) 
    {

        if (!ObjectUtils.equals(this.required, v))
        {
            this.required = v;
            setModified(true);
        }


    }

    /**
     * Get the FilterField
     *
     * @return String
     */
    public String getFilterField()
    {
        return filterField;
    }


    /**
     * Set the value of FilterField
     *
     * @param v new value
     */
    public void setFilterField(String v) 
    {

        if (!ObjectUtils.equals(this.filterField, v))
        {
            this.filterField = v;
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
     * Get the Owner
     *
     * @return Integer
     */
    public Integer getOwner()
    {
        return owner;
    }


    /**
     * Set the value of Owner
     *
     * @param v new value
     */
    public void setOwner(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.owner, v))
        {
            this.owner = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the ProjectType
     *
     * @return Integer
     */
    public Integer getProjectType()
    {
        return projectType;
    }


    /**
     * Set the value of ProjectType
     *
     * @param v new value
     */
    public void setProjectType(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.projectType, v))
        {
            this.projectType = v;
            setModified(true);
        }


        if (aTProjectType != null && !ObjectUtils.equals(aTProjectType.getObjectID(), v))
        {
            aTProjectType = null;
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
            setOwner((Integer) null);
        }
        else
        {
            setOwner(v.getObjectID());
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
        if (aTPerson == null && (!ObjectUtils.equals(this.owner, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.owner));
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
        if (aTPerson == null && (!ObjectUtils.equals(this.owner, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.owner), connection);
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

        setOwner(new Integer(((NumberKey) key).intValue()));
    }




    private TProjectType aTProjectType;

    /**
     * Declares an association between this object and a TProjectType object
     *
     * @param v TProjectType
     * @throws TorqueException
     */
    public void setTProjectType(TProjectType v) throws TorqueException
    {
        if (v == null)
        {
            setProjectType((Integer) null);
        }
        else
        {
            setProjectType(v.getObjectID());
        }
        aTProjectType = v;
    }


    /**
     * Returns the associated TProjectType object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProjectType object
     * @throws TorqueException
     */
    public TProjectType getTProjectType()
        throws TorqueException
    {
        if (aTProjectType == null && (!ObjectUtils.equals(this.projectType, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projectType));
        }
        return aTProjectType;
    }

    /**
     * Return the associated TProjectType object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProjectType object
     * @throws TorqueException
     */
    public TProjectType getTProjectType(Connection connection)
        throws TorqueException
    {
        if (aTProjectType == null && (!ObjectUtils.equals(this.projectType, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projectType), connection);
        }
        return aTProjectType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectTypeKey(ObjectKey key) throws TorqueException
    {

        setProjectType(new Integer(((NumberKey) key).intValue()));
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
   


    /**
     * Collection to store aggregation of collTFieldConfigs
     */
    protected List<TFieldConfig> collTFieldConfigs;

    /**
     * Temporary storage of collTFieldConfigs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTFieldConfigs()
    {
        if (collTFieldConfigs == null)
        {
            collTFieldConfigs = new ArrayList<TFieldConfig>();
        }
    }


    /**
     * Method called to associate a TFieldConfig object to this object
     * through the TFieldConfig foreign key attribute
     *
     * @param l TFieldConfig
     * @throws TorqueException
     */
    public void addTFieldConfig(TFieldConfig l) throws TorqueException
    {
        getTFieldConfigs().add(l);
        l.setTField((TField) this);
    }

    /**
     * Method called to associate a TFieldConfig object to this object
     * through the TFieldConfig foreign key attribute using connection.
     *
     * @param l TFieldConfig
     * @throws TorqueException
     */
    public void addTFieldConfig(TFieldConfig l, Connection con) throws TorqueException
    {
        getTFieldConfigs(con).add(l);
        l.setTField((TField) this);
    }

    /**
     * The criteria used to select the current contents of collTFieldConfigs
     */
    private Criteria lastTFieldConfigsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldConfigs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TFieldConfig> getTFieldConfigs()
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            collTFieldConfigs = getTFieldConfigs(new Criteria(10));
        }
        return collTFieldConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     * If this TField is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TFieldConfig> getTFieldConfigs(Criteria criteria) throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID() );
                collTFieldConfigs = TFieldConfigPeer.doSelect(criteria);
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
                criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
                if (!lastTFieldConfigsCriteria.equals(criteria))
                {
                    collTFieldConfigs = TFieldConfigPeer.doSelect(criteria);
                }
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldConfigs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldConfig> getTFieldConfigs(Connection con) throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            collTFieldConfigs = getTFieldConfigs(new Criteria(10), con);
        }
        return collTFieldConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     * If this TField is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldConfig> getTFieldConfigs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                 criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
                 collTFieldConfigs = TFieldConfigPeer.doSelect(criteria, con);
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
                 criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
                 if (!lastTFieldConfigsCriteria.equals(criteria))
                 {
                     collTFieldConfigs = TFieldConfigPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTFieldConfigsCriteria = criteria;

         return collTFieldConfigs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField is new, it will return
     * an empty collection; or if this TField has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TField.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTField(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTField(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField is new, it will return
     * an empty collection; or if this TField has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TField.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField is new, it will return
     * an empty collection; or if this TField has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TField.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField is new, it will return
     * an empty collection; or if this TField has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TField.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField is new, it will return
     * an empty collection; or if this TField has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TField.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.FIELDKEY, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }





    /**
     * Collection to store aggregation of collTAttributeValues
     */
    protected List<TAttributeValue> collTAttributeValues;

    /**
     * Temporary storage of collTAttributeValues to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAttributeValues()
    {
        if (collTAttributeValues == null)
        {
            collTAttributeValues = new ArrayList<TAttributeValue>();
        }
    }


    /**
     * Method called to associate a TAttributeValue object to this object
     * through the TAttributeValue foreign key attribute
     *
     * @param l TAttributeValue
     * @throws TorqueException
     */
    public void addTAttributeValue(TAttributeValue l) throws TorqueException
    {
        getTAttributeValues().add(l);
        l.setTField((TField) this);
    }

    /**
     * Method called to associate a TAttributeValue object to this object
     * through the TAttributeValue foreign key attribute using connection.
     *
     * @param l TAttributeValue
     * @throws TorqueException
     */
    public void addTAttributeValue(TAttributeValue l, Connection con) throws TorqueException
    {
        getTAttributeValues(con).add(l);
        l.setTField((TField) this);
    }

    /**
     * The criteria used to select the current contents of collTAttributeValues
     */
    private Criteria lastTAttributeValuesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttributeValues(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAttributeValue> getTAttributeValues()
        throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            collTAttributeValues = getTAttributeValues(new Criteria(10));
        }
        return collTAttributeValues;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField has previously
     * been saved, it will retrieve related TAttributeValues from storage.
     * If this TField is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAttributeValue> getTAttributeValues(Criteria criteria) throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            if (isNew())
            {
               collTAttributeValues = new ArrayList<TAttributeValue>();
            }
            else
            {
                criteria.add(TAttributeValuePeer.FIELDKEY, getObjectID() );
                collTAttributeValues = TAttributeValuePeer.doSelect(criteria);
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
                criteria.add(TAttributeValuePeer.FIELDKEY, getObjectID());
                if (!lastTAttributeValuesCriteria.equals(criteria))
                {
                    collTAttributeValues = TAttributeValuePeer.doSelect(criteria);
                }
            }
        }
        lastTAttributeValuesCriteria = criteria;

        return collTAttributeValues;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttributeValues(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttributeValue> getTAttributeValues(Connection con) throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            collTAttributeValues = getTAttributeValues(new Criteria(10), con);
        }
        return collTAttributeValues;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField has previously
     * been saved, it will retrieve related TAttributeValues from storage.
     * If this TField is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttributeValue> getTAttributeValues(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            if (isNew())
            {
               collTAttributeValues = new ArrayList<TAttributeValue>();
            }
            else
            {
                 criteria.add(TAttributeValuePeer.FIELDKEY, getObjectID());
                 collTAttributeValues = TAttributeValuePeer.doSelect(criteria, con);
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
                 criteria.add(TAttributeValuePeer.FIELDKEY, getObjectID());
                 if (!lastTAttributeValuesCriteria.equals(criteria))
                 {
                     collTAttributeValues = TAttributeValuePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAttributeValuesCriteria = criteria;

         return collTAttributeValues;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField is new, it will return
     * an empty collection; or if this TField has previously
     * been saved, it will retrieve related TAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TField.
     */
    protected List<TAttributeValue> getTAttributeValuesJoinTField(Criteria criteria)
        throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            if (isNew())
            {
               collTAttributeValues = new ArrayList<TAttributeValue>();
            }
            else
            {
                criteria.add(TAttributeValuePeer.FIELDKEY, getObjectID());
                collTAttributeValues = TAttributeValuePeer.doSelectJoinTField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttributeValuePeer.FIELDKEY, getObjectID());
            if (!lastTAttributeValuesCriteria.equals(criteria))
            {
                collTAttributeValues = TAttributeValuePeer.doSelectJoinTField(criteria);
            }
        }
        lastTAttributeValuesCriteria = criteria;

        return collTAttributeValues;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField is new, it will return
     * an empty collection; or if this TField has previously
     * been saved, it will retrieve related TAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TField.
     */
    protected List<TAttributeValue> getTAttributeValuesJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            if (isNew())
            {
               collTAttributeValues = new ArrayList<TAttributeValue>();
            }
            else
            {
                criteria.add(TAttributeValuePeer.FIELDKEY, getObjectID());
                collTAttributeValues = TAttributeValuePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttributeValuePeer.FIELDKEY, getObjectID());
            if (!lastTAttributeValuesCriteria.equals(criteria))
            {
                collTAttributeValues = TAttributeValuePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTAttributeValuesCriteria = criteria;

        return collTAttributeValues;
    }





    /**
     * Collection to store aggregation of collTScreenFields
     */
    protected List<TScreenField> collTScreenFields;

    /**
     * Temporary storage of collTScreenFields to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScreenFields()
    {
        if (collTScreenFields == null)
        {
            collTScreenFields = new ArrayList<TScreenField>();
        }
    }


    /**
     * Method called to associate a TScreenField object to this object
     * through the TScreenField foreign key attribute
     *
     * @param l TScreenField
     * @throws TorqueException
     */
    public void addTScreenField(TScreenField l) throws TorqueException
    {
        getTScreenFields().add(l);
        l.setTField((TField) this);
    }

    /**
     * Method called to associate a TScreenField object to this object
     * through the TScreenField foreign key attribute using connection.
     *
     * @param l TScreenField
     * @throws TorqueException
     */
    public void addTScreenField(TScreenField l, Connection con) throws TorqueException
    {
        getTScreenFields(con).add(l);
        l.setTField((TField) this);
    }

    /**
     * The criteria used to select the current contents of collTScreenFields
     */
    private Criteria lastTScreenFieldsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenFields(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScreenField> getTScreenFields()
        throws TorqueException
    {
        if (collTScreenFields == null)
        {
            collTScreenFields = getTScreenFields(new Criteria(10));
        }
        return collTScreenFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField has previously
     * been saved, it will retrieve related TScreenFields from storage.
     * If this TField is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScreenField> getTScreenFields(Criteria criteria) throws TorqueException
    {
        if (collTScreenFields == null)
        {
            if (isNew())
            {
               collTScreenFields = new ArrayList<TScreenField>();
            }
            else
            {
                criteria.add(TScreenFieldPeer.FIELDKEY, getObjectID() );
                collTScreenFields = TScreenFieldPeer.doSelect(criteria);
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
                criteria.add(TScreenFieldPeer.FIELDKEY, getObjectID());
                if (!lastTScreenFieldsCriteria.equals(criteria))
                {
                    collTScreenFields = TScreenFieldPeer.doSelect(criteria);
                }
            }
        }
        lastTScreenFieldsCriteria = criteria;

        return collTScreenFields;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenFields(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenField> getTScreenFields(Connection con) throws TorqueException
    {
        if (collTScreenFields == null)
        {
            collTScreenFields = getTScreenFields(new Criteria(10), con);
        }
        return collTScreenFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField has previously
     * been saved, it will retrieve related TScreenFields from storage.
     * If this TField is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenField> getTScreenFields(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScreenFields == null)
        {
            if (isNew())
            {
               collTScreenFields = new ArrayList<TScreenField>();
            }
            else
            {
                 criteria.add(TScreenFieldPeer.FIELDKEY, getObjectID());
                 collTScreenFields = TScreenFieldPeer.doSelect(criteria, con);
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
                 criteria.add(TScreenFieldPeer.FIELDKEY, getObjectID());
                 if (!lastTScreenFieldsCriteria.equals(criteria))
                 {
                     collTScreenFields = TScreenFieldPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScreenFieldsCriteria = criteria;

         return collTScreenFields;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField is new, it will return
     * an empty collection; or if this TField has previously
     * been saved, it will retrieve related TScreenFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TField.
     */
    protected List<TScreenField> getTScreenFieldsJoinTScreenPanel(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenFields == null)
        {
            if (isNew())
            {
               collTScreenFields = new ArrayList<TScreenField>();
            }
            else
            {
                criteria.add(TScreenFieldPeer.FIELDKEY, getObjectID());
                collTScreenFields = TScreenFieldPeer.doSelectJoinTScreenPanel(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenFieldPeer.FIELDKEY, getObjectID());
            if (!lastTScreenFieldsCriteria.equals(criteria))
            {
                collTScreenFields = TScreenFieldPeer.doSelectJoinTScreenPanel(criteria);
            }
        }
        lastTScreenFieldsCriteria = criteria;

        return collTScreenFields;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TField is new, it will return
     * an empty collection; or if this TField has previously
     * been saved, it will retrieve related TScreenFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TField.
     */
    protected List<TScreenField> getTScreenFieldsJoinTField(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenFields == null)
        {
            if (isNew())
            {
               collTScreenFields = new ArrayList<TScreenField>();
            }
            else
            {
                criteria.add(TScreenFieldPeer.FIELDKEY, getObjectID());
                collTScreenFields = TScreenFieldPeer.doSelectJoinTField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenFieldPeer.FIELDKEY, getObjectID());
            if (!lastTScreenFieldsCriteria.equals(criteria))
            {
                collTScreenFields = TScreenFieldPeer.doSelectJoinTField(criteria);
            }
        }
        lastTScreenFieldsCriteria = criteria;

        return collTScreenFields;
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
            fieldNames.add("Name");
            fieldNames.add("FieldType");
            fieldNames.add("Deprecated");
            fieldNames.add("IsCustom");
            fieldNames.add("Required");
            fieldNames.add("FilterField");
            fieldNames.add("Description");
            fieldNames.add("Owner");
            fieldNames.add("ProjectType");
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
        if (name.equals("ObjectID"))
        {
            return getObjectID();
        }
        if (name.equals("Name"))
        {
            return getName();
        }
        if (name.equals("FieldType"))
        {
            return getFieldType();
        }
        if (name.equals("Deprecated"))
        {
            return getDeprecated();
        }
        if (name.equals("IsCustom"))
        {
            return getIsCustom();
        }
        if (name.equals("Required"))
        {
            return getRequired();
        }
        if (name.equals("FilterField"))
        {
            return getFilterField();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("Owner"))
        {
            return getOwner();
        }
        if (name.equals("ProjectType"))
        {
            return getProjectType();
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
        if (name.equals("Name"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setName((String) value);
            return true;
        }
        if (name.equals("FieldType"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldType((String) value);
            return true;
        }
        if (name.equals("Deprecated"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDeprecated((String) value);
            return true;
        }
        if (name.equals("IsCustom"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsCustom((String) value);
            return true;
        }
        if (name.equals("Required"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRequired((String) value);
            return true;
        }
        if (name.equals("FilterField"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFilterField((String) value);
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
        if (name.equals("Owner"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOwner((Integer) value);
            return true;
        }
        if (name.equals("ProjectType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProjectType((Integer) value);
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
        if (name.equals(TFieldPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TFieldPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TFieldPeer.FIELDTYPE))
        {
            return getFieldType();
        }
        if (name.equals(TFieldPeer.DEPRECATED))
        {
            return getDeprecated();
        }
        if (name.equals(TFieldPeer.ISCUSTOM))
        {
            return getIsCustom();
        }
        if (name.equals(TFieldPeer.REQUIRED))
        {
            return getRequired();
        }
        if (name.equals(TFieldPeer.FILTERFIELD))
        {
            return getFilterField();
        }
        if (name.equals(TFieldPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TFieldPeer.OWNER))
        {
            return getOwner();
        }
        if (name.equals(TFieldPeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TFieldPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TFieldPeer.TPUUID))
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
      if (TFieldPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TFieldPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TFieldPeer.FIELDTYPE.equals(name))
        {
            return setByName("FieldType", value);
        }
      if (TFieldPeer.DEPRECATED.equals(name))
        {
            return setByName("Deprecated", value);
        }
      if (TFieldPeer.ISCUSTOM.equals(name))
        {
            return setByName("IsCustom", value);
        }
      if (TFieldPeer.REQUIRED.equals(name))
        {
            return setByName("Required", value);
        }
      if (TFieldPeer.FILTERFIELD.equals(name))
        {
            return setByName("FilterField", value);
        }
      if (TFieldPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TFieldPeer.OWNER.equals(name))
        {
            return setByName("Owner", value);
        }
      if (TFieldPeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TFieldPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TFieldPeer.TPUUID.equals(name))
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
            return getName();
        }
        if (pos == 2)
        {
            return getFieldType();
        }
        if (pos == 3)
        {
            return getDeprecated();
        }
        if (pos == 4)
        {
            return getIsCustom();
        }
        if (pos == 5)
        {
            return getRequired();
        }
        if (pos == 6)
        {
            return getFilterField();
        }
        if (pos == 7)
        {
            return getDescription();
        }
        if (pos == 8)
        {
            return getOwner();
        }
        if (pos == 9)
        {
            return getProjectType();
        }
        if (pos == 10)
        {
            return getProject();
        }
        if (pos == 11)
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
            return setByName("Name", value);
        }
    if (position == 2)
        {
            return setByName("FieldType", value);
        }
    if (position == 3)
        {
            return setByName("Deprecated", value);
        }
    if (position == 4)
        {
            return setByName("IsCustom", value);
        }
    if (position == 5)
        {
            return setByName("Required", value);
        }
    if (position == 6)
        {
            return setByName("FilterField", value);
        }
    if (position == 7)
        {
            return setByName("Description", value);
        }
    if (position == 8)
        {
            return setByName("Owner", value);
        }
    if (position == 9)
        {
            return setByName("ProjectType", value);
        }
    if (position == 10)
        {
            return setByName("Project", value);
        }
    if (position == 11)
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
        save(TFieldPeer.DATABASE_NAME);
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
                    TFieldPeer.doInsert((TField) this, con);
                    setNew(false);
                }
                else
                {
                    TFieldPeer.doUpdate((TField) this, con);
                }
            }


            if (collTFieldConfigs != null)
            {
                for (int i = 0; i < collTFieldConfigs.size(); i++)
                {
                    ((TFieldConfig) collTFieldConfigs.get(i)).save(con);
                }
            }

            if (collTAttributeValues != null)
            {
                for (int i = 0; i < collTAttributeValues.size(); i++)
                {
                    ((TAttributeValue) collTAttributeValues.get(i)).save(con);
                }
            }

            if (collTScreenFields != null)
            {
                for (int i = 0; i < collTScreenFields.size(); i++)
                {
                    ((TScreenField) collTScreenFields.get(i)).save(con);
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
    public TField copy() throws TorqueException
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
    public TField copy(Connection con) throws TorqueException
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
    public TField copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TField(), deepcopy);
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
    public TField copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TField(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TField copyInto(TField copyObj) throws TorqueException
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
    protected TField copyInto(TField copyObj, Connection con) throws TorqueException
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
    protected TField copyInto(TField copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setFieldType(fieldType);
        copyObj.setDeprecated(deprecated);
        copyObj.setIsCustom(isCustom);
        copyObj.setRequired(required);
        copyObj.setFilterField(filterField);
        copyObj.setDescription(description);
        copyObj.setOwner(owner);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TFieldConfig> vTFieldConfigs = getTFieldConfigs();
        if (vTFieldConfigs != null)
        {
            for (int i = 0; i < vTFieldConfigs.size(); i++)
            {
                TFieldConfig obj =  vTFieldConfigs.get(i);
                copyObj.addTFieldConfig(obj.copy());
            }
        }
        else
        {
            copyObj.collTFieldConfigs = null;
        }


        List<TAttributeValue> vTAttributeValues = getTAttributeValues();
        if (vTAttributeValues != null)
        {
            for (int i = 0; i < vTAttributeValues.size(); i++)
            {
                TAttributeValue obj =  vTAttributeValues.get(i);
                copyObj.addTAttributeValue(obj.copy());
            }
        }
        else
        {
            copyObj.collTAttributeValues = null;
        }


        List<TScreenField> vTScreenFields = getTScreenFields();
        if (vTScreenFields != null)
        {
            for (int i = 0; i < vTScreenFields.size(); i++)
            {
                TScreenField obj =  vTScreenFields.get(i);
                copyObj.addTScreenField(obj.copy());
            }
        }
        else
        {
            copyObj.collTScreenFields = null;
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
    protected TField copyInto(TField copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setFieldType(fieldType);
        copyObj.setDeprecated(deprecated);
        copyObj.setIsCustom(isCustom);
        copyObj.setRequired(required);
        copyObj.setFilterField(filterField);
        copyObj.setDescription(description);
        copyObj.setOwner(owner);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TFieldConfig> vTFieldConfigs = getTFieldConfigs(con);
        if (vTFieldConfigs != null)
        {
            for (int i = 0; i < vTFieldConfigs.size(); i++)
            {
                TFieldConfig obj =  vTFieldConfigs.get(i);
                copyObj.addTFieldConfig(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTFieldConfigs = null;
        }


        List<TAttributeValue> vTAttributeValues = getTAttributeValues(con);
        if (vTAttributeValues != null)
        {
            for (int i = 0; i < vTAttributeValues.size(); i++)
            {
                TAttributeValue obj =  vTAttributeValues.get(i);
                copyObj.addTAttributeValue(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAttributeValues = null;
        }


        List<TScreenField> vTScreenFields = getTScreenFields(con);
        if (vTScreenFields != null)
        {
            for (int i = 0; i < vTScreenFields.size(); i++)
            {
                TScreenField obj =  vTScreenFields.get(i);
                copyObj.addTScreenField(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScreenFields = null;
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
    public TFieldPeer getPeer()
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
        return TFieldPeer.getTableMap();
    }

  
    /**
     * Creates a TFieldBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TFieldBean with the contents of this object
     */
    public TFieldBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TFieldBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TFieldBean with the contents of this object
     */
    public TFieldBean getBean(IdentityMap createdBeans)
    {
        TFieldBean result = (TFieldBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TFieldBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setFieldType(getFieldType());
        result.setDeprecated(getDeprecated());
        result.setIsCustom(getIsCustom());
        result.setRequired(getRequired());
        result.setFilterField(getFilterField());
        result.setDescription(getDescription());
        result.setOwner(getOwner());
        result.setProjectType(getProjectType());
        result.setProject(getProject());
        result.setUuid(getUuid());



        if (collTFieldConfigs != null)
        {
            List<TFieldConfigBean> relatedBeans = new ArrayList<TFieldConfigBean>(collTFieldConfigs.size());
            for (Iterator<TFieldConfig> collTFieldConfigsIt = collTFieldConfigs.iterator(); collTFieldConfigsIt.hasNext(); )
            {
                TFieldConfig related = (TFieldConfig) collTFieldConfigsIt.next();
                TFieldConfigBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTFieldConfigBeans(relatedBeans);
        }


        if (collTAttributeValues != null)
        {
            List<TAttributeValueBean> relatedBeans = new ArrayList<TAttributeValueBean>(collTAttributeValues.size());
            for (Iterator<TAttributeValue> collTAttributeValuesIt = collTAttributeValues.iterator(); collTAttributeValuesIt.hasNext(); )
            {
                TAttributeValue related = (TAttributeValue) collTAttributeValuesIt.next();
                TAttributeValueBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAttributeValueBeans(relatedBeans);
        }


        if (collTScreenFields != null)
        {
            List<TScreenFieldBean> relatedBeans = new ArrayList<TScreenFieldBean>(collTScreenFields.size());
            for (Iterator<TScreenField> collTScreenFieldsIt = collTScreenFields.iterator(); collTScreenFieldsIt.hasNext(); )
            {
                TScreenField related = (TScreenField) collTScreenFieldsIt.next();
                TScreenFieldBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScreenFieldBeans(relatedBeans);
        }




        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTProjectType != null)
        {
            TProjectTypeBean relatedBean = aTProjectType.getBean(createdBeans);
            result.setTProjectTypeBean(relatedBean);
        }



        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TField with the contents
     * of a TFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TFieldBean which contents are used to create
     *        the resulting class
     * @return an instance of TField with the contents of bean
     */
    public static TField createTField(TFieldBean bean)
        throws TorqueException
    {
        return createTField(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TField with the contents
     * of a TFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TFieldBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TField with the contents of bean
     */

    public static TField createTField(TFieldBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TField result = (TField) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TField();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setFieldType(bean.getFieldType());
        result.setDeprecated(bean.getDeprecated());
        result.setIsCustom(bean.getIsCustom());
        result.setRequired(bean.getRequired());
        result.setFilterField(bean.getFilterField());
        result.setDescription(bean.getDescription());
        result.setOwner(bean.getOwner());
        result.setProjectType(bean.getProjectType());
        result.setProject(bean.getProject());
        result.setUuid(bean.getUuid());



        {
            List<TFieldConfigBean> relatedBeans = bean.getTFieldConfigBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TFieldConfigBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TFieldConfigBean relatedBean =  relatedBeansIt.next();
                    TFieldConfig related = TFieldConfig.createTFieldConfig(relatedBean, createdObjects);
                    result.addTFieldConfigFromBean(related);
                }
            }
        }


        {
            List<TAttributeValueBean> relatedBeans = bean.getTAttributeValueBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAttributeValueBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAttributeValueBean relatedBean =  relatedBeansIt.next();
                    TAttributeValue related = TAttributeValue.createTAttributeValue(relatedBean, createdObjects);
                    result.addTAttributeValueFromBean(related);
                }
            }
        }


        {
            List<TScreenFieldBean> relatedBeans = bean.getTScreenFieldBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScreenFieldBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScreenFieldBean relatedBean =  relatedBeansIt.next();
                    TScreenField related = TScreenField.createTScreenField(relatedBean, createdObjects);
                    result.addTScreenFieldFromBean(related);
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
            TProjectTypeBean relatedBean = bean.getTProjectTypeBean();
            if (relatedBean != null)
            {
                TProjectType relatedObject = TProjectType.createTProjectType(relatedBean, createdObjects);
                result.setTProjectType(relatedObject);
            }
        }



        {
            TProjectBean relatedBean = bean.getTProjectBean();
            if (relatedBean != null)
            {
                TProject relatedObject = TProject.createTProject(relatedBean, createdObjects);
                result.setTProject(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TFieldConfig object to this object.
     * through the TFieldConfig foreign key attribute
     *
     * @param toAdd TFieldConfig
     */
    protected void addTFieldConfigFromBean(TFieldConfig toAdd)
    {
        initTFieldConfigs();
        collTFieldConfigs.add(toAdd);
    }


    /**
     * Method called to associate a TAttributeValue object to this object.
     * through the TAttributeValue foreign key attribute
     *
     * @param toAdd TAttributeValue
     */
    protected void addTAttributeValueFromBean(TAttributeValue toAdd)
    {
        initTAttributeValues();
        collTAttributeValues.add(toAdd);
    }


    /**
     * Method called to associate a TScreenField object to this object.
     * through the TScreenField foreign key attribute
     *
     * @param toAdd TScreenField
     */
    protected void addTScreenFieldFromBean(TScreenField toAdd)
    {
        initTScreenFields();
        collTScreenFields.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TField:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("FieldType = ")
           .append(getFieldType())
           .append("\n");
        str.append("Deprecated = ")
           .append(getDeprecated())
           .append("\n");
        str.append("IsCustom = ")
           .append(getIsCustom())
           .append("\n");
        str.append("Required = ")
           .append(getRequired())
           .append("\n");
        str.append("FilterField = ")
           .append(getFilterField())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Owner = ")
           .append(getOwner())
           .append("\n");
        str.append("ProjectType = ")
           .append(getProjectType())
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
