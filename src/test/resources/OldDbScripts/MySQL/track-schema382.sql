
# -----------------------------------------------------------------------
# TACL
# -----------------------------------------------------------------------
drop table if exists TACL;

CREATE TABLE TACL
(
    PERSONKEY INTEGER NOT NULL,
    ROLEKEY INTEGER NOT NULL,
    PROJKEY INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PERSONKEY,ROLEKEY,PROJKEY),
    INDEX TACLINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TBASELINE
# -----------------------------------------------------------------------
drop table if exists TBASELINE;

CREATE TABLE TBASELINE
(
    BLKEY INTEGER NOT NULL,
    WORKITEMKEY INTEGER NOT NULL,
    STARTDATE DATETIME,
    ENDDATE DATETIME,
    REASONFORCHANGE MEDIUMTEXT,
    CHANGEDBY INTEGER,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(BLKEY),
    INDEX TBASINDEX1 (WORKITEMKEY),
    INDEX TBASINDEX2 (CHANGEDBY),
    INDEX TBASINDEX3 (TPUUID));


# -----------------------------------------------------------------------
# TCATEGORY
# -----------------------------------------------------------------------
drop table if exists TCATEGORY;

CREATE TABLE TCATEGORY
(
    PKEY INTEGER NOT NULL,
    LABEL VARCHAR(25) NOT NULL,
    TYPEFLAG INTEGER,
    SORTORDER INTEGER,
    SYMBOL VARCHAR(255),
    ICONKEY INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TCATEGORYINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TCLASS
# -----------------------------------------------------------------------
drop table if exists TCLASS;

CREATE TABLE TCLASS
(
    PKEY INTEGER NOT NULL,
    LABEL VARCHAR(25) NOT NULL,
    PROJKEY INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TCLASSINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TDEPARTMENT
# -----------------------------------------------------------------------
drop table if exists TDEPARTMENT;

CREATE TABLE TDEPARTMENT
(
    PKEY INTEGER NOT NULL,
    LABEL VARCHAR(18) NOT NULL,
    COSTCENTER INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TDEPARTMENTINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TNOTIFY
# -----------------------------------------------------------------------
drop table if exists TNOTIFY;

CREATE TABLE TNOTIFY
(
    PKEY INTEGER NOT NULL,
    PROJCATKEY INTEGER NOT NULL,
    STATEKEY INTEGER,
    PERSONKEY INTEGER NOT NULL,
    WORKITEM INTEGER,
    RACIROLE VARCHAR(1),
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TNOTIFYINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPERSON
# -----------------------------------------------------------------------
drop table if exists TPERSON;

CREATE TABLE TPERSON
(
    PKEY INTEGER NOT NULL,
    FIRSTNAME VARCHAR(25),
    LASTNAME VARCHAR(25),
    LOGINNAME VARCHAR(60) NOT NULL,
    EMAIL VARCHAR(60),
    PASSWD VARCHAR(80),
    PHONE VARCHAR(18),
    DEPKEY INTEGER,
    VALIDUNTIL DATETIME,
    PREFERENCES MEDIUMTEXT,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DELETED CHAR(1) default 'N',
    TOKENPASSWD VARCHAR(80),
    TOKENEXPDATE TIMESTAMP DEFAULT '2008-06-12 00:00:00',
    EMAILFREQUENCY INTEGER,
    EMAILLEAD INTEGER,
    EMAILLASTREMINDED TIMESTAMP DEFAULT '2008-06-12 00:00:00',
    EMAILREMINDME CHAR(1) default 'N',
    PREFEMAILTYPE VARCHAR(15) default 'Plain',
    PREFLOCALE VARCHAR(10),
    MYDEFAULTREPORT INTEGER,
    NOEMAILSPLEASE INTEGER,
    REMINDMEASORIGINATOR CHAR(1) default 'N',
    REMINDMEASMANAGER CHAR(1) default 'Y',
    REMINDMEASRESPONSIBLE CHAR(1) default 'Y',
    EMAILREMINDPLEVEL INTEGER,
    EMAILREMINDSLEVEL INTEGER,
    HOURSPERWORKDAY DOUBLE,
    EMPLOYEEID VARCHAR(30),
    ISGROUP CHAR(1) default 'N',
	USERLEVEL INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TPERSINDEX1 (DEPKEY),
    INDEX TPERSINDEX2 (TPUUID));


# -----------------------------------------------------------------------
# TPRIORITY
# -----------------------------------------------------------------------
drop table if exists TPRIORITY;

CREATE TABLE TPRIORITY
(
    PKEY INTEGER NOT NULL,
    LABEL VARCHAR(25),
    SORTORDER INTEGER,
    WLEVEL INTEGER,
    SYMBOL VARCHAR(255),
    ICONKEY INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TPRIOINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPPRIORITY
# -----------------------------------------------------------------------
drop table if exists TPPRIORITY;

CREATE TABLE TPPRIORITY
(
    OBJECTID INTEGER NOT NULL,
    PRIORITY INTEGER,
    PROJECTTYPE INTEGER,
    LISTTYPE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TPPRIOINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPROJCAT
# -----------------------------------------------------------------------
drop table if exists TPROJCAT;

CREATE TABLE TPROJCAT
(
    PKEY INTEGER NOT NULL,
    LABEL VARCHAR(35),
    PROJKEY INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TPROJCATINDEX1 (PROJKEY),
    INDEX TPROJCATINDEX2 (TPUUID));


# -----------------------------------------------------------------------
# TPROJECT
# -----------------------------------------------------------------------
drop table if exists TPROJECT;

CREATE TABLE TPROJECT
(
    PKEY INTEGER NOT NULL,
    LABEL VARCHAR(35),
    DEFOWNER INTEGER,
    DEFMANAGER INTEGER,
    DEFINITSTATE INTEGER,
    PROJECTTYPE INTEGER,
    VERSIONSYSTEMFIELD0 VARCHAR(255),
    VERSIONSYSTEMFIELD1 VARCHAR(255),
    VERSIONSYSTEMFIELD2 VARCHAR(255),
    VERSIONSYSTEMFIELD3 VARCHAR(255),
    DELETED VARCHAR(1) default 'N' NOT NULL,
    STATUS INTEGER,
    CURRENCYNAME VARCHAR(255),
    CURRENCYSYMBOL VARCHAR(255),
    HOURSPERWORKDAY DOUBLE,
    MOREPROPS MEDIUMTEXT,
    DESCRIPTION VARCHAR(255),
    PREFIX VARCHAR(10),
    LASTID INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TPROJECTINDEX (TPUUID),
    INDEX TPROJECTINDEXIDPF (PREFIX));


# -----------------------------------------------------------------------
# TRELEASE
# -----------------------------------------------------------------------
drop table if exists TRELEASE;

CREATE TABLE TRELEASE
(
    PKEY INTEGER NOT NULL,
    LABEL VARCHAR(25),
    PROJKEY INTEGER NOT NULL,
    STATUS INTEGER,
    SORTORDER INTEGER,
    MOREPROPS MEDIUMTEXT,
    DESCRIPTION VARCHAR(255),
    DUEDATE DATETIME,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TRELINDEX1 (PROJKEY),
    INDEX TRELINDEX2 (TPUUID));


# -----------------------------------------------------------------------
# TROLE
# -----------------------------------------------------------------------
drop table if exists TROLE;

CREATE TABLE TROLE
(
    PKEY INTEGER NOT NULL,
    LABEL VARCHAR(25) NOT NULL,
    ACCESSKEY INTEGER,
    EXTENDEDACCESSKEY VARCHAR(30),
    PROJECTTYPE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TROLEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TSEVERITY
# -----------------------------------------------------------------------
drop table if exists TSEVERITY;

CREATE TABLE TSEVERITY
(
    PKEY INTEGER NOT NULL,
    LABEL VARCHAR(25),
    SORTORDER INTEGER,
    WLEVEL INTEGER,
    SYMBOL VARCHAR(255),
    ICONKEY INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TSEVINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPSEVERITY
# -----------------------------------------------------------------------
drop table if exists TPSEVERITY;

CREATE TABLE TPSEVERITY
(
    OBJECTID INTEGER NOT NULL,
    SEVERITY INTEGER,
    PROJECTTYPE INTEGER,
    LISTTYPE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TPSEVINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TSTATE
# -----------------------------------------------------------------------
drop table if exists TSTATE;

CREATE TABLE TSTATE
(
    PKEY INTEGER NOT NULL,
    LABEL VARCHAR(25) NOT NULL,
    STATEFLAG INTEGER,
    SORTORDER INTEGER,
    SYMBOL VARCHAR(255),
    ICONKEY INTEGER,
    PERCENTCOMPLETE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TSTATEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TSTATECHANGE
# -----------------------------------------------------------------------
drop table if exists TSTATECHANGE;

CREATE TABLE TSTATECHANGE
(
    STATECHANGEKEY INTEGER NOT NULL,
    WORKITEMKEY INTEGER NOT NULL,
    CHANGEDBY INTEGER NOT NULL,
    CHANGEDTO INTEGER NOT NULL,
    CHANGEDESCRIPTION MEDIUMTEXT,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(STATECHANGEKEY),
    INDEX TSTATECHANGEINDEX1 (CHANGEDBY),
    INDEX TSTATECHANGEINDEX2 (CHANGEDTO),
    INDEX TSTATECHANGEINDEX3 (WORKITEMKEY),
    INDEX TSTATECHANGEINDEX4 (TPUUID));


# -----------------------------------------------------------------------
# TTRAIL
# -----------------------------------------------------------------------
drop table if exists TTRAIL;

CREATE TABLE TTRAIL
(
    TRAILKEY INTEGER NOT NULL,
    WORKITEMKEY INTEGER NOT NULL,
    CHANGEDBY INTEGER NOT NULL,
    CHANGEDESCRIPTION MEDIUMTEXT,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(TRAILKEY),
    INDEX TTRAILINDEX1 (WORKITEMKEY),
    INDEX TTRAILINDEX2 (CHANGEDBY),
    INDEX TTRAILINDEX3 (TPUUID));


# -----------------------------------------------------------------------
# TWORKITEM
# -----------------------------------------------------------------------
drop table if exists TWORKITEM;

CREATE TABLE TWORKITEM
(
    WORKITEMKEY INTEGER NOT NULL,
    OWNER INTEGER NOT NULL,
    CHANGEDBY INTEGER NOT NULL,
    ORIGINATOR INTEGER,
    RESPONSIBLE INTEGER,
    PROJCATKEY INTEGER NOT NULL,
    CATEGORYKEY INTEGER NOT NULL,
    CLASSKEY INTEGER,
    PRIORITYKEY INTEGER NOT NULL,
    SEVERITYKEY INTEGER,
    SUPERIORWORKITEM INTEGER,
    PACKAGESYNOPSYS VARCHAR(80) NOT NULL,
    PACKAGEDESCRIPTION MEDIUMTEXT,
    REFERENCE VARCHAR(20),
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    RELNOTICEDKEY INTEGER,
    RELSCHEDULEDKEY INTEGER,
    BUILD VARCHAR(25),
    STATE INTEGER,
    STARTDATE DATETIME,
    ENDDATE DATETIME,
    SUBMITTEREMAIL VARCHAR(60),
    CREATED TIMESTAMP,
    ACTUALSTARTDATE DATETIME,
    ACTUALENDDATE DATETIME,
    WLEVEL VARCHAR(14),
    ACCESSLEVEL INTEGER,
    ARCHIVELEVEL INTEGER,
    TASKISMILESTONE CHAR(1) default 'N',
    TASKISSUBPROJECT CHAR(1) default 'N',
    TASKISSUMMARY CHAR(1) default 'N',
    TASKCONSTRAINT INTEGER,
    TASKCONSTRAINTDATE DATETIME,
    PSPCODE VARCHAR(255),
    IDNUMBER INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(WORKITEMKEY),
    INDEX TWIINDEX1 (PRIORITYKEY),
    INDEX TWIINDEX2 (SEVERITYKEY),
    INDEX TWIINDEX3 (CATEGORYKEY),
    INDEX TWIINDEX4 (CLASSKEY),
    INDEX TWIINDEX5 (CHANGEDBY),
    INDEX TWIINDEX6 (RELNOTICEDKEY),
    INDEX TWIINDEX7 (RELSCHEDULEDKEY),
    INDEX TWIINDEX8 (OWNER),
    INDEX TWIINDEX9 (ORIGINATOR),
    INDEX TWIINDEX10 (RESPONSIBLE),
    INDEX TWIINDEX11 (PROJCATKEY),
    INDEX TWIINDEX12 (STATE),
    INDEX TWIINDEX13 (STARTDATE),
    INDEX TWIINDEX14 (ENDDATE),
    INDEX TWIINDEX15 (ACTUALSTARTDATE),
    INDEX TWIINDEX16 (ACTUALENDDATE),
    INDEX TWIINDEX17 (WLEVEL),
    INDEX TWIINDEX18 (ACCESSLEVEL),
    INDEX TWIINDEX19 (ARCHIVELEVEL),
    INDEX TWIINDEX20 (TPUUID),
    INDEX TWIINDEX21 (IDNUMBER));


# -----------------------------------------------------------------------
# TCOMPUTEDVALUES
# -----------------------------------------------------------------------
drop table if exists TCOMPUTEDVALUES;

CREATE TABLE TCOMPUTEDVALUES
(
    PKEY INTEGER NOT NULL,
    WORKITEMKEY INTEGER NOT NULL,
    EFFORTTYPE INTEGER NOT NULL,
    COMPUTEDVALUETYPE INTEGER NOT NULL,
    COMPUTEDVALUE DOUBLE,
    MEASUREMENTUNIT INTEGER,
    PERSON INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TCVINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPRIVATEREPORTREPOSITORY
# -----------------------------------------------------------------------
drop table if exists TPRIVATEREPORTREPOSITORY;

CREATE TABLE TPRIVATEREPORTREPOSITORY
(
    PKEY INTEGER NOT NULL,
    OWNER INTEGER NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    QUERY MEDIUMTEXT NOT NULL,
    MENUITEM CHAR(1) default 'N',
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TPRIVREPREPINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPUBLICREPORTREPOSITORY
# -----------------------------------------------------------------------
drop table if exists TPUBLICREPORTREPOSITORY;

CREATE TABLE TPUBLICREPORTREPOSITORY
(
    PKEY INTEGER NOT NULL,
    OWNER INTEGER NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    QUERY MEDIUMTEXT NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TPUBREPREPINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPROJECTREPORTREPOSITORY
# -----------------------------------------------------------------------
drop table if exists TPROJECTREPORTREPOSITORY;

CREATE TABLE TPROJECTREPORTREPOSITORY
(
    PKEY INTEGER NOT NULL,
    PROJECT INTEGER NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    QUERY MEDIUMTEXT NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(PKEY),
    INDEX TPROJREPREPINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TACCOUNT
# -----------------------------------------------------------------------
drop table if exists TACCOUNT;

CREATE TABLE TACCOUNT
(
    OBJECTID INTEGER NOT NULL,
    ACCOUNTNUMBER VARCHAR(30) NOT NULL,
    ACCOUNTNAME VARCHAR(80),
    STATUS INTEGER,
    COSTCENTER INTEGER,
    DESCRIPTION VARCHAR(255),
    MOREPROPS MEDIUMTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TACCOUNTINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TATTACHMENT
# -----------------------------------------------------------------------
drop table if exists TATTACHMENT;

CREATE TABLE TATTACHMENT
(
    OBJECTID INTEGER NOT NULL,
    WORKITEM INTEGER NOT NULL,
    CHANGEDBY INTEGER,
    DOCUMENTSTATE INTEGER,
    FILENAME VARCHAR(256) NOT NULL,
    FILESIZE VARCHAR(20),
    MIMETYPE VARCHAR(15),
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    VERSION VARCHAR(20),
    DESCRIPTION MEDIUMTEXT,
    CRYPTKEY MEDIUMTEXT,
    ISENCRYPTED CHAR(1) default 'N',
    ISDELETED CHAR(1) default 'N',
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TATTACHINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TCOST
# -----------------------------------------------------------------------
drop table if exists TCOST;

CREATE TABLE TCOST
(
    OBJECTID INTEGER NOT NULL,
    ACCOUNT INTEGER,
    PERSON INTEGER,
    WORKITEM INTEGER,
    HOURS DOUBLE,
    COST DOUBLE,
    SUBJECT VARCHAR(255),
    EFFORTTYPE INTEGER,
    EFFORTVALUE DOUBLE,
    EFFORTDATE DATETIME,
    INVOICENUMBER VARCHAR(255),
    INVOICEDATE DATETIME,
    INVOICEPATH VARCHAR(255),
    DESCRIPTION VARCHAR(255),
    MOREPROPS MEDIUMTEXT,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TCOSTINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TEFFORTTYPE
# -----------------------------------------------------------------------
drop table if exists TEFFORTTYPE;

CREATE TABLE TEFFORTTYPE
(
    OBJECTID INTEGER NOT NULL,
    LABEL VARCHAR(255) NOT NULL,
    EFFORTUNIT INTEGER,
    DESCRIPTION VARCHAR(255),
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TEFFORTTYPEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TEFFORTUNIT
# -----------------------------------------------------------------------
drop table if exists TEFFORTUNIT;

CREATE TABLE TEFFORTUNIT
(
    OBJECTID INTEGER NOT NULL,
    LABEL VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(255),
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TEFFORTUNITINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TDOCSTATE
# -----------------------------------------------------------------------
drop table if exists TDOCSTATE;

CREATE TABLE TDOCSTATE
(
    OBJECTID INTEGER NOT NULL,
    LABEL VARCHAR(25) NOT NULL,
    STATEFLAG INTEGER,
    PROJECTTYPE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TDOCSTATEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TDISABLEFIELD
# -----------------------------------------------------------------------
drop table if exists TDISABLEFIELD;

CREATE TABLE TDISABLEFIELD
(
    OBJECTID INTEGER NOT NULL,
    FIELDNAME VARCHAR(25) NOT NULL,
    LISTTYPE INTEGER,
    PROJECTTYPE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TDISABLEFINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPLISTTYPE
# -----------------------------------------------------------------------
drop table if exists TPLISTTYPE;

CREATE TABLE TPLISTTYPE
(
    OBJECTID INTEGER NOT NULL,
    PROJECTTYPE INTEGER,
    CATEGORY INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TPLISTTYPEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPROJECTTYPE
# -----------------------------------------------------------------------
drop table if exists TPROJECTTYPE;

CREATE TABLE TPROJECTTYPE
(
    OBJECTID INTEGER NOT NULL,
    LABEL VARCHAR(35),
    NOTIFYOWNERLEVEL INTEGER,
    NOTIFYMANAGERLEVEL INTEGER,
    HOURSPERWORKDAY DOUBLE,
    MOREPROPS MEDIUMTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TPROJTYPEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPSTATE
# -----------------------------------------------------------------------
drop table if exists TPSTATE;

CREATE TABLE TPSTATE
(
    OBJECTID INTEGER NOT NULL,
    STATE INTEGER,
    PROJECTTYPE INTEGER,
    LISTTYPE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TPSTATEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TSITE
# -----------------------------------------------------------------------
drop table if exists TSITE;

CREATE TABLE TSITE
(
    OBJECTID INTEGER NOT NULL,
    TRACKVERSION VARCHAR(10),
    DBVERSION VARCHAR(10),
    LICENSEKEY VARCHAR(80),
    LICENSEEXT VARCHAR(255),
    EXPDATE DATE,
    NUMBEROFUSERS INTEGER,
    TRACKEMAIL VARCHAR(100),
    SMTPSERVERNAME VARCHAR(100),
    SMTPPORT INTEGER,
    MAILENCODING VARCHAR(20),
    SMTPUSER VARCHAR(100),
    SMTPPASSWORD VARCHAR(30),
    POPSERVERNAME VARCHAR(100),
    POPPORT INTEGER,
    POPUSER VARCHAR(100),
    POPPASSWORD VARCHAR(30),
    RECEIVINGPROTOCOL VARCHAR(6),
    ALLOWEDEMAILPATTERN MEDIUMTEXT,
    ISLDAPON CHAR(1),
    LDAPSERVERURL VARCHAR(100),
    LDAPATTRIBUTELOGINNAME VARCHAR(30),
    ATTACHMENTROOTDIR MEDIUMTEXT,
    SERVERURL VARCHAR(100),
    DESCRIPTIONLENGTH INTEGER,
    ISSELFREGISTERALLOWED CHAR(1) default 'Y',
    ISDEMOSITE CHAR(1) default 'N',
    USETRACKFROMADDRESS CHAR(1) default 'Y',
    RESERVEDUSE INTEGER,
    EXECUTABLE1 VARCHAR(255),
    EXECUTABLE2 VARCHAR(255),
    LDAPBINDDN VARCHAR(255),
    LDAPBINDPASSW VARCHAR(20),
    PREFERENCES MEDIUMTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TSITEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TWORKFLOW
# -----------------------------------------------------------------------
drop table if exists TWORKFLOW;

CREATE TABLE TWORKFLOW
(
    OBJECTID INTEGER NOT NULL,
    STATEFROM INTEGER,
    STATETO INTEGER,
    PROJECTTYPE INTEGER,
    RESPONSIBLE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TWORKFLOWINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TWORKFLOWROLE
# -----------------------------------------------------------------------
drop table if exists TWORKFLOWROLE;

CREATE TABLE TWORKFLOWROLE
(
    OBJECTID INTEGER NOT NULL,
    WORKFLOW INTEGER,
    MAYCHANGEROLE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TWORKFLOWROLEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TWORKFLOWCATEGORY
# -----------------------------------------------------------------------
drop table if exists TWORKFLOWCATEGORY;

CREATE TABLE TWORKFLOWCATEGORY
(
    OBJECTID INTEGER NOT NULL,
    WORKFLOW INTEGER,
    CATEGORY INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TWORKFLOWCATINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TROLELISTTYPE
# -----------------------------------------------------------------------
drop table if exists TROLELISTTYPE;

CREATE TABLE TROLELISTTYPE
(
    OBJECTID INTEGER NOT NULL,
    PROLE INTEGER NOT NULL,
    LISTTYPE INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TROLELISTTINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TISSUEATTRIBUTEVALUE
# -----------------------------------------------------------------------
drop table if exists TISSUEATTRIBUTEVALUE;

CREATE TABLE TISSUEATTRIBUTEVALUE
(
    OBJECTID INTEGER NOT NULL,
    ATTRIBUTEID INTEGER NOT NULL,
    DELETED INTEGER,
    ISSUE INTEGER NOT NULL,
    NUMERICVALUE INTEGER,
    TIMESTAMPVALUE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    LONGTEXTVALUE MEDIUMTEXT,
    OPTIONID INTEGER,
    PERSON INTEGER,
    CHARVALUE VARCHAR(255),
    DISPLAYVALUE VARCHAR(255),
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TISSUEATTINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TATTRIBUTEOPTION
# -----------------------------------------------------------------------
drop table if exists TATTRIBUTEOPTION;

CREATE TABLE TATTRIBUTEOPTION
(
    OBJECTID INTEGER NOT NULL,
    ATTRIBUTEID INTEGER NOT NULL,
    PARENTOPTION INTEGER,
    OPTIONNAME VARCHAR(255) NOT NULL,
    DELETED INTEGER,
    SORTORDER INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TATTOPTINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TATTRIBUTECLASS
# -----------------------------------------------------------------------
drop table if exists TATTRIBUTECLASS;

CREATE TABLE TATTRIBUTECLASS
(
    OBJECTID INTEGER NOT NULL,
    ATTRIBUTECLASSNAME VARCHAR(255) NOT NULL,
    ATTRIBUTECLASSDESC VARCHAR(64),
    JAVACLASSNAME VARCHAR(255),
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TATTCLASSINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TATTRIBUTETYPE
# -----------------------------------------------------------------------
drop table if exists TATTRIBUTETYPE;

CREATE TABLE TATTRIBUTETYPE
(
    OBJECTID INTEGER NOT NULL,
    ATTRIBUTETYPENAME VARCHAR(255) NOT NULL,
    ATTRIBUTECLASS INTEGER NOT NULL,
    JAVACLASSNAME VARCHAR(255),
    VALIDATIONKEY VARCHAR(25),
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TATTTYPEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TATTRIBUTE
# -----------------------------------------------------------------------
drop table if exists TATTRIBUTE;

CREATE TABLE TATTRIBUTE
(
    OBJECTID INTEGER NOT NULL,
    ATTRIBUTENAME VARCHAR(255) NOT NULL,
    ATTRIBUTETYPE INTEGER NOT NULL,
    DELETED INTEGER,
    DESCRIPTION VARCHAR(64),
    PERMISSION VARCHAR(255),
    REQUIREDOPTION INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TATTRIBUTEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TREPORTLAYOUT
# -----------------------------------------------------------------------
drop table if exists TREPORTLAYOUT;

CREATE TABLE TREPORTLAYOUT
(
    OBJECTID INTEGER NOT NULL,
    PROJECTTYPE INTEGER,
    PROJECT INTEGER,
    PERSON INTEGER,
    REPORTFIELD INTEGER NOT NULL,
    FPOSITION INTEGER NOT NULL,
    FWIDTH INTEGER NOT NULL,
    FSORTORDER INTEGER,
    FSORTDIR VARCHAR(1),
    FIELDTYPE INTEGER,
    EXPANDING VARCHAR(1),
    LAYOUTKEY INTEGER,
    QUERYID INTEGER,
    QUERYTYPE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TREPORTLAYOUTINDEX1 (FIELDTYPE),
    INDEX TREPORTLAYOUTINDEX2 (LAYOUTKEY),
    INDEX TREPORTLAYOUTINDEX3 (TPUUID),
    INDEX TREPORTLAYOUTINDEX4 (QUERYID));


# -----------------------------------------------------------------------
# TSCHEDULER
# -----------------------------------------------------------------------
drop table if exists TSCHEDULER;

CREATE TABLE TSCHEDULER
(
    OBJECTID INTEGER NOT NULL,
    CRONWHEN VARCHAR(64) NOT NULL,
    JAVACLASSNAME VARCHAR(255),
    EXTERNALEXE VARCHAR(255),
    PERSON INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TSCHEDULERINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TPROJECTACCOUNT
# -----------------------------------------------------------------------
drop table if exists TPROJECTACCOUNT;

CREATE TABLE TPROJECTACCOUNT
(
    ACCOUNT INTEGER NOT NULL,
    PROJECT INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(ACCOUNT,PROJECT),
    INDEX TPROJACCINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TGROUPMEMBER
# -----------------------------------------------------------------------
drop table if exists TGROUPMEMBER;

CREATE TABLE TGROUPMEMBER
(
    OBJECTID INTEGER NOT NULL,
    THEGROUP INTEGER NOT NULL,
    PERSON INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TGROUPMEMINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TBUDGET
# -----------------------------------------------------------------------
drop table if exists TBUDGET;

CREATE TABLE TBUDGET
(
    OBJECTID INTEGER NOT NULL,
    WORKITEMKEY INTEGER NOT NULL,
    ESTIMATEDHOURS DOUBLE,
    TIMEUNIT INTEGER,
    ESTIMATEDCOST DOUBLE,
    EFFORTTYPE INTEGER,
    EFFORTVALUE DOUBLE,
    CHANGEDESCRIPTION VARCHAR(255),
    MOREPROPS MEDIUMTEXT,
    CHANGEDBY INTEGER,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TBUDGETINDEX1 (WORKITEMKEY),
    INDEX TBUDGETINDEX2 (CHANGEDBY),
    INDEX TBUDGETINDEX3 (TPUUID));


# -----------------------------------------------------------------------
# TACTUALESTIMATEDBUDGET
# -----------------------------------------------------------------------
drop table if exists TACTUALESTIMATEDBUDGET;

CREATE TABLE TACTUALESTIMATEDBUDGET
(
    OBJECTID INTEGER NOT NULL,
    WORKITEMKEY INTEGER NOT NULL,
    ESTIMATEDHOURS DOUBLE,
    TIMEUNIT INTEGER,
    ESTIMATEDCOST DOUBLE,
    EFFORTTYPE INTEGER,
    EFFORTVALUE DOUBLE,
    CHANGEDBY INTEGER,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TACTUALESTINDEX1 (WORKITEMKEY),
    INDEX TACTUALESTINDEX2 (CHANGEDBY),
    INDEX TACTUALESTINDEX3 (TPUUID));


# -----------------------------------------------------------------------
# TSYSTEMSTATE
# -----------------------------------------------------------------------
drop table if exists TSYSTEMSTATE;

CREATE TABLE TSYSTEMSTATE
(
    OBJECTID INTEGER NOT NULL,
    LABEL VARCHAR(255),
    STATEFLAG INTEGER,
    SYMBOL VARCHAR(255),
    ENTITYFLAG INTEGER,
    SORTORDER INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TSYSTEMSTATEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TCOSTCENTER
# -----------------------------------------------------------------------
drop table if exists TCOSTCENTER;

CREATE TABLE TCOSTCENTER
(
    OBJECTID INTEGER NOT NULL,
    COSTCENTERNUMBER VARCHAR(30) NOT NULL,
    COSTCENTERNAME VARCHAR(80),
    MOREPROPS MEDIUMTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TCOSTCENTERINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TMOTD
# -----------------------------------------------------------------------
drop table if exists TMOTD;

CREATE TABLE TMOTD
(
    OBJECTID INTEGER NOT NULL,
    THELOCALE VARCHAR(2),
    THEMESSAGE MEDIUMTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TMOTDINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TDASHBOARDSCREEN
# -----------------------------------------------------------------------
drop table if exists TDASHBOARDSCREEN;

CREATE TABLE TDASHBOARDSCREEN
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    LABEL VARCHAR(255),
    PERSONPKEY INTEGER NOT NULL,
    PROJECT INTEGER,
    ENTITYTYPE INTEGER,
    DESCRIPTION MEDIUMTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TDBSCREENINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TDASHBOARDTAB
# -----------------------------------------------------------------------
drop table if exists TDASHBOARDTAB;

CREATE TABLE TDASHBOARDTAB
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    LABEL VARCHAR(255),
    DESCRIPTION MEDIUMTEXT,
    SORTORDER INTEGER,
    PARENT INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TDBTABINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TDASHBOARDPANEL
# -----------------------------------------------------------------------
drop table if exists TDASHBOARDPANEL;

CREATE TABLE TDASHBOARDPANEL
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    LABEL VARCHAR(255),
    DESCRIPTION MEDIUMTEXT,
    SORTORDER INTEGER,
    ROWSNO INTEGER NOT NULL,
    COLSNO INTEGER NOT NULL,
    PARENT INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TDBPANELINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TDASHBOARDFIELD
# -----------------------------------------------------------------------
drop table if exists TDASHBOARDFIELD;

CREATE TABLE TDASHBOARDFIELD
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION MEDIUMTEXT,
    SORTORDER INTEGER,
    COLINDEX INTEGER,
    ROWINDEX INTEGER,
    COLSPAN INTEGER,
    ROWSPAN INTEGER,
    PARENT INTEGER NOT NULL,
    DASHBOARDID VARCHAR(255) NOT NULL,
    THEDESCRIPTION VARCHAR(255),
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TDBFIELDINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TDASHBOARDPARAMETER
# -----------------------------------------------------------------------
drop table if exists TDASHBOARDPARAMETER;

CREATE TABLE TDASHBOARDPARAMETER
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    PARAMVALUE MEDIUMTEXT,
    DASHBOARDFIELD INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TDBPARAMINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TVERSIONCONTROLPARAMETER
# -----------------------------------------------------------------------
drop table if exists TVERSIONCONTROLPARAMETER;

CREATE TABLE TVERSIONCONTROLPARAMETER
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    PARAMVALUE MEDIUMTEXT,
    PROJECT INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TVCPARAMINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TFIELD
# -----------------------------------------------------------------------
drop table if exists TFIELD;

CREATE TABLE TFIELD
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    FIELDTYPE VARCHAR(255) NOT NULL,
    DEPRECATED CHAR(1) default 'N',
    ISCUSTOM CHAR(1) default 'Y' NOT NULL,
    REQUIRED CHAR(1) default 'N' NOT NULL,
    DESCRIPTION MEDIUMTEXT,
    OWNER INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TFIELDINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TFIELDCONFIG
# -----------------------------------------------------------------------
drop table if exists TFIELDCONFIG;

CREATE TABLE TFIELDCONFIG
(
    OBJECTID INTEGER NOT NULL,
    FIELDKEY INTEGER NOT NULL,
    LABEL VARCHAR(255) NOT NULL,
    TOOLTIP VARCHAR(255),
    REQUIRED CHAR(1) default 'N' NOT NULL,
    HISTORY CHAR(1) default 'N' NOT NULL,
    ISSUETYPE INTEGER,
    PROJECTTYPE INTEGER,
    PROJECT INTEGER,
    DESCRIPTION MEDIUMTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TFIELDCONFIGINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TROLEFIELD
# -----------------------------------------------------------------------
drop table if exists TROLEFIELD;

CREATE TABLE TROLEFIELD
(
    OBJECTID INTEGER NOT NULL,
    ROLEKEY INTEGER NOT NULL,
    FIELDKEY INTEGER NOT NULL,
    ACCESSRIGHT INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TROLEFIELDINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TCONFIGOPTIONSROLE
# -----------------------------------------------------------------------
drop table if exists TCONFIGOPTIONSROLE;

CREATE TABLE TCONFIGOPTIONSROLE
(
    OBJECTID INTEGER NOT NULL,
    CONFIGKEY INTEGER NOT NULL,
    ROLEKEY INTEGER NOT NULL,
    OPTIONKEY INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TCONFIGOPTINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TTEXTBOXSETTINGS
# -----------------------------------------------------------------------
drop table if exists TTEXTBOXSETTINGS;

CREATE TABLE TTEXTBOXSETTINGS
(
    OBJECTID INTEGER NOT NULL,
    CONFIG INTEGER NOT NULL,
    REQUIRED VARCHAR(1) default 'N' NOT NULL,
    DEFAULTTEXT VARCHAR(255),
    DEFAULTINTEGER INTEGER,
    DEFAULTDOUBLE DOUBLE,
    DEFAULTDATE DATETIME,
    DEFAULTCHAR CHAR(1),
    DEFAULTOPTION INTEGER,
    MINOPTION INTEGER,
    MAXOPTION INTEGER,
    MINTEXTLENGTH INTEGER,
    MAXTEXTLENGTH INTEGER,
    MINDATE DATETIME,
    MAXDATE DATETIME,
    MININTEGER INTEGER,
    MAXINTEGER INTEGER,
    MINDOUBLE DOUBLE,
    MAXDOUBLE DOUBLE,
    MAXDECIMALDIGIT INTEGER,
    PARAMETERCODE INTEGER,
    VALIDVALUE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TTEXTBOXCONFINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TGENERALSETTINGS
# -----------------------------------------------------------------------
drop table if exists TGENERALSETTINGS;

CREATE TABLE TGENERALSETTINGS
(
    OBJECTID INTEGER NOT NULL,
    CONFIG INTEGER NOT NULL,
    INTEGERVALUE INTEGER,
    DOUBLEVALUE DOUBLE,
    TEXTVALUE VARCHAR(255),
    DATEVALUE DATETIME,
    CHARACTERVALUE CHAR(1),
    PARAMETERCODE INTEGER,
    VALIDVALUE INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TGENERALCONFINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TLIST
# -----------------------------------------------------------------------
drop table if exists TLIST;

CREATE TABLE TLIST
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION MEDIUMTEXT,
    PARENTLIST INTEGER,
    LISTTYPE INTEGER,
    CHILDNUMBER INTEGER,
    DELETED CHAR(1) default 'N',
    REPOSITORYTYPE INTEGER,
    PROJECT INTEGER,
    OWNER INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TLISTINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TOPTION
# -----------------------------------------------------------------------
drop table if exists TOPTION;

CREATE TABLE TOPTION
(
    OBJECTID INTEGER NOT NULL,
    LIST INTEGER NOT NULL,
    LABEL VARCHAR(255) NOT NULL,
    PARENTOPTION INTEGER,
    SORTORDER INTEGER,
    ISDEFAULT CHAR(1) default 'N' NOT NULL,
    DELETED CHAR(1) default 'N',
    ICONKEY INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TOPTIONINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TOPTIONSETTINGS
# -----------------------------------------------------------------------
drop table if exists TOPTIONSETTINGS;

CREATE TABLE TOPTIONSETTINGS
(
    OBJECTID INTEGER NOT NULL,
    LIST INTEGER NOT NULL,
    CONFIG INTEGER NOT NULL,
    PARAMETERCODE INTEGER,
    MULTIPLE CHAR(1) default 'N',
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TOPTIONCONFINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TATTRIBUTEVALUE
# -----------------------------------------------------------------------
drop table if exists TATTRIBUTEVALUE;

CREATE TABLE TATTRIBUTEVALUE
(
    OBJECTID INTEGER NOT NULL,
    FIELDKEY INTEGER NOT NULL,
    WORKITEM INTEGER NOT NULL,
    TEXTVALUE VARCHAR(255),
    INTEGERVALUE INTEGER,
    DOUBLEVALUE DOUBLE,
    DATEVALUE DATETIME,
    CHARACTERVALUE CHAR(1),
    LONGTEXTVALUE MEDIUMTEXT,
    SYSTEMOPTIONID INTEGER,
    SYSTEMOPTIONTYPE INTEGER,
    CUSTOMOPTIONID INTEGER,
    PARAMETERCODE INTEGER,
    VALIDVALUE INTEGER,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TATTVALUEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TSCREEN
# -----------------------------------------------------------------------
drop table if exists TSCREEN;

CREATE TABLE TSCREEN
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    LABEL VARCHAR(255),
    DESCRIPTION MEDIUMTEXT,
    OWNER INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TSCREENINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TSCREENTAB
# -----------------------------------------------------------------------
drop table if exists TSCREENTAB;

CREATE TABLE TSCREENTAB
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    LABEL VARCHAR(255),
    DESCRIPTION MEDIUMTEXT,
    SORTORDER INTEGER,
    PARENT INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TSCREENTABINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TSCREENPANEL
# -----------------------------------------------------------------------
drop table if exists TSCREENPANEL;

CREATE TABLE TSCREENPANEL
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    LABEL VARCHAR(255),
    DESCRIPTION MEDIUMTEXT,
    SORTORDER INTEGER,
    ROWSNO INTEGER NOT NULL,
    COLSNO INTEGER NOT NULL,
    PARENT INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TSCREENPANELINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TSCREENFIELD
# -----------------------------------------------------------------------
drop table if exists TSCREENFIELD;

CREATE TABLE TSCREENFIELD
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION MEDIUMTEXT,
    SORTORDER INTEGER,
    COLINDEX INTEGER,
    ROWINDEX INTEGER,
    COLSPAN INTEGER,
    ROWSPAN INTEGER,
    LABELHALIGN INTEGER NOT NULL,
    LABELVALIGN INTEGER NOT NULL,
    VALUEHALIGN INTEGER NOT NULL,
    VALUEVALIGN INTEGER NOT NULL,
    ISEMPTY CHAR(1) default 'N' NOT NULL,
    PARENT INTEGER NOT NULL,
    FIELDKEY INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TSCREENFIELDINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TACTION
# -----------------------------------------------------------------------
drop table if exists TACTION;

CREATE TABLE TACTION
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    LABEL VARCHAR(255),
    DESCRIPTION MEDIUMTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TACTIONINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TSCREENCONFIG
# -----------------------------------------------------------------------
drop table if exists TSCREENCONFIG;

CREATE TABLE TSCREENCONFIG
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255),
    DESCRIPTION MEDIUMTEXT,
    SCREEN INTEGER,
    ISSUETYPE INTEGER,
    PROJECTTYPE INTEGER,
    PROJECT INTEGER,
    ACTIONKEY INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TSCREENCONFINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TINITSTATE
# -----------------------------------------------------------------------
drop table if exists TINITSTATE;

CREATE TABLE TINITSTATE
(
    OBJECTID INTEGER NOT NULL,
    PROJECT INTEGER NOT NULL,
    LISTTYPE INTEGER NOT NULL,
    STATEKEY INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TINITSTATEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TEVENT
# -----------------------------------------------------------------------
drop table if exists TEVENT;

CREATE TABLE TEVENT
(
    OBJECTID INTEGER NOT NULL,
    EVENTNAME VARCHAR(255),
    EVENTTYPE INTEGER NOT NULL,
    PROJECTTYPE INTEGER,
    PROJECT INTEGER,
    EVENTSCRIPT INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TEVENTINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TCLOB
# -----------------------------------------------------------------------
drop table if exists TCLOB;

CREATE TABLE TCLOB
(
    OBJECTID INTEGER NOT NULL,
    CLOBVALUE LONGTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TCLOBINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TNOTIFYFIELD
# -----------------------------------------------------------------------
drop table if exists TNOTIFYFIELD;

CREATE TABLE TNOTIFYFIELD
(
    OBJECTID INTEGER NOT NULL,
    FIELD INTEGER,
    ACTIONTYPE INTEGER NOT NULL,
    FIELDTYPE INTEGER,
    NOTIFYTRIGGER INTEGER NOT NULL,
    ORIGINATOR CHAR(1) default 'N',
    MANAGER CHAR(1) default 'N',
    RESPONSIBLE CHAR(1) default 'N',
    CONSULTANT CHAR(1) default 'N',
    INFORMANT CHAR(1) default 'N',
    OBSERVER CHAR(1) default 'N',
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TNOTIFYFIELDINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TNOTIFYTRIGGER
# -----------------------------------------------------------------------
drop table if exists TNOTIFYTRIGGER;

CREATE TABLE TNOTIFYTRIGGER
(
    OBJECTID INTEGER NOT NULL,
    LABEL VARCHAR(255),
    PERSON INTEGER,
    ISSYSTEM CHAR(1) default 'N',
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TNOTIFYTRIGGERINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TNOTIFYSETTINGS
# -----------------------------------------------------------------------
drop table if exists TNOTIFYSETTINGS;

CREATE TABLE TNOTIFYSETTINGS
(
    OBJECTID INTEGER NOT NULL,
    PERSON INTEGER,
    PROJECT INTEGER,
    NOTIFYTRIGGER INTEGER,
    NOTIFYFILTER INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TNOTIFYCONFINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TQUERYREPOSITORY
# -----------------------------------------------------------------------
drop table if exists TQUERYREPOSITORY;

CREATE TABLE TQUERYREPOSITORY
(
    OBJECTID INTEGER NOT NULL,
    PERSON INTEGER,
    PROJECT INTEGER,
    LABEL VARCHAR(100),
    QUERYTYPE INTEGER NOT NULL,
    REPOSITORYTYPE INTEGER NOT NULL,
    QUERYKEY INTEGER NOT NULL,
    MENUITEM CHAR(1) default 'N',
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TQUERYREPINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TLOCALIZEDRESOURCES
# -----------------------------------------------------------------------
drop table if exists TLOCALIZEDRESOURCES;

CREATE TABLE TLOCALIZEDRESOURCES
(
    OBJECTID INTEGER NOT NULL,
    TABLENAME VARCHAR(255),
    PRIMARYKEYVALUE INTEGER,
    FIELDNAME VARCHAR(255) NOT NULL,
    LOCALIZEDTEXT VARCHAR(255),
    LOCALE VARCHAR(20),
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TLOCALRESINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TLINKTYPE
# -----------------------------------------------------------------------
drop table if exists TLINKTYPE;

CREATE TABLE TLINKTYPE
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    REVERSENAME VARCHAR(255),
    LEFTTORIGHTFIRST VARCHAR(255),
    LEFTTORIGHTLEVEL VARCHAR(255),
    LEFTTORIGHTALL VARCHAR(255),
    RIGHTTOLEFTFIRST VARCHAR(255),
    RIGHTTOLEFTLEVEL VARCHAR(255),
    RIGHTTOLEFTALL VARCHAR(255),
    LINKDIRECTION INTEGER,
    OUTWARDICONKEY INTEGER,
    INWARDICONKEY INTEGER,
    LINKTYPEPLUGIN VARCHAR(255),
    MOREPROPS MEDIUMTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TLINKTYPEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TWORKITEMLINK
# -----------------------------------------------------------------------
drop table if exists TWORKITEMLINK;

CREATE TABLE TWORKITEMLINK
(
    OBJECTID INTEGER NOT NULL,
    LINKISCROSSPROJECT CHAR(1) default 'N',
    LINKPRED INTEGER NOT NULL,
    LINKSUCC INTEGER,
    LINKTYPE INTEGER NOT NULL,
	LINKDIRECTION INTEGER,
    LINKLAG DOUBLE,
    LINKLAGFORMAT INTEGER,
    STRINGVALUE1 VARCHAR(255),
    STRINGVALUE2 VARCHAR(255),
    STRINGVALUE3 VARCHAR(255),
    INTEGERVALUE1 INTEGER,
    INTEGERVALUE2 INTEGER,
    INTEGERVALUE3 INTEGER,
    DATEVALUE DATETIME,
    DESCRIPTION MEDIUMTEXT,
    EXTERNALLINK VARCHAR(255),
	CHANGEDBY INTEGER,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TWORKITEMLINKINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TLOGGINGLEVEL
# -----------------------------------------------------------------------
drop table if exists TLOGGINGLEVEL;

CREATE TABLE TLOGGINGLEVEL
(
    OBJECTID INTEGER NOT NULL,
    THECLASSNAME VARCHAR(255) NOT NULL,
    LOGLEVEL VARCHAR(10) NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TLOGLEVELINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TWORKITEMLOCK
# -----------------------------------------------------------------------
drop table if exists TWORKITEMLOCK;

CREATE TABLE TWORKITEMLOCK
(
    WORKITEM INTEGER NOT NULL,
    PERSON INTEGER NOT NULL,
    HTTPSESSION VARCHAR(255) NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(WORKITEM),
    INDEX TWORKITEMLOCKINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TEXPORTTEMPLATE
# -----------------------------------------------------------------------
drop table if exists TEXPORTTEMPLATE;

CREATE TABLE TEXPORTTEMPLATE
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    REPORTTYPE VARCHAR(255) NOT NULL,
    EXPORTFORMAT VARCHAR(255) NOT NULL,
    REPOSITORYTYPE INTEGER NOT NULL,
    DESCRIPTION MEDIUMTEXT,
    PROJECT INTEGER,
    PERSON INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TEXPOPRTTEMPINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TEMAILPROCESSED
# -----------------------------------------------------------------------
drop table if exists TEMAILPROCESSED;

CREATE TABLE TEMAILPROCESSED
(
    OBJECTID INTEGER NOT NULL,
    PROCESSEDDATE DATETIME NOT NULL,
    MESSAGEUID VARCHAR(255) NOT NULL,
    RECEIVEDAT VARCHAR(255) NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TEMAILPROCINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TAPPLICATIONCONTEXT
# -----------------------------------------------------------------------
drop table if exists TAPPLICATIONCONTEXT;

CREATE TABLE TAPPLICATIONCONTEXT
(
    OBJECTID INTEGER NOT NULL,
    LOGGEDFULLUSERS INTEGER,
    LOGGEDLIMITEDUSERS INTEGER,
    REFRESHCONFIGURATION INTEGER default 0 NOT NULL,
    FIRSTTIME INTEGER default 0 NOT NULL,
    MOREPROPS MEDIUMTEXT,
    PRIMARY KEY(OBJECTID));


# -----------------------------------------------------------------------
# TLOGGEDINUSERS
# -----------------------------------------------------------------------
drop table if exists TLOGGEDINUSERS;

CREATE TABLE TLOGGEDINUSERS
(
    OBJECTID INTEGER NOT NULL,
    NODEADDRESS INTEGER,
	SESSIONID VARCHAR(255),
    LOGGEDUSER INTEGER NOT NULL,
    USERLEVEL INTEGER,
    LASTUPDATE TIMESTAMP,
    MOREPROPS MEDIUMTEXT,
    PRIMARY KEY(OBJECTID));


# -----------------------------------------------------------------------
# CLUSTERNODE
# -----------------------------------------------------------------------
drop table if exists CLUSTERNODE;

CREATE TABLE CLUSTERNODE
(
    OBJECTID INTEGER NOT NULL,
    NODEADDRESS VARCHAR(40),
    NODEURL VARCHAR(255),
    LASTUPDATE TIMESTAMP,
    MASTERNODE INTEGER default 0,
    RELOADCONFIG INTEGER default 0,
    PRIMARY KEY(OBJECTID));


# -----------------------------------------------------------------------
# TENTITYCHANGES
# -----------------------------------------------------------------------
drop table if exists TENTITYCHANGES;

CREATE TABLE TENTITYCHANGES
(
    OBJECTID INTEGER NOT NULL,
    ENTITYKEY INTEGER NOT NULL,
    ENTITYTYPE INTEGER NOT NULL,
    CLUSTERNODE INTEGER,
    PRIMARY KEY(OBJECTID),
    INDEX ENTITYCHANGESINDEX1 (ENTITYTYPE),
    INDEX ENTITYCHANGESINDEX2 (CLUSTERNODE));


# -----------------------------------------------------------------------
# TSUMMARYMAIL
# -----------------------------------------------------------------------
drop table if exists TSUMMARYMAIL;

CREATE TABLE TSUMMARYMAIL
(
    OBJECTID INTEGER NOT NULL,
    WORKITEM INTEGER NOT NULL,
    PERSONFROM INTEGER NOT NULL,
    FROMADDRESS VARCHAR(255),
    MAILSUBJECT VARCHAR(255),
    WORKITEMLINK VARCHAR(255),
    PERSONTO INTEGER NOT NULL,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TSUMMARYMAILINDEX1 (WORKITEM),
    INDEX TSUMMARYMAILINDEX2 (PERSONFROM),
    INDEX TSUMMARYMAILINDEX3 (PERSONTO),
    INDEX TSUMMARYMAILINDEX4 (TPUUID));


# -----------------------------------------------------------------------
# TOUTLINECODE
# -----------------------------------------------------------------------
drop table if exists TOUTLINECODE;

CREATE TABLE TOUTLINECODE
(
    OBJECTID INTEGER NOT NULL,
    PARENTID INTEGER,
    LEVELNO INTEGER NOT NULL,
    LEVELCODE VARCHAR(50) NOT NULL,
    FULLCODE VARCHAR(255) NOT NULL,
    ENTITYID INTEGER NOT NULL,
    OUTLINETEMPLATE INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TOUTLINECODEINDEX1 (PARENTID),
    INDEX TOUTLINECODEINDEX2 (ENTITYID),
    INDEX TOUTLINECODEINDEX3 (TPUUID));


# -----------------------------------------------------------------------
# TOUTLINETEMPLATEDEF
# -----------------------------------------------------------------------
drop table if exists TOUTLINETEMPLATEDEF;

CREATE TABLE TOUTLINETEMPLATEDEF
(
    OBJECTID INTEGER NOT NULL,
    LEVELNO INTEGER NOT NULL,
    SEQUENCETYPE INTEGER NOT NULL,
    LISTID INTEGER,
    SEQUENCELENGTH INTEGER NOT NULL,
    SEPARATORCHAR VARCHAR(10) NOT NULL,
    OUTLINETEMPLATE INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TOUTLINETEMPLATEDEFINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TOUTLINETEMPLATE
# -----------------------------------------------------------------------
drop table if exists TOUTLINETEMPLATE;

CREATE TABLE TOUTLINETEMPLATE
(
    OBJECTID INTEGER NOT NULL,
    LABEL VARCHAR(255) NOT NULL,
    ENTITYTYPE INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TOUTLINETEMPLATEINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# THISTORYTRANSACTION
# -----------------------------------------------------------------------
drop table if exists THISTORYTRANSACTION;

CREATE TABLE THISTORYTRANSACTION
(
    OBJECTID INTEGER NOT NULL,
    WORKITEM INTEGER NOT NULL,
    CHANGEDBY INTEGER NOT NULL,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX THISTORYTRANSINDEX1 (WORKITEM),
    INDEX THISTORYTRANSINDEX2 (CHANGEDBY),
    INDEX THISTORYTRANSINDEX3 (TPUUID));


# -----------------------------------------------------------------------
# TFIELDCHANGE
# -----------------------------------------------------------------------
drop table if exists TFIELDCHANGE;

CREATE TABLE TFIELDCHANGE
(
    OBJECTID INTEGER NOT NULL,
    FIELDKEY INTEGER NOT NULL,
    HISTORYTRANSACTION INTEGER NOT NULL,
    NEWTEXTVALUE VARCHAR(255),
    OLDTEXTVALUE VARCHAR(255),
    NEWINTEGERVALUE INTEGER,
    OLDINTEGERVALUE INTEGER,
    NEWDOUBLEVALUE DOUBLE,
    OLDDOUBLEVALUE DOUBLE,
    NEWDATEVALUE DATETIME,
    OLDDATEVALUE DATETIME,
    NEWCHARACTERVALUE CHAR(1),
    OLDCHARACTERVALUE CHAR(1),
    NEWLONGTEXTVALUE MEDIUMTEXT,
    OLDLONGTEXTVALUE MEDIUMTEXT,
    NEWSYSTEMOPTIONID INTEGER,
    OLDSYSTEMOPTIONID INTEGER,
    SYSTEMOPTIONTYPE INTEGER,
    NEWCUSTOMOPTIONID INTEGER,
    OLDCUSTOMOPTIONID INTEGER,
    PARAMETERCODE INTEGER,
    VALIDVALUE INTEGER,
    PARENTCOMMENT INTEGER,
    TIMESEDITED INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TFIELDCHANGE1 (FIELDKEY),
    INDEX TFIELDCHANGE2 (HISTORYTRANSACTION),
    INDEX TFIELDCHANGE3 (VALIDVALUE),
    INDEX TFIELDCHANGE4 (TPUUID));


# -----------------------------------------------------------------------
# TSCRIPTS
# -----------------------------------------------------------------------
drop table if exists TSCRIPTS;

CREATE TABLE TSCRIPTS
(
    OBJECTID INTEGER NOT NULL,
    CHANGEDBY INTEGER,
    LASTEDIT TIMESTAMP,
    SCRIPTVERSION INTEGER,
    ORIGINALVERSION INTEGER,
    PROJECTTYPE INTEGER,
    PROJECT INTEGER,
    SCRIPTTYPE INTEGER,
    CLAZZNAME VARCHAR(253),
    SOURCECODE MEDIUMTEXT,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TSCRIPTINDEX1 (CLAZZNAME),
    INDEX TSCRIPTINDEX2 (PROJECTTYPE),
    INDEX TSCRIPTINDEX3 (PROJECT),
    INDEX TSCRIPTINDEX4 (TPUUID));


# -----------------------------------------------------------------------
# TREVISION
# -----------------------------------------------------------------------
drop table if exists TREVISION;

CREATE TABLE TREVISION
(
    OBJECTID INTEGER NOT NULL,
    FILENAME VARCHAR(255) NOT NULL,
    AUTHORNAME VARCHAR(255) NOT NULL,
    CHANGEDESCRIPTION MEDIUMTEXT,
    REVISIONDATE TIMESTAMP NOT NULL,
    REVISIONNUMBR VARCHAR(255) NOT NULL,
    REPOSITORYKEY INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TREVISIONINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TREVISIONWORKITEMS
# -----------------------------------------------------------------------
drop table if exists TREVISIONWORKITEMS;

CREATE TABLE TREVISIONWORKITEMS
(
    OBJECTID INTEGER NOT NULL,
    WORKITEMKEY INTEGER NOT NULL,
    REVISIONKEY INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TREVISIONWORKITEMSINDEX1 (WORKITEMKEY),
    INDEX TREVISIONWORKITEMSINDEX2 (TPUUID));


# -----------------------------------------------------------------------
# TREPOSITORY
# -----------------------------------------------------------------------
drop table if exists TREPOSITORY;

CREATE TABLE TREPOSITORY
(
    OBJECTID INTEGER NOT NULL,
    REPOSITORYTYPE VARCHAR(255) NOT NULL,
    REPOSITORYURL VARCHAR(255) NOT NULL,
    STARTDATE TIMESTAMP,
    ENDDATE TIMESTAMP,
    STATUSKEY INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TREPOSITORYINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TTEMPLATEPERSON
# -----------------------------------------------------------------------
drop table if exists TTEMPLATEPERSON;

CREATE TABLE TTEMPLATEPERSON
(
    OBJECTID INTEGER NOT NULL,
    REPORTTEMPLATE INTEGER NOT NULL,
    PERSON INTEGER NOT NULL,
    RIGHTFLAG INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TTEMPLATEPERSONINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TBLOB
# -----------------------------------------------------------------------
drop table if exists TBLOB;

CREATE TABLE TBLOB
(
    OBJECTID INTEGER NOT NULL,
    BLOBVALUE LONGBLOB,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TBLOBINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TRECURRENCEPATTERN
# -----------------------------------------------------------------------
drop table if exists TRECURRENCEPATTERN;

CREATE TABLE TRECURRENCEPATTERN
(
    OBJECTID INTEGER NOT NULL,
    RECURRENCEPERIOD INTEGER NOT NULL,
    PARAM1 INTEGER,
    PARAM2 INTEGER,
    PARAM3 INTEGER,
    DAYS VARCHAR(255),
    DATEISABSOLUTE CHAR(1) default 'Y',
    STARTDATE DATETIME,
    ENDDATE DATETIME,
    OCCURENCETYPE INTEGER,
    NOOFOCCURENCES INTEGER,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TRECURRENCEPATTERNINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TREPORTPERSONSETTINGS
# -----------------------------------------------------------------------
drop table if exists TREPORTPERSONSETTINGS;

CREATE TABLE TREPORTPERSONSETTINGS
(
    OBJECTID INTEGER NOT NULL,
    PERSON INTEGER NOT NULL,
    RECURRENCEPATTERN INTEGER,
    REPORTTEMPLATE INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TREPORTPERSONSETTINGSINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TREPORTPARAMETER
# -----------------------------------------------------------------------
drop table if exists TREPORTPARAMETER;

CREATE TABLE TREPORTPARAMETER
(
    OBJECTID INTEGER NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    PARAMVALUE MEDIUMTEXT,
    REPORTPERSONSETTINGS INTEGER NOT NULL,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TREPORTPARAMETERINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TMSPROJECTTASK
# -----------------------------------------------------------------------
drop table if exists TMSPROJECTTASK;

CREATE TABLE TMSPROJECTTASK
(
    OBJECTID INTEGER NOT NULL,
    WORKITEM INTEGER NOT NULL,
    UNIQUEID INTEGER NOT NULL,
    TASKTYPE INTEGER NOT NULL,
    CONTACT VARCHAR(100),
    WBS VARCHAR(100),
    OUTLINENUMBER VARCHAR(100),
    DURATION VARCHAR(100),
    DURATIONFORMAT INTEGER,
    ESTIMATED CHAR(1) default 'N',
    MILESTONE CHAR(1) default 'N',
    SUMMARY CHAR(1) default 'N',
    ACTUALDURATION VARCHAR(100),
    REMAININGDURATION VARCHAR(100),
    CONSTRAINTTYPE INTEGER,
    CONSTRAINTDATE DATETIME,
    DEADLINE DATETIME,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TMSPROJECTTASKINDEX1 (TPUUID));


# -----------------------------------------------------------------------
# TMSPROJECTEXCHANGE
# -----------------------------------------------------------------------
drop table if exists TMSPROJECTEXCHANGE;

CREATE TABLE TMSPROJECTEXCHANGE
(
    OBJECTID INTEGER NOT NULL,
    EXCHANGEDIRECTION INTEGER NOT NULL,
    ENTITYID INTEGER NOT NULL,
    ENTITYTYPE INTEGER NOT NULL,
    FILENAME VARCHAR(255),
    FILECONTENT LONGTEXT,
    CHANGEDBY INTEGER,
    LASTEDIT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TPUUID VARCHAR(36),
    PRIMARY KEY(OBJECTID),
    INDEX TMSPROJECTEXCHANGEINDEX1 (TPUUID));

ALTER TABLE TACL
    ADD CONSTRAINT TACL_FK_1
    FOREIGN KEY (PERSONKEY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TACL
    ADD CONSTRAINT TACL_FK_2
    FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
;

ALTER TABLE TACL
    ADD CONSTRAINT TACL_FK_3
    FOREIGN KEY (PROJKEY)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TBASELINE
    ADD CONSTRAINT TBASELINE_FK_1
    FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TBASELINE
    ADD CONSTRAINT TBASELINE_FK_2
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TCATEGORY
    ADD CONSTRAINT TCATEGORY_FK_1
    FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TCLASS
    ADD CONSTRAINT TCLASS_FK_1
    FOREIGN KEY (PROJKEY)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TDEPARTMENT
    ADD CONSTRAINT TDEPARTMENT_FK_1
    FOREIGN KEY (COSTCENTER)
    REFERENCES TCOSTCENTER (OBJECTID)
;

ALTER TABLE TNOTIFY
    ADD CONSTRAINT TNOTIFY_FK_1
    FOREIGN KEY (PROJCATKEY)
    REFERENCES TPROJCAT (PKEY)
;

ALTER TABLE TNOTIFY
    ADD CONSTRAINT TNOTIFY_FK_2
    FOREIGN KEY (STATEKEY)
    REFERENCES TSTATE (PKEY)
;

ALTER TABLE TNOTIFY
    ADD CONSTRAINT TNOTIFY_FK_3
    FOREIGN KEY (PERSONKEY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TNOTIFY
    ADD CONSTRAINT TNOTIFY_FK_4
    FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TPERSON
    ADD CONSTRAINT TPERSON_FK_1
    FOREIGN KEY (DEPKEY)
    REFERENCES TDEPARTMENT (PKEY)
;

ALTER TABLE TPERSON
    ADD CONSTRAINT TPERSON_FK_2
    FOREIGN KEY (MYDEFAULTREPORT)
    REFERENCES TPRIVATEREPORTREPOSITORY (PKEY)
;

ALTER TABLE TPRIORITY
    ADD CONSTRAINT TPRIORITY_FK_1
    FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TPPRIORITY
    ADD CONSTRAINT TPPRIORITY_FK_1
    FOREIGN KEY (PRIORITY)
    REFERENCES TPRIORITY (PKEY)
;

ALTER TABLE TPPRIORITY
    ADD CONSTRAINT TPPRIORITY_FK_2
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TPPRIORITY
    ADD CONSTRAINT TPPRIORITY_FK_3
    FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TPROJCAT
    ADD CONSTRAINT TPROJCAT_FK_1
    FOREIGN KEY (PROJKEY)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_1
    FOREIGN KEY (DEFOWNER)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_2
    FOREIGN KEY (DEFMANAGER)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_3
    FOREIGN KEY (DEFINITSTATE)
    REFERENCES TSTATE (PKEY)
;

ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_4
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TPROJECT
    ADD CONSTRAINT TPROJECT_FK_5
    FOREIGN KEY (STATUS)
    REFERENCES TSYSTEMSTATE (OBJECTID)
;

ALTER TABLE TRELEASE
    ADD CONSTRAINT TRELEASE_FK_1
    FOREIGN KEY (PROJKEY)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TRELEASE
    ADD CONSTRAINT TRELEASE_FK_2
    FOREIGN KEY (STATUS)
    REFERENCES TSYSTEMSTATE (OBJECTID)
;

ALTER TABLE TROLE
    ADD CONSTRAINT TROLE_FK_1
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TSEVERITY
    ADD CONSTRAINT TSEVERITY_FK_1
    FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TPSEVERITY
    ADD CONSTRAINT TPSEVERITY_FK_1
    FOREIGN KEY (SEVERITY)
    REFERENCES TSEVERITY (PKEY)
;

ALTER TABLE TPSEVERITY
    ADD CONSTRAINT TPSEVERITY_FK_2
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TPSEVERITY
    ADD CONSTRAINT TPSEVERITY_FK_3
    FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TSTATE
    ADD CONSTRAINT TSTATE_FK_1
    FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TSTATECHANGE
    ADD CONSTRAINT TSTATECHANGE_FK_1
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TSTATECHANGE
    ADD CONSTRAINT TSTATECHANGE_FK_2
    FOREIGN KEY (CHANGEDTO)
    REFERENCES TSTATE (PKEY)
;

ALTER TABLE TSTATECHANGE
    ADD CONSTRAINT TSTATECHANGE_FK_3
    FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TTRAIL
    ADD CONSTRAINT TTRAIL_FK_1
    FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TTRAIL
    ADD CONSTRAINT TTRAIL_FK_2
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_1
    FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_2
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_3
    FOREIGN KEY (ORIGINATOR)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_4
    FOREIGN KEY (RESPONSIBLE)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_5
    FOREIGN KEY (PROJCATKEY)
    REFERENCES TPROJCAT (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_6
    FOREIGN KEY (CATEGORYKEY)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_7
    FOREIGN KEY (CLASSKEY)
    REFERENCES TCLASS (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_8
    FOREIGN KEY (PRIORITYKEY)
    REFERENCES TPRIORITY (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_9
    FOREIGN KEY (SEVERITYKEY)
    REFERENCES TSEVERITY (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_10
    FOREIGN KEY (RELNOTICEDKEY)
    REFERENCES TRELEASE (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_11
    FOREIGN KEY (RELSCHEDULEDKEY)
    REFERENCES TRELEASE (PKEY)
;

ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_12
    FOREIGN KEY (STATE)
    REFERENCES TSTATE (PKEY)
;

ALTER TABLE TCOMPUTEDVALUES
    ADD CONSTRAINT TCOMPUTEDVALUES_FK_1
    FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TCOMPUTEDVALUES
    ADD CONSTRAINT TCOMPUTEDVALUES_FK_2
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TPRIVATEREPORTREPOSITORY
    ADD CONSTRAINT TPRIVATEREPORTREPOSITORY_FK_1
    FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TPUBLICREPORTREPOSITORY
    ADD CONSTRAINT TPUBLICREPORTREPOSITORY_FK_1
    FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TPROJECTREPORTREPOSITORY
    ADD CONSTRAINT TPROJECTREPORTREPOSITORY_FK_1
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TACCOUNT
    ADD CONSTRAINT TACCOUNT_FK_1
    FOREIGN KEY (STATUS)
    REFERENCES TSYSTEMSTATE (OBJECTID)
;

ALTER TABLE TACCOUNT
    ADD CONSTRAINT TACCOUNT_FK_2
    FOREIGN KEY (COSTCENTER)
    REFERENCES TCOSTCENTER (OBJECTID)
;

ALTER TABLE TATTACHMENT
    ADD CONSTRAINT TATTACHMENT_FK_1
    FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TATTACHMENT
    ADD CONSTRAINT TATTACHMENT_FK_2
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TATTACHMENT
    ADD CONSTRAINT TATTACHMENT_FK_3
    FOREIGN KEY (DOCUMENTSTATE)
    REFERENCES TDOCSTATE (OBJECTID)
;

ALTER TABLE TCOST
    ADD CONSTRAINT TCOST_FK_1
    FOREIGN KEY (ACCOUNT)
    REFERENCES TACCOUNT (OBJECTID)
;

ALTER TABLE TCOST
    ADD CONSTRAINT TCOST_FK_2
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TCOST
    ADD CONSTRAINT TCOST_FK_3
    FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TCOST
    ADD CONSTRAINT TCOST_FK_4
    FOREIGN KEY (EFFORTTYPE)
    REFERENCES TEFFORTTYPE (OBJECTID)
;

ALTER TABLE TEFFORTTYPE
    ADD CONSTRAINT TEFFORTTYPE_FK_1
    FOREIGN KEY (EFFORTUNIT)
    REFERENCES TEFFORTUNIT (OBJECTID)
;

ALTER TABLE TDOCSTATE
    ADD CONSTRAINT TDOCSTATE_FK_1
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TDISABLEFIELD
    ADD CONSTRAINT TDISABLEFIELD_FK_1
    FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TDISABLEFIELD
    ADD CONSTRAINT TDISABLEFIELD_FK_2
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TPLISTTYPE
    ADD CONSTRAINT TPLISTTYPE_FK_1
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TPLISTTYPE
    ADD CONSTRAINT TPLISTTYPE_FK_2
    FOREIGN KEY (CATEGORY)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TPSTATE
    ADD CONSTRAINT TPSTATE_FK_1
    FOREIGN KEY (STATE)
    REFERENCES TSTATE (PKEY)
;

ALTER TABLE TPSTATE
    ADD CONSTRAINT TPSTATE_FK_2
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TPSTATE
    ADD CONSTRAINT TPSTATE_FK_3
    FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_1
    FOREIGN KEY (STATEFROM)
    REFERENCES TSTATE (PKEY)
;

ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_2
    FOREIGN KEY (STATETO)
    REFERENCES TSTATE (PKEY)
;

ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_3
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_4
    FOREIGN KEY (RESPONSIBLE)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TWORKFLOWROLE
    ADD CONSTRAINT TWORKFLOWROLE_FK_1
    FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOW (OBJECTID)
;

ALTER TABLE TWORKFLOWROLE
    ADD CONSTRAINT TWORKFLOWROLE_FK_2
    FOREIGN KEY (MAYCHANGEROLE)
    REFERENCES TROLE (PKEY)
;

ALTER TABLE TWORKFLOWCATEGORY
    ADD CONSTRAINT TWORKFLOWCATEGORY_FK_1
    FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOW (OBJECTID)
;

ALTER TABLE TWORKFLOWCATEGORY
    ADD CONSTRAINT TWORKFLOWCATEGORY_FK_2
    FOREIGN KEY (CATEGORY)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TROLELISTTYPE
    ADD CONSTRAINT TROLELISTTYPE_FK_1
    FOREIGN KEY (PROLE)
    REFERENCES TROLE (PKEY)
;

ALTER TABLE TROLELISTTYPE
    ADD CONSTRAINT TROLELISTTYPE_FK_2
    FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_1
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_2
    FOREIGN KEY (ISSUE)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_3
    FOREIGN KEY (ATTRIBUTEID)
    REFERENCES TATTRIBUTE (OBJECTID)
;

ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_4
    FOREIGN KEY (OPTIONID)
    REFERENCES TATTRIBUTEOPTION (OBJECTID)
;

ALTER TABLE TATTRIBUTEOPTION
    ADD CONSTRAINT TATTRIBUTEOPTION_FK_1
    FOREIGN KEY (ATTRIBUTEID)
    REFERENCES TATTRIBUTE (OBJECTID)
;

ALTER TABLE TATTRIBUTETYPE
    ADD CONSTRAINT TATTRIBUTETYPE_FK_1
    FOREIGN KEY (ATTRIBUTECLASS)
    REFERENCES TATTRIBUTECLASS (OBJECTID)
;

ALTER TABLE TATTRIBUTE
    ADD CONSTRAINT TATTRIBUTE_FK_1
    FOREIGN KEY (REQUIREDOPTION)
    REFERENCES TATTRIBUTEOPTION (OBJECTID)
;

ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_1
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_2
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_3
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TSCHEDULER
    ADD CONSTRAINT TSCHEDULER_FK_1
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TPROJECTACCOUNT
    ADD CONSTRAINT TPROJECTACCOUNT_FK_1
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TPROJECTACCOUNT
    ADD CONSTRAINT TPROJECTACCOUNT_FK_2
    FOREIGN KEY (ACCOUNT)
    REFERENCES TACCOUNT (OBJECTID)
;

ALTER TABLE TGROUPMEMBER
    ADD CONSTRAINT TGROUPMEMBER_FK_1
    FOREIGN KEY (THEGROUP)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TGROUPMEMBER
    ADD CONSTRAINT TGROUPMEMBER_FK_2
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TBUDGET
    ADD CONSTRAINT TBUDGET_FK_1
    FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TBUDGET
    ADD CONSTRAINT TBUDGET_FK_2
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TACTUALESTIMATEDBUDGET
    ADD CONSTRAINT TACTUALESTIMATEDBUDGET_FK_1
    FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TACTUALESTIMATEDBUDGET
    ADD CONSTRAINT TACTUALESTIMATEDBUDGET_FK_2
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TDASHBOARDSCREEN
    ADD CONSTRAINT TDASHBOARDSCREEN_FK_1
    FOREIGN KEY (PERSONPKEY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TDASHBOARDSCREEN
    ADD CONSTRAINT TDASHBOARDSCREEN_FK_2
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TDASHBOARDTAB
    ADD CONSTRAINT TDASHBOARDTAB_FK_1
    FOREIGN KEY (PARENT)
    REFERENCES TDASHBOARDSCREEN (OBJECTID)
;

ALTER TABLE TDASHBOARDPANEL
    ADD CONSTRAINT TDASHBOARDPANEL_FK_1
    FOREIGN KEY (PARENT)
    REFERENCES TDASHBOARDTAB (OBJECTID)
;

ALTER TABLE TDASHBOARDFIELD
    ADD CONSTRAINT TDASHBOARDFIELD_FK_1
    FOREIGN KEY (PARENT)
    REFERENCES TDASHBOARDPANEL (OBJECTID)
;

ALTER TABLE TDASHBOARDPARAMETER
    ADD CONSTRAINT TDASHBOARDPARAMETER_FK_1
    FOREIGN KEY (DASHBOARDFIELD)
    REFERENCES TDASHBOARDFIELD (OBJECTID)
;

ALTER TABLE TVERSIONCONTROLPARAMETER
    ADD CONSTRAINT TVERSIONCONTROLPARAMETER_FK_1
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TFIELD
    ADD CONSTRAINT TFIELD_FK_1
    FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_1
    FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
;

ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_2
    FOREIGN KEY (ISSUETYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_3
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TFIELDCONFIG
    ADD CONSTRAINT TFIELDCONFIG_FK_4
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TROLEFIELD
    ADD CONSTRAINT TROLEFIELD_FK_1
    FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
;

ALTER TABLE TROLEFIELD
    ADD CONSTRAINT TROLEFIELD_FK_2
    FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
;

ALTER TABLE TCONFIGOPTIONSROLE
    ADD CONSTRAINT TCONFIGOPTIONSROLE_FK_1
    FOREIGN KEY (CONFIGKEY)
    REFERENCES TFIELDCONFIG (OBJECTID)
;

ALTER TABLE TCONFIGOPTIONSROLE
    ADD CONSTRAINT TCONFIGOPTIONSROLE_FK_2
    FOREIGN KEY (ROLEKEY)
    REFERENCES TROLE (PKEY)
;

ALTER TABLE TCONFIGOPTIONSROLE
    ADD CONSTRAINT TCONFIGOPTIONSROLE_FK_3
    FOREIGN KEY (OPTIONKEY)
    REFERENCES TOPTION (OBJECTID)
;

ALTER TABLE TTEXTBOXSETTINGS
    ADD CONSTRAINT TTEXTBOXSETTINGS_FK_1
    FOREIGN KEY (CONFIG)
    REFERENCES TFIELDCONFIG (OBJECTID)
;

ALTER TABLE TGENERALSETTINGS
    ADD CONSTRAINT TGENERALSETTINGS_FK_1
    FOREIGN KEY (CONFIG)
    REFERENCES TFIELDCONFIG (OBJECTID)
;

ALTER TABLE TLIST
    ADD CONSTRAINT TLIST_FK_1
    FOREIGN KEY (PARENTLIST)
    REFERENCES TLIST (OBJECTID)
;

ALTER TABLE TLIST
    ADD CONSTRAINT TLIST_FK_2
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TLIST
    ADD CONSTRAINT TLIST_FK_3
    FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TOPTION
    ADD CONSTRAINT TOPTION_FK_1
    FOREIGN KEY (LIST)
    REFERENCES TLIST (OBJECTID)
;

ALTER TABLE TOPTION
    ADD CONSTRAINT TOPTION_FK_2
    FOREIGN KEY (ICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TOPTIONSETTINGS
    ADD CONSTRAINT TOPTIONSETTINGS_FK_1
    FOREIGN KEY (LIST)
    REFERENCES TLIST (OBJECTID)
;

ALTER TABLE TOPTIONSETTINGS
    ADD CONSTRAINT TOPTIONSETTINGS_FK_2
    FOREIGN KEY (CONFIG)
    REFERENCES TFIELDCONFIG (OBJECTID)
;

ALTER TABLE TATTRIBUTEVALUE
    ADD CONSTRAINT TATTRIBUTEVALUE_FK_1
    FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
;

ALTER TABLE TATTRIBUTEVALUE
    ADD CONSTRAINT TATTRIBUTEVALUE_FK_2
    FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TSCREEN
    ADD CONSTRAINT TSCREEN_FK_1
    FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TSCREENTAB
    ADD CONSTRAINT TSCREENTAB_FK_1
    FOREIGN KEY (PARENT)
    REFERENCES TSCREEN (OBJECTID)
;

ALTER TABLE TSCREENPANEL
    ADD CONSTRAINT TSCREENPANEL_FK_1
    FOREIGN KEY (PARENT)
    REFERENCES TSCREENTAB (OBJECTID)
;

ALTER TABLE TSCREENFIELD
    ADD CONSTRAINT TSCREENFIELD_FK_1
    FOREIGN KEY (PARENT)
    REFERENCES TSCREENPANEL (OBJECTID)
;

ALTER TABLE TSCREENFIELD
    ADD CONSTRAINT TSCREENFIELD_FK_2
    FOREIGN KEY (FIELDKEY)
    REFERENCES TFIELD (OBJECTID)
;

ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_1
    FOREIGN KEY (SCREEN)
    REFERENCES TSCREEN (OBJECTID)
;

ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_2
    FOREIGN KEY (ISSUETYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_3
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_4
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TSCREENCONFIG
    ADD CONSTRAINT TSCREENCONFIG_FK_5
    FOREIGN KEY (ACTIONKEY)
    REFERENCES TACTION (OBJECTID)
;

ALTER TABLE TINITSTATE
    ADD CONSTRAINT TINITSTATE_FK_1
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TINITSTATE
    ADD CONSTRAINT TINITSTATE_FK_2
    FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TINITSTATE
    ADD CONSTRAINT TINITSTATE_FK_3
    FOREIGN KEY (STATEKEY)
    REFERENCES TSTATE (PKEY)
;

ALTER TABLE TEVENT
    ADD CONSTRAINT TEVENT_FK_1
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TEVENT
    ADD CONSTRAINT TEVENT_FK_2
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TEVENT
    ADD CONSTRAINT TEVENT_FK_3
    FOREIGN KEY (EVENTSCRIPT)
    REFERENCES TCLOB (OBJECTID)
;

ALTER TABLE TNOTIFYFIELD
    ADD CONSTRAINT TNOTIFYFIELD_FK_1
    FOREIGN KEY (NOTIFYTRIGGER)
    REFERENCES TNOTIFYTRIGGER (OBJECTID)
;

ALTER TABLE TNOTIFYTRIGGER
    ADD CONSTRAINT TNOTIFYTRIGGER_FK_1
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTINGS_FK_1
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTINGS_FK_2
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTINGS_FK_3
    FOREIGN KEY (NOTIFYTRIGGER)
    REFERENCES TNOTIFYTRIGGER (OBJECTID)
;

ALTER TABLE TNOTIFYSETTINGS
    ADD CONSTRAINT TNOTIFYSETTINGS_FK_4
    FOREIGN KEY (NOTIFYFILTER)
    REFERENCES TQUERYREPOSITORY (OBJECTID)
;

ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSITORY_FK_1
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSITORY_FK_2
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TQUERYREPOSITORY
    ADD CONSTRAINT TQUERYREPOSITORY_FK_3
    FOREIGN KEY (QUERYKEY)
    REFERENCES TCLOB (OBJECTID)
;

ALTER TABLE TLINKTYPE
    ADD CONSTRAINT TLINKTYPE_FK_1
    FOREIGN KEY (OUTWARDICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TLINKTYPE
    ADD CONSTRAINT TLINKTYPE_FK_2
    FOREIGN KEY (INWARDICONKEY)
    REFERENCES TBLOB (OBJECTID)
;

ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_1
    FOREIGN KEY (LINKPRED)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_2
    FOREIGN KEY (LINKSUCC)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_3
    FOREIGN KEY (LINKTYPE)
    REFERENCES TLINKTYPE (OBJECTID)
;

ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_4
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TWORKITEMLOCK
    ADD CONSTRAINT TWORKITEMLOCK_FK_1
    FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TWORKITEMLOCK
    ADD CONSTRAINT TWORKITEMLOCK_FK_2
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TEXPORTTEMPLATE
    ADD CONSTRAINT TEXPORTTEMPLATE_FK_1
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TEXPORTTEMPLATE
    ADD CONSTRAINT TEXPORTTEMPLATE_FK_2
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TLOGGEDINUSERS
    ADD CONSTRAINT TLOGGEDINUSERS_FK_1
    FOREIGN KEY (LOGGEDUSER)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TLOGGEDINUSERS
    ADD CONSTRAINT TLOGGEDINUSERS_FK_2
    FOREIGN KEY (NODEADDRESS)
    REFERENCES CLUSTERNODE (OBJECTID)
;

ALTER TABLE TENTITYCHANGES
    ADD CONSTRAINT TENTITYCHANGES_FK_1
    FOREIGN KEY (CLUSTERNODE)
    REFERENCES CLUSTERNODE (OBJECTID)
;

ALTER TABLE TSUMMARYMAIL
    ADD CONSTRAINT TSUMMARYMAIL_FK_1
    FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TSUMMARYMAIL
    ADD CONSTRAINT TSUMMARYMAIL_FK_2
    FOREIGN KEY (PERSONFROM)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TSUMMARYMAIL
    ADD CONSTRAINT TSUMMARYMAIL_FK_3
    FOREIGN KEY (PERSONTO)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TOUTLINECODE
    ADD CONSTRAINT TOUTLINECODE_FK_1
    FOREIGN KEY (OUTLINETEMPLATE)
    REFERENCES TOUTLINETEMPLATE (OBJECTID)
;

ALTER TABLE TOUTLINETEMPLATEDEF
    ADD CONSTRAINT TOUTLINETEMPLATEDEF_FK_1
    FOREIGN KEY (OUTLINETEMPLATE)
    REFERENCES TOUTLINETEMPLATE (OBJECTID)
;

ALTER TABLE THISTORYTRANSACTION
    ADD CONSTRAINT THISTORYTRANSACTION_FK_1
    FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE THISTORYTRANSACTION
    ADD CONSTRAINT THISTORYTRANSACTION_FK_2
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_1
    FOREIGN KEY (HISTORYTRANSACTION)
    REFERENCES THISTORYTRANSACTION (OBJECTID)
;

ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_2
    FOREIGN KEY (PARENTCOMMENT)
    REFERENCES TFIELDCHANGE (OBJECTID)
;

ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_3
    FOREIGN KEY (NEWCUSTOMOPTIONID)
    REFERENCES TOPTION (OBJECTID)
;

ALTER TABLE TFIELDCHANGE
    ADD CONSTRAINT TFIELDCHANGE_FK_4
    FOREIGN KEY (OLDCUSTOMOPTIONID)
    REFERENCES TOPTION (OBJECTID)
;

ALTER TABLE TSCRIPTS
    ADD CONSTRAINT TSCRIPTS_FK_1
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TSCRIPTS
    ADD CONSTRAINT TSCRIPTS_FK_2
    FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TSCRIPTS
    ADD CONSTRAINT TSCRIPTS_FK_3
    FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TREVISION
    ADD CONSTRAINT TREVISION_FK_1
    FOREIGN KEY (REPOSITORYKEY)
    REFERENCES TREPOSITORY (OBJECTID)
;

ALTER TABLE TREVISIONWORKITEMS
    ADD CONSTRAINT TREVISIONWORKITEMS_FK_1
    FOREIGN KEY (REVISIONKEY)
    REFERENCES TREVISION (OBJECTID)
;

ALTER TABLE TTEMPLATEPERSON
    ADD CONSTRAINT TTEMPLATEPERSON_FK_1
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TTEMPLATEPERSON
    ADD CONSTRAINT TTEMPLATEPERSON_FK_2
    FOREIGN KEY (REPORTTEMPLATE)
    REFERENCES TEXPORTTEMPLATE (OBJECTID)
;

ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSONSETTINGS_FK_1
    FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSONSETTINGS_FK_2
    FOREIGN KEY (RECURRENCEPATTERN)
    REFERENCES TRECURRENCEPATTERN (OBJECTID)
;

ALTER TABLE TREPORTPERSONSETTINGS
    ADD CONSTRAINT TREPORTPERSONSETTINGS_FK_3
    FOREIGN KEY (REPORTTEMPLATE)
    REFERENCES TEXPORTTEMPLATE (OBJECTID)
;

ALTER TABLE TREPORTPARAMETER
    ADD CONSTRAINT TREPORTPARAMETER_FK_1
    FOREIGN KEY (REPORTPERSONSETTINGS)
    REFERENCES TREPORTPERSONSETTINGS (OBJECTID)
;

ALTER TABLE TMSPROJECTTASK
    ADD CONSTRAINT TMSPROJECTTASK_FK_1
    FOREIGN KEY (WORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TMSPROJECTEXCHANGE
    ADD CONSTRAINT TMSPROJECTEXCHANGE_FK_1
    FOREIGN KEY (CHANGEDBY)
    REFERENCES TPERSON (PKEY)
;
