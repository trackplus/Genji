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
import com.aurel.track.persist.TCLOB;
import com.aurel.track.persist.TCLOBPeer;
import com.aurel.track.persist.TFilterCategory;
import com.aurel.track.persist.TFilterCategoryPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TCLOBBean;
import com.aurel.track.beans.TFilterCategoryBean;

import com.aurel.track.beans.TNotifySettingsBean;
import com.aurel.track.beans.TMenuitemQueryBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TQueryRepository
 */
public abstract class BaseTQueryRepository extends TpBaseObject
{
    /** The Peer class */
    private static final TQueryRepositoryPeer peer =
        new TQueryRepositoryPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the person field */
    private Integer person;

    /** The value for the project field */
    private Integer project;

    /** The value for the label field */
    private String label;

    /** The value for the queryType field */
    private Integer queryType;

    /** The value for the repositoryType field */
    private Integer repositoryType;

    /** The value for the queryKey field */
    private Integer queryKey;

    /** The value for the menuItem field */
    private String menuItem = "N";

    /** The value for the categoryKey field */
    private Integer categoryKey;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the viewID field */
    private String viewID;

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



        // update associated TNotifySettings
        if (collTNotifySettingss != null)
        {
            for (int i = 0; i < collTNotifySettingss.size(); i++)
            {
                ((TNotifySettings) collTNotifySettingss.get(i))
                        .setNotifyFilter(v);
            }
        }

        // update associated TMenuitemQuery
        if (collTMenuitemQuerys != null)
        {
            for (int i = 0; i < collTMenuitemQuerys.size(); i++)
            {
                ((TMenuitemQuery) collTMenuitemQuerys.get(i))
                        .setQueryKey(v);
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
     * Get the QueryType
     *
     * @return Integer
     */
    public Integer getQueryType()
    {
        return queryType;
    }


    /**
     * Set the value of QueryType
     *
     * @param v new value
     */
    public void setQueryType(Integer v) 
    {

        if (!ObjectUtils.equals(this.queryType, v))
        {
            this.queryType = v;
            setModified(true);
        }


    }

    /**
     * Get the RepositoryType
     *
     * @return Integer
     */
    public Integer getRepositoryType()
    {
        return repositoryType;
    }


    /**
     * Set the value of RepositoryType
     *
     * @param v new value
     */
    public void setRepositoryType(Integer v) 
    {

        if (!ObjectUtils.equals(this.repositoryType, v))
        {
            this.repositoryType = v;
            setModified(true);
        }


    }

    /**
     * Get the QueryKey
     *
     * @return Integer
     */
    public Integer getQueryKey()
    {
        return queryKey;
    }


    /**
     * Set the value of QueryKey
     *
     * @param v new value
     */
    public void setQueryKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.queryKey, v))
        {
            this.queryKey = v;
            setModified(true);
        }


        if (aTCLOB != null && !ObjectUtils.equals(aTCLOB.getObjectID(), v))
        {
            aTCLOB = null;
        }

    }

    /**
     * Get the MenuItem
     *
     * @return String
     */
    public String getMenuItem()
    {
        return menuItem;
    }


    /**
     * Set the value of MenuItem
     *
     * @param v new value
     */
    public void setMenuItem(String v) 
    {

        if (!ObjectUtils.equals(this.menuItem, v))
        {
            this.menuItem = v;
            setModified(true);
        }


    }

    /**
     * Get the CategoryKey
     *
     * @return Integer
     */
    public Integer getCategoryKey()
    {
        return categoryKey;
    }


    /**
     * Set the value of CategoryKey
     *
     * @param v new value
     */
    public void setCategoryKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.categoryKey, v))
        {
            this.categoryKey = v;
            setModified(true);
        }


        if (aTFilterCategory != null && !ObjectUtils.equals(aTFilterCategory.getObjectID(), v))
        {
            aTFilterCategory = null;
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
     * Get the ViewID
     *
     * @return String
     */
    public String getViewID()
    {
        return viewID;
    }


    /**
     * Set the value of ViewID
     *
     * @param v new value
     */
    public void setViewID(String v) 
    {

        if (!ObjectUtils.equals(this.viewID, v))
        {
            this.viewID = v;
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




    private TCLOB aTCLOB;

    /**
     * Declares an association between this object and a TCLOB object
     *
     * @param v TCLOB
     * @throws TorqueException
     */
    public void setTCLOB(TCLOB v) throws TorqueException
    {
        if (v == null)
        {
            setQueryKey((Integer) null);
        }
        else
        {
            setQueryKey(v.getObjectID());
        }
        aTCLOB = v;
    }


    /**
     * Returns the associated TCLOB object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TCLOB object
     * @throws TorqueException
     */
    public TCLOB getTCLOB()
        throws TorqueException
    {
        if (aTCLOB == null && (!ObjectUtils.equals(this.queryKey, null)))
        {
            aTCLOB = TCLOBPeer.retrieveByPK(SimpleKey.keyFor(this.queryKey));
        }
        return aTCLOB;
    }

    /**
     * Return the associated TCLOB object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TCLOB object
     * @throws TorqueException
     */
    public TCLOB getTCLOB(Connection connection)
        throws TorqueException
    {
        if (aTCLOB == null && (!ObjectUtils.equals(this.queryKey, null)))
        {
            aTCLOB = TCLOBPeer.retrieveByPK(SimpleKey.keyFor(this.queryKey), connection);
        }
        return aTCLOB;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTCLOBKey(ObjectKey key) throws TorqueException
    {

        setQueryKey(new Integer(((NumberKey) key).intValue()));
    }




    private TFilterCategory aTFilterCategory;

    /**
     * Declares an association between this object and a TFilterCategory object
     *
     * @param v TFilterCategory
     * @throws TorqueException
     */
    public void setTFilterCategory(TFilterCategory v) throws TorqueException
    {
        if (v == null)
        {
            setCategoryKey((Integer) null);
        }
        else
        {
            setCategoryKey(v.getObjectID());
        }
        aTFilterCategory = v;
    }


    /**
     * Returns the associated TFilterCategory object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TFilterCategory object
     * @throws TorqueException
     */
    public TFilterCategory getTFilterCategory()
        throws TorqueException
    {
        if (aTFilterCategory == null && (!ObjectUtils.equals(this.categoryKey, null)))
        {
            aTFilterCategory = TFilterCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.categoryKey));
        }
        return aTFilterCategory;
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
    public TFilterCategory getTFilterCategory(Connection connection)
        throws TorqueException
    {
        if (aTFilterCategory == null && (!ObjectUtils.equals(this.categoryKey, null)))
        {
            aTFilterCategory = TFilterCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.categoryKey), connection);
        }
        return aTFilterCategory;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTFilterCategoryKey(ObjectKey key) throws TorqueException
    {

        setCategoryKey(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTNotifySettingss
     */
    protected List<TNotifySettings> collTNotifySettingss;

    /**
     * Temporary storage of collTNotifySettingss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTNotifySettingss()
    {
        if (collTNotifySettingss == null)
        {
            collTNotifySettingss = new ArrayList<TNotifySettings>();
        }
    }


    /**
     * Method called to associate a TNotifySettings object to this object
     * through the TNotifySettings foreign key attribute
     *
     * @param l TNotifySettings
     * @throws TorqueException
     */
    public void addTNotifySettings(TNotifySettings l) throws TorqueException
    {
        getTNotifySettingss().add(l);
        l.setTQueryRepository((TQueryRepository) this);
    }

    /**
     * Method called to associate a TNotifySettings object to this object
     * through the TNotifySettings foreign key attribute using connection.
     *
     * @param l TNotifySettings
     * @throws TorqueException
     */
    public void addTNotifySettings(TNotifySettings l, Connection con) throws TorqueException
    {
        getTNotifySettingss(con).add(l);
        l.setTQueryRepository((TQueryRepository) this);
    }

    /**
     * The criteria used to select the current contents of collTNotifySettingss
     */
    private Criteria lastTNotifySettingssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNotifySettingss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TNotifySettings> getTNotifySettingss()
        throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            collTNotifySettingss = getTNotifySettingss(new Criteria(10));
        }
        return collTNotifySettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TQueryRepository has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     * If this TQueryRepository is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TNotifySettings> getTNotifySettingss(Criteria criteria) throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID() );
                collTNotifySettingss = TNotifySettingsPeer.doSelect(criteria);
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
                criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
                if (!lastTNotifySettingssCriteria.equals(criteria))
                {
                    collTNotifySettingss = TNotifySettingsPeer.doSelect(criteria);
                }
            }
        }
        lastTNotifySettingssCriteria = criteria;

        return collTNotifySettingss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNotifySettingss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNotifySettings> getTNotifySettingss(Connection con) throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            collTNotifySettingss = getTNotifySettingss(new Criteria(10), con);
        }
        return collTNotifySettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TQueryRepository has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     * If this TQueryRepository is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNotifySettings> getTNotifySettingss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                 criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
                 collTNotifySettingss = TNotifySettingsPeer.doSelect(criteria, con);
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
                 criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
                 if (!lastTNotifySettingssCriteria.equals(criteria))
                 {
                     collTNotifySettingss = TNotifySettingsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTNotifySettingssCriteria = criteria;

         return collTNotifySettingss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TQueryRepository is new, it will return
     * an empty collection; or if this TQueryRepository has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TQueryRepository.
     */
    protected List<TNotifySettings> getTNotifySettingssJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
            if (!lastTNotifySettingssCriteria.equals(criteria))
            {
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTNotifySettingssCriteria = criteria;

        return collTNotifySettingss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TQueryRepository is new, it will return
     * an empty collection; or if this TQueryRepository has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TQueryRepository.
     */
    protected List<TNotifySettings> getTNotifySettingssJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
            if (!lastTNotifySettingssCriteria.equals(criteria))
            {
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTNotifySettingssCriteria = criteria;

        return collTNotifySettingss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TQueryRepository is new, it will return
     * an empty collection; or if this TQueryRepository has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TQueryRepository.
     */
    protected List<TNotifySettings> getTNotifySettingssJoinTNotifyTrigger(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTNotifyTrigger(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
            if (!lastTNotifySettingssCriteria.equals(criteria))
            {
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTNotifyTrigger(criteria);
            }
        }
        lastTNotifySettingssCriteria = criteria;

        return collTNotifySettingss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TQueryRepository is new, it will return
     * an empty collection; or if this TQueryRepository has previously
     * been saved, it will retrieve related TNotifySettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TQueryRepository.
     */
    protected List<TNotifySettings> getTNotifySettingssJoinTQueryRepository(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifySettingss == null)
        {
            if (isNew())
            {
               collTNotifySettingss = new ArrayList<TNotifySettings>();
            }
            else
            {
                criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTQueryRepository(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifySettingsPeer.NOTIFYFILTER, getObjectID());
            if (!lastTNotifySettingssCriteria.equals(criteria))
            {
                collTNotifySettingss = TNotifySettingsPeer.doSelectJoinTQueryRepository(criteria);
            }
        }
        lastTNotifySettingssCriteria = criteria;

        return collTNotifySettingss;
    }





    /**
     * Collection to store aggregation of collTMenuitemQuerys
     */
    protected List<TMenuitemQuery> collTMenuitemQuerys;

    /**
     * Temporary storage of collTMenuitemQuerys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTMenuitemQuerys()
    {
        if (collTMenuitemQuerys == null)
        {
            collTMenuitemQuerys = new ArrayList<TMenuitemQuery>();
        }
    }


    /**
     * Method called to associate a TMenuitemQuery object to this object
     * through the TMenuitemQuery foreign key attribute
     *
     * @param l TMenuitemQuery
     * @throws TorqueException
     */
    public void addTMenuitemQuery(TMenuitemQuery l) throws TorqueException
    {
        getTMenuitemQuerys().add(l);
        l.setTQueryRepository((TQueryRepository) this);
    }

    /**
     * Method called to associate a TMenuitemQuery object to this object
     * through the TMenuitemQuery foreign key attribute using connection.
     *
     * @param l TMenuitemQuery
     * @throws TorqueException
     */
    public void addTMenuitemQuery(TMenuitemQuery l, Connection con) throws TorqueException
    {
        getTMenuitemQuerys(con).add(l);
        l.setTQueryRepository((TQueryRepository) this);
    }

    /**
     * The criteria used to select the current contents of collTMenuitemQuerys
     */
    private Criteria lastTMenuitemQuerysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMenuitemQuerys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TMenuitemQuery> getTMenuitemQuerys()
        throws TorqueException
    {
        if (collTMenuitemQuerys == null)
        {
            collTMenuitemQuerys = getTMenuitemQuerys(new Criteria(10));
        }
        return collTMenuitemQuerys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TQueryRepository has previously
     * been saved, it will retrieve related TMenuitemQuerys from storage.
     * If this TQueryRepository is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TMenuitemQuery> getTMenuitemQuerys(Criteria criteria) throws TorqueException
    {
        if (collTMenuitemQuerys == null)
        {
            if (isNew())
            {
               collTMenuitemQuerys = new ArrayList<TMenuitemQuery>();
            }
            else
            {
                criteria.add(TMenuitemQueryPeer.QUERYKEY, getObjectID() );
                collTMenuitemQuerys = TMenuitemQueryPeer.doSelect(criteria);
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
                criteria.add(TMenuitemQueryPeer.QUERYKEY, getObjectID());
                if (!lastTMenuitemQuerysCriteria.equals(criteria))
                {
                    collTMenuitemQuerys = TMenuitemQueryPeer.doSelect(criteria);
                }
            }
        }
        lastTMenuitemQuerysCriteria = criteria;

        return collTMenuitemQuerys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMenuitemQuerys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMenuitemQuery> getTMenuitemQuerys(Connection con) throws TorqueException
    {
        if (collTMenuitemQuerys == null)
        {
            collTMenuitemQuerys = getTMenuitemQuerys(new Criteria(10), con);
        }
        return collTMenuitemQuerys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TQueryRepository has previously
     * been saved, it will retrieve related TMenuitemQuerys from storage.
     * If this TQueryRepository is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMenuitemQuery> getTMenuitemQuerys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTMenuitemQuerys == null)
        {
            if (isNew())
            {
               collTMenuitemQuerys = new ArrayList<TMenuitemQuery>();
            }
            else
            {
                 criteria.add(TMenuitemQueryPeer.QUERYKEY, getObjectID());
                 collTMenuitemQuerys = TMenuitemQueryPeer.doSelect(criteria, con);
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
                 criteria.add(TMenuitemQueryPeer.QUERYKEY, getObjectID());
                 if (!lastTMenuitemQuerysCriteria.equals(criteria))
                 {
                     collTMenuitemQuerys = TMenuitemQueryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTMenuitemQuerysCriteria = criteria;

         return collTMenuitemQuerys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TQueryRepository is new, it will return
     * an empty collection; or if this TQueryRepository has previously
     * been saved, it will retrieve related TMenuitemQuerys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TQueryRepository.
     */
    protected List<TMenuitemQuery> getTMenuitemQuerysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTMenuitemQuerys == null)
        {
            if (isNew())
            {
               collTMenuitemQuerys = new ArrayList<TMenuitemQuery>();
            }
            else
            {
                criteria.add(TMenuitemQueryPeer.QUERYKEY, getObjectID());
                collTMenuitemQuerys = TMenuitemQueryPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMenuitemQueryPeer.QUERYKEY, getObjectID());
            if (!lastTMenuitemQuerysCriteria.equals(criteria))
            {
                collTMenuitemQuerys = TMenuitemQueryPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTMenuitemQuerysCriteria = criteria;

        return collTMenuitemQuerys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TQueryRepository is new, it will return
     * an empty collection; or if this TQueryRepository has previously
     * been saved, it will retrieve related TMenuitemQuerys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TQueryRepository.
     */
    protected List<TMenuitemQuery> getTMenuitemQuerysJoinTQueryRepository(Criteria criteria)
        throws TorqueException
    {
        if (collTMenuitemQuerys == null)
        {
            if (isNew())
            {
               collTMenuitemQuerys = new ArrayList<TMenuitemQuery>();
            }
            else
            {
                criteria.add(TMenuitemQueryPeer.QUERYKEY, getObjectID());
                collTMenuitemQuerys = TMenuitemQueryPeer.doSelectJoinTQueryRepository(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMenuitemQueryPeer.QUERYKEY, getObjectID());
            if (!lastTMenuitemQuerysCriteria.equals(criteria))
            {
                collTMenuitemQuerys = TMenuitemQueryPeer.doSelectJoinTQueryRepository(criteria);
            }
        }
        lastTMenuitemQuerysCriteria = criteria;

        return collTMenuitemQuerys;
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
            fieldNames.add("Project");
            fieldNames.add("Label");
            fieldNames.add("QueryType");
            fieldNames.add("RepositoryType");
            fieldNames.add("QueryKey");
            fieldNames.add("MenuItem");
            fieldNames.add("CategoryKey");
            fieldNames.add("Sortorder");
            fieldNames.add("ViewID");
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
        if (name.equals("Project"))
        {
            return getProject();
        }
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("QueryType"))
        {
            return getQueryType();
        }
        if (name.equals("RepositoryType"))
        {
            return getRepositoryType();
        }
        if (name.equals("QueryKey"))
        {
            return getQueryKey();
        }
        if (name.equals("MenuItem"))
        {
            return getMenuItem();
        }
        if (name.equals("CategoryKey"))
        {
            return getCategoryKey();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
        }
        if (name.equals("ViewID"))
        {
            return getViewID();
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
        if (name.equals("QueryType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setQueryType((Integer) value);
            return true;
        }
        if (name.equals("RepositoryType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRepositoryType((Integer) value);
            return true;
        }
        if (name.equals("QueryKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setQueryKey((Integer) value);
            return true;
        }
        if (name.equals("MenuItem"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMenuItem((String) value);
            return true;
        }
        if (name.equals("CategoryKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCategoryKey((Integer) value);
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
        if (name.equals("ViewID"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setViewID((String) value);
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
        if (name.equals(TQueryRepositoryPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TQueryRepositoryPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TQueryRepositoryPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TQueryRepositoryPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TQueryRepositoryPeer.QUERYTYPE))
        {
            return getQueryType();
        }
        if (name.equals(TQueryRepositoryPeer.REPOSITORYTYPE))
        {
            return getRepositoryType();
        }
        if (name.equals(TQueryRepositoryPeer.QUERYKEY))
        {
            return getQueryKey();
        }
        if (name.equals(TQueryRepositoryPeer.MENUITEM))
        {
            return getMenuItem();
        }
        if (name.equals(TQueryRepositoryPeer.CATEGORYKEY))
        {
            return getCategoryKey();
        }
        if (name.equals(TQueryRepositoryPeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TQueryRepositoryPeer.VIEWID))
        {
            return getViewID();
        }
        if (name.equals(TQueryRepositoryPeer.TPUUID))
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
      if (TQueryRepositoryPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TQueryRepositoryPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TQueryRepositoryPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TQueryRepositoryPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TQueryRepositoryPeer.QUERYTYPE.equals(name))
        {
            return setByName("QueryType", value);
        }
      if (TQueryRepositoryPeer.REPOSITORYTYPE.equals(name))
        {
            return setByName("RepositoryType", value);
        }
      if (TQueryRepositoryPeer.QUERYKEY.equals(name))
        {
            return setByName("QueryKey", value);
        }
      if (TQueryRepositoryPeer.MENUITEM.equals(name))
        {
            return setByName("MenuItem", value);
        }
      if (TQueryRepositoryPeer.CATEGORYKEY.equals(name))
        {
            return setByName("CategoryKey", value);
        }
      if (TQueryRepositoryPeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TQueryRepositoryPeer.VIEWID.equals(name))
        {
            return setByName("ViewID", value);
        }
      if (TQueryRepositoryPeer.TPUUID.equals(name))
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
            return getProject();
        }
        if (pos == 3)
        {
            return getLabel();
        }
        if (pos == 4)
        {
            return getQueryType();
        }
        if (pos == 5)
        {
            return getRepositoryType();
        }
        if (pos == 6)
        {
            return getQueryKey();
        }
        if (pos == 7)
        {
            return getMenuItem();
        }
        if (pos == 8)
        {
            return getCategoryKey();
        }
        if (pos == 9)
        {
            return getSortorder();
        }
        if (pos == 10)
        {
            return getViewID();
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
            return setByName("Person", value);
        }
    if (position == 2)
        {
            return setByName("Project", value);
        }
    if (position == 3)
        {
            return setByName("Label", value);
        }
    if (position == 4)
        {
            return setByName("QueryType", value);
        }
    if (position == 5)
        {
            return setByName("RepositoryType", value);
        }
    if (position == 6)
        {
            return setByName("QueryKey", value);
        }
    if (position == 7)
        {
            return setByName("MenuItem", value);
        }
    if (position == 8)
        {
            return setByName("CategoryKey", value);
        }
    if (position == 9)
        {
            return setByName("Sortorder", value);
        }
    if (position == 10)
        {
            return setByName("ViewID", value);
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
        save(TQueryRepositoryPeer.DATABASE_NAME);
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
                    TQueryRepositoryPeer.doInsert((TQueryRepository) this, con);
                    setNew(false);
                }
                else
                {
                    TQueryRepositoryPeer.doUpdate((TQueryRepository) this, con);
                }
            }


            if (collTNotifySettingss != null)
            {
                for (int i = 0; i < collTNotifySettingss.size(); i++)
                {
                    ((TNotifySettings) collTNotifySettingss.get(i)).save(con);
                }
            }

            if (collTMenuitemQuerys != null)
            {
                for (int i = 0; i < collTMenuitemQuerys.size(); i++)
                {
                    ((TMenuitemQuery) collTMenuitemQuerys.get(i)).save(con);
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
    public TQueryRepository copy() throws TorqueException
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
    public TQueryRepository copy(Connection con) throws TorqueException
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
    public TQueryRepository copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TQueryRepository(), deepcopy);
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
    public TQueryRepository copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TQueryRepository(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TQueryRepository copyInto(TQueryRepository copyObj) throws TorqueException
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
    protected TQueryRepository copyInto(TQueryRepository copyObj, Connection con) throws TorqueException
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
    protected TQueryRepository copyInto(TQueryRepository copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setProject(project);
        copyObj.setLabel(label);
        copyObj.setQueryType(queryType);
        copyObj.setRepositoryType(repositoryType);
        copyObj.setQueryKey(queryKey);
        copyObj.setMenuItem(menuItem);
        copyObj.setCategoryKey(categoryKey);
        copyObj.setSortorder(sortorder);
        copyObj.setViewID(viewID);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TNotifySettings> vTNotifySettingss = getTNotifySettingss();
        if (vTNotifySettingss != null)
        {
            for (int i = 0; i < vTNotifySettingss.size(); i++)
            {
                TNotifySettings obj =  vTNotifySettingss.get(i);
                copyObj.addTNotifySettings(obj.copy());
            }
        }
        else
        {
            copyObj.collTNotifySettingss = null;
        }


        List<TMenuitemQuery> vTMenuitemQuerys = getTMenuitemQuerys();
        if (vTMenuitemQuerys != null)
        {
            for (int i = 0; i < vTMenuitemQuerys.size(); i++)
            {
                TMenuitemQuery obj =  vTMenuitemQuerys.get(i);
                copyObj.addTMenuitemQuery(obj.copy());
            }
        }
        else
        {
            copyObj.collTMenuitemQuerys = null;
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
    protected TQueryRepository copyInto(TQueryRepository copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setProject(project);
        copyObj.setLabel(label);
        copyObj.setQueryType(queryType);
        copyObj.setRepositoryType(repositoryType);
        copyObj.setQueryKey(queryKey);
        copyObj.setMenuItem(menuItem);
        copyObj.setCategoryKey(categoryKey);
        copyObj.setSortorder(sortorder);
        copyObj.setViewID(viewID);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TNotifySettings> vTNotifySettingss = getTNotifySettingss(con);
        if (vTNotifySettingss != null)
        {
            for (int i = 0; i < vTNotifySettingss.size(); i++)
            {
                TNotifySettings obj =  vTNotifySettingss.get(i);
                copyObj.addTNotifySettings(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTNotifySettingss = null;
        }


        List<TMenuitemQuery> vTMenuitemQuerys = getTMenuitemQuerys(con);
        if (vTMenuitemQuerys != null)
        {
            for (int i = 0; i < vTMenuitemQuerys.size(); i++)
            {
                TMenuitemQuery obj =  vTMenuitemQuerys.get(i);
                copyObj.addTMenuitemQuery(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTMenuitemQuerys = null;
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
    public TQueryRepositoryPeer getPeer()
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
        return TQueryRepositoryPeer.getTableMap();
    }

  
    /**
     * Creates a TQueryRepositoryBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TQueryRepositoryBean with the contents of this object
     */
    public TQueryRepositoryBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TQueryRepositoryBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TQueryRepositoryBean with the contents of this object
     */
    public TQueryRepositoryBean getBean(IdentityMap createdBeans)
    {
        TQueryRepositoryBean result = (TQueryRepositoryBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TQueryRepositoryBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setPerson(getPerson());
        result.setProject(getProject());
        result.setLabel(getLabel());
        result.setQueryType(getQueryType());
        result.setRepositoryType(getRepositoryType());
        result.setQueryKey(getQueryKey());
        result.setMenuItem(getMenuItem());
        result.setCategoryKey(getCategoryKey());
        result.setSortorder(getSortorder());
        result.setViewID(getViewID());
        result.setUuid(getUuid());



        if (collTNotifySettingss != null)
        {
            List<TNotifySettingsBean> relatedBeans = new ArrayList<TNotifySettingsBean>(collTNotifySettingss.size());
            for (Iterator<TNotifySettings> collTNotifySettingssIt = collTNotifySettingss.iterator(); collTNotifySettingssIt.hasNext(); )
            {
                TNotifySettings related = (TNotifySettings) collTNotifySettingssIt.next();
                TNotifySettingsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTNotifySettingsBeans(relatedBeans);
        }


        if (collTMenuitemQuerys != null)
        {
            List<TMenuitemQueryBean> relatedBeans = new ArrayList<TMenuitemQueryBean>(collTMenuitemQuerys.size());
            for (Iterator<TMenuitemQuery> collTMenuitemQuerysIt = collTMenuitemQuerys.iterator(); collTMenuitemQuerysIt.hasNext(); )
            {
                TMenuitemQuery related = (TMenuitemQuery) collTMenuitemQuerysIt.next();
                TMenuitemQueryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTMenuitemQueryBeans(relatedBeans);
        }




        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }



        if (aTCLOB != null)
        {
            TCLOBBean relatedBean = aTCLOB.getBean(createdBeans);
            result.setTCLOBBean(relatedBean);
        }



        if (aTFilterCategory != null)
        {
            TFilterCategoryBean relatedBean = aTFilterCategory.getBean(createdBeans);
            result.setTFilterCategoryBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TQueryRepository with the contents
     * of a TQueryRepositoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TQueryRepositoryBean which contents are used to create
     *        the resulting class
     * @return an instance of TQueryRepository with the contents of bean
     */
    public static TQueryRepository createTQueryRepository(TQueryRepositoryBean bean)
        throws TorqueException
    {
        return createTQueryRepository(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TQueryRepository with the contents
     * of a TQueryRepositoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TQueryRepositoryBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TQueryRepository with the contents of bean
     */

    public static TQueryRepository createTQueryRepository(TQueryRepositoryBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TQueryRepository result = (TQueryRepository) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TQueryRepository();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setPerson(bean.getPerson());
        result.setProject(bean.getProject());
        result.setLabel(bean.getLabel());
        result.setQueryType(bean.getQueryType());
        result.setRepositoryType(bean.getRepositoryType());
        result.setQueryKey(bean.getQueryKey());
        result.setMenuItem(bean.getMenuItem());
        result.setCategoryKey(bean.getCategoryKey());
        result.setSortorder(bean.getSortorder());
        result.setViewID(bean.getViewID());
        result.setUuid(bean.getUuid());



        {
            List<TNotifySettingsBean> relatedBeans = bean.getTNotifySettingsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TNotifySettingsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TNotifySettingsBean relatedBean =  relatedBeansIt.next();
                    TNotifySettings related = TNotifySettings.createTNotifySettings(relatedBean, createdObjects);
                    result.addTNotifySettingsFromBean(related);
                }
            }
        }


        {
            List<TMenuitemQueryBean> relatedBeans = bean.getTMenuitemQueryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TMenuitemQueryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TMenuitemQueryBean relatedBean =  relatedBeansIt.next();
                    TMenuitemQuery related = TMenuitemQuery.createTMenuitemQuery(relatedBean, createdObjects);
                    result.addTMenuitemQueryFromBean(related);
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
            TProjectBean relatedBean = bean.getTProjectBean();
            if (relatedBean != null)
            {
                TProject relatedObject = TProject.createTProject(relatedBean, createdObjects);
                result.setTProject(relatedObject);
            }
        }



        {
            TCLOBBean relatedBean = bean.getTCLOBBean();
            if (relatedBean != null)
            {
                TCLOB relatedObject = TCLOB.createTCLOB(relatedBean, createdObjects);
                result.setTCLOB(relatedObject);
            }
        }



        {
            TFilterCategoryBean relatedBean = bean.getTFilterCategoryBean();
            if (relatedBean != null)
            {
                TFilterCategory relatedObject = TFilterCategory.createTFilterCategory(relatedBean, createdObjects);
                result.setTFilterCategory(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TNotifySettings object to this object.
     * through the TNotifySettings foreign key attribute
     *
     * @param toAdd TNotifySettings
     */
    protected void addTNotifySettingsFromBean(TNotifySettings toAdd)
    {
        initTNotifySettingss();
        collTNotifySettingss.add(toAdd);
    }


    /**
     * Method called to associate a TMenuitemQuery object to this object.
     * through the TMenuitemQuery foreign key attribute
     *
     * @param toAdd TMenuitemQuery
     */
    protected void addTMenuitemQueryFromBean(TMenuitemQuery toAdd)
    {
        initTMenuitemQuerys();
        collTMenuitemQuerys.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TQueryRepository:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("QueryType = ")
           .append(getQueryType())
           .append("\n");
        str.append("RepositoryType = ")
           .append(getRepositoryType())
           .append("\n");
        str.append("QueryKey = ")
           .append(getQueryKey())
           .append("\n");
        str.append("MenuItem = ")
           .append(getMenuItem())
           .append("\n");
        str.append("CategoryKey = ")
           .append(getCategoryKey())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("ViewID = ")
           .append(getViewID())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
