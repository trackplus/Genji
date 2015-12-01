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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.torque.NoRowsException;
import org.apache.torque.TooManyRowsException;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.TorqueRuntimeException;
import org.apache.torque.map.MapBuilder;
import org.apache.torque.map.TableMap;
import org.apache.torque.om.DateKey;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.StringKey;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;

import com.workingdogs.village.DataSetException;
import com.workingdogs.village.QueryDataSet;
import com.workingdogs.village.Record;

// Local classes
import com.aurel.track.persist.map.*;




/**
 */
public abstract class BaseTFieldChangePeer
    extends BasePeer
{

    /** the default database name for this class */
    public static final String DATABASE_NAME;

     /** the table name for this class */
    public static final String TABLE_NAME;

    /**
     * @return the map builder for this peer
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @deprecated Torque.getMapBuilder(TFieldChangeMapBuilder.CLASS_NAME) instead
     */
    public static MapBuilder getMapBuilder()
        throws TorqueException
    {
        return Torque.getMapBuilder(TFieldChangeMapBuilder.CLASS_NAME);
    }

    /** the column name for the OBJECTID field */
    public static final String OBJECTID;
    /** the column name for the FIELDKEY field */
    public static final String FIELDKEY;
    /** the column name for the HISTORYTRANSACTION field */
    public static final String HISTORYTRANSACTION;
    /** the column name for the NEWTEXTVALUE field */
    public static final String NEWTEXTVALUE;
    /** the column name for the OLDTEXTVALUE field */
    public static final String OLDTEXTVALUE;
    /** the column name for the NEWINTEGERVALUE field */
    public static final String NEWINTEGERVALUE;
    /** the column name for the OLDINTEGERVALUE field */
    public static final String OLDINTEGERVALUE;
    /** the column name for the NEWDOUBLEVALUE field */
    public static final String NEWDOUBLEVALUE;
    /** the column name for the OLDDOUBLEVALUE field */
    public static final String OLDDOUBLEVALUE;
    /** the column name for the NEWDATEVALUE field */
    public static final String NEWDATEVALUE;
    /** the column name for the OLDDATEVALUE field */
    public static final String OLDDATEVALUE;
    /** the column name for the NEWCHARACTERVALUE field */
    public static final String NEWCHARACTERVALUE;
    /** the column name for the OLDCHARACTERVALUE field */
    public static final String OLDCHARACTERVALUE;
    /** the column name for the NEWLONGTEXTVALUE field */
    public static final String NEWLONGTEXTVALUE;
    /** the column name for the OLDLONGTEXTVALUE field */
    public static final String OLDLONGTEXTVALUE;
    /** the column name for the NEWSYSTEMOPTIONID field */
    public static final String NEWSYSTEMOPTIONID;
    /** the column name for the OLDSYSTEMOPTIONID field */
    public static final String OLDSYSTEMOPTIONID;
    /** the column name for the SYSTEMOPTIONTYPE field */
    public static final String SYSTEMOPTIONTYPE;
    /** the column name for the NEWCUSTOMOPTIONID field */
    public static final String NEWCUSTOMOPTIONID;
    /** the column name for the OLDCUSTOMOPTIONID field */
    public static final String OLDCUSTOMOPTIONID;
    /** the column name for the PARAMETERCODE field */
    public static final String PARAMETERCODE;
    /** the column name for the VALIDVALUE field */
    public static final String VALIDVALUE;
    /** the column name for the PARENTCOMMENT field */
    public static final String PARENTCOMMENT;
    /** the column name for the TIMESEDITED field */
    public static final String TIMESEDITED;
    /** the column name for the TPUUID field */
    public static final String TPUUID;

    static
    {
        DATABASE_NAME = "track";
        TABLE_NAME = "TFIELDCHANGE";

        OBJECTID = "TFIELDCHANGE.OBJECTID";
        FIELDKEY = "TFIELDCHANGE.FIELDKEY";
        HISTORYTRANSACTION = "TFIELDCHANGE.HISTORYTRANSACTION";
        NEWTEXTVALUE = "TFIELDCHANGE.NEWTEXTVALUE";
        OLDTEXTVALUE = "TFIELDCHANGE.OLDTEXTVALUE";
        NEWINTEGERVALUE = "TFIELDCHANGE.NEWINTEGERVALUE";
        OLDINTEGERVALUE = "TFIELDCHANGE.OLDINTEGERVALUE";
        NEWDOUBLEVALUE = "TFIELDCHANGE.NEWDOUBLEVALUE";
        OLDDOUBLEVALUE = "TFIELDCHANGE.OLDDOUBLEVALUE";
        NEWDATEVALUE = "TFIELDCHANGE.NEWDATEVALUE";
        OLDDATEVALUE = "TFIELDCHANGE.OLDDATEVALUE";
        NEWCHARACTERVALUE = "TFIELDCHANGE.NEWCHARACTERVALUE";
        OLDCHARACTERVALUE = "TFIELDCHANGE.OLDCHARACTERVALUE";
        NEWLONGTEXTVALUE = "TFIELDCHANGE.NEWLONGTEXTVALUE";
        OLDLONGTEXTVALUE = "TFIELDCHANGE.OLDLONGTEXTVALUE";
        NEWSYSTEMOPTIONID = "TFIELDCHANGE.NEWSYSTEMOPTIONID";
        OLDSYSTEMOPTIONID = "TFIELDCHANGE.OLDSYSTEMOPTIONID";
        SYSTEMOPTIONTYPE = "TFIELDCHANGE.SYSTEMOPTIONTYPE";
        NEWCUSTOMOPTIONID = "TFIELDCHANGE.NEWCUSTOMOPTIONID";
        OLDCUSTOMOPTIONID = "TFIELDCHANGE.OLDCUSTOMOPTIONID";
        PARAMETERCODE = "TFIELDCHANGE.PARAMETERCODE";
        VALIDVALUE = "TFIELDCHANGE.VALIDVALUE";
        PARENTCOMMENT = "TFIELDCHANGE.PARENTCOMMENT";
        TIMESEDITED = "TFIELDCHANGE.TIMESEDITED";
        TPUUID = "TFIELDCHANGE.TPUUID";
        if (Torque.isInit())
        {
            try
            {
                Torque.getMapBuilder(TFieldChangeMapBuilder.CLASS_NAME);
            }
            catch (TorqueException e)
            {
                log.error("Could not initialize Peer", e);
                throw new TorqueRuntimeException(e);
            }
        }
        else
        {
            Torque.registerMapBuilder(TFieldChangeMapBuilder.CLASS_NAME);
        }
    }
 
    /** number of columns for this peer */
    public static final int numColumns =  25;

    /** A class that can be returned by this peer. */
    protected static final String CLASSNAME_DEFAULT =
        "com.aurel.track.persist.TFieldChange";

    /** A class that can be returned by this peer. */
    protected static final Class CLASS_DEFAULT = initClass(CLASSNAME_DEFAULT);

    /**
     * Class object initialization method.
     *
     * @param className name of the class to initialize
     * @return the initialized class
     */
    private static Class initClass(String className)
    {
        Class c = null;
        try
        {
            c = Class.forName(className);
        }
        catch (Throwable t)
        {
            log.error("A FATAL ERROR has occurred which should not "
                + "have happened under any circumstance.  Please notify "
                + "the Torque developers <torque-dev@db.apache.org> "
                + "and give as many details as possible (including the error "
                + "stack trace).", t);

            // Error objects should always be propagated.
            if (t instanceof Error)
            {
                throw (Error) t.fillInStackTrace();
            }
        }
        return c;
    }

    /**
     * Get the list of objects for a ResultSet.  Please not that your
     * resultset MUST return columns in the right order.  You can use
     * getFieldNames() in BaseObject to get the correct sequence.
     *
     * @param results the ResultSet
     * @return the list of objects
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TFieldChange> resultSet2Objects(java.sql.ResultSet results)
            throws TorqueException
    {
        try
        {
            QueryDataSet qds = null;
            List<Record> rows = null;
            try
            {
                qds = new QueryDataSet(results);
                rows = getSelectResults(qds);
            }
            finally
            {
                if (qds != null)
                {
                    qds.close();
                }
            }

            return populateObjects(rows);
        }
        catch (SQLException e)
        {
            throw new TorqueException(e);
        }
        catch (DataSetException e)
        {
            throw new TorqueException(e);
        }
    }



    /**
     * Method to do inserts.
     *
     * @param criteria object used to create the INSERT statement.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static ObjectKey doInsert(Criteria criteria)
        throws TorqueException
    {
        return BaseTFieldChangePeer
            .doInsert(criteria, (Connection) null);
    }

    /**
     * Method to do inserts.  This method is to be used during a transaction,
     * otherwise use the doInsert(Criteria) method.  It will take care of
     * the connection details internally.
     *
     * @param criteria object used to create the INSERT statement.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static ObjectKey doInsert(Criteria criteria, Connection con)
        throws TorqueException
    {
        correctBooleans(criteria);

        setDbName(criteria);

        if (con == null)
        {
            return BasePeer.doInsert(criteria);
        }
        else
        {
            return BasePeer.doInsert(criteria, con);
        }
    }

    /**
     * Add all the columns needed to create a new object.
     *
     * @param criteria object containing the columns to add.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void addSelectColumns(Criteria criteria)
            throws TorqueException
    {
        criteria.addSelectColumn(OBJECTID);
        criteria.addSelectColumn(FIELDKEY);
        criteria.addSelectColumn(HISTORYTRANSACTION);
        criteria.addSelectColumn(NEWTEXTVALUE);
        criteria.addSelectColumn(OLDTEXTVALUE);
        criteria.addSelectColumn(NEWINTEGERVALUE);
        criteria.addSelectColumn(OLDINTEGERVALUE);
        criteria.addSelectColumn(NEWDOUBLEVALUE);
        criteria.addSelectColumn(OLDDOUBLEVALUE);
        criteria.addSelectColumn(NEWDATEVALUE);
        criteria.addSelectColumn(OLDDATEVALUE);
        criteria.addSelectColumn(NEWCHARACTERVALUE);
        criteria.addSelectColumn(OLDCHARACTERVALUE);
        criteria.addSelectColumn(NEWLONGTEXTVALUE);
        criteria.addSelectColumn(OLDLONGTEXTVALUE);
        criteria.addSelectColumn(NEWSYSTEMOPTIONID);
        criteria.addSelectColumn(OLDSYSTEMOPTIONID);
        criteria.addSelectColumn(SYSTEMOPTIONTYPE);
        criteria.addSelectColumn(NEWCUSTOMOPTIONID);
        criteria.addSelectColumn(OLDCUSTOMOPTIONID);
        criteria.addSelectColumn(PARAMETERCODE);
        criteria.addSelectColumn(VALIDVALUE);
        criteria.addSelectColumn(PARENTCOMMENT);
        criteria.addSelectColumn(TIMESEDITED);
        criteria.addSelectColumn(TPUUID);
    }

    /**
     * changes the boolean values in the criteria to the appropriate type,
     * whenever a booleanchar or booleanint column is involved.
     * This enables the user to create criteria using Boolean values
     * for booleanchar or booleanint columns
     * @param criteria the criteria in which the boolean values should be corrected
     * @throws TorqueException if the database map for the criteria cannot be 
               obtained.
     */
    public static void correctBooleans(Criteria criteria) throws TorqueException
    {
        correctBooleans(criteria, getTableMap());
    }

    /**
     * Create a new object of type cls from a resultset row starting
     * from a specified offset.  This is done so that you can select
     * other rows than just those needed for this object.  You may
     * for example want to create two objects from the same row.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static TFieldChange row2Object(Record row,
                                             int offset,
                                             Class cls)
        throws TorqueException
    {
        try
        {
            TFieldChange obj = (TFieldChange) cls.newInstance();
            TFieldChangePeer.populateObject(row, offset, obj);
                obj.setModified(false);
            obj.setNew(false);

            return obj;
        }
        catch (InstantiationException e)
        {
            throw new TorqueException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new TorqueException(e);
        }
    }

    /**
     * Populates an object from a resultset row starting
     * from a specified offset.  This is done so that you can select
     * other rows than just those needed for this object.  You may
     * for example want to create two objects from the same row.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void populateObject(Record row,
                                      int offset,
                                      TFieldChange obj)
        throws TorqueException
    {
        try
        {
            obj.setObjectID(row.getValue(offset + 0).asIntegerObj());
            obj.setFieldKey(row.getValue(offset + 1).asIntegerObj());
            obj.setHistoryTransaction(row.getValue(offset + 2).asIntegerObj());
            obj.setNewTextValue(row.getValue(offset + 3).asString());
            obj.setOldTextValue(row.getValue(offset + 4).asString());
            obj.setNewIntegerValue(row.getValue(offset + 5).asIntegerObj());
            obj.setOldIntegerValue(row.getValue(offset + 6).asIntegerObj());
            obj.setNewDoubleValue(row.getValue(offset + 7).asDoubleObj());
            obj.setOldDoubleValue(row.getValue(offset + 8).asDoubleObj());
            obj.setNewDateValue(row.getValue(offset + 9).asUtilDate());
            obj.setOldDateValue(row.getValue(offset + 10).asUtilDate());
            obj.setNewCharacterValue(row.getValue(offset + 11).asString());
            obj.setOldCharacterValue(row.getValue(offset + 12).asString());
            obj.setNewLongTextValue(row.getValue(offset + 13).asString());
            obj.setOldLongTextValue(row.getValue(offset + 14).asString());
            obj.setNewSystemOptionID(row.getValue(offset + 15).asIntegerObj());
            obj.setOldSystemOptionID(row.getValue(offset + 16).asIntegerObj());
            obj.setSystemOptionType(row.getValue(offset + 17).asIntegerObj());
            obj.setNewCustomOptionID(row.getValue(offset + 18).asIntegerObj());
            obj.setOldCustomOptionID(row.getValue(offset + 19).asIntegerObj());
            obj.setParameterCode(row.getValue(offset + 20).asIntegerObj());
            obj.setValidValue(row.getValue(offset + 21).asIntegerObj());
            obj.setParentComment(row.getValue(offset + 22).asIntegerObj());
            obj.setTimesEdited(row.getValue(offset + 23).asIntegerObj());
            obj.setUuid(row.getValue(offset + 24).asString());
        }
        catch (DataSetException e)
        {
            throw new TorqueException(e);
        }
    }

    /**
     * Method to do selects.
     *
     * @param criteria object used to create the SELECT statement.
     * @return List of selected Objects
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TFieldChange> doSelect(Criteria criteria) throws TorqueException
    {
        return populateObjects(doSelectVillageRecords(criteria));
    }

    /**
     * Method to do selects within a transaction.
     *
     * @param criteria object used to create the SELECT statement.
     * @param con the connection to use
     * @return List of selected Objects
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TFieldChange> doSelect(Criteria criteria, Connection con)
        throws TorqueException
    {
        return populateObjects(doSelectVillageRecords(criteria, con));
    }

    /**
     * Grabs the raw Village records to be formed into objects.
     * This method handles connections internally.  The Record objects
     * returned by this method should be considered readonly.  Do not
     * alter the data and call save(), your results may vary, but are
     * certainly likely to result in hard to track MT bugs.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<Record> doSelectVillageRecords(Criteria criteria)
        throws TorqueException
    {
        return BaseTFieldChangePeer
            .doSelectVillageRecords(criteria, (Connection) null);
    }

    /**
     * Grabs the raw Village records to be formed into objects.
     * This method should be used for transactions
     *
     * @param criteria object used to create the SELECT statement.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<Record> doSelectVillageRecords(Criteria criteria, Connection con)
        throws TorqueException
    {
        if (criteria.getSelectColumns().size() == 0)
        {
            addSelectColumns(criteria);
        }
        correctBooleans(criteria);

        setDbName(criteria);

        // BasePeer returns a List of Value (Village) arrays.  The array
        // order follows the order columns were placed in the Select clause.
        if (con == null)
        {
            return BasePeer.doSelect(criteria);
        }
        else
        {
            return BasePeer.doSelect(criteria, con);
        }
    }

    /**
     * The returned List will contain objects of the default type or
     * objects that inherit from the default.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TFieldChange> populateObjects(List<Record> records)
        throws TorqueException
    {
        List<TFieldChange> results = new ArrayList<TFieldChange>(records.size());

        // populate the object(s)
        for (int i = 0; i < records.size(); i++)
        {
            Record row =  records.get(i);
            results.add(TFieldChangePeer.row2Object(row, 1,
                TFieldChangePeer.getOMClass()));
        }
        return results;
    }
 

    /**
     * The class that the Peer will make instances of.
     * If the BO is abstract then you must implement this method
     * in the BO.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static Class getOMClass()
        throws TorqueException
    {
        return CLASS_DEFAULT;
    }

    /**
     * Method to do updates.
     *
     * @param criteria object containing data that is used to create the UPDATE
     *        statement.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(Criteria criteria) throws TorqueException
    {
         BaseTFieldChangePeer
            .doUpdate(criteria, (Connection) null);
    }

    /**
     * Method to do updates.  This method is to be used during a transaction,
     * otherwise use the doUpdate(Criteria) method.  It will take care of
     * the connection details internally.
     *
     * @param criteria object containing data that is used to create the UPDATE
     *        statement.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(Criteria criteria, Connection con)
        throws TorqueException
    {
        Criteria selectCriteria = new Criteria(DATABASE_NAME, 2);
        correctBooleans(criteria);


         selectCriteria.put(OBJECTID, criteria.remove(OBJECTID));

























        setDbName(criteria);

        if (con == null)
        {
            BasePeer.doUpdate(selectCriteria, criteria);
        }
        else
        {
            BasePeer.doUpdate(selectCriteria, criteria, con);
        }
    }

    /**
     * Method to do deletes.
     *
     * @param criteria object containing data that is used DELETE from database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
     public static void doDelete(Criteria criteria) throws TorqueException
     {
         TFieldChangePeer
            .doDelete(criteria, (Connection) null);
     }

    /**
     * Method to do deletes.  This method is to be used during a transaction,
     * otherwise use the doDelete(Criteria) method.  It will take care of
     * the connection details internally.
     *
     * @param criteria object containing data that is used DELETE from database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
     public static void doDelete(Criteria criteria, Connection con)
        throws TorqueException
     {
        correctBooleans(criteria);

        setDbName(criteria);

        if (con == null)
        {
            BasePeer.doDelete(criteria, TABLE_NAME);
        }
        else
        {
            BasePeer.doDelete(criteria, TABLE_NAME, con);
        }
     }

    /**
     * Method to do selects
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TFieldChange> doSelect(TFieldChange obj) throws TorqueException
    {
        return doSelect(buildSelectCriteria(obj));
    }

    /**
     * Method to do inserts
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(TFieldChange obj) throws TorqueException
    {
        obj.setPrimaryKey(doInsert(buildCriteria(obj)));
        obj.setNew(false);
        obj.setModified(false);
    }

    /**
     * @param obj the data object to update in the database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(TFieldChange obj) throws TorqueException
    {
        doUpdate(buildCriteria(obj));
        obj.setModified(false);
    }

    /**
     * @param obj the data object to delete in the database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(TFieldChange obj) throws TorqueException
    {
        doDelete(buildSelectCriteria(obj));
    }

    /**
     * Method to do inserts.  This method is to be used during a transaction,
     * otherwise use the doInsert(TFieldChange) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to insert into the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(TFieldChange obj, Connection con)
        throws TorqueException
    {
        obj.setPrimaryKey(doInsert(buildCriteria(obj), con));
        obj.setNew(false);
        obj.setModified(false);
    }

    /**
     * Method to do update.  This method is to be used during a transaction,
     * otherwise use the doUpdate(TFieldChange) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to update in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(TFieldChange obj, Connection con)
        throws TorqueException
    {
        doUpdate(buildCriteria(obj), con);
        obj.setModified(false);
    }

    /**
     * Method to delete.  This method is to be used during a transaction,
     * otherwise use the doDelete(TFieldChange) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to delete in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(TFieldChange obj, Connection con)
        throws TorqueException
    {
        doDelete(buildSelectCriteria(obj), con);
    }

    /**
     * Method to do deletes.
     *
     * @param pk ObjectKey that is used DELETE from database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(ObjectKey pk) throws TorqueException
    {
        BaseTFieldChangePeer
           .doDelete(pk, (Connection) null);
    }

    /**
     * Method to delete.  This method is to be used during a transaction,
     * otherwise use the doDelete(ObjectKey) method.  It will take
     * care of the connection details internally.
     *
     * @param pk the primary key for the object to delete in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(ObjectKey pk, Connection con)
        throws TorqueException
    {
        doDelete(buildCriteria(pk), con);
    }

    /** Build a Criteria object from an ObjectKey */
    public static Criteria buildCriteria( ObjectKey pk )
    {
        Criteria criteria = new Criteria();
            criteria.add(OBJECTID, pk);
        return criteria;
     }

    /** Build a Criteria object from the data object for this peer */
    public static Criteria buildCriteria( TFieldChange obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
        if (!obj.isNew())
        criteria.add(OBJECTID, obj.getObjectID());
        criteria.add(FIELDKEY, obj.getFieldKey());
        criteria.add(HISTORYTRANSACTION, obj.getHistoryTransaction());
        criteria.add(NEWTEXTVALUE, obj.getNewTextValue());
        criteria.add(OLDTEXTVALUE, obj.getOldTextValue());
        criteria.add(NEWINTEGERVALUE, obj.getNewIntegerValue());
        criteria.add(OLDINTEGERVALUE, obj.getOldIntegerValue());
        criteria.add(NEWDOUBLEVALUE, obj.getNewDoubleValue());
        criteria.add(OLDDOUBLEVALUE, obj.getOldDoubleValue());
        criteria.add(NEWDATEVALUE, obj.getNewDateValue());
        criteria.add(OLDDATEVALUE, obj.getOldDateValue());
        criteria.add(NEWCHARACTERVALUE, obj.getNewCharacterValue());
        criteria.add(OLDCHARACTERVALUE, obj.getOldCharacterValue());
        criteria.add(NEWLONGTEXTVALUE, obj.getNewLongTextValue());
        criteria.add(OLDLONGTEXTVALUE, obj.getOldLongTextValue());
        criteria.add(NEWSYSTEMOPTIONID, obj.getNewSystemOptionID());
        criteria.add(OLDSYSTEMOPTIONID, obj.getOldSystemOptionID());
        criteria.add(SYSTEMOPTIONTYPE, obj.getSystemOptionType());
        criteria.add(NEWCUSTOMOPTIONID, obj.getNewCustomOptionID());
        criteria.add(OLDCUSTOMOPTIONID, obj.getOldCustomOptionID());
        criteria.add(PARAMETERCODE, obj.getParameterCode());
        criteria.add(VALIDVALUE, obj.getValidValue());
        criteria.add(PARENTCOMMENT, obj.getParentComment());
        criteria.add(TIMESEDITED, obj.getTimesEdited());
        criteria.add(TPUUID, obj.getUuid());
        return criteria;
    }

    /** Build a Criteria object from the data object for this peer, skipping all binary columns */
    public static Criteria buildSelectCriteria( TFieldChange obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
        if (!obj.isNew())
        {
            criteria.add(OBJECTID, obj.getObjectID());
        }
            criteria.add(FIELDKEY, obj.getFieldKey());
            criteria.add(HISTORYTRANSACTION, obj.getHistoryTransaction());
            criteria.add(NEWTEXTVALUE, obj.getNewTextValue());
            criteria.add(OLDTEXTVALUE, obj.getOldTextValue());
            criteria.add(NEWINTEGERVALUE, obj.getNewIntegerValue());
            criteria.add(OLDINTEGERVALUE, obj.getOldIntegerValue());
            criteria.add(NEWDOUBLEVALUE, obj.getNewDoubleValue());
            criteria.add(OLDDOUBLEVALUE, obj.getOldDoubleValue());
            criteria.add(NEWDATEVALUE, obj.getNewDateValue());
            criteria.add(OLDDATEVALUE, obj.getOldDateValue());
            criteria.add(NEWCHARACTERVALUE, obj.getNewCharacterValue());
            criteria.add(OLDCHARACTERVALUE, obj.getOldCharacterValue());
            criteria.add(NEWLONGTEXTVALUE, obj.getNewLongTextValue());
            criteria.add(OLDLONGTEXTVALUE, obj.getOldLongTextValue());
            criteria.add(NEWSYSTEMOPTIONID, obj.getNewSystemOptionID());
            criteria.add(OLDSYSTEMOPTIONID, obj.getOldSystemOptionID());
            criteria.add(SYSTEMOPTIONTYPE, obj.getSystemOptionType());
            criteria.add(NEWCUSTOMOPTIONID, obj.getNewCustomOptionID());
            criteria.add(OLDCUSTOMOPTIONID, obj.getOldCustomOptionID());
            criteria.add(PARAMETERCODE, obj.getParameterCode());
            criteria.add(VALIDVALUE, obj.getValidValue());
            criteria.add(PARENTCOMMENT, obj.getParentComment());
            criteria.add(TIMESEDITED, obj.getTimesEdited());
            criteria.add(TPUUID, obj.getUuid());
        return criteria;
    }
 

    /**
     * Retrieve a single object by pk
     *
     * @param pk the primary key
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @throws NoRowsException Primary key was not found in database.
     * @throws TooManyRowsException Primary key was not found in database.
     */
    public static TFieldChange retrieveByPK(Integer pk)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        return retrieveByPK(SimpleKey.keyFor(pk));
    }

    /**
     * Retrieve a single object by pk
     *
     * @param pk the primary key
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @throws NoRowsException Primary key was not found in database.
     * @throws TooManyRowsException Primary key was not found in database.
     */
    public static TFieldChange retrieveByPK(Integer pk, Connection con)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        return retrieveByPK(SimpleKey.keyFor(pk), con);
    }

    /**
     * Retrieve a single object by pk
     *
     * @param pk the primary key
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @throws NoRowsException Primary key was not found in database.
     * @throws TooManyRowsException Primary key was not found in database.
     */
    public static TFieldChange retrieveByPK(ObjectKey pk)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        Connection db = null;
        TFieldChange retVal = null;
        try
        {
            db = Torque.getConnection(DATABASE_NAME);
            retVal = retrieveByPK(pk, db);
        }
        finally
        {
            Torque.closeConnection(db);
        }
        return retVal;
    }

    /**
     * Retrieve a single object by pk
     *
     * @param pk the primary key
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @throws NoRowsException Primary key was not found in database.
     * @throws TooManyRowsException Primary key was not found in database.
     */
    public static TFieldChange retrieveByPK(ObjectKey pk, Connection con)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        Criteria criteria = buildCriteria(pk);
        List<TFieldChange> v = doSelect(criteria, con);
        if (v.size() == 0)
        {
            throw new NoRowsException("Failed to select a row.");
        }
        else if (v.size() > 1)
        {
            throw new TooManyRowsException("Failed to select only one row.");
        }
        else
        {
            return (TFieldChange)v.get(0);
        }
    }

    /**
     * Retrieve a multiple objects by pk
     *
     * @param pks List of primary keys
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TFieldChange> retrieveByPKs(List<ObjectKey> pks)
        throws TorqueException
    {
        Connection db = null;
        List<TFieldChange> retVal = null;
        try
        {
           db = Torque.getConnection(DATABASE_NAME);
           retVal = retrieveByPKs(pks, db);
        }
        finally
        {
            Torque.closeConnection(db);
        }
        return retVal;
    }

    /**
     * Retrieve a multiple objects by pk
     *
     * @param pks List of primary keys
     * @param dbcon the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TFieldChange> retrieveByPKs( List<ObjectKey> pks, Connection dbcon )
        throws TorqueException
    {
        List<TFieldChange> objs = null;
        if (pks == null || pks.size() == 0)
        {
            objs = new LinkedList<TFieldChange>();
        }
        else
        {
            Criteria criteria = new Criteria();
            criteria.addIn( OBJECTID, pks );
        objs = doSelect(criteria, dbcon);
        }
        return objs;
    }

 








    /**
     * selects a collection of TFieldChange objects pre-filled with their
     * THistoryTransaction objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinTHistoryTransaction(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTHistoryTransaction(criteria, null);
    }

    /**
     * selects a collection of TFieldChange objects pre-filled with their
     * THistoryTransaction objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinTHistoryTransaction(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TFieldChangePeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        THistoryTransactionPeer.addSelectColumns(criteria);

        criteria.addJoin(TFieldChangePeer.HISTORYTRANSACTION,
            THistoryTransactionPeer.OBJECTID);

        correctBooleans(criteria);

        List<Record> rows;
        if (conn == null)
        {
            rows = BasePeer.doSelect(criteria);
        }
        else
        {
            rows = BasePeer.doSelect(criteria,conn);
        }

        List<TFieldChange> results = new ArrayList<TFieldChange>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TFieldChangePeer.getOMClass();
            TFieldChange obj1 = (TFieldChange) TFieldChangePeer
                .row2Object(row, 1, omClass);
             omClass = THistoryTransactionPeer.getOMClass();
            THistoryTransaction obj2 = (THistoryTransaction) THistoryTransactionPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TFieldChange temp_obj1 =  results.get(j);
                THistoryTransaction temp_obj2 = (THistoryTransaction) temp_obj1.getTHistoryTransaction();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTFieldChange(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTFieldChanges();
                obj2.addTFieldChange(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TFieldChange objects pre-filled with their
     * TOption objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinTOptionRelatedByNewCustomOptionID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTOptionRelatedByNewCustomOptionID(criteria, null);
    }

    /**
     * selects a collection of TFieldChange objects pre-filled with their
     * TOption objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinTOptionRelatedByNewCustomOptionID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TFieldChangePeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TOptionPeer.addSelectColumns(criteria);

        criteria.addJoin(TFieldChangePeer.NEWCUSTOMOPTIONID,
            TOptionPeer.OBJECTID);

        correctBooleans(criteria);

        List<Record> rows;
        if (conn == null)
        {
            rows = BasePeer.doSelect(criteria);
        }
        else
        {
            rows = BasePeer.doSelect(criteria,conn);
        }

        List<TFieldChange> results = new ArrayList<TFieldChange>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TFieldChangePeer.getOMClass();
            TFieldChange obj1 = (TFieldChange) TFieldChangePeer
                .row2Object(row, 1, omClass);
             omClass = TOptionPeer.getOMClass();
            TOption obj2 = (TOption) TOptionPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TFieldChange temp_obj1 =  results.get(j);
                TOption temp_obj2 = (TOption) temp_obj1.getTOptionRelatedByNewCustomOptionID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTFieldChangeRelatedByNewCustomOptionID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTFieldChangesRelatedByNewCustomOptionID();
                obj2.addTFieldChangeRelatedByNewCustomOptionID(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TFieldChange objects pre-filled with their
     * TOption objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinTOptionRelatedByOldCustomOptionID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTOptionRelatedByOldCustomOptionID(criteria, null);
    }

    /**
     * selects a collection of TFieldChange objects pre-filled with their
     * TOption objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinTOptionRelatedByOldCustomOptionID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TFieldChangePeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TOptionPeer.addSelectColumns(criteria);

        criteria.addJoin(TFieldChangePeer.OLDCUSTOMOPTIONID,
            TOptionPeer.OBJECTID);

        correctBooleans(criteria);

        List<Record> rows;
        if (conn == null)
        {
            rows = BasePeer.doSelect(criteria);
        }
        else
        {
            rows = BasePeer.doSelect(criteria,conn);
        }

        List<TFieldChange> results = new ArrayList<TFieldChange>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TFieldChangePeer.getOMClass();
            TFieldChange obj1 = (TFieldChange) TFieldChangePeer
                .row2Object(row, 1, omClass);
             omClass = TOptionPeer.getOMClass();
            TOption obj2 = (TOption) TOptionPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TFieldChange temp_obj1 =  results.get(j);
                TOption temp_obj2 = (TOption) temp_obj1.getTOptionRelatedByOldCustomOptionID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTFieldChangeRelatedByOldCustomOptionID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTFieldChangesRelatedByOldCustomOptionID();
                obj2.addTFieldChangeRelatedByOldCustomOptionID(obj1);
            }
            results.add(obj1);
        }
        return results;
    }







    /**
     * selects a collection of TFieldChange objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinAllExceptTHistoryTransaction(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTHistoryTransaction(criteria, null);
    }

    /**
     * selects a collection of TFieldChange objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinAllExceptTHistoryTransaction(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;


        TOptionPeer.addSelectColumns(criteria);
        criteria.addJoin(TFieldChangePeer.NEWCUSTOMOPTIONID, TOptionPeer.OBJECTID);
        int offset3 = offset2 + TOptionPeer.numColumns;

        TOptionPeer.addSelectColumns(criteria);
        criteria.addJoin(TFieldChangePeer.OLDCUSTOMOPTIONID, TOptionPeer.OBJECTID);
        int offset4 = offset3 + TOptionPeer.numColumns;

        correctBooleans(criteria);

        List<Record> rows;
        if (conn == null)
        {
            rows = BasePeer.doSelect(criteria);
        }
        else
        {
            rows = BasePeer.doSelect(criteria,conn);
        }

        List<TFieldChange> results = new ArrayList<TFieldChange>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TFieldChangePeer.getOMClass();
            TFieldChange obj1 = (TFieldChange) TFieldChangePeer
                .row2Object(row, 1, omClass);





             omClass = TOptionPeer.getOMClass();
            TOption obj2 = (TOption) TOptionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TFieldChange temp_obj1 =  results.get(j);
                TOption temp_obj2 = (TOption) temp_obj1.getTOptionRelatedByNewCustomOptionID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTFieldChangeRelatedByNewCustomOptionID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTFieldChangesRelatedByNewCustomOptionID();
                obj2.addTFieldChangeRelatedByNewCustomOptionID(obj1);
            }




             omClass = TOptionPeer.getOMClass();
            TOption obj3 = (TOption) TOptionPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TFieldChange temp_obj1 =  results.get(j);
                TOption temp_obj3 = (TOption) temp_obj1.getTOptionRelatedByOldCustomOptionID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTFieldChangeRelatedByOldCustomOptionID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTFieldChangesRelatedByOldCustomOptionID();
                obj3.addTFieldChangeRelatedByOldCustomOptionID(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TFieldChange objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinAllExceptTFieldChange(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTFieldChange(criteria, null);
    }

    /**
     * selects a collection of TFieldChange objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinAllExceptTFieldChange(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        THistoryTransactionPeer.addSelectColumns(criteria);
        criteria.addJoin(TFieldChangePeer.HISTORYTRANSACTION, THistoryTransactionPeer.OBJECTID);
        int offset3 = offset2 + THistoryTransactionPeer.numColumns;

        TOptionPeer.addSelectColumns(criteria);
        criteria.addJoin(TFieldChangePeer.NEWCUSTOMOPTIONID, TOptionPeer.OBJECTID);
        int offset4 = offset3 + TOptionPeer.numColumns;

        TOptionPeer.addSelectColumns(criteria);
        criteria.addJoin(TFieldChangePeer.OLDCUSTOMOPTIONID, TOptionPeer.OBJECTID);

        correctBooleans(criteria);

        List<Record> rows;
        if (conn == null)
        {
            rows = BasePeer.doSelect(criteria);
        }
        else
        {
            rows = BasePeer.doSelect(criteria,conn);
        }

        List<TFieldChange> results = new ArrayList<TFieldChange>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TFieldChangePeer.getOMClass();
            TFieldChange obj1 = (TFieldChange) TFieldChangePeer
                .row2Object(row, 1, omClass);




             omClass = THistoryTransactionPeer.getOMClass();
            THistoryTransaction obj2 = (THistoryTransaction) THistoryTransactionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TFieldChange temp_obj1 =  results.get(j);
                THistoryTransaction temp_obj2 = (THistoryTransaction) temp_obj1.getTHistoryTransaction();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTFieldChange(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTFieldChanges();
                obj2.addTFieldChange(obj1);
            }




             omClass = TOptionPeer.getOMClass();
            TOption obj3 = (TOption) TOptionPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TFieldChange temp_obj1 =  results.get(j);
                TOption temp_obj3 = (TOption) temp_obj1.getTOptionRelatedByNewCustomOptionID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTFieldChangeRelatedByNewCustomOptionID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTFieldChangesRelatedByNewCustomOptionID();
                obj3.addTFieldChangeRelatedByNewCustomOptionID(obj1);
            }




             omClass = TOptionPeer.getOMClass();
            TOption obj4 = (TOption) TOptionPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TFieldChange temp_obj1 =  results.get(j);
                TOption temp_obj4 = (TOption) temp_obj1.getTOptionRelatedByOldCustomOptionID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTFieldChangeRelatedByOldCustomOptionID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTFieldChangesRelatedByOldCustomOptionID();
                obj4.addTFieldChangeRelatedByOldCustomOptionID(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TFieldChange objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinAllExceptTOptionRelatedByNewCustomOptionID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTOptionRelatedByNewCustomOptionID(criteria, null);
    }

    /**
     * selects a collection of TFieldChange objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinAllExceptTOptionRelatedByNewCustomOptionID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        THistoryTransactionPeer.addSelectColumns(criteria);
        criteria.addJoin(TFieldChangePeer.HISTORYTRANSACTION, THistoryTransactionPeer.OBJECTID);
        int offset3 = offset2 + THistoryTransactionPeer.numColumns;



        correctBooleans(criteria);

        List<Record> rows;
        if (conn == null)
        {
            rows = BasePeer.doSelect(criteria);
        }
        else
        {
            rows = BasePeer.doSelect(criteria,conn);
        }

        List<TFieldChange> results = new ArrayList<TFieldChange>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TFieldChangePeer.getOMClass();
            TFieldChange obj1 = (TFieldChange) TFieldChangePeer
                .row2Object(row, 1, omClass);




             omClass = THistoryTransactionPeer.getOMClass();
            THistoryTransaction obj2 = (THistoryTransaction) THistoryTransactionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TFieldChange temp_obj1 =  results.get(j);
                THistoryTransaction temp_obj2 = (THistoryTransaction) temp_obj1.getTHistoryTransaction();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTFieldChange(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTFieldChanges();
                obj2.addTFieldChange(obj1);
            }


            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TFieldChange objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinAllExceptTOptionRelatedByOldCustomOptionID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTOptionRelatedByOldCustomOptionID(criteria, null);
    }

    /**
     * selects a collection of TFieldChange objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TFieldChangePeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TFieldChange> doSelectJoinAllExceptTOptionRelatedByOldCustomOptionID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        THistoryTransactionPeer.addSelectColumns(criteria);
        criteria.addJoin(TFieldChangePeer.HISTORYTRANSACTION, THistoryTransactionPeer.OBJECTID);
        int offset3 = offset2 + THistoryTransactionPeer.numColumns;



        correctBooleans(criteria);

        List<Record> rows;
        if (conn == null)
        {
            rows = BasePeer.doSelect(criteria);
        }
        else
        {
            rows = BasePeer.doSelect(criteria,conn);
        }

        List<TFieldChange> results = new ArrayList<TFieldChange>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TFieldChangePeer.getOMClass();
            TFieldChange obj1 = (TFieldChange) TFieldChangePeer
                .row2Object(row, 1, omClass);




             omClass = THistoryTransactionPeer.getOMClass();
            THistoryTransaction obj2 = (THistoryTransaction) THistoryTransactionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TFieldChange temp_obj1 =  results.get(j);
                THistoryTransaction temp_obj2 = (THistoryTransaction) temp_obj1.getTHistoryTransaction();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTFieldChange(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTFieldChanges();
                obj2.addTFieldChange(obj1);
            }


            results.add(obj1);
        }
        return results;
    }


    /**
     * Returns the TableMap related to this peer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static TableMap getTableMap()
        throws TorqueException
    {
        return Torque.getDatabaseMap(DATABASE_NAME).getTable(TABLE_NAME);
    }
 
    private static void setDbName(Criteria crit)
    {
        // Set the correct dbName if it has not been overridden
        // crit.getDbName will return the same object if not set to
        // another value so == check is okay and faster
        if (crit.getDbName() == Torque.getDefaultDB())
        {
            crit.setDbName(DATABASE_NAME);
        }
    }
    

    // The following methods wrap some methods in BasePeer
    // to have more support for Java5 generic types in the Peer
    
    /**
     * Utility method which executes a given sql statement.  This
     * method should be used for select statements only.  Use
     * executeStatement for update, insert, and delete operations.
     *
     * @param queryString A String with the sql statement to execute.
     * @return List of Record objects.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @see org.apache.torque.util.BasePeer#executeQuery(String)
     */
    public static List<Record> executeQuery(String queryString) throws TorqueException
    {
        return BasePeer.executeQuery(queryString);
    }

    /**
     * Utility method which executes a given sql statement.  This
     * method should be used for select statements only.  Use
     * executeStatement for update, insert, and delete operations.
     *
     * @param queryString A String with the sql statement to execute.
     * @param dbName The database to connect to.
     * @return List of Record objects.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @see org.apache.torque.util.BasePeer#executeQuery(String,String)
     */
    public static List<Record> executeQuery(String queryString, String dbName)
        throws TorqueException
    {
        return BasePeer.executeQuery(queryString,dbName);
    }
    

    /**
     * Method for performing a SELECT.  Returns all results.
     *
     * @param queryString A String with the sql statement to execute.
     * @param dbName The database to connect to.
     * @param singleRecord Whether or not we want to select only a
     * single record.
     * @return List of Record objects.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @see org.apache.torque.util.BasePeer#executeQuery(String,String,boolean)
     */
    public static List<Record> executeQuery(
        String queryString,
        String dbName,
        boolean singleRecord)
        throws TorqueException
    {
        return BasePeer.executeQuery(queryString,dbName,singleRecord);
    }

    /**
     * Method for performing a SELECT.  Returns all results.
     *
     * @param queryString A String with the sql statement to execute.
     * @param singleRecord Whether or not we want to select only a
     * single record.
     * @param con A Connection.
     * @return List of Record objects.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @see org.apache.torque.util.BasePeer#executeQuery(String,boolean,Connection)
     */
    public static List<Record> executeQuery(
        String queryString,
        boolean singleRecord,
        Connection con)
        throws TorqueException
    {
        return BasePeer.executeQuery(queryString,singleRecord,con);
    }

    /**
     * Method for performing a SELECT.
     *
     * @param queryString A String with the sql statement to execute.
     * @param start The first row to return.
     * @param numberOfResults The number of rows to return.
     * @param dbName The database to connect to.
     * @param singleRecord Whether or not we want to select only a
     * single record.
     * @return List of Record objects.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @see org.apache.torque.util.BasePeer#executeQuery(String,int,int,String,boolean)
     */
    public static List<Record> executeQuery(
        String queryString,
        int start,
        int numberOfResults,
        String dbName,
        boolean singleRecord)
        throws TorqueException
    {
        return BasePeer.executeQuery(queryString,start,numberOfResults,dbName,singleRecord);
    }

    /**
     * Method for performing a SELECT.  Returns all results.
     *
     * @param queryString A String with the sql statement to execute.
     * @param start The first row to return.
     * @param numberOfResults The number of rows to return.
     * @param singleRecord Whether or not we want to select only a
     * single record.
     * @param con A Connection.
     * @return List of Record objects.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @see org.apache.torque.util.BasePeer#executeQuery(String,int,int,String,boolean,Connection)
     */
    public static List<Record> executeQuery(
        String queryString,
        int start,
        int numberOfResults,
        boolean singleRecord,
        Connection con)
        throws TorqueException
    {
        return BasePeer.executeQuery(queryString,start,numberOfResults,singleRecord,con);
    }

    /**
     * Returns all records in a QueryDataSet as a List of Record
     * objects.  Used for functionality like util.LargeSelect.
     *
     * @see #getSelectResults(QueryDataSet, int, int, boolean)
     * @param qds the QueryDataSet
     * @return a List of Record objects
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @see org.apache.torque.util.BasePeer#getSelectResults(QueryDataSet)
     */
    public static List<Record> getSelectResults(QueryDataSet qds)
        throws TorqueException
    {
        return BasePeer.getSelectResults(qds);
    }
    
    /**
     * Returns all records in a QueryDataSet as a List of Record
     * objects.  Used for functionality like util.LargeSelect.
     *
     * @see #getSelectResults(QueryDataSet, int, int, boolean)
     * @param qds the QueryDataSet
     * @param singleRecord
     * @return a List of Record objects
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @see org.apache.torque.util.BasePeer#getSelectResults(QueryDataSet,boolean)
     */
    public static List<Record> getSelectResults(QueryDataSet qds, boolean singleRecord)
        throws TorqueException
    {
        return BasePeer.getSelectResults(qds,singleRecord);
    }
    
    /**
     * Returns numberOfResults records in a QueryDataSet as a List
     * of Record objects.  Starting at record 0.  Used for
     * functionality like util.LargeSelect.
     *
     * @see #getSelectResults(QueryDataSet, int, int, boolean)
     * @param qds the QueryDataSet
     * @param numberOfResults
     * @param singleRecord
     * @return a List of Record objects
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @see org.apache.torque.util.BasePeer#getSelectResults(QueryDataSet,int,boolean)
     */
    public static List<Record> getSelectResults(
        QueryDataSet qds,
        int numberOfResults,
        boolean singleRecord)
        throws TorqueException
    {
        return BasePeer.getSelectResults(qds,numberOfResults,singleRecord);
    }

    /**
     * Returns numberOfResults records in a QueryDataSet as a List
     * of Record objects.  Starting at record start.  Used for
     * functionality like util.LargeSelect.
     *
     * @param qds The <code>QueryDataSet</code> to extract results
     * from.
     * @param start The index from which to start retrieving
     * <code>Record</code> objects from the data set.
     * @param numberOfResults The number of results to return (or
     * <code> -1</code> for all results).
     * @param singleRecord Whether or not we want to select only a
     * single record.
     * @return A <code>List</code> of <code>Record</code> objects.
     * @exception TorqueException If any <code>Exception</code> occurs.
     * @see org.apache.torque.util.BasePeer#getSelectResults(QueryDataSet,int,int,boolean)
     */
    public static List getSelectResults(
        QueryDataSet qds,
        int start,
        int numberOfResults,
        boolean singleRecord)
        throws TorqueException
    {
        return BasePeer.getSelectResults(qds,start,numberOfResults,singleRecord);
    }

    /**
     * Performs a SQL <code>select</code> using a PreparedStatement.
     * Note: this method does not handle null criteria values.
     *
     * @param criteria
     * @param con
     * @return a List of Record objects.
     * @throws TorqueException Error performing database query.
     * @see org.apache.torque.util.BasePeer#doPSSelect(Criteria,Connection)
     */
    public static List<Record> doPSSelect(Criteria criteria, Connection con)
        throws TorqueException
    {
        return BasePeer.doPSSelect(criteria,con);
    }

    /**
     * Do a Prepared Statement select according to the given criteria
     *
     * @param criteria
     * @return a List of Record objects.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     * @see org.apache.torque.util.BasePeer#doPSSelect(Criteria)
     */
    public static List<Record> doPSSelect(Criteria criteria) throws TorqueException
    {
        return BasePeer.doPSSelect(criteria);
    }
}
