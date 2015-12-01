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
public abstract class BaseTWorkflowActivityPeer
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
     * @deprecated Torque.getMapBuilder(TWorkflowActivityMapBuilder.CLASS_NAME) instead
     */
    public static MapBuilder getMapBuilder()
        throws TorqueException
    {
        return Torque.getMapBuilder(TWorkflowActivityMapBuilder.CLASS_NAME);
    }

    /** the column name for the OBJECTID field */
    public static final String OBJECTID;
    /** the column name for the TRANSITIONACTIVITY field */
    public static final String TRANSITIONACTIVITY;
    /** the column name for the STATIONENTRYACTIVITY field */
    public static final String STATIONENTRYACTIVITY;
    /** the column name for the STATIONEXITACTIVITY field */
    public static final String STATIONEXITACTIVITY;
    /** the column name for the STATIONDOACTIVITY field */
    public static final String STATIONDOACTIVITY;
    /** the column name for the ACTIVITYTYPE field */
    public static final String ACTIVITYTYPE;
    /** the column name for the ACTIVITYPARAMS field */
    public static final String ACTIVITYPARAMS;
    /** the column name for the GROOVYSCRIPT field */
    public static final String GROOVYSCRIPT;
    /** the column name for the NEWMAN field */
    public static final String NEWMAN;
    /** the column name for the NEWRESP field */
    public static final String NEWRESP;
    /** the column name for the FIELDSETTERRELATION field */
    public static final String FIELDSETTERRELATION;
    /** the column name for the PARAMNAME field */
    public static final String PARAMNAME;
    /** the column name for the FIELDSETMODE field */
    public static final String FIELDSETMODE;
    /** the column name for the SORTORDER field */
    public static final String SORTORDER;
    /** the column name for the SLA field */
    public static final String SLA;
    /** the column name for the SCREEN field */
    public static final String SCREEN;
    /** the column name for the TPUUID field */
    public static final String TPUUID;

    static
    {
        DATABASE_NAME = "track";
        TABLE_NAME = "TWORKFLOWACTIVITY";

        OBJECTID = "TWORKFLOWACTIVITY.OBJECTID";
        TRANSITIONACTIVITY = "TWORKFLOWACTIVITY.TRANSITIONACTIVITY";
        STATIONENTRYACTIVITY = "TWORKFLOWACTIVITY.STATIONENTRYACTIVITY";
        STATIONEXITACTIVITY = "TWORKFLOWACTIVITY.STATIONEXITACTIVITY";
        STATIONDOACTIVITY = "TWORKFLOWACTIVITY.STATIONDOACTIVITY";
        ACTIVITYTYPE = "TWORKFLOWACTIVITY.ACTIVITYTYPE";
        ACTIVITYPARAMS = "TWORKFLOWACTIVITY.ACTIVITYPARAMS";
        GROOVYSCRIPT = "TWORKFLOWACTIVITY.GROOVYSCRIPT";
        NEWMAN = "TWORKFLOWACTIVITY.NEWMAN";
        NEWRESP = "TWORKFLOWACTIVITY.NEWRESP";
        FIELDSETTERRELATION = "TWORKFLOWACTIVITY.FIELDSETTERRELATION";
        PARAMNAME = "TWORKFLOWACTIVITY.PARAMNAME";
        FIELDSETMODE = "TWORKFLOWACTIVITY.FIELDSETMODE";
        SORTORDER = "TWORKFLOWACTIVITY.SORTORDER";
        SLA = "TWORKFLOWACTIVITY.SLA";
        SCREEN = "TWORKFLOWACTIVITY.SCREEN";
        TPUUID = "TWORKFLOWACTIVITY.TPUUID";
        if (Torque.isInit())
        {
            try
            {
                Torque.getMapBuilder(TWorkflowActivityMapBuilder.CLASS_NAME);
            }
            catch (TorqueException e)
            {
                log.error("Could not initialize Peer", e);
                throw new TorqueRuntimeException(e);
            }
        }
        else
        {
            Torque.registerMapBuilder(TWorkflowActivityMapBuilder.CLASS_NAME);
        }
    }
 
    /** number of columns for this peer */
    public static final int numColumns =  17;

    /** A class that can be returned by this peer. */
    protected static final String CLASSNAME_DEFAULT =
        "com.aurel.track.persist.TWorkflowActivity";

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
    public static List<TWorkflowActivity> resultSet2Objects(java.sql.ResultSet results)
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
        return BaseTWorkflowActivityPeer
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
        criteria.addSelectColumn(TRANSITIONACTIVITY);
        criteria.addSelectColumn(STATIONENTRYACTIVITY);
        criteria.addSelectColumn(STATIONEXITACTIVITY);
        criteria.addSelectColumn(STATIONDOACTIVITY);
        criteria.addSelectColumn(ACTIVITYTYPE);
        criteria.addSelectColumn(ACTIVITYPARAMS);
        criteria.addSelectColumn(GROOVYSCRIPT);
        criteria.addSelectColumn(NEWMAN);
        criteria.addSelectColumn(NEWRESP);
        criteria.addSelectColumn(FIELDSETTERRELATION);
        criteria.addSelectColumn(PARAMNAME);
        criteria.addSelectColumn(FIELDSETMODE);
        criteria.addSelectColumn(SORTORDER);
        criteria.addSelectColumn(SLA);
        criteria.addSelectColumn(SCREEN);
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
    public static TWorkflowActivity row2Object(Record row,
                                             int offset,
                                             Class cls)
        throws TorqueException
    {
        try
        {
            TWorkflowActivity obj = (TWorkflowActivity) cls.newInstance();
            TWorkflowActivityPeer.populateObject(row, offset, obj);
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
                                      TWorkflowActivity obj)
        throws TorqueException
    {
        try
        {
            obj.setObjectID(row.getValue(offset + 0).asIntegerObj());
            obj.setTransitionActivity(row.getValue(offset + 1).asIntegerObj());
            obj.setStationEntryActivity(row.getValue(offset + 2).asIntegerObj());
            obj.setStationExitActivity(row.getValue(offset + 3).asIntegerObj());
            obj.setStationDoActivity(row.getValue(offset + 4).asIntegerObj());
            obj.setActivityType(row.getValue(offset + 5).asIntegerObj());
            obj.setActivityParams(row.getValue(offset + 6).asString());
            obj.setGroovyScript(row.getValue(offset + 7).asIntegerObj());
            obj.setNewMan(row.getValue(offset + 8).asIntegerObj());
            obj.setNewResp(row.getValue(offset + 9).asIntegerObj());
            obj.setFieldSetterRelation(row.getValue(offset + 10).asIntegerObj());
            obj.setParamName(row.getValue(offset + 11).asString());
            obj.setFieldSetMode(row.getValue(offset + 12).asIntegerObj());
            obj.setSortOrder(row.getValue(offset + 13).asIntegerObj());
            obj.setSla(row.getValue(offset + 14).asIntegerObj());
            obj.setScreen(row.getValue(offset + 15).asIntegerObj());
            obj.setUuid(row.getValue(offset + 16).asString());
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
    public static List<TWorkflowActivity> doSelect(Criteria criteria) throws TorqueException
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
    public static List<TWorkflowActivity> doSelect(Criteria criteria, Connection con)
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
        return BaseTWorkflowActivityPeer
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
    public static List<TWorkflowActivity> populateObjects(List<Record> records)
        throws TorqueException
    {
        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>(records.size());

        // populate the object(s)
        for (int i = 0; i < records.size(); i++)
        {
            Record row =  records.get(i);
            results.add(TWorkflowActivityPeer.row2Object(row, 1,
                TWorkflowActivityPeer.getOMClass()));
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
         BaseTWorkflowActivityPeer
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
         TWorkflowActivityPeer
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
    public static List<TWorkflowActivity> doSelect(TWorkflowActivity obj) throws TorqueException
    {
        return doSelect(buildSelectCriteria(obj));
    }

    /**
     * Method to do inserts
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(TWorkflowActivity obj) throws TorqueException
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
    public static void doUpdate(TWorkflowActivity obj) throws TorqueException
    {
        doUpdate(buildCriteria(obj));
        obj.setModified(false);
    }

    /**
     * @param obj the data object to delete in the database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(TWorkflowActivity obj) throws TorqueException
    {
        doDelete(buildSelectCriteria(obj));
    }

    /**
     * Method to do inserts.  This method is to be used during a transaction,
     * otherwise use the doInsert(TWorkflowActivity) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to insert into the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(TWorkflowActivity obj, Connection con)
        throws TorqueException
    {
        obj.setPrimaryKey(doInsert(buildCriteria(obj), con));
        obj.setNew(false);
        obj.setModified(false);
    }

    /**
     * Method to do update.  This method is to be used during a transaction,
     * otherwise use the doUpdate(TWorkflowActivity) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to update in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(TWorkflowActivity obj, Connection con)
        throws TorqueException
    {
        doUpdate(buildCriteria(obj), con);
        obj.setModified(false);
    }

    /**
     * Method to delete.  This method is to be used during a transaction,
     * otherwise use the doDelete(TWorkflowActivity) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to delete in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(TWorkflowActivity obj, Connection con)
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
        BaseTWorkflowActivityPeer
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
    public static Criteria buildCriteria( TWorkflowActivity obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
        if (!obj.isNew())
        criteria.add(OBJECTID, obj.getObjectID());
        criteria.add(TRANSITIONACTIVITY, obj.getTransitionActivity());
        criteria.add(STATIONENTRYACTIVITY, obj.getStationEntryActivity());
        criteria.add(STATIONEXITACTIVITY, obj.getStationExitActivity());
        criteria.add(STATIONDOACTIVITY, obj.getStationDoActivity());
        criteria.add(ACTIVITYTYPE, obj.getActivityType());
        criteria.add(ACTIVITYPARAMS, obj.getActivityParams());
        criteria.add(GROOVYSCRIPT, obj.getGroovyScript());
        criteria.add(NEWMAN, obj.getNewMan());
        criteria.add(NEWRESP, obj.getNewResp());
        criteria.add(FIELDSETTERRELATION, obj.getFieldSetterRelation());
        criteria.add(PARAMNAME, obj.getParamName());
        criteria.add(FIELDSETMODE, obj.getFieldSetMode());
        criteria.add(SORTORDER, obj.getSortOrder());
        criteria.add(SLA, obj.getSla());
        criteria.add(SCREEN, obj.getScreen());
        criteria.add(TPUUID, obj.getUuid());
        return criteria;
    }

    /** Build a Criteria object from the data object for this peer, skipping all binary columns */
    public static Criteria buildSelectCriteria( TWorkflowActivity obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
        if (!obj.isNew())
        {
            criteria.add(OBJECTID, obj.getObjectID());
        }
            criteria.add(TRANSITIONACTIVITY, obj.getTransitionActivity());
            criteria.add(STATIONENTRYACTIVITY, obj.getStationEntryActivity());
            criteria.add(STATIONEXITACTIVITY, obj.getStationExitActivity());
            criteria.add(STATIONDOACTIVITY, obj.getStationDoActivity());
            criteria.add(ACTIVITYTYPE, obj.getActivityType());
            criteria.add(ACTIVITYPARAMS, obj.getActivityParams());
            criteria.add(GROOVYSCRIPT, obj.getGroovyScript());
            criteria.add(NEWMAN, obj.getNewMan());
            criteria.add(NEWRESP, obj.getNewResp());
            criteria.add(FIELDSETTERRELATION, obj.getFieldSetterRelation());
            criteria.add(PARAMNAME, obj.getParamName());
            criteria.add(FIELDSETMODE, obj.getFieldSetMode());
            criteria.add(SORTORDER, obj.getSortOrder());
            criteria.add(SLA, obj.getSla());
            criteria.add(SCREEN, obj.getScreen());
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
    public static TWorkflowActivity retrieveByPK(Integer pk)
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
    public static TWorkflowActivity retrieveByPK(Integer pk, Connection con)
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
    public static TWorkflowActivity retrieveByPK(ObjectKey pk)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        Connection db = null;
        TWorkflowActivity retVal = null;
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
    public static TWorkflowActivity retrieveByPK(ObjectKey pk, Connection con)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        Criteria criteria = buildCriteria(pk);
        List<TWorkflowActivity> v = doSelect(criteria, con);
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
            return (TWorkflowActivity)v.get(0);
        }
    }

    /**
     * Retrieve a multiple objects by pk
     *
     * @param pks List of primary keys
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TWorkflowActivity> retrieveByPKs(List<ObjectKey> pks)
        throws TorqueException
    {
        Connection db = null;
        List<TWorkflowActivity> retVal = null;
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
    public static List<TWorkflowActivity> retrieveByPKs( List<ObjectKey> pks, Connection dbcon )
        throws TorqueException
    {
        List<TWorkflowActivity> objs = null;
        if (pks == null || pks.size() == 0)
        {
            objs = new LinkedList<TWorkflowActivity>();
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
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TWorkflowTransition objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTWorkflowTransition(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TWorkflowTransition objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTWorkflowTransition(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkflowActivityPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TWorkflowTransitionPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkflowActivityPeer.TRANSITIONACTIVITY,
            TWorkflowTransitionPeer.OBJECTID);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);
             omClass = TWorkflowTransitionPeer.getOMClass();
            TWorkflowTransition obj2 = (TWorkflowTransition) TWorkflowTransitionPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowTransition temp_obj2 = (TWorkflowTransition) temp_obj1.getTWorkflowTransition();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TWorkflowStation objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTWorkflowStationRelatedByStationEntryActivity(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TWorkflowStation objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTWorkflowStationRelatedByStationEntryActivity(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkflowActivityPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TWorkflowStationPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkflowActivityPeer.STATIONENTRYACTIVITY,
            TWorkflowStationPeer.OBJECTID);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);
             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj2 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj2 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationEntryActivity();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitysRelatedByStationEntryActivity();
                obj2.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TWorkflowStation objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTWorkflowStationRelatedByStationExitActivity(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TWorkflowStation objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTWorkflowStationRelatedByStationExitActivity(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkflowActivityPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TWorkflowStationPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkflowActivityPeer.STATIONEXITACTIVITY,
            TWorkflowStationPeer.OBJECTID);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);
             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj2 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj2 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationExitActivity();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivityRelatedByStationExitActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitysRelatedByStationExitActivity();
                obj2.addTWorkflowActivityRelatedByStationExitActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TWorkflowStation objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTWorkflowStationRelatedByStationDoActivity(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TWorkflowStation objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTWorkflowStationRelatedByStationDoActivity(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkflowActivityPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TWorkflowStationPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkflowActivityPeer.STATIONDOACTIVITY,
            TWorkflowStationPeer.OBJECTID);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);
             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj2 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj2 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationDoActivity();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivityRelatedByStationDoActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitysRelatedByStationDoActivity();
                obj2.addTWorkflowActivityRelatedByStationDoActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TScripts objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTScripts(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TScripts objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTScripts(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkflowActivityPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TScriptsPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkflowActivityPeer.GROOVYSCRIPT,
            TScriptsPeer.OBJECTID);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);
             omClass = TScriptsPeer.getOMClass();
            TScripts obj2 = (TScripts) TScriptsPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScripts temp_obj2 = (TScripts) temp_obj1.getTScripts();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTPersonRelatedByNewMan(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTPersonRelatedByNewMan(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTPersonRelatedByNewMan(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkflowActivityPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TPersonPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkflowActivityPeer.NEWMAN,
            TPersonPeer.PKEY);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);
             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByNewMan();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivityRelatedByNewMan(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitysRelatedByNewMan();
                obj2.addTWorkflowActivityRelatedByNewMan(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTPersonRelatedByNewResp(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTPersonRelatedByNewResp(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTPersonRelatedByNewResp(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkflowActivityPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TPersonPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkflowActivityPeer.NEWRESP,
            TPersonPeer.PKEY);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);
             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByNewResp();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivityRelatedByNewResp(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitysRelatedByNewResp();
                obj2.addTWorkflowActivityRelatedByNewResp(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TSLA objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTSLA(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTSLA(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TSLA objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTSLA(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkflowActivityPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TSLAPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkflowActivityPeer.SLA,
            TSLAPeer.OBJECTID);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);
             omClass = TSLAPeer.getOMClass();
            TSLA obj2 = (TSLA) TSLAPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TSLA temp_obj2 = (TSLA) temp_obj1.getTSLA();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TScreen objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTScreen(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with their
     * TScreen objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinTScreen(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkflowActivityPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TScreenPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkflowActivityPeer.SCREEN,
            TScreenPeer.OBJECTID);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);
             omClass = TScreenPeer.getOMClass();
            TScreen obj2 = (TScreen) TScreenPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScreen temp_obj2 = (TScreen) temp_obj1.getTScreen();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }







    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTWorkflowTransition(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTWorkflowTransition(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;


        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONENTRYACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset3 = offset2 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONEXITACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset4 = offset3 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONDOACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset5 = offset4 + TWorkflowStationPeer.numColumns;

        TScriptsPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.GROOVYSCRIPT, TScriptsPeer.OBJECTID);
        int offset6 = offset5 + TScriptsPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWMAN, TPersonPeer.PKEY);
        int offset7 = offset6 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWRESP, TPersonPeer.PKEY);
        int offset8 = offset7 + TPersonPeer.numColumns;

        TSLAPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SLA, TSLAPeer.OBJECTID);
        int offset9 = offset8 + TSLAPeer.numColumns;

        TScreenPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SCREEN, TScreenPeer.OBJECTID);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);





             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj2 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj2 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationEntryActivity();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitysRelatedByStationEntryActivity();
                obj2.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj3 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj3 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationExitActivity();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkflowActivityRelatedByStationExitActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkflowActivitysRelatedByStationExitActivity();
                obj3.addTWorkflowActivityRelatedByStationExitActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj4 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj4 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationDoActivity();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkflowActivityRelatedByStationDoActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkflowActivitysRelatedByStationDoActivity();
                obj4.addTWorkflowActivityRelatedByStationDoActivity(obj1);
            }




             omClass = TScriptsPeer.getOMClass();
            TScripts obj5 = (TScripts) TScriptsPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScripts temp_obj5 = (TScripts) temp_obj1.getTScripts();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkflowActivitys();
                obj5.addTWorkflowActivity(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj6 = (TPerson) TPersonPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj6 = (TPerson) temp_obj1.getTPersonRelatedByNewMan();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkflowActivityRelatedByNewMan(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkflowActivitysRelatedByNewMan();
                obj6.addTWorkflowActivityRelatedByNewMan(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj7 = (TPerson) TPersonPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj7 = (TPerson) temp_obj1.getTPersonRelatedByNewResp();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkflowActivityRelatedByNewResp(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkflowActivitysRelatedByNewResp();
                obj7.addTWorkflowActivityRelatedByNewResp(obj1);
            }




             omClass = TSLAPeer.getOMClass();
            TSLA obj8 = (TSLA) TSLAPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TSLA temp_obj8 = (TSLA) temp_obj1.getTSLA();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkflowActivitys();
                obj8.addTWorkflowActivity(obj1);
            }




             omClass = TScreenPeer.getOMClass();
            TScreen obj9 = (TScreen) TScreenPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScreen temp_obj9 = (TScreen) temp_obj1.getTScreen();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkflowActivitys();
                obj9.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTWorkflowStationRelatedByStationEntryActivity(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTWorkflowStationRelatedByStationEntryActivity(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTWorkflowStationRelatedByStationEntryActivity(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TWorkflowTransitionPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.TRANSITIONACTIVITY, TWorkflowTransitionPeer.OBJECTID);
        int offset3 = offset2 + TWorkflowTransitionPeer.numColumns;




        TScriptsPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.GROOVYSCRIPT, TScriptsPeer.OBJECTID);
        int offset4 = offset3 + TScriptsPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWMAN, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWRESP, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TSLAPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SLA, TSLAPeer.OBJECTID);
        int offset7 = offset6 + TSLAPeer.numColumns;

        TScreenPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SCREEN, TScreenPeer.OBJECTID);
        int offset8 = offset7 + TScreenPeer.numColumns;

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);




             omClass = TWorkflowTransitionPeer.getOMClass();
            TWorkflowTransition obj2 = (TWorkflowTransition) TWorkflowTransitionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowTransition temp_obj2 = (TWorkflowTransition) temp_obj1.getTWorkflowTransition();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }







             omClass = TScriptsPeer.getOMClass();
            TScripts obj3 = (TScripts) TScriptsPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScripts temp_obj3 = (TScripts) temp_obj1.getTScripts();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkflowActivitys();
                obj3.addTWorkflowActivity(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByNewMan();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkflowActivityRelatedByNewMan(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkflowActivitysRelatedByNewMan();
                obj4.addTWorkflowActivityRelatedByNewMan(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByNewResp();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkflowActivityRelatedByNewResp(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkflowActivitysRelatedByNewResp();
                obj5.addTWorkflowActivityRelatedByNewResp(obj1);
            }




             omClass = TSLAPeer.getOMClass();
            TSLA obj6 = (TSLA) TSLAPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TSLA temp_obj6 = (TSLA) temp_obj1.getTSLA();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkflowActivitys();
                obj6.addTWorkflowActivity(obj1);
            }




             omClass = TScreenPeer.getOMClass();
            TScreen obj7 = (TScreen) TScreenPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScreen temp_obj7 = (TScreen) temp_obj1.getTScreen();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkflowActivitys();
                obj7.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTWorkflowStationRelatedByStationExitActivity(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTWorkflowStationRelatedByStationExitActivity(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTWorkflowStationRelatedByStationExitActivity(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TWorkflowTransitionPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.TRANSITIONACTIVITY, TWorkflowTransitionPeer.OBJECTID);
        int offset3 = offset2 + TWorkflowTransitionPeer.numColumns;




        TScriptsPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.GROOVYSCRIPT, TScriptsPeer.OBJECTID);
        int offset4 = offset3 + TScriptsPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWMAN, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWRESP, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TSLAPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SLA, TSLAPeer.OBJECTID);
        int offset7 = offset6 + TSLAPeer.numColumns;

        TScreenPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SCREEN, TScreenPeer.OBJECTID);
        int offset8 = offset7 + TScreenPeer.numColumns;

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);




             omClass = TWorkflowTransitionPeer.getOMClass();
            TWorkflowTransition obj2 = (TWorkflowTransition) TWorkflowTransitionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowTransition temp_obj2 = (TWorkflowTransition) temp_obj1.getTWorkflowTransition();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }







             omClass = TScriptsPeer.getOMClass();
            TScripts obj3 = (TScripts) TScriptsPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScripts temp_obj3 = (TScripts) temp_obj1.getTScripts();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkflowActivitys();
                obj3.addTWorkflowActivity(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByNewMan();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkflowActivityRelatedByNewMan(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkflowActivitysRelatedByNewMan();
                obj4.addTWorkflowActivityRelatedByNewMan(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByNewResp();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkflowActivityRelatedByNewResp(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkflowActivitysRelatedByNewResp();
                obj5.addTWorkflowActivityRelatedByNewResp(obj1);
            }




             omClass = TSLAPeer.getOMClass();
            TSLA obj6 = (TSLA) TSLAPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TSLA temp_obj6 = (TSLA) temp_obj1.getTSLA();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkflowActivitys();
                obj6.addTWorkflowActivity(obj1);
            }




             omClass = TScreenPeer.getOMClass();
            TScreen obj7 = (TScreen) TScreenPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScreen temp_obj7 = (TScreen) temp_obj1.getTScreen();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkflowActivitys();
                obj7.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTWorkflowStationRelatedByStationDoActivity(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTWorkflowStationRelatedByStationDoActivity(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTWorkflowStationRelatedByStationDoActivity(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TWorkflowTransitionPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.TRANSITIONACTIVITY, TWorkflowTransitionPeer.OBJECTID);
        int offset3 = offset2 + TWorkflowTransitionPeer.numColumns;




        TScriptsPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.GROOVYSCRIPT, TScriptsPeer.OBJECTID);
        int offset4 = offset3 + TScriptsPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWMAN, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWRESP, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TSLAPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SLA, TSLAPeer.OBJECTID);
        int offset7 = offset6 + TSLAPeer.numColumns;

        TScreenPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SCREEN, TScreenPeer.OBJECTID);
        int offset8 = offset7 + TScreenPeer.numColumns;

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);




             omClass = TWorkflowTransitionPeer.getOMClass();
            TWorkflowTransition obj2 = (TWorkflowTransition) TWorkflowTransitionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowTransition temp_obj2 = (TWorkflowTransition) temp_obj1.getTWorkflowTransition();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }







             omClass = TScriptsPeer.getOMClass();
            TScripts obj3 = (TScripts) TScriptsPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScripts temp_obj3 = (TScripts) temp_obj1.getTScripts();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkflowActivitys();
                obj3.addTWorkflowActivity(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByNewMan();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkflowActivityRelatedByNewMan(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkflowActivitysRelatedByNewMan();
                obj4.addTWorkflowActivityRelatedByNewMan(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByNewResp();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkflowActivityRelatedByNewResp(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkflowActivitysRelatedByNewResp();
                obj5.addTWorkflowActivityRelatedByNewResp(obj1);
            }




             omClass = TSLAPeer.getOMClass();
            TSLA obj6 = (TSLA) TSLAPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TSLA temp_obj6 = (TSLA) temp_obj1.getTSLA();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkflowActivitys();
                obj6.addTWorkflowActivity(obj1);
            }




             omClass = TScreenPeer.getOMClass();
            TScreen obj7 = (TScreen) TScreenPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScreen temp_obj7 = (TScreen) temp_obj1.getTScreen();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkflowActivitys();
                obj7.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTScripts(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTScripts(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTScripts(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TWorkflowTransitionPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.TRANSITIONACTIVITY, TWorkflowTransitionPeer.OBJECTID);
        int offset3 = offset2 + TWorkflowTransitionPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONENTRYACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset4 = offset3 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONEXITACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset5 = offset4 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONDOACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset6 = offset5 + TWorkflowStationPeer.numColumns;


        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWMAN, TPersonPeer.PKEY);
        int offset7 = offset6 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWRESP, TPersonPeer.PKEY);
        int offset8 = offset7 + TPersonPeer.numColumns;

        TSLAPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SLA, TSLAPeer.OBJECTID);
        int offset9 = offset8 + TSLAPeer.numColumns;

        TScreenPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SCREEN, TScreenPeer.OBJECTID);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);




             omClass = TWorkflowTransitionPeer.getOMClass();
            TWorkflowTransition obj2 = (TWorkflowTransition) TWorkflowTransitionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowTransition temp_obj2 = (TWorkflowTransition) temp_obj1.getTWorkflowTransition();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj3 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj3 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationEntryActivity();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkflowActivitysRelatedByStationEntryActivity();
                obj3.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj4 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj4 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationExitActivity();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkflowActivityRelatedByStationExitActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkflowActivitysRelatedByStationExitActivity();
                obj4.addTWorkflowActivityRelatedByStationExitActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj5 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj5 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationDoActivity();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkflowActivityRelatedByStationDoActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkflowActivitysRelatedByStationDoActivity();
                obj5.addTWorkflowActivityRelatedByStationDoActivity(obj1);
            }





             omClass = TPersonPeer.getOMClass();
            TPerson obj6 = (TPerson) TPersonPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj6 = (TPerson) temp_obj1.getTPersonRelatedByNewMan();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkflowActivityRelatedByNewMan(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkflowActivitysRelatedByNewMan();
                obj6.addTWorkflowActivityRelatedByNewMan(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj7 = (TPerson) TPersonPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj7 = (TPerson) temp_obj1.getTPersonRelatedByNewResp();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkflowActivityRelatedByNewResp(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkflowActivitysRelatedByNewResp();
                obj7.addTWorkflowActivityRelatedByNewResp(obj1);
            }




             omClass = TSLAPeer.getOMClass();
            TSLA obj8 = (TSLA) TSLAPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TSLA temp_obj8 = (TSLA) temp_obj1.getTSLA();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkflowActivitys();
                obj8.addTWorkflowActivity(obj1);
            }




             omClass = TScreenPeer.getOMClass();
            TScreen obj9 = (TScreen) TScreenPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScreen temp_obj9 = (TScreen) temp_obj1.getTScreen();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkflowActivitys();
                obj9.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTPersonRelatedByNewMan(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTPersonRelatedByNewMan(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTPersonRelatedByNewMan(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TWorkflowTransitionPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.TRANSITIONACTIVITY, TWorkflowTransitionPeer.OBJECTID);
        int offset3 = offset2 + TWorkflowTransitionPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONENTRYACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset4 = offset3 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONEXITACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset5 = offset4 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONDOACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset6 = offset5 + TWorkflowStationPeer.numColumns;

        TScriptsPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.GROOVYSCRIPT, TScriptsPeer.OBJECTID);
        int offset7 = offset6 + TScriptsPeer.numColumns;



        TSLAPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SLA, TSLAPeer.OBJECTID);
        int offset8 = offset7 + TSLAPeer.numColumns;

        TScreenPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SCREEN, TScreenPeer.OBJECTID);
        int offset9 = offset8 + TScreenPeer.numColumns;

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);




             omClass = TWorkflowTransitionPeer.getOMClass();
            TWorkflowTransition obj2 = (TWorkflowTransition) TWorkflowTransitionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowTransition temp_obj2 = (TWorkflowTransition) temp_obj1.getTWorkflowTransition();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj3 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj3 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationEntryActivity();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkflowActivitysRelatedByStationEntryActivity();
                obj3.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj4 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj4 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationExitActivity();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkflowActivityRelatedByStationExitActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkflowActivitysRelatedByStationExitActivity();
                obj4.addTWorkflowActivityRelatedByStationExitActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj5 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj5 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationDoActivity();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkflowActivityRelatedByStationDoActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkflowActivitysRelatedByStationDoActivity();
                obj5.addTWorkflowActivityRelatedByStationDoActivity(obj1);
            }




             omClass = TScriptsPeer.getOMClass();
            TScripts obj6 = (TScripts) TScriptsPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScripts temp_obj6 = (TScripts) temp_obj1.getTScripts();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkflowActivitys();
                obj6.addTWorkflowActivity(obj1);
            }






             omClass = TSLAPeer.getOMClass();
            TSLA obj7 = (TSLA) TSLAPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TSLA temp_obj7 = (TSLA) temp_obj1.getTSLA();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkflowActivitys();
                obj7.addTWorkflowActivity(obj1);
            }




             omClass = TScreenPeer.getOMClass();
            TScreen obj8 = (TScreen) TScreenPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScreen temp_obj8 = (TScreen) temp_obj1.getTScreen();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkflowActivitys();
                obj8.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTPersonRelatedByNewResp(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTPersonRelatedByNewResp(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTPersonRelatedByNewResp(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TWorkflowTransitionPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.TRANSITIONACTIVITY, TWorkflowTransitionPeer.OBJECTID);
        int offset3 = offset2 + TWorkflowTransitionPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONENTRYACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset4 = offset3 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONEXITACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset5 = offset4 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONDOACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset6 = offset5 + TWorkflowStationPeer.numColumns;

        TScriptsPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.GROOVYSCRIPT, TScriptsPeer.OBJECTID);
        int offset7 = offset6 + TScriptsPeer.numColumns;



        TSLAPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SLA, TSLAPeer.OBJECTID);
        int offset8 = offset7 + TSLAPeer.numColumns;

        TScreenPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SCREEN, TScreenPeer.OBJECTID);
        int offset9 = offset8 + TScreenPeer.numColumns;

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);




             omClass = TWorkflowTransitionPeer.getOMClass();
            TWorkflowTransition obj2 = (TWorkflowTransition) TWorkflowTransitionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowTransition temp_obj2 = (TWorkflowTransition) temp_obj1.getTWorkflowTransition();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj3 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj3 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationEntryActivity();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkflowActivitysRelatedByStationEntryActivity();
                obj3.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj4 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj4 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationExitActivity();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkflowActivityRelatedByStationExitActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkflowActivitysRelatedByStationExitActivity();
                obj4.addTWorkflowActivityRelatedByStationExitActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj5 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj5 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationDoActivity();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkflowActivityRelatedByStationDoActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkflowActivitysRelatedByStationDoActivity();
                obj5.addTWorkflowActivityRelatedByStationDoActivity(obj1);
            }




             omClass = TScriptsPeer.getOMClass();
            TScripts obj6 = (TScripts) TScriptsPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScripts temp_obj6 = (TScripts) temp_obj1.getTScripts();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkflowActivitys();
                obj6.addTWorkflowActivity(obj1);
            }






             omClass = TSLAPeer.getOMClass();
            TSLA obj7 = (TSLA) TSLAPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TSLA temp_obj7 = (TSLA) temp_obj1.getTSLA();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkflowActivitys();
                obj7.addTWorkflowActivity(obj1);
            }




             omClass = TScreenPeer.getOMClass();
            TScreen obj8 = (TScreen) TScreenPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScreen temp_obj8 = (TScreen) temp_obj1.getTScreen();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkflowActivitys();
                obj8.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTSLA(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTSLA(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTSLA(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TWorkflowTransitionPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.TRANSITIONACTIVITY, TWorkflowTransitionPeer.OBJECTID);
        int offset3 = offset2 + TWorkflowTransitionPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONENTRYACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset4 = offset3 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONEXITACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset5 = offset4 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONDOACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset6 = offset5 + TWorkflowStationPeer.numColumns;

        TScriptsPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.GROOVYSCRIPT, TScriptsPeer.OBJECTID);
        int offset7 = offset6 + TScriptsPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWMAN, TPersonPeer.PKEY);
        int offset8 = offset7 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWRESP, TPersonPeer.PKEY);
        int offset9 = offset8 + TPersonPeer.numColumns;


        TScreenPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SCREEN, TScreenPeer.OBJECTID);

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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);




             omClass = TWorkflowTransitionPeer.getOMClass();
            TWorkflowTransition obj2 = (TWorkflowTransition) TWorkflowTransitionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowTransition temp_obj2 = (TWorkflowTransition) temp_obj1.getTWorkflowTransition();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj3 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj3 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationEntryActivity();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkflowActivitysRelatedByStationEntryActivity();
                obj3.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj4 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj4 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationExitActivity();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkflowActivityRelatedByStationExitActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkflowActivitysRelatedByStationExitActivity();
                obj4.addTWorkflowActivityRelatedByStationExitActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj5 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj5 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationDoActivity();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkflowActivityRelatedByStationDoActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkflowActivitysRelatedByStationDoActivity();
                obj5.addTWorkflowActivityRelatedByStationDoActivity(obj1);
            }




             omClass = TScriptsPeer.getOMClass();
            TScripts obj6 = (TScripts) TScriptsPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScripts temp_obj6 = (TScripts) temp_obj1.getTScripts();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkflowActivitys();
                obj6.addTWorkflowActivity(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj7 = (TPerson) TPersonPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj7 = (TPerson) temp_obj1.getTPersonRelatedByNewMan();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkflowActivityRelatedByNewMan(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkflowActivitysRelatedByNewMan();
                obj7.addTWorkflowActivityRelatedByNewMan(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj8 = (TPerson) TPersonPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj8 = (TPerson) temp_obj1.getTPersonRelatedByNewResp();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkflowActivityRelatedByNewResp(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkflowActivitysRelatedByNewResp();
                obj8.addTWorkflowActivityRelatedByNewResp(obj1);
            }





             omClass = TScreenPeer.getOMClass();
            TScreen obj9 = (TScreen) TScreenPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScreen temp_obj9 = (TScreen) temp_obj1.getTScreen();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkflowActivitys();
                obj9.addTWorkflowActivity(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTScreen(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTScreen(criteria, null);
    }

    /**
     * selects a collection of TWorkflowActivity objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivityPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkflowActivity> doSelectJoinAllExceptTScreen(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TWorkflowTransitionPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.TRANSITIONACTIVITY, TWorkflowTransitionPeer.OBJECTID);
        int offset3 = offset2 + TWorkflowTransitionPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONENTRYACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset4 = offset3 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONEXITACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset5 = offset4 + TWorkflowStationPeer.numColumns;

        TWorkflowStationPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.STATIONDOACTIVITY, TWorkflowStationPeer.OBJECTID);
        int offset6 = offset5 + TWorkflowStationPeer.numColumns;

        TScriptsPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.GROOVYSCRIPT, TScriptsPeer.OBJECTID);
        int offset7 = offset6 + TScriptsPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWMAN, TPersonPeer.PKEY);
        int offset8 = offset7 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.NEWRESP, TPersonPeer.PKEY);
        int offset9 = offset8 + TPersonPeer.numColumns;

        TSLAPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkflowActivityPeer.SLA, TSLAPeer.OBJECTID);


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

        List<TWorkflowActivity> results = new ArrayList<TWorkflowActivity>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkflowActivityPeer.getOMClass();
            TWorkflowActivity obj1 = (TWorkflowActivity) TWorkflowActivityPeer
                .row2Object(row, 1, omClass);




             omClass = TWorkflowTransitionPeer.getOMClass();
            TWorkflowTransition obj2 = (TWorkflowTransition) TWorkflowTransitionPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowTransition temp_obj2 = (TWorkflowTransition) temp_obj1.getTWorkflowTransition();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkflowActivitys();
                obj2.addTWorkflowActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj3 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj3 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationEntryActivity();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkflowActivitysRelatedByStationEntryActivity();
                obj3.addTWorkflowActivityRelatedByStationEntryActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj4 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj4 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationExitActivity();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkflowActivityRelatedByStationExitActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkflowActivitysRelatedByStationExitActivity();
                obj4.addTWorkflowActivityRelatedByStationExitActivity(obj1);
            }




             omClass = TWorkflowStationPeer.getOMClass();
            TWorkflowStation obj5 = (TWorkflowStation) TWorkflowStationPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TWorkflowStation temp_obj5 = (TWorkflowStation) temp_obj1.getTWorkflowStationRelatedByStationDoActivity();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkflowActivityRelatedByStationDoActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkflowActivitysRelatedByStationDoActivity();
                obj5.addTWorkflowActivityRelatedByStationDoActivity(obj1);
            }




             omClass = TScriptsPeer.getOMClass();
            TScripts obj6 = (TScripts) TScriptsPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TScripts temp_obj6 = (TScripts) temp_obj1.getTScripts();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkflowActivitys();
                obj6.addTWorkflowActivity(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj7 = (TPerson) TPersonPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj7 = (TPerson) temp_obj1.getTPersonRelatedByNewMan();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkflowActivityRelatedByNewMan(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkflowActivitysRelatedByNewMan();
                obj7.addTWorkflowActivityRelatedByNewMan(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj8 = (TPerson) TPersonPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TPerson temp_obj8 = (TPerson) temp_obj1.getTPersonRelatedByNewResp();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkflowActivityRelatedByNewResp(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkflowActivitysRelatedByNewResp();
                obj8.addTWorkflowActivityRelatedByNewResp(obj1);
            }




             omClass = TSLAPeer.getOMClass();
            TSLA obj9 = (TSLA) TSLAPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkflowActivity temp_obj1 =  results.get(j);
                TSLA temp_obj9 = (TSLA) temp_obj1.getTSLA();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkflowActivity(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkflowActivitys();
                obj9.addTWorkflowActivity(obj1);
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
