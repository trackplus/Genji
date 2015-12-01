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
 * This table holds properties for each node in the cluster
 *
  */
public class TClusterNodeMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "com.aurel.track.persist.map.TClusterNodeMapBuilder";

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

        dbMap.addTable("CLUSTERNODE");
        TableMap tMap = dbMap.getTable("CLUSTERNODE");
        tMap.setJavaName("TClusterNode");
        tMap.setOMClass( com.aurel.track.persist.TClusterNode.class );
        tMap.setPeerClass( com.aurel.track.persist.TClusterNodePeer.class );
        tMap.setDescription("This table holds properties for each node in the cluster");
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
  // ------------- Column: NODEADDRESS --------------------
        cMap = new ColumnMap( "NODEADDRESS", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "NodeAddress" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("The ip address of this node");
        cMap.setInheritance("false");
        cMap.setSize( 40 );
        cMap.setPosition(2);
        tMap.addColumn(cMap);
  // ------------- Column: NODEURL --------------------
        cMap = new ColumnMap( "NODEURL", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "NodeURL" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("The URL where the managing instance of this node can be reached");
        cMap.setInheritance("false");
        cMap.setSize( 255 );
        cMap.setPosition(3);
        tMap.addColumn(cMap);
  // ------------- Column: LASTUPDATE --------------------
        cMap = new ColumnMap( "LASTUPDATE", tMap);
        cMap.setType( new Date() );
        cMap.setTorqueType( "TIMESTAMP" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "LastUpdate" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("This enables to clean the table in case of a crash");
        cMap.setInheritance("false");
        cMap.setPosition(4);
        tMap.addColumn(cMap);
  // ------------- Column: MASTERNODE --------------------
        cMap = new ColumnMap( "MASTERNODE", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MasterNode" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("If this is the master node, this value is > 0");
        cMap.setDefault("0");
        cMap.setInheritance("false");
        cMap.setPosition(5);
        tMap.addColumn(cMap);
  // ------------- Column: RELOADCONFIG --------------------
        cMap = new ColumnMap( "RELOADCONFIG", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ReloadConfig" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("If this is not 0, reload the configuration from TSITE");
        cMap.setDefault("0");
        cMap.setInheritance("false");
        cMap.setPosition(6);
        tMap.addColumn(cMap);
  // ------------- Column: RELOADCHANGES --------------------
        cMap = new ColumnMap( "RELOADCHANGES", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ReloadChanges" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("A comma separated string representing entity types to reload");
        cMap.setInheritance("false");
        cMap.setSize( 255 );
        cMap.setPosition(7);
        tMap.addColumn(cMap);
        tMap.setUseInheritance(false);
    }
}
