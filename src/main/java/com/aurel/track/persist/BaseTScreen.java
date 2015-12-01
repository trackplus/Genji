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
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TProjectBean;

import com.aurel.track.beans.TScreenTabBean;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.beans.TWorkflowActivityBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TScreen
 */
public abstract class BaseTScreen extends TpBaseObject
{
    /** The Peer class */
    private static final TScreenPeer peer =
        new TScreenPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the label field */
    private String label;

    /** The value for the tagLabel field */
    private String tagLabel;

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



        // update associated TScreenTab
        if (collTScreenTabs != null)
        {
            for (int i = 0; i < collTScreenTabs.size(); i++)
            {
                ((TScreenTab) collTScreenTabs.get(i))
                        .setParent(v);
            }
        }

        // update associated TScreenConfig
        if (collTScreenConfigs != null)
        {
            for (int i = 0; i < collTScreenConfigs.size(); i++)
            {
                ((TScreenConfig) collTScreenConfigs.get(i))
                        .setScreen(v);
            }
        }

        // update associated TWorkflowActivity
        if (collTWorkflowActivitys != null)
        {
            for (int i = 0; i < collTWorkflowActivitys.size(); i++)
            {
                ((TWorkflowActivity) collTWorkflowActivitys.get(i))
                        .setScreen(v);
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
     * Collection to store aggregation of collTScreenTabs
     */
    protected List<TScreenTab> collTScreenTabs;

    /**
     * Temporary storage of collTScreenTabs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScreenTabs()
    {
        if (collTScreenTabs == null)
        {
            collTScreenTabs = new ArrayList<TScreenTab>();
        }
    }


    /**
     * Method called to associate a TScreenTab object to this object
     * through the TScreenTab foreign key attribute
     *
     * @param l TScreenTab
     * @throws TorqueException
     */
    public void addTScreenTab(TScreenTab l) throws TorqueException
    {
        getTScreenTabs().add(l);
        l.setTScreen((TScreen) this);
    }

    /**
     * Method called to associate a TScreenTab object to this object
     * through the TScreenTab foreign key attribute using connection.
     *
     * @param l TScreenTab
     * @throws TorqueException
     */
    public void addTScreenTab(TScreenTab l, Connection con) throws TorqueException
    {
        getTScreenTabs(con).add(l);
        l.setTScreen((TScreen) this);
    }

    /**
     * The criteria used to select the current contents of collTScreenTabs
     */
    private Criteria lastTScreenTabsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenTabs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScreenTab> getTScreenTabs()
        throws TorqueException
    {
        if (collTScreenTabs == null)
        {
            collTScreenTabs = getTScreenTabs(new Criteria(10));
        }
        return collTScreenTabs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen has previously
     * been saved, it will retrieve related TScreenTabs from storage.
     * If this TScreen is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScreenTab> getTScreenTabs(Criteria criteria) throws TorqueException
    {
        if (collTScreenTabs == null)
        {
            if (isNew())
            {
               collTScreenTabs = new ArrayList<TScreenTab>();
            }
            else
            {
                criteria.add(TScreenTabPeer.PARENT, getObjectID() );
                collTScreenTabs = TScreenTabPeer.doSelect(criteria);
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
                criteria.add(TScreenTabPeer.PARENT, getObjectID());
                if (!lastTScreenTabsCriteria.equals(criteria))
                {
                    collTScreenTabs = TScreenTabPeer.doSelect(criteria);
                }
            }
        }
        lastTScreenTabsCriteria = criteria;

        return collTScreenTabs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenTabs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenTab> getTScreenTabs(Connection con) throws TorqueException
    {
        if (collTScreenTabs == null)
        {
            collTScreenTabs = getTScreenTabs(new Criteria(10), con);
        }
        return collTScreenTabs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen has previously
     * been saved, it will retrieve related TScreenTabs from storage.
     * If this TScreen is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenTab> getTScreenTabs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScreenTabs == null)
        {
            if (isNew())
            {
               collTScreenTabs = new ArrayList<TScreenTab>();
            }
            else
            {
                 criteria.add(TScreenTabPeer.PARENT, getObjectID());
                 collTScreenTabs = TScreenTabPeer.doSelect(criteria, con);
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
                 criteria.add(TScreenTabPeer.PARENT, getObjectID());
                 if (!lastTScreenTabsCriteria.equals(criteria))
                 {
                     collTScreenTabs = TScreenTabPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScreenTabsCriteria = criteria;

         return collTScreenTabs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TScreenTabs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TScreenTab> getTScreenTabsJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenTabs == null)
        {
            if (isNew())
            {
               collTScreenTabs = new ArrayList<TScreenTab>();
            }
            else
            {
                criteria.add(TScreenTabPeer.PARENT, getObjectID());
                collTScreenTabs = TScreenTabPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenTabPeer.PARENT, getObjectID());
            if (!lastTScreenTabsCriteria.equals(criteria))
            {
                collTScreenTabs = TScreenTabPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTScreenTabsCriteria = criteria;

        return collTScreenTabs;
    }





    /**
     * Collection to store aggregation of collTScreenConfigs
     */
    protected List<TScreenConfig> collTScreenConfigs;

    /**
     * Temporary storage of collTScreenConfigs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScreenConfigs()
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = new ArrayList<TScreenConfig>();
        }
    }


    /**
     * Method called to associate a TScreenConfig object to this object
     * through the TScreenConfig foreign key attribute
     *
     * @param l TScreenConfig
     * @throws TorqueException
     */
    public void addTScreenConfig(TScreenConfig l) throws TorqueException
    {
        getTScreenConfigs().add(l);
        l.setTScreen((TScreen) this);
    }

    /**
     * Method called to associate a TScreenConfig object to this object
     * through the TScreenConfig foreign key attribute using connection.
     *
     * @param l TScreenConfig
     * @throws TorqueException
     */
    public void addTScreenConfig(TScreenConfig l, Connection con) throws TorqueException
    {
        getTScreenConfigs(con).add(l);
        l.setTScreen((TScreen) this);
    }

    /**
     * The criteria used to select the current contents of collTScreenConfigs
     */
    private Criteria lastTScreenConfigsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenConfigs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScreenConfig> getTScreenConfigs()
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = getTScreenConfigs(new Criteria(10));
        }
        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     * If this TScreen is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScreenConfig> getTScreenConfigs(Criteria criteria) throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.SCREEN, getObjectID() );
                collTScreenConfigs = TScreenConfigPeer.doSelect(criteria);
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
                criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
                if (!lastTScreenConfigsCriteria.equals(criteria))
                {
                    collTScreenConfigs = TScreenConfigPeer.doSelect(criteria);
                }
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenConfigs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenConfig> getTScreenConfigs(Connection con) throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = getTScreenConfigs(new Criteria(10), con);
        }
        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     * If this TScreen is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenConfig> getTScreenConfigs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                 criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
                 collTScreenConfigs = TScreenConfigPeer.doSelect(criteria, con);
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
                 criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
                 if (!lastTScreenConfigsCriteria.equals(criteria))
                 {
                     collTScreenConfigs = TScreenConfigPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScreenConfigsCriteria = criteria;

         return collTScreenConfigs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTAction(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTAction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.SCREEN, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTAction(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }





    /**
     * Collection to store aggregation of collTWorkflowActivitys
     */
    protected List<TWorkflowActivity> collTWorkflowActivitys;

    /**
     * Temporary storage of collTWorkflowActivitys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowActivitys()
    {
        if (collTWorkflowActivitys == null)
        {
            collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
        }
    }


    /**
     * Method called to associate a TWorkflowActivity object to this object
     * through the TWorkflowActivity foreign key attribute
     *
     * @param l TWorkflowActivity
     * @throws TorqueException
     */
    public void addTWorkflowActivity(TWorkflowActivity l) throws TorqueException
    {
        getTWorkflowActivitys().add(l);
        l.setTScreen((TScreen) this);
    }

    /**
     * Method called to associate a TWorkflowActivity object to this object
     * through the TWorkflowActivity foreign key attribute using connection.
     *
     * @param l TWorkflowActivity
     * @throws TorqueException
     */
    public void addTWorkflowActivity(TWorkflowActivity l, Connection con) throws TorqueException
    {
        getTWorkflowActivitys(con).add(l);
        l.setTScreen((TScreen) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowActivitys
     */
    private Criteria lastTWorkflowActivitysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowActivitys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowActivity> getTWorkflowActivitys()
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            collTWorkflowActivitys = getTWorkflowActivitys(new Criteria(10));
        }
        return collTWorkflowActivitys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     * If this TScreen is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowActivity> getTWorkflowActivitys(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID() );
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelect(criteria);
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
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                if (!lastTWorkflowActivitysCriteria.equals(criteria))
                {
                    collTWorkflowActivitys = TWorkflowActivityPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowActivitys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowActivity> getTWorkflowActivitys(Connection con) throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            collTWorkflowActivitys = getTWorkflowActivitys(new Criteria(10), con);
        }
        return collTWorkflowActivitys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     * If this TScreen is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowActivity> getTWorkflowActivitys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                 criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                 collTWorkflowActivitys = TWorkflowActivityPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                 if (!lastTWorkflowActivitysCriteria.equals(criteria))
                 {
                     collTWorkflowActivitys = TWorkflowActivityPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowActivitysCriteria = criteria;

         return collTWorkflowActivitys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTWorkflowStationRelatedByStationEntryActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTWorkflowStationRelatedByStationExitActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTWorkflowStationRelatedByStationDoActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTPersonRelatedByNewMan(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTPersonRelatedByNewResp(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTSLA(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreen is new, it will return
     * an empty collection; or if this TScreen has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreen.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SCREEN, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
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
            fieldNames.add("Label");
            fieldNames.add("TagLabel");
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
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("TagLabel"))
        {
            return getTagLabel();
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
        if (name.equals(TScreenPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TScreenPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TScreenPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TScreenPeer.TAGLABEL))
        {
            return getTagLabel();
        }
        if (name.equals(TScreenPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TScreenPeer.OWNER))
        {
            return getOwner();
        }
        if (name.equals(TScreenPeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TScreenPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TScreenPeer.TPUUID))
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
      if (TScreenPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TScreenPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TScreenPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TScreenPeer.TAGLABEL.equals(name))
        {
            return setByName("TagLabel", value);
        }
      if (TScreenPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TScreenPeer.OWNER.equals(name))
        {
            return setByName("Owner", value);
        }
      if (TScreenPeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TScreenPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TScreenPeer.TPUUID.equals(name))
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
            return getLabel();
        }
        if (pos == 3)
        {
            return getTagLabel();
        }
        if (pos == 4)
        {
            return getDescription();
        }
        if (pos == 5)
        {
            return getOwner();
        }
        if (pos == 6)
        {
            return getProjectType();
        }
        if (pos == 7)
        {
            return getProject();
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
            return setByName("Name", value);
        }
    if (position == 2)
        {
            return setByName("Label", value);
        }
    if (position == 3)
        {
            return setByName("TagLabel", value);
        }
    if (position == 4)
        {
            return setByName("Description", value);
        }
    if (position == 5)
        {
            return setByName("Owner", value);
        }
    if (position == 6)
        {
            return setByName("ProjectType", value);
        }
    if (position == 7)
        {
            return setByName("Project", value);
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
        save(TScreenPeer.DATABASE_NAME);
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
                    TScreenPeer.doInsert((TScreen) this, con);
                    setNew(false);
                }
                else
                {
                    TScreenPeer.doUpdate((TScreen) this, con);
                }
            }


            if (collTScreenTabs != null)
            {
                for (int i = 0; i < collTScreenTabs.size(); i++)
                {
                    ((TScreenTab) collTScreenTabs.get(i)).save(con);
                }
            }

            if (collTScreenConfigs != null)
            {
                for (int i = 0; i < collTScreenConfigs.size(); i++)
                {
                    ((TScreenConfig) collTScreenConfigs.get(i)).save(con);
                }
            }

            if (collTWorkflowActivitys != null)
            {
                for (int i = 0; i < collTWorkflowActivitys.size(); i++)
                {
                    ((TWorkflowActivity) collTWorkflowActivitys.get(i)).save(con);
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
    public TScreen copy() throws TorqueException
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
    public TScreen copy(Connection con) throws TorqueException
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
    public TScreen copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TScreen(), deepcopy);
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
    public TScreen copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TScreen(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TScreen copyInto(TScreen copyObj) throws TorqueException
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
    protected TScreen copyInto(TScreen copyObj, Connection con) throws TorqueException
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
    protected TScreen copyInto(TScreen copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setLabel(label);
        copyObj.setTagLabel(tagLabel);
        copyObj.setDescription(description);
        copyObj.setOwner(owner);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TScreenTab> vTScreenTabs = getTScreenTabs();
        if (vTScreenTabs != null)
        {
            for (int i = 0; i < vTScreenTabs.size(); i++)
            {
                TScreenTab obj =  vTScreenTabs.get(i);
                copyObj.addTScreenTab(obj.copy());
            }
        }
        else
        {
            copyObj.collTScreenTabs = null;
        }


        List<TScreenConfig> vTScreenConfigs = getTScreenConfigs();
        if (vTScreenConfigs != null)
        {
            for (int i = 0; i < vTScreenConfigs.size(); i++)
            {
                TScreenConfig obj =  vTScreenConfigs.get(i);
                copyObj.addTScreenConfig(obj.copy());
            }
        }
        else
        {
            copyObj.collTScreenConfigs = null;
        }


        List<TWorkflowActivity> vTWorkflowActivitys = getTWorkflowActivitys();
        if (vTWorkflowActivitys != null)
        {
            for (int i = 0; i < vTWorkflowActivitys.size(); i++)
            {
                TWorkflowActivity obj =  vTWorkflowActivitys.get(i);
                copyObj.addTWorkflowActivity(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowActivitys = null;
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
    protected TScreen copyInto(TScreen copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setLabel(label);
        copyObj.setTagLabel(tagLabel);
        copyObj.setDescription(description);
        copyObj.setOwner(owner);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TScreenTab> vTScreenTabs = getTScreenTabs(con);
        if (vTScreenTabs != null)
        {
            for (int i = 0; i < vTScreenTabs.size(); i++)
            {
                TScreenTab obj =  vTScreenTabs.get(i);
                copyObj.addTScreenTab(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScreenTabs = null;
        }


        List<TScreenConfig> vTScreenConfigs = getTScreenConfigs(con);
        if (vTScreenConfigs != null)
        {
            for (int i = 0; i < vTScreenConfigs.size(); i++)
            {
                TScreenConfig obj =  vTScreenConfigs.get(i);
                copyObj.addTScreenConfig(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScreenConfigs = null;
        }


        List<TWorkflowActivity> vTWorkflowActivitys = getTWorkflowActivitys(con);
        if (vTWorkflowActivitys != null)
        {
            for (int i = 0; i < vTWorkflowActivitys.size(); i++)
            {
                TWorkflowActivity obj =  vTWorkflowActivitys.get(i);
                copyObj.addTWorkflowActivity(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowActivitys = null;
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
    public TScreenPeer getPeer()
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
        return TScreenPeer.getTableMap();
    }

  
    /**
     * Creates a TScreenBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TScreenBean with the contents of this object
     */
    public TScreenBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TScreenBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TScreenBean with the contents of this object
     */
    public TScreenBean getBean(IdentityMap createdBeans)
    {
        TScreenBean result = (TScreenBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TScreenBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setLabel(getLabel());
        result.setTagLabel(getTagLabel());
        result.setDescription(getDescription());
        result.setOwner(getOwner());
        result.setProjectType(getProjectType());
        result.setProject(getProject());
        result.setUuid(getUuid());



        if (collTScreenTabs != null)
        {
            List<TScreenTabBean> relatedBeans = new ArrayList<TScreenTabBean>(collTScreenTabs.size());
            for (Iterator<TScreenTab> collTScreenTabsIt = collTScreenTabs.iterator(); collTScreenTabsIt.hasNext(); )
            {
                TScreenTab related = (TScreenTab) collTScreenTabsIt.next();
                TScreenTabBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScreenTabBeans(relatedBeans);
        }


        if (collTScreenConfigs != null)
        {
            List<TScreenConfigBean> relatedBeans = new ArrayList<TScreenConfigBean>(collTScreenConfigs.size());
            for (Iterator<TScreenConfig> collTScreenConfigsIt = collTScreenConfigs.iterator(); collTScreenConfigsIt.hasNext(); )
            {
                TScreenConfig related = (TScreenConfig) collTScreenConfigsIt.next();
                TScreenConfigBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScreenConfigBeans(relatedBeans);
        }


        if (collTWorkflowActivitys != null)
        {
            List<TWorkflowActivityBean> relatedBeans = new ArrayList<TWorkflowActivityBean>(collTWorkflowActivitys.size());
            for (Iterator<TWorkflowActivity> collTWorkflowActivitysIt = collTWorkflowActivitys.iterator(); collTWorkflowActivitysIt.hasNext(); )
            {
                TWorkflowActivity related = (TWorkflowActivity) collTWorkflowActivitysIt.next();
                TWorkflowActivityBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowActivityBeans(relatedBeans);
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
     * Creates an instance of TScreen with the contents
     * of a TScreenBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TScreenBean which contents are used to create
     *        the resulting class
     * @return an instance of TScreen with the contents of bean
     */
    public static TScreen createTScreen(TScreenBean bean)
        throws TorqueException
    {
        return createTScreen(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TScreen with the contents
     * of a TScreenBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TScreenBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TScreen with the contents of bean
     */

    public static TScreen createTScreen(TScreenBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TScreen result = (TScreen) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TScreen();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setLabel(bean.getLabel());
        result.setTagLabel(bean.getTagLabel());
        result.setDescription(bean.getDescription());
        result.setOwner(bean.getOwner());
        result.setProjectType(bean.getProjectType());
        result.setProject(bean.getProject());
        result.setUuid(bean.getUuid());



        {
            List<TScreenTabBean> relatedBeans = bean.getTScreenTabBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScreenTabBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScreenTabBean relatedBean =  relatedBeansIt.next();
                    TScreenTab related = TScreenTab.createTScreenTab(relatedBean, createdObjects);
                    result.addTScreenTabFromBean(related);
                }
            }
        }


        {
            List<TScreenConfigBean> relatedBeans = bean.getTScreenConfigBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScreenConfigBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScreenConfigBean relatedBean =  relatedBeansIt.next();
                    TScreenConfig related = TScreenConfig.createTScreenConfig(relatedBean, createdObjects);
                    result.addTScreenConfigFromBean(related);
                }
            }
        }


        {
            List<TWorkflowActivityBean> relatedBeans = bean.getTWorkflowActivityBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowActivityBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowActivityBean relatedBean =  relatedBeansIt.next();
                    TWorkflowActivity related = TWorkflowActivity.createTWorkflowActivity(relatedBean, createdObjects);
                    result.addTWorkflowActivityFromBean(related);
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
     * Method called to associate a TScreenTab object to this object.
     * through the TScreenTab foreign key attribute
     *
     * @param toAdd TScreenTab
     */
    protected void addTScreenTabFromBean(TScreenTab toAdd)
    {
        initTScreenTabs();
        collTScreenTabs.add(toAdd);
    }


    /**
     * Method called to associate a TScreenConfig object to this object.
     * through the TScreenConfig foreign key attribute
     *
     * @param toAdd TScreenConfig
     */
    protected void addTScreenConfigFromBean(TScreenConfig toAdd)
    {
        initTScreenConfigs();
        collTScreenConfigs.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowActivity object to this object.
     * through the TWorkflowActivity foreign key attribute
     *
     * @param toAdd TWorkflowActivity
     */
    protected void addTWorkflowActivityFromBean(TWorkflowActivity toAdd)
    {
        initTWorkflowActivitys();
        collTWorkflowActivitys.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TScreen:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("TagLabel = ")
           .append(getTagLabel())
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
