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

package com.aurel.track.persist.map;

import java.util.Date;
import java.math.BigDecimal;

import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.map.MapBuilder;
import org.apache.torque.map.DatabaseMap;
import org.apache.torque.map.TableMap;
import org.apache.torque.map.ColumnMap;
import org.apache.torque.map.InheritanceMap;

/**
 * store the last n executed queries with all data needed to execute them again
 *
  */
public class TLastExecutedQueryMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "com.aurel.track.persist.map.TLastExecutedQueryMapBuilder";

    /**
     * The database map.
     */
    private DatabaseMap dbMap = null;

    /**
     * Tells us if this DatabaseMapBuilder is built so that we
     * don't have to re-build it every time.
     *
     * @return true if this DatabaseMapBuilder is built
     */
    public boolean isBuilt()
    {
        return (dbMap != null);
    }

    /**
     * Gets the databasemap this map builder built.
     *
     * @return the databasemap
     */
    public DatabaseMap getDatabaseMap()
    {
        return this.dbMap;
    }

    /**
     * The doBuild() method builds the DatabaseMap
     *
     * @throws TorqueException
     */
    public synchronized void doBuild() throws TorqueException
    {
        if ( isBuilt() ) {
            return;
        }
        dbMap = Torque.getDatabaseMap("track");

        dbMap.addTable("TLASTEXECUTEDQUERY");
        TableMap tMap = dbMap.getTable("TLASTEXECUTEDQUERY");
        tMap.setJavaName("TLastExecutedQuery");
        tMap.setOMClass( com.aurel.track.persist.TLastExecutedQuery.class );
        tMap.setPeerClass( com.aurel.track.persist.TLastExecutedQueryPeer.class );
        tMap.setDescription("store the last n executed queries with all data needed to execute them again");
        tMap.setPrimaryKeyMethod(TableMap.ID_BROKER);
        tMap.setPrimaryKeyMethodInfo(tMap.getName());

        ColumnMap cMap = null;


  // ------------- Column: OBJECTID --------------------
        cMap = new ColumnMap( "OBJECTID", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(true);
        cMap.setNotNull(true);
        cMap.setJavaName( "ObjectID" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(1);
        tMap.addColumn(cMap);
  // ------------- Column: PERSON --------------------
        cMap = new ColumnMap( "PERSON", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(true);
        cMap.setJavaName( "Person" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("executed by this person");
        cMap.setInheritance("false");
        cMap.setForeignKey("TPERSON", "PKEY");
        cMap.setPosition(2);
        tMap.addColumn(cMap);
  // ------------- Column: QUERYTYPE --------------------
        cMap = new ColumnMap( "QUERYTYPE", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(true);
        cMap.setJavaName( "QueryType" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("can be dashboard link, saved/parametersized/instant tree query/tql query");
        cMap.setInheritance("false");
        cMap.setPosition(3);
        tMap.addColumn(cMap);
  // ------------- Column: QUERYCLOB --------------------
        cMap = new ColumnMap( "QUERYCLOB", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "QueryClob" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("the query key in TCLOB for saved but parameterized/instant queries and dashboard links:     the clob value contains either the xml (tree queries) or the properties (dashboard links)");
        cMap.setInheritance("false");
        cMap.setForeignKey("TCLOB", "OBJECTID");
        cMap.setPosition(4);
        tMap.addColumn(cMap);
  // ------------- Column: QUERYKEY --------------------
        cMap = new ColumnMap( "QUERYKEY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "QueryKey" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("key in queryRepositoryID or projectID or releaseID or basketID");
        cMap.setInheritance("false");
        cMap.setPosition(5);
        tMap.addColumn(cMap);
  // ------------- Column: LASTEXECUTEDTIME --------------------
        cMap = new ColumnMap( "LASTEXECUTEDTIME", tMap);
        cMap.setType( new Date() );
        cMap.setTorqueType( "TIMESTAMP" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "LastExecutedTime" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(6);
        tMap.addColumn(cMap);
  // ------------- Column: TPUUID --------------------
        cMap = new ColumnMap( "TPUUID", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Uuid" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 36 );
        cMap.setPosition(7);
        tMap.addColumn(cMap);
        tMap.setUseInheritance(false);
    }
}
