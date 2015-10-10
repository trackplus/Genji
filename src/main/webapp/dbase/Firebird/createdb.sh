#!/bin/bash
# createdb.sh -- Build Script for the "Track+" Interbase/Firebird database
# $Id: createdb.sh 658 2009-01-02 10:48:52Z friedj $

SYSDBA_PWD=masterkey
TRACKUSER=friedj
TRACKPWD=tissi
DATA_DIR=d:/Daten/trackdata
DBNAME=tracktest351.gdb
IBROOT=c:/Programme/Firebird

if [ -z "$SYSTEMDRIVE" ]
then
  SYSTEMDRIVE="/"
fi

#
# We now check for the database directory, and if necessary create it
# 
if [ -d "$DATA_DIR" ]
then
  echo "Directory for database exists. Good."
else
  echo "Directory for database does not exist. Creating $DATA_DIR"
  mkdir -m 770 -p $DATA_DIR
fi

# We got to fix a little Torque problem
ex track-schema.sql <<EOF
g/;ALTER/s/;ALTER/;\n\nALTER/g
wq!
EOF

#
# We now look for the Interbase/Firebird executables. This may take some
# time since we are using find. This can be accelerated by some hints in
# the top section of this file.
#
if [ -e "$IBROOT/bin/isql.exe" ]
then
  echo "Found isql.exe under $IBROOT/bin. Good."
else
  echo "The default \$IBROOT of $IBROOT is no good."
  echo "Now searching for interbase installation. This may take several minutes..."
  IBROOT=
fi

if [ -z "$IBROOT" ] && [ -e "/opt" ]
then
  echo "Searching for interbase in /opt..."
  IBROOT=`find /opt -name "interbase"` 
fi

if [ -z "$IBROOT" ] && [ -e "$SYSTEMDRIVE/Programme/Borland" ]
then
  echo "Searching for interbase in $SYSTEMDRIVE/Programme/Borland"
  IBROOT=`find $SYSTEMDRIVE/Programme/Borland -name "InterBase"`
fi

if [ -z "$IBROOT" ] && [ -e "/usr" ]
then
  echo "Searching for interbase in /usr" 
  IBROOT=`find /usr -name "interbase"`
fi

if [ -z "$IBROOT" ] && [ -e "$SYSTEMDRIVE" ]
then
  echo "Searching for interbase in $SYSTEMDRIVE"
  IBROOT=`find $SYSTEMDRIVE -name "InterBase"`
fi

if [ -e "$IBROOT" ]
then
  echo Using isql.exe in $IBROOT/bin/
  ISQLEXE=$IBROOT/bin/isql.exe
else
  echo Could not find isql.exe on $SYSTEMDRIVE. Exiting.
  exit 1
fi

$IBROOT/bin/gsec -display $TRACKUSER -user sysdba -password $SYSDBA_PWD 2> tmpgsec
if [ -s tmpgsec ]
then
  echo "User $TRACKUSER already exists in security database."
  echo "Changing password."
  $IBROOT/bin/gsec -modify $TRACKUSER -user sysdba -password $SYSDBA_PWD -pw $TRACKPWD  
else
  echo "Adding user $TRACKUSER to security database"
  $IBROOT/bin/gsec -add $TRACKUSER -user sysdba -password $SYSDBA_PWD -pw $TRACKPWD
fi
rm tmpgsec

if [ -e "$DATA_DIR/$DBNAME" ]
then
  echo "Moving $DATA_DIR/$DBNAME to $DATA_DIR/track_old.gdb" 
  mv $DATA_DIR/$DBNAME $DATA_DIR/track_old.gdb
fi

$ISQLEXE -u $TRACKUSER -p $TRACKPWD <<EOF
  create database "$DATA_DIR/$DBNAME";
  input "id-table-schema.sql";
  input "track-schema.sql";
  input "../populate.sql";
  quit;
EOF


# Now change Torque.properties file
if [ -d ../../WEB-INF ]
then
  PROPPATH=../../WEB-INF
else
  PROPPATH=../../etc
fi

echo "."
echo "Changing Torque.properties file"
ex $PROPPATH/etc/Torque.properties <<EOF
/jdbc:interbase
c
torque.database.track.url = jdbc:interbase://localhost/$DATA_DIR/$DBNAME
.
wq!
EOF



