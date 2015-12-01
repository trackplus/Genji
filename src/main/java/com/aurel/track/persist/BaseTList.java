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



import com.aurel.track.persist.TList;
import com.aurel.track.persist.TListPeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TOptionSettingsBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TList
 */
public abstract class BaseTList extends TpBaseObject
{
    /** The Peer class */
    private static final TListPeer peer =
        new TListPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the description field */
    private String description;

    /** The value for the tagLabel field */
    private String tagLabel;

    /** The value for the parentList field */
    private Integer parentList;

    /** The value for the listType field */
    private Integer listType;

    /** The value for the childNumber field */
    private Integer childNumber;

    /** The value for the deleted field */
    private String deleted = "N";

    /** The value for the repositoryType field */
    private Integer repositoryType;

    /** The value for the project field */
    private Integer project;

    /** The value for the owner field */
    private Integer owner;

    /** The value for the moreProps field */
    private String moreProps;

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



        // update associated TOption
        if (collTOptions != null)
        {
            for (int i = 0; i < collTOptions.size(); i++)
            {
                ((TOption) collTOptions.get(i))
                        .setList(v);
            }
        }

        // update associated TOptionSettings
        if (collTOptionSettingss != null)
        {
            for (int i = 0; i < collTOptionSettingss.size(); i++)
            {
                ((TOptionSettings) collTOptionSettingss.get(i))
                        .setList(v);
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
     * Get the TagLabel
     *
     * @return String
     */
    public String getTagLabel()
    {
        return tagLabel;
    }


    /**
     * Set the value of TagLabel
     *
     * @param v new value
     */
    public void setTagLabel(String v) 
    {

        if (!ObjectUtils.equals(this.tagLabel, v))
        {
            this.tagLabel = v;
            setModified(true);
        }


    }

    /**
     * Get the ParentList
     *
     * @return Integer
     */
    public Integer getParentList()
    {
        return parentList;
    }


    /**
     * Set the value of ParentList
     *
     * @param v new value
     */
    public void setParentList(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.parentList, v))
        {
            this.parentList = v;
            setModified(true);
        }


        if (aTListRelatedByParentList != null && !ObjectUtils.equals(aTListRelatedByParentList.getObjectID(), v))
        {
            aTListRelatedByParentList = null;
        }

    }

    /**
     * Get the ListType
     *
     * @return Integer
     */
    public Integer getListType()
    {
        return listType;
    }


    /**
     * Set the value of ListType
     *
     * @param v new value
     */
    public void setListType(Integer v) 
    {

        if (!ObjectUtils.equals(this.listType, v))
        {
            this.listType = v;
            setModified(true);
        }


    }

    /**
     * Get the ChildNumber
     *
     * @return Integer
     */
    public Integer getChildNumber()
    {
        return childNumber;
    }


    /**
     * Set the value of ChildNumber
     *
     * @param v new value
     */
    public void setChildNumber(Integer v) 
    {

        if (!ObjectUtils.equals(this.childNumber, v))
        {
            this.childNumber = v;
            setModified(true);
        }


    }

    /**
     * Get the Deleted
     *
     * @return String
     */
    public String getDeleted()
    {
        return deleted;
    }


    /**
     * Set the value of Deleted
     *
     * @param v new value
     */
    public void setDeleted(String v) 
    {

        if (!ObjectUtils.equals(this.deleted, v))
        {
            this.deleted = v;
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
     * Get the MoreProps
     *
     * @return String
     */
    public String getMoreProps()
    {
        return moreProps;
    }


    /**
     * Set the value of MoreProps
     *
     * @param v new value
     */
    public void setMoreProps(String v) 
    {

        if (!ObjectUtils.equals(this.moreProps, v))
        {
            this.moreProps = v;
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

    



    private TList aTListRelatedByParentList;

    /**
     * Declares an association between this object and a TList object
     *
     * @param v TList
     * @throws TorqueException
     */
    public void setTListRelatedByParentList(TList v) throws TorqueException
    {
        if (v == null)
        {
            setParentList((Integer) null);
        }
        else
        {
            setParentList(v.getObjectID());
        }
        aTListRelatedByParentList = v;
    }


    /**
     * Returns the associated TList object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TList object
     * @throws TorqueException
     */
    public TList getTListRelatedByParentList()
        throws TorqueException
    {
        if (aTListRelatedByParentList == null && (!ObjectUtils.equals(this.parentList, null)))
        {
            aTListRelatedByParentList = TListPeer.retrieveByPK(SimpleKey.keyFor(this.parentList));
        }
        return aTListRelatedByParentList;
    }

    /**
     * Return the associated TList object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TList object
     * @throws TorqueException
     */
    public TList getTListRelatedByParentList(Connection connection)
        throws TorqueException
    {
        if (aTListRelatedByParentList == null && (!ObjectUtils.equals(this.parentList, null)))
        {
            aTListRelatedByParentList = TListPeer.retrieveByPK(SimpleKey.keyFor(this.parentList), connection);
        }
        return aTListRelatedByParentList;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTListRelatedByParentListKey(ObjectKey key) throws TorqueException
    {

        setParentList(new Integer(((NumberKey) key).intValue()));
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
   





    /**
     * Collection to store aggregation of collTOptions
     */
    protected List<TOption> collTOptions;

    /**
     * Temporary storage of collTOptions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTOptions()
    {
        if (collTOptions == null)
        {
            collTOptions = new ArrayList<TOption>();
        }
    }


    /**
     * Method called to associate a TOption object to this object
     * through the TOption foreign key attribute
     *
     * @param l TOption
     * @throws TorqueException
     */
    public void addTOption(TOption l) throws TorqueException
    {
        getTOptions().add(l);
        l.setTList((TList) this);
    }

    /**
     * Method called to associate a TOption object to this object
     * through the TOption foreign key attribute using connection.
     *
     * @param l TOption
     * @throws TorqueException
     */
    public void addTOption(TOption l, Connection con) throws TorqueException
    {
        getTOptions(con).add(l);
        l.setTList((TList) this);
    }

    /**
     * The criteria used to select the current contents of collTOptions
     */
    private Criteria lastTOptionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOptions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TOption> getTOptions()
        throws TorqueException
    {
        if (collTOptions == null)
        {
            collTOptions = getTOptions(new Criteria(10));
        }
        return collTOptions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TList has previously
     * been saved, it will retrieve related TOptions from storage.
     * If this TList is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TOption> getTOptions(Criteria criteria) throws TorqueException
    {
        if (collTOptions == null)
        {
            if (isNew())
            {
               collTOptions = new ArrayList<TOption>();
            }
            else
            {
                criteria.add(TOptionPeer.LIST, getObjectID() );
                collTOptions = TOptionPeer.doSelect(criteria);
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
                criteria.add(TOptionPeer.LIST, getObjectID());
                if (!lastTOptionsCriteria.equals(criteria))
                {
                    collTOptions = TOptionPeer.doSelect(criteria);
                }
            }
        }
        lastTOptionsCriteria = criteria;

        return collTOptions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOptions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOption> getTOptions(Connection con) throws TorqueException
    {
        if (collTOptions == null)
        {
            collTOptions = getTOptions(new Criteria(10), con);
        }
        return collTOptions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TList has previously
     * been saved, it will retrieve related TOptions from storage.
     * If this TList is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOption> getTOptions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTOptions == null)
        {
            if (isNew())
            {
               collTOptions = new ArrayList<TOption>();
            }
            else
            {
                 criteria.add(TOptionPeer.LIST, getObjectID());
                 collTOptions = TOptionPeer.doSelect(criteria, con);
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
                 criteria.add(TOptionPeer.LIST, getObjectID());
                 if (!lastTOptionsCriteria.equals(criteria))
                 {
                     collTOptions = TOptionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTOptionsCriteria = criteria;

         return collTOptions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TList is new, it will return
     * an empty collection; or if this TList has previously
     * been saved, it will retrieve related TOptions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TList.
     */
    protected List<TOption> getTOptionsJoinTList(Criteria criteria)
        throws TorqueException
    {
        if (collTOptions == null)
        {
            if (isNew())
            {
               collTOptions = new ArrayList<TOption>();
            }
            else
            {
                criteria.add(TOptionPeer.LIST, getObjectID());
                collTOptions = TOptionPeer.doSelectJoinTList(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOptionPeer.LIST, getObjectID());
            if (!lastTOptionsCriteria.equals(criteria))
            {
                collTOptions = TOptionPeer.doSelectJoinTList(criteria);
            }
        }
        lastTOptionsCriteria = criteria;

        return collTOptions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TList is new, it will return
     * an empty collection; or if this TList has previously
     * been saved, it will retrieve related TOptions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TList.
     */
    protected List<TOption> getTOptionsJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTOptions == null)
        {
            if (isNew())
            {
               collTOptions = new ArrayList<TOption>();
            }
            else
            {
                criteria.add(TOptionPeer.LIST, getObjectID());
                collTOptions = TOptionPeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOptionPeer.LIST, getObjectID());
            if (!lastTOptionsCriteria.equals(criteria))
            {
                collTOptions = TOptionPeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTOptionsCriteria = criteria;

        return collTOptions;
    }





    /**
     * Collection to store aggregation of collTOptionSettingss
     */
    protected List<TOptionSettings> collTOptionSettingss;

    /**
     * Temporary storage of collTOptionSettingss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTOptionSettingss()
    {
        if (collTOptionSettingss == null)
        {
            collTOptionSettingss = new ArrayList<TOptionSettings>();
        }
    }


    /**
     * Method called to associate a TOptionSettings object to this object
     * through the TOptionSettings foreign key attribute
     *
     * @param l TOptionSettings
     * @throws TorqueException
     */
    public void addTOptionSettings(TOptionSettings l) throws TorqueException
    {
        getTOptionSettingss().add(l);
        l.setTList((TList) this);
    }

    /**
     * Method called to associate a TOptionSettings object to this object
     * through the TOptionSettings foreign key attribute using connection.
     *
     * @param l TOptionSettings
     * @throws TorqueException
     */
    public void addTOptionSettings(TOptionSettings l, Connection con) throws TorqueException
    {
        getTOptionSettingss(con).add(l);
        l.setTList((TList) this);
    }

    /**
     * The criteria used to select the current contents of collTOptionSettingss
     */
    private Criteria lastTOptionSettingssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOptionSettingss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TOptionSettings> getTOptionSettingss()
        throws TorqueException
    {
        if (collTOptionSettingss == null)
        {
            collTOptionSettingss = getTOptionSettingss(new Criteria(10));
        }
        return collTOptionSettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TList has previously
     * been saved, it will retrieve related TOptionSettingss from storage.
     * If this TList is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TOptionSettings> getTOptionSettingss(Criteria criteria) throws TorqueException
    {
        if (collTOptionSettingss == null)
        {
            if (isNew())
            {
               collTOptionSettingss = new ArrayList<TOptionSettings>();
            }
            else
            {
                criteria.add(TOptionSettingsPeer.LIST, getObjectID() );
                collTOptionSettingss = TOptionSettingsPeer.doSelect(criteria);
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
                criteria.add(TOptionSettingsPeer.LIST, getObjectID());
                if (!lastTOptionSettingssCriteria.equals(criteria))
                {
                    collTOptionSettingss = TOptionSettingsPeer.doSelect(criteria);
                }
            }
        }
        lastTOptionSettingssCriteria = criteria;

        return collTOptionSettingss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOptionSettingss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOptionSettings> getTOptionSettingss(Connection con) throws TorqueException
    {
        if (collTOptionSettingss == null)
        {
            collTOptionSettingss = getTOptionSettingss(new Criteria(10), con);
        }
        return collTOptionSettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TList has previously
     * been saved, it will retrieve related TOptionSettingss from storage.
     * If this TList is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOptionSettings> getTOptionSettingss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTOptionSettingss == null)
        {
            if (isNew())
            {
               collTOptionSettingss = new ArrayList<TOptionSettings>();
            }
            else
            {
                 criteria.add(TOptionSettingsPeer.LIST, getObjectID());
                 collTOptionSettingss = TOptionSettingsPeer.doSelect(criteria, con);
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
                 criteria.add(TOptionSettingsPeer.LIST, getObjectID());
                 if (!lastTOptionSettingssCriteria.equals(criteria))
                 {
                     collTOptionSettingss = TOptionSettingsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTOptionSettingssCriteria = criteria;

         return collTOptionSettingss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TList is new, it will return
     * an empty collection; or if this TList has previously
     * been saved, it will retrieve related TOptionSettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TList.
     */
    protected List<TOptionSettings> getTOptionSettingssJoinTList(Criteria criteria)
        throws TorqueException
    {
        if (collTOptionSettingss == null)
        {
            if (isNew())
            {
               collTOptionSettingss = new ArrayList<TOptionSettings>();
            }
            else
            {
                criteria.add(TOptionSettingsPeer.LIST, getObjectID());
                collTOptionSettingss = TOptionSettingsPeer.doSelectJoinTList(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOptionSettingsPeer.LIST, getObjectID());
            if (!lastTOptionSettingssCriteria.equals(criteria))
            {
                collTOptionSettingss = TOptionSettingsPeer.doSelectJoinTList(criteria);
            }
        }
        lastTOptionSettingssCriteria = criteria;

        return collTOptionSettingss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TList is new, it will return
     * an empty collection; or if this TList has previously
     * been saved, it will retrieve related TOptionSettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TList.
     */
    protected List<TOptionSettings> getTOptionSettingssJoinTFieldConfig(Criteria criteria)
        throws TorqueException
    {
        if (collTOptionSettingss == null)
        {
            if (isNew())
            {
               collTOptionSettingss = new ArrayList<TOptionSettings>();
            }
            else
            {
                criteria.add(TOptionSettingsPeer.LIST, getObjectID());
                collTOptionSettingss = TOptionSettingsPeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOptionSettingsPeer.LIST, getObjectID());
            if (!lastTOptionSettingssCriteria.equals(criteria))
            {
                collTOptionSettingss = TOptionSettingsPeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        lastTOptionSettingssCriteria = criteria;

        return collTOptionSettingss;
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
            fieldNames.add("Description");
            fieldNames.add("TagLabel");
            fieldNames.add("ParentList");
            fieldNames.add("ListType");
            fieldNames.add("ChildNumber");
            fieldNames.add("Deleted");
            fieldNames.add("RepositoryType");
            fieldNames.add("Project");
            fieldNames.add("Owner");
            fieldNames.add("MoreProps");
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
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("TagLabel"))
        {
            return getTagLabel();
        }
        if (name.equals("ParentList"))
        {
            return getParentList();
        }
        if (name.equals("ListType"))
        {
            return getListType();
        }
        if (name.equals("ChildNumber"))
        {
            return getChildNumber();
        }
        if (name.equals("Deleted"))
        {
            return getDeleted();
        }
        if (name.equals("RepositoryType"))
        {
            return getRepositoryType();
        }
        if (name.equals("Project"))
        {
            return getProject();
        }
        if (name.equals("Owner"))
        {
            return getOwner();
        }
        if (name.equals("MoreProps"))
        {
            return getMoreProps();
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
        if (name.equals("TagLabel"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTagLabel((String) value);
            return true;
        }
        if (name.equals("ParentList"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParentList((Integer) value);
            return true;
        }
        if (name.equals("ListType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setListType((Integer) value);
            return true;
        }
        if (name.equals("ChildNumber"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setChildNumber((Integer) value);
            return true;
        }
        if (name.equals("Deleted"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDeleted((String) value);
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
        if (name.equals("MoreProps"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMoreProps((String) value);
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
        if (name.equals(TListPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TListPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TListPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TListPeer.TAGLABEL))
        {
            return getTagLabel();
        }
        if (name.equals(TListPeer.PARENTLIST))
        {
            return getParentList();
        }
        if (name.equals(TListPeer.LISTTYPE))
        {
            return getListType();
        }
        if (name.equals(TListPeer.CHILDNUMBER))
        {
            return getChildNumber();
        }
        if (name.equals(TListPeer.DELETED))
        {
            return getDeleted();
        }
        if (name.equals(TListPeer.REPOSITORYTYPE))
        {
            return getRepositoryType();
        }
        if (name.equals(TListPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TListPeer.OWNER))
        {
            return getOwner();
        }
        if (name.equals(TListPeer.MOREPROPS))
        {
            return getMoreProps();
        }
        if (name.equals(TListPeer.TPUUID))
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
      if (TListPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TListPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TListPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TListPeer.TAGLABEL.equals(name))
        {
            return setByName("TagLabel", value);
        }
      if (TListPeer.PARENTLIST.equals(name))
        {
            return setByName("ParentList", value);
        }
      if (TListPeer.LISTTYPE.equals(name))
        {
            return setByName("ListType", value);
        }
      if (TListPeer.CHILDNUMBER.equals(name))
        {
            return setByName("ChildNumber", value);
        }
      if (TListPeer.DELETED.equals(name))
        {
            return setByName("Deleted", value);
        }
      if (TListPeer.REPOSITORYTYPE.equals(name))
        {
            return setByName("RepositoryType", value);
        }
      if (TListPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TListPeer.OWNER.equals(name))
        {
            return setByName("Owner", value);
        }
      if (TListPeer.MOREPROPS.equals(name))
        {
            return setByName("MoreProps", value);
        }
      if (TListPeer.TPUUID.equals(name))
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
            return getDescription();
        }
        if (pos == 3)
        {
            return getTagLabel();
        }
        if (pos == 4)
        {
            return getParentList();
        }
        if (pos == 5)
        {
            return getListType();
        }
        if (pos == 6)
        {
            return getChildNumber();
        }
        if (pos == 7)
        {
            return getDeleted();
        }
        if (pos == 8)
        {
            return getRepositoryType();
        }
        if (pos == 9)
        {
            return getProject();
        }
        if (pos == 10)
        {
            return getOwner();
        }
        if (pos == 11)
        {
            return getMoreProps();
        }
        if (pos == 12)
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
            return setByName("Description", value);
        }
    if (position == 3)
        {
            return setByName("TagLabel", value);
        }
    if (position == 4)
        {
            return setByName("ParentList", value);
        }
    if (position == 5)
        {
            return setByName("ListType", value);
        }
    if (position == 6)
        {
            return setByName("ChildNumber", value);
        }
    if (position == 7)
        {
            return setByName("Deleted", value);
        }
    if (position == 8)
        {
            return setByName("RepositoryType", value);
        }
    if (position == 9)
        {
            return setByName("Project", value);
        }
    if (position == 10)
        {
            return setByName("Owner", value);
        }
    if (position == 11)
        {
            return setByName("MoreProps", value);
        }
    if (position == 12)
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
        save(TListPeer.DATABASE_NAME);
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
                    TListPeer.doInsert((TList) this, con);
                    setNew(false);
                }
                else
                {
                    TListPeer.doUpdate((TList) this, con);
                }
            }


            if (collTOptions != null)
            {
                for (int i = 0; i < collTOptions.size(); i++)
                {
                    ((TOption) collTOptions.get(i)).save(con);
                }
            }

            if (collTOptionSettingss != null)
            {
                for (int i = 0; i < collTOptionSettingss.size(); i++)
                {
                    ((TOptionSettings) collTOptionSettingss.get(i)).save(con);
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
    public TList copy() throws TorqueException
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
    public TList copy(Connection con) throws TorqueException
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
    public TList copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TList(), deepcopy);
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
    public TList copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TList(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TList copyInto(TList copyObj) throws TorqueException
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
    protected TList copyInto(TList copyObj, Connection con) throws TorqueException
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
    protected TList copyInto(TList copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setDescription(description);
        copyObj.setTagLabel(tagLabel);
        copyObj.setParentList(parentList);
        copyObj.setListType(listType);
        copyObj.setChildNumber(childNumber);
        copyObj.setDeleted(deleted);
        copyObj.setRepositoryType(repositoryType);
        copyObj.setProject(project);
        copyObj.setOwner(owner);
        copyObj.setMoreProps(moreProps);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TOption> vTOptions = getTOptions();
        if (vTOptions != null)
        {
            for (int i = 0; i < vTOptions.size(); i++)
            {
                TOption obj =  vTOptions.get(i);
                copyObj.addTOption(obj.copy());
            }
        }
        else
        {
            copyObj.collTOptions = null;
        }


        List<TOptionSettings> vTOptionSettingss = getTOptionSettingss();
        if (vTOptionSettingss != null)
        {
            for (int i = 0; i < vTOptionSettingss.size(); i++)
            {
                TOptionSettings obj =  vTOptionSettingss.get(i);
                copyObj.addTOptionSettings(obj.copy());
            }
        }
        else
        {
            copyObj.collTOptionSettingss = null;
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
    protected TList copyInto(TList copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setDescription(description);
        copyObj.setTagLabel(tagLabel);
        copyObj.setParentList(parentList);
        copyObj.setListType(listType);
        copyObj.setChildNumber(childNumber);
        copyObj.setDeleted(deleted);
        copyObj.setRepositoryType(repositoryType);
        copyObj.setProject(project);
        copyObj.setOwner(owner);
        copyObj.setMoreProps(moreProps);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TOption> vTOptions = getTOptions(con);
        if (vTOptions != null)
        {
            for (int i = 0; i < vTOptions.size(); i++)
            {
                TOption obj =  vTOptions.get(i);
                copyObj.addTOption(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTOptions = null;
        }


        List<TOptionSettings> vTOptionSettingss = getTOptionSettingss(con);
        if (vTOptionSettingss != null)
        {
            for (int i = 0; i < vTOptionSettingss.size(); i++)
            {
                TOptionSettings obj =  vTOptionSettingss.get(i);
                copyObj.addTOptionSettings(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTOptionSettingss = null;
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
    public TListPeer getPeer()
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
        return TListPeer.getTableMap();
    }

  
    /**
     * Creates a TListBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TListBean with the contents of this object
     */
    public TListBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TListBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TListBean with the contents of this object
     */
    public TListBean getBean(IdentityMap createdBeans)
    {
        TListBean result = (TListBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TListBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setTagLabel(getTagLabel());
        result.setParentList(getParentList());
        result.setListType(getListType());
        result.setChildNumber(getChildNumber());
        result.setDeleted(getDeleted());
        result.setRepositoryType(getRepositoryType());
        result.setProject(getProject());
        result.setOwner(getOwner());
        result.setMoreProps(getMoreProps());
        result.setUuid(getUuid());



        if (collTOptions != null)
        {
            List<TOptionBean> relatedBeans = new ArrayList<TOptionBean>(collTOptions.size());
            for (Iterator<TOption> collTOptionsIt = collTOptions.iterator(); collTOptionsIt.hasNext(); )
            {
                TOption related = (TOption) collTOptionsIt.next();
                TOptionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTOptionBeans(relatedBeans);
        }


        if (collTOptionSettingss != null)
        {
            List<TOptionSettingsBean> relatedBeans = new ArrayList<TOptionSettingsBean>(collTOptionSettingss.size());
            for (Iterator<TOptionSettings> collTOptionSettingssIt = collTOptionSettingss.iterator(); collTOptionSettingssIt.hasNext(); )
            {
                TOptionSettings related = (TOptionSettings) collTOptionSettingssIt.next();
                TOptionSettingsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTOptionSettingsBeans(relatedBeans);
        }




        if (aTListRelatedByParentList != null)
        {
            TListBean relatedBean = aTListRelatedByParentList.getBean(createdBeans);
            result.setTListBeanRelatedByParentList(relatedBean);
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
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TList with the contents
     * of a TListBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TListBean which contents are used to create
     *        the resulting class
     * @return an instance of TList with the contents of bean
     */
    public static TList createTList(TListBean bean)
        throws TorqueException
    {
        return createTList(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TList with the contents
     * of a TListBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TListBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TList with the contents of bean
     */

    public static TList createTList(TListBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TList result = (TList) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TList();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setDescription(bean.getDescription());
        result.setTagLabel(bean.getTagLabel());
        result.setParentList(bean.getParentList());
        result.setListType(bean.getListType());
        result.setChildNumber(bean.getChildNumber());
        result.setDeleted(bean.getDeleted());
        result.setRepositoryType(bean.getRepositoryType());
        result.setProject(bean.getProject());
        result.setOwner(bean.getOwner());
        result.setMoreProps(bean.getMoreProps());
        result.setUuid(bean.getUuid());



        {
            List<TOptionBean> relatedBeans = bean.getTOptionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TOptionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TOptionBean relatedBean =  relatedBeansIt.next();
                    TOption related = TOption.createTOption(relatedBean, createdObjects);
                    result.addTOptionFromBean(related);
                }
            }
        }


        {
            List<TOptionSettingsBean> relatedBeans = bean.getTOptionSettingsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TOptionSettingsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TOptionSettingsBean relatedBean =  relatedBeansIt.next();
                    TOptionSettings related = TOptionSettings.createTOptionSettings(relatedBean, createdObjects);
                    result.addTOptionSettingsFromBean(related);
                }
            }
        }




        {
            TListBean relatedBean = bean.getTListBeanRelatedByParentList();
            if (relatedBean != null)
            {
                TList relatedObject = TList.createTList(relatedBean, createdObjects);
                result.setTListRelatedByParentList(relatedObject);
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
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TOption object to this object.
     * through the TOption foreign key attribute
     *
     * @param toAdd TOption
     */
    protected void addTOptionFromBean(TOption toAdd)
    {
        initTOptions();
        collTOptions.add(toAdd);
    }


    /**
     * Method called to associate a TOptionSettings object to this object.
     * through the TOptionSettings foreign key attribute
     *
     * @param toAdd TOptionSettings
     */
    protected void addTOptionSettingsFromBean(TOptionSettings toAdd)
    {
        initTOptionSettingss();
        collTOptionSettingss.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TList:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("TagLabel = ")
           .append(getTagLabel())
           .append("\n");
        str.append("ParentList = ")
           .append(getParentList())
           .append("\n");
        str.append("ListType = ")
           .append(getListType())
           .append("\n");
        str.append("ChildNumber = ")
           .append(getChildNumber())
           .append("\n");
        str.append("Deleted = ")
           .append(getDeleted())
           .append("\n");
        str.append("RepositoryType = ")
           .append(getRepositoryType())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("Owner = ")
           .append(getOwner())
           .append("\n");
        str.append("MoreProps = ")
           .append(getMoreProps())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
