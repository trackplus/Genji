connect "D:\Daten\trackdata\track370N.gdb"; 
drop database;
create database "D:\Daten\trackdata\track370N.gdb" DEFAULT CHARACTER SET UTF8; 

input "Firebird/id-table-schema.sql"; 
input "Firebird/track-schema.sql"; 
input "Firebird/quartz.sql"; 
input "populate.sql";

commit;
quit;

