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
 * This table holds all user related information, like user name, e-mail, etc.
 *
  */
public class TPersonMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "com.aurel.track.persist.map.TPersonMapBuilder";

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

        dbMap.addTable("TPERSON");
        TableMap tMap = dbMap.getTable("TPERSON");
        tMap.setJavaName("TPerson");
        tMap.setOMClass( com.aurel.track.persist.TPerson.class );
        tMap.setPeerClass( com.aurel.track.persist.TPersonPeer.class );
        tMap.setDescription("This table holds all user related information, like user name, e-mail, etc.");
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
        cMap.setInheritance("false");
        cMap.setPosition(1);
        tMap.addColumn(cMap);
  // ------------- Column: FIRSTNAME --------------------
        cMap = new ColumnMap( "FIRSTNAME", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "FirstName" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 25 );
        cMap.setPosition(2);
        tMap.addColumn(cMap);
  // ------------- Column: LASTNAME --------------------
        cMap = new ColumnMap( "LASTNAME", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "LastName" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 25 );
        cMap.setPosition(3);
        tMap.addColumn(cMap);
  // ------------- Column: LOGINNAME --------------------
        cMap = new ColumnMap( "LOGINNAME", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(true);
        cMap.setJavaName( "LoginName" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 60 );
        cMap.setPosition(4);
        tMap.addColumn(cMap);
  // ------------- Column: EMAIL --------------------
        cMap = new ColumnMap( "EMAIL", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Email" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 60 );
        cMap.setPosition(5);
        tMap.addColumn(cMap);
  // ------------- Column: PASSWD --------------------
        cMap = new ColumnMap( "PASSWD", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Passwd" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 160 );
        cMap.setPosition(6);
        tMap.addColumn(cMap);
  // ------------- Column: SALT --------------------
        cMap = new ColumnMap( "SALT", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Salt" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 80 );
        cMap.setPosition(7);
        tMap.addColumn(cMap);
  // ------------- Column: FORGOTPASSWORDKEY --------------------
        cMap = new ColumnMap( "FORGOTPASSWORDKEY", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ForgotPasswordKey" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 100 );
        cMap.setPosition(8);
        tMap.addColumn(cMap);
  // ------------- Column: PHONE --------------------
        cMap = new ColumnMap( "PHONE", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Phone" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 18 );
        cMap.setPosition(9);
        tMap.addColumn(cMap);
  // ------------- Column: DEPKEY --------------------
        cMap = new ColumnMap( "DEPKEY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "DepartmentID" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TDEPARTMENT", "PKEY");
        cMap.setPosition(10);
        tMap.addColumn(cMap);
  // ------------- Column: VALIDUNTIL --------------------
        cMap = new ColumnMap( "VALIDUNTIL", tMap);
        cMap.setType( new Date() );
        cMap.setTorqueType( "TIMESTAMP" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ValidUntil" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(11);
        tMap.addColumn(cMap);
  // ------------- Column: PREFERENCES --------------------
        cMap = new ColumnMap( "PREFERENCES", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Preferences" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("This is a multi-purpose field. It contains a text in .properties format with a number of additional attributes.");
        cMap.setInheritance("false");
        cMap.setSize( 2000 );
        cMap.setPosition(12);
        tMap.addColumn(cMap);
  // ------------- Column: LASTEDIT --------------------
        cMap = new ColumnMap( "LASTEDIT", tMap);
        cMap.setType( new Date() );
        cMap.setTorqueType( "TIMESTAMP" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "LastEdit" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(13);
        tMap.addColumn(cMap);
  // ------------- Column: CREATED --------------------
        cMap = new ColumnMap( "CREATED", tMap);
        cMap.setType( new Date() );
        cMap.setTorqueType( "TIMESTAMP" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Created" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(14);
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
        cMap.setPosition(15);
        tMap.addColumn(cMap);
  // ------------- Column: TOKENPASSWD --------------------
        cMap = new ColumnMap( "TOKENPASSWD", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "TokenPasswd" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 80 );
        cMap.setPosition(16);
        tMap.addColumn(cMap);
  // ------------- Column: TOKENEXPDATE --------------------
        cMap = new ColumnMap( "TOKENEXPDATE", tMap);
        cMap.setType( new Date() );
        cMap.setTorqueType( "TIMESTAMP" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "TokenExpDate" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(17);
        tMap.addColumn(cMap);
  // ------------- Column: EMAILFREQUENCY --------------------
        cMap = new ColumnMap( "EMAILFREQUENCY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "EmailFrequency" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(18);
        tMap.addColumn(cMap);
  // ------------- Column: EMAILLEAD --------------------
        cMap = new ColumnMap( "EMAILLEAD", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "EmailLead" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(19);
        tMap.addColumn(cMap);
  // ------------- Column: EMAILLASTREMINDED --------------------
        cMap = new ColumnMap( "EMAILLASTREMINDED", tMap);
        cMap.setType( new Date() );
        cMap.setTorqueType( "TIMESTAMP" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "EmailLastReminded" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(20);
        tMap.addColumn(cMap);
  // ------------- Column: EMAILREMINDME --------------------
        cMap = new ColumnMap( "EMAILREMINDME", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "EmailRemindMe" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(21);
        tMap.addColumn(cMap);
  // ------------- Column: PREFEMAILTYPE --------------------
        cMap = new ColumnMap( "PREFEMAILTYPE", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "PrefEmailType" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("Plain");
        cMap.setInheritance("false");
        cMap.setSize( 15 );
        cMap.setPosition(22);
        tMap.addColumn(cMap);
  // ------------- Column: PREFLOCALE --------------------
        cMap = new ColumnMap( "PREFLOCALE", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "PrefLocale" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 10 );
        cMap.setPosition(23);
        tMap.addColumn(cMap);
  // ------------- Column: MYDEFAULTREPORT --------------------
        cMap = new ColumnMap( "MYDEFAULTREPORT", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MyDefaultReport" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setForeignKey("TPRIVATEREPORTREPOSITORY", "PKEY");
        cMap.setPosition(24);
        tMap.addColumn(cMap);
  // ------------- Column: NOEMAILSPLEASE --------------------
        cMap = new ColumnMap( "NOEMAILSPLEASE", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "NoEmailPlease" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(25);
        tMap.addColumn(cMap);
  // ------------- Column: REMINDMEASORIGINATOR --------------------
        cMap = new ColumnMap( "REMINDMEASORIGINATOR", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "RemindMeAsOriginator" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(26);
        tMap.addColumn(cMap);
  // ------------- Column: REMINDMEASMANAGER --------------------
        cMap = new ColumnMap( "REMINDMEASMANAGER", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "RemindMeAsManager" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("Y");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(27);
        tMap.addColumn(cMap);
  // ------------- Column: REMINDMEASRESPONSIBLE --------------------
        cMap = new ColumnMap( "REMINDMEASRESPONSIBLE", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "RemindMeAsResponsible" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("Y");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(28);
        tMap.addColumn(cMap);
  // ------------- Column: EMAILREMINDPLEVEL --------------------
        cMap = new ColumnMap( "EMAILREMINDPLEVEL", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "EmailRemindPriorityLevel" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(29);
        tMap.addColumn(cMap);
  // ------------- Column: EMAILREMINDSLEVEL --------------------
        cMap = new ColumnMap( "EMAILREMINDSLEVEL", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "EmailRemindSeverityLevel" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(30);
        tMap.addColumn(cMap);
  // ------------- Column: HOURSPERWORKDAY --------------------
        cMap = new ColumnMap( "HOURSPERWORKDAY", tMap);
        cMap.setType( new Double(0) );
        cMap.setTorqueType( "DOUBLE" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "HoursPerWorkDay" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(31);
        tMap.addColumn(cMap);
  // ------------- Column: HOURLYWAGE --------------------
        cMap = new ColumnMap( "HOURLYWAGE", tMap);
        cMap.setType( new Double(0) );
        cMap.setTorqueType( "DOUBLE" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "HourlyWage" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(32);
        tMap.addColumn(cMap);
  // ------------- Column: EXTRAHOURWAGE --------------------
        cMap = new ColumnMap( "EXTRAHOURWAGE", tMap);
        cMap.setType( new Double(0) );
        cMap.setTorqueType( "DOUBLE" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ExtraHourWage" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(33);
        tMap.addColumn(cMap);
  // ------------- Column: EMPLOYEEID --------------------
        cMap = new ColumnMap( "EMPLOYEEID", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "EmployeeID" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 30 );
        cMap.setPosition(34);
        tMap.addColumn(cMap);
  // ------------- Column: ISGROUP --------------------
        cMap = new ColumnMap( "ISGROUP", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Isgroup" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("Groups are treated like users in most cases.");
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(35);
        tMap.addColumn(cMap);
  // ------------- Column: USERLEVEL --------------------
        cMap = new ColumnMap( "USERLEVEL", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "UserLevel" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("The user capability level, e.g. full user, limited user, sysadmin, sysman");
        cMap.setInheritance("false");
        cMap.setPosition(36);
        tMap.addColumn(cMap);
  // ------------- Column: MAXASSIGNEDITEMS --------------------
        cMap = new ColumnMap( "MAXASSIGNEDITEMS", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MaxAssignedItems" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("maximal number of assigned items for this person");
        cMap.setInheritance("false");
        cMap.setPosition(37);
        tMap.addColumn(cMap);
  // ------------- Column: MESSENGERURL --------------------
        cMap = new ColumnMap( "MESSENGERURL", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MessengerURL" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("the instant messenger URL");
        cMap.setInheritance("false");
        cMap.setSize( 255 );
        cMap.setPosition(38);
        tMap.addColumn(cMap);
  // ------------- Column: CALLURL --------------------
        cMap = new ColumnMap( "CALLURL", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "CALLURL" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("the voice URL");
        cMap.setInheritance("false");
        cMap.setSize( 255 );
        cMap.setPosition(39);
        tMap.addColumn(cMap);
  // ------------- Column: SYMBOL --------------------
        cMap = new ColumnMap( "SYMBOL", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "Symbol" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("File name for a photo");
        cMap.setInheritance("false");
        cMap.setSize( 255 );
        cMap.setPosition(40);
        tMap.addColumn(cMap);
  // ------------- Column: ICONKEY --------------------
        cMap = new ColumnMap( "ICONKEY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "IconKey" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("The photo stored in the database blob");
        cMap.setInheritance("false");
        cMap.setForeignKey("TBLOB", "OBJECTID");
        cMap.setPosition(41);
        tMap.addColumn(cMap);
  // ------------- Column: SUBSTITUTEKEY --------------------
        cMap = new ColumnMap( "SUBSTITUTEKEY", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "SubstituteID" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("The substituting preson");
        cMap.setInheritance("false");
        cMap.setForeignKey("TPERSON", "PKEY");
        cMap.setPosition(42);
        tMap.addColumn(cMap);
  // ------------- Column: SUBSTITUTEACTIVE --------------------
        cMap = new ColumnMap( "SUBSTITUTEACTIVE", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "SubstituteActive" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDescription("Substitute person is active");
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(43);
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
        cMap.setPosition(44);
        tMap.addColumn(cMap);
        tMap.setUseInheritance(false);
    }
}
