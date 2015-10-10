
/* ---------------------------------------------------------------------- */
/* TACL                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TACL_FK_1')
    ALTER TABLE TACL DROP CONSTRAINT TACL_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TACL_FK_2')
    ALTER TABLE TACL DROP CONSTRAINT TACL_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TACL_FK_3')
    ALTER TABLE TACL DROP CONSTRAINT TACL_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TACL')
BEGIN
     DECLARE @reftable_1 nvarchar(60), @constraintname_1 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TACL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_1, @constraintname_1
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_1+' drop constraint '+@constraintname_1)
       FETCH NEXT from refcursor into @reftable_1, @constraintname_1
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TACL
END
;

CREATE TABLE TACL
(
    PERSONKEY INT NOT NULL,
    ROLEKEY INT NOT NULL,
    PROJKEY INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TACL_PK PRIMARY KEY(PERSONKEY,ROLEKEY,PROJKEY));

CREATE  INDEX TACLINDEX1 ON TACL (TPUUID);




/* ---------------------------------------------------------------------- */
/* TBASELINE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TBASELINE_FK_1')
    ALTER TABLE TBASELINE DROP CONSTRAINT TBASELINE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TBASELINE_FK_2')
    ALTER TABLE TBASELINE DROP CONSTRAINT TBASELINE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TBASELINE')
BEGIN
     DECLARE @reftable_2 nvarchar(60), @constraintname_2 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TBASELINE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_2, @constraintname_2
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_2+' drop constraint '+@constraintname_2)
       FETCH NEXT from refcursor into @reftable_2, @constraintname_2
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TBASELINE
END
;

CREATE TABLE TBASELINE
(
    BLKEY INT NOT NULL,
    WORKITEMKEY INT NOT NULL,
    STARTDATE DATETIME NULL,
    ENDDATE DATETIME NULL,
    REASONFORCHANGE VARCHAR (7000) NULL,
    CHANGEDBY INT NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TBASELINE_PK PRIMARY KEY(BLKEY));

CREATE  INDEX TBASINDEX1 ON TBASELINE (WORKITEMKEY);
CREATE  INDEX TBASINDEX2 ON TBASELINE (CHANGEDBY);
CREATE  INDEX TBASINDEX3 ON TBASELINE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCATEGORY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCATEGORY_FK_1')
    ALTER TABLE TCATEGORY DROP CONSTRAINT TCATEGORY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCATEGORY')
BEGIN
     DECLARE @reftable_3 nvarchar(60), @constraintname_3 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCATEGORY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_3, @constraintname_3
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_3+' drop constraint '+@constraintname_3)
       FETCH NEXT from refcursor into @reftable_3, @constraintname_3
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCATEGORY
END
;

CREATE TABLE TCATEGORY
(
    PKEY INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    TOOLTIP VARCHAR (255) NULL,
    TYPEFLAG INT NULL,
    SORTORDER INT NULL,
    SYMBOL VARCHAR (255) NULL,
    ICONKEY INT NULL,
    ICONCHANGED CHAR (1) default 'N' NULL,
    CSSSTYLE VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCATEGORY_PK PRIMARY KEY(PKEY));

CREATE  INDEX TCATEGORYINDEX1 ON TCATEGORY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCLASS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCLASS_FK_1')
    ALTER TABLE TCLASS DROP CONSTRAINT TCLASS_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCLASS')
BEGIN
     DECLARE @reftable_4 nvarchar(60), @constraintname_4 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCLASS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_4, @constraintname_4
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_4+' drop constraint '+@constraintname_4)
       FETCH NEXT from refcursor into @reftable_4, @constraintname_4
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCLASS
END
;

CREATE TABLE TCLASS
(
    PKEY INT NOT NULL,
    LABEL VARCHAR (25) NOT NULL,
    PROJKEY INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCLASS_PK PRIMARY KEY(PKEY));

CREATE  INDEX TCLASSINDEX1 ON TCLASS (TPUUID);




/* ---------------------------------------------------------------------- */
/* TDEPARTMENT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDEPARTMENT_FK_1')
    ALTER TABLE TDEPARTMENT DROP CONSTRAINT TDEPARTMENT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDEPARTMENT_FK_2')
    ALTER TABLE TDEPARTMENT DROP CONSTRAINT TDEPARTMENT_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TDEPARTMENT')
BEGIN
     DECLARE @reftable_5 nvarchar(60), @constraintname_5 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TDEPARTMENT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_5, @constraintname_5
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_5+' drop constraint '+@constraintname_5)
       FETCH NEXT from refcursor into @reftable_5, @constraintname_5
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TDEPARTMENT
END
;

CREATE TABLE TDEPARTMENT
(
    PKEY INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    COSTCENTER INT NULL,
    PARENT INT NULL,
    DOMAINKEY INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TDEPARTMENT_PK PRIMARY KEY(PKEY));

CREATE  INDEX TDEPARTMENTINDEX1 ON TDEPARTMENT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TNOTIFY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNOTIFY_FK_1')
    ALTER TABLE TNOTIFY DROP CONSTRAINT TNOTIFY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNOTIFY_FK_2')
    ALTER TABLE TNOTIFY DROP CONSTRAINT TNOTIFY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNOTIFY_FK_3')
    ALTER TABLE TNOTIFY DROP CONSTRAINT TNOTIFY_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNOTIFY_FK_4')
    ALTER TABLE TNOTIFY DROP CONSTRAINT TNOTIFY_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TNOTIFY')
BEGIN
     DECLARE @reftable_6 nvarchar(60), @constraintname_6 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TNOTIFY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_6, @constraintname_6
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_6+' drop constraint '+@constraintname_6)
       FETCH NEXT from refcursor into @reftable_6, @constraintname_6
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TNOTIFY
END
;

CREATE TABLE TNOTIFY
(
    PKEY INT NOT NULL,
    PROJCATKEY INT NULL,
    STATEKEY INT NULL,
    PERSONKEY INT NOT NULL,
    WORKITEM INT NULL,
    RACIROLE VARCHAR (1) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TNOTIFY_PK PRIMARY KEY(PKEY));

CREATE  INDEX TNOTIFYINDEX1 ON TNOTIFY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPERSON                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSON_FK_1')
    ALTER TABLE TPERSON DROP CONSTRAINT TPERSON_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSON_FK_2')
    ALTER TABLE TPERSON DROP CONSTRAINT TPERSON_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSON_FK_3')
    ALTER TABLE TPERSON DROP CONSTRAINT TPERSON_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSON_FK_4')
    ALTER TABLE TPERSON DROP CONSTRAINT TPERSON_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPERSON')
BEGIN
     DECLARE @reftable_7 nvarchar(60), @constraintname_7 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPERSON'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_7, @constraintname_7
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_7+' drop constraint '+@constraintname_7)
       FETCH NEXT from refcursor into @reftable_7, @constraintname_7
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPERSON
END
;

CREATE TABLE TPERSON
(
    PKEY INT NOT NULL,
    FIRSTNAME VARCHAR (25) NULL,
    LASTNAME VARCHAR (25) NULL,
    LOGINNAME VARCHAR (60) NOT NULL,
    EMAIL VARCHAR (60) NULL,
    PASSWD VARCHAR (160) NULL,
    SALT VARCHAR (80) NULL,
    FORGOTPASSWORDKEY VARCHAR (100) NULL,
    PHONE VARCHAR (18) NULL,
    DEPKEY INT NULL,
    VALIDUNTIL DATETIME NULL,
    PREFERENCES VARCHAR (7000) NULL,
    LASTEDIT DATETIME NULL,
    CREATED DATETIME NULL,
    DELETED CHAR (1) default 'N' NULL,
    TOKENPASSWD VARCHAR (80) NULL,
    TOKENEXPDATE DATETIME NULL,
    EMAILFREQUENCY INT NULL,
    EMAILLEAD INT NULL,
    EMAILLASTREMINDED DATETIME NULL,
    EMAILREMINDME CHAR (1) default 'N' NULL,
    PREFEMAILTYPE VARCHAR (15) default 'Plain' NULL,
    PREFLOCALE VARCHAR (10) NULL,
    MYDEFAULTREPORT INT NULL,
    NOEMAILSPLEASE INT NULL,
    REMINDMEASORIGINATOR CHAR (1) default 'N' NULL,
    REMINDMEASMANAGER CHAR (1) default 'Y' NULL,
    REMINDMEASRESPONSIBLE CHAR (1) default 'Y' NULL,
    EMAILREMINDPLEVEL INT NULL,
    EMAILREMINDSLEVEL INT NULL,
    HOURSPERWORKDAY FLOAT NULL,
    HOURLYWAGE FLOAT NULL,
    EXTRAHOURWAGE FLOAT NULL,
    EMPLOYEEID VARCHAR (30) NULL,
    ISGROUP CHAR (1) default 'N' NULL,
    USERLEVEL INT NULL,
    MAXASSIGNEDITEMS INT NULL,
    MESSENGERURL VARCHAR (255) NULL,
    CALLURL VARCHAR (255) NULL,
    SYMBOL VARCHAR (255) NULL,
    ICONKEY INT NULL,
    SUBSTITUTEKEY INT NULL,
    SUBSTITUTEACTIVE CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPERSON_PK PRIMARY KEY(PKEY));

CREATE  INDEX TPERSINDEX1 ON TPERSON (DEPKEY);
CREATE  INDEX TPERSINDEX2 ON TPERSON (TPUUID);
CREATE  INDEX TPERSINDEX3 ON TPERSON (FIRSTNAME);
CREATE  INDEX TPERSINDEX4 ON TPERSON (LASTNAME);
CREATE  INDEX TPERSINDEX5 ON TPERSON (LOGINNAME);
CREATE  INDEX TPERSINDEX6 ON TPERSON (ISGROUP);




/* ---------------------------------------------------------------------- */
/* TPRIORITY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPRIORITY_FK_1')
    ALTER TABLE TPRIORITY DROP CONSTRAINT TPRIORITY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPRIORITY')
BEGIN
     DECLARE @reftable_8 nvarchar(60), @constraintname_8 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPRIORITY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_8, @constraintname_8
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_8+' drop constraint '+@constraintname_8)
       FETCH NEXT from refcursor into @reftable_8, @constraintname_8
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPRIORITY
END
;

CREATE TABLE TPRIORITY
(
    PKEY INT NOT NULL,
    LABEL VARCHAR (255) NULL,
    TOOLTIP VARCHAR (255) NULL,
    SORTORDER INT NULL,
    WLEVEL INT NULL,
    SYMBOL VARCHAR (255) NULL,
    ICONKEY INT NULL,
    ICONCHANGED CHAR (1) default 'N' NULL,
    CSSSTYLE VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPRIORITY_PK PRIMARY KEY(PKEY));

CREATE  INDEX TPRIOINDEX1 ON TPRIORITY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPPRIORITY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPPRIORITY_FK_1')
    ALTER TABLE TPPRIORITY DROP CONSTRAINT TPPRIORITY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPPRIORITY_FK_2')
    ALTER TABLE TPPRIORITY DROP CONSTRAINT TPPRIORITY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPPRIORITY_FK_3')
    ALTER TABLE TPPRIORITY DROP CONSTRAINT TPPRIORITY_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPPRIORITY')
BEGIN
     DECLARE @reftable_9 nvarchar(60), @constraintname_9 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPPRIORITY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_9, @constraintname_9
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_9+' drop constraint '+@constraintname_9)
       FETCH NEXT from refcursor into @reftable_9, @constraintname_9
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPPRIORITY
END
;

CREATE TABLE TPPRIORITY
(
    OBJECTID INT NOT NULL,
    PRIORITY INT NULL,
    PROJECTTYPE INT NULL,
    LISTTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPPRIORITY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TPPRIOINDEX1 ON TPPRIORITY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPROJCAT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJCAT_FK_1')
    ALTER TABLE TPROJCAT DROP CONSTRAINT TPROJCAT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPROJCAT')
BEGIN
     DECLARE @reftable_10 nvarchar(60), @constraintname_10 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPROJCAT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_10, @constraintname_10
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_10+' drop constraint '+@constraintname_10)
       FETCH NEXT from refcursor into @reftable_10, @constraintname_10
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPROJCAT
END
;

CREATE TABLE TPROJCAT
(
    PKEY INT NOT NULL,
    LABEL VARCHAR (35) NULL,
    PROJKEY INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPROJCAT_PK PRIMARY KEY(PKEY));

CREATE  INDEX TPROJCATINDEX1 ON TPROJCAT (PROJKEY);
CREATE  INDEX TPROJCATINDEX2 ON TPROJCAT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPROJECT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJECT_FK_1')
    ALTER TABLE TPROJECT DROP CONSTRAINT TPROJECT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJECT_FK_2')
    ALTER TABLE TPROJECT DROP CONSTRAINT TPROJECT_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJECT_FK_3')
    ALTER TABLE TPROJECT DROP CONSTRAINT TPROJECT_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJECT_FK_4')
    ALTER TABLE TPROJECT DROP CONSTRAINT TPROJECT_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJECT_FK_5')
    ALTER TABLE TPROJECT DROP CONSTRAINT TPROJECT_FK_5;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJECT_FK_6')
    ALTER TABLE TPROJECT DROP CONSTRAINT TPROJECT_FK_6;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJECT_FK_7')
    ALTER TABLE TPROJECT DROP CONSTRAINT TPROJECT_FK_7;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPROJECT')
BEGIN
     DECLARE @reftable_11 nvarchar(60), @constraintname_11 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPROJECT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_11, @constraintname_11
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_11+' drop constraint '+@constraintname_11)
       FETCH NEXT from refcursor into @reftable_11, @constraintname_11
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPROJECT
END
;

CREATE TABLE TPROJECT
(
    PKEY INT NOT NULL,
    LABEL VARCHAR (255) NULL,
    DEFOWNER INT NULL,
    DEFMANAGER INT NULL,
    DEFINITSTATE INT NULL,
    PROJECTTYPE INT NULL,
    VERSIONSYSTEMFIELD0 VARCHAR (255) NULL,
    VERSIONSYSTEMFIELD1 VARCHAR (255) NULL,
    VERSIONSYSTEMFIELD2 VARCHAR (255) NULL,
    VERSIONSYSTEMFIELD3 VARCHAR (255) NULL,
    DELETED VARCHAR (1) default 'N' NOT NULL,
    STATUS INT NULL,
    CURRENCYNAME VARCHAR (255) NULL,
    CURRENCYSYMBOL VARCHAR (255) NULL,
    HOURSPERWORKDAY FLOAT NULL,
    MOREPROPS VARCHAR (3000) NULL,
    TAGLABEL VARCHAR (50) NULL,
    DESCRIPTION VARCHAR (255) NULL,
    PREFIX VARCHAR (50) NULL,
    NEXTITEMID INT NULL,
    LASTID INT NULL,
    PARENT INT NULL,
    SORTORDER INT NULL,
    ISPRIVATE VARCHAR (1) default 'N' NULL,
    ISTEMPLATE CHAR (1) default 'N' NULL,
    DOMAINKEY INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPROJECT_PK PRIMARY KEY(PKEY));

CREATE  INDEX TPROJECTINDEX ON TPROJECT (TPUUID);
CREATE  INDEX TPROJECTINDEX1 ON TPROJECT (STATUS);
CREATE  INDEX TPROJECTINDEX2 ON TPROJECT (SORTORDER);




/* ---------------------------------------------------------------------- */
/* TRELEASE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TRELEASE_FK_1')
    ALTER TABLE TRELEASE DROP CONSTRAINT TRELEASE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TRELEASE_FK_2')
    ALTER TABLE TRELEASE DROP CONSTRAINT TRELEASE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TRELEASE_FK_3')
    ALTER TABLE TRELEASE DROP CONSTRAINT TRELEASE_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TRELEASE')
BEGIN
     DECLARE @reftable_12 nvarchar(60), @constraintname_12 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TRELEASE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_12, @constraintname_12
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_12+' drop constraint '+@constraintname_12)
       FETCH NEXT from refcursor into @reftable_12, @constraintname_12
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TRELEASE
END
;

CREATE TABLE TRELEASE
(
    PKEY INT NOT NULL,
    LABEL VARCHAR (255) NULL,
    PROJKEY INT NOT NULL,
    STATUS INT NULL,
    SORTORDER INT NULL,
    MOREPROPS VARCHAR (3000) NULL,
    DESCRIPTION VARCHAR (255) NULL,
    DUEDATE DATETIME NULL,
    PARENT INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TRELEASE_PK PRIMARY KEY(PKEY));

CREATE  INDEX TRELINDEX1 ON TRELEASE (PROJKEY);
CREATE  INDEX TRELINDEX2 ON TRELEASE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TROLE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TROLE_FK_1')
    ALTER TABLE TROLE DROP CONSTRAINT TROLE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TROLE')
BEGIN
     DECLARE @reftable_13 nvarchar(60), @constraintname_13 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TROLE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_13, @constraintname_13
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_13+' drop constraint '+@constraintname_13)
       FETCH NEXT from refcursor into @reftable_13, @constraintname_13
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TROLE
END
;

CREATE TABLE TROLE
(
    PKEY INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    ACCESSKEY INT NULL,
    EXTENDEDACCESSKEY VARCHAR (30) NULL,
    PROJECTTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TROLE_PK PRIMARY KEY(PKEY));

CREATE  INDEX TROLEINDEX1 ON TROLE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSEVERITY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSEVERITY_FK_1')
    ALTER TABLE TSEVERITY DROP CONSTRAINT TSEVERITY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSEVERITY')
BEGIN
     DECLARE @reftable_14 nvarchar(60), @constraintname_14 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSEVERITY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_14, @constraintname_14
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_14+' drop constraint '+@constraintname_14)
       FETCH NEXT from refcursor into @reftable_14, @constraintname_14
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSEVERITY
END
;

CREATE TABLE TSEVERITY
(
    PKEY INT NOT NULL,
    LABEL VARCHAR (255) NULL,
    TOOLTIP VARCHAR (255) NULL,
    SORTORDER INT NULL,
    WLEVEL INT NULL,
    SYMBOL VARCHAR (255) NULL,
    ICONKEY INT NULL,
    ICONCHANGED CHAR (1) default 'N' NULL,
    CSSSTYLE VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSEVERITY_PK PRIMARY KEY(PKEY));

CREATE  INDEX TSEVINDEX1 ON TSEVERITY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPSEVERITY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPSEVERITY_FK_1')
    ALTER TABLE TPSEVERITY DROP CONSTRAINT TPSEVERITY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPSEVERITY_FK_2')
    ALTER TABLE TPSEVERITY DROP CONSTRAINT TPSEVERITY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPSEVERITY_FK_3')
    ALTER TABLE TPSEVERITY DROP CONSTRAINT TPSEVERITY_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPSEVERITY')
BEGIN
     DECLARE @reftable_15 nvarchar(60), @constraintname_15 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPSEVERITY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_15, @constraintname_15
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_15+' drop constraint '+@constraintname_15)
       FETCH NEXT from refcursor into @reftable_15, @constraintname_15
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPSEVERITY
END
;

CREATE TABLE TPSEVERITY
(
    OBJECTID INT NOT NULL,
    SEVERITY INT NULL,
    PROJECTTYPE INT NULL,
    LISTTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPSEVERITY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TPSEVINDEX1 ON TPSEVERITY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSTATE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSTATE_FK_1')
    ALTER TABLE TSTATE DROP CONSTRAINT TSTATE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSTATE')
BEGIN
     DECLARE @reftable_16 nvarchar(60), @constraintname_16 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSTATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_16, @constraintname_16
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_16+' drop constraint '+@constraintname_16)
       FETCH NEXT from refcursor into @reftable_16, @constraintname_16
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSTATE
END
;

CREATE TABLE TSTATE
(
    PKEY INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    TOOLTIP VARCHAR (255) NULL,
    STATEFLAG INT NULL,
    SORTORDER INT NULL,
    SYMBOL VARCHAR (255) NULL,
    ICONKEY INT NULL,
    ICONCHANGED CHAR (1) default 'N' NULL,
    CSSSTYLE VARCHAR (255) NULL,
    PERCENTCOMPLETE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSTATE_PK PRIMARY KEY(PKEY));

CREATE  INDEX TSTATEINDEX1 ON TSTATE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSTATECHANGE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSTATECHANGE_FK_1')
    ALTER TABLE TSTATECHANGE DROP CONSTRAINT TSTATECHANGE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSTATECHANGE_FK_2')
    ALTER TABLE TSTATECHANGE DROP CONSTRAINT TSTATECHANGE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSTATECHANGE_FK_3')
    ALTER TABLE TSTATECHANGE DROP CONSTRAINT TSTATECHANGE_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSTATECHANGE')
BEGIN
     DECLARE @reftable_17 nvarchar(60), @constraintname_17 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSTATECHANGE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_17, @constraintname_17
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_17+' drop constraint '+@constraintname_17)
       FETCH NEXT from refcursor into @reftable_17, @constraintname_17
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSTATECHANGE
END
;

CREATE TABLE TSTATECHANGE
(
    STATECHANGEKEY INT NOT NULL,
    WORKITEMKEY INT NOT NULL,
    CHANGEDBY INT NOT NULL,
    CHANGEDTO INT NOT NULL,
    CHANGEDESCRIPTION VARCHAR (7000) NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSTATECHANGE_PK PRIMARY KEY(STATECHANGEKEY));

CREATE  INDEX TSTATECHANGEINDEX1 ON TSTATECHANGE (CHANGEDBY);
CREATE  INDEX TSTATECHANGEINDEX2 ON TSTATECHANGE (CHANGEDTO);
CREATE  INDEX TSTATECHANGEINDEX3 ON TSTATECHANGE (WORKITEMKEY);
CREATE  INDEX TSTATECHANGEINDEX4 ON TSTATECHANGE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TTRAIL                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TTRAIL_FK_1')
    ALTER TABLE TTRAIL DROP CONSTRAINT TTRAIL_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TTRAIL_FK_2')
    ALTER TABLE TTRAIL DROP CONSTRAINT TTRAIL_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TTRAIL')
BEGIN
     DECLARE @reftable_18 nvarchar(60), @constraintname_18 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TTRAIL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_18, @constraintname_18
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_18+' drop constraint '+@constraintname_18)
       FETCH NEXT from refcursor into @reftable_18, @constraintname_18
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TTRAIL
END
;

CREATE TABLE TTRAIL
(
    TRAILKEY INT NOT NULL,
    WORKITEMKEY INT NOT NULL,
    CHANGEDBY INT NOT NULL,
    CHANGEDESCRIPTION VARCHAR (7000) NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TTRAIL_PK PRIMARY KEY(TRAILKEY));

CREATE  INDEX TTRAILINDEX1 ON TTRAIL (WORKITEMKEY);
CREATE  INDEX TTRAILINDEX2 ON TTRAIL (CHANGEDBY);
CREATE  INDEX TTRAILINDEX3 ON TTRAIL (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKITEM                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_1')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_2')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_3')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_4')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_5')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_5;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_6')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_6;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_7')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_7;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_8')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_8;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_9')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_9;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_10')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_10;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_11')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_11;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_12')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_12;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_13')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_13;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEM_FK_14')
    ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_14;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKITEM')
BEGIN
     DECLARE @reftable_19 nvarchar(60), @constraintname_19 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKITEM'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_19, @constraintname_19
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_19+' drop constraint '+@constraintname_19)
       FETCH NEXT from refcursor into @reftable_19, @constraintname_19
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKITEM
END
;

CREATE TABLE TWORKITEM
(
    WORKITEMKEY INT NOT NULL,
    OWNER INT NOT NULL,
    CHANGEDBY INT NOT NULL,
    ORIGINATOR INT NULL,
    RESPONSIBLE INT NULL,
    PROJECTKEY INT NULL,
    PROJCATKEY INT NULL,
    CATEGORYKEY INT NOT NULL,
    CLASSKEY INT NULL,
    PRIORITYKEY INT NOT NULL,
    SEVERITYKEY INT NULL,
    SUPERIORWORKITEM INT NULL,
    PACKAGESYNOPSYS VARCHAR (255) NOT NULL,
    PACKAGEDESCRIPTION VARCHAR (7000) NULL,
    REFERENCE VARCHAR (20) NULL,
    LASTEDIT DATETIME NULL,
    RELNOTICEDKEY INT NULL,
    RELSCHEDULEDKEY INT NULL,
    BUILD VARCHAR (25) NULL,
    STATE INT NULL,
    STARTDATE DATETIME NULL,
    ENDDATE DATETIME NULL,
    SUBMITTEREMAIL VARCHAR (60) NULL,
    CREATED DATETIME NULL,
    ACTUALSTARTDATE DATETIME NULL,
    ACTUALENDDATE DATETIME NULL,
    WLEVEL VARCHAR (14) NULL,
    ACCESSLEVEL INT default 0 NULL,
    ARCHIVELEVEL INT default 0 NULL,
    ESCALATIONLEVEL INT NULL,
    TASKISMILESTONE CHAR (1) default 'N' NULL,
    TASKISSUBPROJECT CHAR (1) default 'N' NULL,
    TASKISSUMMARY CHAR (1) default 'N' NULL,
    TASKCONSTRAINT INT NULL,
    TASKCONSTRAINTDATE DATETIME NULL,
    PSPCODE VARCHAR (255) NULL,
    IDNUMBER INT NULL,
    WBSONLEVEL INT NULL,
    REMINDERDATE DATETIME NULL,
    TOPDOWNSTARTDATE DATETIME NULL,
    TOPDOWNENDDATE DATETIME NULL,
    OVERBUDGET CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKITEM_PK PRIMARY KEY(WORKITEMKEY));

CREATE  INDEX TWIINDEX1 ON TWORKITEM (PRIORITYKEY);
CREATE  INDEX TWIINDEX2 ON TWORKITEM (SEVERITYKEY);
CREATE  INDEX TWIINDEX3 ON TWORKITEM (CATEGORYKEY);
CREATE  INDEX TWIINDEX4 ON TWORKITEM (CLASSKEY);
CREATE  INDEX TWIINDEX5 ON TWORKITEM (CHANGEDBY);
CREATE  INDEX TWIINDEX6 ON TWORKITEM (RELNOTICEDKEY);
CREATE  INDEX TWIINDEX7 ON TWORKITEM (RELSCHEDULEDKEY);
CREATE  INDEX TWIINDEX8 ON TWORKITEM (OWNER);
CREATE  INDEX TWIINDEX9 ON TWORKITEM (ORIGINATOR);
CREATE  INDEX TWIINDEX10 ON TWORKITEM (RESPONSIBLE);
CREATE  INDEX TWIINDEX11 ON TWORKITEM (PROJCATKEY);
CREATE  INDEX TWIINDEX12 ON TWORKITEM (STATE);
CREATE  INDEX TWIINDEX13 ON TWORKITEM (STARTDATE);
CREATE  INDEX TWIINDEX14 ON TWORKITEM (ENDDATE);
CREATE  INDEX TWIINDEX15 ON TWORKITEM (ACTUALSTARTDATE);
CREATE  INDEX TWIINDEX16 ON TWORKITEM (ACTUALENDDATE);
CREATE  INDEX TWIINDEX17 ON TWORKITEM (WLEVEL);
CREATE  INDEX TWIINDEX18 ON TWORKITEM (ACCESSLEVEL);
CREATE  INDEX TWIINDEX19 ON TWORKITEM (ARCHIVELEVEL);
CREATE  INDEX TWIINDEX20 ON TWORKITEM (TPUUID);
CREATE  INDEX TWIINDEX21 ON TWORKITEM (IDNUMBER);
CREATE  INDEX TWIINDEX22 ON TWORKITEM (CREATED);
CREATE  INDEX TWIINDEX23 ON TWORKITEM (WBSONLEVEL);
CREATE  INDEX TWIINDEX24 ON TWORKITEM (TOPDOWNSTARTDATE);
CREATE  INDEX TWIINDEX25 ON TWORKITEM (TOPDOWNENDDATE);
CREATE  INDEX TWIINDEX26 ON TWORKITEM (PROJECTKEY);




/* ---------------------------------------------------------------------- */
/* TCOMPUTEDVALUES                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCOMPUTEDVALUES_FK_1')
    ALTER TABLE TCOMPUTEDVALUES DROP CONSTRAINT TCOMPUTEDVALUES_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCOMPUTEDVALUES_FK_2')
    ALTER TABLE TCOMPUTEDVALUES DROP CONSTRAINT TCOMPUTEDVALUES_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCOMPUTEDVALUES')
BEGIN
     DECLARE @reftable_20 nvarchar(60), @constraintname_20 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCOMPUTEDVALUES'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_20, @constraintname_20
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_20+' drop constraint '+@constraintname_20)
       FETCH NEXT from refcursor into @reftable_20, @constraintname_20
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCOMPUTEDVALUES
END
;

CREATE TABLE TCOMPUTEDVALUES
(
    PKEY INT NOT NULL,
    WORKITEMKEY INT NOT NULL,
    EFFORTTYPE INT NOT NULL,
    COMPUTEDVALUETYPE INT NOT NULL,
    COMPUTEDVALUE FLOAT NULL,
    MEASUREMENTUNIT INT NULL,
    PERSON INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCOMPUTEDVALUES_PK PRIMARY KEY(PKEY));

CREATE  INDEX TCVINDEX1 ON TCOMPUTEDVALUES (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPRIVATEREPORTREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPRIVATEREPORTREPOSITORY_FK_1')
    ALTER TABLE TPRIVATEREPORTREPOSITORY DROP CONSTRAINT TPRIVATEREPORTREPOSITORY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPRIVATEREPORTREPOSITORY')
BEGIN
     DECLARE @reftable_21 nvarchar(60), @constraintname_21 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPRIVATEREPORTREPOSITORY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_21, @constraintname_21
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_21+' drop constraint '+@constraintname_21)
       FETCH NEXT from refcursor into @reftable_21, @constraintname_21
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPRIVATEREPORTREPOSITORY
END
;

CREATE TABLE TPRIVATEREPORTREPOSITORY
(
    PKEY INT NOT NULL,
    OWNER INT NOT NULL,
    NAME VARCHAR (100) NOT NULL,
    QUERY VARCHAR (7000) NOT NULL,
    MENUITEM CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPRIVATEREPORTREPOSITORY_PK PRIMARY KEY(PKEY));

CREATE  INDEX TPRIVREPREPINDEX1 ON TPRIVATEREPORTREPOSITORY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPUBLICREPORTREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPUBLICREPORTREPOSITORY_FK_1')
    ALTER TABLE TPUBLICREPORTREPOSITORY DROP CONSTRAINT TPUBLICREPORTREPOSITORY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPUBLICREPORTREPOSITORY')
BEGIN
     DECLARE @reftable_22 nvarchar(60), @constraintname_22 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPUBLICREPORTREPOSITORY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_22, @constraintname_22
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_22+' drop constraint '+@constraintname_22)
       FETCH NEXT from refcursor into @reftable_22, @constraintname_22
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPUBLICREPORTREPOSITORY
END
;

CREATE TABLE TPUBLICREPORTREPOSITORY
(
    PKEY INT NOT NULL,
    OWNER INT NOT NULL,
    NAME VARCHAR (100) NOT NULL,
    QUERY VARCHAR (7000) NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPUBLICREPORTREPOSITORY_PK PRIMARY KEY(PKEY));

CREATE  INDEX TPUBREPREPINDEX1 ON TPUBLICREPORTREPOSITORY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPROJECTREPORTREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJECTREPORTREPOSITORY_FK_1')
    ALTER TABLE TPROJECTREPORTREPOSITORY DROP CONSTRAINT TPROJECTREPORTREPOSITORY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPROJECTREPORTREPOSITORY')
BEGIN
     DECLARE @reftable_23 nvarchar(60), @constraintname_23 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPROJECTREPORTREPOSITORY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_23, @constraintname_23
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_23+' drop constraint '+@constraintname_23)
       FETCH NEXT from refcursor into @reftable_23, @constraintname_23
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPROJECTREPORTREPOSITORY
END
;

CREATE TABLE TPROJECTREPORTREPOSITORY
(
    PKEY INT NOT NULL,
    PROJECT INT NOT NULL,
    NAME VARCHAR (100) NOT NULL,
    QUERY VARCHAR (7000) NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPROJECTREPORTREPOSITORY_PK PRIMARY KEY(PKEY));

CREATE  INDEX TPROJREPREPINDEX1 ON TPROJECTREPORTREPOSITORY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TACCOUNT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TACCOUNT_FK_1')
    ALTER TABLE TACCOUNT DROP CONSTRAINT TACCOUNT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TACCOUNT_FK_2')
    ALTER TABLE TACCOUNT DROP CONSTRAINT TACCOUNT_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TACCOUNT')
BEGIN
     DECLARE @reftable_24 nvarchar(60), @constraintname_24 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TACCOUNT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_24, @constraintname_24
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_24+' drop constraint '+@constraintname_24)
       FETCH NEXT from refcursor into @reftable_24, @constraintname_24
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TACCOUNT
END
;

CREATE TABLE TACCOUNT
(
    OBJECTID INT NOT NULL,
    ACCOUNTNUMBER VARCHAR (30) NOT NULL,
    ACCOUNTNAME VARCHAR (80) NULL,
    STATUS INT NULL,
    COSTCENTER INT NULL,
    DESCRIPTION VARCHAR (255) NULL,
    MOREPROPS VARCHAR (3000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TACCOUNT_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TACCOUNTINDEX1 ON TACCOUNT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TATTACHMENT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTACHMENT_FK_1')
    ALTER TABLE TATTACHMENT DROP CONSTRAINT TATTACHMENT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTACHMENT_FK_2')
    ALTER TABLE TATTACHMENT DROP CONSTRAINT TATTACHMENT_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTACHMENT_FK_3')
    ALTER TABLE TATTACHMENT DROP CONSTRAINT TATTACHMENT_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TATTACHMENT')
BEGIN
     DECLARE @reftable_25 nvarchar(60), @constraintname_25 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TATTACHMENT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_25, @constraintname_25
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_25+' drop constraint '+@constraintname_25)
       FETCH NEXT from refcursor into @reftable_25, @constraintname_25
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TATTACHMENT
END
;

CREATE TABLE TATTACHMENT
(
    OBJECTID INT NOT NULL,
    WORKITEM INT NOT NULL,
    CHANGEDBY INT NULL,
    DOCUMENTSTATE INT NULL,
    FILENAME VARCHAR (256) NOT NULL,
    ISURL CHAR (1) default 'N' NULL,
    FILESIZE VARCHAR (20) NULL,
    MIMETYPE VARCHAR (15) NULL,
    LASTEDIT DATETIME NULL,
    VERSION VARCHAR (20) NULL,
    DESCRIPTION VARCHAR (512) NULL,
    CRYPTKEY VARCHAR (4000) NULL,
    ISENCRYPTED CHAR (1) default 'N' NULL,
    ISDELETED CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TATTACHMENT_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TATTACHINDEX1 ON TATTACHMENT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCOST                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCOST_FK_1')
    ALTER TABLE TCOST DROP CONSTRAINT TCOST_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCOST_FK_2')
    ALTER TABLE TCOST DROP CONSTRAINT TCOST_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCOST_FK_3')
    ALTER TABLE TCOST DROP CONSTRAINT TCOST_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCOST_FK_4')
    ALTER TABLE TCOST DROP CONSTRAINT TCOST_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCOST')
BEGIN
     DECLARE @reftable_26 nvarchar(60), @constraintname_26 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCOST'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_26, @constraintname_26
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_26+' drop constraint '+@constraintname_26)
       FETCH NEXT from refcursor into @reftable_26, @constraintname_26
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCOST
END
;

CREATE TABLE TCOST
(
    OBJECTID INT NOT NULL,
    ACCOUNT INT NULL,
    PERSON INT NULL,
    WORKITEM INT NULL,
    HOURS FLOAT NULL,
    COST FLOAT NULL,
    SUBJECT VARCHAR (255) NULL,
    EFFORTTYPE INT NULL,
    EFFORTVALUE FLOAT NULL,
    EFFORTDATE DATETIME NULL,
    INVOICENUMBER VARCHAR (255) NULL,
    INVOICEDATE DATETIME NULL,
    INVOICEPATH VARCHAR (255) NULL,
    DESCRIPTION VARCHAR (255) NULL,
    MOREPROPS VARCHAR (3000) NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCOST_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TCOSTINDEX1 ON TCOST (TPUUID);
CREATE  INDEX TCOSTINDEX2 ON TCOST (LASTEDIT);
CREATE  INDEX TCOSTINDEX3 ON TCOST (EFFORTDATE);




/* ---------------------------------------------------------------------- */
/* TEFFORTTYPE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TEFFORTTYPE_FK_1')
    ALTER TABLE TEFFORTTYPE DROP CONSTRAINT TEFFORTTYPE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TEFFORTTYPE')
BEGIN
     DECLARE @reftable_27 nvarchar(60), @constraintname_27 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TEFFORTTYPE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_27, @constraintname_27
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_27+' drop constraint '+@constraintname_27)
       FETCH NEXT from refcursor into @reftable_27, @constraintname_27
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TEFFORTTYPE
END
;

CREATE TABLE TEFFORTTYPE
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    EFFORTUNIT INT NULL,
    DESCRIPTION VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TEFFORTTYPE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TEFFORTTYPEINDEX1 ON TEFFORTTYPE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TEFFORTUNIT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TEFFORTUNIT')
BEGIN
     DECLARE @reftable_28 nvarchar(60), @constraintname_28 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TEFFORTUNIT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_28, @constraintname_28
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_28+' drop constraint '+@constraintname_28)
       FETCH NEXT from refcursor into @reftable_28, @constraintname_28
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TEFFORTUNIT
END
;

CREATE TABLE TEFFORTUNIT
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    DESCRIPTION VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TEFFORTUNIT_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TEFFORTUNITINDEX1 ON TEFFORTUNIT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TDOCSTATE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDOCSTATE_FK_1')
    ALTER TABLE TDOCSTATE DROP CONSTRAINT TDOCSTATE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TDOCSTATE')
BEGIN
     DECLARE @reftable_29 nvarchar(60), @constraintname_29 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TDOCSTATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_29, @constraintname_29
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_29+' drop constraint '+@constraintname_29)
       FETCH NEXT from refcursor into @reftable_29, @constraintname_29
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TDOCSTATE
END
;

CREATE TABLE TDOCSTATE
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (25) NOT NULL,
    STATEFLAG INT NULL,
    PROJECTTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TDOCSTATE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TDOCSTATEINDEX1 ON TDOCSTATE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TDISABLEFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDISABLEFIELD_FK_1')
    ALTER TABLE TDISABLEFIELD DROP CONSTRAINT TDISABLEFIELD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDISABLEFIELD_FK_2')
    ALTER TABLE TDISABLEFIELD DROP CONSTRAINT TDISABLEFIELD_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TDISABLEFIELD')
BEGIN
     DECLARE @reftable_30 nvarchar(60), @constraintname_30 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TDISABLEFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_30, @constraintname_30
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_30+' drop constraint '+@constraintname_30)
       FETCH NEXT from refcursor into @reftable_30, @constraintname_30
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TDISABLEFIELD
END
;

CREATE TABLE TDISABLEFIELD
(
    OBJECTID INT NOT NULL,
    FIELDNAME VARCHAR (25) NOT NULL,
    LISTTYPE INT NULL,
    PROJECTTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TDISABLEFIELD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TDISABLEFINDEX1 ON TDISABLEFIELD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPLISTTYPE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPLISTTYPE_FK_1')
    ALTER TABLE TPLISTTYPE DROP CONSTRAINT TPLISTTYPE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPLISTTYPE_FK_2')
    ALTER TABLE TPLISTTYPE DROP CONSTRAINT TPLISTTYPE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPLISTTYPE')
BEGIN
     DECLARE @reftable_31 nvarchar(60), @constraintname_31 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPLISTTYPE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_31, @constraintname_31
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_31+' drop constraint '+@constraintname_31)
       FETCH NEXT from refcursor into @reftable_31, @constraintname_31
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPLISTTYPE
END
;

CREATE TABLE TPLISTTYPE
(
    OBJECTID INT NOT NULL,
    PROJECTTYPE INT NULL,
    CATEGORY INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPLISTTYPE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TPLISTTYPEINDEX1 ON TPLISTTYPE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPROJECTTYPE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPROJECTTYPE')
BEGIN
     DECLARE @reftable_32 nvarchar(60), @constraintname_32 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPROJECTTYPE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_32, @constraintname_32
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_32+' drop constraint '+@constraintname_32)
       FETCH NEXT from refcursor into @reftable_32, @constraintname_32
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPROJECTTYPE
END
;

CREATE TABLE TPROJECTTYPE
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NULL,
    NOTIFYOWNERLEVEL INT NULL,
    NOTIFYMANAGERLEVEL INT NULL,
    HOURSPERWORKDAY FLOAT NULL,
    MOREPROPS VARCHAR (3000) NULL,
    DEFAULTFORPRIVATE VARCHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPROJECTTYPE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TPROJTYPEINDEX1 ON TPROJECTTYPE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPSTATE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPSTATE_FK_1')
    ALTER TABLE TPSTATE DROP CONSTRAINT TPSTATE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPSTATE_FK_2')
    ALTER TABLE TPSTATE DROP CONSTRAINT TPSTATE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPSTATE_FK_3')
    ALTER TABLE TPSTATE DROP CONSTRAINT TPSTATE_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPSTATE')
BEGIN
     DECLARE @reftable_33 nvarchar(60), @constraintname_33 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPSTATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_33, @constraintname_33
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_33+' drop constraint '+@constraintname_33)
       FETCH NEXT from refcursor into @reftable_33, @constraintname_33
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPSTATE
END
;

CREATE TABLE TPSTATE
(
    OBJECTID INT NOT NULL,
    STATE INT NULL,
    PROJECTTYPE INT NULL,
    LISTTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPSTATE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TPSTATEINDEX1 ON TPSTATE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSITE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSITE')
BEGIN
     DECLARE @reftable_34 nvarchar(60), @constraintname_34 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSITE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_34, @constraintname_34
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_34+' drop constraint '+@constraintname_34)
       FETCH NEXT from refcursor into @reftable_34, @constraintname_34
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSITE
END
;

CREATE TABLE TSITE
(
    OBJECTID INT NOT NULL,
    TRACKVERSION VARCHAR (10) NULL,
    DBVERSION VARCHAR (10) NULL,
    LICENSEEXT VARCHAR (512) NULL,
    EXPDATE DATETIME NULL,
    NUMBEROFUSERS INT NULL,
    TRACKEMAIL VARCHAR (100) NULL,
    SMTPSERVERNAME VARCHAR (100) NULL,
    SMTPPORT INT NULL,
    MAILENCODING VARCHAR (20) NULL,
    SMTPUSER VARCHAR (100) NULL,
    SMTPPASSWORD VARCHAR (30) NULL,
    POPSERVERNAME VARCHAR (100) NULL,
    POPPORT INT NULL,
    POPUSER VARCHAR (100) NULL,
    POPPASSWORD VARCHAR (30) NULL,
    RECEIVINGPROTOCOL VARCHAR (6) NULL,
    ALLOWEDEMAILPATTERN VARCHAR (1500) NULL,
    ISLDAPON CHAR (1) NULL,
    LDAPSERVERURL VARCHAR (100) NULL,
    LDAPATTRIBUTELOGINNAME VARCHAR (30) NULL,
    ATTACHMENTROOTDIR VARCHAR (512) NULL,
    SERVERURL VARCHAR (100) NULL,
    DESCRIPTIONLENGTH INT NULL,
    ISSELFREGISTERALLOWED CHAR (1) default 'Y' NULL,
    ISDEMOSITE CHAR (1) default 'N' NULL,
    USETRACKFROMADDRESS CHAR (1) default 'Y' NULL,
    RESERVEDUSE INT NULL,
    LDAPBINDDN VARCHAR (255) NULL,
    LDAPBINDPASSW VARCHAR (20) NULL,
    PREFERENCES VARCHAR (2000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSITE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSITEINDEX1 ON TSITE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKFLOW                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOW_FK_1')
    ALTER TABLE TWORKFLOW DROP CONSTRAINT TWORKFLOW_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOW_FK_2')
    ALTER TABLE TWORKFLOW DROP CONSTRAINT TWORKFLOW_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOW_FK_3')
    ALTER TABLE TWORKFLOW DROP CONSTRAINT TWORKFLOW_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOW_FK_4')
    ALTER TABLE TWORKFLOW DROP CONSTRAINT TWORKFLOW_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKFLOW')
BEGIN
     DECLARE @reftable_35 nvarchar(60), @constraintname_35 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKFLOW'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_35, @constraintname_35
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_35+' drop constraint '+@constraintname_35)
       FETCH NEXT from refcursor into @reftable_35, @constraintname_35
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKFLOW
END
;

CREATE TABLE TWORKFLOW
(
    OBJECTID INT NOT NULL,
    STATEFROM INT NULL,
    STATETO INT NULL,
    PROJECTTYPE INT NULL,
    RESPONSIBLE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKFLOW_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKFLOWINDEX1 ON TWORKFLOW (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKFLOWROLE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWROLE_FK_1')
    ALTER TABLE TWORKFLOWROLE DROP CONSTRAINT TWORKFLOWROLE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWROLE_FK_2')
    ALTER TABLE TWORKFLOWROLE DROP CONSTRAINT TWORKFLOWROLE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKFLOWROLE')
BEGIN
     DECLARE @reftable_36 nvarchar(60), @constraintname_36 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKFLOWROLE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_36, @constraintname_36
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_36+' drop constraint '+@constraintname_36)
       FETCH NEXT from refcursor into @reftable_36, @constraintname_36
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKFLOWROLE
END
;

CREATE TABLE TWORKFLOWROLE
(
    OBJECTID INT NOT NULL,
    WORKFLOW INT NULL,
    MAYCHANGEROLE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKFLOWROLE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKFLOWROLEINDEX1 ON TWORKFLOWROLE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKFLOWCATEGORY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWCATEGORY_FK_1')
    ALTER TABLE TWORKFLOWCATEGORY DROP CONSTRAINT TWORKFLOWCATEGORY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWCATEGORY_FK_2')
    ALTER TABLE TWORKFLOWCATEGORY DROP CONSTRAINT TWORKFLOWCATEGORY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKFLOWCATEGORY')
BEGIN
     DECLARE @reftable_37 nvarchar(60), @constraintname_37 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKFLOWCATEGORY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_37, @constraintname_37
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_37+' drop constraint '+@constraintname_37)
       FETCH NEXT from refcursor into @reftable_37, @constraintname_37
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKFLOWCATEGORY
END
;

CREATE TABLE TWORKFLOWCATEGORY
(
    OBJECTID INT NOT NULL,
    WORKFLOW INT NULL,
    CATEGORY INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKFLOWCATEGORY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKFLOWCATINDEX1 ON TWORKFLOWCATEGORY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TROLELISTTYPE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TROLELISTTYPE_FK_1')
    ALTER TABLE TROLELISTTYPE DROP CONSTRAINT TROLELISTTYPE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TROLELISTTYPE_FK_2')
    ALTER TABLE TROLELISTTYPE DROP CONSTRAINT TROLELISTTYPE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TROLELISTTYPE')
BEGIN
     DECLARE @reftable_38 nvarchar(60), @constraintname_38 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TROLELISTTYPE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_38, @constraintname_38
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_38+' drop constraint '+@constraintname_38)
       FETCH NEXT from refcursor into @reftable_38, @constraintname_38
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TROLELISTTYPE
END
;

CREATE TABLE TROLELISTTYPE
(
    OBJECTID INT NOT NULL,
    PROLE INT NOT NULL,
    LISTTYPE INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TROLELISTTYPE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TROLELISTTINDEX1 ON TROLELISTTYPE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TISSUEATTRIBUTEVALUE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TISSUEATTRIBUTEVALUE_FK_1')
    ALTER TABLE TISSUEATTRIBUTEVALUE DROP CONSTRAINT TISSUEATTRIBUTEVALUE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TISSUEATTRIBUTEVALUE_FK_2')
    ALTER TABLE TISSUEATTRIBUTEVALUE DROP CONSTRAINT TISSUEATTRIBUTEVALUE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TISSUEATTRIBUTEVALUE_FK_3')
    ALTER TABLE TISSUEATTRIBUTEVALUE DROP CONSTRAINT TISSUEATTRIBUTEVALUE_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TISSUEATTRIBUTEVALUE_FK_4')
    ALTER TABLE TISSUEATTRIBUTEVALUE DROP CONSTRAINT TISSUEATTRIBUTEVALUE_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TISSUEATTRIBUTEVALUE')
BEGIN
     DECLARE @reftable_39 nvarchar(60), @constraintname_39 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TISSUEATTRIBUTEVALUE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_39, @constraintname_39
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_39+' drop constraint '+@constraintname_39)
       FETCH NEXT from refcursor into @reftable_39, @constraintname_39
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TISSUEATTRIBUTEVALUE
END
;

CREATE TABLE TISSUEATTRIBUTEVALUE
(
    OBJECTID INT NOT NULL,
    ATTRIBUTEID INT NOT NULL,
    DELETED INT NULL,
    ISSUE INT NOT NULL,
    NUMERICVALUE INT NULL,
    TIMESTAMPVALUE DATETIME NULL,
    LONGTEXTVALUE VARCHAR (7000) NULL,
    OPTIONID INT NULL,
    PERSON INT NULL,
    CHARVALUE VARCHAR (255) NULL,
    DISPLAYVALUE VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TISSUEATTRIBUTEVALUE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TISSUEATTINDEX1 ON TISSUEATTRIBUTEVALUE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TATTRIBUTEOPTION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTRIBUTEOPTION_FK_1')
    ALTER TABLE TATTRIBUTEOPTION DROP CONSTRAINT TATTRIBUTEOPTION_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TATTRIBUTEOPTION')
BEGIN
     DECLARE @reftable_40 nvarchar(60), @constraintname_40 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TATTRIBUTEOPTION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_40, @constraintname_40
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_40+' drop constraint '+@constraintname_40)
       FETCH NEXT from refcursor into @reftable_40, @constraintname_40
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TATTRIBUTEOPTION
END
;

CREATE TABLE TATTRIBUTEOPTION
(
    OBJECTID INT NOT NULL,
    ATTRIBUTEID INT NOT NULL,
    PARENTOPTION INT NULL,
    OPTIONNAME VARCHAR (255) NOT NULL,
    DELETED INT NULL,
    SORTORDER INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TATTRIBUTEOPTION_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TATTOPTINDEX1 ON TATTRIBUTEOPTION (TPUUID);




/* ---------------------------------------------------------------------- */
/* TATTRIBUTECLASS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TATTRIBUTECLASS')
BEGIN
     DECLARE @reftable_41 nvarchar(60), @constraintname_41 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TATTRIBUTECLASS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_41, @constraintname_41
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_41+' drop constraint '+@constraintname_41)
       FETCH NEXT from refcursor into @reftable_41, @constraintname_41
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TATTRIBUTECLASS
END
;

CREATE TABLE TATTRIBUTECLASS
(
    OBJECTID INT NOT NULL,
    ATTRIBUTECLASSNAME VARCHAR (255) NOT NULL,
    ATTRIBUTECLASSDESC VARCHAR (64) NULL,
    JAVACLASSNAME VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TATTRIBUTECLASS_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TATTCLASSINDEX1 ON TATTRIBUTECLASS (TPUUID);




/* ---------------------------------------------------------------------- */
/* TATTRIBUTETYPE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTRIBUTETYPE_FK_1')
    ALTER TABLE TATTRIBUTETYPE DROP CONSTRAINT TATTRIBUTETYPE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TATTRIBUTETYPE')
BEGIN
     DECLARE @reftable_42 nvarchar(60), @constraintname_42 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TATTRIBUTETYPE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_42, @constraintname_42
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_42+' drop constraint '+@constraintname_42)
       FETCH NEXT from refcursor into @reftable_42, @constraintname_42
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TATTRIBUTETYPE
END
;

CREATE TABLE TATTRIBUTETYPE
(
    OBJECTID INT NOT NULL,
    ATTRIBUTETYPENAME VARCHAR (255) NOT NULL,
    ATTRIBUTECLASS INT NOT NULL,
    JAVACLASSNAME VARCHAR (255) NULL,
    VALIDATIONKEY VARCHAR (25) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TATTRIBUTETYPE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TATTTYPEINDEX1 ON TATTRIBUTETYPE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TATTRIBUTE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTRIBUTE_FK_1')
    ALTER TABLE TATTRIBUTE DROP CONSTRAINT TATTRIBUTE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TATTRIBUTE')
BEGIN
     DECLARE @reftable_43 nvarchar(60), @constraintname_43 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TATTRIBUTE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_43, @constraintname_43
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_43+' drop constraint '+@constraintname_43)
       FETCH NEXT from refcursor into @reftable_43, @constraintname_43
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TATTRIBUTE
END
;

CREATE TABLE TATTRIBUTE
(
    OBJECTID INT NOT NULL,
    ATTRIBUTENAME VARCHAR (255) NOT NULL,
    ATTRIBUTETYPE INT NOT NULL,
    DELETED INT NULL,
    DESCRIPTION VARCHAR (64) NULL,
    PERMISSION VARCHAR (255) NULL,
    REQUIREDOPTION INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TATTRIBUTE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TATTRIBUTEINDEX1 ON TATTRIBUTE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TREPORTLAYOUT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTLAYOUT_FK_1')
    ALTER TABLE TREPORTLAYOUT DROP CONSTRAINT TREPORTLAYOUT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTLAYOUT_FK_2')
    ALTER TABLE TREPORTLAYOUT DROP CONSTRAINT TREPORTLAYOUT_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTLAYOUT_FK_3')
    ALTER TABLE TREPORTLAYOUT DROP CONSTRAINT TREPORTLAYOUT_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TREPORTLAYOUT')
BEGIN
     DECLARE @reftable_44 nvarchar(60), @constraintname_44 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TREPORTLAYOUT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_44, @constraintname_44
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_44+' drop constraint '+@constraintname_44)
       FETCH NEXT from refcursor into @reftable_44, @constraintname_44
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TREPORTLAYOUT
END
;

CREATE TABLE TREPORTLAYOUT
(
    OBJECTID INT NOT NULL,
    PROJECTTYPE INT NULL,
    PROJECT INT NULL,
    PERSON INT NULL,
    REPORTFIELD INT NOT NULL,
    FPOSITION INT NOT NULL,
    FWIDTH INT NOT NULL,
    FSORTORDER INT NULL,
    FSORTDIR VARCHAR (1) NULL,
    FIELDTYPE INT NULL,
    EXPANDING VARCHAR (1) NULL,
    LAYOUTKEY INT NULL,
    QUERYID INT NULL,
    QUERYTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREPORTLAYOUT_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREPORTLAYOUTINDEX1 ON TREPORTLAYOUT (FIELDTYPE);
CREATE  INDEX TREPORTLAYOUTINDEX2 ON TREPORTLAYOUT (LAYOUTKEY);
CREATE  INDEX TREPORTLAYOUTINDEX3 ON TREPORTLAYOUT (TPUUID);
CREATE  INDEX TREPORTLAYOUTINDEX4 ON TREPORTLAYOUT (QUERYID);




/* ---------------------------------------------------------------------- */
/* TSCHEDULER                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCHEDULER_FK_1')
    ALTER TABLE TSCHEDULER DROP CONSTRAINT TSCHEDULER_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSCHEDULER')
BEGIN
     DECLARE @reftable_45 nvarchar(60), @constraintname_45 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSCHEDULER'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_45, @constraintname_45
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_45+' drop constraint '+@constraintname_45)
       FETCH NEXT from refcursor into @reftable_45, @constraintname_45
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSCHEDULER
END
;

CREATE TABLE TSCHEDULER
(
    OBJECTID INT NOT NULL,
    CRONWHEN VARCHAR (64) NOT NULL,
    JAVACLASSNAME VARCHAR (255) NULL,
    EXTERNALEXE VARCHAR (255) NULL,
    PERSON INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSCHEDULER_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSCHEDULERINDEX1 ON TSCHEDULER (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPROJECTACCOUNT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJECTACCOUNT_FK_1')
    ALTER TABLE TPROJECTACCOUNT DROP CONSTRAINT TPROJECTACCOUNT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROJECTACCOUNT_FK_2')
    ALTER TABLE TPROJECTACCOUNT DROP CONSTRAINT TPROJECTACCOUNT_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPROJECTACCOUNT')
BEGIN
     DECLARE @reftable_46 nvarchar(60), @constraintname_46 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPROJECTACCOUNT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_46, @constraintname_46
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_46+' drop constraint '+@constraintname_46)
       FETCH NEXT from refcursor into @reftable_46, @constraintname_46
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPROJECTACCOUNT
END
;

CREATE TABLE TPROJECTACCOUNT
(
    ACCOUNT INT NOT NULL,
    PROJECT INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPROJECTACCOUNT_PK PRIMARY KEY(ACCOUNT,PROJECT));

CREATE  INDEX TPROJACCINDEX1 ON TPROJECTACCOUNT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TGROUPMEMBER                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TGROUPMEMBER_FK_1')
    ALTER TABLE TGROUPMEMBER DROP CONSTRAINT TGROUPMEMBER_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TGROUPMEMBER_FK_2')
    ALTER TABLE TGROUPMEMBER DROP CONSTRAINT TGROUPMEMBER_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TGROUPMEMBER')
BEGIN
     DECLARE @reftable_47 nvarchar(60), @constraintname_47 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TGROUPMEMBER'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_47, @constraintname_47
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_47+' drop constraint '+@constraintname_47)
       FETCH NEXT from refcursor into @reftable_47, @constraintname_47
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TGROUPMEMBER
END
;

CREATE TABLE TGROUPMEMBER
(
    OBJECTID INT NOT NULL,
    THEGROUP INT NOT NULL,
    PERSON INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TGROUPMEMBER_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TGROUPMEMINDEX1 ON TGROUPMEMBER (TPUUID);




/* ---------------------------------------------------------------------- */
/* TBUDGET                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TBUDGET_FK_1')
    ALTER TABLE TBUDGET DROP CONSTRAINT TBUDGET_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TBUDGET_FK_2')
    ALTER TABLE TBUDGET DROP CONSTRAINT TBUDGET_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TBUDGET')
BEGIN
     DECLARE @reftable_48 nvarchar(60), @constraintname_48 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TBUDGET'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_48, @constraintname_48
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_48+' drop constraint '+@constraintname_48)
       FETCH NEXT from refcursor into @reftable_48, @constraintname_48
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TBUDGET
END
;

CREATE TABLE TBUDGET
(
    OBJECTID INT NOT NULL,
    WORKITEMKEY INT NOT NULL,
    ESTIMATEDHOURS FLOAT NULL,
    TIMEUNIT INT NULL,
    ESTIMATEDCOST FLOAT NULL,
    EFFORTTYPE INT NULL,
    EFFORTVALUE FLOAT NULL,
    CHANGEDESCRIPTION VARCHAR (255) NULL,
    MOREPROPS VARCHAR (3000) NULL,
    CHANGEDBY INT NULL,
    LASTEDIT DATETIME NULL,
    BUDGETTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TBUDGET_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TBUDGETINDEX1 ON TBUDGET (WORKITEMKEY);
CREATE  INDEX TBUDGETINDEX2 ON TBUDGET (CHANGEDBY);
CREATE  INDEX TBUDGETINDEX3 ON TBUDGET (TPUUID);
CREATE  INDEX TBUDGETINDEX4 ON TBUDGET (LASTEDIT);




/* ---------------------------------------------------------------------- */
/* TACTUALESTIMATEDBUDGET                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TACTUALESTIMATEDBUDGET_FK_1')
    ALTER TABLE TACTUALESTIMATEDBUDGET DROP CONSTRAINT TACTUALESTIMATEDBUDGET_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TACTUALESTIMATEDBUDGET_FK_2')
    ALTER TABLE TACTUALESTIMATEDBUDGET DROP CONSTRAINT TACTUALESTIMATEDBUDGET_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TACTUALESTIMATEDBUDGET')
BEGIN
     DECLARE @reftable_49 nvarchar(60), @constraintname_49 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TACTUALESTIMATEDBUDGET'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_49, @constraintname_49
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_49+' drop constraint '+@constraintname_49)
       FETCH NEXT from refcursor into @reftable_49, @constraintname_49
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TACTUALESTIMATEDBUDGET
END
;

CREATE TABLE TACTUALESTIMATEDBUDGET
(
    OBJECTID INT NOT NULL,
    WORKITEMKEY INT NOT NULL,
    ESTIMATEDHOURS FLOAT NULL,
    TIMEUNIT INT NULL,
    ESTIMATEDCOST FLOAT NULL,
    EFFORTTYPE INT NULL,
    EFFORTVALUE FLOAT NULL,
    CHANGEDBY INT NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TACTUALESTIMATEDBUDGET_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TACTUALESTINDEX1 ON TACTUALESTIMATEDBUDGET (WORKITEMKEY);
CREATE  INDEX TACTUALESTINDEX2 ON TACTUALESTIMATEDBUDGET (CHANGEDBY);
CREATE  INDEX TACTUALESTINDEX3 ON TACTUALESTIMATEDBUDGET (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSYSTEMSTATE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSYSTEMSTATE_FK_1')
    ALTER TABLE TSYSTEMSTATE DROP CONSTRAINT TSYSTEMSTATE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSYSTEMSTATE')
BEGIN
     DECLARE @reftable_50 nvarchar(60), @constraintname_50 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSYSTEMSTATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_50, @constraintname_50
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_50+' drop constraint '+@constraintname_50)
       FETCH NEXT from refcursor into @reftable_50, @constraintname_50
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSYSTEMSTATE
END
;

CREATE TABLE TSYSTEMSTATE
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NULL,
    STATEFLAG INT NULL,
    SYMBOL VARCHAR (255) NULL,
    ENTITYFLAG INT NULL,
    SORTORDER INT NULL,
    ICONKEY INT NULL,
    CSSSTYLE VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSYSTEMSTATE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSYSTEMSTATEINDEX1 ON TSYSTEMSTATE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCOSTCENTER                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCOSTCENTER')
BEGIN
     DECLARE @reftable_51 nvarchar(60), @constraintname_51 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCOSTCENTER'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_51, @constraintname_51
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_51+' drop constraint '+@constraintname_51)
       FETCH NEXT from refcursor into @reftable_51, @constraintname_51
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCOSTCENTER
END
;

CREATE TABLE TCOSTCENTER
(
    OBJECTID INT NOT NULL,
    COSTCENTERNUMBER VARCHAR (30) NOT NULL,
    COSTCENTERNAME VARCHAR (80) NULL,
    MOREPROPS VARCHAR (3000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCOSTCENTER_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TCOSTCENTERINDEX1 ON TCOSTCENTER (TPUUID);




/* ---------------------------------------------------------------------- */
/* TMOTD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TMOTD')
BEGIN
     DECLARE @reftable_52 nvarchar(60), @constraintname_52 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TMOTD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_52, @constraintname_52
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_52+' drop constraint '+@constraintname_52)
       FETCH NEXT from refcursor into @reftable_52, @constraintname_52
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TMOTD
END
;

CREATE TABLE TMOTD
(
    OBJECTID INT NOT NULL,
    THELOCALE VARCHAR (2) NULL,
    THEMESSAGE VARCHAR (7000) NULL,
    TEASERTEXT VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TMOTD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TMOTDINDEX1 ON TMOTD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TDASHBOARDSCREEN                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDASHBOARDSCREEN_FK_1')
    ALTER TABLE TDASHBOARDSCREEN DROP CONSTRAINT TDASHBOARDSCREEN_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDASHBOARDSCREEN_FK_2')
    ALTER TABLE TDASHBOARDSCREEN DROP CONSTRAINT TDASHBOARDSCREEN_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDASHBOARDSCREEN_FK_3')
    ALTER TABLE TDASHBOARDSCREEN DROP CONSTRAINT TDASHBOARDSCREEN_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TDASHBOARDSCREEN')
BEGIN
     DECLARE @reftable_53 nvarchar(60), @constraintname_53 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TDASHBOARDSCREEN'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_53, @constraintname_53
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_53+' drop constraint '+@constraintname_53)
       FETCH NEXT from refcursor into @reftable_53, @constraintname_53
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TDASHBOARDSCREEN
END
;

CREATE TABLE TDASHBOARDSCREEN
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    LABEL VARCHAR (255) NULL,
    PERSONPKEY INT NULL,
    PROJECT INT NULL,
    ENTITYTYPE INT NULL,
    OWNER INT NULL,
    DESCRIPTION VARCHAR (512) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TDASHBOARDSCREEN_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TDBSCREENINDEX1 ON TDASHBOARDSCREEN (TPUUID);




/* ---------------------------------------------------------------------- */
/* TDASHBOARDTAB                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDASHBOARDTAB_FK_1')
    ALTER TABLE TDASHBOARDTAB DROP CONSTRAINT TDASHBOARDTAB_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TDASHBOARDTAB')
BEGIN
     DECLARE @reftable_54 nvarchar(60), @constraintname_54 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TDASHBOARDTAB'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_54, @constraintname_54
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_54+' drop constraint '+@constraintname_54)
       FETCH NEXT from refcursor into @reftable_54, @constraintname_54
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TDASHBOARDTAB
END
;

CREATE TABLE TDASHBOARDTAB
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    LABEL VARCHAR (255) NULL,
    DESCRIPTION VARCHAR (512) NULL,
    SORTORDER INT NULL,
    PARENT INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TDASHBOARDTAB_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TDBTABINDEX1 ON TDASHBOARDTAB (TPUUID);




/* ---------------------------------------------------------------------- */
/* TDASHBOARDPANEL                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDASHBOARDPANEL_FK_1')
    ALTER TABLE TDASHBOARDPANEL DROP CONSTRAINT TDASHBOARDPANEL_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TDASHBOARDPANEL')
BEGIN
     DECLARE @reftable_55 nvarchar(60), @constraintname_55 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TDASHBOARDPANEL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_55, @constraintname_55
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_55+' drop constraint '+@constraintname_55)
       FETCH NEXT from refcursor into @reftable_55, @constraintname_55
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TDASHBOARDPANEL
END
;

CREATE TABLE TDASHBOARDPANEL
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    LABEL VARCHAR (255) NULL,
    DESCRIPTION VARCHAR (512) NULL,
    SORTORDER INT NULL,
    ROWSNO INT NOT NULL,
    COLSNO INT NOT NULL,
    PARENT INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TDASHBOARDPANEL_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TDBPANELINDEX1 ON TDASHBOARDPANEL (TPUUID);




/* ---------------------------------------------------------------------- */
/* TDASHBOARDFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDASHBOARDFIELD_FK_1')
    ALTER TABLE TDASHBOARDFIELD DROP CONSTRAINT TDASHBOARDFIELD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TDASHBOARDFIELD')
BEGIN
     DECLARE @reftable_56 nvarchar(60), @constraintname_56 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TDASHBOARDFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_56, @constraintname_56
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_56+' drop constraint '+@constraintname_56)
       FETCH NEXT from refcursor into @reftable_56, @constraintname_56
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TDASHBOARDFIELD
END
;

CREATE TABLE TDASHBOARDFIELD
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    DESCRIPTION VARCHAR (512) NULL,
    SORTORDER INT NULL,
    COLINDEX INT NULL,
    ROWINDEX INT NULL,
    COLSPAN INT NULL,
    ROWSPAN INT NULL,
    PARENT INT NOT NULL,
    DASHBOARDID VARCHAR (255) NOT NULL,
    THEDESCRIPTION VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TDASHBOARDFIELD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TDBFIELDINDEX1 ON TDASHBOARDFIELD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TDASHBOARDPARAMETER                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDASHBOARDPARAMETER_FK_1')
    ALTER TABLE TDASHBOARDPARAMETER DROP CONSTRAINT TDASHBOARDPARAMETER_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TDASHBOARDPARAMETER')
BEGIN
     DECLARE @reftable_57 nvarchar(60), @constraintname_57 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TDASHBOARDPARAMETER'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_57, @constraintname_57
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_57+' drop constraint '+@constraintname_57)
       FETCH NEXT from refcursor into @reftable_57, @constraintname_57
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TDASHBOARDPARAMETER
END
;

CREATE TABLE TDASHBOARDPARAMETER
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    PARAMVALUE VARCHAR (3000) NULL,
    DASHBOARDFIELD INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TDASHBOARDPARAMETER_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TDBPARAMINDEX1 ON TDASHBOARDPARAMETER (TPUUID);




/* ---------------------------------------------------------------------- */
/* TVERSIONCONTROLPARAMETER                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TVERSIONCONTROLPARAMETER_FK_1')
    ALTER TABLE TVERSIONCONTROLPARAMETER DROP CONSTRAINT TVERSIONCONTROLPARAMETER_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TVERSIONCONTROLPARAMETER')
BEGIN
     DECLARE @reftable_58 nvarchar(60), @constraintname_58 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TVERSIONCONTROLPARAMETER'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_58, @constraintname_58
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_58+' drop constraint '+@constraintname_58)
       FETCH NEXT from refcursor into @reftable_58, @constraintname_58
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TVERSIONCONTROLPARAMETER
END
;

CREATE TABLE TVERSIONCONTROLPARAMETER
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    PARAMVALUE VARCHAR (3000) NULL,
    PROJECT INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TVERSIONCONTROLPARAMETER_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TVCPARAMINDEX1 ON TVERSIONCONTROLPARAMETER (TPUUID);




/* ---------------------------------------------------------------------- */
/* TFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELD_FK_1')
    ALTER TABLE TFIELD DROP CONSTRAINT TFIELD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELD_FK_2')
    ALTER TABLE TFIELD DROP CONSTRAINT TFIELD_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELD_FK_3')
    ALTER TABLE TFIELD DROP CONSTRAINT TFIELD_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TFIELD')
BEGIN
     DECLARE @reftable_59 nvarchar(60), @constraintname_59 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_59, @constraintname_59
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_59+' drop constraint '+@constraintname_59)
       FETCH NEXT from refcursor into @reftable_59, @constraintname_59
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TFIELD
END
;

CREATE TABLE TFIELD
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    FIELDTYPE VARCHAR (255) NOT NULL,
    DEPRECATED CHAR (1) default 'N' NULL,
    ISCUSTOM CHAR (1) default 'Y' NOT NULL,
    REQUIRED CHAR (1) default 'N' NOT NULL,
    FILTERFIELD CHAR (1) default 'N' NULL,
    DESCRIPTION VARCHAR (512) NULL,
    OWNER INT NULL,
    PROJECTTYPE INT NULL,
    PROJECT INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TFIELD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TFIELDINDEX1 ON TFIELD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TFIELDCONFIG                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELDCONFIG_FK_1')
    ALTER TABLE TFIELDCONFIG DROP CONSTRAINT TFIELDCONFIG_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELDCONFIG_FK_2')
    ALTER TABLE TFIELDCONFIG DROP CONSTRAINT TFIELDCONFIG_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELDCONFIG_FK_3')
    ALTER TABLE TFIELDCONFIG DROP CONSTRAINT TFIELDCONFIG_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELDCONFIG_FK_4')
    ALTER TABLE TFIELDCONFIG DROP CONSTRAINT TFIELDCONFIG_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELDCONFIG_FK_5')
    ALTER TABLE TFIELDCONFIG DROP CONSTRAINT TFIELDCONFIG_FK_5;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TFIELDCONFIG')
BEGIN
     DECLARE @reftable_60 nvarchar(60), @constraintname_60 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TFIELDCONFIG'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_60, @constraintname_60
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_60+' drop constraint '+@constraintname_60)
       FETCH NEXT from refcursor into @reftable_60, @constraintname_60
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TFIELDCONFIG
END
;

CREATE TABLE TFIELDCONFIG
(
    OBJECTID INT NOT NULL,
    FIELDKEY INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    TOOLTIP VARCHAR (255) NULL,
    REQUIRED CHAR (1) default 'N' NOT NULL,
    HISTORY CHAR (1) default 'N' NOT NULL,
    ISSUETYPE INT NULL,
    PROJECTTYPE INT NULL,
    PROJECT INT NULL,
    DESCRIPTION VARCHAR (512) NULL,
    GROOVYSCRIPT INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TFIELDCONFIG_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TFIELDCONFIGINDEX1 ON TFIELDCONFIG (TPUUID);




/* ---------------------------------------------------------------------- */
/* TROLEFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TROLEFIELD_FK_1')
    ALTER TABLE TROLEFIELD DROP CONSTRAINT TROLEFIELD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TROLEFIELD')
BEGIN
     DECLARE @reftable_61 nvarchar(60), @constraintname_61 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TROLEFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_61, @constraintname_61
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_61+' drop constraint '+@constraintname_61)
       FETCH NEXT from refcursor into @reftable_61, @constraintname_61
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TROLEFIELD
END
;

CREATE TABLE TROLEFIELD
(
    OBJECTID INT NOT NULL,
    ROLEKEY INT NOT NULL,
    FIELDKEY INT NOT NULL,
    ACCESSRIGHT INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TROLEFIELD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TROLEFIELDINDEX1 ON TROLEFIELD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCONFIGOPTIONSROLE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCONFIGOPTIONSROLE_FK_1')
    ALTER TABLE TCONFIGOPTIONSROLE DROP CONSTRAINT TCONFIGOPTIONSROLE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCONFIGOPTIONSROLE_FK_2')
    ALTER TABLE TCONFIGOPTIONSROLE DROP CONSTRAINT TCONFIGOPTIONSROLE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCONFIGOPTIONSROLE_FK_3')
    ALTER TABLE TCONFIGOPTIONSROLE DROP CONSTRAINT TCONFIGOPTIONSROLE_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCONFIGOPTIONSROLE')
BEGIN
     DECLARE @reftable_62 nvarchar(60), @constraintname_62 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCONFIGOPTIONSROLE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_62, @constraintname_62
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_62+' drop constraint '+@constraintname_62)
       FETCH NEXT from refcursor into @reftable_62, @constraintname_62
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCONFIGOPTIONSROLE
END
;

CREATE TABLE TCONFIGOPTIONSROLE
(
    OBJECTID INT NOT NULL,
    CONFIGKEY INT NOT NULL,
    ROLEKEY INT NOT NULL,
    OPTIONKEY INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCONFIGOPTIONSROLE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TCONFIGOPTINDEX1 ON TCONFIGOPTIONSROLE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TTEXTBOXSETTINGS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TTEXTBOXSETTINGS_FK_1')
    ALTER TABLE TTEXTBOXSETTINGS DROP CONSTRAINT TTEXTBOXSETTINGS_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TTEXTBOXSETTINGS')
BEGIN
     DECLARE @reftable_63 nvarchar(60), @constraintname_63 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TTEXTBOXSETTINGS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_63, @constraintname_63
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_63+' drop constraint '+@constraintname_63)
       FETCH NEXT from refcursor into @reftable_63, @constraintname_63
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TTEXTBOXSETTINGS
END
;

CREATE TABLE TTEXTBOXSETTINGS
(
    OBJECTID INT NOT NULL,
    CONFIG INT NOT NULL,
    REQUIRED VARCHAR (1) default 'N' NOT NULL,
    DEFAULTTEXT VARCHAR (255) NULL,
    DEFAULTINTEGER INT NULL,
    DEFAULTDOUBLE FLOAT NULL,
    DEFAULTDATE DATETIME NULL,
    DEFAULTCHAR CHAR (1) NULL,
    DATEISWITHTIME CHAR (1) default 'N' NULL,
    DEFAULTOPTION INT NULL,
    MINOPTION INT NULL,
    MAXOPTION INT NULL,
    MINTEXTLENGTH INT NULL,
    MAXTEXTLENGTH INT NULL,
    MINDATE DATETIME NULL,
    MAXDATE DATETIME NULL,
    MININTEGER INT NULL,
    MAXINTEGER INT NULL,
    MINDOUBLE FLOAT NULL,
    MAXDOUBLE FLOAT NULL,
    MAXDECIMALDIGIT INT NULL,
    PARAMETERCODE INT NULL,
    VALIDVALUE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TTEXTBOXSETTINGS_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TTEXTBOXCONFINDEX1 ON TTEXTBOXSETTINGS (TPUUID);




/* ---------------------------------------------------------------------- */
/* TGENERALSETTINGS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TGENERALSETTINGS_FK_1')
    ALTER TABLE TGENERALSETTINGS DROP CONSTRAINT TGENERALSETTINGS_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TGENERALSETTINGS')
BEGIN
     DECLARE @reftable_64 nvarchar(60), @constraintname_64 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TGENERALSETTINGS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_64, @constraintname_64
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_64+' drop constraint '+@constraintname_64)
       FETCH NEXT from refcursor into @reftable_64, @constraintname_64
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TGENERALSETTINGS
END
;

CREATE TABLE TGENERALSETTINGS
(
    OBJECTID INT NOT NULL,
    CONFIG INT NOT NULL,
    INTEGERVALUE INT NULL,
    DOUBLEVALUE FLOAT NULL,
    TEXTVALUE VARCHAR (255) NULL,
    DATEVALUE DATETIME NULL,
    CHARACTERVALUE CHAR (1) NULL,
    PARAMETERCODE INT NULL,
    VALIDVALUE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TGENERALSETTINGS_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TGENERALCONFINDEX1 ON TGENERALSETTINGS (TPUUID);




/* ---------------------------------------------------------------------- */
/* TLIST                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLIST_FK_1')
    ALTER TABLE TLIST DROP CONSTRAINT TLIST_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLIST_FK_2')
    ALTER TABLE TLIST DROP CONSTRAINT TLIST_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLIST_FK_3')
    ALTER TABLE TLIST DROP CONSTRAINT TLIST_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLIST')
BEGIN
     DECLARE @reftable_65 nvarchar(60), @constraintname_65 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TLIST'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_65, @constraintname_65
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_65+' drop constraint '+@constraintname_65)
       FETCH NEXT from refcursor into @reftable_65, @constraintname_65
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TLIST
END
;

CREATE TABLE TLIST
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    DESCRIPTION VARCHAR (255) NULL,
    TAGLABEL VARCHAR (50) NULL,
    PARENTLIST INT NULL,
    LISTTYPE INT NULL,
    CHILDNUMBER INT NULL,
    DELETED CHAR (1) default 'N' NULL,
    REPOSITORYTYPE INT NULL,
    PROJECT INT NULL,
    OWNER INT NULL,
    MOREPROPS VARCHAR (3000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TLIST_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TLISTINDEX1 ON TLIST (TPUUID);




/* ---------------------------------------------------------------------- */
/* TOPTION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TOPTION_FK_1')
    ALTER TABLE TOPTION DROP CONSTRAINT TOPTION_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TOPTION_FK_2')
    ALTER TABLE TOPTION DROP CONSTRAINT TOPTION_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TOPTION')
BEGIN
     DECLARE @reftable_66 nvarchar(60), @constraintname_66 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TOPTION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_66, @constraintname_66
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_66+' drop constraint '+@constraintname_66)
       FETCH NEXT from refcursor into @reftable_66, @constraintname_66
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TOPTION
END
;

CREATE TABLE TOPTION
(
    OBJECTID INT NOT NULL,
    LIST INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    TOOLTIP VARCHAR (255) NULL,
    PARENTOPTION INT NULL,
    SORTORDER INT NULL,
    ISDEFAULT CHAR (1) default 'N' NOT NULL,
    DELETED CHAR (1) default 'N' NULL,
    SYMBOL VARCHAR (255) NULL,
    ICONKEY INT NULL,
    ICONCHANGED CHAR (1) default 'N' NULL,
    CSSSTYLE VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TOPTION_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TOPTIONINDEX1 ON TOPTION (TPUUID);




/* ---------------------------------------------------------------------- */
/* TOPTIONSETTINGS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TOPTIONSETTINGS_FK_1')
    ALTER TABLE TOPTIONSETTINGS DROP CONSTRAINT TOPTIONSETTINGS_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TOPTIONSETTINGS_FK_2')
    ALTER TABLE TOPTIONSETTINGS DROP CONSTRAINT TOPTIONSETTINGS_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TOPTIONSETTINGS')
BEGIN
     DECLARE @reftable_67 nvarchar(60), @constraintname_67 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TOPTIONSETTINGS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_67, @constraintname_67
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_67+' drop constraint '+@constraintname_67)
       FETCH NEXT from refcursor into @reftable_67, @constraintname_67
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TOPTIONSETTINGS
END
;

CREATE TABLE TOPTIONSETTINGS
(
    OBJECTID INT NOT NULL,
    LIST INT NOT NULL,
    CONFIG INT NOT NULL,
    PARAMETERCODE INT NULL,
    MULTIPLE CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TOPTIONSETTINGS_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TOPTIONCONFINDEX1 ON TOPTIONSETTINGS (TPUUID);




/* ---------------------------------------------------------------------- */
/* TATTRIBUTEVALUE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTRIBUTEVALUE_FK_1')
    ALTER TABLE TATTRIBUTEVALUE DROP CONSTRAINT TATTRIBUTEVALUE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTRIBUTEVALUE_FK_2')
    ALTER TABLE TATTRIBUTEVALUE DROP CONSTRAINT TATTRIBUTEVALUE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TATTRIBUTEVALUE')
BEGIN
     DECLARE @reftable_68 nvarchar(60), @constraintname_68 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TATTRIBUTEVALUE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_68, @constraintname_68
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_68+' drop constraint '+@constraintname_68)
       FETCH NEXT from refcursor into @reftable_68, @constraintname_68
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TATTRIBUTEVALUE
END
;

CREATE TABLE TATTRIBUTEVALUE
(
    OBJECTID INT NOT NULL,
    FIELDKEY INT NOT NULL,
    WORKITEM INT NOT NULL,
    TEXTVALUE VARCHAR (255) NULL,
    INTEGERVALUE INT NULL,
    DOUBLEVALUE FLOAT NULL,
    DATEVALUE DATETIME NULL,
    CHARACTERVALUE CHAR (1) NULL,
    LONGTEXTVALUE VARCHAR (7000) NULL,
    SYSTEMOPTIONID INT NULL,
    SYSTEMOPTIONTYPE INT NULL,
    CUSTOMOPTIONID INT NULL,
    PARAMETERCODE INT NULL,
    VALIDVALUE INT NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TATTRIBUTEVALUE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TATTVALUEINDEX1 ON TATTRIBUTEVALUE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSCREEN                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREEN_FK_1')
    ALTER TABLE TSCREEN DROP CONSTRAINT TSCREEN_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREEN_FK_2')
    ALTER TABLE TSCREEN DROP CONSTRAINT TSCREEN_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREEN_FK_3')
    ALTER TABLE TSCREEN DROP CONSTRAINT TSCREEN_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSCREEN')
BEGIN
     DECLARE @reftable_69 nvarchar(60), @constraintname_69 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSCREEN'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_69, @constraintname_69
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_69+' drop constraint '+@constraintname_69)
       FETCH NEXT from refcursor into @reftable_69, @constraintname_69
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSCREEN
END
;

CREATE TABLE TSCREEN
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    LABEL VARCHAR (255) NULL,
    TAGLABEL VARCHAR (50) NULL,
    DESCRIPTION VARCHAR (512) NULL,
    OWNER INT NULL,
    PROJECTTYPE INT NULL,
    PROJECT INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSCREEN_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSCREENINDEX1 ON TSCREEN (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSCREENTAB                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREENTAB_FK_1')
    ALTER TABLE TSCREENTAB DROP CONSTRAINT TSCREENTAB_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSCREENTAB')
BEGIN
     DECLARE @reftable_70 nvarchar(60), @constraintname_70 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSCREENTAB'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_70, @constraintname_70
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_70+' drop constraint '+@constraintname_70)
       FETCH NEXT from refcursor into @reftable_70, @constraintname_70
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSCREENTAB
END
;

CREATE TABLE TSCREENTAB
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    LABEL VARCHAR (255) NULL,
    DESCRIPTION VARCHAR (512) NULL,
    SORTORDER INT NULL,
    PARENT INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSCREENTAB_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSCREENTABINDEX1 ON TSCREENTAB (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSCREENPANEL                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREENPANEL_FK_1')
    ALTER TABLE TSCREENPANEL DROP CONSTRAINT TSCREENPANEL_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSCREENPANEL')
BEGIN
     DECLARE @reftable_71 nvarchar(60), @constraintname_71 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSCREENPANEL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_71, @constraintname_71
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_71+' drop constraint '+@constraintname_71)
       FETCH NEXT from refcursor into @reftable_71, @constraintname_71
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSCREENPANEL
END
;

CREATE TABLE TSCREENPANEL
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    LABEL VARCHAR (255) NULL,
    DESCRIPTION VARCHAR (512) NULL,
    SORTORDER INT NULL,
    ROWSNO INT NOT NULL,
    COLSNO INT NOT NULL,
    PARENT INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSCREENPANEL_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSCREENPANELINDEX1 ON TSCREENPANEL (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSCREENFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREENFIELD_FK_1')
    ALTER TABLE TSCREENFIELD DROP CONSTRAINT TSCREENFIELD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREENFIELD_FK_2')
    ALTER TABLE TSCREENFIELD DROP CONSTRAINT TSCREENFIELD_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSCREENFIELD')
BEGIN
     DECLARE @reftable_72 nvarchar(60), @constraintname_72 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSCREENFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_72, @constraintname_72
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_72+' drop constraint '+@constraintname_72)
       FETCH NEXT from refcursor into @reftable_72, @constraintname_72
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSCREENFIELD
END
;

CREATE TABLE TSCREENFIELD
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    DESCRIPTION VARCHAR (512) NULL,
    SORTORDER INT NULL,
    COLINDEX INT NULL,
    ROWINDEX INT NULL,
    COLSPAN INT NULL,
    ROWSPAN INT NULL,
    LABELHALIGN INT NOT NULL,
    LABELVALIGN INT NOT NULL,
    VALUEHALIGN INT NOT NULL,
    VALUEVALIGN INT NOT NULL,
    ISEMPTY CHAR (1) default 'N' NOT NULL,
    PARENT INT NOT NULL,
    FIELDKEY INT NULL,
    HIDELABEL CHAR (1) default 'N' NULL,
    ICONRENDERING INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSCREENFIELD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSCREENFIELDINDEX1 ON TSCREENFIELD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TACTION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TACTION_FK_1')
    ALTER TABLE TACTION DROP CONSTRAINT TACTION_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TACTION')
BEGIN
     DECLARE @reftable_73 nvarchar(60), @constraintname_73 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TACTION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_73, @constraintname_73
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_73+' drop constraint '+@constraintname_73)
       FETCH NEXT from refcursor into @reftable_73, @constraintname_73
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TACTION
END
;

CREATE TABLE TACTION
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    LABEL VARCHAR (255) NULL,
    DESCRIPTION VARCHAR (512) NULL,
    ACTIONTYPE INT NULL,
    ICONKEY INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TACTION_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TACTIONINDEX1 ON TACTION (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSCREENCONFIG                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREENCONFIG_FK_1')
    ALTER TABLE TSCREENCONFIG DROP CONSTRAINT TSCREENCONFIG_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREENCONFIG_FK_2')
    ALTER TABLE TSCREENCONFIG DROP CONSTRAINT TSCREENCONFIG_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREENCONFIG_FK_3')
    ALTER TABLE TSCREENCONFIG DROP CONSTRAINT TSCREENCONFIG_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREENCONFIG_FK_4')
    ALTER TABLE TSCREENCONFIG DROP CONSTRAINT TSCREENCONFIG_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREENCONFIG_FK_5')
    ALTER TABLE TSCREENCONFIG DROP CONSTRAINT TSCREENCONFIG_FK_5;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSCREENCONFIG')
BEGIN
     DECLARE @reftable_74 nvarchar(60), @constraintname_74 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSCREENCONFIG'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_74, @constraintname_74
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_74+' drop constraint '+@constraintname_74)
       FETCH NEXT from refcursor into @reftable_74, @constraintname_74
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSCREENCONFIG
END
;

CREATE TABLE TSCREENCONFIG
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NULL,
    DESCRIPTION VARCHAR (512) NULL,
    SCREEN INT NULL,
    ISSUETYPE INT NULL,
    PROJECTTYPE INT NULL,
    PROJECT INT NULL,
    ACTIONKEY INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSCREENCONFIG_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSCREENCONFINDEX1 ON TSCREENCONFIG (TPUUID);




/* ---------------------------------------------------------------------- */
/* TINITSTATE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TINITSTATE_FK_1')
    ALTER TABLE TINITSTATE DROP CONSTRAINT TINITSTATE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TINITSTATE_FK_2')
    ALTER TABLE TINITSTATE DROP CONSTRAINT TINITSTATE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TINITSTATE_FK_3')
    ALTER TABLE TINITSTATE DROP CONSTRAINT TINITSTATE_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TINITSTATE')
BEGIN
     DECLARE @reftable_75 nvarchar(60), @constraintname_75 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TINITSTATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_75, @constraintname_75
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_75+' drop constraint '+@constraintname_75)
       FETCH NEXT from refcursor into @reftable_75, @constraintname_75
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TINITSTATE
END
;

CREATE TABLE TINITSTATE
(
    OBJECTID INT NOT NULL,
    PROJECT INT NOT NULL,
    LISTTYPE INT NOT NULL,
    STATEKEY INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TINITSTATE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TINITSTATEINDEX1 ON TINITSTATE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TEVENT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TEVENT_FK_1')
    ALTER TABLE TEVENT DROP CONSTRAINT TEVENT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TEVENT_FK_2')
    ALTER TABLE TEVENT DROP CONSTRAINT TEVENT_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TEVENT_FK_3')
    ALTER TABLE TEVENT DROP CONSTRAINT TEVENT_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TEVENT')
BEGIN
     DECLARE @reftable_76 nvarchar(60), @constraintname_76 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TEVENT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_76, @constraintname_76
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_76+' drop constraint '+@constraintname_76)
       FETCH NEXT from refcursor into @reftable_76, @constraintname_76
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TEVENT
END
;

CREATE TABLE TEVENT
(
    OBJECTID INT NOT NULL,
    EVENTNAME VARCHAR (255) NULL,
    EVENTTYPE INT NOT NULL,
    PROJECTTYPE INT NULL,
    PROJECT INT NULL,
    EVENTSCRIPT INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TEVENT_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TEVENTINDEX1 ON TEVENT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCLOB                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCLOB')
BEGIN
     DECLARE @reftable_77 nvarchar(60), @constraintname_77 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCLOB'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_77, @constraintname_77
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_77+' drop constraint '+@constraintname_77)
       FETCH NEXT from refcursor into @reftable_77, @constraintname_77
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCLOB
END
;

CREATE TABLE TCLOB
(
    OBJECTID INT NOT NULL,
    CLOBVALUE TEXT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCLOB_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TCLOBINDEX1 ON TCLOB (TPUUID);




/* ---------------------------------------------------------------------- */
/* TNOTIFYFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNOTIFYFIELD_FK_1')
    ALTER TABLE TNOTIFYFIELD DROP CONSTRAINT TNOTIFYFIELD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TNOTIFYFIELD')
BEGIN
     DECLARE @reftable_78 nvarchar(60), @constraintname_78 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TNOTIFYFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_78, @constraintname_78
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_78+' drop constraint '+@constraintname_78)
       FETCH NEXT from refcursor into @reftable_78, @constraintname_78
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TNOTIFYFIELD
END
;

CREATE TABLE TNOTIFYFIELD
(
    OBJECTID INT NOT NULL,
    FIELD INT NULL,
    ACTIONTYPE INT NOT NULL,
    FIELDTYPE INT NULL,
    NOTIFYTRIGGER INT NOT NULL,
    ORIGINATOR CHAR (1) default 'N' NULL,
    MANAGER CHAR (1) default 'N' NULL,
    RESPONSIBLE CHAR (1) default 'N' NULL,
    CONSULTANT CHAR (1) default 'N' NULL,
    INFORMANT CHAR (1) default 'N' NULL,
    OBSERVER CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TNOTIFYFIELD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TNOTIFYFIELDINDEX1 ON TNOTIFYFIELD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TNOTIFYTRIGGER                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNOTIFYTRIGGER_FK_1')
    ALTER TABLE TNOTIFYTRIGGER DROP CONSTRAINT TNOTIFYTRIGGER_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TNOTIFYTRIGGER')
BEGIN
     DECLARE @reftable_79 nvarchar(60), @constraintname_79 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TNOTIFYTRIGGER'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_79, @constraintname_79
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_79+' drop constraint '+@constraintname_79)
       FETCH NEXT from refcursor into @reftable_79, @constraintname_79
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TNOTIFYTRIGGER
END
;

CREATE TABLE TNOTIFYTRIGGER
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NULL,
    PERSON INT NULL,
    ISSYSTEM CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TNOTIFYTRIGGER_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TNOTIFYTRIGGERINDEX1 ON TNOTIFYTRIGGER (TPUUID);




/* ---------------------------------------------------------------------- */
/* TNOTIFYSETTINGS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNOTIFYSETTINGS_FK_1')
    ALTER TABLE TNOTIFYSETTINGS DROP CONSTRAINT TNOTIFYSETTINGS_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNOTIFYSETTINGS_FK_2')
    ALTER TABLE TNOTIFYSETTINGS DROP CONSTRAINT TNOTIFYSETTINGS_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNOTIFYSETTINGS_FK_3')
    ALTER TABLE TNOTIFYSETTINGS DROP CONSTRAINT TNOTIFYSETTINGS_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNOTIFYSETTINGS_FK_4')
    ALTER TABLE TNOTIFYSETTINGS DROP CONSTRAINT TNOTIFYSETTINGS_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TNOTIFYSETTINGS')
BEGIN
     DECLARE @reftable_80 nvarchar(60), @constraintname_80 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TNOTIFYSETTINGS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_80, @constraintname_80
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_80+' drop constraint '+@constraintname_80)
       FETCH NEXT from refcursor into @reftable_80, @constraintname_80
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TNOTIFYSETTINGS
END
;

CREATE TABLE TNOTIFYSETTINGS
(
    OBJECTID INT NOT NULL,
    PERSON INT NULL,
    PROJECT INT NULL,
    NOTIFYTRIGGER INT NULL,
    NOTIFYFILTER INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TNOTIFYSETTINGS_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TNOTIFYCONFINDEX1 ON TNOTIFYSETTINGS (TPUUID);




/* ---------------------------------------------------------------------- */
/* TQUERYREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TQUERYREPOSITORY_FK_1')
    ALTER TABLE TQUERYREPOSITORY DROP CONSTRAINT TQUERYREPOSITORY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TQUERYREPOSITORY_FK_2')
    ALTER TABLE TQUERYREPOSITORY DROP CONSTRAINT TQUERYREPOSITORY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TQUERYREPOSITORY_FK_3')
    ALTER TABLE TQUERYREPOSITORY DROP CONSTRAINT TQUERYREPOSITORY_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TQUERYREPOSITORY_FK_4')
    ALTER TABLE TQUERYREPOSITORY DROP CONSTRAINT TQUERYREPOSITORY_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TQUERYREPOSITORY')
BEGIN
     DECLARE @reftable_81 nvarchar(60), @constraintname_81 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TQUERYREPOSITORY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_81, @constraintname_81
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_81+' drop constraint '+@constraintname_81)
       FETCH NEXT from refcursor into @reftable_81, @constraintname_81
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TQUERYREPOSITORY
END
;

CREATE TABLE TQUERYREPOSITORY
(
    OBJECTID INT NOT NULL,
    PERSON INT NULL,
    PROJECT INT NULL,
    LABEL VARCHAR (100) NULL,
    QUERYTYPE INT NOT NULL,
    REPOSITORYTYPE INT NOT NULL,
    QUERYKEY INT NOT NULL,
    MENUITEM CHAR (1) default 'N' NULL,
    CATEGORYKEY INT NULL,
    SORTORDER INT NULL,
    VIEWID VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TQUERYREPOSITORY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TQUERYREPINDEX1 ON TQUERYREPOSITORY (TPUUID);
CREATE  INDEX TQUERYREPINDEX2 ON TQUERYREPOSITORY (SORTORDER);




/* ---------------------------------------------------------------------- */
/* TLOCALIZEDRESOURCES                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLOCALIZEDRESOURCES')
BEGIN
     DECLARE @reftable_82 nvarchar(60), @constraintname_82 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TLOCALIZEDRESOURCES'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_82, @constraintname_82
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_82+' drop constraint '+@constraintname_82)
       FETCH NEXT from refcursor into @reftable_82, @constraintname_82
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TLOCALIZEDRESOURCES
END
;

CREATE TABLE TLOCALIZEDRESOURCES
(
    OBJECTID INT NOT NULL,
    TABLENAME VARCHAR (255) NULL,
    PRIMARYKEYVALUE INT NULL,
    FIELDNAME VARCHAR (255) NOT NULL,
    LOCALIZEDTEXT VARCHAR (7000) NULL,
    LOCALE VARCHAR (20) NULL,
    TEXTCHANGED CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TLOCALIZEDRESOURCES_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TLOCALRESINDEX1 ON TLOCALIZEDRESOURCES (TPUUID);




/* ---------------------------------------------------------------------- */
/* TLINKTYPE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLINKTYPE_FK_1')
    ALTER TABLE TLINKTYPE DROP CONSTRAINT TLINKTYPE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLINKTYPE_FK_2')
    ALTER TABLE TLINKTYPE DROP CONSTRAINT TLINKTYPE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLINKTYPE')
BEGIN
     DECLARE @reftable_83 nvarchar(60), @constraintname_83 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TLINKTYPE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_83, @constraintname_83
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_83+' drop constraint '+@constraintname_83)
       FETCH NEXT from refcursor into @reftable_83, @constraintname_83
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TLINKTYPE
END
;

CREATE TABLE TLINKTYPE
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    REVERSENAME VARCHAR (255) NULL,
    LEFTTORIGHTFIRST VARCHAR (255) NULL,
    LEFTTORIGHTLEVEL VARCHAR (255) NULL,
    LEFTTORIGHTALL VARCHAR (255) NULL,
    RIGHTTOLEFTFIRST VARCHAR (255) NULL,
    RIGHTTOLEFTLEVEL VARCHAR (255) NULL,
    RIGHTTOLEFTALL VARCHAR (255) NULL,
    LINKDIRECTION INT NULL,
    OUTWARDICONKEY INT NULL,
    INWARDICONKEY INT NULL,
    LINKTYPEPLUGIN VARCHAR (255) NULL,
    MOREPROPS VARCHAR (3000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TLINKTYPE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TLINKTYPEINDEX1 ON TLINKTYPE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKITEMLINK                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEMLINK_FK_1')
    ALTER TABLE TWORKITEMLINK DROP CONSTRAINT TWORKITEMLINK_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEMLINK_FK_2')
    ALTER TABLE TWORKITEMLINK DROP CONSTRAINT TWORKITEMLINK_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEMLINK_FK_3')
    ALTER TABLE TWORKITEMLINK DROP CONSTRAINT TWORKITEMLINK_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEMLINK_FK_4')
    ALTER TABLE TWORKITEMLINK DROP CONSTRAINT TWORKITEMLINK_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKITEMLINK')
BEGIN
     DECLARE @reftable_84 nvarchar(60), @constraintname_84 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKITEMLINK'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_84, @constraintname_84
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_84+' drop constraint '+@constraintname_84)
       FETCH NEXT from refcursor into @reftable_84, @constraintname_84
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKITEMLINK
END
;

CREATE TABLE TWORKITEMLINK
(
    OBJECTID INT NOT NULL,
    LINKISCROSSPROJECT CHAR (1) default 'N' NULL,
    LINKPRED INT NOT NULL,
    LINKSUCC INT NULL,
    LINKTYPE INT NOT NULL,
    LINKDIRECTION INT NULL,
    SORTORDER INT NULL,
    LINKLAG FLOAT NULL,
    LINKLAGFORMAT INT NULL,
    STRINGVALUE1 VARCHAR (255) NULL,
    STRINGVALUE2 VARCHAR (255) NULL,
    STRINGVALUE3 VARCHAR (255) NULL,
    INTEGERVALUE1 INT NULL,
    INTEGERVALUE2 INT NULL,
    INTEGERVALUE3 INT NULL,
    DATEVALUE DATETIME NULL,
    DESCRIPTION VARCHAR (512) NULL,
    EXTERNALLINK VARCHAR (255) NULL,
    CHANGEDBY INT NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKITEMLINK_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKITEMLINKINDEX1 ON TWORKITEMLINK (TPUUID);




/* ---------------------------------------------------------------------- */
/* TLOGGINGLEVEL                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLOGGINGLEVEL')
BEGIN
     DECLARE @reftable_85 nvarchar(60), @constraintname_85 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TLOGGINGLEVEL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_85, @constraintname_85
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_85+' drop constraint '+@constraintname_85)
       FETCH NEXT from refcursor into @reftable_85, @constraintname_85
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TLOGGINGLEVEL
END
;

CREATE TABLE TLOGGINGLEVEL
(
    OBJECTID INT NOT NULL,
    THECLASSNAME VARCHAR (255) NOT NULL,
    LOGLEVEL VARCHAR (10) NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TLOGGINGLEVEL_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TLOGLEVELINDEX1 ON TLOGGINGLEVEL (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKITEMLOCK                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEMLOCK_FK_1')
    ALTER TABLE TWORKITEMLOCK DROP CONSTRAINT TWORKITEMLOCK_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEMLOCK_FK_2')
    ALTER TABLE TWORKITEMLOCK DROP CONSTRAINT TWORKITEMLOCK_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKITEMLOCK')
BEGIN
     DECLARE @reftable_86 nvarchar(60), @constraintname_86 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKITEMLOCK'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_86, @constraintname_86
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_86+' drop constraint '+@constraintname_86)
       FETCH NEXT from refcursor into @reftable_86, @constraintname_86
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKITEMLOCK
END
;

CREATE TABLE TWORKITEMLOCK
(
    WORKITEM INT NOT NULL,
    PERSON INT NOT NULL,
    HTTPSESSION VARCHAR (255) NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKITEMLOCK_PK PRIMARY KEY(WORKITEM));

CREATE  INDEX TWORKITEMLOCKINDEX1 ON TWORKITEMLOCK (TPUUID);




/* ---------------------------------------------------------------------- */
/* TEXPORTTEMPLATE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TEXPORTTEMPLATE_FK_1')
    ALTER TABLE TEXPORTTEMPLATE DROP CONSTRAINT TEXPORTTEMPLATE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TEXPORTTEMPLATE_FK_2')
    ALTER TABLE TEXPORTTEMPLATE DROP CONSTRAINT TEXPORTTEMPLATE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TEXPORTTEMPLATE_FK_3')
    ALTER TABLE TEXPORTTEMPLATE DROP CONSTRAINT TEXPORTTEMPLATE_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TEXPORTTEMPLATE_FK_4')
    ALTER TABLE TEXPORTTEMPLATE DROP CONSTRAINT TEXPORTTEMPLATE_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TEXPORTTEMPLATE')
BEGIN
     DECLARE @reftable_87 nvarchar(60), @constraintname_87 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TEXPORTTEMPLATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_87, @constraintname_87
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_87+' drop constraint '+@constraintname_87)
       FETCH NEXT from refcursor into @reftable_87, @constraintname_87
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TEXPORTTEMPLATE
END
;

CREATE TABLE TEXPORTTEMPLATE
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    REPORTTYPE VARCHAR (255) NOT NULL,
    EXPORTFORMAT VARCHAR (255) NOT NULL,
    REPOSITORYTYPE INT NOT NULL,
    DESCRIPTION VARCHAR (512) NULL,
    PROJECT INT NULL,
    PERSON INT NOT NULL,
    CATEGORYKEY INT NULL,
    PARENT INT NULL,
    SORTORDER INT NULL,
    DELETED CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TEXPORTTEMPLATE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TEXPOPRTTEMPINDEX1 ON TEXPORTTEMPLATE (TPUUID);
CREATE  INDEX TEXPOPRTTEMPINDEX2 ON TEXPORTTEMPLATE (SORTORDER);




/* ---------------------------------------------------------------------- */
/* TEMAILPROCESSED                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TEMAILPROCESSED')
BEGIN
     DECLARE @reftable_88 nvarchar(60), @constraintname_88 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TEMAILPROCESSED'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_88, @constraintname_88
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_88+' drop constraint '+@constraintname_88)
       FETCH NEXT from refcursor into @reftable_88, @constraintname_88
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TEMAILPROCESSED
END
;

CREATE TABLE TEMAILPROCESSED
(
    OBJECTID INT NOT NULL,
    PROCESSEDDATE DATETIME NOT NULL,
    MESSAGEUID VARCHAR (255) NOT NULL,
    RECEIVEDAT VARCHAR (255) NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TEMAILPROCESSED_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TEMAILPROCINDEX1 ON TEMAILPROCESSED (TPUUID);




/* ---------------------------------------------------------------------- */
/* TAPPLICATIONCONTEXT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TAPPLICATIONCONTEXT')
BEGIN
     DECLARE @reftable_89 nvarchar(60), @constraintname_89 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TAPPLICATIONCONTEXT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_89, @constraintname_89
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_89+' drop constraint '+@constraintname_89)
       FETCH NEXT from refcursor into @reftable_89, @constraintname_89
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TAPPLICATIONCONTEXT
END
;

CREATE TABLE TAPPLICATIONCONTEXT
(
    OBJECTID INT NOT NULL,
    LOGGEDFULLUSERS INT NULL,
    LOGGEDLIMITEDUSERS INT NULL,
    REFRESHCONFIGURATION INT default 0 NOT NULL,
    FIRSTTIME INT default 0 NOT NULL,
    MOREPROPS VARCHAR (3000) NULL,

    CONSTRAINT TAPPLICATIONCONTEXT_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TLOGGEDINUSERS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLOGGEDINUSERS_FK_1')
    ALTER TABLE TLOGGEDINUSERS DROP CONSTRAINT TLOGGEDINUSERS_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLOGGEDINUSERS_FK_2')
    ALTER TABLE TLOGGEDINUSERS DROP CONSTRAINT TLOGGEDINUSERS_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLOGGEDINUSERS')
BEGIN
     DECLARE @reftable_90 nvarchar(60), @constraintname_90 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TLOGGEDINUSERS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_90, @constraintname_90
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_90+' drop constraint '+@constraintname_90)
       FETCH NEXT from refcursor into @reftable_90, @constraintname_90
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TLOGGEDINUSERS
END
;

CREATE TABLE TLOGGEDINUSERS
(
    OBJECTID INT NOT NULL,
    NODEADDRESS INT NULL,
    SESSIONID VARCHAR (255) NULL,
    LOGGEDUSER INT NOT NULL,
    USERLEVEL INT NULL,
    LASTUPDATE DATETIME NULL,
    MOREPROPS VARCHAR (3000) NULL,

    CONSTRAINT TLOGGEDINUSERS_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* CLUSTERNODE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'CLUSTERNODE')
BEGIN
     DECLARE @reftable_91 nvarchar(60), @constraintname_91 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'CLUSTERNODE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_91, @constraintname_91
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_91+' drop constraint '+@constraintname_91)
       FETCH NEXT from refcursor into @reftable_91, @constraintname_91
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE CLUSTERNODE
END
;

CREATE TABLE CLUSTERNODE
(
    OBJECTID INT NOT NULL,
    NODEADDRESS VARCHAR (40) NULL,
    NODEURL VARCHAR (255) NULL,
    LASTUPDATE DATETIME NULL,
    MASTERNODE INT default 0 NULL,
    RELOADCONFIG INT default 0 NULL,
    RELOADCHANGES VARCHAR (255) NULL,

    CONSTRAINT CLUSTERNODE_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TENTITYCHANGES                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TENTITYCHANGES_FK_1')
    ALTER TABLE TENTITYCHANGES DROP CONSTRAINT TENTITYCHANGES_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TENTITYCHANGES')
BEGIN
     DECLARE @reftable_92 nvarchar(60), @constraintname_92 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TENTITYCHANGES'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_92, @constraintname_92
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_92+' drop constraint '+@constraintname_92)
       FETCH NEXT from refcursor into @reftable_92, @constraintname_92
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TENTITYCHANGES
END
;

CREATE TABLE TENTITYCHANGES
(
    OBJECTID INT NOT NULL,
    ENTITYKEY INT NULL,
    ENTITYTYPE INT NOT NULL,
    CLUSTERNODE INT NULL,
    LIST INT NULL,
    CHANGETYPE INT NULL,

    CONSTRAINT TENTITYCHANGES_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX ENTITYCHANGESINDEX1 ON TENTITYCHANGES (ENTITYTYPE);
CREATE  INDEX ENTITYCHANGESINDEX2 ON TENTITYCHANGES (CLUSTERNODE);




/* ---------------------------------------------------------------------- */
/* TSUMMARYMAIL                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSUMMARYMAIL_FK_1')
    ALTER TABLE TSUMMARYMAIL DROP CONSTRAINT TSUMMARYMAIL_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSUMMARYMAIL_FK_2')
    ALTER TABLE TSUMMARYMAIL DROP CONSTRAINT TSUMMARYMAIL_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSUMMARYMAIL_FK_3')
    ALTER TABLE TSUMMARYMAIL DROP CONSTRAINT TSUMMARYMAIL_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSUMMARYMAIL')
BEGIN
     DECLARE @reftable_93 nvarchar(60), @constraintname_93 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSUMMARYMAIL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_93, @constraintname_93
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_93+' drop constraint '+@constraintname_93)
       FETCH NEXT from refcursor into @reftable_93, @constraintname_93
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSUMMARYMAIL
END
;

CREATE TABLE TSUMMARYMAIL
(
    OBJECTID INT NOT NULL,
    WORKITEM INT NOT NULL,
    PERSONFROM INT NOT NULL,
    FROMADDRESS VARCHAR (255) NULL,
    MAILSUBJECT VARCHAR (255) NULL,
    WORKITEMLINK VARCHAR (255) NULL,
    PERSONTO INT NOT NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSUMMARYMAIL_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSUMMARYMAILINDEX1 ON TSUMMARYMAIL (WORKITEM);
CREATE  INDEX TSUMMARYMAILINDEX2 ON TSUMMARYMAIL (PERSONFROM);
CREATE  INDEX TSUMMARYMAILINDEX3 ON TSUMMARYMAIL (PERSONTO);
CREATE  INDEX TSUMMARYMAILINDEX4 ON TSUMMARYMAIL (TPUUID);




/* ---------------------------------------------------------------------- */
/* TOUTLINECODE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TOUTLINECODE_FK_1')
    ALTER TABLE TOUTLINECODE DROP CONSTRAINT TOUTLINECODE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TOUTLINECODE')
BEGIN
     DECLARE @reftable_94 nvarchar(60), @constraintname_94 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TOUTLINECODE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_94, @constraintname_94
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_94+' drop constraint '+@constraintname_94)
       FETCH NEXT from refcursor into @reftable_94, @constraintname_94
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TOUTLINECODE
END
;

CREATE TABLE TOUTLINECODE
(
    OBJECTID INT NOT NULL,
    PARENTID INT NULL,
    LEVELNO INT NOT NULL,
    LEVELCODE VARCHAR (50) NOT NULL,
    FULLCODE VARCHAR (255) NOT NULL,
    ENTITYID INT NOT NULL,
    OUTLINETEMPLATE INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TOUTLINECODE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TOUTLINECODEINDEX1 ON TOUTLINECODE (PARENTID);
CREATE  INDEX TOUTLINECODEINDEX2 ON TOUTLINECODE (ENTITYID);
CREATE  INDEX TOUTLINECODEINDEX3 ON TOUTLINECODE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TOUTLINETEMPLATEDEF                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TOUTLINETEMPLATEDEF_FK_1')
    ALTER TABLE TOUTLINETEMPLATEDEF DROP CONSTRAINT TOUTLINETEMPLATEDEF_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TOUTLINETEMPLATEDEF')
BEGIN
     DECLARE @reftable_95 nvarchar(60), @constraintname_95 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TOUTLINETEMPLATEDEF'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_95, @constraintname_95
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_95+' drop constraint '+@constraintname_95)
       FETCH NEXT from refcursor into @reftable_95, @constraintname_95
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TOUTLINETEMPLATEDEF
END
;

CREATE TABLE TOUTLINETEMPLATEDEF
(
    OBJECTID INT NOT NULL,
    LEVELNO INT NOT NULL,
    SEQUENCETYPE INT NOT NULL,
    LISTID INT NULL,
    SEQUENCELENGTH INT NOT NULL,
    SEPARATORCHAR VARCHAR (10) NOT NULL,
    OUTLINETEMPLATE INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TOUTLINETEMPLATEDEF_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TOUTLINETEMPLATEDEFINDEX1 ON TOUTLINETEMPLATEDEF (TPUUID);




/* ---------------------------------------------------------------------- */
/* TOUTLINETEMPLATE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TOUTLINETEMPLATE')
BEGIN
     DECLARE @reftable_96 nvarchar(60), @constraintname_96 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TOUTLINETEMPLATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_96, @constraintname_96
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_96+' drop constraint '+@constraintname_96)
       FETCH NEXT from refcursor into @reftable_96, @constraintname_96
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TOUTLINETEMPLATE
END
;

CREATE TABLE TOUTLINETEMPLATE
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    ENTITYTYPE INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TOUTLINETEMPLATE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TOUTLINETEMPLATEINDEX1 ON TOUTLINETEMPLATE (TPUUID);




/* ---------------------------------------------------------------------- */
/* THISTORYTRANSACTION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='THISTORYTRANSACTION_FK_1')
    ALTER TABLE THISTORYTRANSACTION DROP CONSTRAINT THISTORYTRANSACTION_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='THISTORYTRANSACTION_FK_2')
    ALTER TABLE THISTORYTRANSACTION DROP CONSTRAINT THISTORYTRANSACTION_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'THISTORYTRANSACTION')
BEGIN
     DECLARE @reftable_97 nvarchar(60), @constraintname_97 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'THISTORYTRANSACTION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_97, @constraintname_97
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_97+' drop constraint '+@constraintname_97)
       FETCH NEXT from refcursor into @reftable_97, @constraintname_97
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE THISTORYTRANSACTION
END
;

CREATE TABLE THISTORYTRANSACTION
(
    OBJECTID INT NOT NULL,
    WORKITEM INT NOT NULL,
    CHANGEDBY INT NOT NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT THISTORYTRANSACTION_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX THISTORYTRANSINDEX1 ON THISTORYTRANSACTION (WORKITEM);
CREATE  INDEX THISTORYTRANSINDEX2 ON THISTORYTRANSACTION (CHANGEDBY);
CREATE  INDEX THISTORYTRANSINDEX3 ON THISTORYTRANSACTION (TPUUID);




/* ---------------------------------------------------------------------- */
/* TFIELDCHANGE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELDCHANGE_FK_1')
    ALTER TABLE TFIELDCHANGE DROP CONSTRAINT TFIELDCHANGE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELDCHANGE_FK_2')
    ALTER TABLE TFIELDCHANGE DROP CONSTRAINT TFIELDCHANGE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELDCHANGE_FK_3')
    ALTER TABLE TFIELDCHANGE DROP CONSTRAINT TFIELDCHANGE_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELDCHANGE_FK_4')
    ALTER TABLE TFIELDCHANGE DROP CONSTRAINT TFIELDCHANGE_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TFIELDCHANGE')
BEGIN
     DECLARE @reftable_98 nvarchar(60), @constraintname_98 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TFIELDCHANGE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_98, @constraintname_98
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_98+' drop constraint '+@constraintname_98)
       FETCH NEXT from refcursor into @reftable_98, @constraintname_98
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TFIELDCHANGE
END
;

CREATE TABLE TFIELDCHANGE
(
    OBJECTID INT NOT NULL,
    FIELDKEY INT NOT NULL,
    HISTORYTRANSACTION INT NOT NULL,
    NEWTEXTVALUE VARCHAR (255) NULL,
    OLDTEXTVALUE VARCHAR (255) NULL,
    NEWINTEGERVALUE INT NULL,
    OLDINTEGERVALUE INT NULL,
    NEWDOUBLEVALUE FLOAT NULL,
    OLDDOUBLEVALUE FLOAT NULL,
    NEWDATEVALUE DATETIME NULL,
    OLDDATEVALUE DATETIME NULL,
    NEWCHARACTERVALUE CHAR (1) NULL,
    OLDCHARACTERVALUE CHAR (1) NULL,
    NEWLONGTEXTVALUE VARCHAR (7000) NULL,
    OLDLONGTEXTVALUE VARCHAR (7000) NULL,
    NEWSYSTEMOPTIONID INT NULL,
    OLDSYSTEMOPTIONID INT NULL,
    SYSTEMOPTIONTYPE INT NULL,
    NEWCUSTOMOPTIONID INT NULL,
    OLDCUSTOMOPTIONID INT NULL,
    PARAMETERCODE INT NULL,
    VALIDVALUE INT NULL,
    PARENTCOMMENT INT NULL,
    TIMESEDITED INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TFIELDCHANGE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TFIELDCHANGE1 ON TFIELDCHANGE (FIELDKEY);
CREATE  INDEX TFIELDCHANGE2 ON TFIELDCHANGE (HISTORYTRANSACTION);
CREATE  INDEX TFIELDCHANGE3 ON TFIELDCHANGE (VALIDVALUE);
CREATE  INDEX TFIELDCHANGE4 ON TFIELDCHANGE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSCRIPTS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCRIPTS_FK_1')
    ALTER TABLE TSCRIPTS DROP CONSTRAINT TSCRIPTS_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCRIPTS_FK_2')
    ALTER TABLE TSCRIPTS DROP CONSTRAINT TSCRIPTS_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCRIPTS_FK_3')
    ALTER TABLE TSCRIPTS DROP CONSTRAINT TSCRIPTS_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSCRIPTS')
BEGIN
     DECLARE @reftable_99 nvarchar(60), @constraintname_99 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSCRIPTS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_99, @constraintname_99
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_99+' drop constraint '+@constraintname_99)
       FETCH NEXT from refcursor into @reftable_99, @constraintname_99
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSCRIPTS
END
;

CREATE TABLE TSCRIPTS
(
    OBJECTID INT NOT NULL,
    CHANGEDBY INT NULL,
    LASTEDIT DATETIME NULL,
    SCRIPTVERSION INT NULL,
    ORIGINALVERSION INT NULL,
    PROJECTTYPE INT NULL,
    PROJECT INT NULL,
    SCRIPTTYPE INT NULL,
    SCRIPTROLE INT NULL,
    CLAZZNAME VARCHAR (253) NULL,
    SOURCECODE VARCHAR (7000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSCRIPTS_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSCRIPTINDEX1 ON TSCRIPTS (CLAZZNAME);
CREATE  INDEX TSCRIPTINDEX2 ON TSCRIPTS (PROJECTTYPE);
CREATE  INDEX TSCRIPTINDEX3 ON TSCRIPTS (PROJECT);
CREATE  INDEX TSCRIPTINDEX4 ON TSCRIPTS (TPUUID);




/* ---------------------------------------------------------------------- */
/* TREVISION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREVISION_FK_1')
    ALTER TABLE TREVISION DROP CONSTRAINT TREVISION_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TREVISION')
BEGIN
     DECLARE @reftable_100 nvarchar(60), @constraintname_100 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TREVISION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_100, @constraintname_100
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_100+' drop constraint '+@constraintname_100)
       FETCH NEXT from refcursor into @reftable_100, @constraintname_100
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TREVISION
END
;

CREATE TABLE TREVISION
(
    OBJECTID INT NOT NULL,
    FILENAME VARCHAR (255) NOT NULL,
    AUTHORNAME VARCHAR (255) NOT NULL,
    CHANGEDESCRIPTION VARCHAR (512) NULL,
    REVISIONDATE DATETIME NOT NULL,
    REVISIONNUMBR VARCHAR (255) NOT NULL,
    REPOSITORYKEY INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREVISION_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREVISIONINDEX1 ON TREVISION (TPUUID);




/* ---------------------------------------------------------------------- */
/* TREVISIONWORKITEMS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREVISIONWORKITEMS_FK_1')
    ALTER TABLE TREVISIONWORKITEMS DROP CONSTRAINT TREVISIONWORKITEMS_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TREVISIONWORKITEMS')
BEGIN
     DECLARE @reftable_101 nvarchar(60), @constraintname_101 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TREVISIONWORKITEMS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_101, @constraintname_101
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_101+' drop constraint '+@constraintname_101)
       FETCH NEXT from refcursor into @reftable_101, @constraintname_101
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TREVISIONWORKITEMS
END
;

CREATE TABLE TREVISIONWORKITEMS
(
    OBJECTID INT NOT NULL,
    WORKITEMKEY INT NOT NULL,
    REVISIONKEY INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREVISIONWORKITEMS_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREVISIONWORKITEMSINDEX1 ON TREVISIONWORKITEMS (WORKITEMKEY);
CREATE  INDEX TREVISIONWORKITEMSINDEX2 ON TREVISIONWORKITEMS (TPUUID);




/* ---------------------------------------------------------------------- */
/* TREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TREPOSITORY')
BEGIN
     DECLARE @reftable_102 nvarchar(60), @constraintname_102 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TREPOSITORY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_102, @constraintname_102
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_102+' drop constraint '+@constraintname_102)
       FETCH NEXT from refcursor into @reftable_102, @constraintname_102
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TREPOSITORY
END
;

CREATE TABLE TREPOSITORY
(
    OBJECTID INT NOT NULL,
    REPOSITORYTYPE VARCHAR (255) NOT NULL,
    REPOSITORYURL VARCHAR (255) NOT NULL,
    STARTDATE DATETIME NULL,
    ENDDATE DATETIME NULL,
    STATUSKEY INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREPOSITORY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREPOSITORYINDEX1 ON TREPOSITORY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TTEMPLATEPERSON                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TTEMPLATEPERSON_FK_1')
    ALTER TABLE TTEMPLATEPERSON DROP CONSTRAINT TTEMPLATEPERSON_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TTEMPLATEPERSON_FK_2')
    ALTER TABLE TTEMPLATEPERSON DROP CONSTRAINT TTEMPLATEPERSON_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TTEMPLATEPERSON')
BEGIN
     DECLARE @reftable_103 nvarchar(60), @constraintname_103 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TTEMPLATEPERSON'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_103, @constraintname_103
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_103+' drop constraint '+@constraintname_103)
       FETCH NEXT from refcursor into @reftable_103, @constraintname_103
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TTEMPLATEPERSON
END
;

CREATE TABLE TTEMPLATEPERSON
(
    OBJECTID INT NOT NULL,
    REPORTTEMPLATE INT NOT NULL,
    PERSON INT NOT NULL,
    RIGHTFLAG INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TTEMPLATEPERSON_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TTEMPLATEPERSONINDEX1 ON TTEMPLATEPERSON (TPUUID);




/* ---------------------------------------------------------------------- */
/* TBLOB                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TBLOB')
BEGIN
     DECLARE @reftable_104 nvarchar(60), @constraintname_104 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TBLOB'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_104, @constraintname_104
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_104+' drop constraint '+@constraintname_104)
       FETCH NEXT from refcursor into @reftable_104, @constraintname_104
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TBLOB
END
;

CREATE TABLE TBLOB
(
    OBJECTID INT NOT NULL,
    BLOBVALUE IMAGE NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TBLOB_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TBLOBINDEX1 ON TBLOB (TPUUID);




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
END
;

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
    ALTER TABLE TREPORTPERSONSETTINGS DROP CONSTRAINT TREPORTPERSONSETTINGS_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTPERSONSETTINGS_FK_2')
    ALTER TABLE TREPORTPERSONSETTINGS DROP CONSTRAINT TREPORTPERSONSETTINGS_FK_2;
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
END
;

CREATE TABLE TREPORTPERSONSETTINGS
(
    OBJECTID INT NOT NULL,
    PERSON INT NOT NULL,
    REPORTTEMPLATE INT NOT NULL,
    PARAMSETTINGS VARCHAR (3000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREPORTPERSONSETTINGS_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREPORTPERSONSETTINGSINDEX1 ON TREPORTPERSONSETTINGS (TPUUID);




/* ---------------------------------------------------------------------- */
/* TREPORTPARAMETER                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTPARAMETER_FK_1')
    ALTER TABLE TREPORTPARAMETER DROP CONSTRAINT TREPORTPARAMETER_FK_1;
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
END
;

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
    ALTER TABLE TMSPROJECTTASK DROP CONSTRAINT TMSPROJECTTASK_FK_1;
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
END
;

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
    ALTER TABLE TMSPROJECTEXCHANGE DROP CONSTRAINT TMSPROJECTEXCHANGE_FK_1;
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
END
;

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




/* ---------------------------------------------------------------------- */
/* TFILTERCATEGORY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFILTERCATEGORY_FK_1')
    ALTER TABLE TFILTERCATEGORY DROP CONSTRAINT TFILTERCATEGORY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFILTERCATEGORY_FK_2')
    ALTER TABLE TFILTERCATEGORY DROP CONSTRAINT TFILTERCATEGORY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFILTERCATEGORY_FK_3')
    ALTER TABLE TFILTERCATEGORY DROP CONSTRAINT TFILTERCATEGORY_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TFILTERCATEGORY')
BEGIN
     DECLARE @reftable_110 nvarchar(60), @constraintname_110 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TFILTERCATEGORY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_110, @constraintname_110
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_110+' drop constraint '+@constraintname_110)
       FETCH NEXT from refcursor into @reftable_110, @constraintname_110
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TFILTERCATEGORY
END
;

CREATE TABLE TFILTERCATEGORY
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    REPOSITORY INT NOT NULL,
    FILTERTYPE INT NOT NULL,
    CREATEDBY INT NOT NULL,
    PROJECT INT NULL,
    PARENTID INT NULL,
    SORTORDER INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TFILTERCATEGORY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TFILTERCATEGORYINDEX1 ON TFILTERCATEGORY (TPUUID);
CREATE  INDEX TFILTERCATEGORYINDEX2 ON TFILTERCATEGORY (SORTORDER);




/* ---------------------------------------------------------------------- */
/* TREPORTCATEGORY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTCATEGORY_FK_1')
    ALTER TABLE TREPORTCATEGORY DROP CONSTRAINT TREPORTCATEGORY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTCATEGORY_FK_2')
    ALTER TABLE TREPORTCATEGORY DROP CONSTRAINT TREPORTCATEGORY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTCATEGORY_FK_3')
    ALTER TABLE TREPORTCATEGORY DROP CONSTRAINT TREPORTCATEGORY_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TREPORTCATEGORY')
BEGIN
     DECLARE @reftable_111 nvarchar(60), @constraintname_111 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TREPORTCATEGORY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_111, @constraintname_111
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_111+' drop constraint '+@constraintname_111)
       FETCH NEXT from refcursor into @reftable_111, @constraintname_111
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TREPORTCATEGORY
END
;

CREATE TABLE TREPORTCATEGORY
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    REPOSITORY INT NOT NULL,
    CREATEDBY INT NOT NULL,
    PROJECT INT NULL,
    PARENTID INT NULL,
    SORTORDER INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREPORTCATEGORY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREPORTCATEGORYINDEX1 ON TREPORTCATEGORY (TPUUID);
CREATE  INDEX TREPORTCATEGORYINDEX2 ON TREPORTCATEGORY (SORTORDER);




/* ---------------------------------------------------------------------- */
/* TMENUITEMQUERY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMENUITEMQUERY_FK_1')
    ALTER TABLE TMENUITEMQUERY DROP CONSTRAINT TMENUITEMQUERY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMENUITEMQUERY_FK_2')
    ALTER TABLE TMENUITEMQUERY DROP CONSTRAINT TMENUITEMQUERY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TMENUITEMQUERY')
BEGIN
     DECLARE @reftable_112 nvarchar(60), @constraintname_112 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TMENUITEMQUERY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_112, @constraintname_112
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_112+' drop constraint '+@constraintname_112)
       FETCH NEXT from refcursor into @reftable_112, @constraintname_112
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TMENUITEMQUERY
END
;

CREATE TABLE TMENUITEMQUERY
(
    OBJECTID INT NOT NULL,
    PERSON INT NOT NULL,
    QUERYKEY INT NOT NULL,
    INCLUDEINMENU CHAR (1) default 'N' NULL,
    CSSSTYLEFIELD INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TMENUITEMQUERY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TMENUITEMQUERYINDEX1 ON TMENUITEMQUERY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCHILDISSUETYPE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCHILDISSUETYPE_FK_1')
    ALTER TABLE TCHILDISSUETYPE DROP CONSTRAINT TCHILDISSUETYPE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCHILDISSUETYPE_FK_2')
    ALTER TABLE TCHILDISSUETYPE DROP CONSTRAINT TCHILDISSUETYPE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCHILDISSUETYPE')
BEGIN
     DECLARE @reftable_113 nvarchar(60), @constraintname_113 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCHILDISSUETYPE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_113, @constraintname_113
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_113+' drop constraint '+@constraintname_113)
       FETCH NEXT from refcursor into @reftable_113, @constraintname_113
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCHILDISSUETYPE
END
;

CREATE TABLE TCHILDISSUETYPE
(
    OBJECTID INT NOT NULL,
    ISSUETYPEPARENT INT NOT NULL,
    ISSUETYPECHILD INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCHILDISSUETYPE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TCHILDISSUETYPEINDEX1 ON TCHILDISSUETYPE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPERSONBASKET                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSONBASKET_FK_1')
    ALTER TABLE TPERSONBASKET DROP CONSTRAINT TPERSONBASKET_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSONBASKET_FK_2')
    ALTER TABLE TPERSONBASKET DROP CONSTRAINT TPERSONBASKET_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSONBASKET_FK_3')
    ALTER TABLE TPERSONBASKET DROP CONSTRAINT TPERSONBASKET_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPERSONBASKET')
BEGIN
     DECLARE @reftable_114 nvarchar(60), @constraintname_114 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPERSONBASKET'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_114, @constraintname_114
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_114+' drop constraint '+@constraintname_114)
       FETCH NEXT from refcursor into @reftable_114, @constraintname_114
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPERSONBASKET
END
;

CREATE TABLE TPERSONBASKET
(
    OBJECTID INT NOT NULL,
    BASKET INT NOT NULL,
    WORKITEM INT NOT NULL,
    PERSON INT NULL,
    REMINDERDATE DATETIME NULL,
    DELEGATETEXT VARCHAR (7000) NULL,
    MOREPROPS VARCHAR (7000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPERSONBASKET_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TPERSONBASKETINDEX1 ON TPERSONBASKET (PERSON);
CREATE  INDEX TPERSONBASKETINDEX2 ON TPERSONBASKET (TPUUID);




/* ---------------------------------------------------------------------- */
/* TBASKET                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TBASKET_FK_1')
    ALTER TABLE TBASKET DROP CONSTRAINT TBASKET_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TBASKET')
BEGIN
     DECLARE @reftable_115 nvarchar(60), @constraintname_115 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TBASKET'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_115, @constraintname_115
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_115+' drop constraint '+@constraintname_115)
       FETCH NEXT from refcursor into @reftable_115, @constraintname_115
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TBASKET
END
;

CREATE TABLE TBASKET
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NOT NULL,
    DIVISIBLE CHAR (1) default 'N' NULL,
    PARENTBASKET INT NULL,
    PERSON INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TBASKET_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TBASKETINDEX1 ON TBASKET (TPUUID);




/* ---------------------------------------------------------------------- */
/* TMAILTEMPLATECONFIG                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMAILTEMPLATECONFIG_FK_1')
    ALTER TABLE TMAILTEMPLATECONFIG DROP CONSTRAINT TMAILTEMPLATECONFIG_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMAILTEMPLATECONFIG_FK_2')
    ALTER TABLE TMAILTEMPLATECONFIG DROP CONSTRAINT TMAILTEMPLATECONFIG_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMAILTEMPLATECONFIG_FK_3')
    ALTER TABLE TMAILTEMPLATECONFIG DROP CONSTRAINT TMAILTEMPLATECONFIG_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMAILTEMPLATECONFIG_FK_4')
    ALTER TABLE TMAILTEMPLATECONFIG DROP CONSTRAINT TMAILTEMPLATECONFIG_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TMAILTEMPLATECONFIG')
BEGIN
     DECLARE @reftable_116 nvarchar(60), @constraintname_116 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TMAILTEMPLATECONFIG'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_116, @constraintname_116
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_116+' drop constraint '+@constraintname_116)
       FETCH NEXT from refcursor into @reftable_116, @constraintname_116
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TMAILTEMPLATECONFIG
END
;

CREATE TABLE TMAILTEMPLATECONFIG
(
    OBJECTID INT NOT NULL,
    MAILTEMPLATE INT NOT NULL,
    ISSUETYPE INT NULL,
    PROJECTTYPE INT NULL,
    PROJECT INT NULL,
    EVENTKEY INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TMAILTEMPLATECONFIG_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TMAILTEMPLATECONFIGINDEX1 ON TMAILTEMPLATECONFIG (TPUUID);




/* ---------------------------------------------------------------------- */
/* TMAILTEMPLATE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TMAILTEMPLATE')
BEGIN
     DECLARE @reftable_117 nvarchar(60), @constraintname_117 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TMAILTEMPLATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_117, @constraintname_117
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_117+' drop constraint '+@constraintname_117)
       FETCH NEXT from refcursor into @reftable_117, @constraintname_117
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TMAILTEMPLATE
END
;

CREATE TABLE TMAILTEMPLATE
(
    OBJECTID INT NOT NULL,
    TEMPLATETYPE INT NULL,
    NAME VARCHAR (255) NULL,
    DESCRIPTION VARCHAR (255) NULL,
    TAGLABEL VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TMAILTEMPLATE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TMAILTEMPLATEINDEX1 ON TMAILTEMPLATE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TMAILTEMPLATEDEF                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMAILTEMPLATEDEF_FK_1')
    ALTER TABLE TMAILTEMPLATEDEF DROP CONSTRAINT TMAILTEMPLATEDEF_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TMAILTEMPLATEDEF')
BEGIN
     DECLARE @reftable_118 nvarchar(60), @constraintname_118 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TMAILTEMPLATEDEF'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_118, @constraintname_118
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_118+' drop constraint '+@constraintname_118)
       FETCH NEXT from refcursor into @reftable_118, @constraintname_118
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TMAILTEMPLATEDEF
END
;

CREATE TABLE TMAILTEMPLATEDEF
(
    OBJECTID INT NOT NULL,
    MAILTEMPLATE INT NOT NULL,
    MAILSUBJECT VARCHAR (255) NULL,
    MAILBODY TEXT NULL,
    THELOCALE VARCHAR (20) NULL,
    PLAINEMAIL CHAR (1) default 'N' NULL,
    TEMPLATECHANGED CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TMAILTEMPLATEDEF_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TMAILTEMPLATEDEFINDEX1 ON TMAILTEMPLATEDEF (TPUUID);




/* ---------------------------------------------------------------------- */
/* TLASTVISITEDITEM                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLASTVISITEDITEM_FK_1')
    ALTER TABLE TLASTVISITEDITEM DROP CONSTRAINT TLASTVISITEDITEM_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLASTVISITEDITEM_FK_2')
    ALTER TABLE TLASTVISITEDITEM DROP CONSTRAINT TLASTVISITEDITEM_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLASTVISITEDITEM')
BEGIN
     DECLARE @reftable_119 nvarchar(60), @constraintname_119 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TLASTVISITEDITEM'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_119, @constraintname_119
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_119+' drop constraint '+@constraintname_119)
       FETCH NEXT from refcursor into @reftable_119, @constraintname_119
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TLASTVISITEDITEM
END
;

CREATE TABLE TLASTVISITEDITEM
(
    OBJECTID INT NOT NULL,
    WORKITEM INT NOT NULL,
    PERSON INT NOT NULL,
    LASTVISITEDDATE DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TLASTVISITEDITEM_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TLASTVISITEDITEMINDEX1 ON TLASTVISITEDITEM (PERSON);
CREATE  INDEX TLASTVISITEDITEMINDEX2 ON TLASTVISITEDITEM (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKFLOWDEF                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWDEF_FK_1')
    ALTER TABLE TWORKFLOWDEF DROP CONSTRAINT TWORKFLOWDEF_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKFLOWDEF')
BEGIN
     DECLARE @reftable_120 nvarchar(60), @constraintname_120 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKFLOWDEF'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_120, @constraintname_120
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_120+' drop constraint '+@constraintname_120)
       FETCH NEXT from refcursor into @reftable_120, @constraintname_120
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKFLOWDEF
END
;

CREATE TABLE TWORKFLOWDEF
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    DESCRIPTION VARCHAR (255) NULL,
    TAGLABEL VARCHAR (255) NULL,
    OWNER INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKFLOWDEF_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKFLOWDEFINDEX1 ON TWORKFLOWDEF (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKFLOWTRANSITION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWTRANSITION_FK_1')
    ALTER TABLE TWORKFLOWTRANSITION DROP CONSTRAINT TWORKFLOWTRANSITION_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWTRANSITION_FK_2')
    ALTER TABLE TWORKFLOWTRANSITION DROP CONSTRAINT TWORKFLOWTRANSITION_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWTRANSITION_FK_3')
    ALTER TABLE TWORKFLOWTRANSITION DROP CONSTRAINT TWORKFLOWTRANSITION_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWTRANSITION_FK_4')
    ALTER TABLE TWORKFLOWTRANSITION DROP CONSTRAINT TWORKFLOWTRANSITION_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKFLOWTRANSITION')
BEGIN
     DECLARE @reftable_121 nvarchar(60), @constraintname_121 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKFLOWTRANSITION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_121, @constraintname_121
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_121+' drop constraint '+@constraintname_121)
       FETCH NEXT from refcursor into @reftable_121, @constraintname_121
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKFLOWTRANSITION
END
;

CREATE TABLE TWORKFLOWTRANSITION
(
    OBJECTID INT NOT NULL,
    STATIONFROM INT NULL,
    STATIONTO INT NULL,
    ACTIONKEY INT NULL,
    WORKFLOW INT NOT NULL,
    TEXTPOSITIONX INT NULL,
    TEXTPOSITIONY INT NULL,
    TRANSITIONTYPE INT NULL,
    CONTROLPOINTS VARCHAR (255) NULL,
    ELAPSEDTIME INT NULL,
    TIMEUNIT INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKFLOWTRANSITION_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKFLOWTRANSITIONINDEX1 ON TWORKFLOWTRANSITION (WORKFLOW);
CREATE  INDEX TWORKFLOWTRANSITIONINDEX2 ON TWORKFLOWTRANSITION (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKFLOWSTATION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWSTATION_FK_1')
    ALTER TABLE TWORKFLOWSTATION DROP CONSTRAINT TWORKFLOWSTATION_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWSTATION_FK_2')
    ALTER TABLE TWORKFLOWSTATION DROP CONSTRAINT TWORKFLOWSTATION_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKFLOWSTATION')
BEGIN
     DECLARE @reftable_122 nvarchar(60), @constraintname_122 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKFLOWSTATION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_122, @constraintname_122
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_122+' drop constraint '+@constraintname_122)
       FETCH NEXT from refcursor into @reftable_122, @constraintname_122
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKFLOWSTATION
END
;

CREATE TABLE TWORKFLOWSTATION
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NULL,
    STATUS INT NULL,
    WORKFLOW INT NULL,
    NODEX INT NULL,
    NODEY INT NULL,
    STATIONTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKFLOWSTATION_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKFLOWSTATIONINDEX1 ON TWORKFLOWSTATION (STATUS);
CREATE  INDEX TWORKFLOWSTATIONINDEX2 ON TWORKFLOWSTATION (WORKFLOW);
CREATE  INDEX TWORKFLOWSTATIONINDEX3 ON TWORKFLOWSTATION (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKFLOWACTIVITY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWACTIVITY_FK_1')
    ALTER TABLE TWORKFLOWACTIVITY DROP CONSTRAINT TWORKFLOWACTIVITY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWACTIVITY_FK_2')
    ALTER TABLE TWORKFLOWACTIVITY DROP CONSTRAINT TWORKFLOWACTIVITY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWACTIVITY_FK_3')
    ALTER TABLE TWORKFLOWACTIVITY DROP CONSTRAINT TWORKFLOWACTIVITY_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWACTIVITY_FK_4')
    ALTER TABLE TWORKFLOWACTIVITY DROP CONSTRAINT TWORKFLOWACTIVITY_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWACTIVITY_FK_5')
    ALTER TABLE TWORKFLOWACTIVITY DROP CONSTRAINT TWORKFLOWACTIVITY_FK_5;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWACTIVITY_FK_6')
    ALTER TABLE TWORKFLOWACTIVITY DROP CONSTRAINT TWORKFLOWACTIVITY_FK_6;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWACTIVITY_FK_7')
    ALTER TABLE TWORKFLOWACTIVITY DROP CONSTRAINT TWORKFLOWACTIVITY_FK_7;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWACTIVITY_FK_8')
    ALTER TABLE TWORKFLOWACTIVITY DROP CONSTRAINT TWORKFLOWACTIVITY_FK_8;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWACTIVITY_FK_9')
    ALTER TABLE TWORKFLOWACTIVITY DROP CONSTRAINT TWORKFLOWACTIVITY_FK_9;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKFLOWACTIVITY')
BEGIN
     DECLARE @reftable_123 nvarchar(60), @constraintname_123 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKFLOWACTIVITY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_123, @constraintname_123
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_123+' drop constraint '+@constraintname_123)
       FETCH NEXT from refcursor into @reftable_123, @constraintname_123
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKFLOWACTIVITY
END
;

CREATE TABLE TWORKFLOWACTIVITY
(
    OBJECTID INT NOT NULL,
    TRANSITIONACTIVITY INT NULL,
    STATIONENTRYACTIVITY INT NULL,
    STATIONEXITACTIVITY INT NULL,
    STATIONDOACTIVITY INT NULL,
    ACTIVITYTYPE INT NOT NULL,
    ACTIVITYPARAMS VARCHAR (7000) NULL,
    GROOVYSCRIPT INT NULL,
    NEWMAN INT NULL,
    NEWRESP INT NULL,
    FIELDSETTERRELATION INT NULL,
    PARAMNAME VARCHAR (255) NULL,
    FIELDSETMODE INT NULL,
    SORTORDER INT NULL,
    SLA INT NULL,
    SCREEN INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKFLOWACTIVITY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKFLOWACTIVITYINDEX1 ON TWORKFLOWACTIVITY (TRANSITIONACTIVITY);
CREATE  INDEX TWORKFLOWACTIVITYINDEX2 ON TWORKFLOWACTIVITY (STATIONENTRYACTIVITY);
CREATE  INDEX TWORKFLOWACTIVITYINDEX3 ON TWORKFLOWACTIVITY (STATIONEXITACTIVITY);
CREATE  INDEX TWORKFLOWACTIVITYINDEX4 ON TWORKFLOWACTIVITY (STATIONDOACTIVITY);
CREATE  INDEX TWORKFLOWACTIVITYINDEX5 ON TWORKFLOWACTIVITY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWFACTIVITYCONTEXTPARAMS                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWFACTIVITYCONTEXTPARAMS_FK_1')
    ALTER TABLE TWFACTIVITYCONTEXTPARAMS DROP CONSTRAINT TWFACTIVITYCONTEXTPARAMS_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWFACTIVITYCONTEXTPARAMS_FK_2')
    ALTER TABLE TWFACTIVITYCONTEXTPARAMS DROP CONSTRAINT TWFACTIVITYCONTEXTPARAMS_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWFACTIVITYCONTEXTPARAMS_FK_3')
    ALTER TABLE TWFACTIVITYCONTEXTPARAMS DROP CONSTRAINT TWFACTIVITYCONTEXTPARAMS_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWFACTIVITYCONTEXTPARAMS_FK_4')
    ALTER TABLE TWFACTIVITYCONTEXTPARAMS DROP CONSTRAINT TWFACTIVITYCONTEXTPARAMS_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWFACTIVITYCONTEXTPARAMS')
BEGIN
     DECLARE @reftable_124 nvarchar(60), @constraintname_124 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWFACTIVITYCONTEXTPARAMS'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_124, @constraintname_124
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_124+' drop constraint '+@constraintname_124)
       FETCH NEXT from refcursor into @reftable_124, @constraintname_124
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWFACTIVITYCONTEXTPARAMS
END
;

CREATE TABLE TWFACTIVITYCONTEXTPARAMS
(
    OBJECTID INT NOT NULL,
    WORKFLOWACTIVITY INT NOT NULL,
    ISSUETYPE INT NULL,
    PROJECTTYPE INT NULL,
    PROJECT INT NULL,
    SETTERORPARTID INT NULL,
    ACTIVITYPARAMS VARCHAR (7000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWFACTIVITYCONTEXTPARAMS_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWFACTIVITYCONTEXTPARAMSINDEX1 ON TWFACTIVITYCONTEXTPARAMS (WORKFLOWACTIVITY);
CREATE  INDEX TWFACTIVITYCONTEXTPARAMSINDEX2 ON TWFACTIVITYCONTEXTPARAMS (ISSUETYPE);
CREATE  INDEX TWFACTIVITYCONTEXTPARAMSINDEX3 ON TWFACTIVITYCONTEXTPARAMS (PROJECTTYPE);
CREATE  INDEX TWFACTIVITYCONTEXTPARAMSINDEX4 ON TWFACTIVITYCONTEXTPARAMS (PROJECT);
CREATE  INDEX TWFACTIVITYCONTEXTPARAMSINDEX5 ON TWFACTIVITYCONTEXTPARAMS (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKFLOWGUARD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWGUARD_FK_1')
    ALTER TABLE TWORKFLOWGUARD DROP CONSTRAINT TWORKFLOWGUARD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWGUARD_FK_2')
    ALTER TABLE TWORKFLOWGUARD DROP CONSTRAINT TWORKFLOWGUARD_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWGUARD_FK_3')
    ALTER TABLE TWORKFLOWGUARD DROP CONSTRAINT TWORKFLOWGUARD_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWGUARD_FK_4')
    ALTER TABLE TWORKFLOWGUARD DROP CONSTRAINT TWORKFLOWGUARD_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKFLOWGUARD')
BEGIN
     DECLARE @reftable_125 nvarchar(60), @constraintname_125 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKFLOWGUARD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_125, @constraintname_125
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_125+' drop constraint '+@constraintname_125)
       FETCH NEXT from refcursor into @reftable_125, @constraintname_125
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKFLOWGUARD
END
;

CREATE TABLE TWORKFLOWGUARD
(
    OBJECTID INT NOT NULL,
    GUARDTYPE INT NOT NULL,
    GUARDPARAMS VARCHAR (7000) NULL,
    WORKFLOWTRANSITION INT NOT NULL,
    ROLEKEY INT NULL,
    GROOVYSCRIPT INT NULL,
    PERSON INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKFLOWGUARD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKFLOWGUARDINDEX1 ON TWORKFLOWGUARD (WORKFLOWTRANSITION);
CREATE  INDEX TWORKFLOWGUARDINDEX2 ON TWORKFLOWGUARD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKFLOWCONNECT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWCONNECT_FK_1')
    ALTER TABLE TWORKFLOWCONNECT DROP CONSTRAINT TWORKFLOWCONNECT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWCONNECT_FK_2')
    ALTER TABLE TWORKFLOWCONNECT DROP CONSTRAINT TWORKFLOWCONNECT_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWCONNECT_FK_3')
    ALTER TABLE TWORKFLOWCONNECT DROP CONSTRAINT TWORKFLOWCONNECT_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWCONNECT_FK_4')
    ALTER TABLE TWORKFLOWCONNECT DROP CONSTRAINT TWORKFLOWCONNECT_FK_4;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKFLOWCONNECT')
BEGIN
     DECLARE @reftable_126 nvarchar(60), @constraintname_126 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKFLOWCONNECT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_126, @constraintname_126
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_126+' drop constraint '+@constraintname_126)
       FETCH NEXT from refcursor into @reftable_126, @constraintname_126
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKFLOWCONNECT
END
;

CREATE TABLE TWORKFLOWCONNECT
(
    OBJECTID INT NOT NULL,
    WORKFLOW INT NOT NULL,
    ISSUETYPE INT NULL,
    PROJECTTYPE INT NULL,
    PROJECT INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKFLOWCONNECT_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKFLOWCONNECTINDEX1 ON TWORKFLOWCONNECT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TWORKFLOWCOMMENT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKFLOWCOMMENT_FK_1')
    ALTER TABLE TWORKFLOWCOMMENT DROP CONSTRAINT TWORKFLOWCOMMENT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKFLOWCOMMENT')
BEGIN
     DECLARE @reftable_127 nvarchar(60), @constraintname_127 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TWORKFLOWCOMMENT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_127, @constraintname_127
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_127+' drop constraint '+@constraintname_127)
       FETCH NEXT from refcursor into @reftable_127, @constraintname_127
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKFLOWCOMMENT
END
;

CREATE TABLE TWORKFLOWCOMMENT
(
    OBJECTID INT NOT NULL,
    DESCRIPTION VARCHAR (7000) NULL,
    WORKFLOW INT NULL,
    NODEX INT NULL,
    NODEY INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TWORKFLOWCOMMENT_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TWORKFLOWCOMMENTINDEX1 ON TWORKFLOWCOMMENT (WORKFLOW);
CREATE  INDEX TWORKFLOWCOMMENTINDEX2 ON TWORKFLOWCOMMENT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSLA                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSLA')
BEGIN
     DECLARE @reftable_128 nvarchar(60), @constraintname_128 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSLA'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_128, @constraintname_128
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_128+' drop constraint '+@constraintname_128)
       FETCH NEXT from refcursor into @reftable_128, @constraintname_128
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSLA
END
;

CREATE TABLE TSLA
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSLA_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSLAINDEX1 ON TSLA (TPUUID);




/* ---------------------------------------------------------------------- */
/* TESCALATIONENTRY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TESCALATIONENTRY_FK_1')
    ALTER TABLE TESCALATIONENTRY DROP CONSTRAINT TESCALATIONENTRY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TESCALATIONENTRY_FK_2')
    ALTER TABLE TESCALATIONENTRY DROP CONSTRAINT TESCALATIONENTRY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TESCALATIONENTRY_FK_3')
    ALTER TABLE TESCALATIONENTRY DROP CONSTRAINT TESCALATIONENTRY_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TESCALATIONENTRY')
BEGIN
     DECLARE @reftable_129 nvarchar(60), @constraintname_129 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TESCALATIONENTRY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_129, @constraintname_129
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_129+' drop constraint '+@constraintname_129)
       FETCH NEXT from refcursor into @reftable_129, @constraintname_129
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TESCALATIONENTRY
END
;

CREATE TABLE TESCALATIONENTRY
(
    OBJECTID INT NOT NULL,
    SLA INT NOT NULL,
    PRIORITY INT NULL,
    ESCALATETO INT NULL,
    SPARAMETERS VARCHAR (7000) NULL,
    NLEVEL INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TESCALATIONENTRY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TESCALATIONENTRYINDEX1 ON TESCALATIONENTRY (SLA);
CREATE  INDEX TESCALATIONENTRYINDEX2 ON TESCALATIONENTRY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TESCALATIONSTATE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TESCALATIONSTATE_FK_1')
    ALTER TABLE TESCALATIONSTATE DROP CONSTRAINT TESCALATIONSTATE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TESCALATIONSTATE_FK_2')
    ALTER TABLE TESCALATIONSTATE DROP CONSTRAINT TESCALATIONSTATE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TESCALATIONSTATE')
BEGIN
     DECLARE @reftable_130 nvarchar(60), @constraintname_130 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TESCALATIONSTATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_130, @constraintname_130
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_130+' drop constraint '+@constraintname_130)
       FETCH NEXT from refcursor into @reftable_130, @constraintname_130
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TESCALATIONSTATE
END
;

CREATE TABLE TESCALATIONSTATE
(
    OBJECTID INT NOT NULL,
    ESCALATIONENTRY INT NOT NULL,
    STATUS INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TESCALATIONSTATE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TESCALATIONSTATEINDEX1 ON TESCALATIONSTATE (ESCALATIONENTRY);
CREATE  INDEX TESCALATIONSTATEINDEX2 ON TESCALATIONSTATE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TORGPROJECTSLA                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TORGPROJECTSLA_FK_1')
    ALTER TABLE TORGPROJECTSLA DROP CONSTRAINT TORGPROJECTSLA_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TORGPROJECTSLA_FK_2')
    ALTER TABLE TORGPROJECTSLA DROP CONSTRAINT TORGPROJECTSLA_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TORGPROJECTSLA_FK_3')
    ALTER TABLE TORGPROJECTSLA DROP CONSTRAINT TORGPROJECTSLA_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TORGPROJECTSLA')
BEGIN
     DECLARE @reftable_131 nvarchar(60), @constraintname_131 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TORGPROJECTSLA'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_131, @constraintname_131
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_131+' drop constraint '+@constraintname_131)
       FETCH NEXT from refcursor into @reftable_131, @constraintname_131
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TORGPROJECTSLA
END
;

CREATE TABLE TORGPROJECTSLA
(
    OBJECTID INT NOT NULL,
    DEPARTMENT INT NOT NULL,
    PROJECT INT NOT NULL,
    SLA INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TORGPROJECTSLA_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TORGPROJECTSLAINDEX1 ON TORGPROJECTSLA (TPUUID);




/* ---------------------------------------------------------------------- */
/* TREADISSUE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREADISSUE_FK_1')
    ALTER TABLE TREADISSUE DROP CONSTRAINT TREADISSUE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREADISSUE_FK_2')
    ALTER TABLE TREADISSUE DROP CONSTRAINT TREADISSUE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TREADISSUE')
BEGIN
     DECLARE @reftable_132 nvarchar(60), @constraintname_132 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TREADISSUE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_132, @constraintname_132
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_132+' drop constraint '+@constraintname_132)
       FETCH NEXT from refcursor into @reftable_132, @constraintname_132
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TREADISSUE
END
;

CREATE TABLE TREADISSUE
(
    OBJECTID INT NOT NULL,
    WORKITEM INT NOT NULL,
    FIELDKEY INT NULL,
    PERSON INT NOT NULL,
    READTYPE INT NULL,
    LASTEDIT DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREADISSUE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREADISSUEINDEX1 ON TREADISSUE (PERSON);
CREATE  INDEX TREADISSUEINDEX2 ON TREADISSUE (WORKITEM);
CREATE  INDEX TREADISSUEINDEX3 ON TREADISSUE (TPUUID);
CREATE  INDEX TREADISSUEINDEX4 ON TREADISSUE (FIELDKEY);




/* ---------------------------------------------------------------------- */
/* TLASTEXECUTEDQUERY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLASTEXECUTEDQUERY_FK_1')
    ALTER TABLE TLASTEXECUTEDQUERY DROP CONSTRAINT TLASTEXECUTEDQUERY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLASTEXECUTEDQUERY_FK_2')
    ALTER TABLE TLASTEXECUTEDQUERY DROP CONSTRAINT TLASTEXECUTEDQUERY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLASTEXECUTEDQUERY')
BEGIN
     DECLARE @reftable_133 nvarchar(60), @constraintname_133 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TLASTEXECUTEDQUERY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_133, @constraintname_133
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_133+' drop constraint '+@constraintname_133)
       FETCH NEXT from refcursor into @reftable_133, @constraintname_133
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TLASTEXECUTEDQUERY
END
;

CREATE TABLE TLASTEXECUTEDQUERY
(
    OBJECTID INT NOT NULL,
    PERSON INT NOT NULL,
    QUERYTYPE INT NOT NULL,
    QUERYCLOB INT NULL,
    QUERYKEY INT NULL,
    LASTEXECUTEDTIME DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TLASTEXECUTEDQUERY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TLASTEXECUTEDQUERYINDEX1 ON TLASTEXECUTEDQUERY (PERSON);
CREATE  INDEX TLASTEXECUTEDQUERYINDEX2 ON TLASTEXECUTEDQUERY (TPUUID);




/* ---------------------------------------------------------------------- */
/* TGLOBALCSSSTYLE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TGLOBALCSSSTYLE')
BEGIN
     DECLARE @reftable_134 nvarchar(60), @constraintname_134 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TGLOBALCSSSTYLE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_134, @constraintname_134
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_134+' drop constraint '+@constraintname_134)
       FETCH NEXT from refcursor into @reftable_134, @constraintname_134
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TGLOBALCSSSTYLE
END
;

CREATE TABLE TGLOBALCSSSTYLE
(
    OBJECTID INT NOT NULL,
    STYLEFOR INT NOT NULL,
    CSSSTYLE VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TGLOBALCSSSTYLE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TGLOBALCSSSTYLEINDEX1 ON TGLOBALCSSSTYLE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TREPORTSUBSCRIBE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTSUBSCRIBE_FK_1')
    ALTER TABLE TREPORTSUBSCRIBE DROP CONSTRAINT TREPORTSUBSCRIBE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTSUBSCRIBE_FK_2')
    ALTER TABLE TREPORTSUBSCRIBE DROP CONSTRAINT TREPORTSUBSCRIBE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TREPORTSUBSCRIBE_FK_3')
    ALTER TABLE TREPORTSUBSCRIBE DROP CONSTRAINT TREPORTSUBSCRIBE_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TREPORTSUBSCRIBE')
BEGIN
     DECLARE @reftable_135 nvarchar(60), @constraintname_135 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TREPORTSUBSCRIBE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_135, @constraintname_135
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_135+' drop constraint '+@constraintname_135)
       FETCH NEXT from refcursor into @reftable_135, @constraintname_135
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TREPORTSUBSCRIBE
END
;

CREATE TABLE TREPORTSUBSCRIBE
(
    OBJECTID INT NOT NULL,
    PERSON INT NOT NULL,
    RECURRENCEPATTERN INT NULL,
    REPORTTEMPLATE INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREPORTSUBSCRIBE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREPORTSUBSCRIBEINDEX1 ON TREPORTSUBSCRIBE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TGRIDLAYOUT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TGRIDLAYOUT_FK_1')
    ALTER TABLE TGRIDLAYOUT DROP CONSTRAINT TGRIDLAYOUT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TGRIDLAYOUT')
BEGIN
     DECLARE @reftable_136 nvarchar(60), @constraintname_136 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TGRIDLAYOUT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_136, @constraintname_136
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_136+' drop constraint '+@constraintname_136)
       FETCH NEXT from refcursor into @reftable_136, @constraintname_136
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TGRIDLAYOUT
END
;

CREATE TABLE TGRIDLAYOUT
(
    OBJECTID INT NOT NULL,
    PERSON INT NULL,
    LAYOUTKEY INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TGRIDLAYOUT_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TGRIDLAYOUTINDEX1 ON TGRIDLAYOUT (PERSON);
CREATE  INDEX TGRIDLAYOUTINDEX2 ON TGRIDLAYOUT (LAYOUTKEY);
CREATE  INDEX TGRIDLAYOUTINDEX3 ON TGRIDLAYOUT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TGRIDFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TGRIDFIELD_FK_1')
    ALTER TABLE TGRIDFIELD DROP CONSTRAINT TGRIDFIELD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TGRIDFIELD')
BEGIN
     DECLARE @reftable_137 nvarchar(60), @constraintname_137 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TGRIDFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_137, @constraintname_137
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_137+' drop constraint '+@constraintname_137)
       FETCH NEXT from refcursor into @reftable_137, @constraintname_137
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TGRIDFIELD
END
;

CREATE TABLE TGRIDFIELD
(
    OBJECTID INT NOT NULL,
    GRIDLAYOUT INT NOT NULL,
    GRIDFIELD INT NOT NULL,
    FIELDPOSITION INT NOT NULL,
    FIELDWIDTH INT NOT NULL,
    FILTERSTRING VARCHAR (255) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TGRIDFIELD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TGRIDFIELDINDEX1 ON TGRIDFIELD (GRIDLAYOUT);
CREATE  INDEX TGRIDFIELDINDEX2 ON TGRIDFIELD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TGRIDGROUPINGSORTING                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TGRIDGROUPINGSORTING_FK_1')
    ALTER TABLE TGRIDGROUPINGSORTING DROP CONSTRAINT TGRIDGROUPINGSORTING_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TGRIDGROUPINGSORTING')
BEGIN
     DECLARE @reftable_138 nvarchar(60), @constraintname_138 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TGRIDGROUPINGSORTING'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_138, @constraintname_138
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_138+' drop constraint '+@constraintname_138)
       FETCH NEXT from refcursor into @reftable_138, @constraintname_138
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TGRIDGROUPINGSORTING
END
;

CREATE TABLE TGRIDGROUPINGSORTING
(
    OBJECTID INT NOT NULL,
    GRIDLAYOUT INT NOT NULL,
    GRIDFIELD INT NOT NULL,
    SORTPOSITION INT NOT NULL,
    ISGROUPING CHAR (1) default 'N' NULL,
    ISDESCENDING CHAR (1) default 'N' NULL,
    ISCOLLAPSED CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TGRIDGROUPINGSORTING_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TGRIDGROUPINGSORTINGINDEX1 ON TGRIDGROUPINGSORTING (GRIDLAYOUT);
CREATE  INDEX TGRIDGROUPINGSORTINGINDEX2 ON TGRIDGROUPINGSORTING (TPUUID);




/* ---------------------------------------------------------------------- */
/* TNAVIGATORLAYOUT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNAVIGATORLAYOUT_FK_1')
    ALTER TABLE TNAVIGATORLAYOUT DROP CONSTRAINT TNAVIGATORLAYOUT_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TNAVIGATORLAYOUT')
BEGIN
     DECLARE @reftable_139 nvarchar(60), @constraintname_139 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TNAVIGATORLAYOUT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_139, @constraintname_139
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_139+' drop constraint '+@constraintname_139)
       FETCH NEXT from refcursor into @reftable_139, @constraintname_139
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TNAVIGATORLAYOUT
END
;

CREATE TABLE TNAVIGATORLAYOUT
(
    OBJECTID INT NOT NULL,
    PERSON INT NULL,
    FILTERID INT NULL,
    FILTERTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TNAVIGATORLAYOUT_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TNAVIGATORLAYOUTINDEX1 ON TNAVIGATORLAYOUT (PERSON);
CREATE  INDEX TNAVIGATORLAYOUTINDEX2 ON TNAVIGATORLAYOUT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TNAVIGATORCOLUMN                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNAVIGATORCOLUMN_FK_1')
    ALTER TABLE TNAVIGATORCOLUMN DROP CONSTRAINT TNAVIGATORCOLUMN_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TNAVIGATORCOLUMN')
BEGIN
     DECLARE @reftable_140 nvarchar(60), @constraintname_140 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TNAVIGATORCOLUMN'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_140, @constraintname_140
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_140+' drop constraint '+@constraintname_140)
       FETCH NEXT from refcursor into @reftable_140, @constraintname_140
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TNAVIGATORCOLUMN
END
;

CREATE TABLE TNAVIGATORCOLUMN
(
    OBJECTID INT NOT NULL,
    NAVIGATORLAYOUT INT NOT NULL,
    FIELD INT NOT NULL,
    FIELDPOSITION INT NOT NULL,
    FIELDWIDTH INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TNAVIGATORCOLUMN_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TNAVIGATORCOLUMNINDEX1 ON TNAVIGATORCOLUMN (NAVIGATORLAYOUT);
CREATE  INDEX TNAVIGATORCOLUMNINDEX2 ON TNAVIGATORCOLUMN (TPUUID);




/* ---------------------------------------------------------------------- */
/* TNAVIGATORGROUPINGSORTING                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TNAVIGATORGROUPINGSORTING_FK_1')
    ALTER TABLE TNAVIGATORGROUPINGSORTING DROP CONSTRAINT TNAVIGATORGROUPINGSORTING_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TNAVIGATORGROUPINGSORTING')
BEGIN
     DECLARE @reftable_141 nvarchar(60), @constraintname_141 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TNAVIGATORGROUPINGSORTING'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_141, @constraintname_141
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_141+' drop constraint '+@constraintname_141)
       FETCH NEXT from refcursor into @reftable_141, @constraintname_141
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TNAVIGATORGROUPINGSORTING
END
;

CREATE TABLE TNAVIGATORGROUPINGSORTING
(
    OBJECTID INT NOT NULL,
    NAVIGATORLAYOUT INT NOT NULL,
    FIELD INT NOT NULL,
    SORTPOSITION INT NULL,
    ISGROUPING CHAR (1) default 'N' NULL,
    ISDESCENDING CHAR (1) default 'N' NULL,
    ISCOLLAPSED CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TNAVIGATORGROUPINGSORTING_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TNAVIGATORGROUPINGSORTINGIDX1 ON TNAVIGATORGROUPINGSORTING (NAVIGATORLAYOUT);
CREATE  INDEX TNAVIGATORGROUPINGSORTINGIDX2 ON TNAVIGATORGROUPINGSORTING (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCARDGROUPINGFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCARDGROUPINGFIELD_FK_1')
    ALTER TABLE TCARDGROUPINGFIELD DROP CONSTRAINT TCARDGROUPINGFIELD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCARDGROUPINGFIELD')
BEGIN
     DECLARE @reftable_142 nvarchar(60), @constraintname_142 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCARDGROUPINGFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_142, @constraintname_142
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_142+' drop constraint '+@constraintname_142)
       FETCH NEXT from refcursor into @reftable_142, @constraintname_142
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCARDGROUPINGFIELD
END
;

CREATE TABLE TCARDGROUPINGFIELD
(
    OBJECTID INT NOT NULL,
    NAVIGATORLAYOUT INT NOT NULL,
    CARDFIELD INT NOT NULL,
    ISACTIV CHAR (1) default 'N' NULL,
    SORTFIELD INT NULL,
    ISDESCENDING CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCARDGROUPINGFIELD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TCARDGROUPINGFIELDINDEX1 ON TCARDGROUPINGFIELD (NAVIGATORLAYOUT);
CREATE  INDEX TCARDGROUPINGFIELDINDEX2 ON TCARDGROUPINGFIELD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCARDFIELDOPTION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCARDFIELDOPTION_FK_1')
    ALTER TABLE TCARDFIELDOPTION DROP CONSTRAINT TCARDFIELDOPTION_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCARDFIELDOPTION')
BEGIN
     DECLARE @reftable_143 nvarchar(60), @constraintname_143 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCARDFIELDOPTION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_143, @constraintname_143
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_143+' drop constraint '+@constraintname_143)
       FETCH NEXT from refcursor into @reftable_143, @constraintname_143
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCARDFIELDOPTION
END
;

CREATE TABLE TCARDFIELDOPTION
(
    OBJECTID INT NOT NULL,
    GROUPINGFIELD INT NOT NULL,
    OPTIONID INT NOT NULL,
    OPTIONPOSITION INT NOT NULL,
    OPTIONWIDTH INT NOT NULL,
    MAXNUMBER INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCARDFIELDOPTION_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TCARDFIELDOPTIONINDEX1 ON TCARDFIELDOPTION (GROUPINGFIELD);
CREATE  INDEX TCARDFIELDOPTIONINDEX2 ON TCARDFIELDOPTION (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCARDPANEL                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCARDPANEL')
BEGIN
     DECLARE @reftable_144 nvarchar(60), @constraintname_144 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCARDPANEL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_144, @constraintname_144
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_144+' drop constraint '+@constraintname_144)
       FETCH NEXT from refcursor into @reftable_144, @constraintname_144
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCARDPANEL
END
;

CREATE TABLE TCARDPANEL
(
    OBJECTID INT NOT NULL,
    PERSON INT NULL,
    ROWSNO INT NOT NULL,
    COLSNO INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCARDPANEL_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TCARDPANELINDEX1 ON TCARDPANEL (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCARDFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCARDFIELD_FK_1')
    ALTER TABLE TCARDFIELD DROP CONSTRAINT TCARDFIELD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCARDFIELD')
BEGIN
     DECLARE @reftable_145 nvarchar(60), @constraintname_145 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCARDFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_145, @constraintname_145
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_145+' drop constraint '+@constraintname_145)
       FETCH NEXT from refcursor into @reftable_145, @constraintname_145
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCARDFIELD
END
;

CREATE TABLE TCARDFIELD
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NULL,
    COLINDEX INT NULL,
    ROWINDEX INT NULL,
    COLSPAN INT NULL,
    ROWSPAN INT NULL,
    LABELHALIGN INT NOT NULL,
    LABELVALIGN INT NOT NULL,
    VALUEHALIGN INT NOT NULL,
    VALUEVALIGN INT NOT NULL,
    CARDPANEL INT NOT NULL,
    FIELDKEY INT NULL,
    HIDELABEL CHAR (1) default 'Y' NULL,
    ICONRENDERING INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCARDFIELD_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TCARDFIELDINDEX1 ON TCARDFIELD (TPUUID);




/* ---------------------------------------------------------------------- */
/* TVIEWPARAM                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TVIEWPARAM_FK_1')
    ALTER TABLE TVIEWPARAM DROP CONSTRAINT TVIEWPARAM_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TVIEWPARAM')
BEGIN
     DECLARE @reftable_146 nvarchar(60), @constraintname_146 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TVIEWPARAM'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_146, @constraintname_146
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_146+' drop constraint '+@constraintname_146)
       FETCH NEXT from refcursor into @reftable_146, @constraintname_146
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TVIEWPARAM
END
;

CREATE TABLE TVIEWPARAM
(
    OBJECTID INT NOT NULL,
    NAVIGATORLAYOUT INT NOT NULL,
    PARAMNAME VARCHAR (255) NOT NULL,
    PARAMVALUE VARCHAR (3000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TVIEWPARAM_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TVIEWPARAMINDEX1 ON TVIEWPARAM (NAVIGATORLAYOUT);
CREATE  INDEX TVIEWPARAMINDEX2 ON TVIEWPARAM (TPUUID);




/* ---------------------------------------------------------------------- */
/* TGENERALPARAM                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TGENERALPARAM')
BEGIN
     DECLARE @reftable_147 nvarchar(60), @constraintname_147 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TGENERALPARAM'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_147, @constraintname_147
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_147+' drop constraint '+@constraintname_147)
       FETCH NEXT from refcursor into @reftable_147, @constraintname_147
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TGENERALPARAM
END
;

CREATE TABLE TGENERALPARAM
(
    OBJECTID INT NOT NULL,
    PARAMNAME VARCHAR (255) NOT NULL,
    PARAMVALUE VARCHAR (3000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TGENERALPARAM_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TGENERALPARAMINDEX1 ON TGENERALPARAM (TPUUID);




/* ---------------------------------------------------------------------- */
/* TVERSIONCONTROL                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TVERSIONCONTROL_FK_1')
    ALTER TABLE TVERSIONCONTROL DROP CONSTRAINT TVERSIONCONTROL_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TVERSIONCONTROL')
BEGIN
     DECLARE @reftable_148 nvarchar(60), @constraintname_148 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TVERSIONCONTROL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_148, @constraintname_148
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_148+' drop constraint '+@constraintname_148)
       FETCH NEXT from refcursor into @reftable_148, @constraintname_148
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TVERSIONCONTROL
END
;

CREATE TABLE TVERSIONCONTROL
(
    OBJECTID INT NOT NULL,
    VCTYPE INT NOT NULL,
    REPOSITORYBROWSER INT NOT NULL,
    CHANGESETLINK VARCHAR (255) NULL,
    ADDEDFILESLINK VARCHAR (255) NULL,
    MODIFIEDEDFILESLINK VARCHAR (255) NULL,
    REPLACEDFILESLINK VARCHAR (255) NULL,
    DELETEDFILESLINK VARCHAR (255) NULL,
    CONNECTIONTYPE INT NOT NULL,
    SERVERNAME VARCHAR (255) NOT NULL,
    REPOSITORYPATH VARCHAR (255) NOT NULL,
    DEFAULTSEVERPORT CHAR (1) default 'Y' NULL,
    SEVERPORT INT NULL,
    AUTHENTICATIONMODE INT NOT NULL,
    USERNAME VARCHAR (100) NULL,
    PASSWORD VARCHAR (255) NULL,
    PRIVATEKEY VARCHAR (7000) NULL,
    PASSPHRASE VARCHAR (255) NULL,
    PARENT INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TVERSIONCONTROL_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TVERSIONCONTROLINDEX1 ON TVERSIONCONTROL (TPUUID);




/* ---------------------------------------------------------------------- */
/* TSHORTCUT                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSHORTCUT')
BEGIN
     DECLARE @reftable_149 nvarchar(60), @constraintname_149 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TSHORTCUT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_149, @constraintname_149
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_149+' drop constraint '+@constraintname_149)
       FETCH NEXT from refcursor into @reftable_149, @constraintname_149
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TSHORTCUT
END
;

CREATE TABLE TSHORTCUT
(
    OBJECTID INT NOT NULL,
    SHORTCUTKEY INT NULL,
    CTRLKEY CHAR (1) default 'N' NULL,
    SHIFTKEY CHAR (1) default 'N' NULL,
    ALTKEY CHAR (1) default 'N' NULL,
    MENUITEMKEY INT NULL,
    MENUCONTEXT INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TSHORTCUT_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TSHORTCUTSINDEX1 ON TSHORTCUT (TPUUID);




/* ---------------------------------------------------------------------- */
/* TMAILTEXTBLOCK                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMAILTEXTBLOCK_FK_1')
    ALTER TABLE TMAILTEXTBLOCK DROP CONSTRAINT TMAILTEXTBLOCK_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TMAILTEXTBLOCK_FK_2')
    ALTER TABLE TMAILTEXTBLOCK DROP CONSTRAINT TMAILTEXTBLOCK_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TMAILTEXTBLOCK')
BEGIN
     DECLARE @reftable_150 nvarchar(60), @constraintname_150 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TMAILTEXTBLOCK'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_150, @constraintname_150
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_150+' drop constraint '+@constraintname_150)
       FETCH NEXT from refcursor into @reftable_150, @constraintname_150
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TMAILTEXTBLOCK
END
;

CREATE TABLE TMAILTEXTBLOCK
(
    OBJECTID INT NOT NULL,
    PERSON INT NULL,
    BLOCKTITLE VARCHAR (255) NULL,
    BLOCKCONTENT VARCHAR (7000) NULL,
    TAGLABEL VARCHAR (100) NULL,
    REPOSITORYTYPE INT NULL,
    PROJECT INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TMAILTEXTBLOCK_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TMAILTEXTBLOCKINDEX1 ON TMAILTEXTBLOCK (PERSON);
CREATE  INDEX TMAILTEXTBLOCKINDEX2 ON TMAILTEXTBLOCK (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPROLE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROLE_FK_1')
    ALTER TABLE TPROLE DROP CONSTRAINT TPROLE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPROLE_FK_2')
    ALTER TABLE TPROLE DROP CONSTRAINT TPROLE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPROLE')
BEGIN
     DECLARE @reftable_151 nvarchar(60), @constraintname_151 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPROLE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_151, @constraintname_151
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_151+' drop constraint '+@constraintname_151)
       FETCH NEXT from refcursor into @reftable_151, @constraintname_151
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPROLE
END
;

CREATE TABLE TPROLE
(
    OBJECTID INT NOT NULL,
    PROJECTTYPE INT NULL,
    ROLEKEY INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPROLE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TPROLEINDEX1 ON TPROLE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TCHILDPROJECTTYPE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCHILDPROJECTTYPE_FK_1')
    ALTER TABLE TCHILDPROJECTTYPE DROP CONSTRAINT TCHILDPROJECTTYPE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCHILDPROJECTTYPE_FK_2')
    ALTER TABLE TCHILDPROJECTTYPE DROP CONSTRAINT TCHILDPROJECTTYPE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCHILDPROJECTTYPE')
BEGIN
     DECLARE @reftable_152 nvarchar(60), @constraintname_152 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCHILDPROJECTTYPE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_152, @constraintname_152
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_152+' drop constraint '+@constraintname_152)
       FETCH NEXT from refcursor into @reftable_152, @constraintname_152
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCHILDPROJECTTYPE
END
;

CREATE TABLE TCHILDPROJECTTYPE
(
    OBJECTID INT NOT NULL,
    PROJECTTYPEPARENT INT NOT NULL,
    PROJECTTYPECHILD INT NOT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TCHILDPROJECTTYPE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TCHILDPROJECTTYPEINDEX1 ON TCHILDPROJECTTYPE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TDOMAIN                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TDOMAIN')
BEGIN
     DECLARE @reftable_153 nvarchar(60), @constraintname_153 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TDOMAIN'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_153, @constraintname_153
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_153+' drop constraint '+@constraintname_153)
       FETCH NEXT from refcursor into @reftable_153, @constraintname_153
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TDOMAIN
END
;

CREATE TABLE TDOMAIN
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NULL,
    DESCRIPTION VARCHAR (7000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TDOMAIN_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TDOMAININDEX1 ON TDOMAIN (TPUUID);




/* ---------------------------------------------------------------------- */
/* TPERSONINDOMAIN                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSONINDOMAIN_FK_1')
    ALTER TABLE TPERSONINDOMAIN DROP CONSTRAINT TPERSONINDOMAIN_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSONINDOMAIN_FK_2')
    ALTER TABLE TPERSONINDOMAIN DROP CONSTRAINT TPERSONINDOMAIN_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPERSONINDOMAIN')
BEGIN
     DECLARE @reftable_154 nvarchar(60), @constraintname_154 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TPERSONINDOMAIN'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_154, @constraintname_154
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_154+' drop constraint '+@constraintname_154)
       FETCH NEXT from refcursor into @reftable_154, @constraintname_154
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TPERSONINDOMAIN
END
;

CREATE TABLE TPERSONINDOMAIN
(
    OBJECTID INT NOT NULL,
    PERSONKEY INT NOT NULL,
    DOMAINKEY INT NOT NULL,
    DESCRIPTION VARCHAR (7000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TPERSONINDOMAIN_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TPERSONINDOMAININDEX1 ON TPERSONINDOMAIN (TPUUID);




/* ---------------------------------------------------------------------- */
/* TATTACHMENTVERSION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTACHMENTVERSION_FK_1')
    ALTER TABLE TATTACHMENTVERSION DROP CONSTRAINT TATTACHMENTVERSION_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTACHMENTVERSION_FK_2')
    ALTER TABLE TATTACHMENTVERSION DROP CONSTRAINT TATTACHMENTVERSION_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TATTACHMENTVERSION_FK_3')
    ALTER TABLE TATTACHMENTVERSION DROP CONSTRAINT TATTACHMENTVERSION_FK_3;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TATTACHMENTVERSION')
BEGIN
     DECLARE @reftable_155 nvarchar(60), @constraintname_155 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TATTACHMENTVERSION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_155, @constraintname_155
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_155+' drop constraint '+@constraintname_155)
       FETCH NEXT from refcursor into @reftable_155, @constraintname_155
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TATTACHMENTVERSION
END
;

CREATE TABLE TATTACHMENTVERSION
(
    OBJECTID INT NOT NULL,
    WORKITEM INT NOT NULL,
    CHANGEDBY INT NULL,
    DOCUMENTSTATE INT NULL,
    FILENAME VARCHAR (256) NOT NULL,
    ISURL CHAR (1) default 'N' NULL,
    FILESIZE VARCHAR (20) NULL,
    MIMETYPE VARCHAR (15) NULL,
    LASTEDIT DATETIME NULL,
    VERSION VARCHAR (20) NULL,
    DESCRIPTION VARCHAR (512) NULL,
    CRYPTKEY VARCHAR (4000) NULL,
    ISENCRYPTED CHAR (1) default 'N' NULL,
    ISDELETED CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TATTACHMENTVERSION_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TATTACHMENTVERSIONINDEX1 ON TATTACHMENTVERSION (TPUUID);




/* ---------------------------------------------------------------------- */
/* TUSERLEVEL                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TUSERLEVEL')
BEGIN
     DECLARE @reftable_156 nvarchar(60), @constraintname_156 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TUSERLEVEL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_156, @constraintname_156
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_156+' drop constraint '+@constraintname_156)
       FETCH NEXT from refcursor into @reftable_156, @constraintname_156
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TUSERLEVEL
END
;

CREATE TABLE TUSERLEVEL
(
    OBJECTID INT NOT NULL,
    LABEL VARCHAR (255) NULL,
    DESCRIPTION VARCHAR (7000) NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TUSERLEVEL_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TUSERLEVELINDEX1 ON TUSERLEVEL (TPUUID);




/* ---------------------------------------------------------------------- */
/* TUSERLEVELSETTING                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TUSERLEVELSETTING_FK_1')
    ALTER TABLE TUSERLEVELSETTING DROP CONSTRAINT TUSERLEVELSETTING_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TUSERLEVELSETTING')
BEGIN
     DECLARE @reftable_157 nvarchar(60), @constraintname_157 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TUSERLEVELSETTING'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_157, @constraintname_157
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_157+' drop constraint '+@constraintname_157)
       FETCH NEXT from refcursor into @reftable_157, @constraintname_157
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TUSERLEVELSETTING
END
;

CREATE TABLE TUSERLEVELSETTING
(
    OBJECTID INT NOT NULL,
    USERLEVEL INT NOT NULL,
    ACTIONKEY INT NOT NULL,
    ISACTIVE CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TUSERLEVELSETTING_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TUSERLEVELSETTINGINDEX1 ON TUSERLEVELSETTING (TPUUID);




/* ---------------------------------------------------------------------- */
/* TUSERFEATURE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TUSERFEATURE_FK_1')
    ALTER TABLE TUSERFEATURE DROP CONSTRAINT TUSERFEATURE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TUSERFEATURE')
BEGIN
     DECLARE @reftable_158 nvarchar(60), @constraintname_158 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TUSERFEATURE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_158, @constraintname_158
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_158+' drop constraint '+@constraintname_158)
       FETCH NEXT from refcursor into @reftable_158, @constraintname_158
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TUSERFEATURE
END
;

CREATE TABLE TUSERFEATURE
(
    OBJECTID INT NOT NULL,
    FEATURENAME VARCHAR (255) NOT NULL,
    PERSON INT NULL,
    ISACTIVE CHAR (1) default 'N' NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TUSERFEATURE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TUSERFEATUREINDEX1 ON TUSERFEATURE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TITEMTRANSITION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TITEMTRANSITION_FK_1')
    ALTER TABLE TITEMTRANSITION DROP CONSTRAINT TITEMTRANSITION_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TITEMTRANSITION_FK_2')
    ALTER TABLE TITEMTRANSITION DROP CONSTRAINT TITEMTRANSITION_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TITEMTRANSITION')
BEGIN
     DECLARE @reftable_159 nvarchar(60), @constraintname_159 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TITEMTRANSITION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_159, @constraintname_159
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_159+' drop constraint '+@constraintname_159)
       FETCH NEXT from refcursor into @reftable_159, @constraintname_159
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TITEMTRANSITION
END
;

CREATE TABLE TITEMTRANSITION
(
    OBJECTID INT NOT NULL,
    WORKITEM INT NOT NULL,
    TRANSITION INT NOT NULL,
    TRANSITIONTIME DATETIME NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TITEMTRANSITION_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TITEMTRANSITIONINDEX1 ON TITEMTRANSITION (WORKITEM);
CREATE  INDEX TITEMTRANSITIONINDEX2 ON TITEMTRANSITION (TRANSITION);
CREATE  INDEX TITEMTRANSITIONINDEX3 ON TITEMTRANSITION (TPUUID);




/* ---------------------------------------------------------------------- */
/* TITEMTRANSITION                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TACL
    ADD CONSTRAINT TACL_FK_1 FOREIGN KEY (PERSONKEY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TACL
    ADD CONSTRAINT TACL_FK_2 FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
END
;

BEGIN
ALTER TABLE TACL
    ADD CONSTRAINT TACL_FK_3 FOREIGN KEY (PROJKEY)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TACL                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TBASELINE
    ADD CONSTRAINT TBASELINE_FK_1 FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TBASELINE
    ADD CONSTRAINT TBASELINE_FK_2 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TBASELINE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TCATEGORY
    ADD CONSTRAINT TCATEGORY_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TCATEGORY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TCLASS
    ADD CONSTRAINT TCLASS_FK_1 FOREIGN KEY (PROJKEY)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TCLASS                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TDEPARTMENT
    ADD CONSTRAINT TDEPARTMENT_FK_1 FOREIGN KEY (COSTCENTER)
    REFERENCES TCOSTCENTER (OBJECTID)
END
;

BEGIN
ALTER TABLE TDEPARTMENT
    ADD CONSTRAINT TDEPARTMENT_FK_2 FOREIGN KEY (DOMAINKEY)
    REFERENCES TDOMAIN (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TDEPARTMENT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TNOTIFY
    ADD CONSTRAINT TNOTIFY_FK_1 FOREIGN KEY (PROJCATKEY)
    REFERENCES TPROJCAT (PKEY)
END
;

BEGIN
ALTER TABLE TNOTIFY
    ADD CONSTRAINT TNOTIFY_FK_2 FOREIGN KEY (STATEKEY)
    REFERENCES TSTATE (PKEY)
END
;

BEGIN
ALTER TABLE TNOTIFY
    ADD CONSTRAINT TNOTIFY_FK_3 FOREIGN KEY (PERSONKEY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TNOTIFY
    ADD CONSTRAINT TNOTIFY_FK_4 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TNOTIFY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPERSON
    ADD CONSTRAINT TPERSON_FK_1 FOREIGN KEY (DEPKEY)
    REFERENCES TDEPARTMENT (PKEY)
END
;

BEGIN
ALTER TABLE TPERSON
    ADD CONSTRAINT TPERSON_FK_2 FOREIGN KEY (MYDEFAULTREPORT)
    REFERENCES TPRIVATEREPORTREPOSITORY (PKEY)
END
;

BEGIN
ALTER TABLE TPERSON
    ADD CONSTRAINT TPERSON_FK_3 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;

BEGIN
ALTER TABLE TPERSON
    ADD CONSTRAINT TPERSON_FK_4 FOREIGN KEY (SUBSTITUTEKEY)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPERSON                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPRIORITY
    ADD CONSTRAINT TPRIORITY_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TPRIORITY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPPRIORITY
    ADD CONSTRAINT TPPRIORITY_FK_1 FOREIGN KEY (PRIORITY)
    REFERENCES TPRIORITY (PKEY)
END
;

BEGIN
ALTER TABLE TPPRIORITY
    ADD CONSTRAINT TPPRIORITY_FK_2 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TPPRIORITY
    ADD CONSTRAINT TPPRIORITY_FK_3 FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPPRIORITY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPROJCAT
    ADD CONSTRAINT TPROJCAT_FK_1 FOREIGN KEY (PROJKEY)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPROJCAT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_1 FOREIGN KEY (DEFOWNER)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_2 FOREIGN KEY (DEFMANAGER)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_3 FOREIGN KEY (DEFINITSTATE)
    REFERENCES TSTATE (PKEY)
END
;

BEGIN
ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_4 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_5 FOREIGN KEY (STATUS)
    REFERENCES TSYSTEMSTATE (OBJECTID)
END
;

BEGIN
ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_6 FOREIGN KEY (PARENT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_7 FOREIGN KEY (DOMAINKEY)
    REFERENCES TDOMAIN (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TPROJECT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TRELEASE
    ADD CONSTRAINT TRELEASE_FK_1 FOREIGN KEY (PROJKEY)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TRELEASE
    ADD CONSTRAINT TRELEASE_FK_2 FOREIGN KEY (STATUS)
    REFERENCES TSYSTEMSTATE (OBJECTID)
END
;

BEGIN
ALTER TABLE TRELEASE
    ADD CONSTRAINT TRELEASE_FK_3 FOREIGN KEY (PARENT)
    REFERENCES TRELEASE (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TRELEASE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TROLE
    ADD CONSTRAINT TROLE_FK_1 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TROLE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSEVERITY
    ADD CONSTRAINT TSEVERITY_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TSEVERITY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPSEVERITY
    ADD CONSTRAINT TPSEVERITY_FK_1 FOREIGN KEY (SEVERITY)
    REFERENCES TSEVERITY (PKEY)
END
;

BEGIN
ALTER TABLE TPSEVERITY
    ADD CONSTRAINT TPSEVERITY_FK_2 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TPSEVERITY
    ADD CONSTRAINT TPSEVERITY_FK_3 FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPSEVERITY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSTATE
    ADD CONSTRAINT TSTATE_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TSTATE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSTATECHANGE
    ADD CONSTRAINT TSTATECHANGE_FK_1 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TSTATECHANGE
    ADD CONSTRAINT TSTATECHANGE_FK_2 FOREIGN KEY (CHANGEDTO)
    REFERENCES TSTATE (PKEY)
END
;

BEGIN
ALTER TABLE TSTATECHANGE
    ADD CONSTRAINT TSTATECHANGE_FK_3 FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TSTATECHANGE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TTRAIL
    ADD CONSTRAINT TTRAIL_FK_1 FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TTRAIL
    ADD CONSTRAINT TTRAIL_FK_2 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TTRAIL                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_1 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_2 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_3 FOREIGN KEY (ORIGINATOR)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_4 FOREIGN KEY (RESPONSIBLE)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_5 FOREIGN KEY (PROJCATKEY)
    REFERENCES TPROJCAT (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_6 FOREIGN KEY (CATEGORYKEY)
    REFERENCES TCATEGORY (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_7 FOREIGN KEY (CLASSKEY)
    REFERENCES TCLASS (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_8 FOREIGN KEY (PRIORITYKEY)
    REFERENCES TPRIORITY (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_9 FOREIGN KEY (SEVERITYKEY)
    REFERENCES TSEVERITY (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_10 FOREIGN KEY (RELNOTICEDKEY)
    REFERENCES TRELEASE (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_11 FOREIGN KEY (RELSCHEDULEDKEY)
    REFERENCES TRELEASE (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_12 FOREIGN KEY (STATE)
    REFERENCES TSTATE (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_13 FOREIGN KEY (PROJECTKEY)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_14 FOREIGN KEY (SUPERIORWORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKITEM                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TCOMPUTEDVALUES
    ADD CONSTRAINT TCOMPUTEDVALUES_FK_1 FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TCOMPUTEDVALUES
    ADD CONSTRAINT TCOMPUTEDVALUES_FK_2 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TCOMPUTEDVALUES                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPRIVATEREPORTREPOSITORY
    ADD CONSTRAINT TPRIVATEREPORTREPOSITORY_FK_1 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPRIVATEREPORTREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPUBLICREPORTREPOSITORY
    ADD CONSTRAINT TPUBLICREPORTREPOSITORY_FK_1 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPUBLICREPORTREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPROJECTREPORTREPOSITORY
    ADD CONSTRAINT TPROJECTREPORTREPOSITORY_FK_1 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPROJECTREPORTREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TACCOUNT
    ADD CONSTRAINT TACCOUNT_FK_1 FOREIGN KEY (STATUS)
    REFERENCES TSYSTEMSTATE (OBJECTID)
END
;

BEGIN
ALTER TABLE TACCOUNT
    ADD CONSTRAINT TACCOUNT_FK_2 FOREIGN KEY (COSTCENTER)
    REFERENCES TCOSTCENTER (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TACCOUNT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TATTACHMENT
    ADD CONSTRAINT TATTACHMENT_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TATTACHMENT
    ADD CONSTRAINT TATTACHMENT_FK_2 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TATTACHMENT
    ADD CONSTRAINT TATTACHMENT_FK_3 FOREIGN KEY (DOCUMENTSTATE)
    REFERENCES TDOCSTATE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TATTACHMENT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TCOST
    ADD CONSTRAINT TCOST_FK_1 FOREIGN KEY (ACCOUNT)
    REFERENCES TACCOUNT (OBJECTID)
END
;

BEGIN
ALTER TABLE TCOST
    ADD CONSTRAINT TCOST_FK_2 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TCOST
    ADD CONSTRAINT TCOST_FK_3 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TCOST
    ADD CONSTRAINT TCOST_FK_4 FOREIGN KEY (EFFORTTYPE)
    REFERENCES TEFFORTTYPE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TCOST                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TEFFORTTYPE
    ADD CONSTRAINT TEFFORTTYPE_FK_1 FOREIGN KEY (EFFORTUNIT)
    REFERENCES TEFFORTUNIT (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TEFFORTTYPE                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TEFFORTUNIT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TDOCSTATE
    ADD CONSTRAINT TDOCSTATE_FK_1 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TDOCSTATE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TDISABLEFIELD
    ADD CONSTRAINT TDISABLEFIELD_FK_1 FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
END
;

BEGIN
ALTER TABLE TDISABLEFIELD
    ADD CONSTRAINT TDISABLEFIELD_FK_2 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TDISABLEFIELD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPLISTTYPE
    ADD CONSTRAINT TPLISTTYPE_FK_1 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TPLISTTYPE
    ADD CONSTRAINT TPLISTTYPE_FK_2 FOREIGN KEY (CATEGORY)
    REFERENCES TCATEGORY (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPLISTTYPE                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TPROJECTTYPE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPSTATE
    ADD CONSTRAINT TPSTATE_FK_1 FOREIGN KEY (STATE)
    REFERENCES TSTATE (PKEY)
END
;

BEGIN
ALTER TABLE TPSTATE
    ADD CONSTRAINT TPSTATE_FK_2 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TPSTATE
    ADD CONSTRAINT TPSTATE_FK_3 FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPSTATE                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TSITE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_1 FOREIGN KEY (STATEFROM)
    REFERENCES TSTATE (PKEY)
END
;

BEGIN
ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_2 FOREIGN KEY (STATETO)
    REFERENCES TSTATE (PKEY)
END
;

BEGIN
ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_3 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_4 FOREIGN KEY (RESPONSIBLE)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKFLOW                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKFLOWROLE
    ADD CONSTRAINT TWORKFLOWROLE_FK_1 FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOW (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWROLE
    ADD CONSTRAINT TWORKFLOWROLE_FK_2 FOREIGN KEY (MAYCHANGEROLE)
    REFERENCES TROLE (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKFLOWROLE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKFLOWCATEGORY
    ADD CONSTRAINT TWORKFLOWCATEGORY_FK_1 FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOW (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWCATEGORY
    ADD CONSTRAINT TWORKFLOWCATEGORY_FK_2 FOREIGN KEY (CATEGORY)
    REFERENCES TCATEGORY (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKFLOWCATEGORY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TROLELISTTYPE
    ADD CONSTRAINT TROLELISTTYPE_FK_1 FOREIGN KEY (PROLE)
    REFERENCES TROLE (PKEY)
END
;

BEGIN
ALTER TABLE TROLELISTTYPE
    ADD CONSTRAINT TROLELISTTYPE_FK_2 FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TROLELISTTYPE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_2 FOREIGN KEY (ISSUE)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_3 FOREIGN KEY (ATTRIBUTEID)
    REFERENCES TATTRIBUTE (OBJECTID)
END
;

BEGIN
ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_4 FOREIGN KEY (OPTIONID)
    REFERENCES TATTRIBUTEOPTION (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TISSUEATTRIBUTEVALUE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TATTRIBUTEOPTION
    ADD CONSTRAINT TATTRIBUTEOPTION_FK_1 FOREIGN KEY (ATTRIBUTEID)
    REFERENCES TATTRIBUTE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TATTRIBUTEOPTION                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TATTRIBUTECLASS                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TATTRIBUTETYPE
    ADD CONSTRAINT TATTRIBUTETYPE_FK_1 FOREIGN KEY (ATTRIBUTECLASS)
    REFERENCES TATTRIBUTECLASS (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TATTRIBUTETYPE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TATTRIBUTE
    ADD CONSTRAINT TATTRIBUTE_FK_1 FOREIGN KEY (REQUIREDOPTION)
    REFERENCES TATTRIBUTEOPTION (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TATTRIBUTE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_1 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_3 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TREPORTLAYOUT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSCHEDULER
    ADD CONSTRAINT TSCHEDULER_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TSCHEDULER                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPROJECTACCOUNT
    ADD CONSTRAINT TPROJECTACCOUNT_FK_1 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TPROJECTACCOUNT
    ADD CONSTRAINT TPROJECTACCOUNT_FK_2 FOREIGN KEY (ACCOUNT)
    REFERENCES TACCOUNT (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TPROJECTACCOUNT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TGROUPMEMBER
    ADD CONSTRAINT TGROUPMEMBER_FK_1 FOREIGN KEY (THEGROUP)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TGROUPMEMBER
    ADD CONSTRAINT TGROUPMEMBER_FK_2 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TGROUPMEMBER                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TBUDGET
    ADD CONSTRAINT TBUDGET_FK_1 FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TBUDGET
    ADD CONSTRAINT TBUDGET_FK_2 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TBUDGET                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TACTUALESTIMATEDBUDGET
    ADD CONSTRAINT TACTUALESTIMATEDBUDGET_FK_1 FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TACTUALESTIMATEDBUDGET
    ADD CONSTRAINT TACTUALESTIMATEDBUDGET_FK_2 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TACTUALESTIMATEDBUDGET                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSYSTEMSTATE
    ADD CONSTRAINT TSYSTEMSTATE_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TSYSTEMSTATE                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TCOSTCENTER                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TMOTD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TDASHBOARDSCREEN
    ADD CONSTRAINT TDASHBOARDSCREEN_FK_1 FOREIGN KEY (PERSONPKEY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TDASHBOARDSCREEN
    ADD CONSTRAINT TDASHBOARDSCREEN_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TDASHBOARDSCREEN
    ADD CONSTRAINT TDASHBOARDSCREEN_FK_3 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TDASHBOARDSCREEN                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TDASHBOARDTAB
    ADD CONSTRAINT TDASHBOARDTAB_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TDASHBOARDSCREEN (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TDASHBOARDTAB                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TDASHBOARDPANEL
    ADD CONSTRAINT TDASHBOARDPANEL_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TDASHBOARDTAB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TDASHBOARDPANEL                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TDASHBOARDFIELD
    ADD CONSTRAINT TDASHBOARDFIELD_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TDASHBOARDPANEL (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TDASHBOARDFIELD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TDASHBOARDPARAMETER
    ADD CONSTRAINT TDASHBOARDPARAMETER_FK_1 FOREIGN KEY (DASHBOARDFIELD)
    REFERENCES TDASHBOARDFIELD (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TDASHBOARDPARAMETER                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TVERSIONCONTROLPARAMETER
    ADD CONSTRAINT TVERSIONCONTROLPARAMETER_FK_1 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TVERSIONCONTROLPARAMETER                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TFIELD
    ADD CONSTRAINT TFIELD_FK_1 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TFIELD
    ADD CONSTRAINT TFIELD_FK_2 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TFIELD
    ADD CONSTRAINT TFIELD_FK_3 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TFIELD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_1 FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
END
;

BEGIN
ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_2 FOREIGN KEY (ISSUETYPE)
    REFERENCES TCATEGORY (PKEY)
END
;

BEGIN
ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_3 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_4 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_5 FOREIGN KEY (GROOVYSCRIPT)
    REFERENCES TSCRIPTS (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TFIELDCONFIG                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TROLEFIELD
    ADD CONSTRAINT TROLEFIELD_FK_1 FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TROLEFIELD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TCONFIGOPTIONSROLE
    ADD CONSTRAINT TCONFIGOPTIONSROLE_FK_1 FOREIGN KEY (CONFIGKEY)
    REFERENCES TFIELDCONFIG (OBJECTID)
END
;

BEGIN
ALTER TABLE TCONFIGOPTIONSROLE
    ADD CONSTRAINT TCONFIGOPTIONSROLE_FK_2 FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
END
;

BEGIN
ALTER TABLE TCONFIGOPTIONSROLE
    ADD CONSTRAINT TCONFIGOPTIONSROLE_FK_3 FOREIGN KEY (OPTIONKEY)
    REFERENCES TOPTION (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TCONFIGOPTIONSROLE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TTEXTBOXSETTINGS
    ADD CONSTRAINT TTEXTBOXSETTINGS_FK_1 FOREIGN KEY (CONFIG)
    REFERENCES TFIELDCONFIG (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TTEXTBOXSETTINGS                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TGENERALSETTINGS
    ADD CONSTRAINT TGENERALSETTINGS_FK_1 FOREIGN KEY (CONFIG)
    REFERENCES TFIELDCONFIG (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TGENERALSETTINGS                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TLIST
    ADD CONSTRAINT TLIST_FK_1 FOREIGN KEY (PARENTLIST)
    REFERENCES TLIST (OBJECTID)
END
;

BEGIN
ALTER TABLE TLIST
    ADD CONSTRAINT TLIST_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TLIST
    ADD CONSTRAINT TLIST_FK_3 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TLIST                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TOPTION
    ADD CONSTRAINT TOPTION_FK_1 FOREIGN KEY (LIST)
    REFERENCES TLIST (OBJECTID)
END
;

BEGIN
ALTER TABLE TOPTION
    ADD CONSTRAINT TOPTION_FK_2 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TOPTION                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TOPTIONSETTINGS
    ADD CONSTRAINT TOPTIONSETTINGS_FK_1 FOREIGN KEY (LIST)
    REFERENCES TLIST (OBJECTID)
END
;

BEGIN
ALTER TABLE TOPTIONSETTINGS
    ADD CONSTRAINT TOPTIONSETTINGS_FK_2 FOREIGN KEY (CONFIG)
    REFERENCES TFIELDCONFIG (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TOPTIONSETTINGS                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TATTRIBUTEVALUE
    ADD CONSTRAINT TATTRIBUTEVALUE_FK_1 FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
END
;

BEGIN
ALTER TABLE TATTRIBUTEVALUE
    ADD CONSTRAINT TATTRIBUTEVALUE_FK_2 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TATTRIBUTEVALUE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSCREEN
    ADD CONSTRAINT TSCREEN_FK_1 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TSCREEN
    ADD CONSTRAINT TSCREEN_FK_2 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TSCREEN
    ADD CONSTRAINT TSCREEN_FK_3 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TSCREEN                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSCREENTAB
    ADD CONSTRAINT TSCREENTAB_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TSCREEN (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TSCREENTAB                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSCREENPANEL
    ADD CONSTRAINT TSCREENPANEL_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TSCREENTAB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TSCREENPANEL                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSCREENFIELD
    ADD CONSTRAINT TSCREENFIELD_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TSCREENPANEL (OBJECTID)
END
;

BEGIN
ALTER TABLE TSCREENFIELD
    ADD CONSTRAINT TSCREENFIELD_FK_2 FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TSCREENFIELD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TACTION
    ADD CONSTRAINT TACTION_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TACTION                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_1 FOREIGN KEY (SCREEN)
    REFERENCES TSCREEN (OBJECTID)
END
;

BEGIN
ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_2 FOREIGN KEY (ISSUETYPE)
    REFERENCES TCATEGORY (PKEY)
END
;

BEGIN
ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_3 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_4 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_5 FOREIGN KEY (ACTIONKEY)
    REFERENCES TACTION (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TSCREENCONFIG                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TINITSTATE
    ADD CONSTRAINT TINITSTATE_FK_1 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TINITSTATE
    ADD CONSTRAINT TINITSTATE_FK_2 FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
END
;

BEGIN
ALTER TABLE TINITSTATE
    ADD CONSTRAINT TINITSTATE_FK_3 FOREIGN KEY (STATEKEY)
    REFERENCES TSTATE (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TINITSTATE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TEVENT
    ADD CONSTRAINT TEVENT_FK_1 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TEVENT
    ADD CONSTRAINT TEVENT_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TEVENT
    ADD CONSTRAINT TEVENT_FK_3 FOREIGN KEY (EVENTSCRIPT)
    REFERENCES TCLOB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TEVENT                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TCLOB                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TNOTIFYFIELD
    ADD CONSTRAINT TNOTIFYFIELD_FK_1 FOREIGN KEY (NOTIFYTRIGGER)
    REFERENCES TNOTIFYTRIGGER (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TNOTIFYFIELD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TNOTIFYTRIGGER
    ADD CONSTRAINT TNOTIFYTRIGGER_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TNOTIFYTRIGGER                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTINGS_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTINGS_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTINGS_FK_3 FOREIGN KEY (NOTIFYTRIGGER)
    REFERENCES TNOTIFYTRIGGER (OBJECTID)
END
;

BEGIN
ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTINGS_FK_4 FOREIGN KEY (NOTIFYFILTER)
    REFERENCES TQUERYREPOSITORY (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TNOTIFYSETTINGS                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSITORY_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSITORY_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSITORY_FK_3 FOREIGN KEY (QUERYKEY)
    REFERENCES TCLOB (OBJECTID)
END
;

BEGIN
ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSITORY_FK_4 FOREIGN KEY (CATEGORYKEY)
    REFERENCES TFILTERCATEGORY (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TQUERYREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TLOCALIZEDRESOURCES                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TLINKTYPE
    ADD CONSTRAINT TLINKTYPE_FK_1 FOREIGN KEY (OUTWARDICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;

BEGIN
ALTER TABLE TLINKTYPE
    ADD CONSTRAINT TLINKTYPE_FK_2 FOREIGN KEY (INWARDICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TLINKTYPE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_1 FOREIGN KEY (LINKPRED)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_2 FOREIGN KEY (LINKSUCC)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_3 FOREIGN KEY (LINKTYPE)
    REFERENCES TLINKTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_4 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKITEMLINK                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TLOGGINGLEVEL                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKITEMLOCK
    ADD CONSTRAINT TWORKITEMLOCK_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TWORKITEMLOCK
    ADD CONSTRAINT TWORKITEMLOCK_FK_2 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKITEMLOCK                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TEXPORTTEMPLATE
    ADD CONSTRAINT TEXPORTTEMPLATE_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TEXPORTTEMPLATE
    ADD CONSTRAINT TEXPORTTEMPLATE_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TEXPORTTEMPLATE
    ADD CONSTRAINT TEXPORTTEMPLATE_FK_3 FOREIGN KEY (CATEGORYKEY)
    REFERENCES TREPORTCATEGORY (OBJECTID)
END
;

BEGIN
ALTER TABLE TEXPORTTEMPLATE
    ADD CONSTRAINT TEXPORTTEMPLATE_FK_4 FOREIGN KEY (PARENT)
    REFERENCES TEXPORTTEMPLATE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TEXPORTTEMPLATE                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TEMAILPROCESSED                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TAPPLICATIONCONTEXT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TLOGGEDINUSERS
    ADD CONSTRAINT TLOGGEDINUSERS_FK_1 FOREIGN KEY (LOGGEDUSER)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TLOGGEDINUSERS
    ADD CONSTRAINT TLOGGEDINUSERS_FK_2 FOREIGN KEY (NODEADDRESS)
    REFERENCES CLUSTERNODE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TLOGGEDINUSERS                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* CLUSTERNODE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TENTITYCHANGES
    ADD CONSTRAINT TENTITYCHANGES_FK_1 FOREIGN KEY (CLUSTERNODE)
    REFERENCES CLUSTERNODE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TENTITYCHANGES                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSUMMARYMAIL
    ADD CONSTRAINT TSUMMARYMAIL_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TSUMMARYMAIL
    ADD CONSTRAINT TSUMMARYMAIL_FK_2 FOREIGN KEY (PERSONFROM)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TSUMMARYMAIL
    ADD CONSTRAINT TSUMMARYMAIL_FK_3 FOREIGN KEY (PERSONTO)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TSUMMARYMAIL                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TOUTLINECODE
    ADD CONSTRAINT TOUTLINECODE_FK_1 FOREIGN KEY (OUTLINETEMPLATE)
    REFERENCES TOUTLINETEMPLATE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TOUTLINECODE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TOUTLINETEMPLATEDEF
    ADD CONSTRAINT TOUTLINETEMPLATEDEF_FK_1 FOREIGN KEY (OUTLINETEMPLATE)
    REFERENCES TOUTLINETEMPLATE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TOUTLINETEMPLATEDEF                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TOUTLINETEMPLATE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE THISTORYTRANSACTION
    ADD CONSTRAINT THISTORYTRANSACTION_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE THISTORYTRANSACTION
    ADD CONSTRAINT THISTORYTRANSACTION_FK_2 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* THISTORYTRANSACTION                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_1 FOREIGN KEY (HISTORYTRANSACTION)
    REFERENCES THISTORYTRANSACTION (OBJECTID)
END
;

BEGIN
ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_2 FOREIGN KEY (PARENTCOMMENT)
    REFERENCES TFIELDCHANGE (OBJECTID)
END
;

BEGIN
ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_3 FOREIGN KEY (NEWCUSTOMOPTIONID)
    REFERENCES TOPTION (OBJECTID)
END
;

BEGIN
ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_4 FOREIGN KEY (OLDCUSTOMOPTIONID)
    REFERENCES TOPTION (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TFIELDCHANGE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TSCRIPTS
    ADD CONSTRAINT TSCRIPTS_FK_1 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TSCRIPTS
    ADD CONSTRAINT TSCRIPTS_FK_2 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TSCRIPTS
    ADD CONSTRAINT TSCRIPTS_FK_3 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TSCRIPTS                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TREVISION
    ADD CONSTRAINT TREVISION_FK_1 FOREIGN KEY (REPOSITORYKEY)
    REFERENCES TREPOSITORY (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TREVISION                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TREVISIONWORKITEMS
    ADD CONSTRAINT TREVISIONWORKITEMS_FK_1 FOREIGN KEY (REVISIONKEY)
    REFERENCES TREVISION (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TREVISIONWORKITEMS                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TTEMPLATEPERSON
    ADD CONSTRAINT TTEMPLATEPERSON_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TTEMPLATEPERSON
    ADD CONSTRAINT TTEMPLATEPERSON_FK_2 FOREIGN KEY (REPORTTEMPLATE)
    REFERENCES TEXPORTTEMPLATE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TTEMPLATEPERSON                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TBLOB                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TRECURRENCEPATTERN                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSONSETTINGS_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSONSETTINGS_FK_2 FOREIGN KEY (REPORTTEMPLATE)
    REFERENCES TEXPORTTEMPLATE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TREPORTPERSONSETTINGS                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TREPORTPARAMETER
    ADD CONSTRAINT TREPORTPARAMETER_FK_1 FOREIGN KEY (REPORTPERSONSETTINGS)
    REFERENCES TREPORTPERSONSETTINGS (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TREPORTPARAMETER                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TMSPROJECTTASK
    ADD CONSTRAINT TMSPROJECTTASK_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TMSPROJECTTASK                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TMSPROJECTEXCHANGE
    ADD CONSTRAINT TMSPROJECTEXCHANGE_FK_1 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TMSPROJECTEXCHANGE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TFILTERCATEGORY
    ADD CONSTRAINT TFILTERCATEGORY_FK_1 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TFILTERCATEGORY
    ADD CONSTRAINT TFILTERCATEGORY_FK_2 FOREIGN KEY (CREATEDBY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TFILTERCATEGORY
    ADD CONSTRAINT TFILTERCATEGORY_FK_3 FOREIGN KEY (PARENTID)
    REFERENCES TFILTERCATEGORY (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TFILTERCATEGORY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TREPORTCATEGORY
    ADD CONSTRAINT TREPORTCATEGORY_FK_1 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TREPORTCATEGORY
    ADD CONSTRAINT TREPORTCATEGORY_FK_2 FOREIGN KEY (CREATEDBY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TREPORTCATEGORY
    ADD CONSTRAINT TREPORTCATEGORY_FK_3 FOREIGN KEY (PARENTID)
    REFERENCES TREPORTCATEGORY (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TREPORTCATEGORY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TMENUITEMQUERY
    ADD CONSTRAINT TMENUITEMQUERY_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TMENUITEMQUERY
    ADD CONSTRAINT TMENUITEMQUERY_FK_2 FOREIGN KEY (QUERYKEY)
    REFERENCES TQUERYREPOSITORY (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TMENUITEMQUERY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TCHILDISSUETYPE
    ADD CONSTRAINT TCHILDISSUETYPE_FK_1 FOREIGN KEY (ISSUETYPEPARENT)
    REFERENCES TCATEGORY (PKEY)
END
;

BEGIN
ALTER TABLE TCHILDISSUETYPE
    ADD CONSTRAINT TCHILDISSUETYPE_FK_2 FOREIGN KEY (ISSUETYPECHILD)
    REFERENCES TCATEGORY (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TCHILDISSUETYPE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPERSONBASKET
    ADD CONSTRAINT TPERSONBASKET_FK_1 FOREIGN KEY (BASKET)
    REFERENCES TBASKET (OBJECTID)
END
;

BEGIN
ALTER TABLE TPERSONBASKET
    ADD CONSTRAINT TPERSONBASKET_FK_2 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TPERSONBASKET
    ADD CONSTRAINT TPERSONBASKET_FK_3 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPERSONBASKET                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TBASKET
    ADD CONSTRAINT TBASKET_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TBASKET                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TMAILTEMPLATECONFIG
    ADD CONSTRAINT TMAILTEMPLATECONFIG_FK_1 FOREIGN KEY (MAILTEMPLATE)
    REFERENCES TMAILTEMPLATE (OBJECTID)
END
;

BEGIN
ALTER TABLE TMAILTEMPLATECONFIG
    ADD CONSTRAINT TMAILTEMPLATECONFIG_FK_2 FOREIGN KEY (ISSUETYPE)
    REFERENCES TCATEGORY (PKEY)
END
;

BEGIN
ALTER TABLE TMAILTEMPLATECONFIG
    ADD CONSTRAINT TMAILTEMPLATECONFIG_FK_3 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TMAILTEMPLATECONFIG
    ADD CONSTRAINT TMAILTEMPLATECONFIG_FK_4 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TMAILTEMPLATECONFIG                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TMAILTEMPLATE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TMAILTEMPLATEDEF
    ADD CONSTRAINT TMAILTEMPLATEDEF_FK_1 FOREIGN KEY (MAILTEMPLATE)
    REFERENCES TMAILTEMPLATE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TMAILTEMPLATEDEF                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TLASTVISITEDITEM
    ADD CONSTRAINT TLASTVISITEDITEM_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TLASTVISITEDITEM
    ADD CONSTRAINT TLASTVISITEDITEM_FK_2 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TLASTVISITEDITEM                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKFLOWDEF
    ADD CONSTRAINT TWORKFLOWDEF_FK_1 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKFLOWDEF                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKFLOWTRANSITION
    ADD CONSTRAINT TWORKFLOWTRANSITION_FK_1 FOREIGN KEY (STATIONFROM)
    REFERENCES TWORKFLOWSTATION (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWTRANSITION
    ADD CONSTRAINT TWORKFLOWTRANSITION_FK_2 FOREIGN KEY (STATIONTO)
    REFERENCES TWORKFLOWSTATION (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWTRANSITION
    ADD CONSTRAINT TWORKFLOWTRANSITION_FK_3 FOREIGN KEY (ACTIONKEY)
    REFERENCES TACTION (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWTRANSITION
    ADD CONSTRAINT TWORKFLOWTRANSITION_FK_4 FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOWDEF (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKFLOWTRANSITION                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKFLOWSTATION
    ADD CONSTRAINT TWORKFLOWSTATION_FK_1 FOREIGN KEY (STATUS)
    REFERENCES TSTATE (PKEY)
END
;

BEGIN
ALTER TABLE TWORKFLOWSTATION
    ADD CONSTRAINT TWORKFLOWSTATION_FK_2 FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOWDEF (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKFLOWSTATION                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKFLOWACTIVITY
    ADD CONSTRAINT TWORKFLOWACTIVITY_FK_1 FOREIGN KEY (TRANSITIONACTIVITY)
    REFERENCES TWORKFLOWTRANSITION (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWACTIVITY
    ADD CONSTRAINT TWORKFLOWACTIVITY_FK_2 FOREIGN KEY (STATIONENTRYACTIVITY)
    REFERENCES TWORKFLOWSTATION (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWACTIVITY
    ADD CONSTRAINT TWORKFLOWACTIVITY_FK_3 FOREIGN KEY (STATIONEXITACTIVITY)
    REFERENCES TWORKFLOWSTATION (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWACTIVITY
    ADD CONSTRAINT TWORKFLOWACTIVITY_FK_4 FOREIGN KEY (STATIONDOACTIVITY)
    REFERENCES TWORKFLOWSTATION (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWACTIVITY
    ADD CONSTRAINT TWORKFLOWACTIVITY_FK_5 FOREIGN KEY (GROOVYSCRIPT)
    REFERENCES TSCRIPTS (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWACTIVITY
    ADD CONSTRAINT TWORKFLOWACTIVITY_FK_6 FOREIGN KEY (NEWMAN)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TWORKFLOWACTIVITY
    ADD CONSTRAINT TWORKFLOWACTIVITY_FK_7 FOREIGN KEY (NEWRESP)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TWORKFLOWACTIVITY
    ADD CONSTRAINT TWORKFLOWACTIVITY_FK_8 FOREIGN KEY (SLA)
    REFERENCES TSLA (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWACTIVITY
    ADD CONSTRAINT TWORKFLOWACTIVITY_FK_9 FOREIGN KEY (SCREEN)
    REFERENCES TSCREEN (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKFLOWACTIVITY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWFACTIVITYCONTEXTPARAMS
    ADD CONSTRAINT TWFACTIVITYCONTEXTPARAMS_FK_1 FOREIGN KEY (WORKFLOWACTIVITY)
    REFERENCES TWORKFLOWACTIVITY (OBJECTID)
END
;

BEGIN
ALTER TABLE TWFACTIVITYCONTEXTPARAMS
    ADD CONSTRAINT TWFACTIVITYCONTEXTPARAMS_FK_2 FOREIGN KEY (ISSUETYPE)
    REFERENCES TCATEGORY (PKEY)
END
;

BEGIN
ALTER TABLE TWFACTIVITYCONTEXTPARAMS
    ADD CONSTRAINT TWFACTIVITYCONTEXTPARAMS_FK_3 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TWFACTIVITYCONTEXTPARAMS
    ADD CONSTRAINT TWFACTIVITYCONTEXTPARAMS_FK_4 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TWFACTIVITYCONTEXTPARAMS                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKFLOWGUARD
    ADD CONSTRAINT TWORKFLOWGUARD_FK_1 FOREIGN KEY (WORKFLOWTRANSITION)
    REFERENCES TWORKFLOWTRANSITION (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWGUARD
    ADD CONSTRAINT TWORKFLOWGUARD_FK_2 FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
END
;

BEGIN
ALTER TABLE TWORKFLOWGUARD
    ADD CONSTRAINT TWORKFLOWGUARD_FK_3 FOREIGN KEY (GROOVYSCRIPT)
    REFERENCES TSCRIPTS (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWGUARD
    ADD CONSTRAINT TWORKFLOWGUARD_FK_4 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKFLOWGUARD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKFLOWCONNECT
    ADD CONSTRAINT TWORKFLOWCONNECT_FK_1 FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOWDEF (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWCONNECT
    ADD CONSTRAINT TWORKFLOWCONNECT_FK_2 FOREIGN KEY (ISSUETYPE)
    REFERENCES TCATEGORY (PKEY)
END
;

BEGIN
ALTER TABLE TWORKFLOWCONNECT
    ADD CONSTRAINT TWORKFLOWCONNECT_FK_3 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TWORKFLOWCONNECT
    ADD CONSTRAINT TWORKFLOWCONNECT_FK_4 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKFLOWCONNECT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TWORKFLOWCOMMENT
    ADD CONSTRAINT TWORKFLOWCOMMENT_FK_1 FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOWDEF (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TWORKFLOWCOMMENT                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TSLA                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TESCALATIONENTRY
    ADD CONSTRAINT TESCALATIONENTRY_FK_1 FOREIGN KEY (SLA)
    REFERENCES TSLA (OBJECTID)
END
;

BEGIN
ALTER TABLE TESCALATIONENTRY
    ADD CONSTRAINT TESCALATIONENTRY_FK_2 FOREIGN KEY (PRIORITY)
    REFERENCES TPRIORITY (PKEY)
END
;

BEGIN
ALTER TABLE TESCALATIONENTRY
    ADD CONSTRAINT TESCALATIONENTRY_FK_3 FOREIGN KEY (ESCALATETO)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TESCALATIONENTRY                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TESCALATIONSTATE
    ADD CONSTRAINT TESCALATIONSTATE_FK_1 FOREIGN KEY (ESCALATIONENTRY)
    REFERENCES TESCALATIONENTRY (OBJECTID)
END
;

BEGIN
ALTER TABLE TESCALATIONSTATE
    ADD CONSTRAINT TESCALATIONSTATE_FK_2 FOREIGN KEY (STATUS)
    REFERENCES TSTATE (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TESCALATIONSTATE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TORGPROJECTSLA
    ADD CONSTRAINT TORGPROJECTSLA_FK_1 FOREIGN KEY (DEPARTMENT)
    REFERENCES TDEPARTMENT (PKEY)
END
;

BEGIN
ALTER TABLE TORGPROJECTSLA
    ADD CONSTRAINT TORGPROJECTSLA_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TORGPROJECTSLA
    ADD CONSTRAINT TORGPROJECTSLA_FK_3 FOREIGN KEY (SLA)
    REFERENCES TSLA (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TORGPROJECTSLA                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TREADISSUE
    ADD CONSTRAINT TREADISSUE_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TREADISSUE
    ADD CONSTRAINT TREADISSUE_FK_2 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TREADISSUE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TLASTEXECUTEDQUERY
    ADD CONSTRAINT TLASTEXECUTEDQUERY_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TLASTEXECUTEDQUERY
    ADD CONSTRAINT TLASTEXECUTEDQUERY_FK_2 FOREIGN KEY (QUERYCLOB)
    REFERENCES TCLOB (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TLASTEXECUTEDQUERY                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TGLOBALCSSSTYLE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TREPORTSUBSCRIBE
    ADD CONSTRAINT TREPORTSUBSCRIBE_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TREPORTSUBSCRIBE
    ADD CONSTRAINT TREPORTSUBSCRIBE_FK_2 FOREIGN KEY (RECURRENCEPATTERN)
    REFERENCES TRECURRENCEPATTERN (OBJECTID)
END
;

BEGIN
ALTER TABLE TREPORTSUBSCRIBE
    ADD CONSTRAINT TREPORTSUBSCRIBE_FK_3 FOREIGN KEY (REPORTTEMPLATE)
    REFERENCES TEXPORTTEMPLATE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TREPORTSUBSCRIBE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TGRIDLAYOUT
    ADD CONSTRAINT TGRIDLAYOUT_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TGRIDLAYOUT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TGRIDFIELD
    ADD CONSTRAINT TGRIDFIELD_FK_1 FOREIGN KEY (GRIDLAYOUT)
    REFERENCES TGRIDLAYOUT (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TGRIDFIELD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TGRIDGROUPINGSORTING
    ADD CONSTRAINT TGRIDGROUPINGSORTING_FK_1 FOREIGN KEY (GRIDLAYOUT)
    REFERENCES TGRIDLAYOUT (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TGRIDGROUPINGSORTING                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TNAVIGATORLAYOUT
    ADD CONSTRAINT TNAVIGATORLAYOUT_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TNAVIGATORLAYOUT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TNAVIGATORCOLUMN
    ADD CONSTRAINT TNAVIGATORCOLUMN_FK_1 FOREIGN KEY (NAVIGATORLAYOUT)
    REFERENCES TNAVIGATORLAYOUT (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TNAVIGATORCOLUMN                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TNAVIGATORGROUPINGSORTING
    ADD CONSTRAINT TNAVIGATORGROUPINGSORTING_FK_1 FOREIGN KEY (NAVIGATORLAYOUT)
    REFERENCES TNAVIGATORLAYOUT (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TNAVIGATORGROUPINGSORTING                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TCARDGROUPINGFIELD
    ADD CONSTRAINT TCARDGROUPINGFIELD_FK_1 FOREIGN KEY (NAVIGATORLAYOUT)
    REFERENCES TNAVIGATORLAYOUT (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TCARDGROUPINGFIELD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TCARDFIELDOPTION
    ADD CONSTRAINT TCARDFIELDOPTION_FK_1 FOREIGN KEY (GROUPINGFIELD)
    REFERENCES TCARDGROUPINGFIELD (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TCARDFIELDOPTION                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TCARDPANEL                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TCARDFIELD
    ADD CONSTRAINT TCARDFIELD_FK_1 FOREIGN KEY (CARDPANEL)
    REFERENCES TCARDPANEL (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TCARDFIELD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TVIEWPARAM
    ADD CONSTRAINT TVIEWPARAM_FK_1 FOREIGN KEY (NAVIGATORLAYOUT)
    REFERENCES TNAVIGATORLAYOUT (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TVIEWPARAM                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TGENERALPARAM                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TVERSIONCONTROL
    ADD CONSTRAINT TVERSIONCONTROL_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TVERSIONCONTROL (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TVERSIONCONTROL                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TSHORTCUT                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TMAILTEXTBLOCK
    ADD CONSTRAINT TMAILTEXTBLOCK_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TMAILTEXTBLOCK
    ADD CONSTRAINT TMAILTEXTBLOCK_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TMAILTEXTBLOCK                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPROLE
    ADD CONSTRAINT TPROLE_FK_1 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TPROLE
    ADD CONSTRAINT TPROLE_FK_2 FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TPROLE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TCHILDPROJECTTYPE
    ADD CONSTRAINT TCHILDPROJECTTYPE_FK_1 FOREIGN KEY (PROJECTTYPEPARENT)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TCHILDPROJECTTYPE
    ADD CONSTRAINT TCHILDPROJECTTYPE_FK_2 FOREIGN KEY (PROJECTTYPECHILD)
    REFERENCES TPROJECTTYPE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TCHILDPROJECTTYPE                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TDOMAIN                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TPERSONINDOMAIN
    ADD CONSTRAINT TPERSONINDOMAIN_FK_1 FOREIGN KEY (PERSONKEY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TPERSONINDOMAIN
    ADD CONSTRAINT TPERSONINDOMAIN_FK_2 FOREIGN KEY (DOMAINKEY)
    REFERENCES TDOMAIN (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TPERSONINDOMAIN                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TATTACHMENTVERSION
    ADD CONSTRAINT TATTACHMENTVERSION_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TATTACHMENTVERSION
    ADD CONSTRAINT TATTACHMENTVERSION_FK_2 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
END
;

BEGIN
ALTER TABLE TATTACHMENTVERSION
    ADD CONSTRAINT TATTACHMENTVERSION_FK_3 FOREIGN KEY (DOCUMENTSTATE)
    REFERENCES TDOCSTATE (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TATTACHMENTVERSION                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TUSERLEVEL                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TUSERLEVELSETTING
    ADD CONSTRAINT TUSERLEVELSETTING_FK_1 FOREIGN KEY (USERLEVEL)
    REFERENCES TUSERLEVEL (OBJECTID)
END
;




/* ---------------------------------------------------------------------- */
/* TUSERLEVELSETTING                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TUSERFEATURE
    ADD CONSTRAINT TUSERFEATURE_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;




/* ---------------------------------------------------------------------- */
/* TUSERFEATURE                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TITEMTRANSITION
    ADD CONSTRAINT TITEMTRANSITION_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TITEMTRANSITION
    ADD CONSTRAINT TITEMTRANSITION_FK_2 FOREIGN KEY (TRANSITION)
    REFERENCES TWORKFLOWTRANSITION (OBJECTID)
END
;



