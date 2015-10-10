ALTER TABLE TREPORTLAYOUT ADD QUERYID INTEGER;
ALTER TABLE TREPORTLAYOUT ADD QUERYTYPE INTEGER;

CREATE INDEX TREPORTLAYOUTINDEX4 ON TREPORTLAYOUT (QUERYID);

ALTER TABLE TDASHBOARDSCREEN ADD PROJECT INTEGER;
ALTER TABLE TDASHBOARDSCREEN ADD ENTITYTYPE INTEGER;

ALTER TABLE TCATEGORY ADD TYPEFLAG INTEGER;
ALTER TABLE TSTATE ADD PERCENTCOMPLETE INTEGER;
ALTER TABLE TWORKFLOW ADD RESPONSIBLE INTEGER;

/* ---------------------------------------------------------------------- */
/* TRECURRENCEPATTERN                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TRECURRENCEPATTERN')
BEGIN
     DECLARE @reftable_105 nvarchar(60), @constraintname_105 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TRECURRENCEPATTERN'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_105, @constraintname_105
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_105+' drop constraint '+@constraintname_105)
       FETCH NEXT from refcursor into @reftable_105, @constraintname_105
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TRECURRENCEPATTERN
END;

CREATE TABLE TRECURRENCEPATTERN
(
    OBJECTID INT NOT NULL,
    RECURRENCEPERIOD INT NOT NULL,
    PARAM1 INT NULL,
    PARAM2 INT NULL,
    PARAM3 INT NULL,
    DAYS VARCHAR (255) NULL,
    DATEISABSOLUTE CHAR (1) default 'Y' NULL,
    STARTDATE DATETIME NULL,
    ENDDATE DATETIME NULL,
    OCCURENCETYPE INT NULL,
    NOOFOCCURENCES INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TRECURRENCEPATTERN_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TRECURRENCEPATTERNINDEX1 ON TRECURRENCEPATTERN (TPUUID);

/* ---------------------------------------------------------------------- */
/* TREPORTPERSONSETTINGS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTPERSONSETTINGS_FK_1')
    ALTER TABLE TREPORTPERSONSETTINGS DROP CONSTRAINT TREPORTPERSONSETTINGS_FK_1
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTPERSONSETTINGS_FK_2')
    ALTER TABLE TREPORTPERSONSETTINGS DROP CONSTRAINT TREPORTPERSONSETTINGS_FK_2
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTPERSONSETTINGS_FK_3')
    ALTER TABLE TREPORTPERSONSETTINGS DROP CONSTRAINT TREPORTPERSONSETTINGS_FK_3
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TREPORTPERSONSETTINGS')
BEGIN
     DECLARE @reftable_106 nvarchar(60), @constraintname_106 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TREPORTPERSONSETTINGS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_106, @constraintname_106
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_106+' drop constraint '+@constraintname_106)
       FETCH NEXT from refcursor into @reftable_106, @constraintname_106
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TREPORTPERSONSETTINGS
END;

CREATE TABLE TREPORTPERSONSETTINGS
(
    OBJECTID INT NOT NULL,
    PERSON INT NOT NULL,
    RECURRENCEPATTERN INT NULL,
    REPORTTEMPLATE INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREPORTPERSONSETTINGS_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREPORTPERSONSETTINGSINDEX1 ON TREPORTPERSONSETTINGS (TPUUID);

/* ---------------------------------------------------------------------- */
/* TREPORTPARAMETER                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTPARAMETER_FK_1')
    ALTER TABLE TREPORTPARAMETER DROP CONSTRAINT TREPORTPARAMETER_FK_1
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TREPORTPARAMETER')
BEGIN
     DECLARE @reftable_107 nvarchar(60), @constraintname_107 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TREPORTPARAMETER'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_107, @constraintname_107
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_107+' drop constraint '+@constraintname_107)
       FETCH NEXT from refcursor into @reftable_107, @constraintname_107
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TREPORTPARAMETER
END;

CREATE TABLE TREPORTPARAMETER
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    PARAMVALUE VARCHAR (3000) NULL,
    REPORTPERSONSETTINGS INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREPORTPARAMETER_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREPORTPARAMETERINDEX1 ON TREPORTPARAMETER (TPUUID);


/* ---------------------------------------------------------------------- */
/* TMSPROJECTTASK                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMSPROJECTTASK_FK_1')
    ALTER TABLE TMSPROJECTTASK DROP CONSTRAINT TMSPROJECTTASK_FK_1
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TMSPROJECTTASK')
BEGIN
     DECLARE @reftable_108 nvarchar(60), @constraintname_108 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TMSPROJECTTASK'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_108, @constraintname_108
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_108+' drop constraint '+@constraintname_108)
       FETCH NEXT from refcursor into @reftable_108, @constraintname_108
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TMSPROJECTTASK
END;

CREATE TABLE TMSPROJECTTASK
(
    OBJECTID INT NOT NULL,
    WORKITEM INT NOT NULL,
    UNIQUEID INT NOT NULL,
    TASKTYPE INT NOT NULL,
    CONTACT VARCHAR (100) NULL,
    WBS VARCHAR (100) NULL,
    OUTLINENUMBER VARCHAR (100) NULL,
    DURATION VARCHAR (100) NULL,
    DURATIONFORMAT INT NULL,
    ESTIMATED CHAR (1) default 'N' NULL,
    MILESTONE CHAR (1) default 'N' NULL,
    SUMMARY CHAR (1) default 'N' NULL,
    ACTUALDURATION VARCHAR (100) NULL,
    REMAININGDURATION VARCHAR (100) NULL,
    CONSTRAINTTYPE INT NULL,
    CONSTRAINTDATE DATETIME NULL,
    DEADLINE DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TMSPROJECTTASK_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TMSPROJECTTASKINDEX1 ON TMSPROJECTTASK (TPUUID);


/* ---------------------------------------------------------------------- */
/* TMSPROJECTEXCHANGE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMSPROJECTEXCHANGE_FK_1')
    ALTER TABLE TMSPROJECTEXCHANGE DROP CONSTRAINT TMSPROJECTEXCHANGE_FK_1
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TMSPROJECTEXCHANGE')
BEGIN
     DECLARE @reftable_109 nvarchar(60), @constraintname_109 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TMSPROJECTEXCHANGE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_109, @constraintname_109
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_109+' drop constraint '+@constraintname_109)
       FETCH NEXT from refcursor into @reftable_109, @constraintname_109
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TMSPROJECTEXCHANGE
END;

CREATE TABLE TMSPROJECTEXCHANGE
(
    OBJECTID INT NOT NULL,
    EXCHANGEDIRECTION INT NOT NULL,
    ENTITYID INT NOT NULL,
    ENTITYTYPE INT NOT NULL,
    FILENAME VARCHAR (255) NULL,
    FILECONTENT TEXT NULL,
    CHANGEDBY INT NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TMSPROJECTEXCHANGE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TMSPROJECTEXCHANGEINDEX1 ON TMSPROJECTEXCHANGE (TPUUID);

ALTER TABLE TDASHBOARDSCREEN
    ADD CONSTRAINT TDASHBOARDSCREEN_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY);

ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_4 FOREIGN KEY (RESPONSIBLE)
    REFERENCES TPERSON (PKEY);

ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSONSETTINGS_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY);

ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSONSETTINGS_FK_2 FOREIGN KEY (RECURRENCEPATTERN)
    REFERENCES TRECURRENCEPATTERN (OBJECTID);

ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSONSETTINGS_FK_3 FOREIGN KEY (REPORTTEMPLATE)
    REFERENCES TEXPORTTEMPLATE (OBJECTID);

ALTER TABLE TREPORTPARAMETER
    ADD CONSTRAINT TREPORTPARAMETER_FK_1 FOREIGN KEY (REPORTPERSONSETTINGS)
    REFERENCES TREPORTPERSONSETTINGS (OBJECTID);

ALTER TABLE TMSPROJECTTASK
    ADD CONSTRAINT TMSPROJECTTASK_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY);

ALTER TABLE TMSPROJECTEXCHANGE
    ADD CONSTRAINT TMSPROJECTEXCHANGE_FK_1 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY);

GO

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(106, 'TRECURRENCEPATTERN', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(107, 'TREPORTPERSONSETTINGS', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(108, 'TREPORTPARAMETER', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(109, 'TMSPROJECTTASK', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(110, 'TMSPROJECTEXCHANGE', 1, 1);

INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-1,'Task',1,-3,'task.gif','8e2216df-370c-446e-9348-a20936854b49');
INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-2,'Sprint',2,-2,'sprint.gif','f150b9e8-55da-4ab7-95bd-0d19e8cceb46');
INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-3,'Meeting',3,-1,'meeting.gif', '9ca87dc7-cf62-41a3-bce1-e13d0f5046e1');
