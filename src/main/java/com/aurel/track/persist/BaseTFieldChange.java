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



import com.aurel.track.persist.THistoryTransaction;
import com.aurel.track.persist.THistoryTransactionPeer;
import com.aurel.track.persist.TOption;
import com.aurel.track.persist.TOptionPeer;
import com.aurel.track.persist.TOption;
import com.aurel.track.persist.TOptionPeer;
import com.aurel.track.persist.TFieldChange;
import com.aurel.track.persist.TFieldChangePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.THistoryTransactionBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TOptionBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TFieldChange
 */
public abstract class BaseTFieldChange extends TpBaseObject
{
    /** The Peer class */
    private static final TFieldChangePeer peer =
        new TFieldChangePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the fieldKey field */
    private Integer fieldKey;

    /** The value for the historyTransaction field */
    private Integer historyTransaction;

    /** The value for the newTextValue field */
    private String newTextValue;

    /** The value for the oldTextValue field */
    private String oldTextValue;

    /** The value for the newIntegerValue field */
    private Integer newIntegerValue;

    /** The value for the oldIntegerValue field */
    private Integer oldIntegerValue;

    /** The value for the newDoubleValue field */
    private Double newDoubleValue;

    /** The value for the oldDoubleValue field */
    private Double oldDoubleValue;

    /** The value for the newDateValue field */
    private Date newDateValue;

    /** The value for the oldDateValue field */
    private Date oldDateValue;

    /** The value for the newCharacterValue field */
    private String newCharacterValue;

    /** The value for the oldCharacterValue field */
    private String oldCharacterValue;

    /** The value for the newLongTextValue field */
    private String newLongTextValue;

    /** The value for the oldLongTextValue field */
    private String oldLongTextValue;

    /** The value for the newSystemOptionID field */
    private Integer newSystemOptionID;

    /** The value for the oldSystemOptionID field */
    private Integer oldSystemOptionID;

    /** The value for the systemOptionType field */
    private Integer systemOptionType;

    /** The value for the newCustomOptionID field */
    private Integer newCustomOptionID;

    /** The value for the oldCustomOptionID field */
    private Integer oldCustomOptionID;

    /** The value for the parameterCode field */
    private Integer parameterCode;

    /** The value for the validValue field */
    private Integer validValue;

    /** The value for the parentComment field */
    private Integer parentComment;

    /** The value for the timesEdited field */
    private Integer timesEdited;

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


    }

    /**
     * Get the FieldKey
     *
     * @return Integer
     */
    public Integer getFieldKey()
    {
        return fieldKey;
    }


    /**
     * Set the value of FieldKey
     *
     * @param v new value
     */
    public void setFieldKey(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldKey, v))
        {
            this.fieldKey = v;
            setModified(true);
        }


    }

    /**
     * Get the HistoryTransaction
     *
     * @return Integer
     */
    public Integer getHistoryTransaction()
    {
        return historyTransaction;
    }


    /**
     * Set the value of HistoryTransaction
     *
     * @param v new value
     */
    public void setHistoryTransaction(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.historyTransaction, v))
        {
            this.historyTransaction = v;
            setModified(true);
        }


        if (aTHistoryTransaction != null && !ObjectUtils.equals(aTHistoryTransaction.getObjectID(), v))
        {
            aTHistoryTransaction = null;
        }

    }

    /**
     * Get the NewTextValue
     *
     * @return String
     */
    public String getNewTextValue()
    {
        return newTextValue;
    }


    /**
     * Set the value of NewTextValue
     *
     * @param v new value
     */
    public void setNewTextValue(String v) 
    {

        if (!ObjectUtils.equals(this.newTextValue, v))
        {
            this.newTextValue = v;
            setModified(true);
        }


    }

    /**
     * Get the OldTextValue
     *
     * @return String
     */
    public String getOldTextValue()
    {
        return oldTextValue;
    }


    /**
     * Set the value of OldTextValue
     *
     * @param v new value
     */
    public void setOldTextValue(String v) 
    {

        if (!ObjectUtils.equals(this.oldTextValue, v))
        {
            this.oldTextValue = v;
            setModified(true);
        }


    }

    /**
     * Get the NewIntegerValue
     *
     * @return Integer
     */
    public Integer getNewIntegerValue()
    {
        return newIntegerValue;
    }


    /**
     * Set the value of NewIntegerValue
     *
     * @param v new value
     */
    public void setNewIntegerValue(Integer v) 
    {

        if (!ObjectUtils.equals(this.newIntegerValue, v))
        {
            this.newIntegerValue = v;
            setModified(true);
        }


    }

    /**
     * Get the OldIntegerValue
     *
     * @return Integer
     */
    public Integer getOldIntegerValue()
    {
        return oldIntegerValue;
    }


    /**
     * Set the value of OldIntegerValue
     *
     * @param v new value
     */
    public void setOldIntegerValue(Integer v) 
    {

        if (!ObjectUtils.equals(this.oldIntegerValue, v))
        {
            this.oldIntegerValue = v;
            setModified(true);
        }


    }

    /**
     * Get the NewDoubleValue
     *
     * @return Double
     */
    public Double getNewDoubleValue()
    {
        return newDoubleValue;
    }


    /**
     * Set the value of NewDoubleValue
     *
     * @param v new value
     */
    public void setNewDoubleValue(Double v) 
    {

        if (!ObjectUtils.equals(this.newDoubleValue, v))
        {
            this.newDoubleValue = v;
            setModified(true);
        }


    }

    /**
     * Get the OldDoubleValue
     *
     * @return Double
     */
    public Double getOldDoubleValue()
    {
        return oldDoubleValue;
    }


    /**
     * Set the value of OldDoubleValue
     *
     * @param v new value
     */
    public void setOldDoubleValue(Double v) 
    {

        if (!ObjectUtils.equals(this.oldDoubleValue, v))
        {
            this.oldDoubleValue = v;
            setModified(true);
        }


    }

    /**
     * Get the NewDateValue
     *
     * @return Date
     */
    public Date getNewDateValue()
    {
        return newDateValue;
    }


    /**
     * Set the value of NewDateValue
     *
     * @param v new value
     */
    public void setNewDateValue(Date v) 
    {

        if (!ObjectUtils.equals(this.newDateValue, v))
        {
            this.newDateValue = v;
            setModified(true);
        }


    }

    /**
     * Get the OldDateValue
     *
     * @return Date
     */
    public Date getOldDateValue()
    {
        return oldDateValue;
    }


    /**
     * Set the value of OldDateValue
     *
     * @param v new value
     */
    public void setOldDateValue(Date v) 
    {

        if (!ObjectUtils.equals(this.oldDateValue, v))
        {
            this.oldDateValue = v;
            setModified(true);
        }


    }

    /**
     * Get the NewCharacterValue
     *
     * @return String
     */
    public String getNewCharacterValue()
    {
        return newCharacterValue;
    }


    /**
     * Set the value of NewCharacterValue
     *
     * @param v new value
     */
    public void setNewCharacterValue(String v) 
    {

        if (!ObjectUtils.equals(this.newCharacterValue, v))
        {
            this.newCharacterValue = v;
            setModified(true);
        }


    }

    /**
     * Get the OldCharacterValue
     *
     * @return String
     */
    public String getOldCharacterValue()
    {
        return oldCharacterValue;
    }


    /**
     * Set the value of OldCharacterValue
     *
     * @param v new value
     */
    public void setOldCharacterValue(String v) 
    {

        if (!ObjectUtils.equals(this.oldCharacterValue, v))
        {
            this.oldCharacterValue = v;
            setModified(true);
        }


    }

    /**
     * Get the NewLongTextValue
     *
     * @return String
     */
    public String getNewLongTextValue()
    {
        return newLongTextValue;
    }


    /**
     * Set the value of NewLongTextValue
     *
     * @param v new value
     */
    public void setNewLongTextValue(String v) 
    {

        if (!ObjectUtils.equals(this.newLongTextValue, v))
        {
            this.newLongTextValue = v;
            setModified(true);
        }


    }

    /**
     * Get the OldLongTextValue
     *
     * @return String
     */
    public String getOldLongTextValue()
    {
        return oldLongTextValue;
    }


    /**
     * Set the value of OldLongTextValue
     *
     * @param v new value
     */
    public void setOldLongTextValue(String v) 
    {

        if (!ObjectUtils.equals(this.oldLongTextValue, v))
        {
            this.oldLongTextValue = v;
            setModified(true);
        }


    }

    /**
     * Get the NewSystemOptionID
     *
     * @return Integer
     */
    public Integer getNewSystemOptionID()
    {
        return newSystemOptionID;
    }


    /**
     * Set the value of NewSystemOptionID
     *
     * @param v new value
     */
    public void setNewSystemOptionID(Integer v) 
    {

        if (!ObjectUtils.equals(this.newSystemOptionID, v))
        {
            this.newSystemOptionID = v;
            setModified(true);
        }


    }

    /**
     * Get the OldSystemOptionID
     *
     * @return Integer
     */
    public Integer getOldSystemOptionID()
    {
        return oldSystemOptionID;
    }


    /**
     * Set the value of OldSystemOptionID
     *
     * @param v new value
     */
    public void setOldSystemOptionID(Integer v) 
    {

        if (!ObjectUtils.equals(this.oldSystemOptionID, v))
        {
            this.oldSystemOptionID = v;
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
     * Get the NewCustomOptionID
     *
     * @return Integer
     */
    public Integer getNewCustomOptionID()
    {
        return newCustomOptionID;
    }


    /**
     * Set the value of NewCustomOptionID
     *
     * @param v new value
     */
    public void setNewCustomOptionID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.newCustomOptionID, v))
        {
            this.newCustomOptionID = v;
            setModified(true);
        }


        if (aTOptionRelatedByNewCustomOptionID != null && !ObjectUtils.equals(aTOptionRelatedByNewCustomOptionID.getObjectID(), v))
        {
            aTOptionRelatedByNewCustomOptionID = null;
        }

    }

    /**
     * Get the OldCustomOptionID
     *
     * @return Integer
     */
    public Integer getOldCustomOptionID()
    {
        return oldCustomOptionID;
    }


    /**
     * Set the value of OldCustomOptionID
     *
     * @param v new value
     */
    public void setOldCustomOptionID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.oldCustomOptionID, v))
        {
            this.oldCustomOptionID = v;
            setModified(true);
        }


        if (aTOptionRelatedByOldCustomOptionID != null && !ObjectUtils.equals(aTOptionRelatedByOldCustomOptionID.getObjectID(), v))
        {
            aTOptionRelatedByOldCustomOptionID = null;
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
     * Get the ParentComment
     *
     * @return Integer
     */
    public Integer getParentComment()
    {
        return parentComment;
    }


    /**
     * Set the value of ParentComment
     *
     * @param v new value
     */
    public void setParentComment(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.parentComment, v))
        {
            this.parentComment = v;
            setModified(true);
        }


        if (aTFieldChangeRelatedByParentComment != null && !ObjectUtils.equals(aTFieldChangeRelatedByParentComment.getObjectID(), v))
        {
            aTFieldChangeRelatedByParentComment = null;
        }

    }

    /**
     * Get the TimesEdited
     *
     * @return Integer
     */
    public Integer getTimesEdited()
    {
        return timesEdited;
    }


    /**
     * Set the value of TimesEdited
     *
     * @param v new value
     */
    public void setTimesEdited(Integer v) 
    {

        if (!ObjectUtils.equals(this.timesEdited, v))
        {
            this.timesEdited = v;
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

    



    private THistoryTransaction aTHistoryTransaction;

    /**
     * Declares an association between this object and a THistoryTransaction object
     *
     * @param v THistoryTransaction
     * @throws TorqueException
     */
    public void setTHistoryTransaction(THistoryTransaction v) throws TorqueException
    {
        if (v == null)
        {
            setHistoryTransaction((Integer) null);
        }
        else
        {
            setHistoryTransaction(v.getObjectID());
        }
        aTHistoryTransaction = v;
    }


    /**
     * Returns the associated THistoryTransaction object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated THistoryTransaction object
     * @throws TorqueException
     */
    public THistoryTransaction getTHistoryTransaction()
        throws TorqueException
    {
        if (aTHistoryTransaction == null && (!ObjectUtils.equals(this.historyTransaction, null)))
        {
            aTHistoryTransaction = THistoryTransactionPeer.retrieveByPK(SimpleKey.keyFor(this.historyTransaction));
        }
        return aTHistoryTransaction;
    }

    /**
     * Return the associated THistoryTransaction object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated THistoryTransaction object
     * @throws TorqueException
     */
    public THistoryTransaction getTHistoryTransaction(Connection connection)
        throws TorqueException
    {
        if (aTHistoryTransaction == null && (!ObjectUtils.equals(this.historyTransaction, null)))
        {
            aTHistoryTransaction = THistoryTransactionPeer.retrieveByPK(SimpleKey.keyFor(this.historyTransaction), connection);
        }
        return aTHistoryTransaction;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTHistoryTransactionKey(ObjectKey key) throws TorqueException
    {

        setHistoryTransaction(new Integer(((NumberKey) key).intValue()));
    }




    private TFieldChange aTFieldChangeRelatedByParentComment;

    /**
     * Declares an association between this object and a TFieldChange object
     *
     * @param v TFieldChange
     * @throws TorqueException
     */
    public void setTFieldChangeRelatedByParentComment(TFieldChange v) throws TorqueException
    {
        if (v == null)
        {
            setParentComment((Integer) null);
        }
        else
        {
            setParentComment(v.getObjectID());
        }
        aTFieldChangeRelatedByParentComment = v;
    }


    /**
     * Returns the associated TFieldChange object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TFieldChange object
     * @throws TorqueException
     */
    public TFieldChange getTFieldChangeRelatedByParentComment()
        throws TorqueException
    {
        if (aTFieldChangeRelatedByParentComment == null && (!ObjectUtils.equals(this.parentComment, null)))
        {
            aTFieldChangeRelatedByParentComment = TFieldChangePeer.retrieveByPK(SimpleKey.keyFor(this.parentComment));
        }
        return aTFieldChangeRelatedByParentComment;
    }

    /**
     * Return the associated TFieldChange object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TFieldChange object
     * @throws TorqueException
     */
    public TFieldChange getTFieldChangeRelatedByParentComment(Connection connection)
        throws TorqueException
    {
        if (aTFieldChangeRelatedByParentComment == null && (!ObjectUtils.equals(this.parentComment, null)))
        {
            aTFieldChangeRelatedByParentComment = TFieldChangePeer.retrieveByPK(SimpleKey.keyFor(this.parentComment), connection);
        }
        return aTFieldChangeRelatedByParentComment;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTFieldChangeRelatedByParentCommentKey(ObjectKey key) throws TorqueException
    {

        setParentComment(new Integer(((NumberKey) key).intValue()));
    }




    private TOption aTOptionRelatedByNewCustomOptionID;

    /**
     * Declares an association between this object and a TOption object
     *
     * @param v TOption
     * @throws TorqueException
     */
    public void setTOptionRelatedByNewCustomOptionID(TOption v) throws TorqueException
    {
        if (v == null)
        {
            setNewCustomOptionID((Integer) null);
        }
        else
        {
            setNewCustomOptionID(v.getObjectID());
        }
        aTOptionRelatedByNewCustomOptionID = v;
    }


    /**
     * Returns the associated TOption object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TOption object
     * @throws TorqueException
     */
    public TOption getTOptionRelatedByNewCustomOptionID()
        throws TorqueException
    {
        if (aTOptionRelatedByNewCustomOptionID == null && (!ObjectUtils.equals(this.newCustomOptionID, null)))
        {
            aTOptionRelatedByNewCustomOptionID = TOptionPeer.retrieveByPK(SimpleKey.keyFor(this.newCustomOptionID));
        }
        return aTOptionRelatedByNewCustomOptionID;
    }

    /**
     * Return the associated TOption object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TOption object
     * @throws TorqueException
     */
    public TOption getTOptionRelatedByNewCustomOptionID(Connection connection)
        throws TorqueException
    {
        if (aTOptionRelatedByNewCustomOptionID == null && (!ObjectUtils.equals(this.newCustomOptionID, null)))
        {
            aTOptionRelatedByNewCustomOptionID = TOptionPeer.retrieveByPK(SimpleKey.keyFor(this.newCustomOptionID), connection);
        }
        return aTOptionRelatedByNewCustomOptionID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTOptionRelatedByNewCustomOptionIDKey(ObjectKey key) throws TorqueException
    {

        setNewCustomOptionID(new Integer(((NumberKey) key).intValue()));
    }




    private TOption aTOptionRelatedByOldCustomOptionID;

    /**
     * Declares an association between this object and a TOption object
     *
     * @param v TOption
     * @throws TorqueException
     */
    public void setTOptionRelatedByOldCustomOptionID(TOption v) throws TorqueException
    {
        if (v == null)
        {
            setOldCustomOptionID((Integer) null);
        }
        else
        {
            setOldCustomOptionID(v.getObjectID());
        }
        aTOptionRelatedByOldCustomOptionID = v;
    }


    /**
     * Returns the associated TOption object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TOption object
     * @throws TorqueException
     */
    public TOption getTOptionRelatedByOldCustomOptionID()
        throws TorqueException
    {
        if (aTOptionRelatedByOldCustomOptionID == null && (!ObjectUtils.equals(this.oldCustomOptionID, null)))
        {
            aTOptionRelatedByOldCustomOptionID = TOptionPeer.retrieveByPK(SimpleKey.keyFor(this.oldCustomOptionID));
        }
        return aTOptionRelatedByOldCustomOptionID;
    }

    /**
     * Return the associated TOption object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TOption object
     * @throws TorqueException
     */
    public TOption getTOptionRelatedByOldCustomOptionID(Connection connection)
        throws TorqueException
    {
        if (aTOptionRelatedByOldCustomOptionID == null && (!ObjectUtils.equals(this.oldCustomOptionID, null)))
        {
            aTOptionRelatedByOldCustomOptionID = TOptionPeer.retrieveByPK(SimpleKey.keyFor(this.oldCustomOptionID), connection);
        }
        return aTOptionRelatedByOldCustomOptionID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTOptionRelatedByOldCustomOptionIDKey(ObjectKey key) throws TorqueException
    {

        setOldCustomOptionID(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("FieldKey");
            fieldNames.add("HistoryTransaction");
            fieldNames.add("NewTextValue");
            fieldNames.add("OldTextValue");
            fieldNames.add("NewIntegerValue");
            fieldNames.add("OldIntegerValue");
            fieldNames.add("NewDoubleValue");
            fieldNames.add("OldDoubleValue");
            fieldNames.add("NewDateValue");
            fieldNames.add("OldDateValue");
            fieldNames.add("NewCharacterValue");
            fieldNames.add("OldCharacterValue");
            fieldNames.add("NewLongTextValue");
            fieldNames.add("OldLongTextValue");
            fieldNames.add("NewSystemOptionID");
            fieldNames.add("OldSystemOptionID");
            fieldNames.add("SystemOptionType");
            fieldNames.add("NewCustomOptionID");
            fieldNames.add("OldCustomOptionID");
            fieldNames.add("ParameterCode");
            fieldNames.add("ValidValue");
            fieldNames.add("ParentComment");
            fieldNames.add("TimesEdited");
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
        if (name.equals("FieldKey"))
        {
            return getFieldKey();
        }
        if (name.equals("HistoryTransaction"))
        {
            return getHistoryTransaction();
        }
        if (name.equals("NewTextValue"))
        {
            return getNewTextValue();
        }
        if (name.equals("OldTextValue"))
        {
            return getOldTextValue();
        }
        if (name.equals("NewIntegerValue"))
        {
            return getNewIntegerValue();
        }
        if (name.equals("OldIntegerValue"))
        {
            return getOldIntegerValue();
        }
        if (name.equals("NewDoubleValue"))
        {
            return getNewDoubleValue();
        }
        if (name.equals("OldDoubleValue"))
        {
            return getOldDoubleValue();
        }
        if (name.equals("NewDateValue"))
        {
            return getNewDateValue();
        }
        if (name.equals("OldDateValue"))
        {
            return getOldDateValue();
        }
        if (name.equals("NewCharacterValue"))
        {
            return getNewCharacterValue();
        }
        if (name.equals("OldCharacterValue"))
        {
            return getOldCharacterValue();
        }
        if (name.equals("NewLongTextValue"))
        {
            return getNewLongTextValue();
        }
        if (name.equals("OldLongTextValue"))
        {
            return getOldLongTextValue();
        }
        if (name.equals("NewSystemOptionID"))
        {
            return getNewSystemOptionID();
        }
        if (name.equals("OldSystemOptionID"))
        {
            return getOldSystemOptionID();
        }
        if (name.equals("SystemOptionType"))
        {
            return getSystemOptionType();
        }
        if (name.equals("NewCustomOptionID"))
        {
            return getNewCustomOptionID();
        }
        if (name.equals("OldCustomOptionID"))
        {
            return getOldCustomOptionID();
        }
        if (name.equals("ParameterCode"))
        {
            return getParameterCode();
        }
        if (name.equals("ValidValue"))
        {
            return getValidValue();
        }
        if (name.equals("ParentComment"))
        {
            return getParentComment();
        }
        if (name.equals("TimesEdited"))
        {
            return getTimesEdited();
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
        if (name.equals("FieldKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldKey((Integer) value);
            return true;
        }
        if (name.equals("HistoryTransaction"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setHistoryTransaction((Integer) value);
            return true;
        }
        if (name.equals("NewTextValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNewTextValue((String) value);
            return true;
        }
        if (name.equals("OldTextValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOldTextValue((String) value);
            return true;
        }
        if (name.equals("NewIntegerValue"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNewIntegerValue((Integer) value);
            return true;
        }
        if (name.equals("OldIntegerValue"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOldIntegerValue((Integer) value);
            return true;
        }
        if (name.equals("NewDoubleValue"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNewDoubleValue((Double) value);
            return true;
        }
        if (name.equals("OldDoubleValue"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOldDoubleValue((Double) value);
            return true;
        }
        if (name.equals("NewDateValue"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNewDateValue((Date) value);
            return true;
        }
        if (name.equals("OldDateValue"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOldDateValue((Date) value);
            return true;
        }
        if (name.equals("NewCharacterValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNewCharacterValue((String) value);
            return true;
        }
        if (name.equals("OldCharacterValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOldCharacterValue((String) value);
            return true;
        }
        if (name.equals("NewLongTextValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNewLongTextValue((String) value);
            return true;
        }
        if (name.equals("OldLongTextValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOldLongTextValue((String) value);
            return true;
        }
        if (name.equals("NewSystemOptionID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNewSystemOptionID((Integer) value);
            return true;
        }
        if (name.equals("OldSystemOptionID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOldSystemOptionID((Integer) value);
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
        if (name.equals("NewCustomOptionID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNewCustomOptionID((Integer) value);
            return true;
        }
        if (name.equals("OldCustomOptionID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOldCustomOptionID((Integer) value);
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
        if (name.equals("ParentComment"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParentComment((Integer) value);
            return true;
        }
        if (name.equals("TimesEdited"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTimesEdited((Integer) value);
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
        if (name.equals(TFieldChangePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TFieldChangePeer.FIELDKEY))
        {
            return getFieldKey();
        }
        if (name.equals(TFieldChangePeer.HISTORYTRANSACTION))
        {
            return getHistoryTransaction();
        }
        if (name.equals(TFieldChangePeer.NEWTEXTVALUE))
        {
            return getNewTextValue();
        }
        if (name.equals(TFieldChangePeer.OLDTEXTVALUE))
        {
            return getOldTextValue();
        }
        if (name.equals(TFieldChangePeer.NEWINTEGERVALUE))
        {
            return getNewIntegerValue();
        }
        if (name.equals(TFieldChangePeer.OLDINTEGERVALUE))
        {
            return getOldIntegerValue();
        }
        if (name.equals(TFieldChangePeer.NEWDOUBLEVALUE))
        {
            return getNewDoubleValue();
        }
        if (name.equals(TFieldChangePeer.OLDDOUBLEVALUE))
        {
            return getOldDoubleValue();
        }
        if (name.equals(TFieldChangePeer.NEWDATEVALUE))
        {
            return getNewDateValue();
        }
        if (name.equals(TFieldChangePeer.OLDDATEVALUE))
        {
            return getOldDateValue();
        }
        if (name.equals(TFieldChangePeer.NEWCHARACTERVALUE))
        {
            return getNewCharacterValue();
        }
        if (name.equals(TFieldChangePeer.OLDCHARACTERVALUE))
        {
            return getOldCharacterValue();
        }
        if (name.equals(TFieldChangePeer.NEWLONGTEXTVALUE))
        {
            return getNewLongTextValue();
        }
        if (name.equals(TFieldChangePeer.OLDLONGTEXTVALUE))
        {
            return getOldLongTextValue();
        }
        if (name.equals(TFieldChangePeer.NEWSYSTEMOPTIONID))
        {
            return getNewSystemOptionID();
        }
        if (name.equals(TFieldChangePeer.OLDSYSTEMOPTIONID))
        {
            return getOldSystemOptionID();
        }
        if (name.equals(TFieldChangePeer.SYSTEMOPTIONTYPE))
        {
            return getSystemOptionType();
        }
        if (name.equals(TFieldChangePeer.NEWCUSTOMOPTIONID))
        {
            return getNewCustomOptionID();
        }
        if (name.equals(TFieldChangePeer.OLDCUSTOMOPTIONID))
        {
            return getOldCustomOptionID();
        }
        if (name.equals(TFieldChangePeer.PARAMETERCODE))
        {
            return getParameterCode();
        }
        if (name.equals(TFieldChangePeer.VALIDVALUE))
        {
            return getValidValue();
        }
        if (name.equals(TFieldChangePeer.PARENTCOMMENT))
        {
            return getParentComment();
        }
        if (name.equals(TFieldChangePeer.TIMESEDITED))
        {
            return getTimesEdited();
        }
        if (name.equals(TFieldChangePeer.TPUUID))
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
      if (TFieldChangePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TFieldChangePeer.FIELDKEY.equals(name))
        {
            return setByName("FieldKey", value);
        }
      if (TFieldChangePeer.HISTORYTRANSACTION.equals(name))
        {
            return setByName("HistoryTransaction", value);
        }
      if (TFieldChangePeer.NEWTEXTVALUE.equals(name))
        {
            return setByName("NewTextValue", value);
        }
      if (TFieldChangePeer.OLDTEXTVALUE.equals(name))
        {
            return setByName("OldTextValue", value);
        }
      if (TFieldChangePeer.NEWINTEGERVALUE.equals(name))
        {
            return setByName("NewIntegerValue", value);
        }
      if (TFieldChangePeer.OLDINTEGERVALUE.equals(name))
        {
            return setByName("OldIntegerValue", value);
        }
      if (TFieldChangePeer.NEWDOUBLEVALUE.equals(name))
        {
            return setByName("NewDoubleValue", value);
        }
      if (TFieldChangePeer.OLDDOUBLEVALUE.equals(name))
        {
            return setByName("OldDoubleValue", value);
        }
      if (TFieldChangePeer.NEWDATEVALUE.equals(name))
        {
            return setByName("NewDateValue", value);
        }
      if (TFieldChangePeer.OLDDATEVALUE.equals(name))
        {
            return setByName("OldDateValue", value);
        }
      if (TFieldChangePeer.NEWCHARACTERVALUE.equals(name))
        {
            return setByName("NewCharacterValue", value);
        }
      if (TFieldChangePeer.OLDCHARACTERVALUE.equals(name))
        {
            return setByName("OldCharacterValue", value);
        }
      if (TFieldChangePeer.NEWLONGTEXTVALUE.equals(name))
        {
            return setByName("NewLongTextValue", value);
        }
      if (TFieldChangePeer.OLDLONGTEXTVALUE.equals(name))
        {
            return setByName("OldLongTextValue", value);
        }
      if (TFieldChangePeer.NEWSYSTEMOPTIONID.equals(name))
        {
            return setByName("NewSystemOptionID", value);
        }
      if (TFieldChangePeer.OLDSYSTEMOPTIONID.equals(name))
        {
            return setByName("OldSystemOptionID", value);
        }
      if (TFieldChangePeer.SYSTEMOPTIONTYPE.equals(name))
        {
            return setByName("SystemOptionType", value);
        }
      if (TFieldChangePeer.NEWCUSTOMOPTIONID.equals(name))
        {
            return setByName("NewCustomOptionID", value);
        }
      if (TFieldChangePeer.OLDCUSTOMOPTIONID.equals(name))
        {
            return setByName("OldCustomOptionID", value);
        }
      if (TFieldChangePeer.PARAMETERCODE.equals(name))
        {
            return setByName("ParameterCode", value);
        }
      if (TFieldChangePeer.VALIDVALUE.equals(name))
        {
            return setByName("ValidValue", value);
        }
      if (TFieldChangePeer.PARENTCOMMENT.equals(name))
        {
            return setByName("ParentComment", value);
        }
      if (TFieldChangePeer.TIMESEDITED.equals(name))
        {
            return setByName("TimesEdited", value);
        }
      if (TFieldChangePeer.TPUUID.equals(name))
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
            return getFieldKey();
        }
        if (pos == 2)
        {
            return getHistoryTransaction();
        }
        if (pos == 3)
        {
            return getNewTextValue();
        }
        if (pos == 4)
        {
            return getOldTextValue();
        }
        if (pos == 5)
        {
            return getNewIntegerValue();
        }
        if (pos == 6)
        {
            return getOldIntegerValue();
        }
        if (pos == 7)
        {
            return getNewDoubleValue();
        }
        if (pos == 8)
        {
            return getOldDoubleValue();
        }
        if (pos == 9)
        {
            return getNewDateValue();
        }
        if (pos == 10)
        {
            return getOldDateValue();
        }
        if (pos == 11)
        {
            return getNewCharacterValue();
        }
        if (pos == 12)
        {
            return getOldCharacterValue();
        }
        if (pos == 13)
        {
            return getNewLongTextValue();
        }
        if (pos == 14)
        {
            return getOldLongTextValue();
        }
        if (pos == 15)
        {
            return getNewSystemOptionID();
        }
        if (pos == 16)
        {
            return getOldSystemOptionID();
        }
        if (pos == 17)
        {
            return getSystemOptionType();
        }
        if (pos == 18)
        {
            return getNewCustomOptionID();
        }
        if (pos == 19)
        {
            return getOldCustomOptionID();
        }
        if (pos == 20)
        {
            return getParameterCode();
        }
        if (pos == 21)
        {
            return getValidValue();
        }
        if (pos == 22)
        {
            return getParentComment();
        }
        if (pos == 23)
        {
            return getTimesEdited();
        }
        if (pos == 24)
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
            return setByName("FieldKey", value);
        }
    if (position == 2)
        {
            return setByName("HistoryTransaction", value);
        }
    if (position == 3)
        {
            return setByName("NewTextValue", value);
        }
    if (position == 4)
        {
            return setByName("OldTextValue", value);
        }
    if (position == 5)
        {
            return setByName("NewIntegerValue", value);
        }
    if (position == 6)
        {
            return setByName("OldIntegerValue", value);
        }
    if (position == 7)
        {
            return setByName("NewDoubleValue", value);
        }
    if (position == 8)
        {
            return setByName("OldDoubleValue", value);
        }
    if (position == 9)
        {
            return setByName("NewDateValue", value);
        }
    if (position == 10)
        {
            return setByName("OldDateValue", value);
        }
    if (position == 11)
        {
            return setByName("NewCharacterValue", value);
        }
    if (position == 12)
        {
            return setByName("OldCharacterValue", value);
        }
    if (position == 13)
        {
            return setByName("NewLongTextValue", value);
        }
    if (position == 14)
        {
            return setByName("OldLongTextValue", value);
        }
    if (position == 15)
        {
            return setByName("NewSystemOptionID", value);
        }
    if (position == 16)
        {
            return setByName("OldSystemOptionID", value);
        }
    if (position == 17)
        {
            return setByName("SystemOptionType", value);
        }
    if (position == 18)
        {
            return setByName("NewCustomOptionID", value);
        }
    if (position == 19)
        {
            return setByName("OldCustomOptionID", value);
        }
    if (position == 20)
        {
            return setByName("ParameterCode", value);
        }
    if (position == 21)
        {
            return setByName("ValidValue", value);
        }
    if (position == 22)
        {
            return setByName("ParentComment", value);
        }
    if (position == 23)
        {
            return setByName("TimesEdited", value);
        }
    if (position == 24)
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
        save(TFieldChangePeer.DATABASE_NAME);
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
                    TFieldChangePeer.doInsert((TFieldChange) this, con);
                    setNew(false);
                }
                else
                {
                    TFieldChangePeer.doUpdate((TFieldChange) this, con);
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
    public TFieldChange copy() throws TorqueException
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
    public TFieldChange copy(Connection con) throws TorqueException
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
    public TFieldChange copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TFieldChange(), deepcopy);
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
    public TFieldChange copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TFieldChange(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TFieldChange copyInto(TFieldChange copyObj) throws TorqueException
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
    protected TFieldChange copyInto(TFieldChange copyObj, Connection con) throws TorqueException
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
    protected TFieldChange copyInto(TFieldChange copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setFieldKey(fieldKey);
        copyObj.setHistoryTransaction(historyTransaction);
        copyObj.setNewTextValue(newTextValue);
        copyObj.setOldTextValue(oldTextValue);
        copyObj.setNewIntegerValue(newIntegerValue);
        copyObj.setOldIntegerValue(oldIntegerValue);
        copyObj.setNewDoubleValue(newDoubleValue);
        copyObj.setOldDoubleValue(oldDoubleValue);
        copyObj.setNewDateValue(newDateValue);
        copyObj.setOldDateValue(oldDateValue);
        copyObj.setNewCharacterValue(newCharacterValue);
        copyObj.setOldCharacterValue(oldCharacterValue);
        copyObj.setNewLongTextValue(newLongTextValue);
        copyObj.setOldLongTextValue(oldLongTextValue);
        copyObj.setNewSystemOptionID(newSystemOptionID);
        copyObj.setOldSystemOptionID(oldSystemOptionID);
        copyObj.setSystemOptionType(systemOptionType);
        copyObj.setNewCustomOptionID(newCustomOptionID);
        copyObj.setOldCustomOptionID(oldCustomOptionID);
        copyObj.setParameterCode(parameterCode);
        copyObj.setValidValue(validValue);
        copyObj.setParentComment(parentComment);
        copyObj.setTimesEdited(timesEdited);
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
    protected TFieldChange copyInto(TFieldChange copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setFieldKey(fieldKey);
        copyObj.setHistoryTransaction(historyTransaction);
        copyObj.setNewTextValue(newTextValue);
        copyObj.setOldTextValue(oldTextValue);
        copyObj.setNewIntegerValue(newIntegerValue);
        copyObj.setOldIntegerValue(oldIntegerValue);
        copyObj.setNewDoubleValue(newDoubleValue);
        copyObj.setOldDoubleValue(oldDoubleValue);
        copyObj.setNewDateValue(newDateValue);
        copyObj.setOldDateValue(oldDateValue);
        copyObj.setNewCharacterValue(newCharacterValue);
        copyObj.setOldCharacterValue(oldCharacterValue);
        copyObj.setNewLongTextValue(newLongTextValue);
        copyObj.setOldLongTextValue(oldLongTextValue);
        copyObj.setNewSystemOptionID(newSystemOptionID);
        copyObj.setOldSystemOptionID(oldSystemOptionID);
        copyObj.setSystemOptionType(systemOptionType);
        copyObj.setNewCustomOptionID(newCustomOptionID);
        copyObj.setOldCustomOptionID(oldCustomOptionID);
        copyObj.setParameterCode(parameterCode);
        copyObj.setValidValue(validValue);
        copyObj.setParentComment(parentComment);
        copyObj.setTimesEdited(timesEdited);
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
    public TFieldChangePeer getPeer()
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
        return TFieldChangePeer.getTableMap();
    }

  
    /**
     * Creates a TFieldChangeBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TFieldChangeBean with the contents of this object
     */
    public TFieldChangeBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TFieldChangeBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TFieldChangeBean with the contents of this object
     */
    public TFieldChangeBean getBean(IdentityMap createdBeans)
    {
        TFieldChangeBean result = (TFieldChangeBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TFieldChangeBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setFieldKey(getFieldKey());
        result.setHistoryTransaction(getHistoryTransaction());
        result.setNewTextValue(getNewTextValue());
        result.setOldTextValue(getOldTextValue());
        result.setNewIntegerValue(getNewIntegerValue());
        result.setOldIntegerValue(getOldIntegerValue());
        result.setNewDoubleValue(getNewDoubleValue());
        result.setOldDoubleValue(getOldDoubleValue());
        result.setNewDateValue(getNewDateValue());
        result.setOldDateValue(getOldDateValue());
        result.setNewCharacterValue(getNewCharacterValue());
        result.setOldCharacterValue(getOldCharacterValue());
        result.setNewLongTextValue(getNewLongTextValue());
        result.setOldLongTextValue(getOldLongTextValue());
        result.setNewSystemOptionID(getNewSystemOptionID());
        result.setOldSystemOptionID(getOldSystemOptionID());
        result.setSystemOptionType(getSystemOptionType());
        result.setNewCustomOptionID(getNewCustomOptionID());
        result.setOldCustomOptionID(getOldCustomOptionID());
        result.setParameterCode(getParameterCode());
        result.setValidValue(getValidValue());
        result.setParentComment(getParentComment());
        result.setTimesEdited(getTimesEdited());
        result.setUuid(getUuid());





        if (aTHistoryTransaction != null)
        {
            THistoryTransactionBean relatedBean = aTHistoryTransaction.getBean(createdBeans);
            result.setTHistoryTransactionBean(relatedBean);
        }



        if (aTFieldChangeRelatedByParentComment != null)
        {
            TFieldChangeBean relatedBean = aTFieldChangeRelatedByParentComment.getBean(createdBeans);
            result.setTFieldChangeBeanRelatedByParentComment(relatedBean);
        }



        if (aTOptionRelatedByNewCustomOptionID != null)
        {
            TOptionBean relatedBean = aTOptionRelatedByNewCustomOptionID.getBean(createdBeans);
            result.setTOptionBeanRelatedByNewCustomOptionID(relatedBean);
        }



        if (aTOptionRelatedByOldCustomOptionID != null)
        {
            TOptionBean relatedBean = aTOptionRelatedByOldCustomOptionID.getBean(createdBeans);
            result.setTOptionBeanRelatedByOldCustomOptionID(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TFieldChange with the contents
     * of a TFieldChangeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TFieldChangeBean which contents are used to create
     *        the resulting class
     * @return an instance of TFieldChange with the contents of bean
     */
    public static TFieldChange createTFieldChange(TFieldChangeBean bean)
        throws TorqueException
    {
        return createTFieldChange(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TFieldChange with the contents
     * of a TFieldChangeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TFieldChangeBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TFieldChange with the contents of bean
     */

    public static TFieldChange createTFieldChange(TFieldChangeBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TFieldChange result = (TFieldChange) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TFieldChange();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setFieldKey(bean.getFieldKey());
        result.setHistoryTransaction(bean.getHistoryTransaction());
        result.setNewTextValue(bean.getNewTextValue());
        result.setOldTextValue(bean.getOldTextValue());
        result.setNewIntegerValue(bean.getNewIntegerValue());
        result.setOldIntegerValue(bean.getOldIntegerValue());
        result.setNewDoubleValue(bean.getNewDoubleValue());
        result.setOldDoubleValue(bean.getOldDoubleValue());
        result.setNewDateValue(bean.getNewDateValue());
        result.setOldDateValue(bean.getOldDateValue());
        result.setNewCharacterValue(bean.getNewCharacterValue());
        result.setOldCharacterValue(bean.getOldCharacterValue());
        result.setNewLongTextValue(bean.getNewLongTextValue());
        result.setOldLongTextValue(bean.getOldLongTextValue());
        result.setNewSystemOptionID(bean.getNewSystemOptionID());
        result.setOldSystemOptionID(bean.getOldSystemOptionID());
        result.setSystemOptionType(bean.getSystemOptionType());
        result.setNewCustomOptionID(bean.getNewCustomOptionID());
        result.setOldCustomOptionID(bean.getOldCustomOptionID());
        result.setParameterCode(bean.getParameterCode());
        result.setValidValue(bean.getValidValue());
        result.setParentComment(bean.getParentComment());
        result.setTimesEdited(bean.getTimesEdited());
        result.setUuid(bean.getUuid());





        {
            THistoryTransactionBean relatedBean = bean.getTHistoryTransactionBean();
            if (relatedBean != null)
            {
                THistoryTransaction relatedObject = THistoryTransaction.createTHistoryTransaction(relatedBean, createdObjects);
                result.setTHistoryTransaction(relatedObject);
            }
        }



        {
            TFieldChangeBean relatedBean = bean.getTFieldChangeBeanRelatedByParentComment();
            if (relatedBean != null)
            {
                TFieldChange relatedObject = TFieldChange.createTFieldChange(relatedBean, createdObjects);
                result.setTFieldChangeRelatedByParentComment(relatedObject);
            }
        }



        {
            TOptionBean relatedBean = bean.getTOptionBeanRelatedByNewCustomOptionID();
            if (relatedBean != null)
            {
                TOption relatedObject = TOption.createTOption(relatedBean, createdObjects);
                result.setTOptionRelatedByNewCustomOptionID(relatedObject);
            }
        }



        {
            TOptionBean relatedBean = bean.getTOptionBeanRelatedByOldCustomOptionID();
            if (relatedBean != null)
            {
                TOption relatedObject = TOption.createTOption(relatedBean, createdObjects);
                result.setTOptionRelatedByOldCustomOptionID(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TFieldChange:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("FieldKey = ")
           .append(getFieldKey())
           .append("\n");
        str.append("HistoryTransaction = ")
           .append(getHistoryTransaction())
           .append("\n");
        str.append("NewTextValue = ")
           .append(getNewTextValue())
           .append("\n");
        str.append("OldTextValue = ")
           .append(getOldTextValue())
           .append("\n");
        str.append("NewIntegerValue = ")
           .append(getNewIntegerValue())
           .append("\n");
        str.append("OldIntegerValue = ")
           .append(getOldIntegerValue())
           .append("\n");
        str.append("NewDoubleValue = ")
           .append(getNewDoubleValue())
           .append("\n");
        str.append("OldDoubleValue = ")
           .append(getOldDoubleValue())
           .append("\n");
        str.append("NewDateValue = ")
           .append(getNewDateValue())
           .append("\n");
        str.append("OldDateValue = ")
           .append(getOldDateValue())
           .append("\n");
        str.append("NewCharacterValue = ")
           .append(getNewCharacterValue())
           .append("\n");
        str.append("OldCharacterValue = ")
           .append(getOldCharacterValue())
           .append("\n");
        str.append("NewLongTextValue = ")
           .append(getNewLongTextValue())
           .append("\n");
        str.append("OldLongTextValue = ")
           .append(getOldLongTextValue())
           .append("\n");
        str.append("NewSystemOptionID = ")
           .append(getNewSystemOptionID())
           .append("\n");
        str.append("OldSystemOptionID = ")
           .append(getOldSystemOptionID())
           .append("\n");
        str.append("SystemOptionType = ")
           .append(getSystemOptionType())
           .append("\n");
        str.append("NewCustomOptionID = ")
           .append(getNewCustomOptionID())
           .append("\n");
        str.append("OldCustomOptionID = ")
           .append(getOldCustomOptionID())
           .append("\n");
        str.append("ParameterCode = ")
           .append(getParameterCode())
           .append("\n");
        str.append("ValidValue = ")
           .append(getValidValue())
           .append("\n");
        str.append("ParentComment = ")
           .append(getParentComment())
           .append("\n");
        str.append("TimesEdited = ")
           .append(getTimesEdited())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
