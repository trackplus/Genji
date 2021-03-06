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
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TReportCategory;
import com.aurel.track.persist.TReportCategoryPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TReportCategoryBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReportCategoryBean;

import com.aurel.track.beans.TExportTemplateBean;


/**
 * Hierarchical categorization of the reports
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TReportCategory
 */
public abstract class BaseTReportCategory extends TpBaseObject
{
    /** The Peer class */
    private static final TReportCategoryPeer peer =
        new TReportCategoryPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the repository field */
    private Integer repository;

    /** The value for the createdBy field */
    private Integer createdBy;

    /** The value for the project field */
    private Integer project;

    /** The value for the parentID field */
    private Integer parentID;

    /** The value for the sortorder field */
    private Integer sortorder;

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



        // update associated TExportTemplate
        if (collTExportTemplates != null)
        {
            for (int i = 0; i < collTExportTemplates.size(); i++)
            {
                ((TExportTemplate) collTExportTemplates.get(i))
                        .setCategoryKey(v);
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
     * Get the Repository
     *
     * @return Integer
     */
    public Integer getRepository()
    {
        return repository;
    }


    /**
     * Set the value of Repository
     *
     * @param v new value
     */
    public void setRepository(Integer v) 
    {

        if (!ObjectUtils.equals(this.repository, v))
        {
            this.repository = v;
            setModified(true);
        }


    }

    /**
     * Get the CreatedBy
     *
     * @return Integer
     */
    public Integer getCreatedBy()
    {
        return createdBy;
    }


    /**
     * Set the value of CreatedBy
     *
     * @param v new value
     */
    public void setCreatedBy(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.createdBy, v))
        {
            this.createdBy = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
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
     * Get the ParentID
     *
     * @return Integer
     */
    public Integer getParentID()
    {
        return parentID;
    }


    /**
     * Set the value of ParentID
     *
     * @param v new value
     */
    public void setParentID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.parentID, v))
        {
            this.parentID = v;
            setModified(true);
        }


        if (aTReportCategoryRelatedByParentID != null && !ObjectUtils.equals(aTReportCategoryRelatedByParentID.getObjectID(), v))
        {
            aTReportCategoryRelatedByParentID = null;
        }

    }

    /**
     * Get the Sortorder
     *
     * @return Integer
     */
    public Integer getSortorder()
    {
        return sortorder;
    }


    /**
     * Set the value of Sortorder
     *
     * @param v new value
     */
    public void setSortorder(Integer v) 
    {

        if (!ObjectUtils.equals(this.sortorder, v))
        {
            this.sortorder = v;
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
            setCreatedBy((Integer) null);
        }
        else
        {
            setCreatedBy(v.getObjectID());
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
        if (aTPerson == null && (!ObjectUtils.equals(this.createdBy, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.createdBy));
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
        if (aTPerson == null && (!ObjectUtils.equals(this.createdBy, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.createdBy), connection);
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

        setCreatedBy(new Integer(((NumberKey) key).intValue()));
    }




    private TReportCategory aTReportCategoryRelatedByParentID;

    /**
     * Declares an association between this object and a TReportCategory object
     *
     * @param v TReportCategory
     * @throws TorqueException
     */
    public void setTReportCategoryRelatedByParentID(TReportCategory v) throws TorqueException
    {
        if (v == null)
        {
            setParentID((Integer) null);
        }
        else
        {
            setParentID(v.getObjectID());
        }
        aTReportCategoryRelatedByParentID = v;
    }


    /**
     * Returns the associated TReportCategory object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TReportCategory object
     * @throws TorqueException
     */
    public TReportCategory getTReportCategoryRelatedByParentID()
        throws TorqueException
    {
        if (aTReportCategoryRelatedByParentID == null && (!ObjectUtils.equals(this.parentID, null)))
        {
            aTReportCategoryRelatedByParentID = TReportCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.parentID));
        }
        return aTReportCategoryRelatedByParentID;
    }

    /**
     * Return the associated TReportCategory object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TReportCategory object
     * @throws TorqueException
     */
    public TReportCategory getTReportCategoryRelatedByParentID(Connection connection)
        throws TorqueException
    {
        if (aTReportCategoryRelatedByParentID == null && (!ObjectUtils.equals(this.parentID, null)))
        {
            aTReportCategoryRelatedByParentID = TReportCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.parentID), connection);
        }
        return aTReportCategoryRelatedByParentID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTReportCategoryRelatedByParentIDKey(ObjectKey key) throws TorqueException
    {

        setParentID(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTExportTemplates
     */
    protected List<TExportTemplate> collTExportTemplates;

    /**
     * Temporary storage of collTExportTemplates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTExportTemplates()
    {
        if (collTExportTemplates == null)
        {
            collTExportTemplates = new ArrayList<TExportTemplate>();
        }
    }


    /**
     * Method called to associate a TExportTemplate object to this object
     * through the TExportTemplate foreign key attribute
     *
     * @param l TExportTemplate
     * @throws TorqueException
     */
    public void addTExportTemplate(TExportTemplate l) throws TorqueException
    {
        getTExportTemplates().add(l);
        l.setTReportCategory((TReportCategory) this);
    }

    /**
     * Method called to associate a TExportTemplate object to this object
     * through the TExportTemplate foreign key attribute using connection.
     *
     * @param l TExportTemplate
     * @throws TorqueException
     */
    public void addTExportTemplate(TExportTemplate l, Connection con) throws TorqueException
    {
        getTExportTemplates(con).add(l);
        l.setTReportCategory((TReportCategory) this);
    }

    /**
     * The criteria used to select the current contents of collTExportTemplates
     */
    private Criteria lastTExportTemplatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTExportTemplates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TExportTemplate> getTExportTemplates()
        throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            collTExportTemplates = getTExportTemplates(new Criteria(10));
        }
        return collTExportTemplates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TReportCategory has previously
     * been saved, it will retrieve related TExportTemplates from storage.
     * If this TReportCategory is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TExportTemplate> getTExportTemplates(Criteria criteria) throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            if (isNew())
            {
               collTExportTemplates = new ArrayList<TExportTemplate>();
            }
            else
            {
                criteria.add(TExportTemplatePeer.CATEGORYKEY, getObjectID() );
                collTExportTemplates = TExportTemplatePeer.doSelect(criteria);
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
                criteria.add(TExportTemplatePeer.CATEGORYKEY, getObjectID());
                if (!lastTExportTemplatesCriteria.equals(criteria))
                {
                    collTExportTemplates = TExportTemplatePeer.doSelect(criteria);
                }
            }
        }
        lastTExportTemplatesCriteria = criteria;

        return collTExportTemplates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTExportTemplates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TExportTemplate> getTExportTemplates(Connection con) throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            collTExportTemplates = getTExportTemplates(new Criteria(10), con);
        }
        return collTExportTemplates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TReportCategory has previously
     * been saved, it will retrieve related TExportTemplates from storage.
     * If this TReportCategory is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TExportTemplate> getTExportTemplates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            if (isNew())
            {
               collTExportTemplates = new ArrayList<TExportTemplate>();
            }
            else
            {
                 criteria.add(TExportTemplatePeer.CATEGORYKEY, getObjectID());
                 collTExportTemplates = TExportTemplatePeer.doSelect(criteria, con);
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
                 criteria.add(TExportTemplatePeer.CATEGORYKEY, getObjectID());
                 if (!lastTExportTemplatesCriteria.equals(criteria))
                 {
                     collTExportTemplates = TExportTemplatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTExportTemplatesCriteria = criteria;

         return collTExportTemplates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TReportCategory is new, it will return
     * an empty collection; or if this TReportCategory has previously
     * been saved, it will retrieve related TExportTemplates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TReportCategory.
     */
    protected List<TExportTemplate> getTExportTemplatesJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            if (isNew())
            {
               collTExportTemplates = new ArrayList<TExportTemplate>();
            }
            else
            {
                criteria.add(TExportTemplatePeer.CATEGORYKEY, getObjectID());
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TExportTemplatePeer.CATEGORYKEY, getObjectID());
            if (!lastTExportTemplatesCriteria.equals(criteria))
            {
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTExportTemplatesCriteria = criteria;

        return collTExportTemplates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TReportCategory is new, it will return
     * an empty collection; or if this TReportCategory has previously
     * been saved, it will retrieve related TExportTemplates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TReportCategory.
     */
    protected List<TExportTemplate> getTExportTemplatesJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            if (isNew())
            {
               collTExportTemplates = new ArrayList<TExportTemplate>();
            }
            else
            {
                criteria.add(TExportTemplatePeer.CATEGORYKEY, getObjectID());
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TExportTemplatePeer.CATEGORYKEY, getObjectID());
            if (!lastTExportTemplatesCriteria.equals(criteria))
            {
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTProject(criteria);
            }
        }
        lastTExportTemplatesCriteria = criteria;

        return collTExportTemplates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TReportCategory is new, it will return
     * an empty collection; or if this TReportCategory has previously
     * been saved, it will retrieve related TExportTemplates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TReportCategory.
     */
    protected List<TExportTemplate> getTExportTemplatesJoinTReportCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTExportTemplates == null)
        {
            if (isNew())
            {
               collTExportTemplates = new ArrayList<TExportTemplate>();
            }
            else
            {
                criteria.add(TExportTemplatePeer.CATEGORYKEY, getObjectID());
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTReportCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TExportTemplatePeer.CATEGORYKEY, getObjectID());
            if (!lastTExportTemplatesCriteria.equals(criteria))
            {
                collTExportTemplates = TExportTemplatePeer.doSelectJoinTReportCategory(criteria);
            }
        }
        lastTExportTemplatesCriteria = criteria;

        return collTExportTemplates;
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
            fieldNames.add("Repository");
            fieldNames.add("CreatedBy");
            fieldNames.add("Project");
            fieldNames.add("ParentID");
            fieldNames.add("Sortorder");
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
        if (name.equals("Repository"))
        {
            return getRepository();
        }
        if (name.equals("CreatedBy"))
        {
            return getCreatedBy();
        }
        if (name.equals("Project"))
        {
            return getProject();
        }
        if (name.equals("ParentID"))
        {
            return getParentID();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
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
        if (name.equals("Repository"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRepository((Integer) value);
            return true;
        }
        if (name.equals("CreatedBy"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCreatedBy((Integer) value);
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
        if (name.equals("ParentID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParentID((Integer) value);
            return true;
        }
        if (name.equals("Sortorder"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSortorder((Integer) value);
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
        if (name.equals(TReportCategoryPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TReportCategoryPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TReportCategoryPeer.REPOSITORY))
        {
            return getRepository();
        }
        if (name.equals(TReportCategoryPeer.CREATEDBY))
        {
            return getCreatedBy();
        }
        if (name.equals(TReportCategoryPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TReportCategoryPeer.PARENTID))
        {
            return getParentID();
        }
        if (name.equals(TReportCategoryPeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TReportCategoryPeer.TPUUID))
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
      if (TReportCategoryPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TReportCategoryPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TReportCategoryPeer.REPOSITORY.equals(name))
        {
            return setByName("Repository", value);
        }
      if (TReportCategoryPeer.CREATEDBY.equals(name))
        {
            return setByName("CreatedBy", value);
        }
      if (TReportCategoryPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TReportCategoryPeer.PARENTID.equals(name))
        {
            return setByName("ParentID", value);
        }
      if (TReportCategoryPeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TReportCategoryPeer.TPUUID.equals(name))
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
            return getRepository();
        }
        if (pos == 3)
        {
            return getCreatedBy();
        }
        if (pos == 4)
        {
            return getProject();
        }
        if (pos == 5)
        {
            return getParentID();
        }
        if (pos == 6)
        {
            return getSortorder();
        }
        if (pos == 7)
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
            return setByName("Repository", value);
        }
    if (position == 3)
        {
            return setByName("CreatedBy", value);
        }
    if (position == 4)
        {
            return setByName("Project", value);
        }
    if (position == 5)
        {
            return setByName("ParentID", value);
        }
    if (position == 6)
        {
            return setByName("Sortorder", value);
        }
    if (position == 7)
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
        save(TReportCategoryPeer.DATABASE_NAME);
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
                    TReportCategoryPeer.doInsert((TReportCategory) this, con);
                    setNew(false);
                }
                else
                {
                    TReportCategoryPeer.doUpdate((TReportCategory) this, con);
                }
            }


            if (collTExportTemplates != null)
            {
                for (int i = 0; i < collTExportTemplates.size(); i++)
                {
                    ((TExportTemplate) collTExportTemplates.get(i)).save(con);
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
    public TReportCategory copy() throws TorqueException
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
    public TReportCategory copy(Connection con) throws TorqueException
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
    public TReportCategory copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TReportCategory(), deepcopy);
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
    public TReportCategory copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TReportCategory(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TReportCategory copyInto(TReportCategory copyObj) throws TorqueException
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
    protected TReportCategory copyInto(TReportCategory copyObj, Connection con) throws TorqueException
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
    protected TReportCategory copyInto(TReportCategory copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setRepository(repository);
        copyObj.setCreatedBy(createdBy);
        copyObj.setProject(project);
        copyObj.setParentID(parentID);
        copyObj.setSortorder(sortorder);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TExportTemplate> vTExportTemplates = getTExportTemplates();
        if (vTExportTemplates != null)
        {
            for (int i = 0; i < vTExportTemplates.size(); i++)
            {
                TExportTemplate obj =  vTExportTemplates.get(i);
                copyObj.addTExportTemplate(obj.copy());
            }
        }
        else
        {
            copyObj.collTExportTemplates = null;
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
    protected TReportCategory copyInto(TReportCategory copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setRepository(repository);
        copyObj.setCreatedBy(createdBy);
        copyObj.setProject(project);
        copyObj.setParentID(parentID);
        copyObj.setSortorder(sortorder);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TExportTemplate> vTExportTemplates = getTExportTemplates(con);
        if (vTExportTemplates != null)
        {
            for (int i = 0; i < vTExportTemplates.size(); i++)
            {
                TExportTemplate obj =  vTExportTemplates.get(i);
                copyObj.addTExportTemplate(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTExportTemplates = null;
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
    public TReportCategoryPeer getPeer()
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
        return TReportCategoryPeer.getTableMap();
    }

  
    /**
     * Creates a TReportCategoryBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TReportCategoryBean with the contents of this object
     */
    public TReportCategoryBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TReportCategoryBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TReportCategoryBean with the contents of this object
     */
    public TReportCategoryBean getBean(IdentityMap createdBeans)
    {
        TReportCategoryBean result = (TReportCategoryBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TReportCategoryBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setRepository(getRepository());
        result.setCreatedBy(getCreatedBy());
        result.setProject(getProject());
        result.setParentID(getParentID());
        result.setSortorder(getSortorder());
        result.setUuid(getUuid());



        if (collTExportTemplates != null)
        {
            List<TExportTemplateBean> relatedBeans = new ArrayList<TExportTemplateBean>(collTExportTemplates.size());
            for (Iterator<TExportTemplate> collTExportTemplatesIt = collTExportTemplates.iterator(); collTExportTemplatesIt.hasNext(); )
            {
                TExportTemplate related = (TExportTemplate) collTExportTemplatesIt.next();
                TExportTemplateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTExportTemplateBeans(relatedBeans);
        }




        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }



        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTReportCategoryRelatedByParentID != null)
        {
            TReportCategoryBean relatedBean = aTReportCategoryRelatedByParentID.getBean(createdBeans);
            result.setTReportCategoryBeanRelatedByParentID(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TReportCategory with the contents
     * of a TReportCategoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TReportCategoryBean which contents are used to create
     *        the resulting class
     * @return an instance of TReportCategory with the contents of bean
     */
    public static TReportCategory createTReportCategory(TReportCategoryBean bean)
        throws TorqueException
    {
        return createTReportCategory(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TReportCategory with the contents
     * of a TReportCategoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TReportCategoryBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TReportCategory with the contents of bean
     */

    public static TReportCategory createTReportCategory(TReportCategoryBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TReportCategory result = (TReportCategory) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TReportCategory();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setRepository(bean.getRepository());
        result.setCreatedBy(bean.getCreatedBy());
        result.setProject(bean.getProject());
        result.setParentID(bean.getParentID());
        result.setSortorder(bean.getSortorder());
        result.setUuid(bean.getUuid());



        {
            List<TExportTemplateBean> relatedBeans = bean.getTExportTemplateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TExportTemplateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TExportTemplateBean relatedBean =  relatedBeansIt.next();
                    TExportTemplate related = TExportTemplate.createTExportTemplate(relatedBean, createdObjects);
                    result.addTExportTemplateFromBean(related);
                }
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



        {
            TPersonBean relatedBean = bean.getTPersonBean();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPerson(relatedObject);
            }
        }



        {
            TReportCategoryBean relatedBean = bean.getTReportCategoryBeanRelatedByParentID();
            if (relatedBean != null)
            {
                TReportCategory relatedObject = TReportCategory.createTReportCategory(relatedBean, createdObjects);
                result.setTReportCategoryRelatedByParentID(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TExportTemplate object to this object.
     * through the TExportTemplate foreign key attribute
     *
     * @param toAdd TExportTemplate
     */
    protected void addTExportTemplateFromBean(TExportTemplate toAdd)
    {
        initTExportTemplates();
        collTExportTemplates.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TReportCategory:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Repository = ")
           .append(getRepository())
           .append("\n");
        str.append("CreatedBy = ")
           .append(getCreatedBy())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("ParentID = ")
           .append(getParentID())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
