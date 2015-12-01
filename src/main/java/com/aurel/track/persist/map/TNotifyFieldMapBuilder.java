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
public class TNotifyFieldMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "com.aurel.track.persist.map.TNotifyFieldMapBuilder";

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

        dbMap.addTable("TNOTIFYFIELD");
        TableMap tMap = dbMap.getTable("TNOTIFYFIELD");
        tMap.setJavaName("TNotifyField");
        tMap.setOMClass( com.aurel.track.persist.TNotifyField.class );
        tMap.setPeerClass( com.aurel.track.persist.TNotifyFieldPeer.class );
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
  // ------------- Column: FIELD --------------------
        cMap = new ColumnMap( "FIELD", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Field" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(2);
        tMap.addColumn(cMap);
  // ------------- Column: ACTIONTYPE --------------------
        cMap = new ColumnMap( "ACTIONTYPE", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(true);
        cMap.setJavaName( "ActionType" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(3);
        tMap.addColumn(cMap);
  // ------------- Column: FIELDTYPE --------------------
        cMap = new ColumnMap( "FIELDTYPE", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "FieldType" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(4);
        tMap.addColumn(cMap);
  // ------------- Column: NOTIFYTRIGGER --------------------
        cMap = new ColumnMap( "NOTIFYTRIGGER", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(true);
        cMap.setJavaName( "NotifyTrigger" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TNOTIFYTRIGGER", "OBJECTID");
        cMap.setPosition(5);
        tMap.addColumn(cMap);
  // ------------- Column: ORIGINATOR --------------------
        cMap = new ColumnMap( "ORIGINATOR", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Originator" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(6);
        tMap.addColumn(cMap);
  // ------------- Column: MANAGER --------------------
        cMap = new ColumnMap( "MANAGER", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Manager" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(7);
        tMap.addColumn(cMap);
  // ------------- Column: RESPONSIBLE --------------------
        cMap = new ColumnMap( "RESPONSIBLE", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Responsible" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(8);
        tMap.addColumn(cMap);
  // ------------- Column: CONSULTANT --------------------
        cMap = new ColumnMap( "CONSULTANT", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Consultant" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(9);
        tMap.addColumn(cMap);
  // ------------- Column: INFORMANT --------------------
        cMap = new ColumnMap( "INFORMANT", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Informant" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(10);
        tMap.addColumn(cMap);
  // ------------- Column: OBSERVER --------------------
        cMap = new ColumnMap( "OBSERVER", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Observer" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(11);
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
        cMap.setPosition(12);
        tMap.addColumn(cMap);
        tMap.setUseInheritance(false);
    }
}
