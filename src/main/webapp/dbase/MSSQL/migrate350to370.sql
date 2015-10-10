ALTER TABLE TACL ADD TPUUID VARCHAR(36);
CREATE  INDEX TACLINDEX1 ON TACL (TPUUID);

ALTER TABLE TBASELINE ADD TPUUID VARCHAR(36);
CREATE  INDEX TBASINDEX3 ON TBASELINE (TPUUID);

ALTER TABLE TCATEGORY ADD TPUUID VARCHAR(36);
CREATE  INDEX TCATEGORYINDEX1 ON TCATEGORY (TPUUID);

ALTER TABLE TCLASS ADD TPUUID VARCHAR(36);
CREATE  INDEX TCLASSINDEX1 ON TCLASS (TPUUID);

ALTER TABLE TDEPARTMENT ADD TPUUID VARCHAR(36);
CREATE  INDEX TDEPARTMENTINDEX1 ON TDEPARTMENT (TPUUID);

ALTER TABLE TNOTIFY ADD TPUUID VARCHAR(36);
CREATE  INDEX TNOTIFYINDEX1 ON TNOTIFY (TPUUID);

ALTER TABLE TPERSON ADD USERLEVEL INTEGER;
ALTER TABLE TPERSON ADD TPUUID VARCHAR(36);
CREATE  INDEX TPERSINDEX2 ON TPERSON (TPUUID);

ALTER TABLE TPRIORITY ADD TPUUID VARCHAR(36);
CREATE  INDEX TPRIOINDEX1 ON TPRIORITY (TPUUID);

ALTER TABLE TPPRIORITY ADD TPUUID VARCHAR(36);
CREATE  INDEX TPPRIOINDEX1 ON TPPRIORITY (TPUUID);

ALTER TABLE TPROJCAT ADD TPUUID VARCHAR(36);
CREATE  INDEX TPROJCATINDEX2 ON TPROJCAT (TPUUID);

ALTER TABLE TPROJECT ADD TPUUID VARCHAR(36);
CREATE  INDEX TPROJECTINDEX ON TPROJECT (TPUUID);

ALTER TABLE TRELEASE ADD TPUUID VARCHAR(36);
CREATE  INDEX TRELINDEX2 ON TRELEASE (TPUUID);

ALTER TABLE TROLE ADD TPUUID VARCHAR(36);
CREATE  INDEX TROLEINDEX1 ON TROLE (TPUUID);

ALTER TABLE TSEVERITY ADD TPUUID VARCHAR(36);
CREATE  INDEX TSEVINDEX1 ON TSEVERITY (TPUUID);

ALTER TABLE TPSEVERITY ADD TPUUID VARCHAR(36);
CREATE  INDEX TPSEVINDEX1 ON TPSEVERITY (TPUUID);

ALTER TABLE TSTATE ADD TPUUID VARCHAR(36);
CREATE  INDEX TSTATEINDEX1 ON TSTATE (TPUUID);

ALTER TABLE TSTATECHANGE ADD TPUUID VARCHAR(36);
CREATE  INDEX TSTATECHANGEINDEX4 ON TSTATECHANGE (TPUUID);

ALTER TABLE TTRAIL ADD TPUUID VARCHAR(36);
CREATE  INDEX TTRAILINDEX3 ON TTRAIL (TPUUID);

ALTER TABLE TWORKITEM ADD ARCHIVELEVEL INTEGER;
ALTER TABLE TWORKITEM ADD TPUUID VARCHAR(36);
CREATE  INDEX TWIINDEX19 ON TWORKITEM (ARCHIVELEVEL);
CREATE  INDEX TWIINDEX20 ON TWORKITEM (TPUUID);

ALTER TABLE TCOMPUTEDVALUES ADD TPUUID VARCHAR(36);
CREATE  INDEX TCVINDEX1 ON TCOMPUTEDVALUES (TPUUID);

ALTER TABLE TPRIVATEREPORTREPOSITORY ADD TPUUID VARCHAR(36);
CREATE  INDEX TPRIVREPREPINDEX1 ON TPRIVATEREPORTREPOSITORY (TPUUID);

ALTER TABLE TPUBLICREPORTREPOSITORY ADD TPUUID VARCHAR(36);
CREATE  INDEX TPUBREPREPINDEX1 ON TPUBLICREPORTREPOSITORY (TPUUID);

ALTER TABLE TPROJECTREPORTREPOSITORY ADD TPUUID VARCHAR(36);
CREATE  INDEX TPROJREPREPINDEX1 ON TPROJECTREPORTREPOSITORY (TPUUID);

ALTER TABLE TACCOUNT ADD TPUUID VARCHAR(36);
CREATE  INDEX TACCOUNTINDEX1 ON TACCOUNT (TPUUID);

ALTER TABLE TATTACHMENT ADD TPUUID VARCHAR(36);
CREATE  INDEX TATTACHINDEX1 ON TATTACHMENT (TPUUID);

ALTER TABLE TCOST ADD TPUUID VARCHAR(36);
CREATE  INDEX TCOSTINDEX1 ON TCOST (TPUUID);

ALTER TABLE TEFFORTTYPE ADD TPUUID VARCHAR(36);
CREATE  INDEX TEFFORTTYPEINDEX1 ON TEFFORTTYPE (TPUUID);

ALTER TABLE TEFFORTUNIT ADD TPUUID VARCHAR(36);
CREATE  INDEX TEFFORTUNITINDEX1 ON TEFFORTUNIT (TPUUID);

ALTER TABLE TDOCSTATE ADD TPUUID VARCHAR(36);
CREATE  INDEX TDOCSTATEINDEX1 ON TDOCSTATE (TPUUID);

ALTER TABLE TDISABLEFIELD ADD TPUUID VARCHAR(36);
CREATE  INDEX TDISABLEFINDEX1 ON TDISABLEFIELD (TPUUID);

ALTER TABLE TPLISTTYPE ADD TPUUID VARCHAR(36);
CREATE  INDEX TPLISTTYPEINDEX1 ON TPLISTTYPE (TPUUID);

ALTER TABLE TPROJECTTYPE ADD TPUUID VARCHAR(36);
CREATE  INDEX TPROJTYPEINDEX1 ON TPROJECTTYPE (TPUUID);

ALTER TABLE TPSTATE ADD TPUUID VARCHAR(36);
CREATE  INDEX TPSTATEINDEX1 ON TPSTATE (TPUUID);

ALTER TABLE TSITE ADD TPUUID VARCHAR(36);
CREATE  INDEX TSITEINDEX1 ON TSITE (TPUUID);

ALTER TABLE TWORKFLOW ADD TPUUID VARCHAR(36);
CREATE  INDEX TWORKFLOWINDEX1 ON TWORKFLOW (TPUUID);

ALTER TABLE TWORKFLOWROLE ADD TPUUID VARCHAR(36);
CREATE  INDEX TWORKFLOWROLEINDEX1 ON TWORKFLOWROLE (TPUUID);

ALTER TABLE TWORKFLOWCATEGORY ADD TPUUID VARCHAR(36);
CREATE  INDEX TWORKFLOWCATINDEX1 ON TWORKFLOWCATEGORY (TPUUID);

ALTER TABLE TROLELISTTYPE ADD TPUUID VARCHAR(36);
CREATE  INDEX TROLELISTTINDEX1 ON TROLELISTTYPE (TPUUID);

ALTER TABLE TISSUEATTRIBUTEVALUE ADD TPUUID VARCHAR(36);
CREATE  INDEX TISSUEATTINDEX1 ON TISSUEATTRIBUTEVALUE (TPUUID);

ALTER TABLE TATTRIBUTEOPTION ADD TPUUID VARCHAR(36);
CREATE  INDEX TATTOPTINDEX1 ON TATTRIBUTEOPTION (TPUUID);

ALTER TABLE TATTRIBUTECLASS ADD TPUUID VARCHAR(36);
CREATE  INDEX TATTCLASSINDEX1 ON TATTRIBUTECLASS (TPUUID);

ALTER TABLE TATTRIBUTETYPE ADD TPUUID VARCHAR(36);
CREATE  INDEX TATTTYPEINDEX1 ON TATTRIBUTETYPE (TPUUID);

ALTER TABLE TATTRIBUTE ADD TPUUID VARCHAR(36);
CREATE  INDEX TATTRIBUTEINDEX1 ON TATTRIBUTE (TPUUID);

ALTER TABLE TREPORTLAYOUT ADD EXPANDING VARCHAR (1);
ALTER TABLE TREPORTLAYOUT ADD LAYOUTKEY INTEGER;
ALTER TABLE TREPORTLAYOUT ADD TPUUID VARCHAR(36);
CREATE  INDEX TREPORTLAYOUTINDEX1 ON TREPORTLAYOUT (FIELDTYPE);
CREATE  INDEX TREPORTLAYOUTINDEX2 ON TREPORTLAYOUT (LAYOUTKEY);
CREATE  INDEX TREPORTLAYOUTINDEX3 ON TREPORTLAYOUT (TPUUID);

ALTER TABLE TSCHEDULER ADD TPUUID VARCHAR(36);
CREATE  INDEX TSCHEDULERINDEX1 ON TSCHEDULER (TPUUID);

ALTER TABLE TPROJECTACCOUNT ADD TPUUID VARCHAR(36);
CREATE  INDEX TPROJACCINDEX1 ON TPROJECTACCOUNT (TPUUID);

ALTER TABLE TGROUPMEMBER ADD TPUUID VARCHAR(36);
CREATE  INDEX TGROUPMEMINDEX1 ON TGROUPMEMBER (TPUUID);

ALTER TABLE TBUDGET ADD TPUUID VARCHAR(36);
CREATE  INDEX TBUDGETINDEX3 ON TBUDGET (TPUUID);

ALTER TABLE TACTUALESTIMATEDBUDGET ADD TPUUID VARCHAR(36);
CREATE  INDEX TACTUALESTINDEX3 ON TACTUALESTIMATEDBUDGET (TPUUID);

ALTER TABLE TSYSTEMSTATE ADD TPUUID VARCHAR(36);
CREATE  INDEX TSYSTEMSTATEINDEX1 ON TSYSTEMSTATE (TPUUID);

ALTER TABLE TCOSTCENTER ADD TPUUID VARCHAR(36);
CREATE  INDEX TCOSTCENTERINDEX1 ON TCOSTCENTER (TPUUID);

ALTER TABLE TMOTD ADD TPUUID VARCHAR(36);
CREATE  INDEX TMOTDINDEX1 ON TMOTD (TPUUID);

ALTER TABLE TDASHBOARDSCREEN ADD TPUUID VARCHAR(36);
CREATE  INDEX TDBSCREENINDEX1 ON TDASHBOARDSCREEN (TPUUID);

ALTER TABLE TDASHBOARDTAB ADD TPUUID VARCHAR(36);
CREATE  INDEX TDBTABINDEX1 ON TDASHBOARDTAB (TPUUID);

ALTER TABLE TDASHBOARDPANEL ADD TPUUID VARCHAR(36);
CREATE  INDEX TDBPANELINDEX1 ON TDASHBOARDPANEL (TPUUID);

ALTER TABLE TDASHBOARDFIELD ADD TPUUID VARCHAR(36);
CREATE  INDEX TDBFIELDINDEX1 ON TDASHBOARDFIELD (TPUUID);

ALTER TABLE TDASHBOARDPARAMETER ADD TPUUID VARCHAR(36);
CREATE  INDEX TDBPARAMINDEX1 ON TDASHBOARDPARAMETER (TPUUID);

ALTER TABLE TVERSIONCONTROLPARAMETER ADD TPUUID VARCHAR(36);
CREATE  INDEX TVCPARAMINDEX1 ON TVERSIONCONTROLPARAMETER (TPUUID);

ALTER TABLE TFIELD ADD TPUUID VARCHAR(36);
CREATE  INDEX TFIELDINDEX1 ON TFIELD (TPUUID);

ALTER TABLE TFIELDCONFIG ADD TPUUID VARCHAR(36);
CREATE  INDEX TFIELDCONFIGINDEX1 ON TFIELDCONFIG (TPUUID);

ALTER TABLE TROLEFIELD ADD TPUUID VARCHAR(36);
CREATE  INDEX TROLEFIELDINDEX1 ON TROLEFIELD (TPUUID);

ALTER TABLE TCONFIGOPTIONSROLE ADD TPUUID VARCHAR(36);
CREATE  INDEX TCONFIGOPTINDEX1 ON TCONFIGOPTIONSROLE (TPUUID);

ALTER TABLE TTEXTBOXSETTINGS ADD TPUUID VARCHAR(36);
CREATE  INDEX TTEXTBOXCONFINDEX1 ON TTEXTBOXSETTINGS (TPUUID);

ALTER TABLE TGENERALSETTINGS ADD TPUUID VARCHAR(36);
CREATE  INDEX TGENERALCONFINDEX1 ON TGENERALSETTINGS (TPUUID);

ALTER TABLE TLIST ADD TPUUID VARCHAR(36);
CREATE  INDEX TLISTINDEX1 ON TLIST (TPUUID);

ALTER TABLE TOPTION ADD TPUUID VARCHAR(36);
CREATE  INDEX TOPTIONINDEX1 ON TOPTION (TPUUID);

ALTER TABLE TOPTIONSETTINGS ADD TPUUID VARCHAR(36);
CREATE  INDEX TOPTIONCONFINDEX1 ON TOPTIONSETTINGS (TPUUID);

ALTER TABLE TATTRIBUTEVALUE ADD TPUUID VARCHAR(36);
CREATE  INDEX TATTVALUEINDEX1 ON TATTRIBUTEVALUE (TPUUID);

ALTER TABLE TSCREEN ADD TPUUID VARCHAR(36);
CREATE  INDEX TSCREENINDEX1 ON TSCREEN (TPUUID);

ALTER TABLE TSCREENTAB ADD TPUUID VARCHAR(36);
CREATE  INDEX TSCREENTABINDEX1 ON TSCREENTAB (TPUUID);

ALTER TABLE TSCREENPANEL ADD TPUUID VARCHAR(36);
CREATE  INDEX TSCREENPANELINDEX1 ON TSCREENPANEL (TPUUID);

ALTER TABLE TSCREENFIELD ADD TPUUID VARCHAR(36);
CREATE  INDEX TSCREENFIELDINDEX1 ON TSCREENFIELD (TPUUID);

ALTER TABLE TACTION ADD TPUUID VARCHAR(36);
CREATE  INDEX TACTIONINDEX1 ON TACTION (TPUUID);

ALTER TABLE TSCREENCONFIG ADD TPUUID VARCHAR(36);
CREATE  INDEX TSCREENCONFINDEX1 ON TSCREENCONFIG (TPUUID);

ALTER TABLE TINITSTATE ADD TPUUID VARCHAR(36);
CREATE  INDEX TINITSTATEINDEX1 ON TINITSTATE (TPUUID);

ALTER TABLE TEVENT ADD TPUUID VARCHAR(36);
CREATE  INDEX TEVENTINDEX1 ON TEVENT (TPUUID);

ALTER TABLE TCLOB ADD TPUUID VARCHAR(36);
CREATE  INDEX TCLOBINDEX1 ON TCLOB (TPUUID);

ALTER TABLE TNOTIFYFIELD ADD TPUUID VARCHAR(36);
CREATE  INDEX TNOTIFYFIELDINDEX1 ON TNOTIFYFIELD (TPUUID);

ALTER TABLE TNOTIFYTRIGGER ADD TPUUID VARCHAR(36);
CREATE  INDEX TNOTIFYTRIGGERINDEX1 ON TNOTIFYTRIGGER (TPUUID);

ALTER TABLE TNOTIFYSETTINGS ADD TPUUID VARCHAR(36);
CREATE  INDEX TNOTIFYCONFINDEX1 ON TNOTIFYSETTINGS (TPUUID);

ALTER TABLE TQUERYREPOSITORY ADD TPUUID VARCHAR(36);
CREATE  INDEX TQUERYREPINDEX1 ON TQUERYREPOSITORY (TPUUID);

ALTER TABLE TLOCALIZEDRESOURCES ADD TPUUID VARCHAR(36);
CREATE  INDEX TLOCALRESINDEX1 ON TLOCALIZEDRESOURCES (TPUUID);

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


ALTER TABLE TLOGGINGLEVEL ADD TPUUID VARCHAR(36);
CREATE  INDEX TLOGLEVELINDEX1 ON TLOGGINGLEVEL (TPUUID);

ALTER TABLE TWORKITEMLOCK ADD TPUUID VARCHAR(36);
CREATE  INDEX TWORKITEMLOCKINDEX1 ON TWORKITEMLOCK (TPUUID);

ALTER TABLE TEXPORTTEMPLATE ADD TPUUID VARCHAR(36);
CREATE  INDEX TEXPOPRTTEMPINDEX1 ON TEXPORTTEMPLATE (TPUUID);

ALTER TABLE TEMAILPROCESSED ADD TPUUID VARCHAR(36);
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
    ENTITYKEY INT NOT NULL,
    ENTITYTYPE INT NOT NULL,
    CLUSTERNODE INT NULL,

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


ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_1 FOREIGN KEY (LINKPRED)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_2 FOREIGN KEY (LINKSUCC)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_3 FOREIGN KEY (LINKTYPE)
    REFERENCES TLINKTYPE (OBJECTID)
;

ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_4 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TLOGGEDINUSERS
    ADD CONSTRAINT TLOGGEDINUSERS_FK_1 FOREIGN KEY (LOGGEDUSER)
    REFERENCES TPERSON (PKEY);

ALTER TABLE TLOGGEDINUSERS
    ADD CONSTRAINT TLOGGEDINUSERS_FK_2 FOREIGN KEY (NODEADDRESS)
    REFERENCES CLUSTERNODE (OBJECTID);

ALTER TABLE TENTITYCHANGES
    ADD CONSTRAINT TENTITYCHANGES_FK_1 FOREIGN KEY (CLUSTERNODE)
    REFERENCES CLUSTERNODE (OBJECTID);

ALTER TABLE TSUMMARYMAIL
    ADD CONSTRAINT TSUMMARYMAIL_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY);

ALTER TABLE TSUMMARYMAIL
    ADD CONSTRAINT TSUMMARYMAIL_FK_2 FOREIGN KEY (PERSONFROM)
    REFERENCES TPERSON (PKEY);

ALTER TABLE TSUMMARYMAIL
    ADD CONSTRAINT TSUMMARYMAIL_FK_3 FOREIGN KEY (PERSONTO)
    REFERENCES TPERSON (PKEY);

ALTER TABLE TOUTLINECODE
    ADD CONSTRAINT TOUTLINECODE_FK_1 FOREIGN KEY (OUTLINETEMPLATE)
    REFERENCES TOUTLINETEMPLATE (OBJECTID);

ALTER TABLE TOUTLINETEMPLATEDEF
    ADD CONSTRAINT TOUTLINETEMPLATEDEF_FK_1 FOREIGN KEY (OUTLINETEMPLATE)
    REFERENCES TOUTLINETEMPLATE (OBJECTID);

ALTER TABLE THISTORYTRANSACTION
    ADD CONSTRAINT THISTORYTRANSACTION_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE THISTORYTRANSACTION
    ADD CONSTRAINT THISTORYTRANSACTION_FK_2 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_1 FOREIGN KEY (HISTORYTRANSACTION)
    REFERENCES THISTORYTRANSACTION (OBJECTID)
;

ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_2 FOREIGN KEY (PARENTCOMMENT)
    REFERENCES TFIELDCHANGE (OBJECTID)
;

ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_3 FOREIGN KEY (NEWCUSTOMOPTIONID)
    REFERENCES TOPTION (OBJECTID)
;

ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_4 FOREIGN KEY (OLDCUSTOMOPTIONID)
    REFERENCES TOPTION (OBJECTID)
;

ALTER TABLE TSCRIPTS
    ADD CONSTRAINT TSCRIPTS_FK_1 FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TSCRIPTS
    ADD CONSTRAINT TSCRIPTS_FK_2 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TSCRIPTS
    ADD CONSTRAINT TSCRIPTS_FK_3 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TREVISION
    ADD CONSTRAINT TREVISION_FK_1 FOREIGN KEY (REPOSITORYKEY)
    REFERENCES TREPOSITORY (OBJECTID)
;

ALTER TABLE TREVISIONWORKITEMS
    ADD CONSTRAINT TREVISIONWORKITEMS_FK_1 FOREIGN KEY (REVISIONKEY)
    REFERENCES TREVISION (OBJECTID)
;

ALTER TABLE TTEMPLATEPERSON
    ADD CONSTRAINT TTEMPLATEPERSON_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TTEMPLATEPERSON
    ADD CONSTRAINT TTEMPLATEPERSON_FK_2 FOREIGN KEY (REPORTTEMPLATE)
    REFERENCES TEXPORTTEMPLATE (OBJECTID)
;

ALTER TABLE TCATEGORY ADD ICONKEY INTEGER;
ALTER TABLE TPRIORITY ADD ICONKEY INTEGER;
ALTER TABLE TSEVERITY ADD ICONKEY INTEGER;
ALTER TABLE TSTATE ADD ICONKEY INTEGER;
ALTER TABLE TOPTION ADD ICONKEY INTEGER;

-- ALTER TABLE TLINKTYPE ADD OUTWARDICONKEY INTEGER;
-- ALTER TABLE TLINKTYPE ADD INWARDICONKEY INTEGER;

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


ALTER TABLE TCATEGORY
    ADD CONSTRAINT TCATEGORY_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TPRIORITY
    ADD CONSTRAINT TPRIORITY_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TSEVERITY
    ADD CONSTRAINT TSEVERITY_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TSTATE
    ADD CONSTRAINT TSTATE_FK_1 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TOPTION
    ADD CONSTRAINT TOPTION_FK_2 FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TLINKTYPE
    ADD CONSTRAINT TLINKTYPE_FK_1 FOREIGN KEY (OUTWARDICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TLINKTYPE
    ADD CONSTRAINT TLINKTYPE_FK_2 FOREIGN KEY (INWARDICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TPROJECT ADD PREFIX VARCHAR (10);
ALTER TABLE TPROJECT ADD LASTID INTEGER;

CREATE  INDEX TPROJECTINDEXIDPF ON TPROJECT (PREFIX);

ALTER TABLE TWORKITEM ADD IDNUMBER INTEGER;

CREATE  INDEX TWIINDEX21 ON TWORKITEM (IDNUMBER);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(90, 'TAPPLICATIONCONTEXT', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(91, 'TLOGGEDINUSERS', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(92, 'TEMAILCONNECTIONPOINT', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(93, 'CLUSTERNODE', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(94, 'TSUMMARYMAIL', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(95, 'TOUTLINECODE', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(96, 'TOUTLINETEMPLATEDEF', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(97, 'TOUTLINETEMPLATE', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(98, 'THISTORYTRANSACTION', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(99, 'TFIELDCHANGE', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(100, 'TSCRIPTS', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(101, 'TREVISION', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(102, 'TREVISIONWORKITEMS', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(103, 'TREPOSITORY', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(104, 'TTEMPLATEPERSON', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(105, 'TBLOB', 1, 1);

INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (25, 'ArchiveLevel', 'com.aurel.track.fieldType.types.system.text.SystemArchiveLevel', 'N', 'N', 'N');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (25, 25, 'ArchiveLevel', 'ArchiveLevel', 'N');
INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)
			VALUES (26, 'SubmitterEmail', 'com.aurel.track.fieldType.types.system.text.SystemSubmitterEmail', 'N', 'N', 'N');
INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED)
			VALUES (26, 26, 'SubmitterEmail', 'SubmitterEmail', 'N');

GO

INSERT INTO TLINKTYPE (OBJECTID, NAME, REVERSENAME, LEFTTORIGHTFIRST, RIGHTTOLEFTFIRST, LINKDIRECTION, LINKTYPEPLUGIN) 
	VALUES	(1, 'duplicate of', 'duplicated by', 'duplicates of these issues', 'duplicated by these issues', 3, 'com.aurel.track.linkType.GenericLinkType');
INSERT INTO TLINKTYPE (OBJECTID, NAME, REVERSENAME, LEFTTORIGHTFIRST, RIGHTTOLEFTFIRST, LINKDIRECTION, LINKTYPEPLUGIN) 
	VALUES	(2, 'test case for', 'verified by', 'test cases for these issues', 'verified by these issues', 3, 'com.aurel.track.linkType.GenericLinkType');
