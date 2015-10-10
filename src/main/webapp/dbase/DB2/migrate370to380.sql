ALTER TABLE TCATEGORY ADD COLUMN TYPEFLAG INTEGER;
ALTER TABLE TSTATE ADD COLUMN PERCENTCOMPLETE INTEGER;
ALTER TABLE TWORKFLOW ADD COLUMN RESPONSIBLE INTEGER;
ALTER TABLE TREPORTLAYOUT ADD COLUMN QUERYID INTEGER;
ALTER TABLE TREPORTLAYOUT ADD COLUMN QUERYTYPE INTEGER;

CREATE  INDEX TREPORTLAYOUTINDEX4 ON TREPORTLAYOUT (QUERYID);

ALTER TABLE TDASHBOARDSCREEN ADD COLUMN PROJECT INTEGER;
ALTER TABLE TDASHBOARDSCREEN ADD COLUMN ENTITYTYPE INTEGER;

-----------------------------------------------------------------------------
-- TRECURRENCEPATTERN
-----------------------------------------------------------------------------
drop table TRECURRENCEPATTERN;

CREATE TABLE TRECURRENCEPATTERN
(
    OBJECTID INTEGER NOT NULL,
    RECURRENCEPERIOD INTEGER NOT NULL,
    PARAM1 INTEGER,
    PARAM2 INTEGER,
    PARAM3 INTEGER,
    DAYS VARCHAR (255),
    DATEISABSOLUTE VARCHAR (1) default 'Y',
    STARTDATE TIMESTAMP,
    ENDDATE TIMESTAMP,
    OCCURENCETYPE INTEGER,
    NOOFOCCURENCES INTEGER,
    TPUUID VARCHAR (36)
);

ALTER TABLE TRECURRENCEPATTERN
    ADD PRIMARY KEY (OBJECTID);


CREATE  INDEX TRECURRENCEPATTERNINDEX1 ON TRECURRENCEPATTERN (TPUUID);



COMMENT ON COLUMN TRECURRENCEPATTERN.RECURRENCEPERIOD IS 'daily, weekly, monthly or yearly';
COMMENT ON COLUMN TRECURRENCEPATTERN.DAYS IS 'the weekdays by weekly pattern';
COMMENT ON COLUMN TRECURRENCEPATTERN.DATEISABSOLUTE IS 'by monthly and yearly an absolute date';


-----------------------------------------------------------------------------
-- TREPORTPERSONSETTINGS
-----------------------------------------------------------------------------
drop table TREPORTPERSONSETTINGS;

CREATE TABLE TREPORTPERSONSETTINGS
(
    OBJECTID INTEGER NOT NULL,
    PERSON INTEGER NOT NULL,
    RECURRENCEPATTERN INTEGER,
    REPORTTEMPLATE INTEGER NOT NULL,
    TPUUID VARCHAR (36)
);

ALTER TABLE TREPORTPERSONSETTINGS
    ADD PRIMARY KEY (OBJECTID);


CREATE  INDEX TREPORTPERSONSETTINGSINDEX1 ON TREPORTPERSONSETTINGS (TPUUID);





-----------------------------------------------------------------------------
-- TREPORTPARAMETER
-----------------------------------------------------------------------------
drop table TREPORTPARAMETER;

CREATE TABLE TREPORTPARAMETER
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    PARAMVALUE VARCHAR (10000),
    REPORTPERSONSETTINGS INTEGER NOT NULL,
    TPUUID VARCHAR (36)
);

ALTER TABLE TREPORTPARAMETER
    ADD PRIMARY KEY (OBJECTID);


CREATE  INDEX TREPORTPARAMETERINDEX1 ON TREPORTPARAMETER (TPUUID);





-----------------------------------------------------------------------------
-- TMSPROJECTTASK
-----------------------------------------------------------------------------
drop table TMSPROJECTTASK;

CREATE TABLE TMSPROJECTTASK
(
    OBJECTID INTEGER NOT NULL,
    WORKITEM INTEGER NOT NULL,
    UNIQUEID INTEGER NOT NULL,
    TASKTYPE INTEGER NOT NULL,
    CONTACT VARCHAR (100),
    WBS VARCHAR (100),
    OUTLINENUMBER VARCHAR (100),
    DURATION VARCHAR (100),
    DURATIONFORMAT INTEGER,
    ESTIMATED VARCHAR (1) default 'N',
    MILESTONE VARCHAR (1) default 'N',
    SUMMARY VARCHAR (1) default 'N',
    ACTUALDURATION VARCHAR (100),
    REMAININGDURATION VARCHAR (100),
    CONSTRAINTTYPE INTEGER,
    CONSTRAINTDATE TIMESTAMP,
    DEADLINE TIMESTAMP,
    TPUUID VARCHAR (36)
);

ALTER TABLE TMSPROJECTTASK
    ADD PRIMARY KEY (OBJECTID);


CREATE  INDEX TMSPROJECTTASKINDEX1 ON TMSPROJECTTASK (TPUUID);





-----------------------------------------------------------------------------
-- TMSPROJECTEXCHANGE
-----------------------------------------------------------------------------
drop table TMSPROJECTEXCHANGE;

CREATE TABLE TMSPROJECTEXCHANGE
(
    OBJECTID INTEGER NOT NULL,
    EXCHANGEDIRECTION INTEGER NOT NULL,
    ENTITYID INTEGER NOT NULL,
    ENTITYTYPE INTEGER NOT NULL,
    FILENAME VARCHAR (255),
    FILECONTENT CLOB,
    CHANGEDBY INTEGER,
    LASTEDIT TIMESTAMP,
    TPUUID VARCHAR (36)
);

ALTER TABLE TMSPROJECTEXCHANGE
    ADD PRIMARY KEY (OBJECTID);


CREATE  INDEX TMSPROJECTEXCHANGEINDEX1 ON TMSPROJECTEXCHANGE (TPUUID);

ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_4 FOREIGN KEY (RESPONSIBLE)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TDASHBOARDSCREEN
    ADD CONSTRAINT TDASHBOARDSCR_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;


-----------------------------------------------------------------------------
-- TRECURRENCEPATTERN: FOREIGN KEYS
-----------------------------------------------------------------------------


-----------------------------------------------------------------------------
-- TREPORTPERSONSETTINGS: FOREIGN KEYS
-----------------------------------------------------------------------------
ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSON_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;
ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSON_FK_2 FOREIGN KEY (RECURRENCEPATTERN)
    REFERENCES TRECURRENCEPATTERN (OBJECTID)
;
ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSON_FK_3 FOREIGN KEY (REPORTTEMPLATE)
    REFERENCES TEXPORTTEMPLATE (OBJECTID)
;


-----------------------------------------------------------------------------
-- TREPORTPARAMETER: FOREIGN KEYS
-----------------------------------------------------------------------------
ALTER TABLE TREPORTPARAMETER
    ADD CONSTRAINT TREPORTPARAME_FK_1 FOREIGN KEY (REPORTPERSONSETTINGS)
    REFERENCES TREPORTPERSONSETTINGS (OBJECTID)
;


-----------------------------------------------------------------------------
-- TMSPROJECTTASK: FOREIGN KEYS
-----------------------------------------------------------------------------
ALTER TABLE TMSPROJECTTASK
    ADD CONSTRAINT TMSPROJECTTAS_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;


-----------------------------------------------------------------------------
-- TMSPROJECTEXCHANGE: FOREIGN KEYS
-----------------------------------------------------------------------------
ALTER TABLE TMSPROJECTEXCHANGE
    ADD CONSTRAINT TMSPROJECTEXC_FK_1 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(106, 'TRECURRENCEPATTERN', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(107, 'TREPORTPERSONSETTINGS', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(108, 'TREPORTPARAMETER', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(109, 'TMSPROJECTTASK', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(110, 'TMSPROJECTEXCHANGE', 1, 1);

INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-1,'Task',1,-3,'task.gif','8e2216df-370c-446e-9348-a20936854b49');
INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-2,'Sprint',2,-2,'sprint.gif','f150b9e8-55da-4ab7-95bd-0d19e8cceb46');
INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-3,'Meeting',3,-1,'meeting.gif', '9ca87dc7-cf62-41a3-bce1-e13d0f5046e1');