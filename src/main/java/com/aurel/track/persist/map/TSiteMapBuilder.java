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
public class TSiteMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "com.aurel.track.persist.map.TSiteMapBuilder";

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

        dbMap.addTable("TSITE");
        TableMap tMap = dbMap.getTable("TSITE");
        tMap.setJavaName("TSite");
        tMap.setOMClass( com.aurel.track.persist.TSite.class );
        tMap.setPeerClass( com.aurel.track.persist.TSitePeer.class );
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
  // ------------- Column: TRACKVERSION --------------------
        cMap = new ColumnMap( "TRACKVERSION", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "TrackVersion" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 10 );
        cMap.setPosition(2);
        tMap.addColumn(cMap);
  // ------------- Column: DBVERSION --------------------
        cMap = new ColumnMap( "DBVERSION", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "DbVersion" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 10 );
        cMap.setPosition(3);
        tMap.addColumn(cMap);
  // ------------- Column: LICENSEEXT --------------------
        cMap = new ColumnMap( "LICENSEEXT", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "LicenseExtension" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 512 );
        cMap.setPosition(4);
        tMap.addColumn(cMap);
  // ------------- Column: EXPDATE --------------------
        cMap = new ColumnMap( "EXPDATE", tMap);
        cMap.setType( new Date() );
        cMap.setTorqueType( "DATE" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ExpDate" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(5);
        tMap.addColumn(cMap);
  // ------------- Column: NUMBEROFUSERS --------------------
        cMap = new ColumnMap( "NUMBEROFUSERS", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "NumberOfUsers" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(6);
        tMap.addColumn(cMap);
  // ------------- Column: TRACKEMAIL --------------------
        cMap = new ColumnMap( "TRACKEMAIL", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "TrackEmail" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 100 );
        cMap.setPosition(7);
        tMap.addColumn(cMap);
  // ------------- Column: SMTPSERVERNAME --------------------
        cMap = new ColumnMap( "SMTPSERVERNAME", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "SmtpServerName" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 100 );
        cMap.setPosition(8);
        tMap.addColumn(cMap);
  // ------------- Column: SMTPPORT --------------------
        cMap = new ColumnMap( "SMTPPORT", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "SmtpPort" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(9);
        tMap.addColumn(cMap);
  // ------------- Column: MAILENCODING --------------------
        cMap = new ColumnMap( "MAILENCODING", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MailEncoding" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 20 );
        cMap.setPosition(10);
        tMap.addColumn(cMap);
  // ------------- Column: SMTPUSER --------------------
        cMap = new ColumnMap( "SMTPUSER", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "SmtpUser" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 100 );
        cMap.setPosition(11);
        tMap.addColumn(cMap);
  // ------------- Column: SMTPPASSWORD --------------------
        cMap = new ColumnMap( "SMTPPASSWORD", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "SmtpPassWord" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 30 );
        cMap.setPosition(12);
        tMap.addColumn(cMap);
  // ------------- Column: POPSERVERNAME --------------------
        cMap = new ColumnMap( "POPSERVERNAME", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MailReceivingServerName" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 100 );
        cMap.setPosition(13);
        tMap.addColumn(cMap);
  // ------------- Column: POPPORT --------------------
        cMap = new ColumnMap( "POPPORT", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MailReceivingPort" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(14);
        tMap.addColumn(cMap);
  // ------------- Column: POPUSER --------------------
        cMap = new ColumnMap( "POPUSER", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MailReceivingUser" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 100 );
        cMap.setPosition(15);
        tMap.addColumn(cMap);
  // ------------- Column: POPPASSWORD --------------------
        cMap = new ColumnMap( "POPPASSWORD", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MailReceivingPassword" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 30 );
        cMap.setPosition(16);
        tMap.addColumn(cMap);
  // ------------- Column: RECEIVINGPROTOCOL --------------------
        cMap = new ColumnMap( "RECEIVINGPROTOCOL", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "MailReceivingProtocol" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 6 );
        cMap.setPosition(17);
        tMap.addColumn(cMap);
  // ------------- Column: ALLOWEDEMAILPATTERN --------------------
        cMap = new ColumnMap( "ALLOWEDEMAILPATTERN", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "AllowedEmailPattern" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 4096 );
        cMap.setPosition(18);
        tMap.addColumn(cMap);
  // ------------- Column: ISLDAPON --------------------
        cMap = new ColumnMap( "ISLDAPON", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "IsLDAPOn" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(19);
        tMap.addColumn(cMap);
  // ------------- Column: LDAPSERVERURL --------------------
        cMap = new ColumnMap( "LDAPSERVERURL", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "LdapServerURL" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 100 );
        cMap.setPosition(20);
        tMap.addColumn(cMap);
  // ------------- Column: LDAPATTRIBUTELOGINNAME --------------------
        cMap = new ColumnMap( "LDAPATTRIBUTELOGINNAME", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "LdapAttributeLoginName" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 30 );
        cMap.setPosition(21);
        tMap.addColumn(cMap);
  // ------------- Column: ATTACHMENTROOTDIR --------------------
        cMap = new ColumnMap( "ATTACHMENTROOTDIR", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "AttachmentRootDir" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 512 );
        cMap.setPosition(22);
        tMap.addColumn(cMap);
  // ------------- Column: SERVERURL --------------------
        cMap = new ColumnMap( "SERVERURL", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ServerURL" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 100 );
        cMap.setPosition(23);
        tMap.addColumn(cMap);
  // ------------- Column: DESCRIPTIONLENGTH --------------------
        cMap = new ColumnMap( "DESCRIPTIONLENGTH", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "DescriptionLength" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(24);
        tMap.addColumn(cMap);
  // ------------- Column: ISSELFREGISTERALLOWED --------------------
        cMap = new ColumnMap( "ISSELFREGISTERALLOWED", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "IsSelfRegisterAllowed" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("Y");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(25);
        tMap.addColumn(cMap);
  // ------------- Column: ISDEMOSITE --------------------
        cMap = new ColumnMap( "ISDEMOSITE", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "IsDemoSite" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("N");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(26);
        tMap.addColumn(cMap);
  // ------------- Column: USETRACKFROMADDRESS --------------------
        cMap = new ColumnMap( "USETRACKFROMADDRESS", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "CHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "UseTrackFromAddress" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setDefault("Y");
        cMap.setInheritance("false");
        cMap.setSize( 1 );
        cMap.setPosition(27);
        tMap.addColumn(cMap);
  // ------------- Column: RESERVEDUSE --------------------
        cMap = new ColumnMap( "RESERVEDUSE", tMap);
        cMap.setType( new Integer(0) );
        cMap.setTorqueType( "INTEGER" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "ReservedUse" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setPosition(28);
        tMap.addColumn(cMap);
  // ------------- Column: LDAPBINDDN --------------------
        cMap = new ColumnMap( "LDAPBINDDN", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "LdapBindDN" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 255 );
        cMap.setPosition(29);
        tMap.addColumn(cMap);
  // ------------- Column: LDAPBINDPASSW --------------------
        cMap = new ColumnMap( "LDAPBINDPASSW", tMap);
        cMap.setType( "" );
        cMap.setTorqueType( "VARCHAR" );
        cMap.setUsePrimitive(false);
        cMap.setPrimaryKey(false);
        cMap.setNotNull(false);
        cMap.setJavaName( "LdapBindPassword" );
        cMap.setAutoIncrement(false);
        cMap.setProtected(false);
        cMap.setInheritance("false");
        cMap.setSize( 20 );
        cMap.setPosition(30);
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
        cMap.setSize( 8000 );
        cMap.setPosition(31);
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
        cMap.setPosition(32);
        tMap.addColumn(cMap);
        tMap.setUseInheritance(false);
    }
}
