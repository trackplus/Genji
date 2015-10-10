#!/bin/sh
IBROOT=/opt/interbase

$IBROOT/bin/gbak -BACKUP_DATABASE -USER SYSDBA -PASSWORD superboy $IBROOT/db/track.gdb $IBROOT/backups/track.gbk

