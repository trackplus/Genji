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



import com.aurel.track.persist.TWorkflowActivity;
import com.aurel.track.persist.TWorkflowActivityPeer;
import com.aurel.track.persist.TListType;
import com.aurel.track.persist.TListTypePeer;
import com.aurel.track.persist.TProjectType;
import com.aurel.track.persist.TProjectTypePeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TWfActivityContextParamsBean;
import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TProjectBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TWfActivityContextParams
 */
public abstract class BaseTWfActivityContextParams extends TpBaseObject
{
    /** The Peer class */
    private static final TWfActivityContextParamsPeer peer =
        new TWfActivityContextParamsPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the workflowActivity field */
    private Integer workflowActivity;

    /** The value for the issueType field */
    private Integer issueType;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

    /** The value for the setterOrPartID field */
    private Integer setterOrPartID;

    /** The value for the activityParams field */
    private String activityParams;

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
     * Get the WorkflowActivity
     *
     * @return Integer
     */
    public Integer getWorkflowActivity()
    {
        return workflowActivity;
    }


    /**
     * Set the value of WorkflowActivity
     *
     * @param v new value
     */
    public void setWorkflowActivity(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workflowActivity, v))
        {
            this.workflowActivity = v;
            setModified(true);
        }


        if (aTWorkflowActivity != null && !ObjectUtils.equals(aTWorkflowActivity.getObjectID(), v))
        {
            aTWorkflowActivity = null;
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
     * Get the SetterOrPartID
     *
     * @return Integer
     */
    public Integer getSetterOrPartID()
    {
        return setterOrPartID;
    }


    /**
     * Set the value of SetterOrPartID
     *
     * @param v new value
     */
    public void setSetterOrPartID(Integer v) 
    {

        if (!ObjectUtils.equals(this.setterOrPartID, v))
        {
            this.setterOrPartID = v;
            setModified(true);
        }


    }

    /**
     * Get the ActivityParams
     *
     * @return String
     */
    public String getActivityParams()
    {
        return activityParams;
    }


    /**
     * Set the value of ActivityParams
     *
     * @param v new value
     */
    public void setActivityParams(String v) 
    {

        if (!ObjectUtils.equals(this.activityParams, v))
        {
            this.activityParams = v;
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

    



    private TWorkflowActivity aTWorkflowActivity;

    /**
     * Declares an association between this object and a TWorkflowActivity object
     *
     * @param v TWorkflowActivity
     * @throws TorqueException
     */
    public void setTWorkflowActivity(TWorkflowActivity v) throws TorqueException
    {
        if (v == null)
        {
            setWorkflowActivity((Integer) null);
        }
        else
        {
            setWorkflowActivity(v.getObjectID());
        }
        aTWorkflowActivity = v;
    }


    /**
     * Returns the associated TWorkflowActivity object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowActivity object
     * @throws TorqueException
     */
    public TWorkflowActivity getTWorkflowActivity()
        throws TorqueException
    {
        if (aTWorkflowActivity == null && (!ObjectUtils.equals(this.workflowActivity, null)))
        {
            aTWorkflowActivity = TWorkflowActivityPeer.retrieveByPK(SimpleKey.keyFor(this.workflowActivity));
        }
        return aTWorkflowActivity;
    }

    /**
     * Return the associated TWorkflowActivity object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowActivity object
     * @throws TorqueException
     */
    public TWorkflowActivity getTWorkflowActivity(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowActivity == null && (!ObjectUtils.equals(this.workflowActivity, null)))
        {
            aTWorkflowActivity = TWorkflowActivityPeer.retrieveByPK(SimpleKey.keyFor(this.workflowActivity), connection);
        }
        return aTWorkflowActivity;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowActivityKey(ObjectKey key) throws TorqueException
    {

        setWorkflowActivity(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("WorkflowActivity");
            fieldNames.add("IssueType");
            fieldNames.add("ProjectType");
            fieldNames.add("Project");
            fieldNames.add("SetterOrPartID");
            fieldNames.add("ActivityParams");
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
        if (name.equals("WorkflowActivity"))
        {
            return getWorkflowActivity();
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
        if (name.equals("SetterOrPartID"))
        {
            return getSetterOrPartID();
        }
        if (name.equals("ActivityParams"))
        {
            return getActivityParams();
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
        if (name.equals("WorkflowActivity"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkflowActivity((Integer) value);
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
        if (name.equals("SetterOrPartID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSetterOrPartID((Integer) value);
            return true;
        }
        if (name.equals("ActivityParams"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setActivityParams((String) value);
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
        if (name.equals(TWfActivityContextParamsPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TWfActivityContextParamsPeer.WORKFLOWACTIVITY))
        {
            return getWorkflowActivity();
        }
        if (name.equals(TWfActivityContextParamsPeer.ISSUETYPE))
        {
            return getIssueType();
        }
        if (name.equals(TWfActivityContextParamsPeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TWfActivityContextParamsPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TWfActivityContextParamsPeer.SETTERORPARTID))
        {
            return getSetterOrPartID();
        }
        if (name.equals(TWfActivityContextParamsPeer.ACTIVITYPARAMS))
        {
            return getActivityParams();
        }
        if (name.equals(TWfActivityContextParamsPeer.TPUUID))
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
      if (TWfActivityContextParamsPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TWfActivityContextParamsPeer.WORKFLOWACTIVITY.equals(name))
        {
            return setByName("WorkflowActivity", value);
        }
      if (TWfActivityContextParamsPeer.ISSUETYPE.equals(name))
        {
            return setByName("IssueType", value);
        }
      if (TWfActivityContextParamsPeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TWfActivityContextParamsPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TWfActivityContextParamsPeer.SETTERORPARTID.equals(name))
        {
            return setByName("SetterOrPartID", value);
        }
      if (TWfActivityContextParamsPeer.ACTIVITYPARAMS.equals(name))
        {
            return setByName("ActivityParams", value);
        }
      if (TWfActivityContextParamsPeer.TPUUID.equals(name))
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
            return getWorkflowActivity();
        }
        if (pos == 2)
        {
            return getIssueType();
        }
        if (pos == 3)
        {
            return getProjectType();
        }
        if (pos == 4)
        {
            return getProject();
        }
        if (pos == 5)
        {
            return getSetterOrPartID();
        }
        if (pos == 6)
        {
            return getActivityParams();
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
            return setByName("WorkflowActivity", value);
        }
    if (position == 2)
        {
            return setByName("IssueType", value);
        }
    if (position == 3)
        {
            return setByName("ProjectType", value);
        }
    if (position == 4)
        {
            return setByName("Project", value);
        }
    if (position == 5)
        {
            return setByName("SetterOrPartID", value);
        }
    if (position == 6)
        {
            return setByName("ActivityParams", value);
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
        save(TWfActivityContextParamsPeer.DATABASE_NAME);
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
                    TWfActivityContextParamsPeer.doInsert((TWfActivityContextParams) this, con);
                    setNew(false);
                }
                else
                {
                    TWfActivityContextParamsPeer.doUpdate((TWfActivityContextParams) this, con);
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
    public TWfActivityContextParams copy() throws TorqueException
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
    public TWfActivityContextParams copy(Connection con) throws TorqueException
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
    public TWfActivityContextParams copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TWfActivityContextParams(), deepcopy);
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
    public TWfActivityContextParams copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TWfActivityContextParams(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TWfActivityContextParams copyInto(TWfActivityContextParams copyObj) throws TorqueException
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
    protected TWfActivityContextParams copyInto(TWfActivityContextParams copyObj, Connection con) throws TorqueException
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
    protected TWfActivityContextParams copyInto(TWfActivityContextParams copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkflowActivity(workflowActivity);
        copyObj.setIssueType(issueType);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setSetterOrPartID(setterOrPartID);
        copyObj.setActivityParams(activityParams);
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
    protected TWfActivityContextParams copyInto(TWfActivityContextParams copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkflowActivity(workflowActivity);
        copyObj.setIssueType(issueType);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setSetterOrPartID(setterOrPartID);
        copyObj.setActivityParams(activityParams);
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
    public TWfActivityContextParamsPeer getPeer()
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
        return TWfActivityContextParamsPeer.getTableMap();
    }

  
    /**
     * Creates a TWfActivityContextParamsBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TWfActivityContextParamsBean with the contents of this object
     */
    public TWfActivityContextParamsBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TWfActivityContextParamsBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TWfActivityContextParamsBean with the contents of this object
     */
    public TWfActivityContextParamsBean getBean(IdentityMap createdBeans)
    {
        TWfActivityContextParamsBean result = (TWfActivityContextParamsBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TWfActivityContextParamsBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setWorkflowActivity(getWorkflowActivity());
        result.setIssueType(getIssueType());
        result.setProjectType(getProjectType());
        result.setProject(getProject());
        result.setSetterOrPartID(getSetterOrPartID());
        result.setActivityParams(getActivityParams());
        result.setUuid(getUuid());





        if (aTWorkflowActivity != null)
        {
            TWorkflowActivityBean relatedBean = aTWorkflowActivity.getBean(createdBeans);
            result.setTWorkflowActivityBean(relatedBean);
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
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TWfActivityContextParams with the contents
     * of a TWfActivityContextParamsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TWfActivityContextParamsBean which contents are used to create
     *        the resulting class
     * @return an instance of TWfActivityContextParams with the contents of bean
     */
    public static TWfActivityContextParams createTWfActivityContextParams(TWfActivityContextParamsBean bean)
        throws TorqueException
    {
        return createTWfActivityContextParams(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TWfActivityContextParams with the contents
     * of a TWfActivityContextParamsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TWfActivityContextParamsBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TWfActivityContextParams with the contents of bean
     */

    public static TWfActivityContextParams createTWfActivityContextParams(TWfActivityContextParamsBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TWfActivityContextParams result = (TWfActivityContextParams) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TWfActivityContextParams();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setWorkflowActivity(bean.getWorkflowActivity());
        result.setIssueType(bean.getIssueType());
        result.setProjectType(bean.getProjectType());
        result.setProject(bean.getProject());
        result.setSetterOrPartID(bean.getSetterOrPartID());
        result.setActivityParams(bean.getActivityParams());
        result.setUuid(bean.getUuid());





        {
            TWorkflowActivityBean relatedBean = bean.getTWorkflowActivityBean();
            if (relatedBean != null)
            {
                TWorkflowActivity relatedObject = TWorkflowActivity.createTWorkflowActivity(relatedBean, createdObjects);
                result.setTWorkflowActivity(relatedObject);
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
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TWfActivityContextParams:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("WorkflowActivity = ")
           .append(getWorkflowActivity())
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
        str.append("SetterOrPartID = ")
           .append(getSetterOrPartID())
           .append("\n");
        str.append("ActivityParams = ")
           .append(getActivityParams())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
