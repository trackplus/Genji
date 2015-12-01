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
 * This table holds all user related information, like user name, e-mail, etc.
 *
 */
public abstract class BaseTPersonPeer
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
     * @deprecated Torque.getMapBuilder(TPersonMapBuilder.CLASS_NAME) instead
     */
    public static MapBuilder getMapBuilder()
        throws TorqueException
    {
        return Torque.getMapBuilder(TPersonMapBuilder.CLASS_NAME);
    }

    /** the column name for the PKEY field */
    public static final String PKEY;
    /** the column name for the FIRSTNAME field */
    public static final String FIRSTNAME;
    /** the column name for the LASTNAME field */
    public static final String LASTNAME;
    /** the column name for the LOGINNAME field */
    public static final String LOGINNAME;
    /** the column name for the EMAIL field */
    public static final String EMAIL;
    /** the column name for the PASSWD field */
    public static final String PASSWD;
    /** the column name for the SALT field */
    public static final String SALT;
    /** the column name for the FORGOTPASSWORDKEY field */
    public static final String FORGOTPASSWORDKEY;
    /** the column name for the PHONE field */
    public static final String PHONE;
    /** the column name for the DEPKEY field */
    public static final String DEPKEY;
    /** the column name for the VALIDUNTIL field */
    public static final String VALIDUNTIL;
    /** the column name for the PREFERENCES field */
    public static final String PREFERENCES;
    /** the column name for the LASTEDIT field */
    public static final String LASTEDIT;
    /** the column name for the CREATED field */
    public static final String CREATED;
    /** the column name for the DELETED field */
    public static final String DELETED;
    /** the column name for the TOKENPASSWD field */
    public static final String TOKENPASSWD;
    /** the column name for the TOKENEXPDATE field */
    public static final String TOKENEXPDATE;
    /** the column name for the EMAILFREQUENCY field */
    public static final String EMAILFREQUENCY;
    /** the column name for the EMAILLEAD field */
    public static final String EMAILLEAD;
    /** the column name for the EMAILLASTREMINDED field */
    public static final String EMAILLASTREMINDED;
    /** the column name for the EMAILREMINDME field */
    public static final String EMAILREMINDME;
    /** the column name for the PREFEMAILTYPE field */
    public static final String PREFEMAILTYPE;
    /** the column name for the PREFLOCALE field */
    public static final String PREFLOCALE;
    /** the column name for the MYDEFAULTREPORT field */
    public static final String MYDEFAULTREPORT;
    /** the column name for the NOEMAILSPLEASE field */
    public static final String NOEMAILSPLEASE;
    /** the column name for the REMINDMEASORIGINATOR field */
    public static final String REMINDMEASORIGINATOR;
    /** the column name for the REMINDMEASMANAGER field */
    public static final String REMINDMEASMANAGER;
    /** the column name for the REMINDMEASRESPONSIBLE field */
    public static final String REMINDMEASRESPONSIBLE;
    /** the column name for the EMAILREMINDPLEVEL field */
    public static final String EMAILREMINDPLEVEL;
    /** the column name for the EMAILREMINDSLEVEL field */
    public static final String EMAILREMINDSLEVEL;
    /** the column name for the HOURSPERWORKDAY field */
    public static final String HOURSPERWORKDAY;
    /** the column name for the HOURLYWAGE field */
    public static final String HOURLYWAGE;
    /** the column name for the EXTRAHOURWAGE field */
    public static final String EXTRAHOURWAGE;
    /** the column name for the EMPLOYEEID field */
    public static final String EMPLOYEEID;
    /** the column name for the ISGROUP field */
    public static final String ISGROUP;
    /** the column name for the USERLEVEL field */
    public static final String USERLEVEL;
    /** the column name for the MAXASSIGNEDITEMS field */
    public static final String MAXASSIGNEDITEMS;
    /** the column name for the MESSENGERURL field */
    public static final String MESSENGERURL;
    /** the column name for the CALLURL field */
    public static final String CALLURL;
    /** the column name for the SYMBOL field */
    public static final String SYMBOL;
    /** the column name for the ICONKEY field */
    public static final String ICONKEY;
    /** the column name for the SUBSTITUTEKEY field */
    public static final String SUBSTITUTEKEY;
    /** the column name for the SUBSTITUTEACTIVE field */
    public static final String SUBSTITUTEACTIVE;
    /** the column name for the TPUUID field */
    public static final String TPUUID;

    static
    {
        DATABASE_NAME = "track";
        TABLE_NAME = "TPERSON";

        PKEY = "TPERSON.PKEY";
        FIRSTNAME = "TPERSON.FIRSTNAME";
        LASTNAME = "TPERSON.LASTNAME";
        LOGINNAME = "TPERSON.LOGINNAME";
        EMAIL = "TPERSON.EMAIL";
        PASSWD = "TPERSON.PASSWD";
        SALT = "TPERSON.SALT";
        FORGOTPASSWORDKEY = "TPERSON.FORGOTPASSWORDKEY";
        PHONE = "TPERSON.PHONE";
        DEPKEY = "TPERSON.DEPKEY";
        VALIDUNTIL = "TPERSON.VALIDUNTIL";
        PREFERENCES = "TPERSON.PREFERENCES";
        LASTEDIT = "TPERSON.LASTEDIT";
        CREATED = "TPERSON.CREATED";
        DELETED = "TPERSON.DELETED";
        TOKENPASSWD = "TPERSON.TOKENPASSWD";
        TOKENEXPDATE = "TPERSON.TOKENEXPDATE";
        EMAILFREQUENCY = "TPERSON.EMAILFREQUENCY";
        EMAILLEAD = "TPERSON.EMAILLEAD";
        EMAILLASTREMINDED = "TPERSON.EMAILLASTREMINDED";
        EMAILREMINDME = "TPERSON.EMAILREMINDME";
        PREFEMAILTYPE = "TPERSON.PREFEMAILTYPE";
        PREFLOCALE = "TPERSON.PREFLOCALE";
        MYDEFAULTREPORT = "TPERSON.MYDEFAULTREPORT";
        NOEMAILSPLEASE = "TPERSON.NOEMAILSPLEASE";
        REMINDMEASORIGINATOR = "TPERSON.REMINDMEASORIGINATOR";
        REMINDMEASMANAGER = "TPERSON.REMINDMEASMANAGER";
        REMINDMEASRESPONSIBLE = "TPERSON.REMINDMEASRESPONSIBLE";
        EMAILREMINDPLEVEL = "TPERSON.EMAILREMINDPLEVEL";
        EMAILREMINDSLEVEL = "TPERSON.EMAILREMINDSLEVEL";
        HOURSPERWORKDAY = "TPERSON.HOURSPERWORKDAY";
        HOURLYWAGE = "TPERSON.HOURLYWAGE";
        EXTRAHOURWAGE = "TPERSON.EXTRAHOURWAGE";
        EMPLOYEEID = "TPERSON.EMPLOYEEID";
        ISGROUP = "TPERSON.ISGROUP";
        USERLEVEL = "TPERSON.USERLEVEL";
        MAXASSIGNEDITEMS = "TPERSON.MAXASSIGNEDITEMS";
        MESSENGERURL = "TPERSON.MESSENGERURL";
        CALLURL = "TPERSON.CALLURL";
        SYMBOL = "TPERSON.SYMBOL";
        ICONKEY = "TPERSON.ICONKEY";
        SUBSTITUTEKEY = "TPERSON.SUBSTITUTEKEY";
        SUBSTITUTEACTIVE = "TPERSON.SUBSTITUTEACTIVE";
        TPUUID = "TPERSON.TPUUID";
        if (Torque.isInit())
        {
            try
            {
                Torque.getMapBuilder(TPersonMapBuilder.CLASS_NAME);
            }
            catch (TorqueException e)
            {
                log.error("Could not initialize Peer", e);
                throw new TorqueRuntimeException(e);
            }
        }
        else
        {
            Torque.registerMapBuilder(TPersonMapBuilder.CLASS_NAME);
        }
    }
 
    /** number of columns for this peer */
    public static final int numColumns =  44;

    /** A class that can be returned by this peer. */
    protected static final String CLASSNAME_DEFAULT =
        "com.aurel.track.persist.TPerson";

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
    public static List<TPerson> resultSet2Objects(java.sql.ResultSet results)
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
        return BaseTPersonPeer
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
        criteria.addSelectColumn(PKEY);
        criteria.addSelectColumn(FIRSTNAME);
        criteria.addSelectColumn(LASTNAME);
        criteria.addSelectColumn(LOGINNAME);
        criteria.addSelectColumn(EMAIL);
        criteria.addSelectColumn(PASSWD);
        criteria.addSelectColumn(SALT);
        criteria.addSelectColumn(FORGOTPASSWORDKEY);
        criteria.addSelectColumn(PHONE);
        criteria.addSelectColumn(DEPKEY);
        criteria.addSelectColumn(VALIDUNTIL);
        criteria.addSelectColumn(PREFERENCES);
        criteria.addSelectColumn(LASTEDIT);
        criteria.addSelectColumn(CREATED);
        criteria.addSelectColumn(DELETED);
        criteria.addSelectColumn(TOKENPASSWD);
        criteria.addSelectColumn(TOKENEXPDATE);
        criteria.addSelectColumn(EMAILFREQUENCY);
        criteria.addSelectColumn(EMAILLEAD);
        criteria.addSelectColumn(EMAILLASTREMINDED);
        criteria.addSelectColumn(EMAILREMINDME);
        criteria.addSelectColumn(PREFEMAILTYPE);
        criteria.addSelectColumn(PREFLOCALE);
        criteria.addSelectColumn(MYDEFAULTREPORT);
        criteria.addSelectColumn(NOEMAILSPLEASE);
        criteria.addSelectColumn(REMINDMEASORIGINATOR);
        criteria.addSelectColumn(REMINDMEASMANAGER);
        criteria.addSelectColumn(REMINDMEASRESPONSIBLE);
        criteria.addSelectColumn(EMAILREMINDPLEVEL);
        criteria.addSelectColumn(EMAILREMINDSLEVEL);
        criteria.addSelectColumn(HOURSPERWORKDAY);
        criteria.addSelectColumn(HOURLYWAGE);
        criteria.addSelectColumn(EXTRAHOURWAGE);
        criteria.addSelectColumn(EMPLOYEEID);
        criteria.addSelectColumn(ISGROUP);
        criteria.addSelectColumn(USERLEVEL);
        criteria.addSelectColumn(MAXASSIGNEDITEMS);
        criteria.addSelectColumn(MESSENGERURL);
        criteria.addSelectColumn(CALLURL);
        criteria.addSelectColumn(SYMBOL);
        criteria.addSelectColumn(ICONKEY);
        criteria.addSelectColumn(SUBSTITUTEKEY);
        criteria.addSelectColumn(SUBSTITUTEACTIVE);
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
    public static TPerson row2Object(Record row,
                                             int offset,
                                             Class cls)
        throws TorqueException
    {
        try
        {
            TPerson obj = (TPerson) cls.newInstance();
            TPersonPeer.populateObject(row, offset, obj);
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
                                      TPerson obj)
        throws TorqueException
    {
        try
        {
            obj.setObjectID(row.getValue(offset + 0).asIntegerObj());
            obj.setFirstName(row.getValue(offset + 1).asString());
            obj.setLastName(row.getValue(offset + 2).asString());
            obj.setLoginName(row.getValue(offset + 3).asString());
            obj.setEmail(row.getValue(offset + 4).asString());
            obj.setPasswd(row.getValue(offset + 5).asString());
            obj.setSalt(row.getValue(offset + 6).asString());
            obj.setForgotPasswordKey(row.getValue(offset + 7).asString());
            obj.setPhone(row.getValue(offset + 8).asString());
            obj.setDepartmentID(row.getValue(offset + 9).asIntegerObj());
            obj.setValidUntil(row.getValue(offset + 10).asUtilDate());
            obj.setPreferences(row.getValue(offset + 11).asString());
            obj.setLastEdit(row.getValue(offset + 12).asUtilDate());
            obj.setCreated(row.getValue(offset + 13).asUtilDate());
            obj.setDeleted(row.getValue(offset + 14).asString());
            obj.setTokenPasswd(row.getValue(offset + 15).asString());
            obj.setTokenExpDate(row.getValue(offset + 16).asUtilDate());
            obj.setEmailFrequency(row.getValue(offset + 17).asIntegerObj());
            obj.setEmailLead(row.getValue(offset + 18).asIntegerObj());
            obj.setEmailLastReminded(row.getValue(offset + 19).asUtilDate());
            obj.setEmailRemindMe(row.getValue(offset + 20).asString());
            obj.setPrefEmailType(row.getValue(offset + 21).asString());
            obj.setPrefLocale(row.getValue(offset + 22).asString());
            obj.setMyDefaultReport(row.getValue(offset + 23).asIntegerObj());
            obj.setNoEmailPlease(row.getValue(offset + 24).asIntegerObj());
            obj.setRemindMeAsOriginator(row.getValue(offset + 25).asString());
            obj.setRemindMeAsManager(row.getValue(offset + 26).asString());
            obj.setRemindMeAsResponsible(row.getValue(offset + 27).asString());
            obj.setEmailRemindPriorityLevel(row.getValue(offset + 28).asIntegerObj());
            obj.setEmailRemindSeverityLevel(row.getValue(offset + 29).asIntegerObj());
            obj.setHoursPerWorkDay(row.getValue(offset + 30).asDoubleObj());
            obj.setHourlyWage(row.getValue(offset + 31).asDoubleObj());
            obj.setExtraHourWage(row.getValue(offset + 32).asDoubleObj());
            obj.setEmployeeID(row.getValue(offset + 33).asString());
            obj.setIsgroup(row.getValue(offset + 34).asString());
            obj.setUserLevel(row.getValue(offset + 35).asIntegerObj());
            obj.setMaxAssignedItems(row.getValue(offset + 36).asIntegerObj());
            obj.setMessengerURL(row.getValue(offset + 37).asString());
            obj.setCALLURL(row.getValue(offset + 38).asString());
            obj.setSymbol(row.getValue(offset + 39).asString());
            obj.setIconKey(row.getValue(offset + 40).asIntegerObj());
            obj.setSubstituteID(row.getValue(offset + 41).asIntegerObj());
            obj.setSubstituteActive(row.getValue(offset + 42).asString());
            obj.setUuid(row.getValue(offset + 43).asString());
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
    public static List<TPerson> doSelect(Criteria criteria) throws TorqueException
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
    public static List<TPerson> doSelect(Criteria criteria, Connection con)
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
        return BaseTPersonPeer
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
    public static List<TPerson> populateObjects(List<Record> records)
        throws TorqueException
    {
        List<TPerson> results = new ArrayList<TPerson>(records.size());

        // populate the object(s)
        for (int i = 0; i < records.size(); i++)
        {
            Record row =  records.get(i);
            results.add(TPersonPeer.row2Object(row, 1,
                TPersonPeer.getOMClass()));
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
         BaseTPersonPeer
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


         selectCriteria.put(PKEY, criteria.remove(PKEY));












































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
         TPersonPeer
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
    public static List<TPerson> doSelect(TPerson obj) throws TorqueException
    {
        return doSelect(buildSelectCriteria(obj));
    }

    /**
     * Method to do inserts
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(TPerson obj) throws TorqueException
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
    public static void doUpdate(TPerson obj) throws TorqueException
    {
        doUpdate(buildCriteria(obj));
        obj.setModified(false);
    }

    /**
     * @param obj the data object to delete in the database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(TPerson obj) throws TorqueException
    {
        doDelete(buildSelectCriteria(obj));
    }

    /**
     * Method to do inserts.  This method is to be used during a transaction,
     * otherwise use the doInsert(TPerson) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to insert into the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(TPerson obj, Connection con)
        throws TorqueException
    {
        obj.setPrimaryKey(doInsert(buildCriteria(obj), con));
        obj.setNew(false);
        obj.setModified(false);
    }

    /**
     * Method to do update.  This method is to be used during a transaction,
     * otherwise use the doUpdate(TPerson) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to update in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(TPerson obj, Connection con)
        throws TorqueException
    {
        doUpdate(buildCriteria(obj), con);
        obj.setModified(false);
    }

    /**
     * Method to delete.  This method is to be used during a transaction,
     * otherwise use the doDelete(TPerson) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to delete in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(TPerson obj, Connection con)
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
        BaseTPersonPeer
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
            criteria.add(PKEY, pk);
        return criteria;
     }

    /** Build a Criteria object from the data object for this peer */
    public static Criteria buildCriteria( TPerson obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
        if (!obj.isNew())
        criteria.add(PKEY, obj.getObjectID());
        criteria.add(FIRSTNAME, obj.getFirstName());
        criteria.add(LASTNAME, obj.getLastName());
        criteria.add(LOGINNAME, obj.getLoginName());
        criteria.add(EMAIL, obj.getEmail());
        criteria.add(PASSWD, obj.getPasswd());
        criteria.add(SALT, obj.getSalt());
        criteria.add(FORGOTPASSWORDKEY, obj.getForgotPasswordKey());
        criteria.add(PHONE, obj.getPhone());
        criteria.add(DEPKEY, obj.getDepartmentID());
        criteria.add(VALIDUNTIL, obj.getValidUntil());
        criteria.add(PREFERENCES, obj.getPreferences());
        criteria.add(LASTEDIT, obj.getLastEdit());
        criteria.add(CREATED, obj.getCreated());
        criteria.add(DELETED, obj.getDeleted());
        criteria.add(TOKENPASSWD, obj.getTokenPasswd());
        criteria.add(TOKENEXPDATE, obj.getTokenExpDate());
        criteria.add(EMAILFREQUENCY, obj.getEmailFrequency());
        criteria.add(EMAILLEAD, obj.getEmailLead());
        criteria.add(EMAILLASTREMINDED, obj.getEmailLastReminded());
        criteria.add(EMAILREMINDME, obj.getEmailRemindMe());
        criteria.add(PREFEMAILTYPE, obj.getPrefEmailType());
        criteria.add(PREFLOCALE, obj.getPrefLocale());
        criteria.add(MYDEFAULTREPORT, obj.getMyDefaultReport());
        criteria.add(NOEMAILSPLEASE, obj.getNoEmailPlease());
        criteria.add(REMINDMEASORIGINATOR, obj.getRemindMeAsOriginator());
        criteria.add(REMINDMEASMANAGER, obj.getRemindMeAsManager());
        criteria.add(REMINDMEASRESPONSIBLE, obj.getRemindMeAsResponsible());
        criteria.add(EMAILREMINDPLEVEL, obj.getEmailRemindPriorityLevel());
        criteria.add(EMAILREMINDSLEVEL, obj.getEmailRemindSeverityLevel());
        criteria.add(HOURSPERWORKDAY, obj.getHoursPerWorkDay());
        criteria.add(HOURLYWAGE, obj.getHourlyWage());
        criteria.add(EXTRAHOURWAGE, obj.getExtraHourWage());
        criteria.add(EMPLOYEEID, obj.getEmployeeID());
        criteria.add(ISGROUP, obj.getIsgroup());
        criteria.add(USERLEVEL, obj.getUserLevel());
        criteria.add(MAXASSIGNEDITEMS, obj.getMaxAssignedItems());
        criteria.add(MESSENGERURL, obj.getMessengerURL());
        criteria.add(CALLURL, obj.getCALLURL());
        criteria.add(SYMBOL, obj.getSymbol());
        criteria.add(ICONKEY, obj.getIconKey());
        criteria.add(SUBSTITUTEKEY, obj.getSubstituteID());
        criteria.add(SUBSTITUTEACTIVE, obj.getSubstituteActive());
        criteria.add(TPUUID, obj.getUuid());
        return criteria;
    }

    /** Build a Criteria object from the data object for this peer, skipping all binary columns */
    public static Criteria buildSelectCriteria( TPerson obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
        if (!obj.isNew())
        {
            criteria.add(PKEY, obj.getObjectID());
        }
            criteria.add(FIRSTNAME, obj.getFirstName());
            criteria.add(LASTNAME, obj.getLastName());
            criteria.add(LOGINNAME, obj.getLoginName());
            criteria.add(EMAIL, obj.getEmail());
            criteria.add(PASSWD, obj.getPasswd());
            criteria.add(SALT, obj.getSalt());
            criteria.add(FORGOTPASSWORDKEY, obj.getForgotPasswordKey());
            criteria.add(PHONE, obj.getPhone());
            criteria.add(DEPKEY, obj.getDepartmentID());
            criteria.add(VALIDUNTIL, obj.getValidUntil());
            criteria.add(PREFERENCES, obj.getPreferences());
            criteria.add(LASTEDIT, obj.getLastEdit());
            criteria.add(CREATED, obj.getCreated());
            criteria.add(DELETED, obj.getDeleted());
            criteria.add(TOKENPASSWD, obj.getTokenPasswd());
            criteria.add(TOKENEXPDATE, obj.getTokenExpDate());
            criteria.add(EMAILFREQUENCY, obj.getEmailFrequency());
            criteria.add(EMAILLEAD, obj.getEmailLead());
            criteria.add(EMAILLASTREMINDED, obj.getEmailLastReminded());
            criteria.add(EMAILREMINDME, obj.getEmailRemindMe());
            criteria.add(PREFEMAILTYPE, obj.getPrefEmailType());
            criteria.add(PREFLOCALE, obj.getPrefLocale());
            criteria.add(MYDEFAULTREPORT, obj.getMyDefaultReport());
            criteria.add(NOEMAILSPLEASE, obj.getNoEmailPlease());
            criteria.add(REMINDMEASORIGINATOR, obj.getRemindMeAsOriginator());
            criteria.add(REMINDMEASMANAGER, obj.getRemindMeAsManager());
            criteria.add(REMINDMEASRESPONSIBLE, obj.getRemindMeAsResponsible());
            criteria.add(EMAILREMINDPLEVEL, obj.getEmailRemindPriorityLevel());
            criteria.add(EMAILREMINDSLEVEL, obj.getEmailRemindSeverityLevel());
            criteria.add(HOURSPERWORKDAY, obj.getHoursPerWorkDay());
            criteria.add(HOURLYWAGE, obj.getHourlyWage());
            criteria.add(EXTRAHOURWAGE, obj.getExtraHourWage());
            criteria.add(EMPLOYEEID, obj.getEmployeeID());
            criteria.add(ISGROUP, obj.getIsgroup());
            criteria.add(USERLEVEL, obj.getUserLevel());
            criteria.add(MAXASSIGNEDITEMS, obj.getMaxAssignedItems());
            criteria.add(MESSENGERURL, obj.getMessengerURL());
            criteria.add(CALLURL, obj.getCALLURL());
            criteria.add(SYMBOL, obj.getSymbol());
            criteria.add(ICONKEY, obj.getIconKey());
            criteria.add(SUBSTITUTEKEY, obj.getSubstituteID());
            criteria.add(SUBSTITUTEACTIVE, obj.getSubstituteActive());
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
    public static TPerson retrieveByPK(Integer pk)
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
    public static TPerson retrieveByPK(Integer pk, Connection con)
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
    public static TPerson retrieveByPK(ObjectKey pk)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        Connection db = null;
        TPerson retVal = null;
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
    public static TPerson retrieveByPK(ObjectKey pk, Connection con)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        Criteria criteria = buildCriteria(pk);
        List<TPerson> v = doSelect(criteria, con);
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
            return (TPerson)v.get(0);
        }
    }

    /**
     * Retrieve a multiple objects by pk
     *
     * @param pks List of primary keys
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TPerson> retrieveByPKs(List<ObjectKey> pks)
        throws TorqueException
    {
        Connection db = null;
        List<TPerson> retVal = null;
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
    public static List<TPerson> retrieveByPKs( List<ObjectKey> pks, Connection dbcon )
        throws TorqueException
    {
        List<TPerson> objs = null;
        if (pks == null || pks.size() == 0)
        {
            objs = new LinkedList<TPerson>();
        }
        else
        {
            Criteria criteria = new Criteria();
            criteria.addIn( PKEY, pks );
        objs = doSelect(criteria, dbcon);
        }
        return objs;
    }

 








    /**
     * selects a collection of TPerson objects pre-filled with their
     * TDepartment objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinTDepartment(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTDepartment(criteria, null);
    }

    /**
     * selects a collection of TPerson objects pre-filled with their
     * TDepartment objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinTDepartment(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TPersonPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TDepartmentPeer.addSelectColumns(criteria);

        criteria.addJoin(TPersonPeer.DEPKEY,
            TDepartmentPeer.PKEY);

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

        List<TPerson> results = new ArrayList<TPerson>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TPersonPeer.getOMClass();
            TPerson obj1 = (TPerson) TPersonPeer
                .row2Object(row, 1, omClass);
             omClass = TDepartmentPeer.getOMClass();
            TDepartment obj2 = (TDepartment) TDepartmentPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TDepartment temp_obj2 = (TDepartment) temp_obj1.getTDepartment();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTPersons();
                obj2.addTPerson(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TPerson objects pre-filled with their
     * TPrivateReportRepository objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinTPrivateReportRepository(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTPrivateReportRepository(criteria, null);
    }

    /**
     * selects a collection of TPerson objects pre-filled with their
     * TPrivateReportRepository objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinTPrivateReportRepository(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TPersonPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TPrivateReportRepositoryPeer.addSelectColumns(criteria);

        criteria.addJoin(TPersonPeer.MYDEFAULTREPORT,
            TPrivateReportRepositoryPeer.PKEY);

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

        List<TPerson> results = new ArrayList<TPerson>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TPersonPeer.getOMClass();
            TPerson obj1 = (TPerson) TPersonPeer
                .row2Object(row, 1, omClass);
             omClass = TPrivateReportRepositoryPeer.getOMClass();
            TPrivateReportRepository obj2 = (TPrivateReportRepository) TPrivateReportRepositoryPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TPrivateReportRepository temp_obj2 = (TPrivateReportRepository) temp_obj1.getTPrivateReportRepository();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTPersons();
                obj2.addTPerson(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TPerson objects pre-filled with their
     * TBLOB objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTBLOB(criteria, null);
    }

    /**
     * selects a collection of TPerson objects pre-filled with their
     * TBLOB objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinTBLOB(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TPersonPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TBLOBPeer.addSelectColumns(criteria);

        criteria.addJoin(TPersonPeer.ICONKEY,
            TBLOBPeer.OBJECTID);

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

        List<TPerson> results = new ArrayList<TPerson>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TPersonPeer.getOMClass();
            TPerson obj1 = (TPerson) TPersonPeer
                .row2Object(row, 1, omClass);
             omClass = TBLOBPeer.getOMClass();
            TBLOB obj2 = (TBLOB) TBLOBPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TBLOB temp_obj2 = (TBLOB) temp_obj1.getTBLOB();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTPersons();
                obj2.addTPerson(obj1);
            }
            results.add(obj1);
        }
        return results;
    }







    /**
     * selects a collection of TPerson objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinAllExceptTDepartment(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTDepartment(criteria, null);
    }

    /**
     * selects a collection of TPerson objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinAllExceptTDepartment(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;


        TPrivateReportRepositoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TPersonPeer.MYDEFAULTREPORT, TPrivateReportRepositoryPeer.PKEY);
        int offset3 = offset2 + TPrivateReportRepositoryPeer.numColumns;

        TBLOBPeer.addSelectColumns(criteria);
        criteria.addJoin(TPersonPeer.ICONKEY, TBLOBPeer.OBJECTID);
        int offset4 = offset3 + TBLOBPeer.numColumns;

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

        List<TPerson> results = new ArrayList<TPerson>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TPersonPeer.getOMClass();
            TPerson obj1 = (TPerson) TPersonPeer
                .row2Object(row, 1, omClass);





             omClass = TPrivateReportRepositoryPeer.getOMClass();
            TPrivateReportRepository obj2 = (TPrivateReportRepository) TPrivateReportRepositoryPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TPrivateReportRepository temp_obj2 = (TPrivateReportRepository) temp_obj1.getTPrivateReportRepository();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTPersons();
                obj2.addTPerson(obj1);
            }




             omClass = TBLOBPeer.getOMClass();
            TBLOB obj3 = (TBLOB) TBLOBPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TBLOB temp_obj3 = (TBLOB) temp_obj1.getTBLOB();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTPersons();
                obj3.addTPerson(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TPerson objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinAllExceptTPrivateReportRepository(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTPrivateReportRepository(criteria, null);
    }

    /**
     * selects a collection of TPerson objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinAllExceptTPrivateReportRepository(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TDepartmentPeer.addSelectColumns(criteria);
        criteria.addJoin(TPersonPeer.DEPKEY, TDepartmentPeer.PKEY);
        int offset3 = offset2 + TDepartmentPeer.numColumns;


        TBLOBPeer.addSelectColumns(criteria);
        criteria.addJoin(TPersonPeer.ICONKEY, TBLOBPeer.OBJECTID);
        int offset4 = offset3 + TBLOBPeer.numColumns;

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

        List<TPerson> results = new ArrayList<TPerson>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TPersonPeer.getOMClass();
            TPerson obj1 = (TPerson) TPersonPeer
                .row2Object(row, 1, omClass);




             omClass = TDepartmentPeer.getOMClass();
            TDepartment obj2 = (TDepartment) TDepartmentPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TDepartment temp_obj2 = (TDepartment) temp_obj1.getTDepartment();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTPersons();
                obj2.addTPerson(obj1);
            }





             omClass = TBLOBPeer.getOMClass();
            TBLOB obj3 = (TBLOB) TBLOBPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TBLOB temp_obj3 = (TBLOB) temp_obj1.getTBLOB();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTPersons();
                obj3.addTPerson(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TPerson objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinAllExceptTBLOB(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTBLOB(criteria, null);
    }

    /**
     * selects a collection of TPerson objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinAllExceptTBLOB(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TDepartmentPeer.addSelectColumns(criteria);
        criteria.addJoin(TPersonPeer.DEPKEY, TDepartmentPeer.PKEY);
        int offset3 = offset2 + TDepartmentPeer.numColumns;

        TPrivateReportRepositoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TPersonPeer.MYDEFAULTREPORT, TPrivateReportRepositoryPeer.PKEY);
        int offset4 = offset3 + TPrivateReportRepositoryPeer.numColumns;


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

        List<TPerson> results = new ArrayList<TPerson>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TPersonPeer.getOMClass();
            TPerson obj1 = (TPerson) TPersonPeer
                .row2Object(row, 1, omClass);




             omClass = TDepartmentPeer.getOMClass();
            TDepartment obj2 = (TDepartment) TDepartmentPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TDepartment temp_obj2 = (TDepartment) temp_obj1.getTDepartment();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTPersons();
                obj2.addTPerson(obj1);
            }




             omClass = TPrivateReportRepositoryPeer.getOMClass();
            TPrivateReportRepository obj3 = (TPrivateReportRepository) TPrivateReportRepositoryPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TPrivateReportRepository temp_obj3 = (TPrivateReportRepository) temp_obj1.getTPrivateReportRepository();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTPersons();
                obj3.addTPerson(obj1);
            }

            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TPerson objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinAllExceptTPerson(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTPerson(criteria, null);
    }

    /**
     * selects a collection of TPerson objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPersonPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TPerson> doSelectJoinAllExceptTPerson(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TDepartmentPeer.addSelectColumns(criteria);
        criteria.addJoin(TPersonPeer.DEPKEY, TDepartmentPeer.PKEY);
        int offset3 = offset2 + TDepartmentPeer.numColumns;

        TPrivateReportRepositoryPeer.addSelectColumns(criteria);
        criteria.addJoin(TPersonPeer.MYDEFAULTREPORT, TPrivateReportRepositoryPeer.PKEY);
        int offset4 = offset3 + TPrivateReportRepositoryPeer.numColumns;

        TBLOBPeer.addSelectColumns(criteria);
        criteria.addJoin(TPersonPeer.ICONKEY, TBLOBPeer.OBJECTID);

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

        List<TPerson> results = new ArrayList<TPerson>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TPersonPeer.getOMClass();
            TPerson obj1 = (TPerson) TPersonPeer
                .row2Object(row, 1, omClass);




             omClass = TDepartmentPeer.getOMClass();
            TDepartment obj2 = (TDepartment) TDepartmentPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TDepartment temp_obj2 = (TDepartment) temp_obj1.getTDepartment();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTPersons();
                obj2.addTPerson(obj1);
            }




             omClass = TPrivateReportRepositoryPeer.getOMClass();
            TPrivateReportRepository obj3 = (TPrivateReportRepository) TPrivateReportRepositoryPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TPrivateReportRepository temp_obj3 = (TPrivateReportRepository) temp_obj1.getTPrivateReportRepository();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTPersons();
                obj3.addTPerson(obj1);
            }




             omClass = TBLOBPeer.getOMClass();
            TBLOB obj4 = (TBLOB) TBLOBPeer
                .row2Object( row, offset4, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TPerson temp_obj1 =  results.get(j);
                TBLOB temp_obj4 = (TBLOB) temp_obj1.getTBLOB();
                if (temp_obj4.getPrimaryKey().equals(obj4.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj4.addTPerson(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj4.initTPersons();
                obj4.addTPerson(obj1);
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
