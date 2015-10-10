drop database if exists track400e;
create database track400e;

SET CHARACTER SET 'utf8';

GRANT ALL PRIVILEGES ON track400e.* TO 'trackp'@'localhost' IDENTIFIED BY 'tissi';
GRANT ALL ON track400e.* TO 'trackp'@'localhost';

flush privileges;

commit;

use track400e;

-- id table
source id-table-schema.sql

-- quartz
source quartz.sql

-- track schema
source track-schema.sql
