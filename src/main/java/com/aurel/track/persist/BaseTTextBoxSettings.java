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



import com.aurel.track.persist.TFieldConfig;
import com.aurel.track.persist.TFieldConfigPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TFieldConfigBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TTextBoxSettings
 */
public abstract class BaseTTextBoxSettings extends TpBaseObject
{
    /** The Peer class */
    private static final TTextBoxSettingsPeer peer =
        new TTextBoxSettingsPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the config field */
    private Integer config;

    /** The value for the required field */
    private String required = "N";

    /** The value for the defaultText field */
    private String defaultText;

    /** The value for the defaultInteger field */
    private Integer defaultInteger;

    /** The value for the defaultDouble field */
    private Double defaultDouble;

    /** The value for the defaultDate field */
    private Date defaultDate;

    /** The value for the defaultChar field */
    private String defaultChar;

    /** The value for the dateIsWithTime field */
    private String dateIsWithTime = "N";

    /** The value for the defaultOption field */
    private Integer defaultOption;

    /** The value for the minOption field */
    private Integer minOption;

    /** The value for the maxOption field */
    private Integer maxOption;

    /** The value for the minTextLength field */
    private Integer minTextLength;

    /** The value for the maxTextLength field */
    private Integer maxTextLength;

    /** The value for the minDate field */
    private Date minDate;

    /** The value for the maxDate field */
    private Date maxDate;

    /** The value for the minInteger field */
    private Integer minInteger;

    /** The value for the maxInteger field */
    private Integer maxInteger;

    /** The value for the minDouble field */
    private Double minDouble;

    /** The value for the maxDouble field */
    private Double maxDouble;

    /** The value for the maxDecimalDigit field */
    private Integer maxDecimalDigit;

    /** The value for the parameterCode field */
    private Integer parameterCode;

    /** The value for the validValue field */
    private Integer validValue;

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
     * Get the Config
     *
     * @return Integer
     */
    public Integer getConfig()
    {
        return config;
    }


    /**
     * Set the value of Config
     *
     * @param v new value
     */
    public void setConfig(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.config, v))
        {
            this.config = v;
            setModified(true);
        }


        if (aTFieldConfig != null && !ObjectUtils.equals(aTFieldConfig.getObjectID(), v))
        {
            aTFieldConfig = null;
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
     * Get the DefaultText
     *
     * @return String
     */
    public String getDefaultText()
    {
        return defaultText;
    }


    /**
     * Set the value of DefaultText
     *
     * @param v new value
     */
    public void setDefaultText(String v) 
    {

        if (!ObjectUtils.equals(this.defaultText, v))
        {
            this.defaultText = v;
            setModified(true);
        }


    }

    /**
     * Get the DefaultInteger
     *
     * @return Integer
     */
    public Integer getDefaultInteger()
    {
        return defaultInteger;
    }


    /**
     * Set the value of DefaultInteger
     *
     * @param v new value
     */
    public void setDefaultInteger(Integer v) 
    {

        if (!ObjectUtils.equals(this.defaultInteger, v))
        {
            this.defaultInteger = v;
            setModified(true);
        }


    }

    /**
     * Get the DefaultDouble
     *
     * @return Double
     */
    public Double getDefaultDouble()
    {
        return defaultDouble;
    }


    /**
     * Set the value of DefaultDouble
     *
     * @param v new value
     */
    public void setDefaultDouble(Double v) 
    {

        if (!ObjectUtils.equals(this.defaultDouble, v))
        {
            this.defaultDouble = v;
            setModified(true);
        }


    }

    /**
     * Get the DefaultDate
     *
     * @return Date
     */
    public Date getDefaultDate()
    {
        return defaultDate;
    }


    /**
     * Set the value of DefaultDate
     *
     * @param v new value
     */
    public void setDefaultDate(Date v) 
    {

        if (!ObjectUtils.equals(this.defaultDate, v))
        {
            this.defaultDate = v;
            setModified(true);
        }


    }

    /**
     * Get the DefaultChar
     *
     * @return String
     */
    public String getDefaultChar()
    {
        return defaultChar;
    }


    /**
     * Set the value of DefaultChar
     *
     * @param v new value
     */
    public void setDefaultChar(String v) 
    {

        if (!ObjectUtils.equals(this.defaultChar, v))
        {
            this.defaultChar = v;
            setModified(true);
        }


    }

    /**
     * Get the DateIsWithTime
     *
     * @return String
     */
    public String getDateIsWithTime()
    {
        return dateIsWithTime;
    }


    /**
     * Set the value of DateIsWithTime
     *
     * @param v new value
     */
    public void setDateIsWithTime(String v) 
    {

        if (!ObjectUtils.equals(this.dateIsWithTime, v))
        {
            this.dateIsWithTime = v;
            setModified(true);
        }


    }

    /**
     * Get the DefaultOption
     *
     * @return Integer
     */
    public Integer getDefaultOption()
    {
        return defaultOption;
    }


    /**
     * Set the value of DefaultOption
     *
     * @param v new value
     */
    public void setDefaultOption(Integer v) 
    {

        if (!ObjectUtils.equals(this.defaultOption, v))
        {
            this.defaultOption = v;
            setModified(true);
        }


    }

    /**
     * Get the MinOption
     *
     * @return Integer
     */
    public Integer getMinOption()
    {
        return minOption;
    }


    /**
     * Set the value of MinOption
     *
     * @param v new value
     */
    public void setMinOption(Integer v) 
    {

        if (!ObjectUtils.equals(this.minOption, v))
        {
            this.minOption = v;
            setModified(true);
        }


    }

    /**
     * Get the MaxOption
     *
     * @return Integer
     */
    public Integer getMaxOption()
    {
        return maxOption;
    }


    /**
     * Set the value of MaxOption
     *
     * @param v new value
     */
    public void setMaxOption(Integer v) 
    {

        if (!ObjectUtils.equals(this.maxOption, v))
        {
            this.maxOption = v;
            setModified(true);
        }


    }

    /**
     * Get the MinTextLength
     *
     * @return Integer
     */
    public Integer getMinTextLength()
    {
        return minTextLength;
    }


    /**
     * Set the value of MinTextLength
     *
     * @param v new value
     */
    public void setMinTextLength(Integer v) 
    {

        if (!ObjectUtils.equals(this.minTextLength, v))
        {
            this.minTextLength = v;
            setModified(true);
        }


    }

    /**
     * Get the MaxTextLength
     *
     * @return Integer
     */
    public Integer getMaxTextLength()
    {
        return maxTextLength;
    }


    /**
     * Set the value of MaxTextLength
     *
     * @param v new value
     */
    public void setMaxTextLength(Integer v) 
    {

        if (!ObjectUtils.equals(this.maxTextLength, v))
        {
            this.maxTextLength = v;
            setModified(true);
        }


    }

    /**
     * Get the MinDate
     *
     * @return Date
     */
    public Date getMinDate()
    {
        return minDate;
    }


    /**
     * Set the value of MinDate
     *
     * @param v new value
     */
    public void setMinDate(Date v) 
    {

        if (!ObjectUtils.equals(this.minDate, v))
        {
            this.minDate = v;
            setModified(true);
        }


    }

    /**
     * Get the MaxDate
     *
     * @return Date
     */
    public Date getMaxDate()
    {
        return maxDate;
    }


    /**
     * Set the value of MaxDate
     *
     * @param v new value
     */
    public void setMaxDate(Date v) 
    {

        if (!ObjectUtils.equals(this.maxDate, v))
        {
            this.maxDate = v;
            setModified(true);
        }


    }

    /**
     * Get the MinInteger
     *
     * @return Integer
     */
    public Integer getMinInteger()
    {
        return minInteger;
    }


    /**
     * Set the value of MinInteger
     *
     * @param v new value
     */
    public void setMinInteger(Integer v) 
    {

        if (!ObjectUtils.equals(this.minInteger, v))
        {
            this.minInteger = v;
            setModified(true);
        }


    }

    /**
     * Get the MaxInteger
     *
     * @return Integer
     */
    public Integer getMaxInteger()
    {
        return maxInteger;
    }


    /**
     * Set the value of MaxInteger
     *
     * @param v new value
     */
    public void setMaxInteger(Integer v) 
    {

        if (!ObjectUtils.equals(this.maxInteger, v))
        {
            this.maxInteger = v;
            setModified(true);
        }


    }

    /**
     * Get the MinDouble
     *
     * @return Double
     */
    public Double getMinDouble()
    {
        return minDouble;
    }


    /**
     * Set the value of MinDouble
     *
     * @param v new value
     */
    public void setMinDouble(Double v) 
    {

        if (!ObjectUtils.equals(this.minDouble, v))
        {
            this.minDouble = v;
            setModified(true);
        }


    }

    /**
     * Get the MaxDouble
     *
     * @return Double
     */
    public Double getMaxDouble()
    {
        return maxDouble;
    }


    /**
     * Set the value of MaxDouble
     *
     * @param v new value
     */
    public void setMaxDouble(Double v) 
    {

        if (!ObjectUtils.equals(this.maxDouble, v))
        {
            this.maxDouble = v;
            setModified(true);
        }


    }

    /**
     * Get the MaxDecimalDigit
     *
     * @return Integer
     */
    public Integer getMaxDecimalDigit()
    {
        return maxDecimalDigit;
    }


    /**
     * Set the value of MaxDecimalDigit
     *
     * @param v new value
     */
    public void setMaxDecimalDigit(Integer v) 
    {

        if (!ObjectUtils.equals(this.maxDecimalDigit, v))
        {
            this.maxDecimalDigit = v;
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

    



    private TFieldConfig aTFieldConfig;

    /**
     * Declares an association between this object and a TFieldConfig object
     *
     * @param v TFieldConfig
     * @throws TorqueException
     */
    public void setTFieldConfig(TFieldConfig v) throws TorqueException
    {
        if (v == null)
        {
            setConfig((Integer) null);
        }
        else
        {
            setConfig(v.getObjectID());
        }
        aTFieldConfig = v;
    }


    /**
     * Returns the associated TFieldConfig object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TFieldConfig object
     * @throws TorqueException
     */
    public TFieldConfig getTFieldConfig()
        throws TorqueException
    {
        if (aTFieldConfig == null && (!ObjectUtils.equals(this.config, null)))
        {
            aTFieldConfig = TFieldConfigPeer.retrieveByPK(SimpleKey.keyFor(this.config));
        }
        return aTFieldConfig;
    }

    /**
     * Return the associated TFieldConfig object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TFieldConfig object
     * @throws TorqueException
     */
    public TFieldConfig getTFieldConfig(Connection connection)
        throws TorqueException
    {
        if (aTFieldConfig == null && (!ObjectUtils.equals(this.config, null)))
        {
            aTFieldConfig = TFieldConfigPeer.retrieveByPK(SimpleKey.keyFor(this.config), connection);
        }
        return aTFieldConfig;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTFieldConfigKey(ObjectKey key) throws TorqueException
    {

        setConfig(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Config");
            fieldNames.add("Required");
            fieldNames.add("DefaultText");
            fieldNames.add("DefaultInteger");
            fieldNames.add("DefaultDouble");
            fieldNames.add("DefaultDate");
            fieldNames.add("DefaultChar");
            fieldNames.add("DateIsWithTime");
            fieldNames.add("DefaultOption");
            fieldNames.add("MinOption");
            fieldNames.add("MaxOption");
            fieldNames.add("MinTextLength");
            fieldNames.add("MaxTextLength");
            fieldNames.add("MinDate");
            fieldNames.add("MaxDate");
            fieldNames.add("MinInteger");
            fieldNames.add("MaxInteger");
            fieldNames.add("MinDouble");
            fieldNames.add("MaxDouble");
            fieldNames.add("MaxDecimalDigit");
            fieldNames.add("ParameterCode");
            fieldNames.add("ValidValue");
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
        if (name.equals("Config"))
        {
            return getConfig();
        }
        if (name.equals("Required"))
        {
            return getRequired();
        }
        if (name.equals("DefaultText"))
        {
            return getDefaultText();
        }
        if (name.equals("DefaultInteger"))
        {
            return getDefaultInteger();
        }
        if (name.equals("DefaultDouble"))
        {
            return getDefaultDouble();
        }
        if (name.equals("DefaultDate"))
        {
            return getDefaultDate();
        }
        if (name.equals("DefaultChar"))
        {
            return getDefaultChar();
        }
        if (name.equals("DateIsWithTime"))
        {
            return getDateIsWithTime();
        }
        if (name.equals("DefaultOption"))
        {
            return getDefaultOption();
        }
        if (name.equals("MinOption"))
        {
            return getMinOption();
        }
        if (name.equals("MaxOption"))
        {
            return getMaxOption();
        }
        if (name.equals("MinTextLength"))
        {
            return getMinTextLength();
        }
        if (name.equals("MaxTextLength"))
        {
            return getMaxTextLength();
        }
        if (name.equals("MinDate"))
        {
            return getMinDate();
        }
        if (name.equals("MaxDate"))
        {
            return getMaxDate();
        }
        if (name.equals("MinInteger"))
        {
            return getMinInteger();
        }
        if (name.equals("MaxInteger"))
        {
            return getMaxInteger();
        }
        if (name.equals("MinDouble"))
        {
            return getMinDouble();
        }
        if (name.equals("MaxDouble"))
        {
            return getMaxDouble();
        }
        if (name.equals("MaxDecimalDigit"))
        {
            return getMaxDecimalDigit();
        }
        if (name.equals("ParameterCode"))
        {
            return getParameterCode();
        }
        if (name.equals("ValidValue"))
        {
            return getValidValue();
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
        if (name.equals("Config"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setConfig((Integer) value);
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
        if (name.equals("DefaultText"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultText((String) value);
            return true;
        }
        if (name.equals("DefaultInteger"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultInteger((Integer) value);
            return true;
        }
        if (name.equals("DefaultDouble"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultDouble((Double) value);
            return true;
        }
        if (name.equals("DefaultDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultDate((Date) value);
            return true;
        }
        if (name.equals("DefaultChar"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultChar((String) value);
            return true;
        }
        if (name.equals("DateIsWithTime"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDateIsWithTime((String) value);
            return true;
        }
        if (name.equals("DefaultOption"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultOption((Integer) value);
            return true;
        }
        if (name.equals("MinOption"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMinOption((Integer) value);
            return true;
        }
        if (name.equals("MaxOption"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMaxOption((Integer) value);
            return true;
        }
        if (name.equals("MinTextLength"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMinTextLength((Integer) value);
            return true;
        }
        if (name.equals("MaxTextLength"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMaxTextLength((Integer) value);
            return true;
        }
        if (name.equals("MinDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMinDate((Date) value);
            return true;
        }
        if (name.equals("MaxDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMaxDate((Date) value);
            return true;
        }
        if (name.equals("MinInteger"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMinInteger((Integer) value);
            return true;
        }
        if (name.equals("MaxInteger"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMaxInteger((Integer) value);
            return true;
        }
        if (name.equals("MinDouble"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMinDouble((Double) value);
            return true;
        }
        if (name.equals("MaxDouble"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMaxDouble((Double) value);
            return true;
        }
        if (name.equals("MaxDecimalDigit"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMaxDecimalDigit((Integer) value);
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
        if (name.equals(TTextBoxSettingsPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TTextBoxSettingsPeer.CONFIG))
        {
            return getConfig();
        }
        if (name.equals(TTextBoxSettingsPeer.REQUIRED))
        {
            return getRequired();
        }
        if (name.equals(TTextBoxSettingsPeer.DEFAULTTEXT))
        {
            return getDefaultText();
        }
        if (name.equals(TTextBoxSettingsPeer.DEFAULTINTEGER))
        {
            return getDefaultInteger();
        }
        if (name.equals(TTextBoxSettingsPeer.DEFAULTDOUBLE))
        {
            return getDefaultDouble();
        }
        if (name.equals(TTextBoxSettingsPeer.DEFAULTDATE))
        {
            return getDefaultDate();
        }
        if (name.equals(TTextBoxSettingsPeer.DEFAULTCHAR))
        {
            return getDefaultChar();
        }
        if (name.equals(TTextBoxSettingsPeer.DATEISWITHTIME))
        {
            return getDateIsWithTime();
        }
        if (name.equals(TTextBoxSettingsPeer.DEFAULTOPTION))
        {
            return getDefaultOption();
        }
        if (name.equals(TTextBoxSettingsPeer.MINOPTION))
        {
            return getMinOption();
        }
        if (name.equals(TTextBoxSettingsPeer.MAXOPTION))
        {
            return getMaxOption();
        }
        if (name.equals(TTextBoxSettingsPeer.MINTEXTLENGTH))
        {
            return getMinTextLength();
        }
        if (name.equals(TTextBoxSettingsPeer.MAXTEXTLENGTH))
        {
            return getMaxTextLength();
        }
        if (name.equals(TTextBoxSettingsPeer.MINDATE))
        {
            return getMinDate();
        }
        if (name.equals(TTextBoxSettingsPeer.MAXDATE))
        {
            return getMaxDate();
        }
        if (name.equals(TTextBoxSettingsPeer.MININTEGER))
        {
            return getMinInteger();
        }
        if (name.equals(TTextBoxSettingsPeer.MAXINTEGER))
        {
            return getMaxInteger();
        }
        if (name.equals(TTextBoxSettingsPeer.MINDOUBLE))
        {
            return getMinDouble();
        }
        if (name.equals(TTextBoxSettingsPeer.MAXDOUBLE))
        {
            return getMaxDouble();
        }
        if (name.equals(TTextBoxSettingsPeer.MAXDECIMALDIGIT))
        {
            return getMaxDecimalDigit();
        }
        if (name.equals(TTextBoxSettingsPeer.PARAMETERCODE))
        {
            return getParameterCode();
        }
        if (name.equals(TTextBoxSettingsPeer.VALIDVALUE))
        {
            return getValidValue();
        }
        if (name.equals(TTextBoxSettingsPeer.TPUUID))
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
      if (TTextBoxSettingsPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TTextBoxSettingsPeer.CONFIG.equals(name))
        {
            return setByName("Config", value);
        }
      if (TTextBoxSettingsPeer.REQUIRED.equals(name))
        {
            return setByName("Required", value);
        }
      if (TTextBoxSettingsPeer.DEFAULTTEXT.equals(name))
        {
            return setByName("DefaultText", value);
        }
      if (TTextBoxSettingsPeer.DEFAULTINTEGER.equals(name))
        {
            return setByName("DefaultInteger", value);
        }
      if (TTextBoxSettingsPeer.DEFAULTDOUBLE.equals(name))
        {
            return setByName("DefaultDouble", value);
        }
      if (TTextBoxSettingsPeer.DEFAULTDATE.equals(name))
        {
            return setByName("DefaultDate", value);
        }
      if (TTextBoxSettingsPeer.DEFAULTCHAR.equals(name))
        {
            return setByName("DefaultChar", value);
        }
      if (TTextBoxSettingsPeer.DATEISWITHTIME.equals(name))
        {
            return setByName("DateIsWithTime", value);
        }
      if (TTextBoxSettingsPeer.DEFAULTOPTION.equals(name))
        {
            return setByName("DefaultOption", value);
        }
      if (TTextBoxSettingsPeer.MINOPTION.equals(name))
        {
            return setByName("MinOption", value);
        }
      if (TTextBoxSettingsPeer.MAXOPTION.equals(name))
        {
            return setByName("MaxOption", value);
        }
      if (TTextBoxSettingsPeer.MINTEXTLENGTH.equals(name))
        {
            return setByName("MinTextLength", value);
        }
      if (TTextBoxSettingsPeer.MAXTEXTLENGTH.equals(name))
        {
            return setByName("MaxTextLength", value);
        }
      if (TTextBoxSettingsPeer.MINDATE.equals(name))
        {
            return setByName("MinDate", value);
        }
      if (TTextBoxSettingsPeer.MAXDATE.equals(name))
        {
            return setByName("MaxDate", value);
        }
      if (TTextBoxSettingsPeer.MININTEGER.equals(name))
        {
            return setByName("MinInteger", value);
        }
      if (TTextBoxSettingsPeer.MAXINTEGER.equals(name))
        {
            return setByName("MaxInteger", value);
        }
      if (TTextBoxSettingsPeer.MINDOUBLE.equals(name))
        {
            return setByName("MinDouble", value);
        }
      if (TTextBoxSettingsPeer.MAXDOUBLE.equals(name))
        {
            return setByName("MaxDouble", value);
        }
      if (TTextBoxSettingsPeer.MAXDECIMALDIGIT.equals(name))
        {
            return setByName("MaxDecimalDigit", value);
        }
      if (TTextBoxSettingsPeer.PARAMETERCODE.equals(name))
        {
            return setByName("ParameterCode", value);
        }
      if (TTextBoxSettingsPeer.VALIDVALUE.equals(name))
        {
            return setByName("ValidValue", value);
        }
      if (TTextBoxSettingsPeer.TPUUID.equals(name))
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
            return getConfig();
        }
        if (pos == 2)
        {
            return getRequired();
        }
        if (pos == 3)
        {
            return getDefaultText();
        }
        if (pos == 4)
        {
            return getDefaultInteger();
        }
        if (pos == 5)
        {
            return getDefaultDouble();
        }
        if (pos == 6)
        {
            return getDefaultDate();
        }
        if (pos == 7)
        {
            return getDefaultChar();
        }
        if (pos == 8)
        {
            return getDateIsWithTime();
        }
        if (pos == 9)
        {
            return getDefaultOption();
        }
        if (pos == 10)
        {
            return getMinOption();
        }
        if (pos == 11)
        {
            return getMaxOption();
        }
        if (pos == 12)
        {
            return getMinTextLength();
        }
        if (pos == 13)
        {
            return getMaxTextLength();
        }
        if (pos == 14)
        {
            return getMinDate();
        }
        if (pos == 15)
        {
            return getMaxDate();
        }
        if (pos == 16)
        {
            return getMinInteger();
        }
        if (pos == 17)
        {
            return getMaxInteger();
        }
        if (pos == 18)
        {
            return getMinDouble();
        }
        if (pos == 19)
        {
            return getMaxDouble();
        }
        if (pos == 20)
        {
            return getMaxDecimalDigit();
        }
        if (pos == 21)
        {
            return getParameterCode();
        }
        if (pos == 22)
        {
            return getValidValue();
        }
        if (pos == 23)
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
            return setByName("Config", value);
        }
    if (position == 2)
        {
            return setByName("Required", value);
        }
    if (position == 3)
        {
            return setByName("DefaultText", value);
        }
    if (position == 4)
        {
            return setByName("DefaultInteger", value);
        }
    if (position == 5)
        {
            return setByName("DefaultDouble", value);
        }
    if (position == 6)
        {
            return setByName("DefaultDate", value);
        }
    if (position == 7)
        {
            return setByName("DefaultChar", value);
        }
    if (position == 8)
        {
            return setByName("DateIsWithTime", value);
        }
    if (position == 9)
        {
            return setByName("DefaultOption", value);
        }
    if (position == 10)
        {
            return setByName("MinOption", value);
        }
    if (position == 11)
        {
            return setByName("MaxOption", value);
        }
    if (position == 12)
        {
            return setByName("MinTextLength", value);
        }
    if (position == 13)
        {
            return setByName("MaxTextLength", value);
        }
    if (position == 14)
        {
            return setByName("MinDate", value);
        }
    if (position == 15)
        {
            return setByName("MaxDate", value);
        }
    if (position == 16)
        {
            return setByName("MinInteger", value);
        }
    if (position == 17)
        {
            return setByName("MaxInteger", value);
        }
    if (position == 18)
        {
            return setByName("MinDouble", value);
        }
    if (position == 19)
        {
            return setByName("MaxDouble", value);
        }
    if (position == 20)
        {
            return setByName("MaxDecimalDigit", value);
        }
    if (position == 21)
        {
            return setByName("ParameterCode", value);
        }
    if (position == 22)
        {
            return setByName("ValidValue", value);
        }
    if (position == 23)
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
        save(TTextBoxSettingsPeer.DATABASE_NAME);
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
                    TTextBoxSettingsPeer.doInsert((TTextBoxSettings) this, con);
                    setNew(false);
                }
                else
                {
                    TTextBoxSettingsPeer.doUpdate((TTextBoxSettings) this, con);
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
    public TTextBoxSettings copy() throws TorqueException
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
    public TTextBoxSettings copy(Connection con) throws TorqueException
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
    public TTextBoxSettings copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TTextBoxSettings(), deepcopy);
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
    public TTextBoxSettings copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TTextBoxSettings(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TTextBoxSettings copyInto(TTextBoxSettings copyObj) throws TorqueException
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
    protected TTextBoxSettings copyInto(TTextBoxSettings copyObj, Connection con) throws TorqueException
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
    protected TTextBoxSettings copyInto(TTextBoxSettings copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setConfig(config);
        copyObj.setRequired(required);
        copyObj.setDefaultText(defaultText);
        copyObj.setDefaultInteger(defaultInteger);
        copyObj.setDefaultDouble(defaultDouble);
        copyObj.setDefaultDate(defaultDate);
        copyObj.setDefaultChar(defaultChar);
        copyObj.setDateIsWithTime(dateIsWithTime);
        copyObj.setDefaultOption(defaultOption);
        copyObj.setMinOption(minOption);
        copyObj.setMaxOption(maxOption);
        copyObj.setMinTextLength(minTextLength);
        copyObj.setMaxTextLength(maxTextLength);
        copyObj.setMinDate(minDate);
        copyObj.setMaxDate(maxDate);
        copyObj.setMinInteger(minInteger);
        copyObj.setMaxInteger(maxInteger);
        copyObj.setMinDouble(minDouble);
        copyObj.setMaxDouble(maxDouble);
        copyObj.setMaxDecimalDigit(maxDecimalDigit);
        copyObj.setParameterCode(parameterCode);
        copyObj.setValidValue(validValue);
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
    protected TTextBoxSettings copyInto(TTextBoxSettings copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setConfig(config);
        copyObj.setRequired(required);
        copyObj.setDefaultText(defaultText);
        copyObj.setDefaultInteger(defaultInteger);
        copyObj.setDefaultDouble(defaultDouble);
        copyObj.setDefaultDate(defaultDate);
        copyObj.setDefaultChar(defaultChar);
        copyObj.setDateIsWithTime(dateIsWithTime);
        copyObj.setDefaultOption(defaultOption);
        copyObj.setMinOption(minOption);
        copyObj.setMaxOption(maxOption);
        copyObj.setMinTextLength(minTextLength);
        copyObj.setMaxTextLength(maxTextLength);
        copyObj.setMinDate(minDate);
        copyObj.setMaxDate(maxDate);
        copyObj.setMinInteger(minInteger);
        copyObj.setMaxInteger(maxInteger);
        copyObj.setMinDouble(minDouble);
        copyObj.setMaxDouble(maxDouble);
        copyObj.setMaxDecimalDigit(maxDecimalDigit);
        copyObj.setParameterCode(parameterCode);
        copyObj.setValidValue(validValue);
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
    public TTextBoxSettingsPeer getPeer()
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
        return TTextBoxSettingsPeer.getTableMap();
    }

  
    /**
     * Creates a TTextBoxSettingsBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TTextBoxSettingsBean with the contents of this object
     */
    public TTextBoxSettingsBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TTextBoxSettingsBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TTextBoxSettingsBean with the contents of this object
     */
    public TTextBoxSettingsBean getBean(IdentityMap createdBeans)
    {
        TTextBoxSettingsBean result = (TTextBoxSettingsBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TTextBoxSettingsBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setConfig(getConfig());
        result.setRequired(getRequired());
        result.setDefaultText(getDefaultText());
        result.setDefaultInteger(getDefaultInteger());
        result.setDefaultDouble(getDefaultDouble());
        result.setDefaultDate(getDefaultDate());
        result.setDefaultChar(getDefaultChar());
        result.setDateIsWithTime(getDateIsWithTime());
        result.setDefaultOption(getDefaultOption());
        result.setMinOption(getMinOption());
        result.setMaxOption(getMaxOption());
        result.setMinTextLength(getMinTextLength());
        result.setMaxTextLength(getMaxTextLength());
        result.setMinDate(getMinDate());
        result.setMaxDate(getMaxDate());
        result.setMinInteger(getMinInteger());
        result.setMaxInteger(getMaxInteger());
        result.setMinDouble(getMinDouble());
        result.setMaxDouble(getMaxDouble());
        result.setMaxDecimalDigit(getMaxDecimalDigit());
        result.setParameterCode(getParameterCode());
        result.setValidValue(getValidValue());
        result.setUuid(getUuid());





        if (aTFieldConfig != null)
        {
            TFieldConfigBean relatedBean = aTFieldConfig.getBean(createdBeans);
            result.setTFieldConfigBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TTextBoxSettings with the contents
     * of a TTextBoxSettingsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TTextBoxSettingsBean which contents are used to create
     *        the resulting class
     * @return an instance of TTextBoxSettings with the contents of bean
     */
    public static TTextBoxSettings createTTextBoxSettings(TTextBoxSettingsBean bean)
        throws TorqueException
    {
        return createTTextBoxSettings(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TTextBoxSettings with the contents
     * of a TTextBoxSettingsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TTextBoxSettingsBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TTextBoxSettings with the contents of bean
     */

    public static TTextBoxSettings createTTextBoxSettings(TTextBoxSettingsBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TTextBoxSettings result = (TTextBoxSettings) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TTextBoxSettings();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setConfig(bean.getConfig());
        result.setRequired(bean.getRequired());
        result.setDefaultText(bean.getDefaultText());
        result.setDefaultInteger(bean.getDefaultInteger());
        result.setDefaultDouble(bean.getDefaultDouble());
        result.setDefaultDate(bean.getDefaultDate());
        result.setDefaultChar(bean.getDefaultChar());
        result.setDateIsWithTime(bean.getDateIsWithTime());
        result.setDefaultOption(bean.getDefaultOption());
        result.setMinOption(bean.getMinOption());
        result.setMaxOption(bean.getMaxOption());
        result.setMinTextLength(bean.getMinTextLength());
        result.setMaxTextLength(bean.getMaxTextLength());
        result.setMinDate(bean.getMinDate());
        result.setMaxDate(bean.getMaxDate());
        result.setMinInteger(bean.getMinInteger());
        result.setMaxInteger(bean.getMaxInteger());
        result.setMinDouble(bean.getMinDouble());
        result.setMaxDouble(bean.getMaxDouble());
        result.setMaxDecimalDigit(bean.getMaxDecimalDigit());
        result.setParameterCode(bean.getParameterCode());
        result.setValidValue(bean.getValidValue());
        result.setUuid(bean.getUuid());





        {
            TFieldConfigBean relatedBean = bean.getTFieldConfigBean();
            if (relatedBean != null)
            {
                TFieldConfig relatedObject = TFieldConfig.createTFieldConfig(relatedBean, createdObjects);
                result.setTFieldConfig(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TTextBoxSettings:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Config = ")
           .append(getConfig())
           .append("\n");
        str.append("Required = ")
           .append(getRequired())
           .append("\n");
        str.append("DefaultText = ")
           .append(getDefaultText())
           .append("\n");
        str.append("DefaultInteger = ")
           .append(getDefaultInteger())
           .append("\n");
        str.append("DefaultDouble = ")
           .append(getDefaultDouble())
           .append("\n");
        str.append("DefaultDate = ")
           .append(getDefaultDate())
           .append("\n");
        str.append("DefaultChar = ")
           .append(getDefaultChar())
           .append("\n");
        str.append("DateIsWithTime = ")
           .append(getDateIsWithTime())
           .append("\n");
        str.append("DefaultOption = ")
           .append(getDefaultOption())
           .append("\n");
        str.append("MinOption = ")
           .append(getMinOption())
           .append("\n");
        str.append("MaxOption = ")
           .append(getMaxOption())
           .append("\n");
        str.append("MinTextLength = ")
           .append(getMinTextLength())
           .append("\n");
        str.append("MaxTextLength = ")
           .append(getMaxTextLength())
           .append("\n");
        str.append("MinDate = ")
           .append(getMinDate())
           .append("\n");
        str.append("MaxDate = ")
           .append(getMaxDate())
           .append("\n");
        str.append("MinInteger = ")
           .append(getMinInteger())
           .append("\n");
        str.append("MaxInteger = ")
           .append(getMaxInteger())
           .append("\n");
        str.append("MinDouble = ")
           .append(getMinDouble())
           .append("\n");
        str.append("MaxDouble = ")
           .append(getMaxDouble())
           .append("\n");
        str.append("MaxDecimalDigit = ")
           .append(getMaxDecimalDigit())
           .append("\n");
        str.append("ParameterCode = ")
           .append(getParameterCode())
           .append("\n");
        str.append("ValidValue = ")
           .append(getValidValue())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
