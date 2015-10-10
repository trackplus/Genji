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
public abstract class BaseTAttachmentVersionPeer
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
     * @deprecated Torque.getMapBuilder(TAttachmentVersionMapBuilder.CLASS_NAME) instead
     */
    public static MapBuilder getMapBuilder()
        throws TorqueException
    {
        return Torque.getMapBuilder(TAttachmentVersionMapBuilder.CLASS_NAME);
    }

    /** the column name for the OBJECTID field */
    public static final String OBJECTID;
    /** the column name for the WORKITEM field */
    public static final String WORKITEM;
    /** the column name for the CHANGEDBY field */
    public static final String CHANGEDBY;
    /** the column name for the DOCUMENTSTATE field */
    public static final String DOCUMENTSTATE;
    /** the column name for the FILENAME field */
    public static final String FILENAME;
    /** the column name for the ISURL field */
    public static final String ISURL;
    /** the column name for the FILESIZE field */
    public static final String FILESIZE;
    /** the column name for the MIMETYPE field */
    public static final String MIMETYPE;
    /** the column name for the LASTEDIT field */
    public static final String LASTEDIT;
    /** the column name for the VERSION field */
    public static final String VERSION;
    /** the column name for the DESCRIPTION field */
    public static final String DESCRIPTION;
    /** the column name for the CRYPTKEY field */
    public static final String CRYPTKEY;
    /** the column name for the ISENCRYPTED field */
    public static final String ISENCRYPTED;
    /** the column name for the ISDELETED field */
    public static final String ISDELETED;
    /** the column name for the TPUUID field */
    public static final String TPUUID;

    static
    {
        DATABASE_NAME = "track";
        TABLE_NAME = "TATTACHMENTVERSION";

        OBJECTID = "TATTACHMENTVERSION.OBJECTID";
        WORKITEM = "TATTACHMENTVERSION.WORKITEM";
        CHANGEDBY = "TATTACHMENTVERSION.CHANGEDBY";
        DOCUMENTSTATE = "TATTACHMENTVERSION.DOCUMENTSTATE";
        FILENAME = "TATTACHMENTVERSION.FILENAME";
        ISURL = "TATTACHMENTVERSION.ISURL";
        FILESIZE = "TATTACHMENTVERSION.FILESIZE";
        MIMETYPE = "TATTACHMENTVERSION.MIMETYPE";
        LASTEDIT = "TATTACHMENTVERSION.LASTEDIT";
        VERSION = "TATTACHMENTVERSION.VERSION";
        DESCRIPTION = "TATTACHMENTVERSION.DESCRIPTION";
        CRYPTKEY = "TATTACHMENTVERSION.CRYPTKEY";
        ISENCRYPTED = "TATTACHMENTVERSION.ISENCRYPTED";
        ISDELETED = "TATTACHMENTVERSION.ISDELETED";
        TPUUID = "TATTACHMENTVERSION.TPUUID";
        if (Torque.isInit())
        {
            try
            {
                Torque.getMapBuilder(TAttachmentVersionMapBuilder.CLASS_NAME);
            }
            catch (TorqueException e)
            {
                log.error("Could not initialize Peer", e);
                throw new TorqueRuntimeException(e);
            }
        }
        else
        {
            Torque.registerMapBuilder(TAttachmentVersionMapBuilder.CLASS_NAME);
        }
    }
 
    /** number of columns for this peer */
    public static final int numColumns =  15;

    /** A class that can be returned by this peer. */
    protected static final String CLASSNAME_DEFAULT =
        "com.aurel.track.persist.TAttachmentVersion";

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
    public static List<TAttachmentVersion> resultSet2Objects(java.sql.ResultSet results)
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
        return BaseTAttachmentVersionPeer
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
        criteria.addSelectColumn(WORKITEM);
        criteria.addSelectColumn(CHANGEDBY);
        criteria.addSelectColumn(DOCUMENTSTATE);
        criteria.addSelectColumn(FILENAME);
        criteria.addSelectColumn(ISURL);
        criteria.addSelectColumn(FILESIZE);
        criteria.addSelectColumn(MIMETYPE);
        criteria.addSelectColumn(LASTEDIT);
        criteria.addSelectColumn(VERSION);
        criteria.addSelectColumn(DESCRIPTION);
        criteria.addSelectColumn(CRYPTKEY);
        criteria.addSelectColumn(ISENCRYPTED);
        criteria.addSelectColumn(ISDELETED);
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
    public static TAttachmentVersion row2Object(Record row,
                                             int offset,
                                             Class cls)
        throws TorqueException
    {
        try
        {
            TAttachmentVersion obj = (TAttachmentVersion) cls.newInstance();
            TAttachmentVersionPeer.populateObject(row, offset, obj);
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
                                      TAttachmentVersion obj)
        throws TorqueException
    {
        try
        {
            obj.setObjectID(row.getValue(offset + 0).asIntegerObj());
            obj.setWorkItem(row.getValue(offset + 1).asIntegerObj());
            obj.setChangedBy(row.getValue(offset + 2).asIntegerObj());
            obj.setDocumentState(row.getValue(offset + 3).asIntegerObj());
            obj.setFileName(row.getValue(offset + 4).asString());
            obj.setIsUrl(row.getValue(offset + 5).asString());
            obj.setFileSize(row.getValue(offset + 6).asString());
            obj.setMimeType(row.getValue(offset + 7).asString());
            obj.setLastEdit(row.getValue(offset + 8).asUtilDate());
            obj.setVersion(row.getValue(offset + 9).asString());
            obj.setDescription(row.getValue(offset + 10).asString());
            obj.setCryptKey(row.getValue(offset + 11).asString());
            obj.setIsEncrypted(row.getValue(offset + 12).asString());
            obj.setIsDeleted(row.getValue(offset + 13).asString());
            obj.setUuid(row.getValue(offset + 14).asString());
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
    public static List<TAttachmentVersion> doSelect(Criteria criteria) throws TorqueException
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
    public static List<TAttachmentVersion> doSelect(Criteria criteria, Connection con)
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
        return BaseTAttachmentVersionPeer
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
    public static List<TAttachmentVersion> populateObjects(List<Record> records)
        throws TorqueException
    {
        List<TAttachmentVersion> results = new ArrayList<TAttachmentVersion>(records.size());

        // populate the object(s)
        for (int i = 0; i < records.size(); i++)
        {
            Record row =  records.get(i);
            results.add(TAttachmentVersionPeer.row2Object(row, 1,
                TAttachmentVersionPeer.getOMClass()));
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
         BaseTAttachmentVersionPeer
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
         TAttachmentVersionPeer
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
    public static List<TAttachmentVersion> doSelect(TAttachmentVersion obj) throws TorqueException
    {
        return doSelect(buildSelectCriteria(obj));
    }

    /**
     * Method to do inserts
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(TAttachmentVersion obj) throws TorqueException
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
    public static void doUpdate(TAttachmentVersion obj) throws TorqueException
    {
        doUpdate(buildCriteria(obj));
        obj.setModified(false);
    }

    /**
     * @param obj the data object to delete in the database.
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(TAttachmentVersion obj) throws TorqueException
    {
        doDelete(buildSelectCriteria(obj));
    }

    /**
     * Method to do inserts.  This method is to be used during a transaction,
     * otherwise use the doInsert(TAttachmentVersion) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to insert into the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doInsert(TAttachmentVersion obj, Connection con)
        throws TorqueException
    {
        obj.setPrimaryKey(doInsert(buildCriteria(obj), con));
        obj.setNew(false);
        obj.setModified(false);
    }

    /**
     * Method to do update.  This method is to be used during a transaction,
     * otherwise use the doUpdate(TAttachmentVersion) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to update in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doUpdate(TAttachmentVersion obj, Connection con)
        throws TorqueException
    {
        doUpdate(buildCriteria(obj), con);
        obj.setModified(false);
    }

    /**
     * Method to delete.  This method is to be used during a transaction,
     * otherwise use the doDelete(TAttachmentVersion) method.  It will take
     * care of the connection details internally.
     *
     * @param obj the data object to delete in the database.
     * @param con the connection to use
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static void doDelete(TAttachmentVersion obj, Connection con)
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
        BaseTAttachmentVersionPeer
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
    public static Criteria buildCriteria( TAttachmentVersion obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
        if (!obj.isNew())
        criteria.add(OBJECTID, obj.getObjectID());
        criteria.add(WORKITEM, obj.getWorkItem());
        criteria.add(CHANGEDBY, obj.getChangedBy());
        criteria.add(DOCUMENTSTATE, obj.getDocumentState());
        criteria.add(FILENAME, obj.getFileName());
        criteria.add(ISURL, obj.getIsUrl());
        criteria.add(FILESIZE, obj.getFileSize());
        criteria.add(MIMETYPE, obj.getMimeType());
        criteria.add(LASTEDIT, obj.getLastEdit());
        criteria.add(VERSION, obj.getVersion());
        criteria.add(DESCRIPTION, obj.getDescription());
        criteria.add(CRYPTKEY, obj.getCryptKey());
        criteria.add(ISENCRYPTED, obj.getIsEncrypted());
        criteria.add(ISDELETED, obj.getIsDeleted());
        criteria.add(TPUUID, obj.getUuid());
        return criteria;
    }

    /** Build a Criteria object from the data object for this peer, skipping all binary columns */
    public static Criteria buildSelectCriteria( TAttachmentVersion obj )
    {
        Criteria criteria = new Criteria(DATABASE_NAME);
        if (!obj.isNew())
        {
            criteria.add(OBJECTID, obj.getObjectID());
        }
            criteria.add(WORKITEM, obj.getWorkItem());
            criteria.add(CHANGEDBY, obj.getChangedBy());
            criteria.add(DOCUMENTSTATE, obj.getDocumentState());
            criteria.add(FILENAME, obj.getFileName());
            criteria.add(ISURL, obj.getIsUrl());
            criteria.add(FILESIZE, obj.getFileSize());
            criteria.add(MIMETYPE, obj.getMimeType());
            criteria.add(LASTEDIT, obj.getLastEdit());
            criteria.add(VERSION, obj.getVersion());
            criteria.add(DESCRIPTION, obj.getDescription());
            criteria.add(CRYPTKEY, obj.getCryptKey());
            criteria.add(ISENCRYPTED, obj.getIsEncrypted());
            criteria.add(ISDELETED, obj.getIsDeleted());
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
    public static TAttachmentVersion retrieveByPK(Integer pk)
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
    public static TAttachmentVersion retrieveByPK(Integer pk, Connection con)
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
    public static TAttachmentVersion retrieveByPK(ObjectKey pk)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        Connection db = null;
        TAttachmentVersion retVal = null;
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
    public static TAttachmentVersion retrieveByPK(ObjectKey pk, Connection con)
        throws TorqueException, NoRowsException, TooManyRowsException
    {
        Criteria criteria = buildCriteria(pk);
        List<TAttachmentVersion> v = doSelect(criteria, con);
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
            return (TAttachmentVersion)v.get(0);
        }
    }

    /**
     * Retrieve a multiple objects by pk
     *
     * @param pks List of primary keys
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    public static List<TAttachmentVersion> retrieveByPKs(List<ObjectKey> pks)
        throws TorqueException
    {
        Connection db = null;
        List<TAttachmentVersion> retVal = null;
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
    public static List<TAttachmentVersion> retrieveByPKs( List<ObjectKey> pks, Connection dbcon )
        throws TorqueException
    {
        List<TAttachmentVersion> objs = null;
        if (pks == null || pks.size() == 0)
        {
            objs = new LinkedList<TAttachmentVersion>();
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
     * selects a collection of TAttachmentVersion objects pre-filled with their
     * TWorkItem objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTWorkItem(criteria, null);
    }

    /**
     * selects a collection of TAttachmentVersion objects pre-filled with their
     * TWorkItem objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinTWorkItem(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TAttachmentVersionPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TWorkItemPeer.addSelectColumns(criteria);

        criteria.addJoin(TAttachmentVersionPeer.WORKITEM,
            TWorkItemPeer.WORKITEMKEY);

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

        List<TAttachmentVersion> results = new ArrayList<TAttachmentVersion>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TAttachmentVersionPeer.getOMClass();
            TAttachmentVersion obj1 = (TAttachmentVersion) TAttachmentVersionPeer
                .row2Object(row, 1, omClass);
             omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj2 = (TWorkItem) TWorkItemPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TAttachmentVersion temp_obj1 =  results.get(j);
                TWorkItem temp_obj2 = (TWorkItem) temp_obj1.getTWorkItem();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTAttachmentVersion(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTAttachmentVersions();
                obj2.addTAttachmentVersion(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TAttachmentVersion objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTPerson(criteria, null);
    }

    /**
     * selects a collection of TAttachmentVersion objects pre-filled with their
     * TPerson objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinTPerson(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TAttachmentVersionPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TPersonPeer.addSelectColumns(criteria);

        criteria.addJoin(TAttachmentVersionPeer.CHANGEDBY,
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

        List<TAttachmentVersion> results = new ArrayList<TAttachmentVersion>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TAttachmentVersionPeer.getOMClass();
            TAttachmentVersion obj1 = (TAttachmentVersion) TAttachmentVersionPeer
                .row2Object(row, 1, omClass);
             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TAttachmentVersion temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPerson();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTAttachmentVersion(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTAttachmentVersions();
                obj2.addTAttachmentVersion(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TAttachmentVersion objects pre-filled with their
     * TDocState objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinTDocState(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinTDocState(criteria, null);
    }

    /**
     * selects a collection of TAttachmentVersion objects pre-filled with their
     * TDocState objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinTDocState(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        TAttachmentVersionPeer.addSelectColumns(criteria);
        int offset = numColumns + 1;
        TDocStatePeer.addSelectColumns(criteria);

        criteria.addJoin(TAttachmentVersionPeer.DOCUMENTSTATE,
            TDocStatePeer.OBJECTID);

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

        List<TAttachmentVersion> results = new ArrayList<TAttachmentVersion>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TAttachmentVersionPeer.getOMClass();
            TAttachmentVersion obj1 = (TAttachmentVersion) TAttachmentVersionPeer
                .row2Object(row, 1, omClass);
             omClass = TDocStatePeer.getOMClass();
            TDocState obj2 = (TDocState) TDocStatePeer
                .row2Object(row, offset, omClass);

            boolean newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TAttachmentVersion temp_obj1 =  results.get(j);
                TDocState temp_obj2 = (TDocState) temp_obj1.getTDocState();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTAttachmentVersion(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTAttachmentVersions();
                obj2.addTAttachmentVersion(obj1);
            }
            results.add(obj1);
        }
        return results;
    }







    /**
     * selects a collection of TAttachmentVersion objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinAllExceptTWorkItem(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTWorkItem(criteria, null);
    }

    /**
     * selects a collection of TAttachmentVersion objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinAllExceptTWorkItem(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;


        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TAttachmentVersionPeer.CHANGEDBY, TPersonPeer.PKEY);
        int offset3 = offset2 + TPersonPeer.numColumns;

        TDocStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TAttachmentVersionPeer.DOCUMENTSTATE, TDocStatePeer.OBJECTID);

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

        List<TAttachmentVersion> results = new ArrayList<TAttachmentVersion>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TAttachmentVersionPeer.getOMClass();
            TAttachmentVersion obj1 = (TAttachmentVersion) TAttachmentVersionPeer
                .row2Object(row, 1, omClass);





             omClass = TPersonPeer.getOMClass();
            TPerson obj2 = (TPerson) TPersonPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TAttachmentVersion temp_obj1 =  results.get(j);
                TPerson temp_obj2 = (TPerson) temp_obj1.getTPerson();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTAttachmentVersion(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTAttachmentVersions();
                obj2.addTAttachmentVersion(obj1);
            }




             omClass = TDocStatePeer.getOMClass();
            TDocState obj3 = (TDocState) TDocStatePeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TAttachmentVersion temp_obj1 =  results.get(j);
                TDocState temp_obj3 = (TDocState) temp_obj1.getTDocState();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTAttachmentVersion(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTAttachmentVersions();
                obj3.addTAttachmentVersion(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TAttachmentVersion objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinAllExceptTPerson(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTPerson(criteria, null);
    }

    /**
     * selects a collection of TAttachmentVersion objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinAllExceptTPerson(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TWorkItemPeer.addSelectColumns(criteria);
        criteria.addJoin(TAttachmentVersionPeer.WORKITEM, TWorkItemPeer.WORKITEMKEY);
        int offset3 = offset2 + TWorkItemPeer.numColumns;


        TDocStatePeer.addSelectColumns(criteria);
        criteria.addJoin(TAttachmentVersionPeer.DOCUMENTSTATE, TDocStatePeer.OBJECTID);

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

        List<TAttachmentVersion> results = new ArrayList<TAttachmentVersion>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TAttachmentVersionPeer.getOMClass();
            TAttachmentVersion obj1 = (TAttachmentVersion) TAttachmentVersionPeer
                .row2Object(row, 1, omClass);




             omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj2 = (TWorkItem) TWorkItemPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TAttachmentVersion temp_obj1 =  results.get(j);
                TWorkItem temp_obj2 = (TWorkItem) temp_obj1.getTWorkItem();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTAttachmentVersion(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTAttachmentVersions();
                obj2.addTAttachmentVersion(obj1);
            }





             omClass = TDocStatePeer.getOMClass();
            TDocState obj3 = (TDocState) TDocStatePeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TAttachmentVersion temp_obj1 =  results.get(j);
                TDocState temp_obj3 = (TDocState) temp_obj1.getTDocState();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTAttachmentVersion(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTAttachmentVersions();
                obj3.addTAttachmentVersion(obj1);
            }
            results.add(obj1);
        }
        return results;
    }




    /**
     * selects a collection of TAttachmentVersion objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinAllExceptTDocState(Criteria criteria)
        throws TorqueException
    {
        return doSelectJoinAllExceptTDocState(criteria, null);
    }

    /**
     * selects a collection of TAttachmentVersion objects pre-filled with
     * all related objects.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttachmentVersionPeer.
     *
     * @throws TorqueException Any exceptions caught during processing will be
     *         rethrown wrapped into a TorqueException.
     */
    protected static List<TAttachmentVersion> doSelectJoinAllExceptTDocState(Criteria criteria, Connection conn)
        throws TorqueException
    {
        setDbName(criteria);

        addSelectColumns(criteria);
        int offset2 = numColumns + 1;

        TWorkItemPeer.addSelectColumns(criteria);
        criteria.addJoin(TAttachmentVersionPeer.WORKITEM, TWorkItemPeer.WORKITEMKEY);
        int offset3 = offset2 + TWorkItemPeer.numColumns;

        TPersonPeer.addSelectColumns(criteria);
        criteria.addJoin(TAttachmentVersionPeer.CHANGEDBY, TPersonPeer.PKEY);


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

        List<TAttachmentVersion> results = new ArrayList<TAttachmentVersion>();

        for (int i = 0; i < rows.size(); i++)
        {
            Record row =  rows.get(i);

            Class omClass = TAttachmentVersionPeer.getOMClass();
            TAttachmentVersion obj1 = (TAttachmentVersion) TAttachmentVersionPeer
                .row2Object(row, 1, omClass);




             omClass = TWorkItemPeer.getOMClass();
            TWorkItem obj2 = (TWorkItem) TWorkItemPeer
                .row2Object( row, offset2, omClass);

 boolean  newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TAttachmentVersion temp_obj1 =  results.get(j);
                TWorkItem temp_obj2 = (TWorkItem) temp_obj1.getTWorkItem();
                if (temp_obj2.getPrimaryKey().equals(obj2.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj2.addTAttachmentVersion(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj2.initTAttachmentVersions();
                obj2.addTAttachmentVersion(obj1);
            }




             omClass = TPersonPeer.getOMClass();
            TPerson obj3 = (TPerson) TPersonPeer
                .row2Object( row, offset3, omClass);

 newObject = true;
            for (int j = 0; j < results.size(); j++)
            {
                TAttachmentVersion temp_obj1 =  results.get(j);
                TPerson temp_obj3 = (TPerson) temp_obj1.getTPerson();
                if (temp_obj3.getPrimaryKey().equals(obj3.getPrimaryKey()))
                {
                    newObject = false;
                    temp_obj3.addTAttachmentVersion(obj1);
                    break;
                }
            }
            if (newObject)
            {
                obj3.initTAttachmentVersions();
                obj3.addTAttachmentVersion(obj1);
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