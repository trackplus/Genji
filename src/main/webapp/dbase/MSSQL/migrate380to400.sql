ALTER TABLE TCATEGORY ADD ICONCHANGED CHAR (1) default 'N' NULL;
ALTER TABLE TCATEGORY ADD CSSSTYLE VARCHAR (255) NULL;


UPDATE TCATEGORY SET SYMBOL='task.png' WHERE SYMBOL='task.gif';
UPDATE TCATEGORY SET SYMBOL='problemReport.png' WHERE SYMBOL='problemReport.gif';
UPDATE TCATEGORY SET SYMBOL='actionItem.png' WHERE SYMBOL='actionItem.gif';
UPDATE TCATEGORY SET SYMBOL='requirements.png' WHERE SYMBOL='requirements.gif';

UPDATE TPRIORITY SET SYMBOL='occasionally.png' WHERE SYMBOL='occasionally.gif';
UPDATE TPRIORITY SET SYMBOL='soon.png' WHERE SYMBOL='soon.gif';
UPDATE TPRIORITY SET SYMBOL='immediate.png' WHERE SYMBOL='immediate.gif';

UPDATE TSEVERITY SET SYMBOL='minor.png' WHERE SYMBOL='minor.gif';
UPDATE TSEVERITY SET SYMBOL='major.png' WHERE SYMBOL='major.gif';
UPDATE TSEVERITY SET SYMBOL='blocker.png' WHERE SYMBOL='blocker.gif';



ALTER TABLE TDEPARTMENT ADD PARENT INT NULL;

ALTER TABLE TNOTIFY ALTER COLUMN PROJCATKEY INT NULL;

ALTER TABLE TPERSON ADD SALT VARCHAR (80) NULL;
ALTER TABLE TPERSON ALTER COLUMN PASSWD varchar(160) NULL;
      
ALTER TABLE TPERSON ADD FORGOTPASSWORDKEY VARCHAR (100) NULL;
ALTER TABLE TPERSON ADD CREATED DATETIME NULL;
ALTER TABLE TPERSON ADD MAXASSIGNEDITEMS INT NULL;
ALTER TABLE TPERSON ADD MESSENGERURL VARCHAR (255) NULL;
ALTER TABLE TPERSON ADD CALLURL VARCHAR (255) NULL;
ALTER TABLE TPERSON ADD SYMBOL VARCHAR (255) NULL;
ALTER TABLE TPERSON ADD ICONKEY INT NULL;

ALTER TABLE TPRIORITY ADD ICONCHANGED CHAR (1) default 'N' NULL;
ALTER TABLE TPRIORITY ADD CSSSTYLE VARCHAR (255) NULL;

ALTER TABLE TPROJECT ADD TAGLABEL VARCHAR (50) NULL;
ALTER TABLE TPROJECT ALTER COLUMN PREFIX VARCHAR (50) NULL;
ALTER TABLE TPROJECT ADD NEXTITEMID INT NULL;
ALTER TABLE TPROJECT ADD PARENT INT NULL;
ALTER TABLE TPROJECT ADD ISPRIVATE VARCHAR (1) default 'N' NULL;

DROP  INDEX TPROJECTINDEXIDPF ON TPROJECT;

IF EXISTS (SELECT 1 FROM sysobjects WHERE name='TROLEFIELD_FK_2')
ALTER TABLE TROLEFIELD DROP CONSTRAINT TROLEFIELD_FK_2;


    
ALTER TABLE TRELEASE ADD PARENT INT NULL;

ALTER TABLE TSEVERITY ADD ICONCHANGED CHAR (1) default 'N' NULL;
ALTER TABLE TSEVERITY ADD CSSSTYLE VARCHAR (255) NULL;

ALTER TABLE TSTATE ADD ICONCHANGED CHAR (1) default 'N' NULL;
ALTER TABLE TSTATE ADD CSSSTYLE VARCHAR (255) NULL;




ALTER TABLE TWORKITEM ADD PROJECTKEY INT NULL;
GO
ALTER TABLE TWORKITEM ADD ESCALATIONLEVEL INT NULL;
ALTER TABLE TWORKITEM ADD WBSONLEVEL INT NULL;
ALTER TABLE TWORKITEM ADD REMINDERDATE DATETIME NULL;
ALTER TABLE TWORKITEM ADD TOPDOWNSTARTDATE DATETIME NULL;
ALTER TABLE TWORKITEM ADD TOPDOWNENDDATE DATETIME NULL;
GO


--set the projects directly in TWORKITEM
UPDATE TWORKITEM SET TWORKITEM.PROJECTKEY=(SELECT max(PROJKEY) FROM TPROJCAT WHERE TWORKITEM.PROJCATKEY=TPROJCAT.PKEY);
GO


--remove the not null constraint from TWORKITEM.PROJCATKEY
--by using a temorary column. Although neither the field, nor the foreign keys or indexes 
--are nedded any more in the future, reconstruct them with the same name to remain compatible  
--with the old Track+ versions regarding the generated constraint (forignn key and index) naming:
--older Track+ would have holes in generated constraint naming while the new Track+ installations not, 
--and the generated constarint naming would be not any more compatible for eventual further constraint drops
--The field can't be dropped (should be recreated) because the subsystem data should be migrated in custom lists.  
ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_5;
GO
DROP INDEX TWIINDEX11 ON TWORKITEM;
GO
ALTER TABLE TWORKITEM ADD PROJCATKEYTMP INT;
GO
UPDATE TWORKITEM SET PROJCATKEYTMP=PROJCATKEY;
GO
ALTER TABLE TWORKITEM DROP COLUMN PROJCATKEY;
ALTER TABLE TWORKITEM ADD PROJCATKEY INT NULL;
GO
UPDATE TWORKITEM SET PROJCATKEY=PROJCATKEYTMP;
GO
ALTER TABLE TWORKITEM DROP COLUMN PROJCATKEYTMP;
GO
ALTER TABLE TWORKITEM
	ADD CONSTRAINT TWORKITEM_FK_5 FOREIGN KEY (PROJCATKEY)
	REFERENCES TPROJCAT (PKEY);
GO
CREATE  INDEX TWIINDEX11 ON TWORKITEM (PROJCATKEY);
GO


ALTER TABLE TPROJECTTYPE ADD DEFAULTFORPRIVATE VARCHAR (1) default 'N' NULL;

ALTER TABLE TBUDGET ADD BUDGETTYPE INT NULL;


    
ALTER TABLE TSYSTEMSTATE ADD ICONKEY INT NULL;

ALTER TABLE TSYSTEMSTATE ADD CSSSTYLE VARCHAR (255) NULL;

ALTER TABLE TMOTD ADD TEASERTEXT VARCHAR (255) NULL;

ALTER TABLE TDASHBOARDSCREEN ALTER COLUMN PERSONPKEY INT NULL;
    
ALTER TABLE TFIELD ADD FILTERFIELD CHAR (1) default 'N' NULL;    
    

ALTER TABLE TFIELDCONFIG ADD GROOVYSCRIPT INT NULL;   
    


    
ALTER TABLE TTEXTBOXSETTINGS ADD DATEISWITHTIME CHAR (1) default 'N' NULL;    
    
UPDATE TLIST SET DESCRIPTION = LEFT(DESCRIPTION, 255);
ALTER TABLE TLIST ALTER  COLUMN DESCRIPTION VARCHAR (255) NULL;
ALTER TABLE TLIST ADD TAGLABEL VARCHAR (50) NULL;
ALTER TABLE TLIST ADD MOREPROPS VARCHAR (3000) NULL;   

ALTER TABLE TOPTION ADD SYMBOL VARCHAR (255) NULL;
ALTER TABLE TOPTION ADD ICONCHANGED CHAR (1) default 'N' NULL;
ALTER TABLE TOPTION ADD CSSSTYLE VARCHAR (255) NULL;


ALTER TABLE TSCREEN ADD TAGLABEL VARCHAR (50) NULL;

ALTER TABLE TACTION ADD ACTIONTYPE INT NULL;
ALTER TABLE TACTION ADD ICONKEY INT NULL;
BEGIN
ALTER TABLE TACTION
    ADD CONSTRAINT TACTION_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END;
    
ALTER TABLE TQUERYREPOSITORY ADD CATEGORYKEY INT NULL;

ALTER TABLE TLOCALIZEDRESOURCES ALTER COLUMN LOCALIZEDTEXT VARCHAR(7000) NULL;
ALTER TABLE TLOCALIZEDRESOURCES ADD   TEXTCHANGED CHAR (1) default 'N' NULL;



ALTER TABLE TEXPORTTEMPLATE ADD CATEGORYKEY INT NULL;
ALTER TABLE TEXPORTTEMPLATE ADD DELETED CHAR (1) default 'N' NULL;

ALTER TABLE CLUSTERNODE ADD RELOADCHANGES VARCHAR (255) NULL;    

ALTER TABLE TSCRIPTS  ADD  SCRIPTROLE INT NULL;

ALTER TABLE TREPORTPERSONSETTINGS ADD PARAMSETTINGS VARCHAR (3000) NULL;

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
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TFILTERCATEGORY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TFILTERCATEGORYINDEX1 ON TFILTERCATEGORY (TPUUID);




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
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREPORTCATEGORY_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREPORTCATEGORYINDEX1 ON TREPORTCATEGORY (TPUUID);




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
    MAILBODY VARCHAR (7000) NULL,
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
         and tables.name = 'TWORKFLOWGUARD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_124, @constraintname_124
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_124+' drop constraint '+@constraintname_124)
       FETCH NEXT from refcursor into @reftable_124, @constraintname_124
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
         and tables.name = 'TWORKFLOWCONNECT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_125, @constraintname_125
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_125+' drop constraint '+@constraintname_125)
       FETCH NEXT from refcursor into @reftable_125, @constraintname_125
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
/* TSLA                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TSLA')
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
         and tables.name = 'TSLA'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_126, @constraintname_126
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_126+' drop constraint '+@constraintname_126)
       FETCH NEXT from refcursor into @reftable_126, @constraintname_126
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
         and tables.name = 'TESCALATIONENTRY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_127, @constraintname_127
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_127+' drop constraint '+@constraintname_127)
       FETCH NEXT from refcursor into @reftable_127, @constraintname_127
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
         and tables.name = 'TESCALATIONSTATE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_128, @constraintname_128
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_128+' drop constraint '+@constraintname_128)
       FETCH NEXT from refcursor into @reftable_128, @constraintname_128
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
         and tables.name = 'TORGPROJECTSLA'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_129, @constraintname_129
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_129+' drop constraint '+@constraintname_129)
       FETCH NEXT from refcursor into @reftable_129, @constraintname_129
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
         and tables.name = 'TREADISSUE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_130, @constraintname_130
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_130+' drop constraint '+@constraintname_130)
       FETCH NEXT from refcursor into @reftable_130, @constraintname_130
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
    PERSON INT NOT NULL,
    READTYPE INT NULL,
    TPUUID VARCHAR (36) NULL,

    CONSTRAINT TREADISSUE_PK PRIMARY KEY(OBJECTID));

CREATE  INDEX TREADISSUEINDEX1 ON TREADISSUE (PERSON);
CREATE  INDEX TREADISSUEINDEX2 ON TREADISSUE (WORKITEM);
CREATE  INDEX TREADISSUEINDEX3 ON TREADISSUE (TPUUID);




/* ---------------------------------------------------------------------- */
/* TLASTEXECUTEDQUERY                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLASTEXECUTEDQUERY_FK_1')
    ALTER TABLE TLASTEXECUTEDQUERY DROP CONSTRAINT TLASTEXECUTEDQUERY_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TLASTEXECUTEDQUERY_FK_2')
    ALTER TABLE TLASTEXECUTEDQUERY DROP CONSTRAINT TLASTEXECUTEDQUERY_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLASTEXECUTEDQUERY')
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
         and tables.name = 'TLASTEXECUTEDQUERY'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_131, @constraintname_131
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_131+' drop constraint '+@constraintname_131)
       FETCH NEXT from refcursor into @reftable_131, @constraintname_131
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
         and tables.name = 'TGLOBALCSSSTYLE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_132, @constraintname_132
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_132+' drop constraint '+@constraintname_132)
       FETCH NEXT from refcursor into @reftable_132, @constraintname_132
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
/* TGLOBALCSSSTYLE                                                      */
/* ---------------------------------------------------------------------- */


BEGIN
ALTER TABLE TPERSON
    ADD CONSTRAINT TPERSON_FK_3 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;

BEGIN
ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_6 FOREIGN KEY (PARENT)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TRELEASE
    ADD CONSTRAINT TRELEASE_FK_3 FOREIGN KEY (PARENT)
    REFERENCES TRELEASE (PKEY)
END
;

BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_13 FOREIGN KEY (PROJECTKEY)
    REFERENCES TPROJECT (PKEY)
END
;

BEGIN
ALTER TABLE TSYSTEMSTATE
    ADD CONSTRAINT TSYSTEMSTATE_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
END
;

BEGIN
ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_5 FOREIGN KEY (GROOVYSCRIPT)
    REFERENCES TSCRIPTS (OBJECTID)
END
;

BEGIN
ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSITORY_FK_4 FOREIGN KEY (CATEGORYKEY)
    REFERENCES TFILTERCATEGORY (OBJECTID)
END
;

BEGIN
ALTER TABLE TEXPORTTEMPLATE
    ADD CONSTRAINT TEXPORTTEMPLATE_FK_3 FOREIGN KEY (CATEGORYKEY)
    REFERENCES TREPORTCATEGORY (OBJECTID)
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



INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (27, 'WBS', 'com.aurel.track.fieldType.types.system.text.SystemWBS', 'N', 'N', 'N');
GO
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (27, 27, 'WBS', 'WBS', 'N');
GO
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (28, 'ProjectSpecificIssueNo', 'com.aurel.track.fieldType.types.system.text.SystemProjectSpecificIssueNo', 'N', 'N', 'N');
GO
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (28, 28, 'ProjectSpecificIssueNo', 'ProjectSpecificIssueNo', 'N');
GO			
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (29, 'TargetStartDate', 'com.aurel.track.fieldType.types.system.text.SystemTextBoxTargetDate', 'N', 'N', 'N');
GO
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED, HISTORY)
			VALUES (29, 29, 'Start date target', 'Start date target (top-down)', 'N', 'Y');
GO
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (30, 'TargetEndDate', 'com.aurel.track.fieldType.types.system.text.SystemTextBoxTargetDate', 'N', 'N', 'N');
GO
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED, HISTORY)
			VALUES (30, 30, 'End date target', 'End date target (top-down)', 'N', 'Y');
GO












    