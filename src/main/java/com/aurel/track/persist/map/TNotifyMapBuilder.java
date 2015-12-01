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
  */
public class TNotifyMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "com.aurel.track.persist.map.TNotifyMapBuilder";

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
    @Override
    public boolean isBuilt()
    {
        return (dbMap != null);
    }

    /**
     * Gets the databasemap this map builder built.
     *
     * @return the databasemap
     */
    @Override
    public DatabaseMap getDatabaseMap()
    {
        return this.dbMap;
    }

    /**
     * The doBuild() method builds the DatabaseMap
     *
     * @throws TorqueException
     */
    @Override
    public synchronized void doBuild() throws TorqueException
    {
        if ( isBuilt() ) {
            return;
        }
        dbMap = Torque.getDatabaseMap("track");

        dbMap.addTable("TNOTIFY");
        TableMap tMap = dbMap.getTable("TNOTIFY");
        tMap.setJavaName("TNotify");
        tMap.setOMClass( com.aurel.track.persist.TNotify.class );
        tMap.setPeerClass( com.aurel.track.persist.TNotifyPeer.class );
        tMap.setPrimaryKeyMethod(TableMap.ID_BROKER);
        tMap.setPrimaryKeyMethodInfo(tMap.getName());

        ColumnMap cMap = null;


  // ------------- Column: PKEY --------------------
        cMap = new ColumnMap( "PKEY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(true);
        cMap.setNotNull(true);
        cMap.setJavaName( "ObjectID" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("This table holds the notification configuration for each user.");
        cMap.setInheritance("false");
        cMap.setPosition(1);
        tMap.addColumn(cMap);
  // ------------- Column: PROJCATKEY --------------------
        cMap = new ColumnMap( "PROJCATKEY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ProjectCategoryID" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("Notify me only for this project component (category).");
        cMap.setInheritance("false");
        cMap.setForeignKey("TPROJCAT", "PKEY");
        cMap.setPosition(2);
        tMap.addColumn(cMap);
  // ------------- Column: STATEKEY --------------------
        cMap = new ColumnMap( "STATEKEY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "StateID" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("Notify me only for these statuses.");
        cMap.setInheritance("false");
        cMap.setForeignKey("TSTATE", "PKEY");
        cMap.setPosition(3);
        tMap.addColumn(cMap);
  // ------------- Column: PERSONKEY --------------------
        cMap = new ColumnMap( "PERSONKEY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(true);
        cMap.setJavaName( "PersonID" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TPERSON", "PKEY");
        cMap.setPosition(4);
        tMap.addColumn(cMap);
  // ------------- Column: WORKITEM --------------------
        cMap = new ColumnMap( "WORKITEM", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "WorkItem" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("If this is null, this pertains to the regular watch list, otherwise it pertains to this specific WORKITEM.");
        cMap.setInheritance("false");
        cMap.setForeignKey("TWORKITEM", "WORKITEMKEY");
        cMap.setPosition(5);
        tMap.addColumn(cMap);
  // ------------- Column: RACIROLE --------------------
        cMap = new ColumnMap( "RACIROLE", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "RaciRole" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("Abbreviation for RACI roles of (C)onsulted or (I)nformed");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
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
