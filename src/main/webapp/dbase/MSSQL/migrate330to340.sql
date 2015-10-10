--Remove report layout
DELETE FROM TREPORTLAYOUT;

UPDATE TWORKITEM SET SUPERIORWORKITEM=NULL WHERE SUPERIORWORKITEM=0;

ALTER TABLE TCATEGORY ADD SYMBOL [VARCHAR] (255) NULL;
ALTER TABLE TPERSON ALTER COLUMN LOGINNAME varchar(60);

ALTER TABLE TWORKITEM ADD TASKISMILESTONE CHAR (1) default 'N' NULL;
ALTER TABLE TWORKITEM ADD TASKISSUBPROJECT CHAR (1) default 'N' NULL;
ALTER TABLE TWORKITEM ADD TASKISSUMMARY CHAR (1) default 'N' NULL;
ALTER TABLE TWORKITEM ADD TASKCONSTRAINT INT NULL;
ALTER TABLE TWORKITEM ADD TASKCONSTRAINTDATE DATETIME NULL;
ALTER TABLE TWORKITEM ADD PSPCODE [VARCHAR] (255) NULL;

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
            COMPUTEDVALUE FLOAT NOT NULL,

    CONSTRAINT TCOMPUTEDVALUES_PK PRIMARY KEY(PKEY));
    

ALTER TABLE TPRIVATEREPORTREPOSITORY ADD MENUITEM CHAR (1) default 'N' NULL;

ALTER TABLE TCOST ADD EFFORTTYPE INT NULL;
ALTER TABLE TCOST ADD EFFORTVALUE FLOAT NULL;
ALTER TABLE TCOST ADD EFFORTDATE DATETIME NULL;
ALTER TABLE TCOST ADD INVOICENUMBER [VARCHAR] (255) NULL;
ALTER TABLE TCOST ADD INVOICEDATE DATETIME NULL;
ALTER TABLE TCOST ADD INVOICEPATH [VARCHAR] (255) NULL;

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

    CONSTRAINT TEFFORTTYPE_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TEFFORTUNIT_PK PRIMARY KEY(OBJECTID));
    
ALTER TABLE TSITE ADD RECEIVINGPROTOCOL [VARCHAR] (6) NULL;
ALTER TABLE TSITE ALTER COLUMN LDAPATTRIBUTELOGINNAME varchar(30);

ALTER TABLE TBUDGET ADD EFFORTTYPE INT NULL;
ALTER TABLE TBUDGET ADD EFFORTVALUE FLOAT NULL;

ALTER TABLE TACTUALESTIMATEDBUDGET ADD EFFORTTYPE INT NULL;
ALTER TABLE TACTUALESTIMATEDBUDGET ADD EFFORTVALUE FLOAT NULL;

/* ---------------------------------------------------------------------- */
/* TDASHBOARDSCREEN                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TDASHBOARDSCREEN_FK_1')
    ALTER TABLE TDASHBOARDSCREEN DROP CONSTRAINT TDASHBOARDSCREEN_FK_1;
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
            PERSONPKEY INT NOT NULL,
            DESCRIPTION VARCHAR (512) NULL,

    CONSTRAINT TDASHBOARDSCREEN_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TDASHBOARDTAB_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TDASHBOARDPANEL_PK PRIMARY KEY(OBJECTID));





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
            DASHBOARDID VARCHAR (512) NOT NULL,
            THEDESCRIPTION VARCHAR (512) NULL,

    CONSTRAINT TDASHBOARDFIELD_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TDASHBOARDPARAMETER_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TVERSIONCONTROLPARAMETER_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TFIELD_FK_1')
    ALTER TABLE TFIELD DROP CONSTRAINT TFIELD_FK_1;
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
            DESCRIPTION VARCHAR (512) NULL,
            OWNER INT NULL,

    CONSTRAINT TFIELD_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TFIELDCONFIG_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TROLEFIELD                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TROLEFIELD_FK_1')
    ALTER TABLE TROLEFIELD DROP CONSTRAINT TROLEFIELD_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TROLEFIELD_FK_2')
    ALTER TABLE TROLEFIELD DROP CONSTRAINT TROLEFIELD_FK_2;
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

    CONSTRAINT TROLEFIELD_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TCONFIGOPTIONSROLE_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TTEXTBOXSETTINGS_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TGENERALSETTINGS_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TLIST                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLIST_FK_1')
    ALTER TABLE TLIST DROP CONSTRAINT TLIST_FK_1;
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
            DESCRIPTION VARCHAR (512) NULL,
            PARENTLIST INT NULL,
            LISTTYPE INT NULL,
            CHILDNUMBER INT NULL,
            DELETED CHAR (1) default 'N' NULL,
			REPOSITORYTYPE INT NULL,
		    PROJECT INT NULL,
		    OWNER INT NULL,

    CONSTRAINT TLIST_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TOPTION                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TOPTION_FK_1')
    ALTER TABLE TOPTION DROP CONSTRAINT TOPTION_FK_1;
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
            PARENTOPTION INT NULL,
            SORTORDER INT NULL,
            ISDEFAULT CHAR (1) default 'N' NOT NULL,
            DELETED CHAR (1) default 'N' NULL,

    CONSTRAINT TOPTION_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TOPTIONSETTINGS_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TATTRIBUTEVALUE_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TSCREEN                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TSCREEN_FK_1')
    ALTER TABLE TSCREEN DROP CONSTRAINT TSCREEN_FK_1;
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
            DESCRIPTION VARCHAR (512) NULL,
            OWNER INT NULL,

    CONSTRAINT TSCREEN_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TSCREENTAB_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TSCREENPANEL_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TSCREENFIELD_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TACTION                                                      */
/* ---------------------------------------------------------------------- */

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

    CONSTRAINT TACTION_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TSCREENCONFIG_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TINITSTATE_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TEVENT_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TCLOB_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TNOTIFYFIELD_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TNOTIFYTRIGGER_PK PRIMARY KEY(OBJECTID));





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

    CONSTRAINT TNOTIFYSETTINGS_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TQUERYREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TQUERYREPOSITORY_FK_1')
    ALTER TABLE TQUERYREPOSITORY DROP CONSTRAINT TQUERYREPOSITORY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TQUERYREPOSITORY_FK_2')
    ALTER TABLE TQUERYREPOSITORY DROP CONSTRAINT TQUERYREPOSITORY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TQUERYREPOSITORY_FK_3')
    ALTER TABLE TQUERYREPOSITORY DROP CONSTRAINT TQUERYREPOSITORY_FK_3;
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

    CONSTRAINT TQUERYREPOSITORY_PK PRIMARY KEY(OBJECTID));





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
            TABLENAME VARCHAR (255) NOT NULL,
            PRIMARYKEYVALUE INT NOT NULL,
            FIELDNAME VARCHAR (255) NOT NULL,
            LOCALIZEDTEXT VARCHAR (255) NOT NULL,
            LOCALE VARCHAR (20) NOT NULL,

    CONSTRAINT TLOCALIZEDRESOURCES_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TWORKITEMLINK                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEMLINK_FK_1')
    ALTER TABLE TWORKITEMLINK DROP CONSTRAINT TWORKITEMLINK_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEMLINK_FK_2')
    ALTER TABLE TWORKITEMLINK DROP CONSTRAINT TWORKITEMLINK_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKITEMLINK')
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
         and tables.name = 'TWORKITEMLINK'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_83, @constraintname_83
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_83+' drop constraint '+@constraintname_83)
       FETCH NEXT from refcursor into @reftable_83, @constraintname_83
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TWORKITEMLINK
END
;

CREATE TABLE TWORKITEMLINK
(
            OBJECTID INT NOT NULL,
            LINKISCROSSPROJECT CHAR (1) default 'N' NOT NULL,
            LINKPRED INT NOT NULL,
            LINKSUCC INT NOT NULL,
            LINKTYPE INT NOT NULL,
            LINKLAG INT NULL,
            LINKLAGFORMAT INT NULL,

    CONSTRAINT TWORKITEMLINK_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TLOGGINGLEVEL                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLOGGINGLEVEL')
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
         and tables.name = 'TLOGGINGLEVEL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_84, @constraintname_84
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_84+' drop constraint '+@constraintname_84)
       FETCH NEXT from refcursor into @reftable_84, @constraintname_84
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

    CONSTRAINT TLOGGINGLEVEL_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TWORKITEMLOCK                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEMLOCK_FK_1')
    ALTER TABLE TWORKITEMLOCK DROP CONSTRAINT TWORKITEMLOCK_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TWORKITEMLOCK_FK_2')
    ALTER TABLE TWORKITEMLOCK DROP CONSTRAINT TWORKITEMLOCK_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TWORKITEMLOCK')
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
         and tables.name = 'TWORKITEMLOCK'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_85, @constraintname_85
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_85+' drop constraint '+@constraintname_85)
       FETCH NEXT from refcursor into @reftable_85, @constraintname_85
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

    CONSTRAINT TWORKITEMLOCK_PK PRIMARY KEY(WORKITEM));





/* ---------------------------------------------------------------------- */
/* TEXPORTTEMPLATE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TEXPORTTEMPLATE_FK_1')
    ALTER TABLE TEXPORTTEMPLATE DROP CONSTRAINT TEXPORTTEMPLATE_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TEXPORTTEMPLATE_FK_2')
    ALTER TABLE TEXPORTTEMPLATE DROP CONSTRAINT TEXPORTTEMPLATE_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TEXPORTTEMPLATE')
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
         and tables.name = 'TEXPORTTEMPLATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_86, @constraintname_86
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_86+' drop constraint '+@constraintname_86)
       FETCH NEXT from refcursor into @reftable_86, @constraintname_86
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

    CONSTRAINT TEXPORTTEMPLATE_PK PRIMARY KEY(OBJECTID));





/* ---------------------------------------------------------------------- */
/* TEMAILPROCESSED                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TEMAILPROCESSED')
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
         and tables.name = 'TEMAILPROCESSED'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_87, @constraintname_87
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_87+' drop constraint '+@constraintname_87)
       FETCH NEXT from refcursor into @reftable_87, @constraintname_87
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

    CONSTRAINT TEMAILPROCESSED_PK PRIMARY KEY(OBJECTID));
 
 
BEGIN
ALTER TABLE TCOMPUTEDVALUES
    ADD CONSTRAINT TCOMPUTEDVALUES_FK_1 FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TCOMPUTEDVALUES
    ADD CONSTRAINT TCOMPUTEDVALUES_FK_2 FOREIGN KEY (EFFORTTYPE)
    REFERENCES TEFFORTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TCOST
    ADD CONSTRAINT TCOST_FK_4 FOREIGN KEY (EFFORTTYPE)
    REFERENCES TEFFORTTYPE (OBJECTID)
END
;

BEGIN
ALTER TABLE TEFFORTTYPE
    ADD CONSTRAINT TEFFORTTYPE_FK_1 FOREIGN KEY (EFFORTUNIT)
    REFERENCES TEFFORTUNIT (OBJECTID)
END
;

BEGIN
ALTER TABLE TREPORTLAYOUT 
	DROP CONSTRAINT TREPORTLAYOUT_FK_4
END;

BEGIN
ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_4 FOREIGN KEY (REPORTFIELD)
    REFERENCES TFIELD (OBJECTID)
END
;


/* ---------------------------------------------------------------------- */
/* TMOTD                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TDASHBOARDSCREEN
    ADD CONSTRAINT TDASHBOARDSCREEN_FK_1 FOREIGN KEY (PERSONPKEY)
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




/* ---------------------------------------------------------------------- */
/* TFIELDCONFIG                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TROLEFIELD
    ADD CONSTRAINT TROLEFIELD_FK_1 FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
END
;

BEGIN
ALTER TABLE TROLEFIELD
    ADD CONSTRAINT TROLEFIELD_FK_2 FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
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




/* ---------------------------------------------------------------------- */
/* TQUERYREPOSITORY                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* TLOCALIZEDRESOURCES                                                      */
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

--# thanks to George Papastamatopoulos for submitting this ... and Marko Lahma for
--# updating it.
--#
--# In your Quartz properties file, you'll need to set 
--# org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.MSSQLDelegate
--#
--# you shouse enter your DB instance's name on the next line in place of "enter_db_name_here"
--#
--#
--# From a helpful (but anonymous) Quartz user:
--#
--# Regarding this error message:  
--#
--#     [Microsoft][SQLServer 2000 Driver for JDBC]Can't start a cloned connection while in manual transaction mode.
--#
--#
--#     I added "SelectMethod=cursor;" to my Connection URL in the config file. 
--#     It Seems to work, hopefully no side effects.
--#
--#		example:
--#		"jdbc:microsoft:sqlserver://dbmachine:1433;SelectMethod=cursor"; 
--#
--# Another user has pointed out that you will probably need to use the 
--# JTDS driver
--#


IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[FK_QRTZ_JOB_LISTENERS_QRTZ_JOB_DETAILS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
ALTER TABLE [dbo].[QRTZ_JOB_LISTENERS] DROP CONSTRAINT FK_QRTZ_JOB_LISTENERS_QRTZ_JOB_DETAILS
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
ALTER TABLE [dbo].[QRTZ_TRIGGERS] DROP CONSTRAINT FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
ALTER TABLE [dbo].[QRTZ_CRON_TRIGGERS] DROP CONSTRAINT FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
ALTER TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] DROP CONSTRAINT FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[FK_QRTZ_TRIGGER_LISTENERS_QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
ALTER TABLE [dbo].[QRTZ_TRIGGER_LISTENERS] DROP CONSTRAINT FK_QRTZ_TRIGGER_LISTENERS_QRTZ_TRIGGERS
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_CALENDARS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_CALENDARS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_CRON_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_CRON_TRIGGERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_BLOB_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_BLOB_TRIGGERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_FIRED_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_FIRED_TRIGGERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_PAUSED_TRIGGER_GRPS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_PAUSED_TRIGGER_GRPS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_SCHEDULER_STATE]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_SCHEDULER_STATE]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_LOCKS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_LOCKS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_JOB_DETAILS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_JOB_DETAILS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_JOB_LISTENERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_JOB_LISTENERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_SIMPLE_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_TRIGGER_LISTENERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_TRIGGER_LISTENERS]
GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
DROP TABLE [dbo].[QRTZ_TRIGGERS]
GO

CREATE TABLE [dbo].[QRTZ_CALENDARS] (
  [CALENDAR_NAME] [VARCHAR] (80)  NOT NULL ,
  [CALENDAR] [IMAGE] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_CRON_TRIGGERS] (
  [TRIGGER_NAME] [VARCHAR] (80)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (80)  NOT NULL ,
  [CRON_EXPRESSION] [VARCHAR] (80)  NOT NULL ,
  [TIME_ZONE_ID] [VARCHAR] (80) 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_FIRED_TRIGGERS] (
  [ENTRY_ID] [VARCHAR] (95)  NOT NULL ,
  [TRIGGER_NAME] [VARCHAR] (80)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (80)  NOT NULL ,
  [IS_VOLATILE] [VARCHAR] (1)  NOT NULL ,
  [INSTANCE_NAME] [VARCHAR] (80)  NOT NULL ,
  [FIRED_TIME] [BIGINT] NOT NULL ,
  [PRIORITY] [INTEGER] NOT NULL ,
  [STATE] [VARCHAR] (16)  NOT NULL,
  [JOB_NAME] [VARCHAR] (80)  NULL ,
  [JOB_GROUP] [VARCHAR] (80)  NULL ,
  [IS_STATEFUL] [VARCHAR] (1)  NULL ,
  [REQUESTS_RECOVERY] [VARCHAR] (1)  NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_PAUSED_TRIGGER_GRPS] (
  [TRIGGER_GROUP] [VARCHAR] (80)  NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_SCHEDULER_STATE] (
  [INSTANCE_NAME] [VARCHAR] (80)  NOT NULL ,
  [LAST_CHECKIN_TIME] [BIGINT] NOT NULL ,
  [CHECKIN_INTERVAL] [BIGINT] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_LOCKS] (
  [LOCK_NAME] [VARCHAR] (40)  NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_JOB_DETAILS] (
  [JOB_NAME] [VARCHAR] (80)  NOT NULL ,
  [JOB_GROUP] [VARCHAR] (80)  NOT NULL ,
  [DESCRIPTION] [VARCHAR] (120) NULL ,
  [JOB_CLASS_NAME] [VARCHAR] (128)  NOT NULL ,
  [IS_DURABLE] [VARCHAR] (1)  NOT NULL ,
  [IS_VOLATILE] [VARCHAR] (1)  NOT NULL ,
  [IS_STATEFUL] [VARCHAR] (1)  NOT NULL ,
  [REQUESTS_RECOVERY] [VARCHAR] (1)  NOT NULL ,
  [JOB_DATA] [IMAGE] NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_JOB_LISTENERS] (
  [JOB_NAME] [VARCHAR] (80)  NOT NULL ,
  [JOB_GROUP] [VARCHAR] (80)  NOT NULL ,
  [JOB_LISTENER] [VARCHAR] (80)  NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] (
  [TRIGGER_NAME] [VARCHAR] (80)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (80)  NOT NULL ,
  [REPEAT_COUNT] [BIGINT] NOT NULL ,
  [REPEAT_INTERVAL] [BIGINT] NOT NULL ,
  [TIMES_TRIGGERED] [BIGINT] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_BLOB_TRIGGERS] (
  [TRIGGER_NAME] [VARCHAR] (80)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (80)  NOT NULL ,
  [BLOB_DATA] [IMAGE] NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_TRIGGER_LISTENERS] (
  [TRIGGER_NAME] [VARCHAR] (80)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (80)  NOT NULL ,
  [TRIGGER_LISTENER] [VARCHAR] (80)  NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[QRTZ_TRIGGERS] (
  [TRIGGER_NAME] [VARCHAR] (80)  NOT NULL ,
  [TRIGGER_GROUP] [VARCHAR] (80)  NOT NULL ,
  [JOB_NAME] [VARCHAR] (80)  NOT NULL ,
  [JOB_GROUP] [VARCHAR] (80)  NOT NULL ,
  [IS_VOLATILE] [VARCHAR] (1)  NOT NULL ,
  [DESCRIPTION] [VARCHAR] (120) NULL ,
  [NEXT_FIRE_TIME] [BIGINT] NULL ,
  [PREV_FIRE_TIME] [BIGINT] NULL ,
  [PRIORITY] [INTEGER] NULL ,
  [TRIGGER_STATE] [VARCHAR] (16)  NOT NULL ,
  [TRIGGER_TYPE] [VARCHAR] (8)  NOT NULL ,
  [START_TIME] [BIGINT] NOT NULL ,
  [END_TIME] [BIGINT] NULL ,
  [CALENDAR_NAME] [VARCHAR] (80)  NULL ,
  [MISFIRE_INSTR] [SMALLINT] NULL ,
  [JOB_DATA] [IMAGE] NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_CALENDARS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_CALENDARS] PRIMARY KEY  CLUSTERED
  (
    [CALENDAR_NAME]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_CRON_TRIGGERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_CRON_TRIGGERS] PRIMARY KEY  CLUSTERED
  (
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_FIRED_TRIGGERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_FIRED_TRIGGERS] PRIMARY KEY  CLUSTERED
  (
    [ENTRY_ID]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_PAUSED_TRIGGER_GRPS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_PAUSED_TRIGGER_GRPS] PRIMARY KEY  CLUSTERED
  (
    [TRIGGER_GROUP]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_SCHEDULER_STATE] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_SCHEDULER_STATE] PRIMARY KEY  CLUSTERED
  (
    [INSTANCE_NAME]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_LOCKS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_LOCKS] PRIMARY KEY  CLUSTERED
  (
    [LOCK_NAME]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_JOB_DETAILS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_JOB_DETAILS] PRIMARY KEY  CLUSTERED
  (
    [JOB_NAME],
    [JOB_GROUP]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_JOB_LISTENERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_JOB_LISTENERS] PRIMARY KEY  CLUSTERED
  (
    [JOB_NAME],
    [JOB_GROUP],
    [JOB_LISTENER]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_SIMPLE_TRIGGERS] PRIMARY KEY  CLUSTERED
  (
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_TRIGGER_LISTENERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_TRIGGER_LISTENERS] PRIMARY KEY  CLUSTERED
  (
    [TRIGGER_NAME],
    [TRIGGER_GROUP],
    [TRIGGER_LISTENER]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_TRIGGERS] WITH NOCHECK ADD
  CONSTRAINT [PK_QRTZ_TRIGGERS] PRIMARY KEY  CLUSTERED
  (
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QRTZ_CRON_TRIGGERS] ADD
  CONSTRAINT [FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS] FOREIGN KEY
  (
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  ) REFERENCES [dbo].[QRTZ_TRIGGERS] (
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  ) ON DELETE CASCADE
GO

ALTER TABLE [dbo].[QRTZ_JOB_LISTENERS] ADD
  CONSTRAINT [FK_QRTZ_JOB_LISTENERS_QRTZ_JOB_DETAILS] FOREIGN KEY
  (
    [JOB_NAME],
    [JOB_GROUP]
  ) REFERENCES [dbo].[QRTZ_JOB_DETAILS] (
    [JOB_NAME],
    [JOB_GROUP]
  ) ON DELETE CASCADE
GO

ALTER TABLE [dbo].[QRTZ_SIMPLE_TRIGGERS] ADD
  CONSTRAINT [FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS] FOREIGN KEY
  (
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  ) REFERENCES [dbo].[QRTZ_TRIGGERS] (
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  ) ON DELETE CASCADE
GO

ALTER TABLE [dbo].[QRTZ_TRIGGER_LISTENERS] ADD
  CONSTRAINT [FK_QRTZ_TRIGGER_LISTENERS_QRTZ_TRIGGERS] FOREIGN KEY
  (
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  ) REFERENCES [dbo].[QRTZ_TRIGGERS] (
    [TRIGGER_NAME],
    [TRIGGER_GROUP]
  ) ON DELETE CASCADE
GO

ALTER TABLE [dbo].[QRTZ_TRIGGERS] ADD
  CONSTRAINT [FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS] FOREIGN KEY
  (
    [JOB_NAME],
    [JOB_GROUP]
  ) REFERENCES [dbo].[QRTZ_JOB_DETAILS] (
    [JOB_NAME],
    [JOB_GROUP]
  )
GO

INSERT INTO [dbo].[QRTZ_LOCKS] VALUES('TRIGGER_ACCESS');
INSERT INTO [dbo].[QRTZ_LOCKS] VALUES('JOB_ACCESS');
INSERT INTO [dbo].[QRTZ_LOCKS] VALUES('CALENDAR_ACCESS');
INSERT INTO [dbo].[QRTZ_LOCKS] VALUES('STATE_ACCESS');
INSERT INTO [dbo].[QRTZ_LOCKS] VALUES('MISFIRE_ACCESS');

----------------------------------------------------------------------------------------
-- populate
----------------------------------------------------------------------------------------

-- New for 3.4
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(54, 'TDASHBOARDSCREEN', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(55, 'TDASHBOARDTAB', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(56, 'TDASHBOARDPANEL', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(57, 'TDASHBOARDFIELD', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(58, 'TDASHBOARDPARAMETER', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(59, 'TFIELD', 50, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(60, 'TFIELDCONFIG', 50, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(61, 'TROLEFIELD', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(62, 'TCONFIGOPTIONSROLE', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(63, 'TTEXTBOXSETTINGS', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(64, 'TGENERALSETTINGS', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(65, 'TLIST', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(66, 'TOPTION', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(67, 'TOPTIONSETTINGS', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(68, 'TATTRIBUTEVALUE', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(69, 'TSCREEN', 10, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(70, 'TSCREENTAB', 10, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(71, 'TSCREENPANEL', 60, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(72, 'TSCREENFIELD', 200, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(73, 'TACTION', 10, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(74, 'TSCREENCONFIG', 10, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(75, 'TINITSTATE', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(76, 'TEVENT', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(77, 'TCLOB', 10, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(78, 'TNOTIFYFIELD', 100, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(79, 'TNOTIFYTRIGGER', 10, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(80, 'TNOTIFYSETTINGS', 10, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(81, 'TQUERYREPOSITORY', 10, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(82, 'TLOCALIZEDRESOURCES', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(83, 'TWORKITEMLINK', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(84, 'TLOGGINGLEVEL', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(85, 'TVERSIONCONTROLPARAMETER', 1, 1);			
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(86, 'TEXPORTTEMPLATE', 10, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(87, 'TEMAILPROCESSED', 1, 1);						
-- system fields
--Required system dropdowns
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (1, 'Project', 'com.aurel.track.fieldType.types.system.select.SystemSelectProject', 'N', 'N', 'Y');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (2, 'IssueType', 'com.aurel.track.fieldType.types.system.select.SystemSelectIssueType', 'N', 'N', 'Y');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (3, 'Subproject', 'com.aurel.track.fieldType.types.system.select.SystemSelectSubproject', 'N', 'N', 'Y');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (4, 'Status', 'com.aurel.track.fieldType.types.system.select.SystemSelectState', 'N', 'N', 'Y');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (5, 'Manager', 'com.aurel.track.fieldType.types.system.select.SystemSelectManager', 'N', 'N', 'Y');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (6, 'Responsible', 'com.aurel.track.fieldType.types.system.select.SystemSelectResponsible', 'N', 'N', 'Y');
--Not required system dropdowns
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (7, 'Class', 'com.aurel.track.fieldType.types.system.select.SystemSelectTheClass', 'N', 'N', 'N');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (8, 'ReleaseNoticed', 'com.aurel.track.fieldType.types.system.select.SystemSelectReleaseNoticed', 'N', 'N', 'N');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (9, 'ReleaseScheduled', 'com.aurel.track.fieldType.types.system.select.SystemSelectReleaseScheduled', 'N', 'N', 'N');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (10, 'Priority', 'com.aurel.track.fieldType.types.system.select.SystemSelectPriority', 'N', 'N', 'Y');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (11, 'Severity', 'com.aurel.track.fieldType.types.system.select.SystemSelectSeverity', 'N', 'N', 'N');
--System label fields
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (12, 'IssueNo', 'com.aurel.track.fieldType.types.system.text.SystemLabel', 'N', 'N', 'Y');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (13, 'Originator', 'com.aurel.track.fieldType.types.system.text.SystemOriginator', 'N', 'N', 'Y');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (14, 'CreateDate', 'com.aurel.track.fieldType.types.system.text.SystemLabelDate', 'N', 'N', 'Y');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (15, 'LastModifiedDate', 'com.aurel.track.fieldType.types.system.text.SystemLabelDate', 'N', 'N', 'Y');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (16, 'SuperiorWorkItem', 'com.aurel.track.fieldType.types.system.SystemParent', 'N', 'N', 'N');
--Required system textbox
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (17, 'Synopsis', 'com.aurel.track.fieldType.types.system.text.SystemSynopsis', 'N', 'N', 'Y');
--Not required system textbox
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (18, 'Build', 'com.aurel.track.fieldType.types.system.text.SystemTextBoxText', 'N', 'N', 'N');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (19, 'StartDate', 'com.aurel.track.fieldType.types.system.text.SystemTextBoxDate', 'N', 'N', 'N');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (20, 'EndDate', 'com.aurel.track.fieldType.types.system.text.SystemTextBoxDate', 'N', 'N', 'N');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (21, 'Description', 'com.aurel.track.fieldType.types.system.text.SystemDescriptionTextEditor', 'N', 'N', 'N');
--Not required system checkbox
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (22, 'AccessLevel', 'com.aurel.track.fieldType.types.system.check.SystemAccessLevel', 'N', 'N', 'N');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (23, 'Comment', 'com.aurel.track.fieldType.types.system.text.SystemCommentTextEditor', 'N', 'N', 'N');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (24, 'LastEditedBy', 'com.aurel.track.fieldType.types.system.text.SystemLastEditedBy', 'N', 'N', 'Y');

-- system field configs
--Configs for required system dropdowns
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (1, 1, 'Project', 'Project', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (2, 2, 'Issue Type', 'The type of issue', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (3, 3, 'Subproject', 'Subproject', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (4, 4, 'Status', 'Status', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (5, 5, 'Manager', 'Manager', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (6, 6, 'Responsible', 'Responsible', 'Y');
--Configs for not required system dropdowns
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (7, 7, 'Class', 'Class', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (8, 8, 'Affects Release', 'Release which is affected by this issue', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (9, 9, 'Fix Release', 'Release where this issue is resolved', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (10, 10, 'Priority', 'Priority', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (11, 11, 'Severity', 'Severity', 'Y');
--Configs for system label fields
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (12, 12, 'Issue No.', 'The issue identification number', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (13, 13, 'Originator', 'Originator', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (14, 14, 'Create Date', 'Created on', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (15, 15, 'Last Modified', 'Last modified on this date', 'Y');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (16, 16, 'Parent Issue', 'The parent issue', 'N');
--Configs for required system textbox
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (17, 17, 'Synopsis', 'Synopsis', 'Y');
--Configs for not required system textbox
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (18, 18, 'Build', 'Build', 'N');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (19, 19, 'Start Date', 'Start Date', 'N');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (20, 20, 'End Date', 'End Date', 'N');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (21, 21, 'Description', 'Description', 'N');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (22, 22, 'AccessLevel', 'Access Level', 'N');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (23, 23, 'Comment', 'Comment', 'N');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (24, 24, 'LastEditedBy', 'LastEditedBy', 'Y');
			
-- the actions: new,edit and extra action "Other"
INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION) VALUES (1,'New','New','New issue');
INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION) VALUES (2,'Edit','Edit','Edit issue');
INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION) VALUES (3,'Move','Move','Move issue');
INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION) VALUES (4,'NewChild','NewChild','New issue child');
INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION) VALUES (5,'ChangeStatus','ChangeStatus','Change status');
INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION) VALUES (6,'AddComment','AddComment','Add comment');

-- default screens
INSERT INTO TSCREEN (OBJECTID,NAME,LABEL,DESCRIPTION,OWNER) VALUES (1,'Default Form (NEW)','Default Mask (NEW)','The default input mask for creating new issues', 1);
INSERT INTO TSCREEN (OBJECTID,NAME,LABEL,DESCRIPTION,OWNER) VALUES (2,'Default Form (EDIT)','Default Mask (EDIT)','The default input mask for editing issues', 1);
INSERT INTO TSCREEN (OBJECTID,NAME,LABEL,DESCRIPTION,OWNER) VALUES (3,'Default Form (MOVE)','Default Mask (OTHER)','The default mask for other actions', 1);
INSERT INTO TSCREEN (OBJECTID,NAME,LABEL,DESCRIPTION,OWNER) VALUES (4,'Default Form (NEW CHILD)','Copy ofDefault Mask (NEW)','The default input mask for creating new issues', 1);
INSERT INTO TSCREEN (OBJECTID,NAME,LABEL,DESCRIPTION,OWNER) VALUES (5,'Default Form (CHANGE STATUS)','Copy ofDefault Mask (OTHER)','The default mask for other actions', 1);
INSERT INTO TSCREEN (OBJECTID,NAME,LABEL,DESCRIPTION,OWNER) VALUES (6,'Default Form (ADD COMMENT)','Add comment','Add comment to existing issue', 1);

-- default screenConfigs for action:new,edit,Move
INSERT INTO TSCREENCONFIG (OBJECTID,SCREEN,ACTIONKEY) VALUES (1,1,1);
INSERT INTO TSCREENCONFIG (OBJECTID,SCREEN,ACTIONKEY) VALUES (2,2,2);
INSERT INTO TSCREENCONFIG (OBJECTID,SCREEN,ACTIONKEY) VALUES (3,3,3);
INSERT INTO TSCREENCONFIG (OBJECTID,SCREEN,ACTIONKEY) VALUES (4,4,4);
INSERT INTO TSCREENCONFIG (OBJECTID,SCREEN,ACTIONKEY) VALUES (5,5,5);
INSERT INTO TSCREENCONFIG (OBJECTID,SCREEN,ACTIONKEY) VALUES (6,6,6);

--screenTab
INSERT INTO TSCREENTAB (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,PARENT) VALUES (1,'main','Main','The main tab',0,1);
INSERT INTO TSCREENTAB (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,PARENT) VALUES (2, 'main', 'Main', 'The main tab', 0, 2);
INSERT INTO TSCREENTAB (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,PARENT) VALUES (3, 'main', 'Main', 'The main tab', 0, 3);
INSERT INTO TSCREENTAB (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,PARENT) VALUES (4, 'main', 'Main', 'The main tab', 0, 4);
INSERT INTO TSCREENTAB (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,PARENT) VALUES (5, 'main', 'Main', 'The main tab', 0, 5);
INSERT INTO TSCREENTAB (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,PARENT) VALUES (6, 'main', 'Main', 'The main tab', 0, 6);

--  panels
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (30, 'Panel0', 'Panel0', 'Panel0 description', 0, 6, 3, 1);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (31, 'Panel1', 'Panel1', 'Panel1 description', 1, 2, 1, 1);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (32, 'Panel0', 'Panel0', 'Panel0 description', 0, 6, 3, 2);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (33, 'Panel1', 'Panel1', 'Panel1 description', 1, 2, 1, 2);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (34, 'Panel2', 'Panel2', 'Panel2 description', 2, 1, 1, 2);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (35, 'Panel2', 'Panel2', 'Panel2 description', 2, 1, 1, 1);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (36, 'Panel0', 'Panel0', 'Panel0 description', 0, 4, 3, 3);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (37, 'Panel0', 'Panel0', 'Panel0 description', 0, 6, 3, 4);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (38, 'Panel1', 'Panel1', 'Panel1 description', 1, 2, 1, 4);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (39, 'Panel2', 'Panel2', 'Panel2 description', 2, 1, 1, 4);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (40, 'Panel0', 'Panel0', 'Panel0 description', 0, 3, 3, 5);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (41, 'Panel1', 'Panel1', 'Panel1 description', 1, 1, 1, 5);
INSERT INTO TSCREENPANEL (OBJECTID,NAME,LABEL,DESCRIPTION,SORTORDER,ROWSNO,COLSNO,PARENT) VALUES (42, 'Panel1', 'Panel1', 'Panel1 description', 1, 2, 2, 6);

-- fields
INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (100, 'IssueNo', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 30, 12);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (101, 'CreateDate', NULL, NULL, 0, 1, 1, 1, 3, 2, 3, 2, 'N', 30, 14);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (102, 'Originator', NULL, NULL, 0, 2, 1, 1, 3, 2, 3, 2, 'N', 30, 13);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (103, 'Manager', NULL, NULL, 0, 3, 1, 1, 3, 2, 3, 2, 'N', 30, 5);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (104, 'Responsible', NULL, NULL, 0, 4, 1, 1, 3, 2, 3, 2, 'N', 30, 6);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (105, 'Subproject', NULL, NULL, 1, 1, 1, 1, 3, 2, 3, 2, 'N', 30, 3);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (106, 'Class', NULL, NULL, 1, 2, 1, 1, 3, 2, 3, 2, 'N', 30, 7);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (107, 'Release Noticed', NULL, NULL, 1, 3, 1, 1, 3, 2, 3, 2, 'N', 30, 8);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (108, 'Release Scheduled', NULL, NULL, 1, 4, 1, 1, 3, 2, 3, 2, 'N', 30, 9);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (109, 'Build', NULL, NULL, 1, 5, 1, 1, 3, 2, 3, 2, 'N', 30, 18);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (110, 'Priority', NULL, NULL, 2, 1, 1, 1, 3, 2, 3, 2, 'N', 30, 10);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (111, 'Severity', NULL, NULL, 2, 2, 1, 1, 3, 2, 3, 2, 'N', 30, 11);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (112, 'Start Date', NULL, NULL, 2, 3, 1, 1, 3, 2, 3, 2, 'N', 30, 19);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (113, 'End Date', NULL, NULL, 2, 4, 1, 1, 3, 2, 3, 2, 'N', 30, 20);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (114, 'Synopsis', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 31, 17);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (115, 'Description', NULL, NULL, 0, 1, 1, 1, 3, 2, 3, 2, 'N', 31, 21);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (116, 'IssueNo', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 32, 12);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (117, 'Create Date', NULL, NULL, 0, 1, 1, 1, 3, 2, 3, 2, 'N', 32, 14);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (118, 'Originator', NULL, NULL, 0, 2, 1, 1, 3, 2, 3, 2, 'N', 32, 13);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (119, 'Manager', NULL, NULL, 0, 3, 1, 1, 3, 2, 3, 2, 'N', 32, 5);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (120, 'Responsible', NULL, NULL, 0, 4, 1, 1, 3, 2, 3, 2, 'N', 32, 6);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (121, 'Subproject', NULL, NULL, 1, 1, 1, 1, 3, 2, 3, 2, 'N', 32, 3);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (122, 'Class', NULL, NULL, 1, 2, 1, 1, 3, 2, 3, 2, 'N', 32, 7);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (123, 'Release Noticed', NULL, NULL, 1, 3, 1, 1, 3, 2, 3, 2, 'N', 32, 8);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (124, 'Release Scheduled', NULL, NULL, 1, 4, 1, 1, 3, 2, 3, 2, 'N', 32, 9);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (125, 'Build', NULL, NULL, 1, 5, 1, 1, 3, 2, 3, 2, 'N', 32, 18);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (126, 'Priority', NULL, NULL, 2, 1, 1, 1, 3, 2, 3, 2, 'N', 32, 10);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (127, 'Severity', NULL, NULL, 2, 2, 1, 1, 3, 2, 3, 2, 'N', 32, 11);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (128, 'Start Date', NULL, NULL, 2, 3, 1, 1, 3, 2, 3, 2, 'N', 32, 19);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (129, 'End Date', NULL, NULL, 2, 4, 1, 1, 3, 2, 3, 2, 'N', 32, 20);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (130, 'Synopsis', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 33, 17);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (131, 'Description', '', NULL, 0, 1, 1, 1, 3, 0, 3, 2, 'N', 33, 21);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (132, 'SuperiorWorkItem', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 34, 16);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (133, 'SuperiorWorkItem', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 35, 16);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (134, 'Originator', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 36, 13);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (135, 'Manager', NULL, NULL, 0, 1, 1, 1, 3, 2, 3, 2, 'N', 36, 5);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (136, 'Responsible', NULL, NULL, 0, 2, 1, 1, 3, 2, 3, 2, 'N', 36, 6);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (137, 'Subproject', NULL, NULL, 1, 0, 1, 1, 3, 2, 3, 2, 'N', 36, 3);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (138, 'Class', NULL, NULL, 1, 1, 1, 1, 3, 2, 3, 2, 'N', 36, 7);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (139, 'Release Noticed', NULL, NULL, 1, 2, 1, 1, 3, 2, 3, 2, 'N', 36, 8);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (140, 'Release Scheduled', NULL, NULL, 1, 3, 1, 1, 3, 2, 3, 2, 'N', 36, 9);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (141, 'Status', NULL, NULL, 2, 0, 1, 1, 3, 2, 3, 2, 'N', 36, 4);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (142, 'Priority', NULL, NULL, 2, 1, 1, 1, 3, 2, 3, 2, 'N', 36, 10);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (143, 'Severity', NULL, NULL, 2, 2, 1, 1, 3, 2, 3, 2, 'N', 36, 11);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (144, 'IssueType', NULL, NULL, 2, 0, 1, 1, 3, 2, 3, 2, 'N', 30, 2);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (145, 'Project', NULL, NULL, 1, 0, 1, 1, 3, 2, 3, 2, 'N', 30, 1);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (146, 'Project', NULL, NULL, 1, 0, 1, 1, 3, 2, 3, 2, 'N', 32, 1);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (147, 'IssueType', NULL, NULL, 2, 0, 1, 1, 3, 2, 3, 2, 'N', 32, 2);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (148, 'Status', NULL, NULL, 2, 5, 1, 1, 3, 2, 3, 2, 'N', 30, 4);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (149, 'CreateDate', NULL, NULL, 0, 1, 1, 1, 3, 2, 3, 2, 'N', 37, 14);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (150, 'Originator', NULL, NULL, 0, 2, 1, 1, 3, 2, 3, 2, 'N', 37, 13);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (151, 'Manager', NULL, NULL, 0, 3, 1, 1, 3, 2, 3, 2, 'N', 37, 5);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (152, 'Responsible', NULL, NULL, 0, 4, 1, 1, 3, 2, 3, 2, 'N', 37, 6);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (153, 'Subproject', NULL, NULL, 1, 1, 1, 1, 3, 2, 3, 2, 'N', 37, 3);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (154, 'Class', NULL, NULL, 1, 2, 1, 1, 3, 2, 3, 2, 'N', 37, 7);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (155, 'Release Noticed', NULL, NULL, 1, 3, 1, 1, 3, 2, 3, 2, 'N', 37, 8);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (156, 'Release Scheduled', NULL, NULL, 1, 4, 1, 1, 3, 2, 3, 2, 'N', 37, 9);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (157, 'Build', NULL, NULL, 1, 5, 1, 1, 3, 2, 3, 2, 'N', 37, 18);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (158, 'Priority', NULL, NULL, 2, 1, 1, 1, 3, 2, 3, 2, 'N', 37, 10);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (159, 'Severity', NULL, NULL, 2, 2, 1, 1, 3, 2, 3, 2, 'N', 37, 11);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (160, 'Start Date', NULL, NULL, 2, 3, 1, 1, 3, 2, 3, 2, 'N', 37, 19);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (161, 'End Date', NULL, NULL, 2, 4, 1, 1, 3, 2, 3, 2, 'N', 37, 20);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (162, 'IssueType', NULL, NULL, 2, 0, 1, 1, 3, 2, 3, 2, 'N', 37, 2);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (163, 'Project', NULL, NULL, 1, 0, 1, 1, 3, 2, 3, 2, 'N', 37, 1);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (164, 'Status', NULL, NULL, 2, 5, 1, 1, 3, 2, 3, 2, 'N', 37, 4);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (165, 'Synopsis', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 38, 17);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (166, 'Description', NULL, NULL, 0, 1, 1, 1, 3, 2, 3, 2, 'N', 38, 21);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (167, 'SuperiorWorkItem', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 39, 16);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (169, 'Manager', NULL, NULL, 1, 1, 1, 1, 3, 2, 3, 2, 'N', 40, 5);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (170, 'Responsible', NULL, NULL, 2, 1, 1, 1, 3, 2, 3, 2, 'N', 40, 6);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (175, 'Status', NULL, NULL, 1, 2, 1, 1, 3, 2, 3, 2, 'N', 40, 4);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (178, 'IssueNo', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 40, 12);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (179, 'Synopsis', '', NULL, 1, 0, 2, 1, 3, 2, 3, 2, 'N', 40, 17);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (180, 'CreateDate', NULL, NULL, 0, 1, 1, 1, 3, 2, 3, 2, 'N', 40, 14);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (181, 'Originator', NULL, NULL, 0, 2, 1, 1, 3, 2, 3, 2, 'N', 40, 13);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (182, 'Issue No.', NULL, NULL, 0, 0, 1, 1, 3, 2, 3, 2, 'N', 42, 12);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (183, 'Synopsis', NULL, NULL, 1, 0, 1, 1, 3, 2, 3, 2, 'N', 42, 17);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (184, 'Comment', NULL, NULL, 0, 1, 2, 1, 3, 0, 3, 2, 'N', 42, 23);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (185, 'Comment', '', NULL, 0, 0, 1, 1, 3, 0, 3, 2, 'N', 41, 23);

INSERT INTO TSCREENFIELD (OBJECTID,NAME,DESCRIPTION,SORTORDER,COLINDEX,ROWINDEX,COLSPAN,ROWSPAN,LABELHALIGN,LABELVALIGN,VALUEHALIGN,VALUEVALIGN,ISEMPTY,PARENT,FIELDKEY)
VALUES (186, 'Status', NULL, NULL, 2, 5, 1, 1, 3, 2, 3, 2, 'N', 32, 4);

--default notification filters
INSERT INTO TCLOB (OBJECTID, CLOBVALUE) VALUES (1, '<QUERY><AND id="0" negate="false"></AND></QUERY>');
INSERT INTO TQUERYREPOSITORY (OBJECTID, PERSON, LABEL, QUERYTYPE, REPOSITORYTYPE, QUERYKEY) VALUES(1,1,'Default Filter',2,2,1);
--default notification triggers
INSERT INTO TNOTIFYTRIGGER (OBJECTID, LABEL, PERSON, ISSYSTEM) VALUES (1, 'Full Trigger', 1, 'Y');
INSERT INTO TNOTIFYTRIGGER (OBJECTID, LABEL, PERSON, ISSYSTEM) VALUES (2, 'Empty Trigger', 1, 'Y');
--notify field for new item
INSERT INTO TNOTIFYFIELD (OBJECTID, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (1, 1, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
--notify field for edit item fields
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (2, 1, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (3, 2, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (4, 3, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (5, 4, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (6, 5, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (7, 6, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (8, 7, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (9, 8, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (10, 9, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (11, 10, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (12, 11, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (13, 16, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (14, 17, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (15, 18, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (16, 19, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (17, 20, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (18, 21, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (19, 22, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (20, 23, 2, 1, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
-- notify field for edit budget fields
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (21, 1, 2, 2, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (22, 2, 2, 2, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
-- notify field for edit estimated remaining budget fields
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (23, 1, 2, 3, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (24, 2, 2, 3, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
-- notify field for edit expense fields
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (25, 1, 2, 4, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (26, 2, 2, 4, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');

--ExporTemplate
INSERT INTO TEXPORTTEMPLATE (OBJECTID,NAME,EXPORTFORMAT,REPOSITORYTYPE,DESCRIPTION,PROJECT,PERSON,REPORTTYPE) VALUES(1,'Grouped by Release','pdf',2,'Report grouped by release',NULL,1,'Jasper Report');
INSERT INTO TEXPORTTEMPLATE (OBJECTID,NAME,EXPORTFORMAT,REPOSITORYTYPE,DESCRIPTION,PROJECT,PERSON,REPORTTYPE) VALUES(2,'Grouped by Responsible','pdf',2,'Report grouped by responsible',NULL,1,'Jasper Report');
INSERT INTO TEXPORTTEMPLATE (OBJECTID,NAME,EXPORTFORMAT,REPOSITORYTYPE,DESCRIPTION,PROJECT,PERSON,REPORTTYPE) VALUES(3,'Detailed with history','pdf',2,'Detailed report including history',NULL,1,'Jasper Report');
INSERT INTO TEXPORTTEMPLATE (OBJECTID,NAME,EXPORTFORMAT,REPOSITORYTYPE,DESCRIPTION,PROJECT,PERSON,REPORTTYPE) VALUES(4,'Pie Chart: Issues by responsible','pdf',2,'Pie chart: issues by responsible',NULL,1,'Jasper Report');
INSERT INTO TEXPORTTEMPLATE (OBJECTID,NAME,EXPORTFORMAT,REPOSITORYTYPE,DESCRIPTION,PROJECT,PERSON,REPORTTYPE) VALUES(5,'Standard weekly report','pdf',2,'Weekly report w/o history',NULL,1,'Jasper Report');

--Symbols for Issue type
UPDATE TCATEGORY SET SYMBOL='problemReport.gif' WHERE PKEY=1;
UPDATE TCATEGORY SET SYMBOL='requirementChange.gif' WHERE PKEY=2;
UPDATE TCATEGORY SET SYMBOL='implementationError.gif' WHERE PKEY=3;
UPDATE TCATEGORY SET SYMBOL='workPackage.gif' WHERE PKEY=4;
UPDATE TCATEGORY SET SYMBOL='actionItem.gif' WHERE PKEY=5;
UPDATE TCATEGORY SET SYMBOL='milestone.gif' WHERE PKEY=6;
UPDATE TCATEGORY SET SYMBOL='risk.gif' WHERE PKEY=7;
UPDATE TCATEGORY SET SYMBOL='requirements.gif' WHERE PKEY=8;
UPDATE TCATEGORY SET SYMBOL='releaseNotes.gif' WHERE PKEY=9;

--Symbols for TSTATE
UPDATE TSTATE SET SYMBOL='opened.gif' WHERE PKEY=1;
UPDATE TSTATE SET SYMBOL='analyzed.gif' WHERE PKEY=2;
UPDATE TSTATE SET SYMBOL='assigned.gif' WHERE PKEY=3;
UPDATE TSTATE SET SYMBOL='suspended.gif' WHERE PKEY=4;
UPDATE TSTATE SET SYMBOL='processing.gif' WHERE PKEY=5;
UPDATE TSTATE SET SYMBOL='implemented.gif' WHERE PKEY=6;
UPDATE TSTATE SET SYMBOL='integrated.gif' WHERE PKEY=7;
UPDATE TSTATE SET SYMBOL='closed.gif' WHERE PKEY=8;
UPDATE TSTATE SET SYMBOL='assessing.gif' WHERE PKEY=9;

--Symbols for TPRIORITY
UPDATE TPRIORITY SET SYMBOL='occasionally.gif' WHERE PKEY=1;
UPDATE TPRIORITY SET SYMBOL='soon.gif' WHERE PKEY=2;
UPDATE TPRIORITY SET SYMBOL='immediate.gif' WHERE PKEY=3;

-- Symbols for severities
UPDATE TSEVERITY SET SYMBOL='minor.gif' WHERE PKEY=1;
UPDATE TSEVERITY SET SYMBOL='major.gif' WHERE PKEY=2;
UPDATE TSEVERITY SET SYMBOL='blocker.gif' WHERE PKEY=3;
