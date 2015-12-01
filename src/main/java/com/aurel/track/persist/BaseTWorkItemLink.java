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



import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.persist.TLinkType;
import com.aurel.track.persist.TLinkTypePeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TPersonBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TWorkItemLink
 */
public abstract class BaseTWorkItemLink extends TpBaseObject
{
    /** The Peer class */
    private static final TWorkItemLinkPeer peer =
        new TWorkItemLinkPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the linkIsCrossProject field */
    private String linkIsCrossProject = "N";

    /** The value for the linkPred field */
    private Integer linkPred;

    /** The value for the linkSucc field */
    private Integer linkSucc;

    /** The value for the linkType field */
    private Integer linkType;

    /** The value for the linkDirection field */
    private Integer linkDirection;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the linkLag field */
    private Double linkLag;

    /** The value for the linkLagFormat field */
    private Integer linkLagFormat;

    /** The value for the stringValue1 field */
    private String stringValue1;

    /** The value for the stringValue2 field */
    private String stringValue2;

    /** The value for the stringValue3 field */
    private String stringValue3;

    /** The value for the integerValue1 field */
    private Integer integerValue1;

    /** The value for the integerValue2 field */
    private Integer integerValue2;

    /** The value for the integerValue3 field */
    private Integer integerValue3;

    /** The value for the dATEVALUE field */
    private Date dATEVALUE;

    /** The value for the description field */
    private String description;

    /** The value for the externalLink field */
    private String externalLink;

    /** The value for the changedBy field */
    private Integer changedBy;

    /** The value for the lastEdit field */
    private Date lastEdit;

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
     * Get the LinkIsCrossProject
     *
     * @return String
     */
    public String getLinkIsCrossProject()
    {
        return linkIsCrossProject;
    }


    /**
     * Set the value of LinkIsCrossProject
     *
     * @param v new value
     */
    public void setLinkIsCrossProject(String v) 
    {

        if (!ObjectUtils.equals(this.linkIsCrossProject, v))
        {
            this.linkIsCrossProject = v;
            setModified(true);
        }


    }

    /**
     * Get the LinkPred
     *
     * @return Integer
     */
    public Integer getLinkPred()
    {
        return linkPred;
    }


    /**
     * Set the value of LinkPred
     *
     * @param v new value
     */
    public void setLinkPred(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.linkPred, v))
        {
            this.linkPred = v;
            setModified(true);
        }


        if (aTWorkItemRelatedByLinkPred != null && !ObjectUtils.equals(aTWorkItemRelatedByLinkPred.getObjectID(), v))
        {
            aTWorkItemRelatedByLinkPred = null;
        }

    }

    /**
     * Get the LinkSucc
     *
     * @return Integer
     */
    public Integer getLinkSucc()
    {
        return linkSucc;
    }


    /**
     * Set the value of LinkSucc
     *
     * @param v new value
     */
    public void setLinkSucc(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.linkSucc, v))
        {
            this.linkSucc = v;
            setModified(true);
        }


        if (aTWorkItemRelatedByLinkSucc != null && !ObjectUtils.equals(aTWorkItemRelatedByLinkSucc.getObjectID(), v))
        {
            aTWorkItemRelatedByLinkSucc = null;
        }

    }

    /**
     * Get the LinkType
     *
     * @return Integer
     */
    public Integer getLinkType()
    {
        return linkType;
    }


    /**
     * Set the value of LinkType
     *
     * @param v new value
     */
    public void setLinkType(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.linkType, v))
        {
            this.linkType = v;
            setModified(true);
        }


        if (aTLinkType != null && !ObjectUtils.equals(aTLinkType.getObjectID(), v))
        {
            aTLinkType = null;
        }

    }

    /**
     * Get the LinkDirection
     *
     * @return Integer
     */
    public Integer getLinkDirection()
    {
        return linkDirection;
    }


    /**
     * Set the value of LinkDirection
     *
     * @param v new value
     */
    public void setLinkDirection(Integer v) 
    {

        if (!ObjectUtils.equals(this.linkDirection, v))
        {
            this.linkDirection = v;
            setModified(true);
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
     * Get the LinkLag
     *
     * @return Double
     */
    public Double getLinkLag()
    {
        return linkLag;
    }


    /**
     * Set the value of LinkLag
     *
     * @param v new value
     */
    public void setLinkLag(Double v) 
    {

        if (!ObjectUtils.equals(this.linkLag, v))
        {
            this.linkLag = v;
            setModified(true);
        }


    }

    /**
     * Get the LinkLagFormat
     *
     * @return Integer
     */
    public Integer getLinkLagFormat()
    {
        return linkLagFormat;
    }


    /**
     * Set the value of LinkLagFormat
     *
     * @param v new value
     */
    public void setLinkLagFormat(Integer v) 
    {

        if (!ObjectUtils.equals(this.linkLagFormat, v))
        {
            this.linkLagFormat = v;
            setModified(true);
        }


    }

    /**
     * Get the StringValue1
     *
     * @return String
     */
    public String getStringValue1()
    {
        return stringValue1;
    }


    /**
     * Set the value of StringValue1
     *
     * @param v new value
     */
    public void setStringValue1(String v) 
    {

        if (!ObjectUtils.equals(this.stringValue1, v))
        {
            this.stringValue1 = v;
            setModified(true);
        }


    }

    /**
     * Get the StringValue2
     *
     * @return String
     */
    public String getStringValue2()
    {
        return stringValue2;
    }


    /**
     * Set the value of StringValue2
     *
     * @param v new value
     */
    public void setStringValue2(String v) 
    {

        if (!ObjectUtils.equals(this.stringValue2, v))
        {
            this.stringValue2 = v;
            setModified(true);
        }


    }

    /**
     * Get the StringValue3
     *
     * @return String
     */
    public String getStringValue3()
    {
        return stringValue3;
    }


    /**
     * Set the value of StringValue3
     *
     * @param v new value
     */
    public void setStringValue3(String v) 
    {

        if (!ObjectUtils.equals(this.stringValue3, v))
        {
            this.stringValue3 = v;
            setModified(true);
        }


    }

    /**
     * Get the IntegerValue1
     *
     * @return Integer
     */
    public Integer getIntegerValue1()
    {
        return integerValue1;
    }


    /**
     * Set the value of IntegerValue1
     *
     * @param v new value
     */
    public void setIntegerValue1(Integer v) 
    {

        if (!ObjectUtils.equals(this.integerValue1, v))
        {
            this.integerValue1 = v;
            setModified(true);
        }


    }

    /**
     * Get the IntegerValue2
     *
     * @return Integer
     */
    public Integer getIntegerValue2()
    {
        return integerValue2;
    }


    /**
     * Set the value of IntegerValue2
     *
     * @param v new value
     */
    public void setIntegerValue2(Integer v) 
    {

        if (!ObjectUtils.equals(this.integerValue2, v))
        {
            this.integerValue2 = v;
            setModified(true);
        }


    }

    /**
     * Get the IntegerValue3
     *
     * @return Integer
     */
    public Integer getIntegerValue3()
    {
        return integerValue3;
    }


    /**
     * Set the value of IntegerValue3
     *
     * @param v new value
     */
    public void setIntegerValue3(Integer v) 
    {

        if (!ObjectUtils.equals(this.integerValue3, v))
        {
            this.integerValue3 = v;
            setModified(true);
        }


    }

    /**
     * Get the DATEVALUE
     *
     * @return Date
     */
    public Date getDATEVALUE()
    {
        return dATEVALUE;
    }


    /**
     * Set the value of DATEVALUE
     *
     * @param v new value
     */
    public void setDATEVALUE(Date v) 
    {

        if (!ObjectUtils.equals(this.dATEVALUE, v))
        {
            this.dATEVALUE = v;
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
     * Get the ExternalLink
     *
     * @return String
     */
    public String getExternalLink()
    {
        return externalLink;
    }


    /**
     * Set the value of ExternalLink
     *
     * @param v new value
     */
    public void setExternalLink(String v) 
    {

        if (!ObjectUtils.equals(this.externalLink, v))
        {
            this.externalLink = v;
            setModified(true);
        }


    }

    /**
     * Get the ChangedBy
     *
     * @return Integer
     */
    public Integer getChangedBy()
    {
        return changedBy;
    }


    /**
     * Set the value of ChangedBy
     *
     * @param v new value
     */
    public void setChangedBy(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.changedBy, v))
        {
            this.changedBy = v;
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

    



    private TWorkItem aTWorkItemRelatedByLinkPred;

    /**
     * Declares an association between this object and a TWorkItem object
     *
     * @param v TWorkItem
     * @throws TorqueException
     */
    public void setTWorkItemRelatedByLinkPred(TWorkItem v) throws TorqueException
    {
        if (v == null)
        {
            setLinkPred((Integer) null);
        }
        else
        {
            setLinkPred(v.getObjectID());
        }
        aTWorkItemRelatedByLinkPred = v;
    }


    /**
     * Returns the associated TWorkItem object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItemRelatedByLinkPred()
        throws TorqueException
    {
        if (aTWorkItemRelatedByLinkPred == null && (!ObjectUtils.equals(this.linkPred, null)))
        {
            aTWorkItemRelatedByLinkPred = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.linkPred));
        }
        return aTWorkItemRelatedByLinkPred;
    }

    /**
     * Return the associated TWorkItem object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItemRelatedByLinkPred(Connection connection)
        throws TorqueException
    {
        if (aTWorkItemRelatedByLinkPred == null && (!ObjectUtils.equals(this.linkPred, null)))
        {
            aTWorkItemRelatedByLinkPred = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.linkPred), connection);
        }
        return aTWorkItemRelatedByLinkPred;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkItemRelatedByLinkPredKey(ObjectKey key) throws TorqueException
    {

        setLinkPred(new Integer(((NumberKey) key).intValue()));
    }




    private TWorkItem aTWorkItemRelatedByLinkSucc;

    /**
     * Declares an association between this object and a TWorkItem object
     *
     * @param v TWorkItem
     * @throws TorqueException
     */
    public void setTWorkItemRelatedByLinkSucc(TWorkItem v) throws TorqueException
    {
        if (v == null)
        {
            setLinkSucc((Integer) null);
        }
        else
        {
            setLinkSucc(v.getObjectID());
        }
        aTWorkItemRelatedByLinkSucc = v;
    }


    /**
     * Returns the associated TWorkItem object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItemRelatedByLinkSucc()
        throws TorqueException
    {
        if (aTWorkItemRelatedByLinkSucc == null && (!ObjectUtils.equals(this.linkSucc, null)))
        {
            aTWorkItemRelatedByLinkSucc = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.linkSucc));
        }
        return aTWorkItemRelatedByLinkSucc;
    }

    /**
     * Return the associated TWorkItem object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItemRelatedByLinkSucc(Connection connection)
        throws TorqueException
    {
        if (aTWorkItemRelatedByLinkSucc == null && (!ObjectUtils.equals(this.linkSucc, null)))
        {
            aTWorkItemRelatedByLinkSucc = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.linkSucc), connection);
        }
        return aTWorkItemRelatedByLinkSucc;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkItemRelatedByLinkSuccKey(ObjectKey key) throws TorqueException
    {

        setLinkSucc(new Integer(((NumberKey) key).intValue()));
    }




    private TLinkType aTLinkType;

    /**
     * Declares an association between this object and a TLinkType object
     *
     * @param v TLinkType
     * @throws TorqueException
     */
    public void setTLinkType(TLinkType v) throws TorqueException
    {
        if (v == null)
        {
            setLinkType((Integer) null);
        }
        else
        {
            setLinkType(v.getObjectID());
        }
        aTLinkType = v;
    }


    /**
     * Returns the associated TLinkType object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TLinkType object
     * @throws TorqueException
     */
    public TLinkType getTLinkType()
        throws TorqueException
    {
        if (aTLinkType == null && (!ObjectUtils.equals(this.linkType, null)))
        {
            aTLinkType = TLinkTypePeer.retrieveByPK(SimpleKey.keyFor(this.linkType));
        }
        return aTLinkType;
    }

    /**
     * Return the associated TLinkType object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TLinkType object
     * @throws TorqueException
     */
    public TLinkType getTLinkType(Connection connection)
        throws TorqueException
    {
        if (aTLinkType == null && (!ObjectUtils.equals(this.linkType, null)))
        {
            aTLinkType = TLinkTypePeer.retrieveByPK(SimpleKey.keyFor(this.linkType), connection);
        }
        return aTLinkType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTLinkTypeKey(ObjectKey key) throws TorqueException
    {

        setLinkType(new Integer(((NumberKey) key).intValue()));
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
            setChangedBy((Integer) null);
        }
        else
        {
            setChangedBy(v.getObjectID());
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
        if (aTPerson == null && (!ObjectUtils.equals(this.changedBy, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.changedBy));
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
        if (aTPerson == null && (!ObjectUtils.equals(this.changedBy, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.changedBy), connection);
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

        setChangedBy(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("LinkIsCrossProject");
            fieldNames.add("LinkPred");
            fieldNames.add("LinkSucc");
            fieldNames.add("LinkType");
            fieldNames.add("LinkDirection");
            fieldNames.add("Sortorder");
            fieldNames.add("LinkLag");
            fieldNames.add("LinkLagFormat");
            fieldNames.add("StringValue1");
            fieldNames.add("StringValue2");
            fieldNames.add("StringValue3");
            fieldNames.add("IntegerValue1");
            fieldNames.add("IntegerValue2");
            fieldNames.add("IntegerValue3");
            fieldNames.add("DATEVALUE");
            fieldNames.add("Description");
            fieldNames.add("ExternalLink");
            fieldNames.add("ChangedBy");
            fieldNames.add("LastEdit");
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
        if (name.equals("LinkIsCrossProject"))
        {
            return getLinkIsCrossProject();
        }
        if (name.equals("LinkPred"))
        {
            return getLinkPred();
        }
        if (name.equals("LinkSucc"))
        {
            return getLinkSucc();
        }
        if (name.equals("LinkType"))
        {
            return getLinkType();
        }
        if (name.equals("LinkDirection"))
        {
            return getLinkDirection();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
        }
        if (name.equals("LinkLag"))
        {
            return getLinkLag();
        }
        if (name.equals("LinkLagFormat"))
        {
            return getLinkLagFormat();
        }
        if (name.equals("StringValue1"))
        {
            return getStringValue1();
        }
        if (name.equals("StringValue2"))
        {
            return getStringValue2();
        }
        if (name.equals("StringValue3"))
        {
            return getStringValue3();
        }
        if (name.equals("IntegerValue1"))
        {
            return getIntegerValue1();
        }
        if (name.equals("IntegerValue2"))
        {
            return getIntegerValue2();
        }
        if (name.equals("IntegerValue3"))
        {
            return getIntegerValue3();
        }
        if (name.equals("DATEVALUE"))
        {
            return getDATEVALUE();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("ExternalLink"))
        {
            return getExternalLink();
        }
        if (name.equals("ChangedBy"))
        {
            return getChangedBy();
        }
        if (name.equals("LastEdit"))
        {
            return getLastEdit();
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
        if (name.equals("LinkIsCrossProject"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLinkIsCrossProject((String) value);
            return true;
        }
        if (name.equals("LinkPred"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLinkPred((Integer) value);
            return true;
        }
        if (name.equals("LinkSucc"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLinkSucc((Integer) value);
            return true;
        }
        if (name.equals("LinkType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLinkType((Integer) value);
            return true;
        }
        if (name.equals("LinkDirection"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLinkDirection((Integer) value);
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
        if (name.equals("LinkLag"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLinkLag((Double) value);
            return true;
        }
        if (name.equals("LinkLagFormat"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLinkLagFormat((Integer) value);
            return true;
        }
        if (name.equals("StringValue1"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStringValue1((String) value);
            return true;
        }
        if (name.equals("StringValue2"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStringValue2((String) value);
            return true;
        }
        if (name.equals("StringValue3"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStringValue3((String) value);
            return true;
        }
        if (name.equals("IntegerValue1"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIntegerValue1((Integer) value);
            return true;
        }
        if (name.equals("IntegerValue2"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIntegerValue2((Integer) value);
            return true;
        }
        if (name.equals("IntegerValue3"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIntegerValue3((Integer) value);
            return true;
        }
        if (name.equals("DATEVALUE"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDATEVALUE((Date) value);
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
        if (name.equals("ExternalLink"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setExternalLink((String) value);
            return true;
        }
        if (name.equals("ChangedBy"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setChangedBy((Integer) value);
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
        if (name.equals(TWorkItemLinkPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TWorkItemLinkPeer.LINKISCROSSPROJECT))
        {
            return getLinkIsCrossProject();
        }
        if (name.equals(TWorkItemLinkPeer.LINKPRED))
        {
            return getLinkPred();
        }
        if (name.equals(TWorkItemLinkPeer.LINKSUCC))
        {
            return getLinkSucc();
        }
        if (name.equals(TWorkItemLinkPeer.LINKTYPE))
        {
            return getLinkType();
        }
        if (name.equals(TWorkItemLinkPeer.LINKDIRECTION))
        {
            return getLinkDirection();
        }
        if (name.equals(TWorkItemLinkPeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TWorkItemLinkPeer.LINKLAG))
        {
            return getLinkLag();
        }
        if (name.equals(TWorkItemLinkPeer.LINKLAGFORMAT))
        {
            return getLinkLagFormat();
        }
        if (name.equals(TWorkItemLinkPeer.STRINGVALUE1))
        {
            return getStringValue1();
        }
        if (name.equals(TWorkItemLinkPeer.STRINGVALUE2))
        {
            return getStringValue2();
        }
        if (name.equals(TWorkItemLinkPeer.STRINGVALUE3))
        {
            return getStringValue3();
        }
        if (name.equals(TWorkItemLinkPeer.INTEGERVALUE1))
        {
            return getIntegerValue1();
        }
        if (name.equals(TWorkItemLinkPeer.INTEGERVALUE2))
        {
            return getIntegerValue2();
        }
        if (name.equals(TWorkItemLinkPeer.INTEGERVALUE3))
        {
            return getIntegerValue3();
        }
        if (name.equals(TWorkItemLinkPeer.DATEVALUE))
        {
            return getDATEVALUE();
        }
        if (name.equals(TWorkItemLinkPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TWorkItemLinkPeer.EXTERNALLINK))
        {
            return getExternalLink();
        }
        if (name.equals(TWorkItemLinkPeer.CHANGEDBY))
        {
            return getChangedBy();
        }
        if (name.equals(TWorkItemLinkPeer.LASTEDIT))
        {
            return getLastEdit();
        }
        if (name.equals(TWorkItemLinkPeer.TPUUID))
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
      if (TWorkItemLinkPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TWorkItemLinkPeer.LINKISCROSSPROJECT.equals(name))
        {
            return setByName("LinkIsCrossProject", value);
        }
      if (TWorkItemLinkPeer.LINKPRED.equals(name))
        {
            return setByName("LinkPred", value);
        }
      if (TWorkItemLinkPeer.LINKSUCC.equals(name))
        {
            return setByName("LinkSucc", value);
        }
      if (TWorkItemLinkPeer.LINKTYPE.equals(name))
        {
            return setByName("LinkType", value);
        }
      if (TWorkItemLinkPeer.LINKDIRECTION.equals(name))
        {
            return setByName("LinkDirection", value);
        }
      if (TWorkItemLinkPeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TWorkItemLinkPeer.LINKLAG.equals(name))
        {
            return setByName("LinkLag", value);
        }
      if (TWorkItemLinkPeer.LINKLAGFORMAT.equals(name))
        {
            return setByName("LinkLagFormat", value);
        }
      if (TWorkItemLinkPeer.STRINGVALUE1.equals(name))
        {
            return setByName("StringValue1", value);
        }
      if (TWorkItemLinkPeer.STRINGVALUE2.equals(name))
        {
            return setByName("StringValue2", value);
        }
      if (TWorkItemLinkPeer.STRINGVALUE3.equals(name))
        {
            return setByName("StringValue3", value);
        }
      if (TWorkItemLinkPeer.INTEGERVALUE1.equals(name))
        {
            return setByName("IntegerValue1", value);
        }
      if (TWorkItemLinkPeer.INTEGERVALUE2.equals(name))
        {
            return setByName("IntegerValue2", value);
        }
      if (TWorkItemLinkPeer.INTEGERVALUE3.equals(name))
        {
            return setByName("IntegerValue3", value);
        }
      if (TWorkItemLinkPeer.DATEVALUE.equals(name))
        {
            return setByName("DATEVALUE", value);
        }
      if (TWorkItemLinkPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TWorkItemLinkPeer.EXTERNALLINK.equals(name))
        {
            return setByName("ExternalLink", value);
        }
      if (TWorkItemLinkPeer.CHANGEDBY.equals(name))
        {
            return setByName("ChangedBy", value);
        }
      if (TWorkItemLinkPeer.LASTEDIT.equals(name))
        {
            return setByName("LastEdit", value);
        }
      if (TWorkItemLinkPeer.TPUUID.equals(name))
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
            return getLinkIsCrossProject();
        }
        if (pos == 2)
        {
            return getLinkPred();
        }
        if (pos == 3)
        {
            return getLinkSucc();
        }
        if (pos == 4)
        {
            return getLinkType();
        }
        if (pos == 5)
        {
            return getLinkDirection();
        }
        if (pos == 6)
        {
            return getSortorder();
        }
        if (pos == 7)
        {
            return getLinkLag();
        }
        if (pos == 8)
        {
            return getLinkLagFormat();
        }
        if (pos == 9)
        {
            return getStringValue1();
        }
        if (pos == 10)
        {
            return getStringValue2();
        }
        if (pos == 11)
        {
            return getStringValue3();
        }
        if (pos == 12)
        {
            return getIntegerValue1();
        }
        if (pos == 13)
        {
            return getIntegerValue2();
        }
        if (pos == 14)
        {
            return getIntegerValue3();
        }
        if (pos == 15)
        {
            return getDATEVALUE();
        }
        if (pos == 16)
        {
            return getDescription();
        }
        if (pos == 17)
        {
            return getExternalLink();
        }
        if (pos == 18)
        {
            return getChangedBy();
        }
        if (pos == 19)
        {
            return getLastEdit();
        }
        if (pos == 20)
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
            return setByName("LinkIsCrossProject", value);
        }
    if (position == 2)
        {
            return setByName("LinkPred", value);
        }
    if (position == 3)
        {
            return setByName("LinkSucc", value);
        }
    if (position == 4)
        {
            return setByName("LinkType", value);
        }
    if (position == 5)
        {
            return setByName("LinkDirection", value);
        }
    if (position == 6)
        {
            return setByName("Sortorder", value);
        }
    if (position == 7)
        {
            return setByName("LinkLag", value);
        }
    if (position == 8)
        {
            return setByName("LinkLagFormat", value);
        }
    if (position == 9)
        {
            return setByName("StringValue1", value);
        }
    if (position == 10)
        {
            return setByName("StringValue2", value);
        }
    if (position == 11)
        {
            return setByName("StringValue3", value);
        }
    if (position == 12)
        {
            return setByName("IntegerValue1", value);
        }
    if (position == 13)
        {
            return setByName("IntegerValue2", value);
        }
    if (position == 14)
        {
            return setByName("IntegerValue3", value);
        }
    if (position == 15)
        {
            return setByName("DATEVALUE", value);
        }
    if (position == 16)
        {
            return setByName("Description", value);
        }
    if (position == 17)
        {
            return setByName("ExternalLink", value);
        }
    if (position == 18)
        {
            return setByName("ChangedBy", value);
        }
    if (position == 19)
        {
            return setByName("LastEdit", value);
        }
    if (position == 20)
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
        save(TWorkItemLinkPeer.DATABASE_NAME);
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
                    TWorkItemLinkPeer.doInsert((TWorkItemLink) this, con);
                    setNew(false);
                }
                else
                {
                    TWorkItemLinkPeer.doUpdate((TWorkItemLink) this, con);
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
    public TWorkItemLink copy() throws TorqueException
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
    public TWorkItemLink copy(Connection con) throws TorqueException
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
    public TWorkItemLink copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TWorkItemLink(), deepcopy);
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
    public TWorkItemLink copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TWorkItemLink(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TWorkItemLink copyInto(TWorkItemLink copyObj) throws TorqueException
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
    protected TWorkItemLink copyInto(TWorkItemLink copyObj, Connection con) throws TorqueException
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
    protected TWorkItemLink copyInto(TWorkItemLink copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLinkIsCrossProject(linkIsCrossProject);
        copyObj.setLinkPred(linkPred);
        copyObj.setLinkSucc(linkSucc);
        copyObj.setLinkType(linkType);
        copyObj.setLinkDirection(linkDirection);
        copyObj.setSortorder(sortorder);
        copyObj.setLinkLag(linkLag);
        copyObj.setLinkLagFormat(linkLagFormat);
        copyObj.setStringValue1(stringValue1);
        copyObj.setStringValue2(stringValue2);
        copyObj.setStringValue3(stringValue3);
        copyObj.setIntegerValue1(integerValue1);
        copyObj.setIntegerValue2(integerValue2);
        copyObj.setIntegerValue3(integerValue3);
        copyObj.setDATEVALUE(dATEVALUE);
        copyObj.setDescription(description);
        copyObj.setExternalLink(externalLink);
        copyObj.setChangedBy(changedBy);
        copyObj.setLastEdit(lastEdit);
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
    protected TWorkItemLink copyInto(TWorkItemLink copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLinkIsCrossProject(linkIsCrossProject);
        copyObj.setLinkPred(linkPred);
        copyObj.setLinkSucc(linkSucc);
        copyObj.setLinkType(linkType);
        copyObj.setLinkDirection(linkDirection);
        copyObj.setSortorder(sortorder);
        copyObj.setLinkLag(linkLag);
        copyObj.setLinkLagFormat(linkLagFormat);
        copyObj.setStringValue1(stringValue1);
        copyObj.setStringValue2(stringValue2);
        copyObj.setStringValue3(stringValue3);
        copyObj.setIntegerValue1(integerValue1);
        copyObj.setIntegerValue2(integerValue2);
        copyObj.setIntegerValue3(integerValue3);
        copyObj.setDATEVALUE(dATEVALUE);
        copyObj.setDescription(description);
        copyObj.setExternalLink(externalLink);
        copyObj.setChangedBy(changedBy);
        copyObj.setLastEdit(lastEdit);
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
    public TWorkItemLinkPeer getPeer()
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
        return TWorkItemLinkPeer.getTableMap();
    }

  
    /**
     * Creates a TWorkItemLinkBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TWorkItemLinkBean with the contents of this object
     */
    public TWorkItemLinkBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TWorkItemLinkBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TWorkItemLinkBean with the contents of this object
     */
    public TWorkItemLinkBean getBean(IdentityMap createdBeans)
    {
        TWorkItemLinkBean result = (TWorkItemLinkBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TWorkItemLinkBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLinkIsCrossProject(getLinkIsCrossProject());
        result.setLinkPred(getLinkPred());
        result.setLinkSucc(getLinkSucc());
        result.setLinkType(getLinkType());
        result.setLinkDirection(getLinkDirection());
        result.setSortorder(getSortorder());
        result.setLinkLag(getLinkLag());
        result.setLinkLagFormat(getLinkLagFormat());
        result.setStringValue1(getStringValue1());
        result.setStringValue2(getStringValue2());
        result.setStringValue3(getStringValue3());
        result.setIntegerValue1(getIntegerValue1());
        result.setIntegerValue2(getIntegerValue2());
        result.setIntegerValue3(getIntegerValue3());
        result.setDATEVALUE(getDATEVALUE());
        result.setDescription(getDescription());
        result.setExternalLink(getExternalLink());
        result.setChangedBy(getChangedBy());
        result.setLastEdit(getLastEdit());
        result.setUuid(getUuid());





        if (aTWorkItemRelatedByLinkPred != null)
        {
            TWorkItemBean relatedBean = aTWorkItemRelatedByLinkPred.getBean(createdBeans);
            result.setTWorkItemBeanRelatedByLinkPred(relatedBean);
        }



        if (aTWorkItemRelatedByLinkSucc != null)
        {
            TWorkItemBean relatedBean = aTWorkItemRelatedByLinkSucc.getBean(createdBeans);
            result.setTWorkItemBeanRelatedByLinkSucc(relatedBean);
        }



        if (aTLinkType != null)
        {
            TLinkTypeBean relatedBean = aTLinkType.getBean(createdBeans);
            result.setTLinkTypeBean(relatedBean);
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
     * Creates an instance of TWorkItemLink with the contents
     * of a TWorkItemLinkBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TWorkItemLinkBean which contents are used to create
     *        the resulting class
     * @return an instance of TWorkItemLink with the contents of bean
     */
    public static TWorkItemLink createTWorkItemLink(TWorkItemLinkBean bean)
        throws TorqueException
    {
        return createTWorkItemLink(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TWorkItemLink with the contents
     * of a TWorkItemLinkBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TWorkItemLinkBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TWorkItemLink with the contents of bean
     */

    public static TWorkItemLink createTWorkItemLink(TWorkItemLinkBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TWorkItemLink result = (TWorkItemLink) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TWorkItemLink();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLinkIsCrossProject(bean.getLinkIsCrossProject());
        result.setLinkPred(bean.getLinkPred());
        result.setLinkSucc(bean.getLinkSucc());
        result.setLinkType(bean.getLinkType());
        result.setLinkDirection(bean.getLinkDirection());
        result.setSortorder(bean.getSortorder());
        result.setLinkLag(bean.getLinkLag());
        result.setLinkLagFormat(bean.getLinkLagFormat());
        result.setStringValue1(bean.getStringValue1());
        result.setStringValue2(bean.getStringValue2());
        result.setStringValue3(bean.getStringValue3());
        result.setIntegerValue1(bean.getIntegerValue1());
        result.setIntegerValue2(bean.getIntegerValue2());
        result.setIntegerValue3(bean.getIntegerValue3());
        result.setDATEVALUE(bean.getDATEVALUE());
        result.setDescription(bean.getDescription());
        result.setExternalLink(bean.getExternalLink());
        result.setChangedBy(bean.getChangedBy());
        result.setLastEdit(bean.getLastEdit());
        result.setUuid(bean.getUuid());





        {
            TWorkItemBean relatedBean = bean.getTWorkItemBeanRelatedByLinkPred();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItemRelatedByLinkPred(relatedObject);
            }
        }



        {
            TWorkItemBean relatedBean = bean.getTWorkItemBeanRelatedByLinkSucc();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItemRelatedByLinkSucc(relatedObject);
            }
        }



        {
            TLinkTypeBean relatedBean = bean.getTLinkTypeBean();
            if (relatedBean != null)
            {
                TLinkType relatedObject = TLinkType.createTLinkType(relatedBean, createdObjects);
                result.setTLinkType(relatedObject);
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
        str.append("TWorkItemLink:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("LinkIsCrossProject = ")
           .append(getLinkIsCrossProject())
           .append("\n");
        str.append("LinkPred = ")
           .append(getLinkPred())
           .append("\n");
        str.append("LinkSucc = ")
           .append(getLinkSucc())
           .append("\n");
        str.append("LinkType = ")
           .append(getLinkType())
           .append("\n");
        str.append("LinkDirection = ")
           .append(getLinkDirection())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("LinkLag = ")
           .append(getLinkLag())
           .append("\n");
        str.append("LinkLagFormat = ")
           .append(getLinkLagFormat())
           .append("\n");
        str.append("StringValue1 = ")
           .append(getStringValue1())
           .append("\n");
        str.append("StringValue2 = ")
           .append(getStringValue2())
           .append("\n");
        str.append("StringValue3 = ")
           .append(getStringValue3())
           .append("\n");
        str.append("IntegerValue1 = ")
           .append(getIntegerValue1())
           .append("\n");
        str.append("IntegerValue2 = ")
           .append(getIntegerValue2())
           .append("\n");
        str.append("IntegerValue3 = ")
           .append(getIntegerValue3())
           .append("\n");
        str.append("DATEVALUE = ")
           .append(getDATEVALUE())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("ExternalLink = ")
           .append(getExternalLink())
           .append("\n");
        str.append("ChangedBy = ")
           .append(getChangedBy())
           .append("\n");
        str.append("LastEdit = ")
           .append(getLastEdit())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
