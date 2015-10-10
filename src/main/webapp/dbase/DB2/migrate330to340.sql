-- Remove report layout
DELETE FROM TREPORTLAYOUT;
UPDATE TWORKITEM SET SUPERIORWORKITEM=NULL WHERE SUPERIORWORKITEM=0;

ALTER TABLE TCATEGORY ADD COLUMN SYMBOL VARCHAR(255);

ALTER TABLE TPERSON ALTER LOGINNAME SET DATA TYPE VARCHAR(60); 

ALTER TABLE TWORKITEM ADD COLUMN TASKISMILESTONE VARCHAR(1) default 'N';
ALTER TABLE TWORKITEM ADD COLUMN TASKISSUBPROJECT VARCHAR(1) default 'N';
ALTER TABLE TWORKITEM ADD COLUMN TASKISSUMMARY VARCHAR(1) default 'N';
ALTER TABLE TWORKITEM ADD COLUMN TASKCONSTRAINT INTEGER;
ALTER TABLE TWORKITEM ADD COLUMN TASKCONSTRAINTDATE TIMESTAMP;
ALTER TABLE TWORKITEM ADD COLUMN PSPCODE VARCHAR(255);

-----------------------------------------------------------------------------
-- TCOMPUTEDVALUES
-----------------------------------------------------------------------------
-- DROP TABLE TCOMPUTEDVALUES CASCADE CONSTRAINTS;

drop table TCOMPUTEDVALUES;

CREATE TABLE TCOMPUTEDVALUES
(
                    PKEY INTEGER NOT NULL,
                    WORKITEMKEY INTEGER NOT NULL,
                    EFFORTTYPE INTEGER NOT NULL,
                    COMPUTEDVALUETYPE INTEGER NOT NULL,
                COMPUTEDVALUE DOUBLE NOT NULL
);

ALTER TABLE TCOMPUTEDVALUES
    ADD PRIMARY KEY (PKEY);


ALTER TABLE TPRIVATEREPORTREPOSITORY ADD COLUMN MENUITEM VARCHAR(1) default 'N';

ALTER TABLE TCOST ADD COLUMN EFFORTTYPE INTEGER;
ALTER TABLE TCOST ADD COLUMN EFFORTVALUE DOUBLE;
ALTER TABLE TCOST ADD COLUMN EFFORTDATE TIMESTAMP;
ALTER TABLE TCOST ADD COLUMN INVOICENUMBER VARCHAR(255);
ALTER TABLE TCOST ADD COLUMN INVOICEDATE TIMESTAMP;
ALTER TABLE TCOST ADD COLUMN INVOICEPATH VARCHAR(255);


----------------------------------------------------------------------------
-- TEFFORTTYPE
-----------------------------------------------------------------------------
drop table TEFFORTTYPE;

CREATE TABLE TEFFORTTYPE
(
                    OBJECTID INTEGER NOT NULL,
                LABEL VARCHAR (255) NOT NULL,
                    EFFORTUNIT INTEGER,
                DESCRIPTION VARCHAR (255)
);

ALTER TABLE TEFFORTTYPE
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TEFFORTUNIT
-----------------------------------------------------------------------------
drop table TEFFORTUNIT;

CREATE TABLE TEFFORTUNIT
(
                    OBJECTID INTEGER NOT NULL,
                LABEL VARCHAR (255) NOT NULL,
                DESCRIPTION VARCHAR (255)
);


ALTER TABLE TEFFORTUNIT
    ADD PRIMARY KEY (OBJECTID);


ALTER TABLE TSITE ADD COLUMN RECEIVINGPROTOCOL VARCHAR(6);
ALTER TABLE TSITE ALTER LDAPATTRIBUTELOGINNAME SET DATA TYPE VARCHAR(30); 

ALTER TABLE TBUDGET ADD COLUMN EFFORTTYPE INTEGER;
ALTER TABLE TBUDGET ADD COLUMN EFFORTVALUE DOUBLE;

ALTER TABLE TACTUALESTIMATEDBUDGET ADD COLUMN EFFORTTYPE INTEGER;
ALTER TABLE TACTUALESTIMATEDBUDGET ADD COLUMN EFFORTVALUE DOUBLE;


-----------------------------------------------------------------------------
-- TDASHBOARDSCREEN
-----------------------------------------------------------------------------
drop table TDASHBOARDSCREEN;

CREATE TABLE TDASHBOARDSCREEN
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                LABEL VARCHAR (255),
                    PERSONPKEY INTEGER NOT NULL,
                DESCRIPTION VARCHAR (10000)
);

ALTER TABLE TDASHBOARDSCREEN
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TDASHBOARDTAB
-----------------------------------------------------------------------------
drop table TDASHBOARDTAB;

CREATE TABLE TDASHBOARDTAB
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                LABEL VARCHAR (255),
                DESCRIPTION VARCHAR (10000),
                    SORTORDER INTEGER,
                    PARENT INTEGER NOT NULL
);

ALTER TABLE TDASHBOARDTAB
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TDASHBOARDPANEL
-----------------------------------------------------------------------------
drop table TDASHBOARDPANEL;

CREATE TABLE TDASHBOARDPANEL
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                LABEL VARCHAR (255),
                DESCRIPTION VARCHAR (10000),
                    SORTORDER INTEGER,
                    ROWSNO INTEGER NOT NULL,
                    COLSNO INTEGER NOT NULL,
                    PARENT INTEGER NOT NULL
);

ALTER TABLE TDASHBOARDPANEL
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TDASHBOARDFIELD
-----------------------------------------------------------------------------
drop table TDASHBOARDFIELD;

CREATE TABLE TDASHBOARDFIELD
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                DESCRIPTION VARCHAR (10000),
                    SORTORDER INTEGER,
                    COLINDEX INTEGER,
                    ROWINDEX INTEGER,
                    COLSPAN INTEGER,
                    ROWSPAN INTEGER,
                    PARENT INTEGER NOT NULL,
                DASHBOARDID VARCHAR (10000) NOT NULL,
                THEDESCRIPTION VARCHAR (10000)
);

ALTER TABLE TDASHBOARDFIELD
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TDASHBOARDPARAMETER
-----------------------------------------------------------------------------
drop table TDASHBOARDPARAMETER;

CREATE TABLE TDASHBOARDPARAMETER
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                PARAMVALUE VARCHAR (10000),
                    DASHBOARDFIELD INTEGER NOT NULL
);

ALTER TABLE TDASHBOARDPARAMETER
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TVERSIONCONTROLPARAMETER
-----------------------------------------------------------------------------
drop table TVERSIONCONTROLPARAMETER;

CREATE TABLE TVERSIONCONTROLPARAMETER
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                PARAMVALUE VARCHAR (10000),
                    PROJECT INTEGER NOT NULL
);

ALTER TABLE TVERSIONCONTROLPARAMETER
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TFIELD
-----------------------------------------------------------------------------
drop table TFIELD;

CREATE TABLE TFIELD
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                FIELDTYPE VARCHAR (255) NOT NULL,
                DEPRECATED VARCHAR (1) default 'N',
                ISCUSTOM VARCHAR (1) default 'Y' NOT NULL,
                REQUIRED VARCHAR (1) default 'N' NOT NULL,
                DESCRIPTION VARCHAR (10000),
                    OWNER INTEGER
);

ALTER TABLE TFIELD
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TFIELDCONFIG
-----------------------------------------------------------------------------
drop table TFIELDCONFIG;

CREATE TABLE TFIELDCONFIG
(
                    OBJECTID INTEGER NOT NULL,
                    FIELDKEY INTEGER NOT NULL,
                LABEL VARCHAR (255) NOT NULL,
                TOOLTIP VARCHAR (255),
                REQUIRED VARCHAR (1) default 'N' NOT NULL,
                HISTORY VARCHAR (1) default 'N' NOT NULL,
                    ISSUETYPE INTEGER,
                    PROJECTTYPE INTEGER,
                    PROJECT INTEGER,
                DESCRIPTION VARCHAR (10000)
);

ALTER TABLE TFIELDCONFIG
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TROLEFIELD
-----------------------------------------------------------------------------
drop table TROLEFIELD;

CREATE TABLE TROLEFIELD
(
                    OBJECTID INTEGER NOT NULL,
                    ROLEKEY INTEGER NOT NULL,
                    FIELDKEY INTEGER NOT NULL,
                    ACCESSRIGHT INTEGER
);

ALTER TABLE TROLEFIELD
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TCONFIGOPTIONSROLE
-----------------------------------------------------------------------------
drop table TCONFIGOPTIONSROLE;

CREATE TABLE TCONFIGOPTIONSROLE
(
                    OBJECTID INTEGER NOT NULL,
                    CONFIGKEY INTEGER NOT NULL,
                    ROLEKEY INTEGER NOT NULL,
                    OPTIONKEY INTEGER NOT NULL
);

ALTER TABLE TCONFIGOPTIONSROLE
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TTEXTBOXSETTINGS
-----------------------------------------------------------------------------
drop table TTEXTBOXSETTINGS;

CREATE TABLE TTEXTBOXSETTINGS
(
                    OBJECTID INTEGER NOT NULL,
                    CONFIG INTEGER NOT NULL,
                REQUIRED VARCHAR (1) default 'N' NOT NULL,
                DEFAULTTEXT VARCHAR (255),
                    DEFAULTINTEGER INTEGER,
                DEFAULTDOUBLE DOUBLE,
                DEFAULTDATE TIMESTAMP,
                DEFAULTCHAR VARCHAR (1),
                    DEFAULTOPTION INTEGER,
                    MINOPTION INTEGER,
                    MAXOPTION INTEGER,
                    MINTEXTLENGTH INTEGER,
                    MAXTEXTLENGTH INTEGER,
                MINDATE TIMESTAMP,
                MAXDATE TIMESTAMP,
                    MININTEGER INTEGER,
                    MAXINTEGER INTEGER,
                MINDOUBLE DOUBLE,
                MAXDOUBLE DOUBLE,
                    MAXDECIMALDIGIT INTEGER,
                    PARAMETERCODE INTEGER,
                    VALIDVALUE INTEGER
);

ALTER TABLE TTEXTBOXSETTINGS
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TGENERALSETTINGS
-----------------------------------------------------------------------------
drop table TGENERALSETTINGS;

CREATE TABLE TGENERALSETTINGS
(
                    OBJECTID INTEGER NOT NULL,
                    CONFIG INTEGER NOT NULL,
                    INTEGERVALUE INTEGER,
                DOUBLEVALUE DOUBLE,
                TEXTVALUE VARCHAR (255),
                DATEVALUE TIMESTAMP,
                CHARACTERVALUE VARCHAR (1),
                    PARAMETERCODE INTEGER,
                    VALIDVALUE INTEGER
);

ALTER TABLE TGENERALSETTINGS
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TLIST
-----------------------------------------------------------------------------
drop table TLIST;

CREATE TABLE TLIST
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                DESCRIPTION VARCHAR (10000),
                    PARENTLIST INTEGER,
                    LISTTYPE INTEGER,
                    CHILDNUMBER INTEGER,
                DELETED VARCHAR (1) default 'N',
				REPOSITORYTYPE INTEGER,
			    PROJECT INTEGER,
			    OWNER INTEGER
);

ALTER TABLE TLIST
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TOPTION
-----------------------------------------------------------------------------
drop table TOPTION;

CREATE TABLE TOPTION
(
                    OBJECTID INTEGER NOT NULL,
                    LIST INTEGER NOT NULL,
                LABEL VARCHAR (255) NOT NULL,
                    PARENTOPTION INTEGER,
                    SORTORDER INTEGER,
                ISDEFAULT VARCHAR (1) default 'N' NOT NULL,
                DELETED VARCHAR (1) default 'N'
);

ALTER TABLE TOPTION
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TOPTIONSETTINGS
-----------------------------------------------------------------------------
drop table TOPTIONSETTINGS;

CREATE TABLE TOPTIONSETTINGS
(
                    OBJECTID INTEGER NOT NULL,
                    LIST INTEGER NOT NULL,
                    CONFIG INTEGER NOT NULL,
                    PARAMETERCODE INTEGER,
                MULTIPLE VARCHAR (1) default 'N'
);

ALTER TABLE TOPTIONSETTINGS
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TATTRIBUTEVALUE
-----------------------------------------------------------------------------
drop table TATTRIBUTEVALUE;

CREATE TABLE TATTRIBUTEVALUE
(
                    OBJECTID INTEGER NOT NULL,
                    FIELDKEY INTEGER NOT NULL,
                    WORKITEM INTEGER NOT NULL,
                TEXTVALUE VARCHAR (255),
                    INTEGERVALUE INTEGER,
                DOUBLEVALUE DOUBLE,
                DATEVALUE TIMESTAMP,
                CHARACTERVALUE VARCHAR (1),
                LONGTEXTVALUE VARCHAR (32000),
                    SYSTEMOPTIONID INTEGER,
                    SYSTEMOPTIONTYPE INTEGER,
                    CUSTOMOPTIONID INTEGER,
                    PARAMETERCODE INTEGER,
                    VALIDVALUE INTEGER,
                LASTEDIT TIMESTAMP
);

ALTER TABLE TATTRIBUTEVALUE
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TSCREEN
-----------------------------------------------------------------------------
drop table TSCREEN;

CREATE TABLE TSCREEN
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                LABEL VARCHAR (255),
                DESCRIPTION VARCHAR (10000),
                    OWNER INTEGER
);

ALTER TABLE TSCREEN
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TSCREENTAB
-----------------------------------------------------------------------------
drop table TSCREENTAB;

CREATE TABLE TSCREENTAB
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                LABEL VARCHAR (255),
                DESCRIPTION VARCHAR (10000),
                    SORTORDER INTEGER,
                    PARENT INTEGER NOT NULL
);

ALTER TABLE TSCREENTAB
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TSCREENPANEL
-----------------------------------------------------------------------------
drop table TSCREENPANEL;

CREATE TABLE TSCREENPANEL
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                LABEL VARCHAR (255),
                DESCRIPTION VARCHAR (10000),
                    SORTORDER INTEGER,
                    ROWSNO INTEGER NOT NULL,
                    COLSNO INTEGER NOT NULL,
                    PARENT INTEGER NOT NULL
);

ALTER TABLE TSCREENPANEL
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TSCREENFIELD
-----------------------------------------------------------------------------
drop table TSCREENFIELD;

CREATE TABLE TSCREENFIELD
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                DESCRIPTION VARCHAR (10000),
                    SORTORDER INTEGER,
                    COLINDEX INTEGER,
                    ROWINDEX INTEGER,
                    COLSPAN INTEGER,
                    ROWSPAN INTEGER,
                    LABELHALIGN INTEGER NOT NULL,
                    LABELVALIGN INTEGER NOT NULL,
                    VALUEHALIGN INTEGER NOT NULL,
                    VALUEVALIGN INTEGER NOT NULL,
                ISEMPTY VARCHAR (1) default 'N' NOT NULL,
                    PARENT INTEGER NOT NULL,
                    FIELDKEY INTEGER
);

ALTER TABLE TSCREENFIELD
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TACTION
-----------------------------------------------------------------------------
drop table TACTION;

CREATE TABLE TACTION
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                LABEL VARCHAR (255),
                DESCRIPTION VARCHAR (10000)
);

ALTER TABLE TACTION
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TSCREENCONFIG
-----------------------------------------------------------------------------
drop table TSCREENCONFIG;

CREATE TABLE TSCREENCONFIG
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255),
                DESCRIPTION VARCHAR (10000),
                    SCREEN INTEGER,
                    ISSUETYPE INTEGER,
                    PROJECTTYPE INTEGER,
                    PROJECT INTEGER,
                    ACTIONKEY INTEGER NOT NULL
);

ALTER TABLE TSCREENCONFIG
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TINITSTATE
-----------------------------------------------------------------------------
drop table TINITSTATE;

CREATE TABLE TINITSTATE
(
                    OBJECTID INTEGER NOT NULL,
                    PROJECT INTEGER NOT NULL,
                    LISTTYPE INTEGER NOT NULL,
                    STATEKEY INTEGER NOT NULL
);

ALTER TABLE TINITSTATE
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TEVENT
-----------------------------------------------------------------------------
drop table TEVENT;

CREATE TABLE TEVENT
(
                    OBJECTID INTEGER NOT NULL,
                EVENTNAME VARCHAR (255),
                    EVENTTYPE INTEGER NOT NULL,
                    PROJECTTYPE INTEGER,
                    PROJECT INTEGER,
                    EVENTSCRIPT INTEGER NOT NULL
);

ALTER TABLE TEVENT
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TCLOB
-----------------------------------------------------------------------------
drop table TCLOB;

CREATE TABLE TCLOB
(
                    OBJECTID INTEGER NOT NULL,
                CLOBVALUE CLOB
);

ALTER TABLE TCLOB
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TNOTIFYFIELD
-----------------------------------------------------------------------------
drop table TNOTIFYFIELD;

CREATE TABLE TNOTIFYFIELD
(
                    OBJECTID INTEGER NOT NULL,
                    FIELD INTEGER,
                    ACTIONTYPE INTEGER NOT NULL,
                    FIELDTYPE INTEGER,
                    NOTIFYTRIGGER INTEGER NOT NULL,
                ORIGINATOR VARCHAR (1) default 'N',
                MANAGER VARCHAR (1) default 'N',
                RESPONSIBLE VARCHAR (1) default 'N',
                CONSULTANT VARCHAR (1) default 'N',
                INFORMANT VARCHAR (1) default 'N',
                OBSERVER VARCHAR (1) default 'N'
);

ALTER TABLE TNOTIFYFIELD
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TNOTIFYTRIGGER
-----------------------------------------------------------------------------
drop table TNOTIFYTRIGGER;

CREATE TABLE TNOTIFYTRIGGER
(
                    OBJECTID INTEGER NOT NULL,
                LABEL VARCHAR (255),
                    PERSON INTEGER,
                ISSYSTEM VARCHAR (1) default 'N'
);

ALTER TABLE TNOTIFYTRIGGER
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TNOTIFYSETTINGS
-----------------------------------------------------------------------------
drop table TNOTIFYSETTINGS;

CREATE TABLE TNOTIFYSETTINGS
(
                    OBJECTID INTEGER NOT NULL,
                    PERSON INTEGER,
                    PROJECT INTEGER,
                    NOTIFYTRIGGER INTEGER,
                    NOTIFYFILTER INTEGER
);

ALTER TABLE TNOTIFYSETTINGS
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TQUERYREPOSITORY
-----------------------------------------------------------------------------
drop table TQUERYREPOSITORY;

CREATE TABLE TQUERYREPOSITORY
(
                    OBJECTID INTEGER NOT NULL,
                    PERSON INTEGER,
                    PROJECT INTEGER,
                LABEL VARCHAR (100),
                    QUERYTYPE INTEGER NOT NULL,
                    REPOSITORYTYPE INTEGER NOT NULL,
                    QUERYKEY INTEGER NOT NULL,
                MENUITEM VARCHAR (1) default 'N'
);

ALTER TABLE TQUERYREPOSITORY
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TLOCALIZEDRESOURCES
-----------------------------------------------------------------------------
drop table TLOCALIZEDRESOURCES;

CREATE TABLE TLOCALIZEDRESOURCES
(
                    OBJECTID INTEGER NOT NULL,
                TABLENAME VARCHAR (255) NOT NULL,
                    PRIMARYKEYVALUE INTEGER NOT NULL,
                FIELDNAME VARCHAR (255) NOT NULL,
                LOCALIZEDTEXT VARCHAR (255) NOT NULL,
                LOCALE VARCHAR (20) NOT NULL
);

ALTER TABLE TLOCALIZEDRESOURCES
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TWORKITEMLINK
-----------------------------------------------------------------------------
drop table TWORKITEMLINK;

CREATE TABLE TWORKITEMLINK
(
                    OBJECTID INTEGER NOT NULL,
                LINKISCROSSPROJECT VARCHAR (1) default 'N' NOT NULL,
                    LINKPRED INTEGER NOT NULL,
                    LINKSUCC INTEGER NOT NULL,
                    LINKTYPE INTEGER NOT NULL,
                    LINKLAG INTEGER,
                    LINKLAGFORMAT INTEGER
);

ALTER TABLE TWORKITEMLINK
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TLOGGINGLEVEL
-----------------------------------------------------------------------------
drop table TLOGGINGLEVEL;

CREATE TABLE TLOGGINGLEVEL
(
                    OBJECTID INTEGER NOT NULL,
                THECLASSNAME VARCHAR (255) NOT NULL,
                LOGLEVEL VARCHAR (10) NOT NULL
);

ALTER TABLE TLOGGINGLEVEL
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TWORKITEMLOCK
-----------------------------------------------------------------------------
drop table TWORKITEMLOCK;

CREATE TABLE TWORKITEMLOCK
(
                    WORKITEM INTEGER NOT NULL,
                    PERSON INTEGER NOT NULL,
                HTTPSESSION VARCHAR (255) NOT NULL
);

ALTER TABLE TWORKITEMLOCK
    ADD PRIMARY KEY (WORKITEM);







-----------------------------------------------------------------------------
-- TEXPORTTEMPLATE
-----------------------------------------------------------------------------
drop table TEXPORTTEMPLATE;

CREATE TABLE TEXPORTTEMPLATE
(
                    OBJECTID INTEGER NOT NULL,
                NAME VARCHAR (255) NOT NULL,
                REPORTTYPE VARCHAR (255) NOT NULL,
                EXPORTFORMAT VARCHAR (255) NOT NULL,
                    REPOSITORYTYPE INTEGER NOT NULL,
                DESCRIPTION VARCHAR (10000),
                    PROJECT INTEGER,
                    PERSON INTEGER NOT NULL
);

ALTER TABLE TEXPORTTEMPLATE
    ADD PRIMARY KEY (OBJECTID);







-----------------------------------------------------------------------------
-- TEMAILPROCESSED
-----------------------------------------------------------------------------
drop table TEMAILPROCESSED;

CREATE TABLE TEMAILPROCESSED
(
                    OBJECTID INTEGER NOT NULL,
                PROCESSEDDATE TIMESTAMP NOT NULL,
                MESSAGEUID VARCHAR (255) NOT NULL,
                RECEIVEDAT VARCHAR (255) NOT NULL
);

ALTER TABLE TEMAILPROCESSED
    ADD PRIMARY KEY (OBJECTID);





-----------------------------------------------------------------------------
-- TCOMPUTEDVALUES: FOREIGN KEYS
-----------------------------------------------------------------------------
ALTER TABLE TCOMPUTEDVALUES
    ADD CONSTRAINT TCOMPUTEDVALU_FK_1 FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
;
ALTER TABLE TCOMPUTEDVALUES
    ADD CONSTRAINT TCOMPUTEDVALU_FK_2 FOREIGN KEY (EFFORTTYPE)
    REFERENCES TEFFORTTYPE (OBJECTID)
;


ALTER TABLE TCOST
    ADD CONSTRAINT TCOST_FK_4 FOREIGN KEY (EFFORTTYPE)
    REFERENCES TEFFORTTYPE (OBJECTID)
;



ALTER TABLE TEFFORTTYPE
    ADD CONSTRAINT TEFFORTTYPE_FK_1 FOREIGN KEY (EFFORTUNIT)
    REFERENCES TEFFORTUNIT (OBJECTID)
;

ALTER TABLE TREPORTLAYOUT DROP CONSTRAINT TREPORTLAYOUT_FK_4;
ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_4 FOREIGN KEY (REPORTFIELD)
    REFERENCES TFIELD (OBJECTID)
;

ALTER TABLE TDASHBOARDSCREEN
    ADD CONSTRAINT TDASHBOARDSCR_FK_1 FOREIGN KEY (PERSONPKEY)
    REFERENCES TPERSON (PKEY)
;



ALTER TABLE TDASHBOARDTAB
    ADD CONSTRAINT TDASHBOARDTAB_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TDASHBOARDSCREEN (OBJECTID)
;



ALTER TABLE TDASHBOARDPANEL
    ADD CONSTRAINT TDASHBOARDPAN_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TDASHBOARDTAB (OBJECTID)
;



ALTER TABLE TDASHBOARDFIELD
    ADD CONSTRAINT TDASHBOARDFIE_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TDASHBOARDPANEL (OBJECTID)
;



ALTER TABLE TDASHBOARDPARAMETER
    ADD CONSTRAINT TDASHBOARDPAR_FK_1 FOREIGN KEY (DASHBOARDFIELD)
    REFERENCES TDASHBOARDFIELD (OBJECTID)
;



ALTER TABLE TVERSIONCONTROLPARAMETER
    ADD CONSTRAINT TVERSIONCONTR_FK_1 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;



ALTER TABLE TFIELD
    ADD CONSTRAINT TFIELD_FK_1 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
;



ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_1 FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
;

ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_2 FOREIGN KEY (ISSUETYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_3 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_4 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;



ALTER TABLE TROLEFIELD
    ADD CONSTRAINT TROLEFIELD_FK_1 FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
;

ALTER TABLE TROLEFIELD
    ADD CONSTRAINT TROLEFIELD_FK_2 FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
;



ALTER TABLE TCONFIGOPTIONSROLE
    ADD CONSTRAINT TCONFIGOPTION_FK_1 FOREIGN KEY (CONFIGKEY)
    REFERENCES TFIELDCONFIG (OBJECTID)
;

ALTER TABLE TCONFIGOPTIONSROLE
    ADD CONSTRAINT TCONFIGOPTION_FK_2 FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
;

ALTER TABLE TCONFIGOPTIONSROLE
    ADD CONSTRAINT TCONFIGOPTION_FK_3 FOREIGN KEY (OPTIONKEY)
    REFERENCES TOPTION (OBJECTID)
;



ALTER TABLE TTEXTBOXSETTINGS
    ADD CONSTRAINT TTEXTBOXSETTI_FK_1 FOREIGN KEY (CONFIG)
    REFERENCES TFIELDCONFIG (OBJECTID)
;



ALTER TABLE TGENERALSETTINGS
    ADD CONSTRAINT TGENERALSETTI_FK_1 FOREIGN KEY (CONFIG)
    REFERENCES TFIELDCONFIG (OBJECTID)
;



ALTER TABLE TLIST
    ADD CONSTRAINT TLIST_FK_1 FOREIGN KEY (PARENTLIST)
    REFERENCES TLIST (OBJECTID)
;

ALTER TABLE TLIST
    ADD CONSTRAINT TLIST_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY);

ALTER TABLE TLIST
    ADD CONSTRAINT TLIST_FK_3 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY);

ALTER TABLE TOPTION
    ADD CONSTRAINT TOPTION_FK_1 FOREIGN KEY (LIST)
    REFERENCES TLIST (OBJECTID)
;



ALTER TABLE TOPTIONSETTINGS
    ADD CONSTRAINT TOPTIONSETTIN_FK_1 FOREIGN KEY (LIST)
    REFERENCES TLIST (OBJECTID)
;

ALTER TABLE TOPTIONSETTINGS
    ADD CONSTRAINT TOPTIONSETTIN_FK_2 FOREIGN KEY (CONFIG)
    REFERENCES TFIELDCONFIG (OBJECTID)
;



ALTER TABLE TATTRIBUTEVALUE
    ADD CONSTRAINT TATTRIBUTEVAL_FK_1 FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
;

ALTER TABLE TATTRIBUTEVALUE
    ADD CONSTRAINT TATTRIBUTEVAL_FK_2 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;



ALTER TABLE TSCREEN
    ADD CONSTRAINT TSCREEN_FK_1 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
;



ALTER TABLE TSCREENTAB
    ADD CONSTRAINT TSCREENTAB_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TSCREEN (OBJECTID)
;



ALTER TABLE TSCREENPANEL
    ADD CONSTRAINT TSCREENPANEL_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TSCREENTAB (OBJECTID)
;



ALTER TABLE TSCREENFIELD
    ADD CONSTRAINT TSCREENFIELD_FK_1 FOREIGN KEY (PARENT)
    REFERENCES TSCREENPANEL (OBJECTID)
;

ALTER TABLE TSCREENFIELD
    ADD CONSTRAINT TSCREENFIELD_FK_2 FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
;





ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_1 FOREIGN KEY (SCREEN)
    REFERENCES TSCREEN (OBJECTID)
;

ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_2 FOREIGN KEY (ISSUETYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_3 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_4 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_5 FOREIGN KEY (ACTIONKEY)
    REFERENCES TACTION (OBJECTID)
;



ALTER TABLE TINITSTATE
    ADD CONSTRAINT TINITSTATE_FK_1 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TINITSTATE
    ADD CONSTRAINT TINITSTATE_FK_2 FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TINITSTATE
    ADD CONSTRAINT TINITSTATE_FK_3 FOREIGN KEY (STATEKEY)
    REFERENCES TSTATE (PKEY)
;



ALTER TABLE TEVENT
    ADD CONSTRAINT TEVENT_FK_1 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TEVENT
    ADD CONSTRAINT TEVENT_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TEVENT
    ADD CONSTRAINT TEVENT_FK_3 FOREIGN KEY (EVENTSCRIPT)
    REFERENCES TCLOB (OBJECTID)
;





ALTER TABLE TNOTIFYFIELD
    ADD CONSTRAINT TNOTIFYFIELD_FK_1 FOREIGN KEY (NOTIFYTRIGGER)
    REFERENCES TNOTIFYTRIGGER (OBJECTID)
;



ALTER TABLE TNOTIFYTRIGGER
    ADD CONSTRAINT TNOTIFYTRIGGE_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;



ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTIN_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTIN_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTIN_FK_3 FOREIGN KEY (NOTIFYTRIGGER)
    REFERENCES TNOTIFYTRIGGER (OBJECTID)
;

ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTIN_FK_4 FOREIGN KEY (NOTIFYFILTER)
    REFERENCES TQUERYREPOSITORY (OBJECTID)
;



ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSIT_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSIT_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSIT_FK_3 FOREIGN KEY (QUERYKEY)
    REFERENCES TCLOB (OBJECTID)
;





ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_1 FOREIGN KEY (LINKPRED)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_2 FOREIGN KEY (LINKSUCC)
    REFERENCES TWORKITEM (WORKITEMKEY)
;





ALTER TABLE TWORKITEMLOCK
    ADD CONSTRAINT TWORKITEMLOCK_FK_1 FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TWORKITEMLOCK
    ADD CONSTRAINT TWORKITEMLOCK_FK_2 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;



ALTER TABLE TEXPORTTEMPLATE
    ADD CONSTRAINT TEXPORTTEMPLA_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TEXPORTTEMPLATE
    ADD CONSTRAINT TEXPORTTEMPLA_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;


-- 
-- Quartz
--
create table qrtz_job_details (
  job_name varchar(80) not null,
  job_group varchar(80) not null,
  description varchar(120),
  job_class_name varchar(128) not null,
  is_durable varchar(1) not null,
  is_volatile varchar(1) not null,
  is_stateful varchar(1) not null,
  requests_recovery varchar(1) not null,
  job_data blob(2000),
    primary key (job_name,job_group)
);

create table qrtz_job_listeners(
  job_name varchar(80) not null,
  job_group varchar(80) not null,
  job_listener varchar(80) not null,
    primary key (job_name,job_group,job_listener),
    foreign key (job_name,job_group) references qrtz_job_details(job_name,job_group)
);

create table qrtz_triggers(
  trigger_name varchar(80) not null,
  trigger_group varchar(80) not null,
  job_name varchar(80) not null,
  job_group varchar(80) not null,
  is_volatile varchar(1) not null,
  description varchar(120),
  next_fire_time bigint,
  prev_fire_time bigint,
  priority integer,
  trigger_state varchar(16) not null,
  trigger_type varchar(8) not null,
  start_time bigint not null,
  end_time bigint,
  calendar_name varchar(80),
  misfire_instr smallint,
  job_data blob(2000),
    primary key (trigger_name,trigger_group),
    foreign key (job_name,job_group) references qrtz_job_details(job_name,job_group)
);

create table qrtz_simple_triggers(
  trigger_name varchar(80) not null,
  trigger_group varchar(80) not null,
  repeat_count bigint not null,
  repeat_interval bigint not null,
  times_triggered bigint not null,
    primary key (trigger_name,trigger_group),
    foreign key (trigger_name,trigger_group) references qrtz_triggers(trigger_name,trigger_group)
);

create table qrtz_cron_triggers(
  trigger_name varchar(80) not null,
  trigger_group varchar(80) not null,
  cron_expression varchar(80) not null,
  time_zone_id varchar(80),
    primary key (trigger_name,trigger_group),
    foreign key (trigger_name,trigger_group) references qrtz_triggers(trigger_name,trigger_group)
);

create table qrtz_blob_triggers(
  trigger_name varchar(80) not null,
  trigger_group varchar(80) not null,
  blob_data blob(2000),
    primary key (trigger_name,trigger_group),
    foreign key (trigger_name,trigger_group) references qrtz_triggers(trigger_name,trigger_group)
);

create table qrtz_trigger_listeners(
  trigger_name varchar(80) not null,
  trigger_group varchar(80) not null,
  trigger_listener varchar(80) not null,
    primary key (trigger_name,trigger_group,trigger_listener),
    foreign key (trigger_name,trigger_group) references qrtz_triggers(trigger_name,trigger_group)
);

create table qrtz_calendars(
  calendar_name varchar(80) not null,
  calendar blob(2000) not null,
    primary key (calendar_name)
);

create table qrtz_fired_triggers(
  entry_id varchar(95) not null,
  trigger_name varchar(80) not null,
  trigger_group varchar(80) not null,
  is_volatile varchar(1) not null,
  instance_name varchar(80) not null,
  fired_time bigint not null,
  priority integer not null,
  state varchar(16) not null,
  job_name varchar(80),
  job_group varchar(80),
  is_stateful varchar(1),
  requests_recovery varchar(1),
    primary key (entry_id)
);


create table qrtz_paused_trigger_grps(
  trigger_group  varchar(80) not null, 
    primary key (trigger_group)
);

create table qrtz_scheduler_state (
  instance_name varchar(80) not null,
  last_checkin_time bigint not null,
  checkin_interval bigint not null,
    primary key (instance_name)
);

create table qrtz_locks
  (
    lock_name  varchar(40) not null, 
      primary key (lock_name)
);

insert into qrtz_locks values('TRIGGER_ACCESS');
insert into qrtz_locks values('JOB_ACCESS');
insert into qrtz_locks values('CALENDAR_ACCESS');
insert into qrtz_locks values('STATE_ACCESS');
insert into qrtz_locks values('MISFIRE_ACCESS');

commit;

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
--Insert system fields
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
			VALUES (10, 'Priority', 'com.aurel.track.fieldType.types.system.select.SystemSelectPriority', 'N', 'N', 'N');
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

--Insert the actions: new,edit and extra action "Other"
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
--notify field for edit budget fields
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (20, 1, 2, 2, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (21, 2, 2, 2, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
--notify field for edit estimated remaining budget fields
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (22, 1, 2, 3, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (23, 2, 2, 3, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
--notify field for edit expense fields
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (24, 1, 2, 4, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');
INSERT INTO TNOTIFYFIELD (OBJECTID, FIELD, ACTIONTYPE, FIELDTYPE, NOTIFYTRIGGER, ORIGINATOR, MANAGER, RESPONSIBLE, CONSULTANT, INFORMANT, OBSERVER)
VALUES (25, 2, 2, 4, 1, 'Y', 'Y', 'Y', 'Y', 'Y', 'Y');

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




