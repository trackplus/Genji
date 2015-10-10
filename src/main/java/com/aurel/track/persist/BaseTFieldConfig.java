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



import com.aurel.track.persist.TField;
import com.aurel.track.persist.TFieldPeer;
import com.aurel.track.persist.TListType;
import com.aurel.track.persist.TListTypePeer;
import com.aurel.track.persist.TProjectType;
import com.aurel.track.persist.TProjectTypePeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TScripts;
import com.aurel.track.persist.TScriptsPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TScriptsBean;

import com.aurel.track.beans.TConfigOptionsRoleBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TOptionSettingsBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TFieldConfig
 */
public abstract class BaseTFieldConfig extends TpBaseObject
{
    /** The Peer class */
    private static final TFieldConfigPeer peer =
        new TFieldConfigPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the field field */
    private Integer field;

    /** The value for the label field */
    private String label;

    /** The value for the tooltip field */
    private String tooltip;

    /** The value for the required field */
    private String required = "N";

    /** The value for the history field */
    private String history = "N";

    /** The value for the issueType field */
    private Integer issueType;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

    /** The value for the description field */
    private String description;

    /** The value for the groovyScript field */
    private Integer groovyScript;

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



        // update associated TConfigOptionsRole
        if (collTConfigOptionsRoles != null)
        {
            for (int i = 0; i < collTConfigOptionsRoles.size(); i++)
            {
                ((TConfigOptionsRole) collTConfigOptionsRoles.get(i))
                        .setConfigKey(v);
            }
        }

        // update associated TTextBoxSettings
        if (collTTextBoxSettingss != null)
        {
            for (int i = 0; i < collTTextBoxSettingss.size(); i++)
            {
                ((TTextBoxSettings) collTTextBoxSettingss.get(i))
                        .setConfig(v);
            }
        }

        // update associated TGeneralSettings
        if (collTGeneralSettingss != null)
        {
            for (int i = 0; i < collTGeneralSettingss.size(); i++)
            {
                ((TGeneralSettings) collTGeneralSettingss.get(i))
                        .setConfig(v);
            }
        }

        // update associated TOptionSettings
        if (collTOptionSettingss != null)
        {
            for (int i = 0; i < collTOptionSettingss.size(); i++)
            {
                ((TOptionSettings) collTOptionSettingss.get(i))
                        .setConfig(v);
            }
        }
    }

    /**
     * Get the Field
     *
     * @return Integer
     */
    public Integer getField()
    {
        return field;
    }


    /**
     * Set the value of Field
     *
     * @param v new value
     */
    public void setField(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.field, v))
        {
            this.field = v;
            setModified(true);
        }


        if (aTField != null && !ObjectUtils.equals(aTField.getObjectID(), v))
        {
            aTField = null;
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
     * Get the Tooltip
     *
     * @return String
     */
    public String getTooltip()
    {
        return tooltip;
    }


    /**
     * Set the value of Tooltip
     *
     * @param v new value
     */
    public void setTooltip(String v) 
    {

        if (!ObjectUtils.equals(this.tooltip, v))
        {
            this.tooltip = v;
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
     * Get the History
     *
     * @return String
     */
    public String getHistory()
    {
        return history;
    }


    /**
     * Set the value of History
     *
     * @param v new value
     */
    public void setHistory(String v) 
    {

        if (!ObjectUtils.equals(this.history, v))
        {
            this.history = v;
            setModified(true);
        }


    }

    /**
     * Get the IssueType
     *
     * @return Integer
     */
    public Integer getIssueType()
    {
        return issueType;
    }


    /**
     * Set the value of IssueType
     *
     * @param v new value
     */
    public void setIssueType(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.issueType, v))
        {
            this.issueType = v;
            setModified(true);
        }


        if (aTListType != null && !ObjectUtils.equals(aTListType.getObjectID(), v))
        {
            aTListType = null;
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
     * Get the GroovyScript
     *
     * @return Integer
     */
    public Integer getGroovyScript()
    {
        return groovyScript;
    }


    /**
     * Set the value of GroovyScript
     *
     * @param v new value
     */
    public void setGroovyScript(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.groovyScript, v))
        {
            this.groovyScript = v;
            setModified(true);
        }


        if (aTScripts != null && !ObjectUtils.equals(aTScripts.getObjectID(), v))
        {
            aTScripts = null;
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

    



    private TField aTField;

    /**
     * Declares an association between this object and a TField object
     *
     * @param v TField
     * @throws TorqueException
     */
    public void setTField(TField v) throws TorqueException
    {
        if (v == null)
        {
            setField((Integer) null);
        }
        else
        {
            setField(v.getObjectID());
        }
        aTField = v;
    }


    /**
     * Returns the associated TField object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TField object
     * @throws TorqueException
     */
    public TField getTField()
        throws TorqueException
    {
        if (aTField == null && (!ObjectUtils.equals(this.field, null)))
        {
            aTField = TFieldPeer.retrieveByPK(SimpleKey.keyFor(this.field));
        }
        return aTField;
    }

    /**
     * Return the associated TField object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TField object
     * @throws TorqueException
     */
    public TField getTField(Connection connection)
        throws TorqueException
    {
        if (aTField == null && (!ObjectUtils.equals(this.field, null)))
        {
            aTField = TFieldPeer.retrieveByPK(SimpleKey.keyFor(this.field), connection);
        }
        return aTField;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTFieldKey(ObjectKey key) throws TorqueException
    {

        setField(new Integer(((NumberKey) key).intValue()));
    }




    private TListType aTListType;

    /**
     * Declares an association between this object and a TListType object
     *
     * @param v TListType
     * @throws TorqueException
     */
    public void setTListType(TListType v) throws TorqueException
    {
        if (v == null)
        {
            setIssueType((Integer) null);
        }
        else
        {
            setIssueType(v.getObjectID());
        }
        aTListType = v;
    }


    /**
     * Returns the associated TListType object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TListType object
     * @throws TorqueException
     */
    public TListType getTListType()
        throws TorqueException
    {
        if (aTListType == null && (!ObjectUtils.equals(this.issueType, null)))
        {
            aTListType = TListTypePeer.retrieveByPK(SimpleKey.keyFor(this.issueType));
        }
        return aTListType;
    }

    /**
     * Return the associated TListType object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TListType object
     * @throws TorqueException
     */
    public TListType getTListType(Connection connection)
        throws TorqueException
    {
        if (aTListType == null && (!ObjectUtils.equals(this.issueType, null)))
        {
            aTListType = TListTypePeer.retrieveByPK(SimpleKey.keyFor(this.issueType), connection);
        }
        return aTListType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTListTypeKey(ObjectKey key) throws TorqueException
    {

        setIssueType(new Integer(((NumberKey) key).intValue()));
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




    private TScripts aTScripts;

    /**
     * Declares an association between this object and a TScripts object
     *
     * @param v TScripts
     * @throws TorqueException
     */
    public void setTScripts(TScripts v) throws TorqueException
    {
        if (v == null)
        {
            setGroovyScript((Integer) null);
        }
        else
        {
            setGroovyScript(v.getObjectID());
        }
        aTScripts = v;
    }


    /**
     * Returns the associated TScripts object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TScripts object
     * @throws TorqueException
     */
    public TScripts getTScripts()
        throws TorqueException
    {
        if (aTScripts == null && (!ObjectUtils.equals(this.groovyScript, null)))
        {
            aTScripts = TScriptsPeer.retrieveByPK(SimpleKey.keyFor(this.groovyScript));
        }
        return aTScripts;
    }

    /**
     * Return the associated TScripts object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TScripts object
     * @throws TorqueException
     */
    public TScripts getTScripts(Connection connection)
        throws TorqueException
    {
        if (aTScripts == null && (!ObjectUtils.equals(this.groovyScript, null)))
        {
            aTScripts = TScriptsPeer.retrieveByPK(SimpleKey.keyFor(this.groovyScript), connection);
        }
        return aTScripts;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTScriptsKey(ObjectKey key) throws TorqueException
    {

        setGroovyScript(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTConfigOptionsRoles
     */
    protected List<TConfigOptionsRole> collTConfigOptionsRoles;

    /**
     * Temporary storage of collTConfigOptionsRoles to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTConfigOptionsRoles()
    {
        if (collTConfigOptionsRoles == null)
        {
            collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
        }
    }


    /**
     * Method called to associate a TConfigOptionsRole object to this object
     * through the TConfigOptionsRole foreign key attribute
     *
     * @param l TConfigOptionsRole
     * @throws TorqueException
     */
    public void addTConfigOptionsRole(TConfigOptionsRole l) throws TorqueException
    {
        getTConfigOptionsRoles().add(l);
        l.setTFieldConfig((TFieldConfig) this);
    }

    /**
     * Method called to associate a TConfigOptionsRole object to this object
     * through the TConfigOptionsRole foreign key attribute using connection.
     *
     * @param l TConfigOptionsRole
     * @throws TorqueException
     */
    public void addTConfigOptionsRole(TConfigOptionsRole l, Connection con) throws TorqueException
    {
        getTConfigOptionsRoles(con).add(l);
        l.setTFieldConfig((TFieldConfig) this);
    }

    /**
     * The criteria used to select the current contents of collTConfigOptionsRoles
     */
    private Criteria lastTConfigOptionsRolesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTConfigOptionsRoles(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TConfigOptionsRole> getTConfigOptionsRoles()
        throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            collTConfigOptionsRoles = getTConfigOptionsRoles(new Criteria(10));
        }
        return collTConfigOptionsRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     * If this TFieldConfig is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TConfigOptionsRole> getTConfigOptionsRoles(Criteria criteria) throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            if (isNew())
            {
               collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
            }
            else
            {
                criteria.add(TConfigOptionsRolePeer.CONFIGKEY, getObjectID() );
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelect(criteria);
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
                criteria.add(TConfigOptionsRolePeer.CONFIGKEY, getObjectID());
                if (!lastTConfigOptionsRolesCriteria.equals(criteria))
                {
                    collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelect(criteria);
                }
            }
        }
        lastTConfigOptionsRolesCriteria = criteria;

        return collTConfigOptionsRoles;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTConfigOptionsRoles(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TConfigOptionsRole> getTConfigOptionsRoles(Connection con) throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            collTConfigOptionsRoles = getTConfigOptionsRoles(new Criteria(10), con);
        }
        return collTConfigOptionsRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     * If this TFieldConfig is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TConfigOptionsRole> getTConfigOptionsRoles(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            if (isNew())
            {
               collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
            }
            else
            {
                 criteria.add(TConfigOptionsRolePeer.CONFIGKEY, getObjectID());
                 collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelect(criteria, con);
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
                 criteria.add(TConfigOptionsRolePeer.CONFIGKEY, getObjectID());
                 if (!lastTConfigOptionsRolesCriteria.equals(criteria))
                 {
                     collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTConfigOptionsRolesCriteria = criteria;

         return collTConfigOptionsRoles;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig is new, it will return
     * an empty collection; or if this TFieldConfig has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldConfig.
     */
    protected List<TConfigOptionsRole> getTConfigOptionsRolesJoinTFieldConfig(Criteria criteria)
        throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            if (isNew())
            {
               collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
            }
            else
            {
                criteria.add(TConfigOptionsRolePeer.CONFIGKEY, getObjectID());
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TConfigOptionsRolePeer.CONFIGKEY, getObjectID());
            if (!lastTConfigOptionsRolesCriteria.equals(criteria))
            {
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        lastTConfigOptionsRolesCriteria = criteria;

        return collTConfigOptionsRoles;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig is new, it will return
     * an empty collection; or if this TFieldConfig has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldConfig.
     */
    protected List<TConfigOptionsRole> getTConfigOptionsRolesJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            if (isNew())
            {
               collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
            }
            else
            {
                criteria.add(TConfigOptionsRolePeer.CONFIGKEY, getObjectID());
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TConfigOptionsRolePeer.CONFIGKEY, getObjectID());
            if (!lastTConfigOptionsRolesCriteria.equals(criteria))
            {
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTRole(criteria);
            }
        }
        lastTConfigOptionsRolesCriteria = criteria;

        return collTConfigOptionsRoles;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig is new, it will return
     * an empty collection; or if this TFieldConfig has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldConfig.
     */
    protected List<TConfigOptionsRole> getTConfigOptionsRolesJoinTOption(Criteria criteria)
        throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            if (isNew())
            {
               collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
            }
            else
            {
                criteria.add(TConfigOptionsRolePeer.CONFIGKEY, getObjectID());
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTOption(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TConfigOptionsRolePeer.CONFIGKEY, getObjectID());
            if (!lastTConfigOptionsRolesCriteria.equals(criteria))
            {
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTOption(criteria);
            }
        }
        lastTConfigOptionsRolesCriteria = criteria;

        return collTConfigOptionsRoles;
    }





    /**
     * Collection to store aggregation of collTTextBoxSettingss
     */
    protected List<TTextBoxSettings> collTTextBoxSettingss;

    /**
     * Temporary storage of collTTextBoxSettingss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTTextBoxSettingss()
    {
        if (collTTextBoxSettingss == null)
        {
            collTTextBoxSettingss = new ArrayList<TTextBoxSettings>();
        }
    }


    /**
     * Method called to associate a TTextBoxSettings object to this object
     * through the TTextBoxSettings foreign key attribute
     *
     * @param l TTextBoxSettings
     * @throws TorqueException
     */
    public void addTTextBoxSettings(TTextBoxSettings l) throws TorqueException
    {
        getTTextBoxSettingss().add(l);
        l.setTFieldConfig((TFieldConfig) this);
    }

    /**
     * Method called to associate a TTextBoxSettings object to this object
     * through the TTextBoxSettings foreign key attribute using connection.
     *
     * @param l TTextBoxSettings
     * @throws TorqueException
     */
    public void addTTextBoxSettings(TTextBoxSettings l, Connection con) throws TorqueException
    {
        getTTextBoxSettingss(con).add(l);
        l.setTFieldConfig((TFieldConfig) this);
    }

    /**
     * The criteria used to select the current contents of collTTextBoxSettingss
     */
    private Criteria lastTTextBoxSettingssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTTextBoxSettingss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TTextBoxSettings> getTTextBoxSettingss()
        throws TorqueException
    {
        if (collTTextBoxSettingss == null)
        {
            collTTextBoxSettingss = getTTextBoxSettingss(new Criteria(10));
        }
        return collTTextBoxSettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig has previously
     * been saved, it will retrieve related TTextBoxSettingss from storage.
     * If this TFieldConfig is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TTextBoxSettings> getTTextBoxSettingss(Criteria criteria) throws TorqueException
    {
        if (collTTextBoxSettingss == null)
        {
            if (isNew())
            {
               collTTextBoxSettingss = new ArrayList<TTextBoxSettings>();
            }
            else
            {
                criteria.add(TTextBoxSettingsPeer.CONFIG, getObjectID() );
                collTTextBoxSettingss = TTextBoxSettingsPeer.doSelect(criteria);
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
                criteria.add(TTextBoxSettingsPeer.CONFIG, getObjectID());
                if (!lastTTextBoxSettingssCriteria.equals(criteria))
                {
                    collTTextBoxSettingss = TTextBoxSettingsPeer.doSelect(criteria);
                }
            }
        }
        lastTTextBoxSettingssCriteria = criteria;

        return collTTextBoxSettingss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTTextBoxSettingss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TTextBoxSettings> getTTextBoxSettingss(Connection con) throws TorqueException
    {
        if (collTTextBoxSettingss == null)
        {
            collTTextBoxSettingss = getTTextBoxSettingss(new Criteria(10), con);
        }
        return collTTextBoxSettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig has previously
     * been saved, it will retrieve related TTextBoxSettingss from storage.
     * If this TFieldConfig is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TTextBoxSettings> getTTextBoxSettingss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTTextBoxSettingss == null)
        {
            if (isNew())
            {
               collTTextBoxSettingss = new ArrayList<TTextBoxSettings>();
            }
            else
            {
                 criteria.add(TTextBoxSettingsPeer.CONFIG, getObjectID());
                 collTTextBoxSettingss = TTextBoxSettingsPeer.doSelect(criteria, con);
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
                 criteria.add(TTextBoxSettingsPeer.CONFIG, getObjectID());
                 if (!lastTTextBoxSettingssCriteria.equals(criteria))
                 {
                     collTTextBoxSettingss = TTextBoxSettingsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTTextBoxSettingssCriteria = criteria;

         return collTTextBoxSettingss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig is new, it will return
     * an empty collection; or if this TFieldConfig has previously
     * been saved, it will retrieve related TTextBoxSettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldConfig.
     */
    protected List<TTextBoxSettings> getTTextBoxSettingssJoinTFieldConfig(Criteria criteria)
        throws TorqueException
    {
        if (collTTextBoxSettingss == null)
        {
            if (isNew())
            {
               collTTextBoxSettingss = new ArrayList<TTextBoxSettings>();
            }
            else
            {
                criteria.add(TTextBoxSettingsPeer.CONFIG, getObjectID());
                collTTextBoxSettingss = TTextBoxSettingsPeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TTextBoxSettingsPeer.CONFIG, getObjectID());
            if (!lastTTextBoxSettingssCriteria.equals(criteria))
            {
                collTTextBoxSettingss = TTextBoxSettingsPeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        lastTTextBoxSettingssCriteria = criteria;

        return collTTextBoxSettingss;
    }





    /**
     * Collection to store aggregation of collTGeneralSettingss
     */
    protected List<TGeneralSettings> collTGeneralSettingss;

    /**
     * Temporary storage of collTGeneralSettingss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTGeneralSettingss()
    {
        if (collTGeneralSettingss == null)
        {
            collTGeneralSettingss = new ArrayList<TGeneralSettings>();
        }
    }


    /**
     * Method called to associate a TGeneralSettings object to this object
     * through the TGeneralSettings foreign key attribute
     *
     * @param l TGeneralSettings
     * @throws TorqueException
     */
    public void addTGeneralSettings(TGeneralSettings l) throws TorqueException
    {
        getTGeneralSettingss().add(l);
        l.setTFieldConfig((TFieldConfig) this);
    }

    /**
     * Method called to associate a TGeneralSettings object to this object
     * through the TGeneralSettings foreign key attribute using connection.
     *
     * @param l TGeneralSettings
     * @throws TorqueException
     */
    public void addTGeneralSettings(TGeneralSettings l, Connection con) throws TorqueException
    {
        getTGeneralSettingss(con).add(l);
        l.setTFieldConfig((TFieldConfig) this);
    }

    /**
     * The criteria used to select the current contents of collTGeneralSettingss
     */
    private Criteria lastTGeneralSettingssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTGeneralSettingss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TGeneralSettings> getTGeneralSettingss()
        throws TorqueException
    {
        if (collTGeneralSettingss == null)
        {
            collTGeneralSettingss = getTGeneralSettingss(new Criteria(10));
        }
        return collTGeneralSettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig has previously
     * been saved, it will retrieve related TGeneralSettingss from storage.
     * If this TFieldConfig is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TGeneralSettings> getTGeneralSettingss(Criteria criteria) throws TorqueException
    {
        if (collTGeneralSettingss == null)
        {
            if (isNew())
            {
               collTGeneralSettingss = new ArrayList<TGeneralSettings>();
            }
            else
            {
                criteria.add(TGeneralSettingsPeer.CONFIG, getObjectID() );
                collTGeneralSettingss = TGeneralSettingsPeer.doSelect(criteria);
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
                criteria.add(TGeneralSettingsPeer.CONFIG, getObjectID());
                if (!lastTGeneralSettingssCriteria.equals(criteria))
                {
                    collTGeneralSettingss = TGeneralSettingsPeer.doSelect(criteria);
                }
            }
        }
        lastTGeneralSettingssCriteria = criteria;

        return collTGeneralSettingss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTGeneralSettingss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TGeneralSettings> getTGeneralSettingss(Connection con) throws TorqueException
    {
        if (collTGeneralSettingss == null)
        {
            collTGeneralSettingss = getTGeneralSettingss(new Criteria(10), con);
        }
        return collTGeneralSettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig has previously
     * been saved, it will retrieve related TGeneralSettingss from storage.
     * If this TFieldConfig is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TGeneralSettings> getTGeneralSettingss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTGeneralSettingss == null)
        {
            if (isNew())
            {
               collTGeneralSettingss = new ArrayList<TGeneralSettings>();
            }
            else
            {
                 criteria.add(TGeneralSettingsPeer.CONFIG, getObjectID());
                 collTGeneralSettingss = TGeneralSettingsPeer.doSelect(criteria, con);
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
                 criteria.add(TGeneralSettingsPeer.CONFIG, getObjectID());
                 if (!lastTGeneralSettingssCriteria.equals(criteria))
                 {
                     collTGeneralSettingss = TGeneralSettingsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTGeneralSettingssCriteria = criteria;

         return collTGeneralSettingss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TFieldConfig is new, it will return
     * an empty collection; or if this TFieldConfig has previously
     * been saved, it will retrieve related TGeneralSettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldConfig.
     */
    protected List<TGeneralSettings> getTGeneralSettingssJoinTFieldConfig(Criteria criteria)
        throws TorqueException
    {
        if (collTGeneralSettingss == null)
        {
            if (isNew())
            {
               collTGeneralSettingss = new ArrayList<TGeneralSettings>();
            }
            else
            {
                criteria.add(TGeneralSettingsPeer.CONFIG, getObjectID());
                collTGeneralSettingss = TGeneralSettingsPeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TGeneralSettingsPeer.CONFIG, getObjectID());
            if (!lastTGeneralSettingssCriteria.equals(criteria))
            {
                collTGeneralSettingss = TGeneralSettingsPeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        lastTGeneralSettingssCriteria = criteria;

        return collTGeneralSettingss;
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
        l.setTFieldConfig((TFieldConfig) this);
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
        l.setTFieldConfig((TFieldConfig) this);
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
     * Otherwise if this TFieldConfig has previously
     * been saved, it will retrieve related TOptionSettingss from storage.
     * If this TFieldConfig is new, it will return
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
                criteria.add(TOptionSettingsPeer.CONFIG, getObjectID() );
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
                criteria.add(TOptionSettingsPeer.CONFIG, getObjectID());
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
     * Otherwise if this TFieldConfig has previously
     * been saved, it will retrieve related TOptionSettingss from storage.
     * If this TFieldConfig is new, it will return
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
                 criteria.add(TOptionSettingsPeer.CONFIG, getObjectID());
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
                 criteria.add(TOptionSettingsPeer.CONFIG, getObjectID());
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
     * Otherwise if this TFieldConfig is new, it will return
     * an empty collection; or if this TFieldConfig has previously
     * been saved, it will retrieve related TOptionSettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldConfig.
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
                criteria.add(TOptionSettingsPeer.CONFIG, getObjectID());
                collTOptionSettingss = TOptionSettingsPeer.doSelectJoinTList(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOptionSettingsPeer.CONFIG, getObjectID());
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
     * Otherwise if this TFieldConfig is new, it will return
     * an empty collection; or if this TFieldConfig has previously
     * been saved, it will retrieve related TOptionSettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldConfig.
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
                criteria.add(TOptionSettingsPeer.CONFIG, getObjectID());
                collTOptionSettingss = TOptionSettingsPeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOptionSettingsPeer.CONFIG, getObjectID());
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
            fieldNames.add("Field");
            fieldNames.add("Label");
            fieldNames.add("Tooltip");
            fieldNames.add("Required");
            fieldNames.add("History");
            fieldNames.add("IssueType");
            fieldNames.add("ProjectType");
            fieldNames.add("Project");
            fieldNames.add("Description");
            fieldNames.add("GroovyScript");
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
        if (name.equals("Field"))
        {
            return getField();
        }
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("Tooltip"))
        {
            return getTooltip();
        }
        if (name.equals("Required"))
        {
            return getRequired();
        }
        if (name.equals("History"))
        {
            return getHistory();
        }
        if (name.equals("IssueType"))
        {
            return getIssueType();
        }
        if (name.equals("ProjectType"))
        {
            return getProjectType();
        }
        if (name.equals("Project"))
        {
            return getProject();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("GroovyScript"))
        {
            return getGroovyScript();
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
        if (name.equals("Field"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setField((Integer) value);
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
        if (name.equals("Tooltip"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTooltip((String) value);
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
        if (name.equals("History"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setHistory((String) value);
            return true;
        }
        if (name.equals("IssueType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIssueType((Integer) value);
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
        if (name.equals("GroovyScript"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setGroovyScript((Integer) value);
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
        if (name.equals(TFieldConfigPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TFieldConfigPeer.FIELDKEY))
        {
            return getField();
        }
        if (name.equals(TFieldConfigPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TFieldConfigPeer.TOOLTIP))
        {
            return getTooltip();
        }
        if (name.equals(TFieldConfigPeer.REQUIRED))
        {
            return getRequired();
        }
        if (name.equals(TFieldConfigPeer.HISTORY))
        {
            return getHistory();
        }
        if (name.equals(TFieldConfigPeer.ISSUETYPE))
        {
            return getIssueType();
        }
        if (name.equals(TFieldConfigPeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TFieldConfigPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TFieldConfigPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TFieldConfigPeer.GROOVYSCRIPT))
        {
            return getGroovyScript();
        }
        if (name.equals(TFieldConfigPeer.TPUUID))
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
      if (TFieldConfigPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TFieldConfigPeer.FIELDKEY.equals(name))
        {
            return setByName("Field", value);
        }
      if (TFieldConfigPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TFieldConfigPeer.TOOLTIP.equals(name))
        {
            return setByName("Tooltip", value);
        }
      if (TFieldConfigPeer.REQUIRED.equals(name))
        {
            return setByName("Required", value);
        }
      if (TFieldConfigPeer.HISTORY.equals(name))
        {
            return setByName("History", value);
        }
      if (TFieldConfigPeer.ISSUETYPE.equals(name))
        {
            return setByName("IssueType", value);
        }
      if (TFieldConfigPeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TFieldConfigPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TFieldConfigPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TFieldConfigPeer.GROOVYSCRIPT.equals(name))
        {
            return setByName("GroovyScript", value);
        }
      if (TFieldConfigPeer.TPUUID.equals(name))
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
            return getField();
        }
        if (pos == 2)
        {
            return getLabel();
        }
        if (pos == 3)
        {
            return getTooltip();
        }
        if (pos == 4)
        {
            return getRequired();
        }
        if (pos == 5)
        {
            return getHistory();
        }
        if (pos == 6)
        {
            return getIssueType();
        }
        if (pos == 7)
        {
            return getProjectType();
        }
        if (pos == 8)
        {
            return getProject();
        }
        if (pos == 9)
        {
            return getDescription();
        }
        if (pos == 10)
        {
            return getGroovyScript();
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
            return setByName("Field", value);
        }
    if (position == 2)
        {
            return setByName("Label", value);
        }
    if (position == 3)
        {
            return setByName("Tooltip", value);
        }
    if (position == 4)
        {
            return setByName("Required", value);
        }
    if (position == 5)
        {
            return setByName("History", value);
        }
    if (position == 6)
        {
            return setByName("IssueType", value);
        }
    if (position == 7)
        {
            return setByName("ProjectType", value);
        }
    if (position == 8)
        {
            return setByName("Project", value);
        }
    if (position == 9)
        {
            return setByName("Description", value);
        }
    if (position == 10)
        {
            return setByName("GroovyScript", value);
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
        save(TFieldConfigPeer.DATABASE_NAME);
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
                    TFieldConfigPeer.doInsert((TFieldConfig) this, con);
                    setNew(false);
                }
                else
                {
                    TFieldConfigPeer.doUpdate((TFieldConfig) this, con);
                }
            }


            if (collTConfigOptionsRoles != null)
            {
                for (int i = 0; i < collTConfigOptionsRoles.size(); i++)
                {
                    ((TConfigOptionsRole) collTConfigOptionsRoles.get(i)).save(con);
                }
            }

            if (collTTextBoxSettingss != null)
            {
                for (int i = 0; i < collTTextBoxSettingss.size(); i++)
                {
                    ((TTextBoxSettings) collTTextBoxSettingss.get(i)).save(con);
                }
            }

            if (collTGeneralSettingss != null)
            {
                for (int i = 0; i < collTGeneralSettingss.size(); i++)
                {
                    ((TGeneralSettings) collTGeneralSettingss.get(i)).save(con);
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
    public TFieldConfig copy() throws TorqueException
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
    public TFieldConfig copy(Connection con) throws TorqueException
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
    public TFieldConfig copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TFieldConfig(), deepcopy);
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
    public TFieldConfig copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TFieldConfig(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TFieldConfig copyInto(TFieldConfig copyObj) throws TorqueException
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
    protected TFieldConfig copyInto(TFieldConfig copyObj, Connection con) throws TorqueException
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
    protected TFieldConfig copyInto(TFieldConfig copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setField(field);
        copyObj.setLabel(label);
        copyObj.setTooltip(tooltip);
        copyObj.setRequired(required);
        copyObj.setHistory(history);
        copyObj.setIssueType(issueType);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setDescription(description);
        copyObj.setGroovyScript(groovyScript);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TConfigOptionsRole> vTConfigOptionsRoles = getTConfigOptionsRoles();
        if (vTConfigOptionsRoles != null)
        {
            for (int i = 0; i < vTConfigOptionsRoles.size(); i++)
            {
                TConfigOptionsRole obj =  vTConfigOptionsRoles.get(i);
                copyObj.addTConfigOptionsRole(obj.copy());
            }
        }
        else
        {
            copyObj.collTConfigOptionsRoles = null;
        }


        List<TTextBoxSettings> vTTextBoxSettingss = getTTextBoxSettingss();
        if (vTTextBoxSettingss != null)
        {
            for (int i = 0; i < vTTextBoxSettingss.size(); i++)
            {
                TTextBoxSettings obj =  vTTextBoxSettingss.get(i);
                copyObj.addTTextBoxSettings(obj.copy());
            }
        }
        else
        {
            copyObj.collTTextBoxSettingss = null;
        }


        List<TGeneralSettings> vTGeneralSettingss = getTGeneralSettingss();
        if (vTGeneralSettingss != null)
        {
            for (int i = 0; i < vTGeneralSettingss.size(); i++)
            {
                TGeneralSettings obj =  vTGeneralSettingss.get(i);
                copyObj.addTGeneralSettings(obj.copy());
            }
        }
        else
        {
            copyObj.collTGeneralSettingss = null;
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
    protected TFieldConfig copyInto(TFieldConfig copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setField(field);
        copyObj.setLabel(label);
        copyObj.setTooltip(tooltip);
        copyObj.setRequired(required);
        copyObj.setHistory(history);
        copyObj.setIssueType(issueType);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setDescription(description);
        copyObj.setGroovyScript(groovyScript);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TConfigOptionsRole> vTConfigOptionsRoles = getTConfigOptionsRoles(con);
        if (vTConfigOptionsRoles != null)
        {
            for (int i = 0; i < vTConfigOptionsRoles.size(); i++)
            {
                TConfigOptionsRole obj =  vTConfigOptionsRoles.get(i);
                copyObj.addTConfigOptionsRole(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTConfigOptionsRoles = null;
        }


        List<TTextBoxSettings> vTTextBoxSettingss = getTTextBoxSettingss(con);
        if (vTTextBoxSettingss != null)
        {
            for (int i = 0; i < vTTextBoxSettingss.size(); i++)
            {
                TTextBoxSettings obj =  vTTextBoxSettingss.get(i);
                copyObj.addTTextBoxSettings(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTTextBoxSettingss = null;
        }


        List<TGeneralSettings> vTGeneralSettingss = getTGeneralSettingss(con);
        if (vTGeneralSettingss != null)
        {
            for (int i = 0; i < vTGeneralSettingss.size(); i++)
            {
                TGeneralSettings obj =  vTGeneralSettingss.get(i);
                copyObj.addTGeneralSettings(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTGeneralSettingss = null;
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
    public TFieldConfigPeer getPeer()
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
        return TFieldConfigPeer.getTableMap();
    }

  
    /**
     * Creates a TFieldConfigBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TFieldConfigBean with the contents of this object
     */
    public TFieldConfigBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TFieldConfigBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TFieldConfigBean with the contents of this object
     */
    public TFieldConfigBean getBean(IdentityMap createdBeans)
    {
        TFieldConfigBean result = (TFieldConfigBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TFieldConfigBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setField(getField());
        result.setLabel(getLabel());
        result.setTooltip(getTooltip());
        result.setRequired(getRequired());
        result.setHistory(getHistory());
        result.setIssueType(getIssueType());
        result.setProjectType(getProjectType());
        result.setProject(getProject());
        result.setDescription(getDescription());
        result.setGroovyScript(getGroovyScript());
        result.setUuid(getUuid());



        if (collTConfigOptionsRoles != null)
        {
            List<TConfigOptionsRoleBean> relatedBeans = new ArrayList<TConfigOptionsRoleBean>(collTConfigOptionsRoles.size());
            for (Iterator<TConfigOptionsRole> collTConfigOptionsRolesIt = collTConfigOptionsRoles.iterator(); collTConfigOptionsRolesIt.hasNext(); )
            {
                TConfigOptionsRole related = (TConfigOptionsRole) collTConfigOptionsRolesIt.next();
                TConfigOptionsRoleBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTConfigOptionsRoleBeans(relatedBeans);
        }


        if (collTTextBoxSettingss != null)
        {
            List<TTextBoxSettingsBean> relatedBeans = new ArrayList<TTextBoxSettingsBean>(collTTextBoxSettingss.size());
            for (Iterator<TTextBoxSettings> collTTextBoxSettingssIt = collTTextBoxSettingss.iterator(); collTTextBoxSettingssIt.hasNext(); )
            {
                TTextBoxSettings related = (TTextBoxSettings) collTTextBoxSettingssIt.next();
                TTextBoxSettingsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTTextBoxSettingsBeans(relatedBeans);
        }


        if (collTGeneralSettingss != null)
        {
            List<TGeneralSettingsBean> relatedBeans = new ArrayList<TGeneralSettingsBean>(collTGeneralSettingss.size());
            for (Iterator<TGeneralSettings> collTGeneralSettingssIt = collTGeneralSettingss.iterator(); collTGeneralSettingssIt.hasNext(); )
            {
                TGeneralSettings related = (TGeneralSettings) collTGeneralSettingssIt.next();
                TGeneralSettingsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTGeneralSettingsBeans(relatedBeans);
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




        if (aTField != null)
        {
            TFieldBean relatedBean = aTField.getBean(createdBeans);
            result.setTFieldBean(relatedBean);
        }



        if (aTListType != null)
        {
            TListTypeBean relatedBean = aTListType.getBean(createdBeans);
            result.setTListTypeBean(relatedBean);
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



        if (aTScripts != null)
        {
            TScriptsBean relatedBean = aTScripts.getBean(createdBeans);
            result.setTScriptsBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TFieldConfig with the contents
     * of a TFieldConfigBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TFieldConfigBean which contents are used to create
     *        the resulting class
     * @return an instance of TFieldConfig with the contents of bean
     */
    public static TFieldConfig createTFieldConfig(TFieldConfigBean bean)
        throws TorqueException
    {
        return createTFieldConfig(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TFieldConfig with the contents
     * of a TFieldConfigBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TFieldConfigBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TFieldConfig with the contents of bean
     */

    public static TFieldConfig createTFieldConfig(TFieldConfigBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TFieldConfig result = (TFieldConfig) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TFieldConfig();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setField(bean.getField());
        result.setLabel(bean.getLabel());
        result.setTooltip(bean.getTooltip());
        result.setRequired(bean.getRequired());
        result.setHistory(bean.getHistory());
        result.setIssueType(bean.getIssueType());
        result.setProjectType(bean.getProjectType());
        result.setProject(bean.getProject());
        result.setDescription(bean.getDescription());
        result.setGroovyScript(bean.getGroovyScript());
        result.setUuid(bean.getUuid());



        {
            List<TConfigOptionsRoleBean> relatedBeans = bean.getTConfigOptionsRoleBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TConfigOptionsRoleBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TConfigOptionsRoleBean relatedBean =  relatedBeansIt.next();
                    TConfigOptionsRole related = TConfigOptionsRole.createTConfigOptionsRole(relatedBean, createdObjects);
                    result.addTConfigOptionsRoleFromBean(related);
                }
            }
        }


        {
            List<TTextBoxSettingsBean> relatedBeans = bean.getTTextBoxSettingsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TTextBoxSettingsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TTextBoxSettingsBean relatedBean =  relatedBeansIt.next();
                    TTextBoxSettings related = TTextBoxSettings.createTTextBoxSettings(relatedBean, createdObjects);
                    result.addTTextBoxSettingsFromBean(related);
                }
            }
        }


        {
            List<TGeneralSettingsBean> relatedBeans = bean.getTGeneralSettingsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TGeneralSettingsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TGeneralSettingsBean relatedBean =  relatedBeansIt.next();
                    TGeneralSettings related = TGeneralSettings.createTGeneralSettings(relatedBean, createdObjects);
                    result.addTGeneralSettingsFromBean(related);
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
            TFieldBean relatedBean = bean.getTFieldBean();
            if (relatedBean != null)
            {
                TField relatedObject = TField.createTField(relatedBean, createdObjects);
                result.setTField(relatedObject);
            }
        }



        {
            TListTypeBean relatedBean = bean.getTListTypeBean();
            if (relatedBean != null)
            {
                TListType relatedObject = TListType.createTListType(relatedBean, createdObjects);
                result.setTListType(relatedObject);
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



        {
            TScriptsBean relatedBean = bean.getTScriptsBean();
            if (relatedBean != null)
            {
                TScripts relatedObject = TScripts.createTScripts(relatedBean, createdObjects);
                result.setTScripts(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TConfigOptionsRole object to this object.
     * through the TConfigOptionsRole foreign key attribute
     *
     * @param toAdd TConfigOptionsRole
     */
    protected void addTConfigOptionsRoleFromBean(TConfigOptionsRole toAdd)
    {
        initTConfigOptionsRoles();
        collTConfigOptionsRoles.add(toAdd);
    }


    /**
     * Method called to associate a TTextBoxSettings object to this object.
     * through the TTextBoxSettings foreign key attribute
     *
     * @param toAdd TTextBoxSettings
     */
    protected void addTTextBoxSettingsFromBean(TTextBoxSettings toAdd)
    {
        initTTextBoxSettingss();
        collTTextBoxSettingss.add(toAdd);
    }


    /**
     * Method called to associate a TGeneralSettings object to this object.
     * through the TGeneralSettings foreign key attribute
     *
     * @param toAdd TGeneralSettings
     */
    protected void addTGeneralSettingsFromBean(TGeneralSettings toAdd)
    {
        initTGeneralSettingss();
        collTGeneralSettingss.add(toAdd);
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
        str.append("TFieldConfig:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Field = ")
           .append(getField())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Tooltip = ")
           .append(getTooltip())
           .append("\n");
        str.append("Required = ")
           .append(getRequired())
           .append("\n");
        str.append("History = ")
           .append(getHistory())
           .append("\n");
        str.append("IssueType = ")
           .append(getIssueType())
           .append("\n");
        str.append("ProjectType = ")
           .append(getProjectType())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("GroovyScript = ")
           .append(getGroovyScript())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
