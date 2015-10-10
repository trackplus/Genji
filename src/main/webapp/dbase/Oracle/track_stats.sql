spool gatherstats.log
exec dbms_stats.delete_schema_stats(USER);
exec dbms_stats.gather_schema_stats(USER, cascade => -
     true, estimate_percent=> dbms_stats.auto_sample_size, - 
     degree => 4, method_opt => 'for all indexed columns size AUTO');
spool off
exit


