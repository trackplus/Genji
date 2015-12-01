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
public class TWorkflowActivityMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "com.aurel.track.persist.map.TWorkflowActivityMapBuilder";

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

        dbMap.addTable("TWORKFLOWACTIVITY");
        TableMap tMap = dbMap.getTable("TWORKFLOWACTIVITY");
        tMap.setJavaName("TWorkflowActivity");
        tMap.setOMClass( com.aurel.track.persist.TWorkflowActivity.class );
        tMap.setPeerClass( com.aurel.track.persist.TWorkflowActivityPeer.class );
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
  // ------------- Column: TRANSITIONACTIVITY --------------------
        cMap = new ColumnMap( "TRANSITIONACTIVITY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "TransitionActivity" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TWORKFLOWTRANSITION", "OBJECTID");
        cMap.setPosition(2);
        tMap.addColumn(cMap);
  // ------------- Column: STATIONENTRYACTIVITY --------------------
        cMap = new ColumnMap( "STATIONENTRYACTIVITY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "StationEntryActivity" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TWORKFLOWSTATION", "OBJECTID");
        cMap.setPosition(3);
        tMap.addColumn(cMap);
  // ------------- Column: STATIONEXITACTIVITY --------------------
        cMap = new ColumnMap( "STATIONEXITACTIVITY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "StationExitActivity" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TWORKFLOWSTATION", "OBJECTID");
        cMap.setPosition(4);
        tMap.addColumn(cMap);
  // ------------- Column: STATIONDOACTIVITY --------------------
        cMap = new ColumnMap( "STATIONDOACTIVITY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "StationDoActivity" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TWORKFLOWSTATION", "OBJECTID");
        cMap.setPosition(5);
        tMap.addColumn(cMap);
  // ------------- Column: ACTIVITYTYPE --------------------
        cMap = new ColumnMap( "ACTIVITYTYPE", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(true);
        cMap.setJavaName( "ActivityType" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("whether it is GroovyScript, new man, new resp, escalation, create child issue, send e-mail etc.");
        cMap.setInheritance("false");
        cMap.setPosition(6);
        tMap.addColumn(cMap);
  // ------------- Column: ACTIVITYPARAMS --------------------
        cMap = new ColumnMap( "ACTIVITYPARAMS", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ActivityParams" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("parameters for some activity types: like for sending e-mail");
        cMap.setInheritance("false");
        cMap.setSize( 8191 );
        cMap.setPosition(7);
        tMap.addColumn(cMap);
  // ------------- Column: GROOVYSCRIPT --------------------
        cMap = new ColumnMap( "GROOVYSCRIPT", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "GroovyScript" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TSCRIPTS", "OBJECTID");
        cMap.setPosition(8);
        tMap.addColumn(cMap);
  // ------------- Column: NEWMAN --------------------
        cMap = new ColumnMap( "NEWMAN", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "NewMan" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TPERSON", "PKEY");
        cMap.setPosition(9);
        tMap.addColumn(cMap);
  // ------------- Column: NEWRESP --------------------
        cMap = new ColumnMap( "NEWRESP", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "NewResp" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TPERSON", "PKEY");
        cMap.setPosition(10);
        tMap.addColumn(cMap);
  // ------------- Column: FIELDSETTERRELATION --------------------
        cMap = new ColumnMap( "FIELDSETTERRELATION", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "FieldSetterRelation" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(11);
        tMap.addColumn(cMap);
  // ------------- Column: PARAMNAME --------------------
        cMap = new ColumnMap( "PARAMNAME", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ParamName" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("an intuitive name for parameter settings by workflow assignment");
        cMap.setInheritance("false");
        cMap.setSize( 255 );
        cMap.setPosition(12);
        tMap.addColumn(cMap);
  // ------------- Column: FIELDSETMODE --------------------
        cMap = new ColumnMap( "FIELDSETMODE", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "FieldSetMode" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("default value or enforced value");
        cMap.setInheritance("false");
        cMap.setPosition(13);
        tMap.addColumn(cMap);
  // ------------- Column: SORTORDER --------------------
        cMap = new ColumnMap( "SORTORDER", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "SortOrder" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("executing the activities in a certain order might be important");
        cMap.setInheritance("false");
        cMap.setPosition(14);
        tMap.addColumn(cMap);
  // ------------- Column: SLA --------------------
        cMap = new ColumnMap( "SLA", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Sla" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TSLA", "OBJECTID");
        cMap.setPosition(15);
        tMap.addColumn(cMap);
  // ------------- Column: SCREEN --------------------
        cMap = new ColumnMap( "SCREEN", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Screen" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TSCREEN", "OBJECTID");
        cMap.setPosition(16);
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
        cMap.setPosition(17);
        tMap.addColumn(cMap);
        tMap.setUseInheritance(false);
    }
}
