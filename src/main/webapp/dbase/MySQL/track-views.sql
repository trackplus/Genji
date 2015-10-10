# Some helpful views for reporting
# -----------------------------------------------------------------------
# ITEM
# -----------------------------------------------------------------------
drop view if exists ITEM;

CREATE VIEW ITEM
(
    OBJECTID, 
    ORIGINATOR,
    MANAGER,
    RESPONSIBLE,
    CHANGEDBY,
    PROJECTKEY,
	PROJECT,
    PRIORITYKEY,
    PRIORITY,
    SEVERITYKEY,
    SEVERITY,
    PARENTITEM,
    TITLE,
    DESCRIPTION,
    LASTEDIT,
    RELNOTICEDKEY,
    RELSCHEDULEDKEY,
    BUILD,
    STATEKEY,
    STATE,
    STARTDATE,
    ENDDATE,
    CREATED)
    
 AS SELECT 
 
    wi.WORKITEMKEY AS OBJECTID,
    orig.LASTNAME AS ORIGINATOR,
    man.LASTNAME AS MANAGER,
    resp.LASTNAME AS RESPONSIBLE,
    cb.LASTNAME AS CHANGEDBY,
    wi.PROJECTKEY,
    proj.LABEL AS PROJECT,
    wi.PRIORITYKEY,
    prio.LABEL AS PRIORITY,
    wi.SEVERITYKEY,
    sev.LABEL,
    wi.SUPERIORWORKITEM,
    wi.PACKAGESYNOPSYS,
    wi.PACKAGEDESCRIPTION,
    wi.LASTEDIT,
    wi.RELNOTICEDKEY,
    wi.RELSCHEDULEDKEY,
    wi.BUILD,
    wi.STATE,
    st.LABEL,
    wi.STARTDATE,
    wi.ENDDATE AS ENDATE,
    wi.CREATED AS CREATED

    FROM TWORKITEM wi
    
    JOIN TPERSON orig ON wi.ORIGINATOR=orig.PKEY 
    JOIN TPERSON man ON wi.OWNER=man.PKEY
    JOIN TPERSON resp ON wi.RESPONSIBLE=resp.PKEY
    JOIN TPERSON cb ON wi.CHANGEDBY=cb.PKEY
    JOIN TPROJECT proj ON wi.PROJECTKEY=proj.PKEY
    JOIN TPRIORITY prio ON wi.PRIORITYKEY=prio.PKEY
    JOIN TSEVERITY sev ON wi.SEVERITYKEY=sev.PKEY 
    JOIN TSTATE st ON wi.STATE=st.PKEY;
    