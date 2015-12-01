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



import com.aurel.track.persist.TProjectType;
import com.aurel.track.persist.TProjectTypePeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TPersonBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TReportLayout
 */
public abstract class BaseTReportLayout extends TpBaseObject
{
    /** The Peer class */
    private static final TReportLayoutPeer peer =
        new TReportLayoutPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

    /** The value for the person field */
    private Integer person;

    /** The value for the reportField field */
    private Integer reportField;

    /** The value for the fieldPosition field */
    private Integer fieldPosition;

    /** The value for the fieldWidth field */
    private Integer fieldWidth;

    /** The value for the fieldSortOrder field */
    private Integer fieldSortOrder;

    /** The value for the fieldSortDirection field */
    private String fieldSortDirection;

    /** The value for the fieldType field */
    private Integer fieldType;

    /** The value for the expanding field */
    private String expanding;

    /** The value for the layoutKey field */
    private Integer layoutKey;

    /** The value for the queryID field */
    private Integer queryID;

    /** The value for the queryType field */
    private Integer queryType;

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
     * Get the ReportField
     *
     * @return Integer
     */
    public Integer getReportField()
    {
        return reportField;
    }


    /**
     * Set the value of ReportField
     *
     * @param v new value
     */
    public void setReportField(Integer v) 
    {

        if (!ObjectUtils.equals(this.reportField, v))
        {
            this.reportField = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldPosition
     *
     * @return Integer
     */
    public Integer getFieldPosition()
    {
        return fieldPosition;
    }


    /**
     * Set the value of FieldPosition
     *
     * @param v new value
     */
    public void setFieldPosition(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldPosition, v))
        {
            this.fieldPosition = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldWidth
     *
     * @return Integer
     */
    public Integer getFieldWidth()
    {
        return fieldWidth;
    }


    /**
     * Set the value of FieldWidth
     *
     * @param v new value
     */
    public void setFieldWidth(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldWidth, v))
        {
            this.fieldWidth = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldSortOrder
     *
     * @return Integer
     */
    public Integer getFieldSortOrder()
    {
        return fieldSortOrder;
    }


    /**
     * Set the value of FieldSortOrder
     *
     * @param v new value
     */
    public void setFieldSortOrder(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldSortOrder, v))
        {
            this.fieldSortOrder = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldSortDirection
     *
     * @return String
     */
    public String getFieldSortDirection()
    {
        return fieldSortDirection;
    }


    /**
     * Set the value of FieldSortDirection
     *
     * @param v new value
     */
    public void setFieldSortDirection(String v) 
    {

        if (!ObjectUtils.equals(this.fieldSortDirection, v))
        {
            this.fieldSortDirection = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldType
     *
     * @return Integer
     */
    public Integer getFieldType()
    {
        return fieldType;
    }


    /**
     * Set the value of FieldType
     *
     * @param v new value
     */
    public void setFieldType(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldType, v))
        {
            this.fieldType = v;
            setModified(true);
        }


    }

    /**
     * Get the Expanding
     *
     * @return String
     */
    public String getExpanding()
    {
        return expanding;
    }


    /**
     * Set the value of Expanding
     *
     * @param v new value
     */
    public void setExpanding(String v) 
    {

        if (!ObjectUtils.equals(this.expanding, v))
        {
            this.expanding = v;
            setModified(true);
        }


    }

    /**
     * Get the LayoutKey
     *
     * @return Integer
     */
    public Integer getLayoutKey()
    {
        return layoutKey;
    }


    /**
     * Set the value of LayoutKey
     *
     * @param v new value
     */
    public void setLayoutKey(Integer v) 
    {

        if (!ObjectUtils.equals(this.layoutKey, v))
        {
            this.layoutKey = v;
            setModified(true);
        }


    }

    /**
     * Get the QueryID
     *
     * @return Integer
     */
    public Integer getQueryID()
    {
        return queryID;
    }


    /**
     * Set the value of QueryID
     *
     * @param v new value
     */
    public void setQueryID(Integer v) 
    {

        if (!ObjectUtils.equals(this.queryID, v))
        {
            this.queryID = v;
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
            fieldNames.add("ProjectType");
            fieldNames.add("Project");
            fieldNames.add("Person");
            fieldNames.add("ReportField");
            fieldNames.add("FieldPosition");
            fieldNames.add("FieldWidth");
            fieldNames.add("FieldSortOrder");
            fieldNames.add("FieldSortDirection");
            fieldNames.add("FieldType");
            fieldNames.add("Expanding");
            fieldNames.add("LayoutKey");
            fieldNames.add("QueryID");
            fieldNames.add("QueryType");
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
        if (name.equals("ProjectType"))
        {
            return getProjectType();
        }
        if (name.equals("Project"))
        {
            return getProject();
        }
        if (name.equals("Person"))
        {
            return getPerson();
        }
        if (name.equals("ReportField"))
        {
            return getReportField();
        }
        if (name.equals("FieldPosition"))
        {
            return getFieldPosition();
        }
        if (name.equals("FieldWidth"))
        {
            return getFieldWidth();
        }
        if (name.equals("FieldSortOrder"))
        {
            return getFieldSortOrder();
        }
        if (name.equals("FieldSortDirection"))
        {
            return getFieldSortDirection();
        }
        if (name.equals("FieldType"))
        {
            return getFieldType();
        }
        if (name.equals("Expanding"))
        {
            return getExpanding();
        }
        if (name.equals("LayoutKey"))
        {
            return getLayoutKey();
        }
        if (name.equals("QueryID"))
        {
            return getQueryID();
        }
        if (name.equals("QueryType"))
        {
            return getQueryType();
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
        if (name.equals("ReportField"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReportField((Integer) value);
            return true;
        }
        if (name.equals("FieldPosition"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldPosition((Integer) value);
            return true;
        }
        if (name.equals("FieldWidth"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldWidth((Integer) value);
            return true;
        }
        if (name.equals("FieldSortOrder"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldSortOrder((Integer) value);
            return true;
        }
        if (name.equals("FieldSortDirection"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldSortDirection((String) value);
            return true;
        }
        if (name.equals("FieldType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldType((Integer) value);
            return true;
        }
        if (name.equals("Expanding"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setExpanding((String) value);
            return true;
        }
        if (name.equals("LayoutKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLayoutKey((Integer) value);
            return true;
        }
        if (name.equals("QueryID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setQueryID((Integer) value);
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
        if (name.equals(TReportLayoutPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TReportLayoutPeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TReportLayoutPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TReportLayoutPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TReportLayoutPeer.REPORTFIELD))
        {
            return getReportField();
        }
        if (name.equals(TReportLayoutPeer.FPOSITION))
        {
            return getFieldPosition();
        }
        if (name.equals(TReportLayoutPeer.FWIDTH))
        {
            return getFieldWidth();
        }
        if (name.equals(TReportLayoutPeer.FSORTORDER))
        {
            return getFieldSortOrder();
        }
        if (name.equals(TReportLayoutPeer.FSORTDIR))
        {
            return getFieldSortDirection();
        }
        if (name.equals(TReportLayoutPeer.FIELDTYPE))
        {
            return getFieldType();
        }
        if (name.equals(TReportLayoutPeer.EXPANDING))
        {
            return getExpanding();
        }
        if (name.equals(TReportLayoutPeer.LAYOUTKEY))
        {
            return getLayoutKey();
        }
        if (name.equals(TReportLayoutPeer.QUERYID))
        {
            return getQueryID();
        }
        if (name.equals(TReportLayoutPeer.QUERYTYPE))
        {
            return getQueryType();
        }
        if (name.equals(TReportLayoutPeer.TPUUID))
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
      if (TReportLayoutPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TReportLayoutPeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TReportLayoutPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TReportLayoutPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TReportLayoutPeer.REPORTFIELD.equals(name))
        {
            return setByName("ReportField", value);
        }
      if (TReportLayoutPeer.FPOSITION.equals(name))
        {
            return setByName("FieldPosition", value);
        }
      if (TReportLayoutPeer.FWIDTH.equals(name))
        {
            return setByName("FieldWidth", value);
        }
      if (TReportLayoutPeer.FSORTORDER.equals(name))
        {
            return setByName("FieldSortOrder", value);
        }
      if (TReportLayoutPeer.FSORTDIR.equals(name))
        {
            return setByName("FieldSortDirection", value);
        }
      if (TReportLayoutPeer.FIELDTYPE.equals(name))
        {
            return setByName("FieldType", value);
        }
      if (TReportLayoutPeer.EXPANDING.equals(name))
        {
            return setByName("Expanding", value);
        }
      if (TReportLayoutPeer.LAYOUTKEY.equals(name))
        {
            return setByName("LayoutKey", value);
        }
      if (TReportLayoutPeer.QUERYID.equals(name))
        {
            return setByName("QueryID", value);
        }
      if (TReportLayoutPeer.QUERYTYPE.equals(name))
        {
            return setByName("QueryType", value);
        }
      if (TReportLayoutPeer.TPUUID.equals(name))
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
            return getProjectType();
        }
        if (pos == 2)
        {
            return getProject();
        }
        if (pos == 3)
        {
            return getPerson();
        }
        if (pos == 4)
        {
            return getReportField();
        }
        if (pos == 5)
        {
            return getFieldPosition();
        }
        if (pos == 6)
        {
            return getFieldWidth();
        }
        if (pos == 7)
        {
            return getFieldSortOrder();
        }
        if (pos == 8)
        {
            return getFieldSortDirection();
        }
        if (pos == 9)
        {
            return getFieldType();
        }
        if (pos == 10)
        {
            return getExpanding();
        }
        if (pos == 11)
        {
            return getLayoutKey();
        }
        if (pos == 12)
        {
            return getQueryID();
        }
        if (pos == 13)
        {
            return getQueryType();
        }
        if (pos == 14)
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
            return setByName("ProjectType", value);
        }
    if (position == 2)
        {
            return setByName("Project", value);
        }
    if (position == 3)
        {
            return setByName("Person", value);
        }
    if (position == 4)
        {
            return setByName("ReportField", value);
        }
    if (position == 5)
        {
            return setByName("FieldPosition", value);
        }
    if (position == 6)
        {
            return setByName("FieldWidth", value);
        }
    if (position == 7)
        {
            return setByName("FieldSortOrder", value);
        }
    if (position == 8)
        {
            return setByName("FieldSortDirection", value);
        }
    if (position == 9)
        {
            return setByName("FieldType", value);
        }
    if (position == 10)
        {
            return setByName("Expanding", value);
        }
    if (position == 11)
        {
            return setByName("LayoutKey", value);
        }
    if (position == 12)
        {
            return setByName("QueryID", value);
        }
    if (position == 13)
        {
            return setByName("QueryType", value);
        }
    if (position == 14)
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
        save(TReportLayoutPeer.DATABASE_NAME);
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
                    TReportLayoutPeer.doInsert((TReportLayout) this, con);
                    setNew(false);
                }
                else
                {
                    TReportLayoutPeer.doUpdate((TReportLayout) this, con);
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
    public TReportLayout copy() throws TorqueException
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
    public TReportLayout copy(Connection con) throws TorqueException
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
    public TReportLayout copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TReportLayout(), deepcopy);
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
    public TReportLayout copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TReportLayout(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TReportLayout copyInto(TReportLayout copyObj) throws TorqueException
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
    protected TReportLayout copyInto(TReportLayout copyObj, Connection con) throws TorqueException
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
    protected TReportLayout copyInto(TReportLayout copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setPerson(person);
        copyObj.setReportField(reportField);
        copyObj.setFieldPosition(fieldPosition);
        copyObj.setFieldWidth(fieldWidth);
        copyObj.setFieldSortOrder(fieldSortOrder);
        copyObj.setFieldSortDirection(fieldSortDirection);
        copyObj.setFieldType(fieldType);
        copyObj.setExpanding(expanding);
        copyObj.setLayoutKey(layoutKey);
        copyObj.setQueryID(queryID);
        copyObj.setQueryType(queryType);
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
    protected TReportLayout copyInto(TReportLayout copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setProjectType(projectType);
        copyObj.setProject(project);
        copyObj.setPerson(person);
        copyObj.setReportField(reportField);
        copyObj.setFieldPosition(fieldPosition);
        copyObj.setFieldWidth(fieldWidth);
        copyObj.setFieldSortOrder(fieldSortOrder);
        copyObj.setFieldSortDirection(fieldSortDirection);
        copyObj.setFieldType(fieldType);
        copyObj.setExpanding(expanding);
        copyObj.setLayoutKey(layoutKey);
        copyObj.setQueryID(queryID);
        copyObj.setQueryType(queryType);
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
    public TReportLayoutPeer getPeer()
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
        return TReportLayoutPeer.getTableMap();
    }

  
    /**
     * Creates a TReportLayoutBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TReportLayoutBean with the contents of this object
     */
    public TReportLayoutBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TReportLayoutBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TReportLayoutBean with the contents of this object
     */
    public TReportLayoutBean getBean(IdentityMap createdBeans)
    {
        TReportLayoutBean result = (TReportLayoutBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TReportLayoutBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setProjectType(getProjectType());
        result.setProject(getProject());
        result.setPerson(getPerson());
        result.setReportField(getReportField());
        result.setFieldPosition(getFieldPosition());
        result.setFieldWidth(getFieldWidth());
        result.setFieldSortOrder(getFieldSortOrder());
        result.setFieldSortDirection(getFieldSortDirection());
        result.setFieldType(getFieldType());
        result.setExpanding(getExpanding());
        result.setLayoutKey(getLayoutKey());
        result.setQueryID(getQueryID());
        result.setQueryType(getQueryType());
        result.setUuid(getUuid());





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
     * Creates an instance of TReportLayout with the contents
     * of a TReportLayoutBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TReportLayoutBean which contents are used to create
     *        the resulting class
     * @return an instance of TReportLayout with the contents of bean
     */
    public static TReportLayout createTReportLayout(TReportLayoutBean bean)
        throws TorqueException
    {
        return createTReportLayout(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TReportLayout with the contents
     * of a TReportLayoutBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TReportLayoutBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TReportLayout with the contents of bean
     */

    public static TReportLayout createTReportLayout(TReportLayoutBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TReportLayout result = (TReportLayout) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TReportLayout();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setProjectType(bean.getProjectType());
        result.setProject(bean.getProject());
        result.setPerson(bean.getPerson());
        result.setReportField(bean.getReportField());
        result.setFieldPosition(bean.getFieldPosition());
        result.setFieldWidth(bean.getFieldWidth());
        result.setFieldSortOrder(bean.getFieldSortOrder());
        result.setFieldSortDirection(bean.getFieldSortDirection());
        result.setFieldType(bean.getFieldType());
        result.setExpanding(bean.getExpanding());
        result.setLayoutKey(bean.getLayoutKey());
        result.setQueryID(bean.getQueryID());
        result.setQueryType(bean.getQueryType());
        result.setUuid(bean.getUuid());





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



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TReportLayout:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("ProjectType = ")
           .append(getProjectType())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("ReportField = ")
           .append(getReportField())
           .append("\n");
        str.append("FieldPosition = ")
           .append(getFieldPosition())
           .append("\n");
        str.append("FieldWidth = ")
           .append(getFieldWidth())
           .append("\n");
        str.append("FieldSortOrder = ")
           .append(getFieldSortOrder())
           .append("\n");
        str.append("FieldSortDirection = ")
           .append(getFieldSortDirection())
           .append("\n");
        str.append("FieldType = ")
           .append(getFieldType())
           .append("\n");
        str.append("Expanding = ")
           .append(getExpanding())
           .append("\n");
        str.append("LayoutKey = ")
           .append(getLayoutKey())
           .append("\n");
        str.append("QueryID = ")
           .append(getQueryID())
           .append("\n");
        str.append("QueryType = ")
           .append(getQueryType())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
