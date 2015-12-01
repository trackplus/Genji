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
public abstract class BaseTWorkItemPeer
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
     * @deprecated Torque.getMapBuilder(TWorkItemMapBuilder.CLASS_NAME) instead
     */
    public static MapBuilder getMapBuilder()
        throws TorqueException
    {
        return Torque.getMapBuilder(TWorkItemMapBuilder.CLASS_NAME);
    }

    /** the column name for the WORKITEMKEY field */
    public static final String WORKITEMKEY;
    /** the column name for the OWNER field */
    public static final String OWNER;
    /** the column name for the CHANGEDBY field */
    public static final String CHANGEDBY;
    /** the column name for the ORIGINATOR field */
    public static final String ORIGINATOR;
    /** the column name for the RESPONSIBLE field */
    public static final String RESPONSIBLE;
    /** the column name for the PROJECTKEY field */
    public static final String PROJECTKEY;
    /** the column name for the PROJCATKEY field */
    public static final String PROJCATKEY;
    /** the column name for the CATEGORYKEY field */
    public static final String CATEGORYKEY;
    /** the column name for the CLASSKEY field */
    public static final String CLASSKEY;
    /** the column name for the PRIORITYKEY field */
    public static final String PRIORITYKEY;
    /** the column name for the SEVERITYKEY field */
    public static final String SEVERITYKEY;
    /** the column name for the SUPERIORWORKITEM field */
    public static final String SUPERIORWORKITEM;
    /** the column name for the PACKAGESYNOPSYS field */
    public static final String PACKAGESYNOPSYS;
    /** the column name for the PACKAGEDESCRIPTION field */
    public static final String PACKAGEDESCRIPTION;
    /** the column name for the REFERENCE field */
    public static final String REFERENCE;
    /** the column name for the LASTEDIT field */
    public static final String LASTEDIT;
    /** the column name for the RELNOTICEDKEY field */
    public static final String RELNOTICEDKEY;
    /** the column name for the RELSCHEDULEDKEY field */
    public static final String RELSCHEDULEDKEY;
    /** the column name for the BUILD field */
    public static final String BUILD;
    /** the column name for the STATE field */
    public static final String STATE;
    /** the column name for the STARTDATE field */
    public static final String STARTDATE;
    /** the column name for the ENDDATE field */
    public static final String ENDDATE;
    /** the column name for the SUBMITTEREMAIL field */
    public static final String SUBMITTEREMAIL;
    /** the column name for the CREATED field */
    public static final String CREATED;
    /** the column name for the ACTUALSTARTDATE field */
    public static final String ACTUALSTARTDATE;
    /** the column name for the ACTUALENDDATE field */
    public static final String ACTUALENDDATE;
    /** the column name for the WLEVEL field */
    public static final String WLEVEL;
    /** the column name for the ACCESSLEVEL field */
    public static final String ACCESSLEVEL;
    /** the column name for the ARCHIVELEVEL field */
    public static final String ARCHIVELEVEL;
    /** the column name for the ESCALATIONLEVEL field */
    public static final String ESCALATIONLEVEL;
    /** the column name for the TASKISMILESTONE field */
    public static final String TASKISMILESTONE;
    /** the column name for the TASKISSUBPROJECT field */
    public static final String TASKISSUBPROJECT;
    /** the column name for the TASKISSUMMARY field */
    public static final String TASKISSUMMARY;
    /** the column name for the TASKCONSTRAINT field */
    public static final String TASKCONSTRAINT;
    /** the column name for the TASKCONSTRAINTDATE field */
    public static final String TASKCONSTRAINTDATE;
    /** the column name for the PSPCODE field */
    public static final String PSPCODE;
    /** the column name for the IDNUMBER field */
    public static final String IDNUMBER;
    /** the column name for the WBSONLEVEL field */
    public static final String WBSONLEVEL;
    /** the column name for the REMINDERDATE field */
    public static final String REMINDERDATE;
    /** the column name for the TOPDOWNSTARTDATE field */
    public static final String TOPDOWNSTARTDATE;
    /** the column name for the TOPDOWNENDDATE field */
    public static final String TOPDOWNENDDATE;
    /** the column name for the OVERBUDGET field */
    public static final String OVERBUDGET;
    /** the column name for the TPUUID field */
    public static final String TPUUID;

    static
    {
        DATABASE_NAME = "track";
        TABLE_NAME = "TWORKITEM";

        WORKITEMKEY = "TWORKITEM.WORKITEMKEY";
        OWNER = "TWORKITEM.OWNER";
        CHANGEDBY = "TWORKITEM.CHANGEDBY";
        ORIGINATOR = "TWORKITEM.ORIGINATOR";
        RESPONSIBLE = "TWORKITEM.RESPONSIBLE";
        PROJECTKEY = "TWORKITEM.PROJECTKEY";
        PROJCATKEY = "TWORKITEM.PROJCATKEY";
        CATEGORYKEY = "TWORKITEM.CATEGORYKEY";
        CLASSKEY = "TWORKITEM.CLASSKEY";
        PRIORITYKEY = "TWORKITEM.PRIORITYKEY";
        SEVERITYKEY = "TWORKITEM.SEVERITYKEY";
        SUPERIORWORKITEM = "TWORKITEM.SUPERIORWORKITEM";
        PACKAGESYNOPSYS = "TWORKITEM.PACKAGESYNOPSYS";
        PACKAGEDESCRIPTION = "TWORKITEM.PACKAGEDESCRIPTION";
        REFERENCE = "TWORKITEM.REFERENCE";
        LASTEDIT = "TWORKITEM.LASTEDIT";
        RELNOTICEDKEY = "TWORKITEM.RELNOTICEDKEY";
        RELSCHEDULEDKEY = "TWORKITEM.RELSCHEDULEDKEY";
        BUILD = "TWORKITEM.BUILD";
        STATE = "TWORKITEM.STATE";
        STARTDATE = "TWORKITEM.STARTDATE";
        ENDDATE = "TWORKITEM.ENDDATE";
        SUBMITTEREMAIL = "TWORKITEM.SUBMITTEREMAIL";
        CREATED = "TWORKITEM.CREATED";
        ACTUALSTARTDATE = "TWORKITEM.ACTUALSTARTDATE";
        ACTUALENDDATE = "TWORKITEM.ACTUALENDDATE";
        WLEVEL = "TWORKITEM.WLEVEL";
        ACCESSLEVEL = "TWORKITEM.ACCESSLEVEL";
        ARCHIVELEVEL = "TWORKITEM.ARCHIVELEVEL";
        ESCALATIONLEVEL = "TWORKITEM.ESCALATIONLEVEL";
        TASKISMILESTONE = "TWORKITEM.TASKISMILESTONE";
        TASKISSUBPROJECT = "TWORKITEM.TASKISSUBPROJECT";
        TASKISSUMMARY = "TWORKITEM.TASKISSUMMARY";
        TASKCONSTRAINT = "TWORKITEM.TASKCONSTRAINT";
        TASKCONSTRAINTDATE = "TWORKITEM.TASKCONSTRAINTDATE";
        PSPCODE = "TWORKITEM.PSPCODE";
        IDNUMBER = "TWORKITEM.IDNUMBER";
        WBSONLEVEL = "TWORKITEM.WBSONLEVEL";
        REMINDERDATE = "TWORKITEM.REMINDERDATE";
        TOPDOWNSTARTDATE = "TWORKITEM.TOPDOWNSTARTDATE";
        TOPDOWNENDDATE = "TWORKITEM.TOPDOWNENDDATE";
        OVERBUDGET = "TWORKITEM.OVERBUDGET";
        TPUUID = "TWORKITEM.TPUUID";
        if (Torque.isInit())
        {
            try
            {
                Torque.getMapBuilder(TWorkItemMapBuilder.CLASS_NAME);
            }
            catch (TorqueException e)
            {
                log.error("Could not initialize Peer", e);
                throw new TorqueRuntimeException(e);
            }
        }
        else
        {
            Torque.registerMapBuilder(TWorkItemMapBuilder.CLASS_NAME);
        }
    }
 
    /** number of columns for this peer */
    public static final int numColumns =  43;

    /** A class that can be returned by this peer. */
    protected static final String CLASSNAME_DEFAULT =
        "com.aurel.track.persist.TWorkItem";

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
    public static List<TWorkItem> resultSet2Objects(java.sql.ResultSet results)
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
        return BaseTWorkItemPeer
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
        criteria.addSelectColumn(WORKITEMKEY);
        criteria.addSelectColumn(OWNER);
        criteria.addSelectColumn(CHANGEDBY);
        criteria.addSelectColumn(ORIGINATOR);
        criteria.addSelectColumn(RESPONSIBLE);
        criteria.addSelectColumn(PROJECTKEY);
        criteria.addSelectColumn(PROJCATKEY);
        criteria.addSelectColumn(CATEGORYKEY);
        criteria.addSelectColumn(CLASSKEY);
        criteria.addSelectColumn(PRIORITYKEY);
        criteria.addSelectColumn(SEVERITYKEY);
        criteria.addSelectColumn(SUPERIORWORKITEM);
        criteria.addSelectColumn(PACKAGESYNOPSYS);
        criteria.addSelectColumn(PACKAGEDESCRIPTION);
        criteria.addSelectColumn(REFERENCE);
        criteria.addSelectColumn(LASTEDIT);
        criteria.addSelectColumn(RELNOTICEDKEY);
        criteria.addSelectColumn(RELSCHEDULEDKEY);
        criteria.addSelectColumn(BUILD);
        criteria.addSelectColumn(STATE);
        criteria.addSelectColumn(STARTDATE);
        criteria.addSelectColumn(ENDDATE);
        criteria.addSelectColumn(SUBMITTEREMAIL);
        criteria.addSelectColumn(CREATED);
        criteria.addSelectColumn(ACTUALSTARTDATE);
        criteria.addSelectColumn(ACTUALENDDATE);
        criteria.addSelectColumn(WLEVEL);
        criteria.addSelectColumn(ACCESSLEVEL);
        criteria.addSelectColumn(ARCHIVELEVEL);
        criteria.addSelectColumn(ESCALATIONLEVEL);
        criteria.addSelectColumn(TASKISMILESTONE);
        criteria.addSelectColumn(TASKISSUBPROJECT);
        criteria.addSelectColumn(TASKISSUMMARY);
        criteria.addSelectColumn(TASKCONSTRAINT);
        criteria.addSelectColumn(TASKCONSTRAINTDATE);
        criteria.addSelectColumn(PSPCODE);
        criteria.addSelectColumn(IDNUMBER);
        criteria.addSelectColumn(WBSONLEVEL);
        criteria.addSelectColumn(REMINDERDATE);
        criteria.addSelectColumn(TOPDOWNSTARTDATE);
        criteria.addSelectColumn(TOPDOWNENDDATE);
        criteria.addSelectColumn(OVERBUDGET);
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
    public static TWorkItem row2Object(Record row,
                                             int offset,
                                             Class cls)
        throws TorqueException
    {
        try
        {
            TWorkItem obj = (TWorkItem) cls.newInstance();
            TWorkItemPeer.populateObject(row, offset, obj);
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
                                      TWorkItem obj)
        throws TorqueException
    {
        try
        {
            obj.setObjectID(row.getValue(offset + 0).asIntegerObj());
            obj.setOwnerID(row.getValue(offset + 1).asIntegerObj());
            obj.setChangedByID(row.getValue(offset + 2).asIntegerObj());
            obj.setOriginatorID(row.getValue(offset + 3).asIntegerObj());
            obj.setResponsibleID(row.getValue(offset + 4).asIntegerObj());
            obj.setProjectID(row.getValue(offset + 5).asIntegerObj());
            obj.setProjectCategoryID(row.getValue(offset + 6).asIntegerObj());
            obj.setListTypeID(row.getValue(offset + 7).asIntegerObj());
            obj.setClassID(row.getValue(offset + 8).asIntegerObj());
            obj.setPriorityID(row.getValue(offset + 9).asIntegerObj());
            obj.setSeverityID(row.getValue(offset + 10).asIntegerObj());
            obj.setSuperiorworkitem(row.getValue(offset + 11).asIntegerObj());
            obj.setSynopsis(row.getValue(offset + 12).asString());
            obj.setDescription(row.getValue(offset + 13).asString());
            obj.setReference(row.getValue(offset + 14).asString());
            obj.setLastEdit(row.getValue(offset + 15).asUtilDate());
            obj.setReleaseNoticedID(row.getValue(offset + 16).asIntegerObj());
            obj.setReleaseScheduledID(row.getValue(offset + 17).asIntegerObj());
            obj.setBuild(row.getValue(offset + 18).asString());
            obj.setStateID(row.getValue(offset + 19).asIntegerObj());
            obj.setStartDate(row.getValue(offset + 20).asUtilDate());
            obj.setEndDate(row.getValue(offset + 21).asUtilDate());
            obj.setSubmitterEmail(row.getValue(offset + 22).asString());
            obj.setCreated(row.getValue(offset + 23).asUtilDate());
            obj.setActualStartDate(row.getValue(offset + 24).asUtilDate());
            obj.setActualEndDate(row.getValue(offset + 25).asUtilDate());
            obj.setLevel(row.getValue(offset + 26).asString());
            obj.setAccessLevel(row.getValue(offset + 27).asIntegerObj());
            obj.setArchiveLevel(row.getValue(offset + 28).asIntegerObj());
            obj.setEscalationLevel(row.getValue(offset + 29).asIntegerObj());
            obj.setTaskIsMilestone(row.getValue(offset + 30).asString());
            obj.setTaskIsSubproject(row.getValue(offset + 31).asString());
            obj.setTaskIsSummary(row.getValue(offset + 32).asString());
            obj.setTaskConstraint(row.getValue(offset + 33).asIntegerObj());
            obj.setTaskConstraintDate(row.getValue(offset + 34).asUtilDate());
            obj.setPSPCode(row.getValue(offset + 35).asString());
            obj.setIDNumber(row.getValue(offset + 36).asIntegerObj());
            obj.setWBSOnLevel(row.getValue(offset + 37).asIntegerObj());
            obj.setReminderDate(row.getValue(offset + 38).asUtilDate());
            obj.setTopDownStartDate(row.getValue(offset + 39).asUtilDate());
            obj.setTopDownEndDate(row.getValue(offset + 40).asUtilDate());
            obj.setOverBudget(row.getValue(offset + 41).asString());
            obj.setUuid(row.getValue(offset + 42).asString());
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
    public static List<TWorkItem> doSelect(Criteria criteria) throws TorqueException
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
    public static List<TWorkItem> doSelect(Criteria criteria, Connection con)
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
        return BaseTWorkItemPeer
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
    public static List<TWorkItem> populateObjects(List<Record> records)
        throws TorqueException
    {
        List<TWorkItem> results = new ArrayList<TWorkItem>(records.size());

        // populate the object(s)
        for (int i = 0; i < records.size(); i++)
        {
            Record row =  records.get(i);
            results.add(TWorkItemPeer.row2Object(row, 1,
                TWorkItemPeer.getOMClass()));
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
         BaseTWorkItemPeer
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


         selectCriteria.put(WORKITEMKEY, criteria.remove(WORKITEMKEY));











































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
         TWorkItemPeer
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
    public static List<TWorkItem> doSelect(TWorkItem obj) throws TorqueException
    {
        return doSelect(buildSelectCriteria(obj));
    }

    /**
     * Method to do inserts
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(TWorkItem obj) throws TorqueException
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
    public static void doUpdate(TWorkItem obj) throws TorqueException
    {
        doUpdate(buildCriteria(obj));
        obj.setModified(false);
    }

    /**
     * @param obj the data object to delete in the database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(TWorkItem obj) throws TorqueException
    {
        doDelete(buildSelectCriteria(obj));
    }

    /**
     * Method to do inserts.  This method is to be used during a transaction,
     * otherwise use the doInsert(TWorkItem) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to insert into the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(TWorkItem obj, Connection con)
        throws TorqueException
    {
        obj.setPrimaryKey(doInsert(buildCriteria(obj), con));
        obj.setNew(false);
        obj.setModified(false);
    }

    /**
     * Method to do update.  This method is to be used during a transaction,
     * otherwise use the doUpdate(TWorkItem) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to update in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(TWorkItem obj, Connection con)
        throws TorqueException
    {
        doUpdate(buildCriteria(obj), con);
        obj.setModified(false);
    }

    /**
     * Method to delete.  This method is to be used during a transaction,
     * otherwise use the doDelete(TWorkItem) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to delete in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(TWorkItem obj, Connection con)
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
        BaseTWorkItemPeer
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
            criteria.add(WORKITEMKEY, pk);
        return criteria;
     }

    /** Build a Criteria object from the data object for this peer */
    public static Criteria buildCriteria( TWorkItem obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
        if (!obj.isNew())
        criteria.add(WORKITEMKEY, obj.getObjectID());
        criteria.add(OWNER, obj.getOwnerID());
        criteria.add(CHANGEDBY, obj.getChangedByID());
        criteria.add(ORIGINATOR, obj.getOriginatorID());
        criteria.add(RESPONSIBLE, obj.getResponsibleID());
        criteria.add(PROJECTKEY, obj.getProjectID());
        criteria.add(PROJCATKEY, obj.getProjectCategoryID());
        criteria.add(CATEGORYKEY, obj.getListTypeID());
        criteria.add(CLASSKEY, obj.getClassID());
        criteria.add(PRIORITYKEY, obj.getPriorityID());
        criteria.add(SEVERITYKEY, obj.getSeverityID());
        criteria.add(SUPERIORWORKITEM, obj.getSuperiorworkitem());
        criteria.add(PACKAGESYNOPSYS, obj.getSynopsis());
        criteria.add(PACKAGEDESCRIPTION, obj.getDescription());
        criteria.add(REFERENCE, obj.getReference());
        criteria.add(LASTEDIT, obj.getLastEdit());
        criteria.add(RELNOTICEDKEY, obj.getReleaseNoticedID());
        criteria.add(RELSCHEDULEDKEY, obj.getReleaseScheduledID());
        criteria.add(BUILD, obj.getBuild());
        criteria.add(STATE, obj.getStateID());
        criteria.add(STARTDATE, obj.getStartDate());
        criteria.add(ENDDATE, obj.getEndDate());
        criteria.add(SUBMITTEREMAIL, obj.getSubmitterEmail());
        criteria.add(CREATED, obj.getCreated());
        criteria.add(ACTUALSTARTDATE, obj.getActualStartDate());
        criteria.add(ACTUALENDDATE, obj.getActualEndDate());
        criteria.add(WLEVEL, obj.getLevel());
        criteria.add(ACCESSLEVEL, obj.getAccessLevel());
        criteria.add(ARCHIVELEVEL, obj.getArchiveLevel());
        criteria.add(ESCALATIONLEVEL, obj.getEscalationLevel());
        criteria.add(TASKISMILESTONE, obj.getTaskIsMilestone());
        criteria.add(TASKISSUBPROJECT, obj.getTaskIsSubproject());
        criteria.add(TASKISSUMMARY, obj.getTaskIsSummary());
        criteria.add(TASKCONSTRAINT, obj.getTaskConstraint());
        criteria.add(TASKCONSTRAINTDATE, obj.getTaskConstraintDate());
        criteria.add(PSPCODE, obj.getPSPCode());
        criteria.add(IDNUMBER, obj.getIDNumber());
        criteria.add(WBSONLEVEL, obj.getWBSOnLevel());
        criteria.add(REMINDERDATE, obj.getReminderDate());
        criteria.add(TOPDOWNSTARTDATE, obj.getTopDownStartDate());
        criteria.add(TOPDOWNENDDATE, obj.getTopDownEndDate());
        criteria.add(OVERBUDGET, obj.getOverBudget());
        criteria.add(TPUUID, obj.getUuid());
        return criteria;
    }

    /** Build a Criteria object from the data object for this peer, skipping all binary columns */
    public static Criteria buildSelectCriteria( TWorkItem obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
        if (!obj.isNew())
        {
            criteria.add(WORKITEMKEY, obj.getObjectID());
        }
            criteria.add(OWNER, obj.getOwnerID());
            criteria.add(CHANGEDBY, obj.getChangedByID());
            criteria.add(ORIGINATOR, obj.getOriginatorID());
            criteria.add(RESPONSIBLE, obj.getResponsibleID());
            criteria.add(PROJECTKEY, obj.getProjectID());
            criteria.add(PROJCATKEY, obj.getProjectCategoryID());
            criteria.add(CATEGORYKEY, obj.getListTypeID());
            criteria.add(CLASSKEY, obj.getClassID());
            criteria.add(PRIORITYKEY, obj.getPriorityID());
            criteria.add(SEVERITYKEY, obj.getSeverityID());
            criteria.add(SUPERIORWORKITEM, obj.getSuperiorworkitem());
            criteria.add(PACKAGESYNOPSYS, obj.getSynopsis());
            criteria.add(PACKAGEDESCRIPTION, obj.getDescription());
            criteria.add(REFERENCE, obj.getReference());
            criteria.add(LASTEDIT, obj.getLastEdit());
            criteria.add(RELNOTICEDKEY, obj.getReleaseNoticedID());
            criteria.add(RELSCHEDULEDKEY, obj.getReleaseScheduledID());
            criteria.add(BUILD, obj.getBuild());
            criteria.add(STATE, obj.getStateID());
            criteria.add(STARTDATE, obj.getStartDate());
            criteria.add(ENDDATE, obj.getEndDate());
            criteria.add(SUBMITTEREMAIL, obj.getSubmitterEmail());
            criteria.add(CREATED, obj.getCreated());
            criteria.add(ACTUALSTARTDATE, obj.getActualStartDate());
            criteria.add(ACTUALENDDATE, obj.getActualEndDate());
            criteria.add(WLEVEL, obj.getLevel());
            criteria.add(ACCESSLEVEL, obj.getAccessLevel());
            criteria.add(ARCHIVELEVEL, obj.getArchiveLevel());
            criteria.add(ESCALATIONLEVEL, obj.getEscalationLevel());
            criteria.add(TASKISMILESTONE, obj.getTaskIsMilestone());
            criteria.add(TASKISSUBPROJECT, obj.getTaskIsSubproject());
            criteria.add(TASKISSUMMARY, obj.getTaskIsSummary());
            criteria.add(TASKCONSTRAINT, obj.getTaskConstraint());
            criteria.add(TASKCONSTRAINTDATE, obj.getTaskConstraintDate());
            criteria.add(PSPCODE, obj.getPSPCode());
            criteria.add(IDNUMBER, obj.getIDNumber());
            criteria.add(WBSONLEVEL, obj.getWBSOnLevel());
            criteria.add(REMINDERDATE, obj.getReminderDate());
            criteria.add(TOPDOWNSTARTDATE, obj.getTopDownStartDate());
            criteria.add(TOPDOWNENDDATE, obj.getTopDownEndDate());
            criteria.add(OVERBUDGET, obj.getOverBudget());
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
    public static TWorkItem retrieveByPK(Integer pk)
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
    public static TWorkItem retrieveByPK(Integer pk, Connection con)
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
    public static TWorkItem retrieveByPK(ObjectKey pk)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        Connection db = null;
        TWorkItem retVal = null;
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
    public static TWorkItem retrieveByPK(ObjectKey pk, Connection con)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        Criteria criteria = buildCriteria(pk);
        List<TWorkItem> v = doSelect(criteria, con);
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
            return (TWorkItem)v.get(0);
        }
    }

    /**
     * Retrieve a multiple objects by pk
     *
     * @param pks List of primary keys
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TWorkItem> retrieveByPKs(List<ObjectKey> pks)
        throws TorqueException
    {
        Connection db = null;
        List<TWorkItem> retVal = null;
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
    public static List<TWorkItem> retrieveByPKs( List<ObjectKey> pks, Connection dbcon )
        throws TorqueException
    {
        List<TWorkItem> objs = null;
        if (pks == null || pks.size() == 0)
        {
            objs = new LinkedList<TWorkItem>();
        }
        else
        {
            Criteria criteria = new Criteria();
            criteria.addIn( WORKITEMKEY, pks );
        objs = doSelect(criteria, dbcon);
        }
        return objs;
    }

 








    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTPersonRelatedByOwnerID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTPersonRelatedByOwnerID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTPersonRelatedByOwnerID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TPersonPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.OWNER,
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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTPersonRelatedByChangedByID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTPersonRelatedByChangedByID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTPersonRelatedByChangedByID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TPersonPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.CHANGEDBY,
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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByChangedByID();
                obj2.addTWorkItemRelatedByChangedByID(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTPersonRelatedByOriginatorID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTPersonRelatedByOriginatorID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTPersonRelatedByOriginatorID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TPersonPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.ORIGINATOR,
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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOriginatorID();
                obj2.addTWorkItemRelatedByOriginatorID(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTPersonRelatedByResponsibleID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTPersonRelatedByResponsibleID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTPersonRelatedByResponsibleID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TPersonPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.RESPONSIBLE,
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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByResponsibleID();
                obj2.addTWorkItemRelatedByResponsibleID(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TProjectCategory objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTProjectCategory(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTProjectCategory(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TProjectCategory objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTProjectCategory(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TProjectCategoryPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.PROJCATKEY,
            TProjectCategoryPeer.PKEY);

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj2 = (TProjectCategory) TProjectCategoryPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj2 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TListType objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTListType(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTListType(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TListType objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTListType(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TListTypePeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.CATEGORYKEY,
            TListTypePeer.PKEY);

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TListTypePeer.getOMClass();
            TListType obj2 = (TListType) TListTypePeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj2 = (TListType) temp_obj1.getTListType();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TClass objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTClass(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTClass(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TClass objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTClass(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TClassPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.CLASSKEY,
            TClassPeer.PKEY);

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TClassPeer.getOMClass();
            TClass obj2 = (TClass) TClassPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj2 = (TClass) temp_obj1.getTClass();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TPriority objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTPriority(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TPriority objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTPriority(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TPriorityPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.PRIORITYKEY,
            TPriorityPeer.PKEY);

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TPriorityPeer.getOMClass();
            TPriority obj2 = (TPriority) TPriorityPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj2 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TSeverity objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTSeverity(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTSeverity(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TSeverity objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTSeverity(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TSeverityPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.SEVERITYKEY,
            TSeverityPeer.PKEY);

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TSeverityPeer.getOMClass();
            TSeverity obj2 = (TSeverity) TSeverityPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj2 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TRelease objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTReleaseRelatedByReleaseNoticedID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TRelease objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTReleaseRelatedByReleaseNoticedID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TReleasePeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY,
            TReleasePeer.PKEY);

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TReleasePeer.getOMClass();
            TRelease obj2 = (TRelease) TReleasePeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj2 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByReleaseNoticedID();
                obj2.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TRelease objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTReleaseRelatedByReleaseScheduledID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TRelease objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTReleaseRelatedByReleaseScheduledID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TReleasePeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY,
            TReleasePeer.PKEY);

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TReleasePeer.getOMClass();
            TRelease obj2 = (TRelease) TReleasePeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj2 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByReleaseScheduledID();
                obj2.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TState objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTState(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTState(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TState objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTState(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TStatePeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.STATE,
            TStatePeer.PKEY);

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TStatePeer.getOMClass();
            TState obj2 = (TState) TStatePeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj2 = (TState) temp_obj1.getTState();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TProject objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTProject(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTProject(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with their
     * TProject objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinTProject(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TWorkItemPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TProjectPeer.addSelectColumns(criteria);

        criteria.addJoin(TWorkItemPeer.PROJECTKEY,
            TProjectPeer.PKEY);

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);
             omClass = TProjectPeer.getOMClass();
            TProject obj2 = (TProject) TProjectPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj2 = (TProject) temp_obj1.getTProject();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }







    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTPersonRelatedByOwnerID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTPersonRelatedByOwnerID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTPersonRelatedByOwnerID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;





        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset3 = offset2 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset4 = offset3 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset5 = offset4 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset6 = offset5 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset7 = offset6 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset8 = offset7 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset9 = offset8 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset10 = offset9 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset11 = offset10 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);








             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj2 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj2 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj3 = (TListType) TListTypePeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj3 = (TListType) temp_obj1.getTListType();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItems();
                obj3.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj4 = (TClass) TClassPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj4 = (TClass) temp_obj1.getTClass();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItems();
                obj4.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj5 = (TPriority) TPriorityPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj5 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItems();
                obj5.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj6 = (TSeverity) TSeverityPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj6 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj7 = (TRelease) TReleasePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj7 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItemsRelatedByReleaseNoticedID();
                obj7.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj8 = (TRelease) TReleasePeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj8 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItemsRelatedByReleaseScheduledID();
                obj8.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj9 = (TState) TStatePeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj9 = (TState) temp_obj1.getTState();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj10 = (TProject) TProjectPeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj10 = (TProject) temp_obj1.getTProject();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItems();
                obj10.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTPersonRelatedByChangedByID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTPersonRelatedByChangedByID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTPersonRelatedByChangedByID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;





        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset3 = offset2 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset4 = offset3 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset5 = offset4 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset6 = offset5 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset7 = offset6 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset8 = offset7 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset9 = offset8 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset10 = offset9 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset11 = offset10 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);








             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj2 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj2 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj3 = (TListType) TListTypePeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj3 = (TListType) temp_obj1.getTListType();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItems();
                obj3.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj4 = (TClass) TClassPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj4 = (TClass) temp_obj1.getTClass();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItems();
                obj4.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj5 = (TPriority) TPriorityPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj5 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItems();
                obj5.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj6 = (TSeverity) TSeverityPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj6 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj7 = (TRelease) TReleasePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj7 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItemsRelatedByReleaseNoticedID();
                obj7.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj8 = (TRelease) TReleasePeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj8 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItemsRelatedByReleaseScheduledID();
                obj8.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj9 = (TState) TStatePeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj9 = (TState) temp_obj1.getTState();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj10 = (TProject) TProjectPeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj10 = (TProject) temp_obj1.getTProject();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItems();
                obj10.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTPersonRelatedByOriginatorID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTPersonRelatedByOriginatorID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTPersonRelatedByOriginatorID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;





        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset3 = offset2 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset4 = offset3 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset5 = offset4 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset6 = offset5 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset7 = offset6 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset8 = offset7 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset9 = offset8 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset10 = offset9 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset11 = offset10 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);








             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj2 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj2 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj3 = (TListType) TListTypePeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj3 = (TListType) temp_obj1.getTListType();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItems();
                obj3.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj4 = (TClass) TClassPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj4 = (TClass) temp_obj1.getTClass();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItems();
                obj4.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj5 = (TPriority) TPriorityPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj5 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItems();
                obj5.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj6 = (TSeverity) TSeverityPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj6 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj7 = (TRelease) TReleasePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj7 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItemsRelatedByReleaseNoticedID();
                obj7.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj8 = (TRelease) TReleasePeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj8 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItemsRelatedByReleaseScheduledID();
                obj8.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj9 = (TState) TStatePeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj9 = (TState) temp_obj1.getTState();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj10 = (TProject) TProjectPeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj10 = (TProject) temp_obj1.getTProject();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItems();
                obj10.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTPersonRelatedByResponsibleID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTPersonRelatedByResponsibleID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTPersonRelatedByResponsibleID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;





        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset3 = offset2 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset4 = offset3 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset5 = offset4 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset6 = offset5 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset7 = offset6 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset8 = offset7 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset9 = offset8 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset10 = offset9 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset11 = offset10 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);








             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj2 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj2 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItems();
                obj2.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj3 = (TListType) TListTypePeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj3 = (TListType) temp_obj1.getTListType();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItems();
                obj3.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj4 = (TClass) TClassPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj4 = (TClass) temp_obj1.getTClass();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItems();
                obj4.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj5 = (TPriority) TPriorityPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj5 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItems();
                obj5.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj6 = (TSeverity) TSeverityPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj6 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj7 = (TRelease) TReleasePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj7 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItemsRelatedByReleaseNoticedID();
                obj7.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj8 = (TRelease) TReleasePeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj8 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItemsRelatedByReleaseScheduledID();
                obj8.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj9 = (TState) TStatePeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj9 = (TState) temp_obj1.getTState();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj10 = (TProject) TProjectPeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj10 = (TProject) temp_obj1.getTProject();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItems();
                obj10.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTProjectCategory(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTProjectCategory(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTProjectCategory(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.OWNER, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset4 = offset3 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.ORIGINATOR, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RESPONSIBLE, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;


        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset7 = offset6 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset8 = offset7 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset9 = offset8 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset10 = offset9 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset11 = offset10 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset12 = offset11 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset13 = offset12 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset14 = offset13 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);




             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItemsRelatedByChangedByID();
                obj3.addTWorkItemRelatedByChangedByID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItemsRelatedByOriginatorID();
                obj4.addTWorkItemRelatedByOriginatorID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItemsRelatedByResponsibleID();
                obj5.addTWorkItemRelatedByResponsibleID(obj1);
            }





             omClass = TListTypePeer.getOMClass();
            TListType obj6 = (TListType) TListTypePeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj6 = (TListType) temp_obj1.getTListType();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj7 = (TClass) TClassPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj7 = (TClass) temp_obj1.getTClass();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItems();
                obj7.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj8 = (TPriority) TPriorityPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj8 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItems();
                obj8.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj9 = (TSeverity) TSeverityPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj9 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj10 = (TRelease) TReleasePeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj10 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItemsRelatedByReleaseNoticedID();
                obj10.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj11 = (TRelease) TReleasePeer
                .row2Object( row, offset11, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj11 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj11.getPrimaryKey().equals(obj11.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj11.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj11.initTWorkItemsRelatedByReleaseScheduledID();
                obj11.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj12 = (TState) TStatePeer
                .row2Object( row, offset12, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj12 = (TState) temp_obj1.getTState();
                if (temp_obj12.getPrimaryKey().equals(obj12.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj12.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj12.initTWorkItems();
                obj12.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj13 = (TProject) TProjectPeer
                .row2Object( row, offset13, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj13 = (TProject) temp_obj1.getTProject();
                if (temp_obj13.getPrimaryKey().equals(obj13.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj13.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj13.initTWorkItems();
                obj13.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTListType(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTListType(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTListType(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.OWNER, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset4 = offset3 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.ORIGINATOR, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RESPONSIBLE, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset7 = offset6 + TProjectCategoryPeer.numColumns;


        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset8 = offset7 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset9 = offset8 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset10 = offset9 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset11 = offset10 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset12 = offset11 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset13 = offset12 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset14 = offset13 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);




             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItemsRelatedByChangedByID();
                obj3.addTWorkItemRelatedByChangedByID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItemsRelatedByOriginatorID();
                obj4.addTWorkItemRelatedByOriginatorID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItemsRelatedByResponsibleID();
                obj5.addTWorkItemRelatedByResponsibleID(obj1);
            }




             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj6 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj6 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }





             omClass = TClassPeer.getOMClass();
            TClass obj7 = (TClass) TClassPeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj7 = (TClass) temp_obj1.getTClass();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItems();
                obj7.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj8 = (TPriority) TPriorityPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj8 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItems();
                obj8.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj9 = (TSeverity) TSeverityPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj9 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj10 = (TRelease) TReleasePeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj10 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItemsRelatedByReleaseNoticedID();
                obj10.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj11 = (TRelease) TReleasePeer
                .row2Object( row, offset11, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj11 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj11.getPrimaryKey().equals(obj11.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj11.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj11.initTWorkItemsRelatedByReleaseScheduledID();
                obj11.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj12 = (TState) TStatePeer
                .row2Object( row, offset12, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj12 = (TState) temp_obj1.getTState();
                if (temp_obj12.getPrimaryKey().equals(obj12.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj12.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj12.initTWorkItems();
                obj12.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj13 = (TProject) TProjectPeer
                .row2Object( row, offset13, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj13 = (TProject) temp_obj1.getTProject();
                if (temp_obj13.getPrimaryKey().equals(obj13.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj13.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj13.initTWorkItems();
                obj13.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTClass(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTClass(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTClass(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.OWNER, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset4 = offset3 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.ORIGINATOR, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RESPONSIBLE, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset7 = offset6 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset8 = offset7 + TListTypePeer.numColumns;


        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset9 = offset8 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset10 = offset9 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset11 = offset10 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset12 = offset11 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset13 = offset12 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset14 = offset13 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);




             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItemsRelatedByChangedByID();
                obj3.addTWorkItemRelatedByChangedByID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItemsRelatedByOriginatorID();
                obj4.addTWorkItemRelatedByOriginatorID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItemsRelatedByResponsibleID();
                obj5.addTWorkItemRelatedByResponsibleID(obj1);
            }




             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj6 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj6 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj7 = (TListType) TListTypePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj7 = (TListType) temp_obj1.getTListType();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItems();
                obj7.addTWorkItem(obj1);
            }





             omClass = TPriorityPeer.getOMClass();
            TPriority obj8 = (TPriority) TPriorityPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj8 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItems();
                obj8.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj9 = (TSeverity) TSeverityPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj9 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj10 = (TRelease) TReleasePeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj10 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItemsRelatedByReleaseNoticedID();
                obj10.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj11 = (TRelease) TReleasePeer
                .row2Object( row, offset11, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj11 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj11.getPrimaryKey().equals(obj11.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj11.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj11.initTWorkItemsRelatedByReleaseScheduledID();
                obj11.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj12 = (TState) TStatePeer
                .row2Object( row, offset12, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj12 = (TState) temp_obj1.getTState();
                if (temp_obj12.getPrimaryKey().equals(obj12.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj12.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj12.initTWorkItems();
                obj12.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj13 = (TProject) TProjectPeer
                .row2Object( row, offset13, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj13 = (TProject) temp_obj1.getTProject();
                if (temp_obj13.getPrimaryKey().equals(obj13.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj13.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj13.initTWorkItems();
                obj13.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTPriority(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTPriority(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTPriority(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.OWNER, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset4 = offset3 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.ORIGINATOR, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RESPONSIBLE, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset7 = offset6 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset8 = offset7 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset9 = offset8 + TClassPeer.numColumns;


        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset10 = offset9 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset11 = offset10 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset12 = offset11 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset13 = offset12 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset14 = offset13 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);




             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItemsRelatedByChangedByID();
                obj3.addTWorkItemRelatedByChangedByID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItemsRelatedByOriginatorID();
                obj4.addTWorkItemRelatedByOriginatorID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItemsRelatedByResponsibleID();
                obj5.addTWorkItemRelatedByResponsibleID(obj1);
            }




             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj6 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj6 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj7 = (TListType) TListTypePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj7 = (TListType) temp_obj1.getTListType();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItems();
                obj7.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj8 = (TClass) TClassPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj8 = (TClass) temp_obj1.getTClass();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItems();
                obj8.addTWorkItem(obj1);
            }





             omClass = TSeverityPeer.getOMClass();
            TSeverity obj9 = (TSeverity) TSeverityPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj9 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj10 = (TRelease) TReleasePeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj10 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItemsRelatedByReleaseNoticedID();
                obj10.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj11 = (TRelease) TReleasePeer
                .row2Object( row, offset11, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj11 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj11.getPrimaryKey().equals(obj11.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj11.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj11.initTWorkItemsRelatedByReleaseScheduledID();
                obj11.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj12 = (TState) TStatePeer
                .row2Object( row, offset12, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj12 = (TState) temp_obj1.getTState();
                if (temp_obj12.getPrimaryKey().equals(obj12.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj12.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj12.initTWorkItems();
                obj12.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj13 = (TProject) TProjectPeer
                .row2Object( row, offset13, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj13 = (TProject) temp_obj1.getTProject();
                if (temp_obj13.getPrimaryKey().equals(obj13.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj13.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj13.initTWorkItems();
                obj13.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTSeverity(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTSeverity(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTSeverity(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.OWNER, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset4 = offset3 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.ORIGINATOR, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RESPONSIBLE, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset7 = offset6 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset8 = offset7 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset9 = offset8 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset10 = offset9 + TPriorityPeer.numColumns;


        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset11 = offset10 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset12 = offset11 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset13 = offset12 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset14 = offset13 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);




             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItemsRelatedByChangedByID();
                obj3.addTWorkItemRelatedByChangedByID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItemsRelatedByOriginatorID();
                obj4.addTWorkItemRelatedByOriginatorID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItemsRelatedByResponsibleID();
                obj5.addTWorkItemRelatedByResponsibleID(obj1);
            }




             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj6 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj6 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj7 = (TListType) TListTypePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj7 = (TListType) temp_obj1.getTListType();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItems();
                obj7.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj8 = (TClass) TClassPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj8 = (TClass) temp_obj1.getTClass();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItems();
                obj8.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj9 = (TPriority) TPriorityPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj9 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }





             omClass = TReleasePeer.getOMClass();
            TRelease obj10 = (TRelease) TReleasePeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj10 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItemsRelatedByReleaseNoticedID();
                obj10.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj11 = (TRelease) TReleasePeer
                .row2Object( row, offset11, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj11 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj11.getPrimaryKey().equals(obj11.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj11.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj11.initTWorkItemsRelatedByReleaseScheduledID();
                obj11.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj12 = (TState) TStatePeer
                .row2Object( row, offset12, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj12 = (TState) temp_obj1.getTState();
                if (temp_obj12.getPrimaryKey().equals(obj12.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj12.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj12.initTWorkItems();
                obj12.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj13 = (TProject) TProjectPeer
                .row2Object( row, offset13, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj13 = (TProject) temp_obj1.getTProject();
                if (temp_obj13.getPrimaryKey().equals(obj13.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj13.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj13.initTWorkItems();
                obj13.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTReleaseRelatedByReleaseNoticedID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTReleaseRelatedByReleaseNoticedID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTReleaseRelatedByReleaseNoticedID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.OWNER, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset4 = offset3 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.ORIGINATOR, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RESPONSIBLE, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset7 = offset6 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset8 = offset7 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset9 = offset8 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset10 = offset9 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset11 = offset10 + TSeverityPeer.numColumns;



        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset12 = offset11 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset13 = offset12 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);




             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItemsRelatedByChangedByID();
                obj3.addTWorkItemRelatedByChangedByID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItemsRelatedByOriginatorID();
                obj4.addTWorkItemRelatedByOriginatorID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItemsRelatedByResponsibleID();
                obj5.addTWorkItemRelatedByResponsibleID(obj1);
            }




             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj6 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj6 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj7 = (TListType) TListTypePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj7 = (TListType) temp_obj1.getTListType();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItems();
                obj7.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj8 = (TClass) TClassPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj8 = (TClass) temp_obj1.getTClass();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItems();
                obj8.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj9 = (TPriority) TPriorityPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj9 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj10 = (TSeverity) TSeverityPeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj10 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItems();
                obj10.addTWorkItem(obj1);
            }






             omClass = TStatePeer.getOMClass();
            TState obj11 = (TState) TStatePeer
                .row2Object( row, offset11, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj11 = (TState) temp_obj1.getTState();
                if (temp_obj11.getPrimaryKey().equals(obj11.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj11.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj11.initTWorkItems();
                obj11.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj12 = (TProject) TProjectPeer
                .row2Object( row, offset12, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj12 = (TProject) temp_obj1.getTProject();
                if (temp_obj12.getPrimaryKey().equals(obj12.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj12.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj12.initTWorkItems();
                obj12.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTReleaseRelatedByReleaseScheduledID(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTReleaseRelatedByReleaseScheduledID(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTReleaseRelatedByReleaseScheduledID(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.OWNER, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset4 = offset3 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.ORIGINATOR, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RESPONSIBLE, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset7 = offset6 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset8 = offset7 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset9 = offset8 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset10 = offset9 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset11 = offset10 + TSeverityPeer.numColumns;



        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset12 = offset11 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset13 = offset12 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);




             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItemsRelatedByChangedByID();
                obj3.addTWorkItemRelatedByChangedByID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItemsRelatedByOriginatorID();
                obj4.addTWorkItemRelatedByOriginatorID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItemsRelatedByResponsibleID();
                obj5.addTWorkItemRelatedByResponsibleID(obj1);
            }




             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj6 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj6 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj7 = (TListType) TListTypePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj7 = (TListType) temp_obj1.getTListType();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItems();
                obj7.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj8 = (TClass) TClassPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj8 = (TClass) temp_obj1.getTClass();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItems();
                obj8.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj9 = (TPriority) TPriorityPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj9 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj10 = (TSeverity) TSeverityPeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj10 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItems();
                obj10.addTWorkItem(obj1);
            }






             omClass = TStatePeer.getOMClass();
            TState obj11 = (TState) TStatePeer
                .row2Object( row, offset11, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj11 = (TState) temp_obj1.getTState();
                if (temp_obj11.getPrimaryKey().equals(obj11.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj11.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj11.initTWorkItems();
                obj11.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj12 = (TProject) TProjectPeer
                .row2Object( row, offset12, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj12 = (TProject) temp_obj1.getTProject();
                if (temp_obj12.getPrimaryKey().equals(obj12.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj12.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj12.initTWorkItems();
                obj12.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTState(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTState(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTState(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.OWNER, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset4 = offset3 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.ORIGINATOR, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RESPONSIBLE, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset7 = offset6 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset8 = offset7 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset9 = offset8 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset10 = offset9 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset11 = offset10 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset12 = offset11 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset13 = offset12 + TReleasePeer.numColumns;


        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        int offset14 = offset13 + TProjectPeer.numColumns;

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);




             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItemsRelatedByChangedByID();
                obj3.addTWorkItemRelatedByChangedByID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItemsRelatedByOriginatorID();
                obj4.addTWorkItemRelatedByOriginatorID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItemsRelatedByResponsibleID();
                obj5.addTWorkItemRelatedByResponsibleID(obj1);
            }




             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj6 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj6 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj7 = (TListType) TListTypePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj7 = (TListType) temp_obj1.getTListType();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItems();
                obj7.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj8 = (TClass) TClassPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj8 = (TClass) temp_obj1.getTClass();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItems();
                obj8.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj9 = (TPriority) TPriorityPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj9 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj10 = (TSeverity) TSeverityPeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj10 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItems();
                obj10.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj11 = (TRelease) TReleasePeer
                .row2Object( row, offset11, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj11 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj11.getPrimaryKey().equals(obj11.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj11.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj11.initTWorkItemsRelatedByReleaseNoticedID();
                obj11.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj12 = (TRelease) TReleasePeer
                .row2Object( row, offset12, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj12 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj12.getPrimaryKey().equals(obj12.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj12.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj12.initTWorkItemsRelatedByReleaseScheduledID();
                obj12.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }





             omClass = TProjectPeer.getOMClass();
            TProject obj13 = (TProject) TProjectPeer
                .row2Object( row, offset13, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj13 = (TProject) temp_obj1.getTProject();
                if (temp_obj13.getPrimaryKey().equals(obj13.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj13.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj13.initTWorkItems();
                obj13.addTWorkItem(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTProject(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTProject(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTProject(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.OWNER, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset4 = offset3 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.ORIGINATOR, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RESPONSIBLE, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset7 = offset6 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset8 = offset7 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset9 = offset8 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset10 = offset9 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset11 = offset10 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset12 = offset11 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset13 = offset12 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset14 = offset13 + TStatePeer.numColumns;


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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);




             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItemsRelatedByChangedByID();
                obj3.addTWorkItemRelatedByChangedByID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItemsRelatedByOriginatorID();
                obj4.addTWorkItemRelatedByOriginatorID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItemsRelatedByResponsibleID();
                obj5.addTWorkItemRelatedByResponsibleID(obj1);
            }




             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj6 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj6 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj7 = (TListType) TListTypePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj7 = (TListType) temp_obj1.getTListType();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItems();
                obj7.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj8 = (TClass) TClassPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj8 = (TClass) temp_obj1.getTClass();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItems();
                obj8.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj9 = (TPriority) TPriorityPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj9 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj10 = (TSeverity) TSeverityPeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj10 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItems();
                obj10.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj11 = (TRelease) TReleasePeer
                .row2Object( row, offset11, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj11 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj11.getPrimaryKey().equals(obj11.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj11.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj11.initTWorkItemsRelatedByReleaseNoticedID();
                obj11.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj12 = (TRelease) TReleasePeer
                .row2Object( row, offset12, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj12 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj12.getPrimaryKey().equals(obj12.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj12.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj12.initTWorkItemsRelatedByReleaseScheduledID();
                obj12.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj13 = (TState) TStatePeer
                .row2Object( row, offset13, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj13 = (TState) temp_obj1.getTState();
                if (temp_obj13.getPrimaryKey().equals(obj13.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj13.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj13.initTWorkItems();
                obj13.addTWorkItem(obj1);
            }

            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTWorkItem(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTWorkItem(criteria, null);
    }

    /**
     * selects a collection of TWorkItem objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItemPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TWorkItem> doSelectJoinAllExceptTWorkItem(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.OWNER, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset4 = offset3 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.ORIGINATOR, TPersonPeer.PKEY);
        int offset5 = offset4 + TPersonPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RESPONSIBLE, TPersonPeer.PKEY);
        int offset6 = offset5 + TPersonPeer.numColumns;

        TProjectCategoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJCATKEY, TProjectCategoryPeer.PKEY);
        int offset7 = offset6 + TProjectCategoryPeer.numColumns;

        TListTypePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        int offset8 = offset7 + TListTypePeer.numColumns;

        TClassPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.CLASSKEY, TClassPeer.PKEY);
        int offset9 = offset8 + TClassPeer.numColumns;

        TPriorityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PRIORITYKEY, TPriorityPeer.PKEY);
        int offset10 = offset9 + TPriorityPeer.numColumns;

        TSeverityPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.SEVERITYKEY, TSeverityPeer.PKEY);
        int offset11 = offset10 + TSeverityPeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELNOTICEDKEY, TReleasePeer.PKEY);
        int offset12 = offset11 + TReleasePeer.numColumns;

        TReleasePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.RELSCHEDULEDKEY, TReleasePeer.PKEY);
        int offset13 = offset12 + TReleasePeer.numColumns;

        TStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        int offset14 = offset13 + TStatePeer.numColumns;

        TProjectPeer.addSelectColumns(criteria);
        criteria.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);

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

        List<TWorkItem> results = new ArrayList<TWorkItem>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj1 = (TWorkItem) TWorkItemPeer
                .row2Object(row, 1, omClass);




             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPersonRelatedByOwnerID();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTWorkItemRelatedByOwnerID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTWorkItemsRelatedByOwnerID();
                obj2.addTWorkItemRelatedByOwnerID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPersonRelatedByChangedByID();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTWorkItemRelatedByChangedByID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTWorkItemsRelatedByChangedByID();
                obj3.addTWorkItemRelatedByChangedByID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj4 = (TPerson) TPersonPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj4 = (TPerson) temp_obj1.getTPersonRelatedByOriginatorID();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTWorkItemRelatedByOriginatorID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTWorkItemsRelatedByOriginatorID();
                obj4.addTWorkItemRelatedByOriginatorID(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj5 = (TPerson) TPersonPeer
                .row2Object( row, offset5, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPerson temp_obj5 = (TPerson) temp_obj1.getTPersonRelatedByResponsibleID();
                if (temp_obj5.getPrimaryKey().equals(obj5.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj5.addTWorkItemRelatedByResponsibleID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj5.initTWorkItemsRelatedByResponsibleID();
                obj5.addTWorkItemRelatedByResponsibleID(obj1);
            }




             omClass = TProjectCategoryPeer.getOMClass();
            TProjectCategory obj6 = (TProjectCategory) TProjectCategoryPeer
                .row2Object( row, offset6, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProjectCategory temp_obj6 = (TProjectCategory) temp_obj1.getTProjectCategory();
                if (temp_obj6.getPrimaryKey().equals(obj6.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj6.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj6.initTWorkItems();
                obj6.addTWorkItem(obj1);
            }




             omClass = TListTypePeer.getOMClass();
            TListType obj7 = (TListType) TListTypePeer
                .row2Object( row, offset7, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TListType temp_obj7 = (TListType) temp_obj1.getTListType();
                if (temp_obj7.getPrimaryKey().equals(obj7.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj7.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj7.initTWorkItems();
                obj7.addTWorkItem(obj1);
            }




             omClass = TClassPeer.getOMClass();
            TClass obj8 = (TClass) TClassPeer
                .row2Object( row, offset8, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TClass temp_obj8 = (TClass) temp_obj1.getTClass();
                if (temp_obj8.getPrimaryKey().equals(obj8.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj8.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj8.initTWorkItems();
                obj8.addTWorkItem(obj1);
            }




             omClass = TPriorityPeer.getOMClass();
            TPriority obj9 = (TPriority) TPriorityPeer
                .row2Object( row, offset9, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TPriority temp_obj9 = (TPriority) temp_obj1.getTPriority();
                if (temp_obj9.getPrimaryKey().equals(obj9.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj9.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj9.initTWorkItems();
                obj9.addTWorkItem(obj1);
            }




             omClass = TSeverityPeer.getOMClass();
            TSeverity obj10 = (TSeverity) TSeverityPeer
                .row2Object( row, offset10, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TSeverity temp_obj10 = (TSeverity) temp_obj1.getTSeverity();
                if (temp_obj10.getPrimaryKey().equals(obj10.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj10.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj10.initTWorkItems();
                obj10.addTWorkItem(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj11 = (TRelease) TReleasePeer
                .row2Object( row, offset11, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj11 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseNoticedID();
                if (temp_obj11.getPrimaryKey().equals(obj11.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj11.addTWorkItemRelatedByReleaseNoticedID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj11.initTWorkItemsRelatedByReleaseNoticedID();
                obj11.addTWorkItemRelatedByReleaseNoticedID(obj1);
            }




             omClass = TReleasePeer.getOMClass();
            TRelease obj12 = (TRelease) TReleasePeer
                .row2Object( row, offset12, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TRelease temp_obj12 = (TRelease) temp_obj1.getTReleaseRelatedByReleaseScheduledID();
                if (temp_obj12.getPrimaryKey().equals(obj12.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj12.addTWorkItemRelatedByReleaseScheduledID(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj12.initTWorkItemsRelatedByReleaseScheduledID();
                obj12.addTWorkItemRelatedByReleaseScheduledID(obj1);
            }




             omClass = TStatePeer.getOMClass();
            TState obj13 = (TState) TStatePeer
                .row2Object( row, offset13, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TState temp_obj13 = (TState) temp_obj1.getTState();
                if (temp_obj13.getPrimaryKey().equals(obj13.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj13.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj13.initTWorkItems();
                obj13.addTWorkItem(obj1);
            }




             omClass = TProjectPeer.getOMClass();
            TProject obj14 = (TProject) TProjectPeer
                .row2Object( row, offset14, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TWorkItem temp_obj1 =  results.get(j);
                TProject temp_obj14 = (TProject) temp_obj1.getTProject();
                if (temp_obj14.getPrimaryKey().equals(obj14.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj14.addTWorkItem(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj14.initTWorkItems();
                obj14.addTWorkItem(obj1);
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
