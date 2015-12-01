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



import com.aurel.track.persist.TAttribute;
import com.aurel.track.persist.TAttributePeer;
import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.persist.TAttributeOption;
import com.aurel.track.persist.TAttributeOptionPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TIssueAttributeValueBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TAttributeBean;
import com.aurel.track.beans.TAttributeOptionBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TIssueAttributeValue
 */
public abstract class BaseTIssueAttributeValue extends TpBaseObject
{
    /** The Peer class */
    private static final TIssueAttributeValuePeer peer =
        new TIssueAttributeValuePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the attributeID field */
    private Integer attributeID;

    /** The value for the deleted field */
    private Integer deleted;

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the numericValue field */
    private Integer numericValue;

    /** The value for the timeStampValue field */
    private Date timeStampValue;

    /** The value for the longTextValue field */
    private String longTextValue;

    /** The value for the optionID field */
    private Integer optionID;

    /** The value for the person field */
    private Integer person;

    /** The value for the charValue field */
    private String charValue;

    /** The value for the displayValue field */
    private String displayValue;

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
     * Get the AttributeID
     *
     * @return Integer
     */
    public Integer getAttributeID()
    {
        return attributeID;
    }


    /**
     * Set the value of AttributeID
     *
     * @param v new value
     */
    public void setAttributeID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.attributeID, v))
        {
            this.attributeID = v;
            setModified(true);
        }


        if (aTAttribute != null && !ObjectUtils.equals(aTAttribute.getObjectID(), v))
        {
            aTAttribute = null;
        }

    }

    /**
     * Get the Deleted
     *
     * @return Integer
     */
    public Integer getDeleted()
    {
        return deleted;
    }


    /**
     * Set the value of Deleted
     *
     * @param v new value
     */
    public void setDeleted(Integer v) 
    {

        if (!ObjectUtils.equals(this.deleted, v))
        {
            this.deleted = v;
            setModified(true);
        }


    }

    /**
     * Get the WorkItem
     *
     * @return Integer
     */
    public Integer getWorkItem()
    {
        return workItem;
    }


    /**
     * Set the value of WorkItem
     *
     * @param v new value
     */
    public void setWorkItem(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workItem, v))
        {
            this.workItem = v;
            setModified(true);
        }


        if (aTWorkItem != null && !ObjectUtils.equals(aTWorkItem.getObjectID(), v))
        {
            aTWorkItem = null;
        }

    }

    /**
     * Get the NumericValue
     *
     * @return Integer
     */
    public Integer getNumericValue()
    {
        return numericValue;
    }


    /**
     * Set the value of NumericValue
     *
     * @param v new value
     */
    public void setNumericValue(Integer v) 
    {

        if (!ObjectUtils.equals(this.numericValue, v))
        {
            this.numericValue = v;
            setModified(true);
        }


    }

    /**
     * Get the TimeStampValue
     *
     * @return Date
     */
    public Date getTimeStampValue()
    {
        return timeStampValue;
    }


    /**
     * Set the value of TimeStampValue
     *
     * @param v new value
     */
    public void setTimeStampValue(Date v) 
    {

        if (!ObjectUtils.equals(this.timeStampValue, v))
        {
            this.timeStampValue = v;
            setModified(true);
        }


    }

    /**
     * Get the LongTextValue
     *
     * @return String
     */
    public String getLongTextValue()
    {
        return longTextValue;
    }


    /**
     * Set the value of LongTextValue
     *
     * @param v new value
     */
    public void setLongTextValue(String v) 
    {

        if (!ObjectUtils.equals(this.longTextValue, v))
        {
            this.longTextValue = v;
            setModified(true);
        }


    }

    /**
     * Get the OptionID
     *
     * @return Integer
     */
    public Integer getOptionID()
    {
        return optionID;
    }


    /**
     * Set the value of OptionID
     *
     * @param v new value
     */
    public void setOptionID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.optionID, v))
        {
            this.optionID = v;
            setModified(true);
        }


        if (aTAttributeOption != null && !ObjectUtils.equals(aTAttributeOption.getObjectID(), v))
        {
            aTAttributeOption = null;
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
     * Get the CharValue
     *
     * @return String
     */
    public String getCharValue()
    {
        return charValue;
    }


    /**
     * Set the value of CharValue
     *
     * @param v new value
     */
    public void setCharValue(String v) 
    {

        if (!ObjectUtils.equals(this.charValue, v))
        {
            this.charValue = v;
            setModified(true);
        }


    }

    /**
     * Get the DisplayValue
     *
     * @return String
     */
    public String getDisplayValue()
    {
        return displayValue;
    }


    /**
     * Set the value of DisplayValue
     *
     * @param v new value
     */
    public void setDisplayValue(String v) 
    {

        if (!ObjectUtils.equals(this.displayValue, v))
        {
            this.displayValue = v;
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




    private TWorkItem aTWorkItem;

    /**
     * Declares an association between this object and a TWorkItem object
     *
     * @param v TWorkItem
     * @throws TorqueException
     */
    public void setTWorkItem(TWorkItem v) throws TorqueException
    {
        if (v == null)
        {
            setWorkItem((Integer) null);
        }
        else
        {
            setWorkItem(v.getObjectID());
        }
        aTWorkItem = v;
    }


    /**
     * Returns the associated TWorkItem object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem()
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItem));
        }
        return aTWorkItem;
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
    public TWorkItem getTWorkItem(Connection connection)
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItem), connection);
        }
        return aTWorkItem;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkItemKey(ObjectKey key) throws TorqueException
    {

        setWorkItem(new Integer(((NumberKey) key).intValue()));
    }




    private TAttribute aTAttribute;

    /**
     * Declares an association between this object and a TAttribute object
     *
     * @param v TAttribute
     * @throws TorqueException
     */
    public void setTAttribute(TAttribute v) throws TorqueException
    {
        if (v == null)
        {
            setAttributeID((Integer) null);
        }
        else
        {
            setAttributeID(v.getObjectID());
        }
        aTAttribute = v;
    }


    /**
     * Returns the associated TAttribute object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TAttribute object
     * @throws TorqueException
     */
    public TAttribute getTAttribute()
        throws TorqueException
    {
        if (aTAttribute == null && (!ObjectUtils.equals(this.attributeID, null)))
        {
            aTAttribute = TAttributePeer.retrieveByPK(SimpleKey.keyFor(this.attributeID));
        }
        return aTAttribute;
    }

    /**
     * Return the associated TAttribute object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TAttribute object
     * @throws TorqueException
     */
    public TAttribute getTAttribute(Connection connection)
        throws TorqueException
    {
        if (aTAttribute == null && (!ObjectUtils.equals(this.attributeID, null)))
        {
            aTAttribute = TAttributePeer.retrieveByPK(SimpleKey.keyFor(this.attributeID), connection);
        }
        return aTAttribute;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTAttributeKey(ObjectKey key) throws TorqueException
    {

        setAttributeID(new Integer(((NumberKey) key).intValue()));
    }




    private TAttributeOption aTAttributeOption;

    /**
     * Declares an association between this object and a TAttributeOption object
     *
     * @param v TAttributeOption
     * @throws TorqueException
     */
    public void setTAttributeOption(TAttributeOption v) throws TorqueException
    {
        if (v == null)
        {
            setOptionID((Integer) null);
        }
        else
        {
            setOptionID(v.getObjectID());
        }
        aTAttributeOption = v;
    }


    /**
     * Returns the associated TAttributeOption object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TAttributeOption object
     * @throws TorqueException
     */
    public TAttributeOption getTAttributeOption()
        throws TorqueException
    {
        if (aTAttributeOption == null && (!ObjectUtils.equals(this.optionID, null)))
        {
            aTAttributeOption = TAttributeOptionPeer.retrieveByPK(SimpleKey.keyFor(this.optionID));
        }
        return aTAttributeOption;
    }

    /**
     * Return the associated TAttributeOption object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TAttributeOption object
     * @throws TorqueException
     */
    public TAttributeOption getTAttributeOption(Connection connection)
        throws TorqueException
    {
        if (aTAttributeOption == null && (!ObjectUtils.equals(this.optionID, null)))
        {
            aTAttributeOption = TAttributeOptionPeer.retrieveByPK(SimpleKey.keyFor(this.optionID), connection);
        }
        return aTAttributeOption;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTAttributeOptionKey(ObjectKey key) throws TorqueException
    {

        setOptionID(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("AttributeID");
            fieldNames.add("Deleted");
            fieldNames.add("WorkItem");
            fieldNames.add("NumericValue");
            fieldNames.add("TimeStampValue");
            fieldNames.add("LongTextValue");
            fieldNames.add("OptionID");
            fieldNames.add("Person");
            fieldNames.add("CharValue");
            fieldNames.add("DisplayValue");
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
        if (name.equals("AttributeID"))
        {
            return getAttributeID();
        }
        if (name.equals("Deleted"))
        {
            return getDeleted();
        }
        if (name.equals("WorkItem"))
        {
            return getWorkItem();
        }
        if (name.equals("NumericValue"))
        {
            return getNumericValue();
        }
        if (name.equals("TimeStampValue"))
        {
            return getTimeStampValue();
        }
        if (name.equals("LongTextValue"))
        {
            return getLongTextValue();
        }
        if (name.equals("OptionID"))
        {
            return getOptionID();
        }
        if (name.equals("Person"))
        {
            return getPerson();
        }
        if (name.equals("CharValue"))
        {
            return getCharValue();
        }
        if (name.equals("DisplayValue"))
        {
            return getDisplayValue();
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
        if (name.equals("AttributeID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAttributeID((Integer) value);
            return true;
        }
        if (name.equals("Deleted"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDeleted((Integer) value);
            return true;
        }
        if (name.equals("WorkItem"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkItem((Integer) value);
            return true;
        }
        if (name.equals("NumericValue"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNumericValue((Integer) value);
            return true;
        }
        if (name.equals("TimeStampValue"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTimeStampValue((Date) value);
            return true;
        }
        if (name.equals("LongTextValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLongTextValue((String) value);
            return true;
        }
        if (name.equals("OptionID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOptionID((Integer) value);
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
        if (name.equals("CharValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCharValue((String) value);
            return true;
        }
        if (name.equals("DisplayValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDisplayValue((String) value);
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
        if (name.equals(TIssueAttributeValuePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TIssueAttributeValuePeer.ATTRIBUTEID))
        {
            return getAttributeID();
        }
        if (name.equals(TIssueAttributeValuePeer.DELETED))
        {
            return getDeleted();
        }
        if (name.equals(TIssueAttributeValuePeer.ISSUE))
        {
            return getWorkItem();
        }
        if (name.equals(TIssueAttributeValuePeer.NUMERICVALUE))
        {
            return getNumericValue();
        }
        if (name.equals(TIssueAttributeValuePeer.TIMESTAMPVALUE))
        {
            return getTimeStampValue();
        }
        if (name.equals(TIssueAttributeValuePeer.LONGTEXTVALUE))
        {
            return getLongTextValue();
        }
        if (name.equals(TIssueAttributeValuePeer.OPTIONID))
        {
            return getOptionID();
        }
        if (name.equals(TIssueAttributeValuePeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TIssueAttributeValuePeer.CHARVALUE))
        {
            return getCharValue();
        }
        if (name.equals(TIssueAttributeValuePeer.DISPLAYVALUE))
        {
            return getDisplayValue();
        }
        if (name.equals(TIssueAttributeValuePeer.TPUUID))
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
      if (TIssueAttributeValuePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TIssueAttributeValuePeer.ATTRIBUTEID.equals(name))
        {
            return setByName("AttributeID", value);
        }
      if (TIssueAttributeValuePeer.DELETED.equals(name))
        {
            return setByName("Deleted", value);
        }
      if (TIssueAttributeValuePeer.ISSUE.equals(name))
        {
            return setByName("WorkItem", value);
        }
      if (TIssueAttributeValuePeer.NUMERICVALUE.equals(name))
        {
            return setByName("NumericValue", value);
        }
      if (TIssueAttributeValuePeer.TIMESTAMPVALUE.equals(name))
        {
            return setByName("TimeStampValue", value);
        }
      if (TIssueAttributeValuePeer.LONGTEXTVALUE.equals(name))
        {
            return setByName("LongTextValue", value);
        }
      if (TIssueAttributeValuePeer.OPTIONID.equals(name))
        {
            return setByName("OptionID", value);
        }
      if (TIssueAttributeValuePeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TIssueAttributeValuePeer.CHARVALUE.equals(name))
        {
            return setByName("CharValue", value);
        }
      if (TIssueAttributeValuePeer.DISPLAYVALUE.equals(name))
        {
            return setByName("DisplayValue", value);
        }
      if (TIssueAttributeValuePeer.TPUUID.equals(name))
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
            return getAttributeID();
        }
        if (pos == 2)
        {
            return getDeleted();
        }
        if (pos == 3)
        {
            return getWorkItem();
        }
        if (pos == 4)
        {
            return getNumericValue();
        }
        if (pos == 5)
        {
            return getTimeStampValue();
        }
        if (pos == 6)
        {
            return getLongTextValue();
        }
        if (pos == 7)
        {
            return getOptionID();
        }
        if (pos == 8)
        {
            return getPerson();
        }
        if (pos == 9)
        {
            return getCharValue();
        }
        if (pos == 10)
        {
            return getDisplayValue();
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
            return setByName("AttributeID", value);
        }
    if (position == 2)
        {
            return setByName("Deleted", value);
        }
    if (position == 3)
        {
            return setByName("WorkItem", value);
        }
    if (position == 4)
        {
            return setByName("NumericValue", value);
        }
    if (position == 5)
        {
            return setByName("TimeStampValue", value);
        }
    if (position == 6)
        {
            return setByName("LongTextValue", value);
        }
    if (position == 7)
        {
            return setByName("OptionID", value);
        }
    if (position == 8)
        {
            return setByName("Person", value);
        }
    if (position == 9)
        {
            return setByName("CharValue", value);
        }
    if (position == 10)
        {
            return setByName("DisplayValue", value);
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
        save(TIssueAttributeValuePeer.DATABASE_NAME);
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
                    TIssueAttributeValuePeer.doInsert((TIssueAttributeValue) this, con);
                    setNew(false);
                }
                else
                {
                    TIssueAttributeValuePeer.doUpdate((TIssueAttributeValue) this, con);
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
    public TIssueAttributeValue copy() throws TorqueException
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
    public TIssueAttributeValue copy(Connection con) throws TorqueException
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
    public TIssueAttributeValue copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TIssueAttributeValue(), deepcopy);
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
    public TIssueAttributeValue copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TIssueAttributeValue(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TIssueAttributeValue copyInto(TIssueAttributeValue copyObj) throws TorqueException
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
    protected TIssueAttributeValue copyInto(TIssueAttributeValue copyObj, Connection con) throws TorqueException
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
    protected TIssueAttributeValue copyInto(TIssueAttributeValue copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAttributeID(attributeID);
        copyObj.setDeleted(deleted);
        copyObj.setWorkItem(workItem);
        copyObj.setNumericValue(numericValue);
        copyObj.setTimeStampValue(timeStampValue);
        copyObj.setLongTextValue(longTextValue);
        copyObj.setOptionID(optionID);
        copyObj.setPerson(person);
        copyObj.setCharValue(charValue);
        copyObj.setDisplayValue(displayValue);
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
    protected TIssueAttributeValue copyInto(TIssueAttributeValue copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAttributeID(attributeID);
        copyObj.setDeleted(deleted);
        copyObj.setWorkItem(workItem);
        copyObj.setNumericValue(numericValue);
        copyObj.setTimeStampValue(timeStampValue);
        copyObj.setLongTextValue(longTextValue);
        copyObj.setOptionID(optionID);
        copyObj.setPerson(person);
        copyObj.setCharValue(charValue);
        copyObj.setDisplayValue(displayValue);
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
    public TIssueAttributeValuePeer getPeer()
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
        return TIssueAttributeValuePeer.getTableMap();
    }

  
    /**
     * Creates a TIssueAttributeValueBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TIssueAttributeValueBean with the contents of this object
     */
    public TIssueAttributeValueBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TIssueAttributeValueBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TIssueAttributeValueBean with the contents of this object
     */
    public TIssueAttributeValueBean getBean(IdentityMap createdBeans)
    {
        TIssueAttributeValueBean result = (TIssueAttributeValueBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TIssueAttributeValueBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setAttributeID(getAttributeID());
        result.setDeleted(getDeleted());
        result.setWorkItem(getWorkItem());
        result.setNumericValue(getNumericValue());
        result.setTimeStampValue(getTimeStampValue());
        result.setLongTextValue(getLongTextValue());
        result.setOptionID(getOptionID());
        result.setPerson(getPerson());
        result.setCharValue(getCharValue());
        result.setDisplayValue(getDisplayValue());
        result.setUuid(getUuid());





        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTWorkItem != null)
        {
            TWorkItemBean relatedBean = aTWorkItem.getBean(createdBeans);
            result.setTWorkItemBean(relatedBean);
        }



        if (aTAttribute != null)
        {
            TAttributeBean relatedBean = aTAttribute.getBean(createdBeans);
            result.setTAttributeBean(relatedBean);
        }



        if (aTAttributeOption != null)
        {
            TAttributeOptionBean relatedBean = aTAttributeOption.getBean(createdBeans);
            result.setTAttributeOptionBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TIssueAttributeValue with the contents
     * of a TIssueAttributeValueBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TIssueAttributeValueBean which contents are used to create
     *        the resulting class
     * @return an instance of TIssueAttributeValue with the contents of bean
     */
    public static TIssueAttributeValue createTIssueAttributeValue(TIssueAttributeValueBean bean)
        throws TorqueException
    {
        return createTIssueAttributeValue(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TIssueAttributeValue with the contents
     * of a TIssueAttributeValueBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TIssueAttributeValueBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TIssueAttributeValue with the contents of bean
     */

    public static TIssueAttributeValue createTIssueAttributeValue(TIssueAttributeValueBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TIssueAttributeValue result = (TIssueAttributeValue) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TIssueAttributeValue();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setAttributeID(bean.getAttributeID());
        result.setDeleted(bean.getDeleted());
        result.setWorkItem(bean.getWorkItem());
        result.setNumericValue(bean.getNumericValue());
        result.setTimeStampValue(bean.getTimeStampValue());
        result.setLongTextValue(bean.getLongTextValue());
        result.setOptionID(bean.getOptionID());
        result.setPerson(bean.getPerson());
        result.setCharValue(bean.getCharValue());
        result.setDisplayValue(bean.getDisplayValue());
        result.setUuid(bean.getUuid());





        {
            TPersonBean relatedBean = bean.getTPersonBean();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPerson(relatedObject);
            }
        }



        {
            TWorkItemBean relatedBean = bean.getTWorkItemBean();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItem(relatedObject);
            }
        }



        {
            TAttributeBean relatedBean = bean.getTAttributeBean();
            if (relatedBean != null)
            {
                TAttribute relatedObject = TAttribute.createTAttribute(relatedBean, createdObjects);
                result.setTAttribute(relatedObject);
            }
        }



        {
            TAttributeOptionBean relatedBean = bean.getTAttributeOptionBean();
            if (relatedBean != null)
            {
                TAttributeOption relatedObject = TAttributeOption.createTAttributeOption(relatedBean, createdObjects);
                result.setTAttributeOption(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TIssueAttributeValue:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("AttributeID = ")
           .append(getAttributeID())
           .append("\n");
        str.append("Deleted = ")
           .append(getDeleted())
           .append("\n");
        str.append("WorkItem = ")
           .append(getWorkItem())
           .append("\n");
        str.append("NumericValue = ")
           .append(getNumericValue())
           .append("\n");
        str.append("TimeStampValue = ")
           .append(getTimeStampValue())
           .append("\n");
        str.append("LongTextValue = ")
           .append(getLongTextValue())
           .append("\n");
        str.append("OptionID = ")
           .append(getOptionID())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("CharValue = ")
           .append(getCharValue())
           .append("\n");
        str.append("DisplayValue = ")
           .append(getDisplayValue())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
