#!/bin/bash
C:/Programme/Firebird/bin/isql -u friedj -p tissi <<EOF

connect "D:\Daten\trackdata\tracktest351.gdb"; 
# drop database;
# create database "D:\Daten\trackdata\tracktest351.gdb"; 

input "id-table-schema.sql"; 
input "track-schema.sql"; 
input "quartz.sql";
input "../populate.sql";
commit;
quit;
EOF
