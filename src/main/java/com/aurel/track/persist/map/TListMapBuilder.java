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
  */
public class TListMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "com.aurel.track.persist.map.TListMapBuilder";

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

        dbMap.addTable("TLIST");
        TableMap tMap = dbMap.getTable("TLIST");
        tMap.setJavaName("TList");
        tMap.setOMClass( com.aurel.track.persist.TList.class );
        tMap.setPeerClass( com.aurel.track.persist.TListPeer.class );
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
  // ------------- Column: NAME --------------------
        cMap = new ColumnMap( "NAME", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(true);
        cMap.setJavaName( "Name" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 255 );
        cMap.setPosition(2);
        tMap.addColumn(cMap);
  // ------------- Column: DESCRIPTION --------------------
        cMap = new ColumnMap( "DESCRIPTION", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Description" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 255 );
        cMap.setPosition(3);
        tMap.addColumn(cMap);
  // ------------- Column: TAGLABEL --------------------
        cMap = new ColumnMap( "TAGLABEL", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "TagLabel" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("A user defined tag used for filtering/grouping the lists");
        cMap.setInheritance("false");
        cMap.setSize( 50 );
        cMap.setPosition(4);
        tMap.addColumn(cMap);
  // ------------- Column: PARENTLIST --------------------
        cMap = new ColumnMap( "PARENTLIST", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ParentList" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TLIST", "OBJECTID");
        cMap.setPosition(5);
        tMap.addColumn(cMap);
  // ------------- Column: LISTTYPE --------------------
        cMap = new ColumnMap( "LISTTYPE", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ListType" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(6);
        tMap.addColumn(cMap);
  // ------------- Column: CHILDNUMBER --------------------
        cMap = new ColumnMap( "CHILDNUMBER", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ChildNumber" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(7);
        tMap.addColumn(cMap);
  // ------------- Column: DELETED --------------------
        cMap = new ColumnMap( "DELETED", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Deleted" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(8);
        tMap.addColumn(cMap);
  // ------------- Column: REPOSITORYTYPE --------------------
        cMap = new ColumnMap( "REPOSITORYTYPE", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "RepositoryType" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(9);
        tMap.addColumn(cMap);
  // ------------- Column: PROJECT --------------------
        cMap = new ColumnMap( "PROJECT", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Project" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TPROJECT", "PKEY");
        cMap.setPosition(10);
        tMap.addColumn(cMap);
  // ------------- Column: OWNER --------------------
        cMap = new ColumnMap( "OWNER", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Owner" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TPERSON", "PKEY");
        cMap.setPosition(11);
        tMap.addColumn(cMap);
  // ------------- Column: MOREPROPS --------------------
        cMap = new ColumnMap( "MOREPROPS", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MoreProps" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("parameters for configuring the list import/synchronisation");
        cMap.setInheritance("false");
        cMap.setSize( 8191 );
        cMap.setPosition(12);
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
        cMap.setPosition(13);
        tMap.addColumn(cMap);
        tMap.setUseInheritance(false);
    }
}
