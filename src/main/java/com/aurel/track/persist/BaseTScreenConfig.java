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



import com.aurel.track.persist.TScreen;
import com.aurel.track.persist.TScreenPeer;
import com.aurel.track.persist.TListType;
import com.aurel.track.persist.TListTypePeer;
import com.aurel.track.persist.TProjectType;
import com.aurel.track.persist.TProjectTypePeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TAction;
import com.aurel.track.persist.TActionPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TActionBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TScreenConfig
 */
public abstract class BaseTScreenConfig extends TpBaseObject
{
    /** The Peer class */
    private static final TScreenConfigPeer peer =
        new TScreenConfigPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the description field */
    private String description;

    /** The value for the screen field */
    private Integer screen;

    /** The value for the issueType field */
    private Integer issueType;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

    /** The value for the action field */
    private Integer action;

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
     * Get the Screen
     *
     * @return Integer
     */
    public Integer getScreen()
    {
        return screen;
    }


    /**
     * Set the value of Screen
     *
     * @param v new value
     */
    public void setScreen(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.screen, v))
        {
            this.screen = v;
            setModified(true);
        }


        if (aTScreen != null && !ObjectUtils.equals(aTScreen.getObjectID(), v))
        {
            aTScreen = null;
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
     * Get the Action
     *
     * @return Integer
     */
    public Integer getAction()
    {
        return action;
    }


    /**
     * Set the value of Action
     *
     * @param v new value
     */
    public void setAction(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.action, v))
        {
            this.action = v;
            setModified(true);
        }


        if (aTAction != null && !ObjectUtils.equals(aTAction.getObjectID(), v))
        {
            aTAction = null;
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

    



    private TScreen aTScreen;

    /**
     * Declares an association between this object and a TScreen object
     *
     * @param v TScreen
     * @throws TorqueException
     */
    public void setTScreen(TScreen v) throws TorqueException
    {
        if (v == null)
        {
            setScreen((Integer) null);
        }
        else
        {
            setScreen(v.getObjectID());
        }
        aTScreen = v;
    }


    /**
     * Returns the associated TScreen object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TScreen object
     * @throws TorqueException
     */
    public TScreen getTScreen()
        throws TorqueException
    {
        if (aTScreen == null && (!ObjectUtils.equals(this.screen, null)))
        {
            aTScreen = TScreenPeer.retrieveByPK(SimpleKey.keyFor(this.screen));
        }
        return aTScreen;
    }

    /**
     * Return the associated TScreen object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TScreen object
     * @throws TorqueException
     */
    public TScreen getTScreen(Connection connection)
        throws TorqueException
    {
        if (aTScreen == null && (!ObjectUtils.equals(this.screen, null)))
        {
            aTScreen = TScreenPeer.retrieveByPK(SimpleKey.keyFor(this.screen), connection);
        }
        return aTScreen;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTScreenKey(ObjectKey key) throws TorqueException
    {

        setScreen(new Integer(((NumberKey) key).intValue()));
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




    private TAction aTAction;

    /**
     * Declares an association between this object and a TAction object
     *
     * @param v TAction
     * @throws TorqueException
     */
    public void setTAction(TAction v) throws TorqueException
    {
        if (v == null)
        {
            setAction((Integer) null);
        }
        else
        {
            setAction(v.getObjectID());
        }
        aTAction = v;
    }


    /**
     * Returns the associated TAction object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TAction object
     * @throws TorqueException
     */
    public TAction getTAction()
        throws TorqueException
    {
        if (aTAction == null && (!ObjectUtils.equals(this.action, null)))
        {
            aTAction = TActionPeer.retrieveByPK(SimpleKey.keyFor(this.action));
        }
        return aTAction;
    }

    /**
     * Return the associated TAction object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TAction object
     * @throws TorqueException
     */
    public TAction getTAction(Connection connection)
        throws TorqueException
    {
        if (aTAction == null && (!ObjectUtils.equals(this.action, null)))
        {
            aTAction = TActionPeer.retrieveByPK(SimpleKey.keyFor(this.action), connection);
        }
        return aTAction;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTActionKey(ObjectKey key) throws TorqueException
    {

        setAction(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Screen");
            fieldNames.add("IssueType");
            fieldNames.add("ProjectType");
            fieldNames.add("Project");
            fieldNames.add("Action");
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
        if (name.equals("Screen"))
        {
            return getScreen();
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
        if (name.equals("Action"))
        {
            return getAction();
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
        if (name.equals("Screen"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setScreen((Integer) value);
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
        if (name.equals("Action"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAction((Integer) value);
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
        if (name.equals(TScreenConfigPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TScreenConfigPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TScreenConfigPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TScreenConfigPeer.SCREEN))
        {
            return getScreen();
        }
        if (name.equals(TScreenConfigPeer.ISSUETYPE))
        {
            return getIssueType();
        }
        if (name.equals(TScreenConfigPeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TScreenConfigPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TScreenConfigPeer.ACTIONKEY))
        {
            return getAction();
        }
        if (name.equals(TScreenConfigPeer.TPUUID))
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
      if (TScreenConfigPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TScreenConfigPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TScreenConfigPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TScreenConfigPeer.SCREEN.equals(name))
        {
            return setByName("Screen", value);
        }
      if (TScreenConfigPeer.ISSUETYPE.equals(name))
        {
            return setByName("IssueType", value);
        }
      if (TScreenConfigPeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TScreenConfigPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TScreenConfigPeer.ACTIONKEY.equals(name))
        {
            return setByName("Action", value);
        }
      if (TScreenConfigPeer.TPUUID.equals(name))
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
            return getScreen();
        }
        if (pos == 4)
        {
            return getIssueType();
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
            return getAction();
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
            return setByName("Description", value);
        }
    if (position == 3)
        {
            return setByName("Screen", value);
        }
    if (position == 4)
        {
            return setByName("IssueType", value);
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
            return setByName("Action", value);
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
        save(TScreenConfigPeer.DATABASE_NAME);
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
                    TScreenConfigPeer.doInsert((TScreenConfig) this, con);
                    setNew(false);
                }
                else
                {
                    TScreenConfigPeer.doUpdate((TScreenConfig) this, con);
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
    public TScreenConfig copy() throws TorqueException
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
    public TScreenConfig copy(Connection con) throws TorqueException
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
    public TScreenConfig copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TScreenConfig(), deepcopy);
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
    public TScreenConfig copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TScreenConfig(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TScreenConfig copyInto(TScreenConfig copyObj) throws TorqueException
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
    protected TScreenConfig copyInto(TScreenConfig copyObj, Connection con) throws TorqueException
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
    protected TScreenConfig copyInto(TScreenConfig copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setDescription(description);
        copyObj.setScreen(screen);
        copyObj.setIssueType(issueType);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setAction(action);
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
    protected TScreenConfig copyInto(TScreenConfig copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setDescription(description);
        copyObj.setScreen(screen);
        copyObj.setIssueType(issueType);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setAction(action);
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
    public TScreenConfigPeer getPeer()
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
        return TScreenConfigPeer.getTableMap();
    }

  
    /**
     * Creates a TScreenConfigBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TScreenConfigBean with the contents of this object
     */
    public TScreenConfigBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TScreenConfigBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TScreenConfigBean with the contents of this object
     */
    public TScreenConfigBean getBean(IdentityMap createdBeans)
    {
        TScreenConfigBean result = (TScreenConfigBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TScreenConfigBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setScreen(getScreen());
        result.setIssueType(getIssueType());
        result.setProjectType(getProjectType());
        result.setProject(getProject());
        result.setAction(getAction());
        result.setUuid(getUuid());





        if (aTScreen != null)
        {
            TScreenBean relatedBean = aTScreen.getBean(createdBeans);
            result.setTScreenBean(relatedBean);
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



        if (aTAction != null)
        {
            TActionBean relatedBean = aTAction.getBean(createdBeans);
            result.setTActionBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TScreenConfig with the contents
     * of a TScreenConfigBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TScreenConfigBean which contents are used to create
     *        the resulting class
     * @return an instance of TScreenConfig with the contents of bean
     */
    public static TScreenConfig createTScreenConfig(TScreenConfigBean bean)
        throws TorqueException
    {
        return createTScreenConfig(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TScreenConfig with the contents
     * of a TScreenConfigBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TScreenConfigBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TScreenConfig with the contents of bean
     */

    public static TScreenConfig createTScreenConfig(TScreenConfigBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TScreenConfig result = (TScreenConfig) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TScreenConfig();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setDescription(bean.getDescription());
        result.setScreen(bean.getScreen());
        result.setIssueType(bean.getIssueType());
        result.setProjectType(bean.getProjectType());
        result.setProject(bean.getProject());
        result.setAction(bean.getAction());
        result.setUuid(bean.getUuid());





        {
            TScreenBean relatedBean = bean.getTScreenBean();
            if (relatedBean != null)
            {
                TScreen relatedObject = TScreen.createTScreen(relatedBean, createdObjects);
                result.setTScreen(relatedObject);
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
            TActionBean relatedBean = bean.getTActionBean();
            if (relatedBean != null)
            {
                TAction relatedObject = TAction.createTAction(relatedBean, createdObjects);
                result.setTAction(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TScreenConfig:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Screen = ")
           .append(getScreen())
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
        str.append("Action = ")
           .append(getAction())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
