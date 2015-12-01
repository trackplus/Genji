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
import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TProjectBean;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.beans.TWorkflowGuardBean;


/**
 * Contains Groovy scripts
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TScripts
 */
public abstract class BaseTScripts extends TpBaseObject
{
    /** The Peer class */
    private static final TScriptsPeer peer =
        new TScriptsPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the person field */
    private Integer person;

    /** The value for the lastEdit field */
    private Date lastEdit;

    /** The value for the scriptVersion field */
    private Integer scriptVersion;

    /** The value for the originalVersion field */
    private Integer originalVersion;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

    /** The value for the scriptType field */
    private Integer scriptType;

    /** The value for the scriptRole field */
    private Integer scriptRole;

    /** The value for the clazzName field */
    private String clazzName;

    /** The value for the sourceCode field */
    private String sourceCode;

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
                        .setGroovyScript(v);
            }
        }

        // update associated TWorkflowActivity
        if (collTWorkflowActivitys != null)
        {
            for (int i = 0; i < collTWorkflowActivitys.size(); i++)
            {
                ((TWorkflowActivity) collTWorkflowActivitys.get(i))
                        .setGroovyScript(v);
            }
        }

        // update associated TWorkflowGuard
        if (collTWorkflowGuards != null)
        {
            for (int i = 0; i < collTWorkflowGuards.size(); i++)
            {
                ((TWorkflowGuard) collTWorkflowGuards.get(i))
                        .setGroovyScript(v);
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
     * Get the LastEdit
     *
     * @return Date
     */
    public Date getLastEdit()
    {
        return lastEdit;
    }


    /**
     * Set the value of LastEdit
     *
     * @param v new value
     */
    public void setLastEdit(Date v) 
    {

        if (!ObjectUtils.equals(this.lastEdit, v))
        {
            this.lastEdit = v;
            setModified(true);
        }


    }

    /**
     * Get the ScriptVersion
     *
     * @return Integer
     */
    public Integer getScriptVersion()
    {
        return scriptVersion;
    }


    /**
     * Set the value of ScriptVersion
     *
     * @param v new value
     */
    public void setScriptVersion(Integer v) 
    {

        if (!ObjectUtils.equals(this.scriptVersion, v))
        {
            this.scriptVersion = v;
            setModified(true);
        }


    }

    /**
     * Get the OriginalVersion
     *
     * @return Integer
     */
    public Integer getOriginalVersion()
    {
        return originalVersion;
    }


    /**
     * Set the value of OriginalVersion
     *
     * @param v new value
     */
    public void setOriginalVersion(Integer v) 
    {

        if (!ObjectUtils.equals(this.originalVersion, v))
        {
            this.originalVersion = v;
            setModified(true);
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
     * Get the ScriptType
     *
     * @return Integer
     */
    public Integer getScriptType()
    {
        return scriptType;
    }


    /**
     * Set the value of ScriptType
     *
     * @param v new value
     */
    public void setScriptType(Integer v) 
    {

        if (!ObjectUtils.equals(this.scriptType, v))
        {
            this.scriptType = v;
            setModified(true);
        }


    }

    /**
     * Get the ScriptRole
     *
     * @return Integer
     */
    public Integer getScriptRole()
    {
        return scriptRole;
    }


    /**
     * Set the value of ScriptRole
     *
     * @param v new value
     */
    public void setScriptRole(Integer v) 
    {

        if (!ObjectUtils.equals(this.scriptRole, v))
        {
            this.scriptRole = v;
            setModified(true);
        }


    }

    /**
     * Get the ClazzName
     *
     * @return String
     */
    public String getClazzName()
    {
        return clazzName;
    }


    /**
     * Set the value of ClazzName
     *
     * @param v new value
     */
    public void setClazzName(String v) 
    {

        if (!ObjectUtils.equals(this.clazzName, v))
        {
            this.clazzName = v;
            setModified(true);
        }


    }

    /**
     * Get the SourceCode
     *
     * @return String
     */
    public String getSourceCode()
    {
        return sourceCode;
    }


    /**
     * Set the value of SourceCode
     *
     * @param v new value
     */
    public void setSourceCode(String v) 
    {

        if (!ObjectUtils.equals(this.sourceCode, v))
        {
            this.sourceCode = v;
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
        l.setTScripts((TScripts) this);
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
        l.setTScripts((TScripts) this);
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
     * Otherwise if this TScripts has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     * If this TScripts is new, it will return
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
                criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID() );
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
                criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     * If this TScripts is new, it will return
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
                 criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
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
                 criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.GROOVYSCRIPT, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
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
        l.setTScripts((TScripts) this);
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
        l.setTScripts((TScripts) this);
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
     * Otherwise if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     * If this TScripts is new, it will return
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID() );
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     * If this TScripts is new, it will return
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
                 criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
                 criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
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
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
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
                criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.GROOVYSCRIPT, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }





    /**
     * Collection to store aggregation of collTWorkflowGuards
     */
    protected List<TWorkflowGuard> collTWorkflowGuards;

    /**
     * Temporary storage of collTWorkflowGuards to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowGuards()
    {
        if (collTWorkflowGuards == null)
        {
            collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
        }
    }


    /**
     * Method called to associate a TWorkflowGuard object to this object
     * through the TWorkflowGuard foreign key attribute
     *
     * @param l TWorkflowGuard
     * @throws TorqueException
     */
    public void addTWorkflowGuard(TWorkflowGuard l) throws TorqueException
    {
        getTWorkflowGuards().add(l);
        l.setTScripts((TScripts) this);
    }

    /**
     * Method called to associate a TWorkflowGuard object to this object
     * through the TWorkflowGuard foreign key attribute using connection.
     *
     * @param l TWorkflowGuard
     * @throws TorqueException
     */
    public void addTWorkflowGuard(TWorkflowGuard l, Connection con) throws TorqueException
    {
        getTWorkflowGuards(con).add(l);
        l.setTScripts((TScripts) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowGuards
     */
    private Criteria lastTWorkflowGuardsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowGuards(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowGuard> getTWorkflowGuards()
        throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            collTWorkflowGuards = getTWorkflowGuards(new Criteria(10));
        }
        return collTWorkflowGuards;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScripts has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     * If this TScripts is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowGuard> getTWorkflowGuards(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID() );
                collTWorkflowGuards = TWorkflowGuardPeer.doSelect(criteria);
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
                criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
                if (!lastTWorkflowGuardsCriteria.equals(criteria))
                {
                    collTWorkflowGuards = TWorkflowGuardPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowGuards(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowGuard> getTWorkflowGuards(Connection con) throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            collTWorkflowGuards = getTWorkflowGuards(new Criteria(10), con);
        }
        return collTWorkflowGuards;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScripts has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     * If this TScripts is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowGuard> getTWorkflowGuards(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                 criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
                 collTWorkflowGuards = TWorkflowGuardPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
                 if (!lastTWorkflowGuardsCriteria.equals(criteria))
                 {
                     collTWorkflowGuards = TWorkflowGuardPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowGuardsCriteria = criteria;

         return collTWorkflowGuards;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
     */
    protected List<TWorkflowGuard> getTWorkflowGuardsJoinTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
            if (!lastTWorkflowGuardsCriteria.equals(criteria))
            {
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
     */
    protected List<TWorkflowGuard> getTWorkflowGuardsJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
            if (!lastTWorkflowGuardsCriteria.equals(criteria))
            {
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTRole(criteria);
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
     */
    protected List<TWorkflowGuard> getTWorkflowGuardsJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
            if (!lastTWorkflowGuardsCriteria.equals(criteria))
            {
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScripts is new, it will return
     * an empty collection; or if this TScripts has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScripts.
     */
    protected List<TWorkflowGuard> getTWorkflowGuardsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.GROOVYSCRIPT, getObjectID());
            if (!lastTWorkflowGuardsCriteria.equals(criteria))
            {
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
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
            fieldNames.add("LastEdit");
            fieldNames.add("ScriptVersion");
            fieldNames.add("OriginalVersion");
            fieldNames.add("ProjectType");
            fieldNames.add("Project");
            fieldNames.add("ScriptType");
            fieldNames.add("ScriptRole");
            fieldNames.add("ClazzName");
            fieldNames.add("SourceCode");
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
        if (name.equals("LastEdit"))
        {
            return getLastEdit();
        }
        if (name.equals("ScriptVersion"))
        {
            return getScriptVersion();
        }
        if (name.equals("OriginalVersion"))
        {
            return getOriginalVersion();
        }
        if (name.equals("ProjectType"))
        {
            return getProjectType();
        }
        if (name.equals("Project"))
        {
            return getProject();
        }
        if (name.equals("ScriptType"))
        {
            return getScriptType();
        }
        if (name.equals("ScriptRole"))
        {
            return getScriptRole();
        }
        if (name.equals("ClazzName"))
        {
            return getClazzName();
        }
        if (name.equals("SourceCode"))
        {
            return getSourceCode();
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
        if (name.equals("LastEdit"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLastEdit((Date) value);
            return true;
        }
        if (name.equals("ScriptVersion"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setScriptVersion((Integer) value);
            return true;
        }
        if (name.equals("OriginalVersion"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOriginalVersion((Integer) value);
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
        if (name.equals("ScriptType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setScriptType((Integer) value);
            return true;
        }
        if (name.equals("ScriptRole"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setScriptRole((Integer) value);
            return true;
        }
        if (name.equals("ClazzName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setClazzName((String) value);
            return true;
        }
        if (name.equals("SourceCode"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSourceCode((String) value);
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
        if (name.equals(TScriptsPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TScriptsPeer.CHANGEDBY))
        {
            return getPerson();
        }
        if (name.equals(TScriptsPeer.LASTEDIT))
        {
            return getLastEdit();
        }
        if (name.equals(TScriptsPeer.SCRIPTVERSION))
        {
            return getScriptVersion();
        }
        if (name.equals(TScriptsPeer.ORIGINALVERSION))
        {
            return getOriginalVersion();
        }
        if (name.equals(TScriptsPeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TScriptsPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TScriptsPeer.SCRIPTTYPE))
        {
            return getScriptType();
        }
        if (name.equals(TScriptsPeer.SCRIPTROLE))
        {
            return getScriptRole();
        }
        if (name.equals(TScriptsPeer.CLAZZNAME))
        {
            return getClazzName();
        }
        if (name.equals(TScriptsPeer.SOURCECODE))
        {
            return getSourceCode();
        }
        if (name.equals(TScriptsPeer.TPUUID))
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
      if (TScriptsPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TScriptsPeer.CHANGEDBY.equals(name))
        {
            return setByName("Person", value);
        }
      if (TScriptsPeer.LASTEDIT.equals(name))
        {
            return setByName("LastEdit", value);
        }
      if (TScriptsPeer.SCRIPTVERSION.equals(name))
        {
            return setByName("ScriptVersion", value);
        }
      if (TScriptsPeer.ORIGINALVERSION.equals(name))
        {
            return setByName("OriginalVersion", value);
        }
      if (TScriptsPeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TScriptsPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TScriptsPeer.SCRIPTTYPE.equals(name))
        {
            return setByName("ScriptType", value);
        }
      if (TScriptsPeer.SCRIPTROLE.equals(name))
        {
            return setByName("ScriptRole", value);
        }
      if (TScriptsPeer.CLAZZNAME.equals(name))
        {
            return setByName("ClazzName", value);
        }
      if (TScriptsPeer.SOURCECODE.equals(name))
        {
            return setByName("SourceCode", value);
        }
      if (TScriptsPeer.TPUUID.equals(name))
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
            return getLastEdit();
        }
        if (pos == 3)
        {
            return getScriptVersion();
        }
        if (pos == 4)
        {
            return getOriginalVersion();
        }
        if (pos == 5)
        {
            return getProjectType();
        }
        if (pos == 6)
        {
            return getProject();
        }
        if (pos == 7)
        {
            return getScriptType();
        }
        if (pos == 8)
        {
            return getScriptRole();
        }
        if (pos == 9)
        {
            return getClazzName();
        }
        if (pos == 10)
        {
            return getSourceCode();
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
            return setByName("LastEdit", value);
        }
    if (position == 3)
        {
            return setByName("ScriptVersion", value);
        }
    if (position == 4)
        {
            return setByName("OriginalVersion", value);
        }
    if (position == 5)
        {
            return setByName("ProjectType", value);
        }
    if (position == 6)
        {
            return setByName("Project", value);
        }
    if (position == 7)
        {
            return setByName("ScriptType", value);
        }
    if (position == 8)
        {
            return setByName("ScriptRole", value);
        }
    if (position == 9)
        {
            return setByName("ClazzName", value);
        }
    if (position == 10)
        {
            return setByName("SourceCode", value);
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
        save(TScriptsPeer.DATABASE_NAME);
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
                    TScriptsPeer.doInsert((TScripts) this, con);
                    setNew(false);
                }
                else
                {
                    TScriptsPeer.doUpdate((TScripts) this, con);
                }
            }


            if (collTFieldConfigs != null)
            {
                for (int i = 0; i < collTFieldConfigs.size(); i++)
                {
                    ((TFieldConfig) collTFieldConfigs.get(i)).save(con);
                }
            }

            if (collTWorkflowActivitys != null)
            {
                for (int i = 0; i < collTWorkflowActivitys.size(); i++)
                {
                    ((TWorkflowActivity) collTWorkflowActivitys.get(i)).save(con);
                }
            }

            if (collTWorkflowGuards != null)
            {
                for (int i = 0; i < collTWorkflowGuards.size(); i++)
                {
                    ((TWorkflowGuard) collTWorkflowGuards.get(i)).save(con);
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
    public TScripts copy() throws TorqueException
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
    public TScripts copy(Connection con) throws TorqueException
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
    public TScripts copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TScripts(), deepcopy);
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
    public TScripts copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TScripts(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TScripts copyInto(TScripts copyObj) throws TorqueException
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
    protected TScripts copyInto(TScripts copyObj, Connection con) throws TorqueException
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
    protected TScripts copyInto(TScripts copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setLastEdit(lastEdit);
        copyObj.setScriptVersion(scriptVersion);
        copyObj.setOriginalVersion(originalVersion);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setScriptType(scriptType);
        copyObj.setScriptRole(scriptRole);
        copyObj.setClazzName(clazzName);
        copyObj.setSourceCode(sourceCode);
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


        List<TWorkflowGuard> vTWorkflowGuards = getTWorkflowGuards();
        if (vTWorkflowGuards != null)
        {
            for (int i = 0; i < vTWorkflowGuards.size(); i++)
            {
                TWorkflowGuard obj =  vTWorkflowGuards.get(i);
                copyObj.addTWorkflowGuard(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowGuards = null;
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
    protected TScripts copyInto(TScripts copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setLastEdit(lastEdit);
        copyObj.setScriptVersion(scriptVersion);
        copyObj.setOriginalVersion(originalVersion);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setScriptType(scriptType);
        copyObj.setScriptRole(scriptRole);
        copyObj.setClazzName(clazzName);
        copyObj.setSourceCode(sourceCode);
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


        List<TWorkflowGuard> vTWorkflowGuards = getTWorkflowGuards(con);
        if (vTWorkflowGuards != null)
        {
            for (int i = 0; i < vTWorkflowGuards.size(); i++)
            {
                TWorkflowGuard obj =  vTWorkflowGuards.get(i);
                copyObj.addTWorkflowGuard(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowGuards = null;
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
    public TScriptsPeer getPeer()
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
        return TScriptsPeer.getTableMap();
    }

  
    /**
     * Creates a TScriptsBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TScriptsBean with the contents of this object
     */
    public TScriptsBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TScriptsBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TScriptsBean with the contents of this object
     */
    public TScriptsBean getBean(IdentityMap createdBeans)
    {
        TScriptsBean result = (TScriptsBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TScriptsBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setPerson(getPerson());
        result.setLastEdit(getLastEdit());
        result.setScriptVersion(getScriptVersion());
        result.setOriginalVersion(getOriginalVersion());
        result.setProjectType(getProjectType());
        result.setProject(getProject());
        result.setScriptType(getScriptType());
        result.setScriptRole(getScriptRole());
        result.setClazzName(getClazzName());
        result.setSourceCode(getSourceCode());
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


        if (collTWorkflowGuards != null)
        {
            List<TWorkflowGuardBean> relatedBeans = new ArrayList<TWorkflowGuardBean>(collTWorkflowGuards.size());
            for (Iterator<TWorkflowGuard> collTWorkflowGuardsIt = collTWorkflowGuards.iterator(); collTWorkflowGuardsIt.hasNext(); )
            {
                TWorkflowGuard related = (TWorkflowGuard) collTWorkflowGuardsIt.next();
                TWorkflowGuardBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowGuardBeans(relatedBeans);
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
     * Creates an instance of TScripts with the contents
     * of a TScriptsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TScriptsBean which contents are used to create
     *        the resulting class
     * @return an instance of TScripts with the contents of bean
     */
    public static TScripts createTScripts(TScriptsBean bean)
        throws TorqueException
    {
        return createTScripts(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TScripts with the contents
     * of a TScriptsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TScriptsBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TScripts with the contents of bean
     */

    public static TScripts createTScripts(TScriptsBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TScripts result = (TScripts) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TScripts();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setPerson(bean.getPerson());
        result.setLastEdit(bean.getLastEdit());
        result.setScriptVersion(bean.getScriptVersion());
        result.setOriginalVersion(bean.getOriginalVersion());
        result.setProjectType(bean.getProjectType());
        result.setProject(bean.getProject());
        result.setScriptType(bean.getScriptType());
        result.setScriptRole(bean.getScriptRole());
        result.setClazzName(bean.getClazzName());
        result.setSourceCode(bean.getSourceCode());
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
            List<TWorkflowGuardBean> relatedBeans = bean.getTWorkflowGuardBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowGuardBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowGuardBean relatedBean =  relatedBeansIt.next();
                    TWorkflowGuard related = TWorkflowGuard.createTWorkflowGuard(relatedBean, createdObjects);
                    result.addTWorkflowGuardFromBean(related);
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


    /**
     * Method called to associate a TWorkflowGuard object to this object.
     * through the TWorkflowGuard foreign key attribute
     *
     * @param toAdd TWorkflowGuard
     */
    protected void addTWorkflowGuardFromBean(TWorkflowGuard toAdd)
    {
        initTWorkflowGuards();
        collTWorkflowGuards.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TScripts:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("LastEdit = ")
           .append(getLastEdit())
           .append("\n");
        str.append("ScriptVersion = ")
           .append(getScriptVersion())
           .append("\n");
        str.append("OriginalVersion = ")
           .append(getOriginalVersion())
           .append("\n");
        str.append("ProjectType = ")
           .append(getProjectType())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("ScriptType = ")
           .append(getScriptType())
           .append("\n");
        str.append("ScriptRole = ")
           .append(getScriptRole())
           .append("\n");
        str.append("ClazzName = ")
           .append(getClazzName())
           .append("\n");
        str.append("SourceCode = ")
           .append(getSourceCode())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
