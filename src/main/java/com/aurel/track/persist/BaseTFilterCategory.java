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
import com.aurel.track.persist.TFilterCategory;
import com.aurel.track.persist.TFilterCategoryPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TFilterCategoryBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TFilterCategoryBean;

import com.aurel.track.beans.TQueryRepositoryBean;


/**
 * Hierarchical categorization of the queries
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TFilterCategory
 */
public abstract class BaseTFilterCategory extends TpBaseObject
{
    /** The Peer class */
    private static final TFilterCategoryPeer peer =
        new TFilterCategoryPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the repository field */
    private Integer repository;

    /** The value for the filterType field */
    private Integer filterType;

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



        // update associated TQueryRepository
        if (collTQueryRepositorys != null)
        {
            for (int i = 0; i < collTQueryRepositorys.size(); i++)
            {
                ((TQueryRepository) collTQueryRepositorys.get(i))
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
     * Get the FilterType
     *
     * @return Integer
     */
    public Integer getFilterType()
    {
        return filterType;
    }


    /**
     * Set the value of FilterType
     *
     * @param v new value
     */
    public void setFilterType(Integer v) 
    {

        if (!ObjectUtils.equals(this.filterType, v))
        {
            this.filterType = v;
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


        if (aTFilterCategoryRelatedByParentID != null && !ObjectUtils.equals(aTFilterCategoryRelatedByParentID.getObjectID(), v))
        {
            aTFilterCategoryRelatedByParentID = null;
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




    private TFilterCategory aTFilterCategoryRelatedByParentID;

    /**
     * Declares an association between this object and a TFilterCategory object
     *
     * @param v TFilterCategory
     * @throws TorqueException
     */
    public void setTFilterCategoryRelatedByParentID(TFilterCategory v) throws TorqueException
    {
        if (v == null)
        {
            setParentID((Integer) null);
        }
        else
        {
            setParentID(v.getObjectID());
        }
        aTFilterCategoryRelatedByParentID = v;
    }


    /**
     * Returns the associated TFilterCategory object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TFilterCategory object
     * @throws TorqueException
     */
    public TFilterCategory getTFilterCategoryRelatedByParentID()
        throws TorqueException
    {
        if (aTFilterCategoryRelatedByParentID == null && (!ObjectUtils.equals(this.parentID, null)))
        {
            aTFilterCategoryRelatedByParentID = TFilterCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.parentID));
        }
        return aTFilterCategoryRelatedByParentID;
    }

    /**
     * Return the associated TFilterCategory object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TFilterCategory object
     * @throws TorqueException
     */
    public TFilterCategory getTFilterCategoryRelatedByParentID(Connection connection)
        throws TorqueException
    {
        if (aTFilterCategoryRelatedByParentID == null && (!ObjectUtils.equals(this.parentID, null)))
        {
            aTFilterCategoryRelatedByParentID = TFilterCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.parentID), connection);
        }
        return aTFilterCategoryRelatedByParentID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTFilterCategoryRelatedByParentIDKey(ObjectKey key) throws TorqueException
    {

        setParentID(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTQueryRepositorys
     */
    protected List<TQueryRepository> collTQueryRepositorys;

    /**
     * Temporary storage of collTQueryRepositorys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTQueryRepositorys()
    {
        if (collTQueryRepositorys == null)
        {
            collTQueryRepositorys = new ArrayList<TQueryRepository>();
        }
    }


    /**
     * Method called to associate a TQueryRepository object to this object
     * through the TQueryRepository foreign key attribute
     *
     * @param l TQueryRepository
     * @throws TorqueException
     */
    public void addTQueryRepository(TQueryRepository l) throws TorqueException
    {
        getTQueryRepositorys().add(l);
        l.setTFilterCategory((TFilterCategory) this);
    }

    /**
     * Method called to associate a TQueryRepository object to this object
     * through the TQueryRepository foreign key attribute using connection.
     *
     * @param l TQueryRepository
     * @throws TorqueException
     */
    public void addTQueryRepository(TQueryRepository l, Connection con) throws TorqueException
    {
        getTQueryRepositorys(con).add(l);
        l.setTFilterCategory((TFilterCategory) this);
    }

    /**
     * The criteria used to select the current contents of collTQueryRepositorys
     */
    private Criteria lastTQueryRepositorysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTQueryRepositorys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TQueryRepository> getTQueryRepositorys()
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            collTQueryRepositorys = getTQueryRepositorys(new Criteria(10));
        }
        return collTQueryRepositorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFilterCategory has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     * If this TFilterCategory is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TQueryRepository> getTQueryRepositorys(Criteria criteria) throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID() );
                collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria);
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
                criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
                if (!lastTQueryRepositorysCriteria.equals(criteria))
                {
                    collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria);
                }
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTQueryRepositorys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TQueryRepository> getTQueryRepositorys(Connection con) throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            collTQueryRepositorys = getTQueryRepositorys(new Criteria(10), con);
        }
        return collTQueryRepositorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFilterCategory has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     * If this TFilterCategory is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TQueryRepository> getTQueryRepositorys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                 criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
                 collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria, con);
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
                 criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
                 if (!lastTQueryRepositorysCriteria.equals(criteria))
                 {
                     collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTQueryRepositorysCriteria = criteria;

         return collTQueryRepositorys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFilterCategory is new, it will return
     * an empty collection; or if this TFilterCategory has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFilterCategory.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFilterCategory is new, it will return
     * an empty collection; or if this TFilterCategory has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFilterCategory.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFilterCategory is new, it will return
     * an empty collection; or if this TFilterCategory has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFilterCategory.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTCLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTCLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTCLOB(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFilterCategory is new, it will return
     * an empty collection; or if this TFilterCategory has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFilterCategory.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTFilterCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTFilterCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.CATEGORYKEY, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTFilterCategory(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
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
            fieldNames.add("FilterType");
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
        if (name.equals("FilterType"))
        {
            return getFilterType();
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
        if (name.equals("FilterType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFilterType((Integer) value);
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
        if (name.equals(TFilterCategoryPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TFilterCategoryPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TFilterCategoryPeer.REPOSITORY))
        {
            return getRepository();
        }
        if (name.equals(TFilterCategoryPeer.FILTERTYPE))
        {
            return getFilterType();
        }
        if (name.equals(TFilterCategoryPeer.CREATEDBY))
        {
            return getCreatedBy();
        }
        if (name.equals(TFilterCategoryPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TFilterCategoryPeer.PARENTID))
        {
            return getParentID();
        }
        if (name.equals(TFilterCategoryPeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TFilterCategoryPeer.TPUUID))
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
      if (TFilterCategoryPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TFilterCategoryPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TFilterCategoryPeer.REPOSITORY.equals(name))
        {
            return setByName("Repository", value);
        }
      if (TFilterCategoryPeer.FILTERTYPE.equals(name))
        {
            return setByName("FilterType", value);
        }
      if (TFilterCategoryPeer.CREATEDBY.equals(name))
        {
            return setByName("CreatedBy", value);
        }
      if (TFilterCategoryPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TFilterCategoryPeer.PARENTID.equals(name))
        {
            return setByName("ParentID", value);
        }
      if (TFilterCategoryPeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TFilterCategoryPeer.TPUUID.equals(name))
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
            return getFilterType();
        }
        if (pos == 4)
        {
            return getCreatedBy();
        }
        if (pos == 5)
        {
            return getProject();
        }
        if (pos == 6)
        {
            return getParentID();
        }
        if (pos == 7)
        {
            return getSortorder();
        }
        if (pos == 8)
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
            return setByName("FilterType", value);
        }
    if (position == 4)
        {
            return setByName("CreatedBy", value);
        }
    if (position == 5)
        {
            return setByName("Project", value);
        }
    if (position == 6)
        {
            return setByName("ParentID", value);
        }
    if (position == 7)
        {
            return setByName("Sortorder", value);
        }
    if (position == 8)
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
        save(TFilterCategoryPeer.DATABASE_NAME);
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
                    TFilterCategoryPeer.doInsert((TFilterCategory) this, con);
                    setNew(false);
                }
                else
                {
                    TFilterCategoryPeer.doUpdate((TFilterCategory) this, con);
                }
            }


            if (collTQueryRepositorys != null)
            {
                for (int i = 0; i < collTQueryRepositorys.size(); i++)
                {
                    ((TQueryRepository) collTQueryRepositorys.get(i)).save(con);
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
    public TFilterCategory copy() throws TorqueException
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
    public TFilterCategory copy(Connection con) throws TorqueException
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
    public TFilterCategory copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TFilterCategory(), deepcopy);
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
    public TFilterCategory copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TFilterCategory(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TFilterCategory copyInto(TFilterCategory copyObj) throws TorqueException
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
    protected TFilterCategory copyInto(TFilterCategory copyObj, Connection con) throws TorqueException
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
    protected TFilterCategory copyInto(TFilterCategory copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setRepository(repository);
        copyObj.setFilterType(filterType);
        copyObj.setCreatedBy(createdBy);
        copyObj.setProject(project);
        copyObj.setParentID(parentID);
        copyObj.setSortorder(sortorder);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TQueryRepository> vTQueryRepositorys = getTQueryRepositorys();
        if (vTQueryRepositorys != null)
        {
            for (int i = 0; i < vTQueryRepositorys.size(); i++)
            {
                TQueryRepository obj =  vTQueryRepositorys.get(i);
                copyObj.addTQueryRepository(obj.copy());
            }
        }
        else
        {
            copyObj.collTQueryRepositorys = null;
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
    protected TFilterCategory copyInto(TFilterCategory copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setRepository(repository);
        copyObj.setFilterType(filterType);
        copyObj.setCreatedBy(createdBy);
        copyObj.setProject(project);
        copyObj.setParentID(parentID);
        copyObj.setSortorder(sortorder);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TQueryRepository> vTQueryRepositorys = getTQueryRepositorys(con);
        if (vTQueryRepositorys != null)
        {
            for (int i = 0; i < vTQueryRepositorys.size(); i++)
            {
                TQueryRepository obj =  vTQueryRepositorys.get(i);
                copyObj.addTQueryRepository(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTQueryRepositorys = null;
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
    public TFilterCategoryPeer getPeer()
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
        return TFilterCategoryPeer.getTableMap();
    }

  
    /**
     * Creates a TFilterCategoryBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TFilterCategoryBean with the contents of this object
     */
    public TFilterCategoryBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TFilterCategoryBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TFilterCategoryBean with the contents of this object
     */
    public TFilterCategoryBean getBean(IdentityMap createdBeans)
    {
        TFilterCategoryBean result = (TFilterCategoryBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TFilterCategoryBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setRepository(getRepository());
        result.setFilterType(getFilterType());
        result.setCreatedBy(getCreatedBy());
        result.setProject(getProject());
        result.setParentID(getParentID());
        result.setSortorder(getSortorder());
        result.setUuid(getUuid());



        if (collTQueryRepositorys != null)
        {
            List<TQueryRepositoryBean> relatedBeans = new ArrayList<TQueryRepositoryBean>(collTQueryRepositorys.size());
            for (Iterator<TQueryRepository> collTQueryRepositorysIt = collTQueryRepositorys.iterator(); collTQueryRepositorysIt.hasNext(); )
            {
                TQueryRepository related = (TQueryRepository) collTQueryRepositorysIt.next();
                TQueryRepositoryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTQueryRepositoryBeans(relatedBeans);
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



        if (aTFilterCategoryRelatedByParentID != null)
        {
            TFilterCategoryBean relatedBean = aTFilterCategoryRelatedByParentID.getBean(createdBeans);
            result.setTFilterCategoryBeanRelatedByParentID(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TFilterCategory with the contents
     * of a TFilterCategoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TFilterCategoryBean which contents are used to create
     *        the resulting class
     * @return an instance of TFilterCategory with the contents of bean
     */
    public static TFilterCategory createTFilterCategory(TFilterCategoryBean bean)
        throws TorqueException
    {
        return createTFilterCategory(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TFilterCategory with the contents
     * of a TFilterCategoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TFilterCategoryBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TFilterCategory with the contents of bean
     */

    public static TFilterCategory createTFilterCategory(TFilterCategoryBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TFilterCategory result = (TFilterCategory) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TFilterCategory();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setRepository(bean.getRepository());
        result.setFilterType(bean.getFilterType());
        result.setCreatedBy(bean.getCreatedBy());
        result.setProject(bean.getProject());
        result.setParentID(bean.getParentID());
        result.setSortorder(bean.getSortorder());
        result.setUuid(bean.getUuid());



        {
            List<TQueryRepositoryBean> relatedBeans = bean.getTQueryRepositoryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TQueryRepositoryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TQueryRepositoryBean relatedBean =  relatedBeansIt.next();
                    TQueryRepository related = TQueryRepository.createTQueryRepository(relatedBean, createdObjects);
                    result.addTQueryRepositoryFromBean(related);
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
            TFilterCategoryBean relatedBean = bean.getTFilterCategoryBeanRelatedByParentID();
            if (relatedBean != null)
            {
                TFilterCategory relatedObject = TFilterCategory.createTFilterCategory(relatedBean, createdObjects);
                result.setTFilterCategoryRelatedByParentID(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TQueryRepository object to this object.
     * through the TQueryRepository foreign key attribute
     *
     * @param toAdd TQueryRepository
     */
    protected void addTQueryRepositoryFromBean(TQueryRepository toAdd)
    {
        initTQueryRepositorys();
        collTQueryRepositorys.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TFilterCategory:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Repository = ")
           .append(getRepository())
           .append("\n");
        str.append("FilterType = ")
           .append(getFilterType())
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
