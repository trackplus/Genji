IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TDOMAIN')
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
         and tables.name = 'TDOMAIN'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_155, @constraintname_155
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_155+' drop constraint '+@constraintname_155)
       FETCH NEXT from refcursor into @reftable_155, @constraintname_155
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

ALTER TABLE TPERSON ADD HOURLYWAGE FLOAT NULL;
ALTER TABLE TPERSON ADD EXTRAHOURWAGE FLOAT NULL;  
ALTER TABLE TWORKITEM ADD OVERBUDGET CHAR (1) default 'N' NULL;
ALTER TABLE TSCREENFIELD ADD ICONRENDERING INT NULL;
ALTER TABLE TWORKITEMLINK ADD SORTORDER INT NULL;


ALTER TABLE TCATEGORY ADD TOOLTIP VARCHAR (255) NULL;
ALTER TABLE TDEPARTMENT ADD DOMAINKEY INT NULL;
BEGIN
ALTER TABLE TDEPARTMENT
    ADD CONSTRAINT TDEPARTMENT_FK_2 FOREIGN KEY (DOMAINKEY)
    REFERENCES TDOMAIN (OBJECTID)
END
;
ALTER TABLE TPRIORITY ADD TOOLTIP VARCHAR (255) NULL;
ALTER TABLE TPROJECT ADD SORTORDER INT NULL;
ALTER TABLE TPROJECT ADD ISTEMPLATE CHAR (1) default 'N' NULL;
ALTER TABLE TPROJECT ADD DOMAINKEY INT NULL;
CREATE  INDEX TPROJECTINDEX2 ON TPROJECT (SORTORDER);
BEGIN
ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_7 FOREIGN KEY (DOMAINKEY)
    REFERENCES TDOMAIN (OBJECTID)
END
;
ALTER TABLE TSEVERITY ADD TOOLTIP VARCHAR (255) NULL;
ALTER TABLE TSTATE ADD TOOLTIP VARCHAR (255) NULL;
ALTER TABLE TATTACHMENT ADD ISURL CHAR (1) default 'N' NULL;
ALTER TABLE TFIELD ADD PROJECTTYPE INT NULL;
ALTER TABLE TFIELD ADD PROJECT INT NULL;
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
ALTER TABLE TOPTION ADD TOOLTIP VARCHAR (255) NULL;
ALTER TABLE TSCREEN ADD PROJECTTYPE INT NULL;
ALTER TABLE TSCREEN ADD PROJECT INT NULL;
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

ALTER TABLE TDASHBOARDSCREEN ADD OWNER INT NULL;
BEGIN
ALTER TABLE TDASHBOARDSCREEN
    ADD CONSTRAINT TDASHBOARDSCREEN_FK_3 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
END
;

ALTER TABLE TQUERYREPOSITORY ADD SORTORDER INT NULL;
ALTER TABLE TQUERYREPOSITORY ADD VIEWID VARCHAR (255) NULL;
CREATE  INDEX TQUERYREPINDEX2 ON TQUERYREPOSITORY (SORTORDER);
ALTER TABLE TEXPORTTEMPLATE ADD PARENT INT NULL;
ALTER TABLE TEXPORTTEMPLATE ADD SORTORDER INT NULL;
CREATE  INDEX TEXPOPRTTEMPINDEX2 ON TEXPORTTEMPLATE (SORTORDER);
BEGIN
ALTER TABLE TEXPORTTEMPLATE
    ADD CONSTRAINT TEXPORTTEMPLATE_FK_4 FOREIGN KEY (PARENT)
    REFERENCES TEXPORTTEMPLATE (OBJECTID)
END
;
BEGIN
ALTER TABLE TREPORTPERSONSETTINGS 
	DROP CONSTRAINT TREPORTPERSONSETTINGS_FK_2
END;
BEGIN
ALTER TABLE TREPORTPERSONSETTINGS 
	DROP CONSTRAINT TREPORTPERSONSETTINGS_FK_3
END;
BEGIN
ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSONSETTINGS_FK_2 FOREIGN KEY (REPORTTEMPLATE)
    REFERENCES TEXPORTTEMPLATE (OBJECTID)
END
;
ALTER TABLE TREPORTPERSONSETTINGS DROP COLUMN RECURRENCEPATTERN;

ALTER TABLE TFILTERCATEGORY ADD SORTORDER INT NULL;
CREATE  INDEX TFILTERCATEGORYINDEX2 ON TFILTERCATEGORY (SORTORDER);
ALTER TABLE TREPORTCATEGORY ADD SORTORDER INT NULL;
CREATE  INDEX TREPORTCATEGORYINDEX2 ON TREPORTCATEGORY (SORTORDER);
ALTER TABLE TWORKFLOWTRANSITION ADD ELAPSEDTIME INT NULL;
ALTER TABLE TWORKFLOWTRANSITION ADD TIMEUNIT INT NULL;
ALTER TABLE TWORKFLOWACTIVITY ADD FIELDSETTERRELATION INT NULL;
ALTER TABLE TWORKFLOWACTIVITY ADD PARAMNAME VARCHAR (255) NULL;
ALTER TABLE TWORKFLOWACTIVITY ADD FIELDSETMODE INT NULL;
ALTER TABLE TWORKFLOWACTIVITY ADD SORTORDER INT NULL;
ALTER TABLE TREADISSUE ADD FIELDKEY INT NULL;
ALTER TABLE TREADISSUE ADD  LASTEDIT DATETIME NULL;
CREATE  INDEX TREADISSUEINDEX4 ON TREADISSUE (FIELDKEY);

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
         and tables.name = 'TREPORTSUBSCRIBE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_133, @constraintname_133
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_133+' drop constraint '+@constraintname_133)
       FETCH NEXT from refcursor into @reftable_133, @constraintname_133
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
         and tables.name = 'TGRIDLAYOUT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_134, @constraintname_134
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_134+' drop constraint '+@constraintname_134)
       FETCH NEXT from refcursor into @reftable_134, @constraintname_134
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
         and tables.name = 'TGRIDFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_135, @constraintname_135
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_135+' drop constraint '+@constraintname_135)
       FETCH NEXT from refcursor into @reftable_135, @constraintname_135
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
         and tables.name = 'TGRIDGROUPINGSORTING'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_136, @constraintname_136
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_136+' drop constraint '+@constraintname_136)
       FETCH NEXT from refcursor into @reftable_136, @constraintname_136
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
         and tables.name = 'TNAVIGATORLAYOUT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_137, @constraintname_137
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_137+' drop constraint '+@constraintname_137)
       FETCH NEXT from refcursor into @reftable_137, @constraintname_137
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
         and tables.name = 'TNAVIGATORGROUPINGSORTING'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_139, @constraintname_139
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_139+' drop constraint '+@constraintname_139)
       FETCH NEXT from refcursor into @reftable_139, @constraintname_139
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
         and tables.name = 'TCARDGROUPINGFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_141, @constraintname_141
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_141+' drop constraint '+@constraintname_141)
       FETCH NEXT from refcursor into @reftable_141, @constraintname_141
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
         and tables.name = 'TCARDFIELDOPTION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_142, @constraintname_142
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_142+' drop constraint '+@constraintname_142)
       FETCH NEXT from refcursor into @reftable_142, @constraintname_142
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
         and tables.name = 'TCARDPANEL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_152, @constraintname_152
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_152+' drop constraint '+@constraintname_152)
       FETCH NEXT from refcursor into @reftable_152, @constraintname_152
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
         and tables.name = 'TCARDFIELD'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_143, @constraintname_143
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_143+' drop constraint '+@constraintname_143)
       FETCH NEXT from refcursor into @reftable_143, @constraintname_143
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
         and tables.name = 'TVIEWPARAM'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_144, @constraintname_144
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_144+' drop constraint '+@constraintname_144)
       FETCH NEXT from refcursor into @reftable_144, @constraintname_144
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
         and tables.name = 'TGENERALPARAM'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_145, @constraintname_145
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_145+' drop constraint '+@constraintname_145)
       FETCH NEXT from refcursor into @reftable_145, @constraintname_145
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
         and tables.name = 'TVERSIONCONTROL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_146, @constraintname_146
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_146+' drop constraint '+@constraintname_146)
       FETCH NEXT from refcursor into @reftable_146, @constraintname_146
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
         and tables.name = 'TSHORTCUT'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_147, @constraintname_147
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_147+' drop constraint '+@constraintname_147)
       FETCH NEXT from refcursor into @reftable_147, @constraintname_147
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
         and tables.name = 'TMAILTEXTBLOCK'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_148, @constraintname_148
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_148+' drop constraint '+@constraintname_148)
       FETCH NEXT from refcursor into @reftable_148, @constraintname_148
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
         and tables.name = 'TPROLE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_149, @constraintname_149
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_149+' drop constraint '+@constraintname_149)
       FETCH NEXT from refcursor into @reftable_149, @constraintname_149
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
         and tables.name = 'TCHILDPROJECTTYPE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_150, @constraintname_150
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_150+' drop constraint '+@constraintname_150)
       FETCH NEXT from refcursor into @reftable_150, @constraintname_150
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
/* TPERSONINDOMAIN                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSONINDOMAIN_FK_1')
    ALTER TABLE TPERSONINDOMAIN DROP CONSTRAINT TPERSONINDOMAIN_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TPERSONINDOMAIN_FK_2')
    ALTER TABLE TPERSONINDOMAIN DROP CONSTRAINT TPERSONINDOMAIN_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TPERSONINDOMAIN')
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
         and tables.name = 'TPERSONINDOMAIN'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_156, @constraintname_156
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_156+' drop constraint '+@constraintname_156)
       FETCH NEXT from refcursor into @reftable_156, @constraintname_156
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
         and tables.name = 'TATTACHMENTVERSION'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_157, @constraintname_157
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_157+' drop constraint '+@constraintname_157)
       FETCH NEXT from refcursor into @reftable_157, @constraintname_157
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
         and tables.name = 'TUSERLEVEL'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_158, @constraintname_158
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_158+' drop constraint '+@constraintname_158)
       FETCH NEXT from refcursor into @reftable_158, @constraintname_158
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
         and tables.name = 'TUSERLEVELSETTING'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_159, @constraintname_159
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_159+' drop constraint '+@constraintname_159)
       FETCH NEXT from refcursor into @reftable_159, @constraintname_159
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
     DECLARE @reftable_161 nvarchar(60), @constraintname_161 nvarchar(60)
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
     FETCH NEXT from refcursor into @reftable_161, @constraintname_161
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_161+' drop constraint '+@constraintname_161)
       FETCH NEXT from refcursor into @reftable_161, @constraintname_161
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
     DECLARE @reftable_160 nvarchar(60), @constraintname_160 nvarchar(60)
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
     FETCH NEXT from refcursor into @reftable_160, @constraintname_160
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_160+' drop constraint '+@constraintname_160)
       FETCH NEXT from refcursor into @reftable_160, @constraintname_160
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

BEGIN
ALTER TABLE TWORKFLOWCOMMENT
    ADD CONSTRAINT TWORKFLOWCOMMENT_FK_1 FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOWDEF (OBJECTID)
END
;

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

BEGIN
ALTER TABLE TUSERLEVELSETTING
    ADD CONSTRAINT TUSERLEVELSETTING_FK_1 FOREIGN KEY (USERLEVEL)
    REFERENCES TUSERLEVEL (OBJECTID)
END
;

BEGIN
ALTER TABLE TUSERFEATURE
    ADD CONSTRAINT TUSERFEATURE_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;

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

GO