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



import com.aurel.track.persist.TField;
import com.aurel.track.persist.TFieldPeer;
import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TWorkItemBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TAttributeValue
 */
public abstract class BaseTAttributeValue extends TpBaseObject
{
    /** The Peer class */
    private static final TAttributeValuePeer peer =
        new TAttributeValuePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the field field */
    private Integer field;

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the textValue field */
    private String textValue;

    /** The value for the integerValue field */
    private Integer integerValue;

    /** The value for the doubleValue field */
    private Double doubleValue;

    /** The value for the dateValue field */
    private Date dateValue;

    /** The value for the characterValue field */
    private String characterValue;

    /** The value for the longTextValue field */
    private String longTextValue;

    /** The value for the systemOptionID field */
    private Integer systemOptionID;

    /** The value for the systemOptionType field */
    private Integer systemOptionType;

    /** The value for the customOptionID field */
    private Integer customOptionID;

    /** The value for the parameterCode field */
    private Integer parameterCode;

    /** The value for the validValue field */
    private Integer validValue;

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
     * Get the TextValue
     *
     * @return String
     */
    public String getTextValue()
    {
        return textValue;
    }


    /**
     * Set the value of TextValue
     *
     * @param v new value
     */
    public void setTextValue(String v) 
    {

        if (!ObjectUtils.equals(this.textValue, v))
        {
            this.textValue = v;
            setModified(true);
        }


    }

    /**
     * Get the IntegerValue
     *
     * @return Integer
     */
    public Integer getIntegerValue()
    {
        return integerValue;
    }


    /**
     * Set the value of IntegerValue
     *
     * @param v new value
     */
    public void setIntegerValue(Integer v) 
    {

        if (!ObjectUtils.equals(this.integerValue, v))
        {
            this.integerValue = v;
            setModified(true);
        }


    }

    /**
     * Get the DoubleValue
     *
     * @return Double
     */
    public Double getDoubleValue()
    {
        return doubleValue;
    }


    /**
     * Set the value of DoubleValue
     *
     * @param v new value
     */
    public void setDoubleValue(Double v) 
    {

        if (!ObjectUtils.equals(this.doubleValue, v))
        {
            this.doubleValue = v;
            setModified(true);
        }


    }

    /**
     * Get the DateValue
     *
     * @return Date
     */
    public Date getDateValue()
    {
        return dateValue;
    }


    /**
     * Set the value of DateValue
     *
     * @param v new value
     */
    public void setDateValue(Date v) 
    {

        if (!ObjectUtils.equals(this.dateValue, v))
        {
            this.dateValue = v;
            setModified(true);
        }


    }

    /**
     * Get the CharacterValue
     *
     * @return String
     */
    public String getCharacterValue()
    {
        return characterValue;
    }


    /**
     * Set the value of CharacterValue
     *
     * @param v new value
     */
    public void setCharacterValue(String v) 
    {

        if (!ObjectUtils.equals(this.characterValue, v))
        {
            this.characterValue = v;
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
     * Get the SystemOptionID
     *
     * @return Integer
     */
    public Integer getSystemOptionID()
    {
        return systemOptionID;
    }


    /**
     * Set the value of SystemOptionID
     *
     * @param v new value
     */
    public void setSystemOptionID(Integer v) 
    {

        if (!ObjectUtils.equals(this.systemOptionID, v))
        {
            this.systemOptionID = v;
            setModified(true);
        }


    }

    /**
     * Get the SystemOptionType
     *
     * @return Integer
     */
    public Integer getSystemOptionType()
    {
        return systemOptionType;
    }


    /**
     * Set the value of SystemOptionType
     *
     * @param v new value
     */
    public void setSystemOptionType(Integer v) 
    {

        if (!ObjectUtils.equals(this.systemOptionType, v))
        {
            this.systemOptionType = v;
            setModified(true);
        }


    }

    /**
     * Get the CustomOptionID
     *
     * @return Integer
     */
    public Integer getCustomOptionID()
    {
        return customOptionID;
    }


    /**
     * Set the value of CustomOptionID
     *
     * @param v new value
     */
    public void setCustomOptionID(Integer v) 
    {

        if (!ObjectUtils.equals(this.customOptionID, v))
        {
            this.customOptionID = v;
            setModified(true);
        }


    }

    /**
     * Get the ParameterCode
     *
     * @return Integer
     */
    public Integer getParameterCode()
    {
        return parameterCode;
    }


    /**
     * Set the value of ParameterCode
     *
     * @param v new value
     */
    public void setParameterCode(Integer v) 
    {

        if (!ObjectUtils.equals(this.parameterCode, v))
        {
            this.parameterCode = v;
            setModified(true);
        }


    }

    /**
     * Get the ValidValue
     *
     * @return Integer
     */
    public Integer getValidValue()
    {
        return validValue;
    }


    /**
     * Set the value of ValidValue
     *
     * @param v new value
     */
    public void setValidValue(Integer v) 
    {

        if (!ObjectUtils.equals(this.validValue, v))
        {
            this.validValue = v;
            setModified(true);
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
            fieldNames.add("WorkItem");
            fieldNames.add("TextValue");
            fieldNames.add("IntegerValue");
            fieldNames.add("DoubleValue");
            fieldNames.add("DateValue");
            fieldNames.add("CharacterValue");
            fieldNames.add("LongTextValue");
            fieldNames.add("SystemOptionID");
            fieldNames.add("SystemOptionType");
            fieldNames.add("CustomOptionID");
            fieldNames.add("ParameterCode");
            fieldNames.add("ValidValue");
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
        if (name.equals("Field"))
        {
            return getField();
        }
        if (name.equals("WorkItem"))
        {
            return getWorkItem();
        }
        if (name.equals("TextValue"))
        {
            return getTextValue();
        }
        if (name.equals("IntegerValue"))
        {
            return getIntegerValue();
        }
        if (name.equals("DoubleValue"))
        {
            return getDoubleValue();
        }
        if (name.equals("DateValue"))
        {
            return getDateValue();
        }
        if (name.equals("CharacterValue"))
        {
            return getCharacterValue();
        }
        if (name.equals("LongTextValue"))
        {
            return getLongTextValue();
        }
        if (name.equals("SystemOptionID"))
        {
            return getSystemOptionID();
        }
        if (name.equals("SystemOptionType"))
        {
            return getSystemOptionType();
        }
        if (name.equals("CustomOptionID"))
        {
            return getCustomOptionID();
        }
        if (name.equals("ParameterCode"))
        {
            return getParameterCode();
        }
        if (name.equals("ValidValue"))
        {
            return getValidValue();
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
        if (name.equals("TextValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTextValue((String) value);
            return true;
        }
        if (name.equals("IntegerValue"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIntegerValue((Integer) value);
            return true;
        }
        if (name.equals("DoubleValue"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDoubleValue((Double) value);
            return true;
        }
        if (name.equals("DateValue"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDateValue((Date) value);
            return true;
        }
        if (name.equals("CharacterValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCharacterValue((String) value);
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
        if (name.equals("SystemOptionID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSystemOptionID((Integer) value);
            return true;
        }
        if (name.equals("SystemOptionType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSystemOptionType((Integer) value);
            return true;
        }
        if (name.equals("CustomOptionID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCustomOptionID((Integer) value);
            return true;
        }
        if (name.equals("ParameterCode"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParameterCode((Integer) value);
            return true;
        }
        if (name.equals("ValidValue"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setValidValue((Integer) value);
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
        if (name.equals(TAttributeValuePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TAttributeValuePeer.FIELDKEY))
        {
            return getField();
        }
        if (name.equals(TAttributeValuePeer.WORKITEM))
        {
            return getWorkItem();
        }
        if (name.equals(TAttributeValuePeer.TEXTVALUE))
        {
            return getTextValue();
        }
        if (name.equals(TAttributeValuePeer.INTEGERVALUE))
        {
            return getIntegerValue();
        }
        if (name.equals(TAttributeValuePeer.DOUBLEVALUE))
        {
            return getDoubleValue();
        }
        if (name.equals(TAttributeValuePeer.DATEVALUE))
        {
            return getDateValue();
        }
        if (name.equals(TAttributeValuePeer.CHARACTERVALUE))
        {
            return getCharacterValue();
        }
        if (name.equals(TAttributeValuePeer.LONGTEXTVALUE))
        {
            return getLongTextValue();
        }
        if (name.equals(TAttributeValuePeer.SYSTEMOPTIONID))
        {
            return getSystemOptionID();
        }
        if (name.equals(TAttributeValuePeer.SYSTEMOPTIONTYPE))
        {
            return getSystemOptionType();
        }
        if (name.equals(TAttributeValuePeer.CUSTOMOPTIONID))
        {
            return getCustomOptionID();
        }
        if (name.equals(TAttributeValuePeer.PARAMETERCODE))
        {
            return getParameterCode();
        }
        if (name.equals(TAttributeValuePeer.VALIDVALUE))
        {
            return getValidValue();
        }
        if (name.equals(TAttributeValuePeer.LASTEDIT))
        {
            return getLastEdit();
        }
        if (name.equals(TAttributeValuePeer.TPUUID))
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
      if (TAttributeValuePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TAttributeValuePeer.FIELDKEY.equals(name))
        {
            return setByName("Field", value);
        }
      if (TAttributeValuePeer.WORKITEM.equals(name))
        {
            return setByName("WorkItem", value);
        }
      if (TAttributeValuePeer.TEXTVALUE.equals(name))
        {
            return setByName("TextValue", value);
        }
      if (TAttributeValuePeer.INTEGERVALUE.equals(name))
        {
            return setByName("IntegerValue", value);
        }
      if (TAttributeValuePeer.DOUBLEVALUE.equals(name))
        {
            return setByName("DoubleValue", value);
        }
      if (TAttributeValuePeer.DATEVALUE.equals(name))
        {
            return setByName("DateValue", value);
        }
      if (TAttributeValuePeer.CHARACTERVALUE.equals(name))
        {
            return setByName("CharacterValue", value);
        }
      if (TAttributeValuePeer.LONGTEXTVALUE.equals(name))
        {
            return setByName("LongTextValue", value);
        }
      if (TAttributeValuePeer.SYSTEMOPTIONID.equals(name))
        {
            return setByName("SystemOptionID", value);
        }
      if (TAttributeValuePeer.SYSTEMOPTIONTYPE.equals(name))
        {
            return setByName("SystemOptionType", value);
        }
      if (TAttributeValuePeer.CUSTOMOPTIONID.equals(name))
        {
            return setByName("CustomOptionID", value);
        }
      if (TAttributeValuePeer.PARAMETERCODE.equals(name))
        {
            return setByName("ParameterCode", value);
        }
      if (TAttributeValuePeer.VALIDVALUE.equals(name))
        {
            return setByName("ValidValue", value);
        }
      if (TAttributeValuePeer.LASTEDIT.equals(name))
        {
            return setByName("LastEdit", value);
        }
      if (TAttributeValuePeer.TPUUID.equals(name))
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
            return getWorkItem();
        }
        if (pos == 3)
        {
            return getTextValue();
        }
        if (pos == 4)
        {
            return getIntegerValue();
        }
        if (pos == 5)
        {
            return getDoubleValue();
        }
        if (pos == 6)
        {
            return getDateValue();
        }
        if (pos == 7)
        {
            return getCharacterValue();
        }
        if (pos == 8)
        {
            return getLongTextValue();
        }
        if (pos == 9)
        {
            return getSystemOptionID();
        }
        if (pos == 10)
        {
            return getSystemOptionType();
        }
        if (pos == 11)
        {
            return getCustomOptionID();
        }
        if (pos == 12)
        {
            return getParameterCode();
        }
        if (pos == 13)
        {
            return getValidValue();
        }
        if (pos == 14)
        {
            return getLastEdit();
        }
        if (pos == 15)
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
            return setByName("WorkItem", value);
        }
    if (position == 3)
        {
            return setByName("TextValue", value);
        }
    if (position == 4)
        {
            return setByName("IntegerValue", value);
        }
    if (position == 5)
        {
            return setByName("DoubleValue", value);
        }
    if (position == 6)
        {
            return setByName("DateValue", value);
        }
    if (position == 7)
        {
            return setByName("CharacterValue", value);
        }
    if (position == 8)
        {
            return setByName("LongTextValue", value);
        }
    if (position == 9)
        {
            return setByName("SystemOptionID", value);
        }
    if (position == 10)
        {
            return setByName("SystemOptionType", value);
        }
    if (position == 11)
        {
            return setByName("CustomOptionID", value);
        }
    if (position == 12)
        {
            return setByName("ParameterCode", value);
        }
    if (position == 13)
        {
            return setByName("ValidValue", value);
        }
    if (position == 14)
        {
            return setByName("LastEdit", value);
        }
    if (position == 15)
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
        save(TAttributeValuePeer.DATABASE_NAME);
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
                    TAttributeValuePeer.doInsert((TAttributeValue) this, con);
                    setNew(false);
                }
                else
                {
                    TAttributeValuePeer.doUpdate((TAttributeValue) this, con);
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
    public TAttributeValue copy() throws TorqueException
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
    public TAttributeValue copy(Connection con) throws TorqueException
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
    public TAttributeValue copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TAttributeValue(), deepcopy);
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
    public TAttributeValue copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TAttributeValue(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TAttributeValue copyInto(TAttributeValue copyObj) throws TorqueException
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
    protected TAttributeValue copyInto(TAttributeValue copyObj, Connection con) throws TorqueException
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
    protected TAttributeValue copyInto(TAttributeValue copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setField(field);
        copyObj.setWorkItem(workItem);
        copyObj.setTextValue(textValue);
        copyObj.setIntegerValue(integerValue);
        copyObj.setDoubleValue(doubleValue);
        copyObj.setDateValue(dateValue);
        copyObj.setCharacterValue(characterValue);
        copyObj.setLongTextValue(longTextValue);
        copyObj.setSystemOptionID(systemOptionID);
        copyObj.setSystemOptionType(systemOptionType);
        copyObj.setCustomOptionID(customOptionID);
        copyObj.setParameterCode(parameterCode);
        copyObj.setValidValue(validValue);
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
    protected TAttributeValue copyInto(TAttributeValue copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setField(field);
        copyObj.setWorkItem(workItem);
        copyObj.setTextValue(textValue);
        copyObj.setIntegerValue(integerValue);
        copyObj.setDoubleValue(doubleValue);
        copyObj.setDateValue(dateValue);
        copyObj.setCharacterValue(characterValue);
        copyObj.setLongTextValue(longTextValue);
        copyObj.setSystemOptionID(systemOptionID);
        copyObj.setSystemOptionType(systemOptionType);
        copyObj.setCustomOptionID(customOptionID);
        copyObj.setParameterCode(parameterCode);
        copyObj.setValidValue(validValue);
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
    public TAttributeValuePeer getPeer()
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
        return TAttributeValuePeer.getTableMap();
    }

  
    /**
     * Creates a TAttributeValueBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TAttributeValueBean with the contents of this object
     */
    public TAttributeValueBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TAttributeValueBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TAttributeValueBean with the contents of this object
     */
    public TAttributeValueBean getBean(IdentityMap createdBeans)
    {
        TAttributeValueBean result = (TAttributeValueBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TAttributeValueBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setField(getField());
        result.setWorkItem(getWorkItem());
        result.setTextValue(getTextValue());
        result.setIntegerValue(getIntegerValue());
        result.setDoubleValue(getDoubleValue());
        result.setDateValue(getDateValue());
        result.setCharacterValue(getCharacterValue());
        result.setLongTextValue(getLongTextValue());
        result.setSystemOptionID(getSystemOptionID());
        result.setSystemOptionType(getSystemOptionType());
        result.setCustomOptionID(getCustomOptionID());
        result.setParameterCode(getParameterCode());
        result.setValidValue(getValidValue());
        result.setLastEdit(getLastEdit());
        result.setUuid(getUuid());





        if (aTField != null)
        {
            TFieldBean relatedBean = aTField.getBean(createdBeans);
            result.setTFieldBean(relatedBean);
        }



        if (aTWorkItem != null)
        {
            TWorkItemBean relatedBean = aTWorkItem.getBean(createdBeans);
            result.setTWorkItemBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TAttributeValue with the contents
     * of a TAttributeValueBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TAttributeValueBean which contents are used to create
     *        the resulting class
     * @return an instance of TAttributeValue with the contents of bean
     */
    public static TAttributeValue createTAttributeValue(TAttributeValueBean bean)
        throws TorqueException
    {
        return createTAttributeValue(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TAttributeValue with the contents
     * of a TAttributeValueBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TAttributeValueBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TAttributeValue with the contents of bean
     */

    public static TAttributeValue createTAttributeValue(TAttributeValueBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TAttributeValue result = (TAttributeValue) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TAttributeValue();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setField(bean.getField());
        result.setWorkItem(bean.getWorkItem());
        result.setTextValue(bean.getTextValue());
        result.setIntegerValue(bean.getIntegerValue());
        result.setDoubleValue(bean.getDoubleValue());
        result.setDateValue(bean.getDateValue());
        result.setCharacterValue(bean.getCharacterValue());
        result.setLongTextValue(bean.getLongTextValue());
        result.setSystemOptionID(bean.getSystemOptionID());
        result.setSystemOptionType(bean.getSystemOptionType());
        result.setCustomOptionID(bean.getCustomOptionID());
        result.setParameterCode(bean.getParameterCode());
        result.setValidValue(bean.getValidValue());
        result.setLastEdit(bean.getLastEdit());
        result.setUuid(bean.getUuid());





        {
            TFieldBean relatedBean = bean.getTFieldBean();
            if (relatedBean != null)
            {
                TField relatedObject = TField.createTField(relatedBean, createdObjects);
                result.setTField(relatedObject);
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
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TAttributeValue:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Field = ")
           .append(getField())
           .append("\n");
        str.append("WorkItem = ")
           .append(getWorkItem())
           .append("\n");
        str.append("TextValue = ")
           .append(getTextValue())
           .append("\n");
        str.append("IntegerValue = ")
           .append(getIntegerValue())
           .append("\n");
        str.append("DoubleValue = ")
           .append(getDoubleValue())
           .append("\n");
        str.append("DateValue = ")
           .append(getDateValue())
           .append("\n");
        str.append("CharacterValue = ")
           .append(getCharacterValue())
           .append("\n");
        str.append("LongTextValue = ")
           .append(getLongTextValue())
           .append("\n");
        str.append("SystemOptionID = ")
           .append(getSystemOptionID())
           .append("\n");
        str.append("SystemOptionType = ")
           .append(getSystemOptionType())
           .append("\n");
        str.append("CustomOptionID = ")
           .append(getCustomOptionID())
           .append("\n");
        str.append("ParameterCode = ")
           .append(getParameterCode())
           .append("\n");
        str.append("ValidValue = ")
           .append(getValidValue())
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
