-- Firebird update script 300 to 310

/* Table: TATTRIBUTE */

CREATE TABLE "TATTRIBUTE" 
(
  "OBJECTID"	INTEGER NOT NULL,
  "ATTRIBUTENAME"	VARCHAR(255) NOT NULL,
  "ATTRIBUTETYPE"	INTEGER NOT NULL,
  "DELETED"	INTEGER,
  "DESCRIPTION"	VARCHAR(64),
  "PERMISSION"	VARCHAR(255),
  "REQUIREDOPTION"	INTEGER,
 PRIMARY KEY ("OBJECTID")
);

/* Table: TATTRIBUTECLASS */

CREATE TABLE "TATTRIBUTECLASS" 
(
  "OBJECTID"	INTEGER NOT NULL,
  "ATTRIBUTECLASSNAME"	VARCHAR(255) NOT NULL,
  "ATTRIBUTECLASSDESC"	VARCHAR(64),
  "JAVACLASSNAME"	VARCHAR(255),
 PRIMARY KEY ("OBJECTID")
);

/* Table: TATTRIBUTEOPTION */

CREATE TABLE "TATTRIBUTEOPTION" 
(
  "OBJECTID"	INTEGER NOT NULL,
  "ATTRIBUTEID"	INTEGER NOT NULL,
  "PARENTOPTION"	INTEGER,
  "OPTIONNAME"	VARCHAR(255) NOT NULL,
  "DELETED"	INTEGER,
 PRIMARY KEY ("OBJECTID")
);

/* Table: TATTRIBUTETYPE, Owner: FRIEDJ */

CREATE TABLE "TATTRIBUTETYPE" 
(
  "OBJECTID"	INTEGER NOT NULL,
  "ATTRIBUTETYPENAME"	VARCHAR(255) NOT NULL,
  "ATTRIBUTECLASS"	INTEGER NOT NULL,
  "JAVACLASSNAME"	VARCHAR(255),
  "VALIDATIONKEY"	VARCHAR(25),
 PRIMARY KEY ("OBJECTID")
);

ALTER TABLE TBASELINE ADD STARTDATE2   TIMESTAMP;
ALTER TABLE TBASELINE ADD ENDDATE2     TIMESTAMP;
UPDATE TBASELINE SET STARTDATE2 = STARTDATE, ENDDATE2 = ENDDATE;
ALTER TABLE TBASELINE ALTER STARTDATE  TYPE TIMESTAMP;
ALTER TABLE TBASELINE ALTER ENDDATE    TYPE TIMESTAMP;
UPDATE TBASELINE SET STARTDATE = STARTDATE2, ENDDATE = ENDDATE2;
ALTER TABLE TBASELINE DROP STARTDATE2;
ALTER TABLE TBASELINE DROP ENDDATE2;

/* Table: TISSUEATTRIBUTEVALUE, Owner: FRIEDJ */

CREATE TABLE "TISSUEATTRIBUTEVALUE" 
(
  "OBJECTID"	INTEGER NOT NULL,
  "ATTRIBUTEID"	INTEGER NOT NULL,
  "DELETED"	INTEGER,
  "ISSUE"	INTEGER NOT NULL,
  "NUMERICVALUE"	INTEGER,
  "TIMESTAMPVALUE"	TIMESTAMP,
  "LONGTEXTVALUE"	BLOB SUB_TYPE TEXT SEGMENT SIZE 80,
  "OPTIONID"	INTEGER,
  "PERSON"	INTEGER,
  "CHARVALUE"	VARCHAR(255),
  "DISPLAYVALUE"	VARCHAR(255),
 PRIMARY KEY ("OBJECTID")
);


-- TPERSON
ALTER TABLE TPERSON ALTER VALIDUNTIL TYPE TIMESTAMP;
ALTER TABLE TPERSON ADD REMINDMEASORIGINATOR CHAR (1) default 'N';
ALTER TABLE TPERSON ADD REMINDMEASMANAGER CHAR (1) default 'Y';
ALTER TABLE TPERSON ADD REMINDMEASRESPONSIBLE CHAR (1) default 'Y';
ALTER TABLE TPERSON ADD EMAILREMINDPLEVEL INTEGER;
ALTER TABLE TPERSON ADD EMAILREMINDSLEVEL INTEGER;
UPDATE TPERSON SET NOEMAILSPLEASE = 0;


-- TPPRIORITY
CREATE TABLE TPPRIORITY
(
            "OBJECTID" INTEGER NOT NULL,
            "PRIORITY" INTEGER,
            "PROJECTTYPE" INTEGER,
            "LISTTYPE" INTEGER,
    PRIMARY KEY(OBJECTID)
);


-- TPRIORITY
ALTER TABLE TPRIORITY ADD "WLEVEL" INTEGER;

-- TPROJECT
ALTER TABLE TPROJECT ADD VERSIONSYSTEMFIELD0 VARCHAR (255);
ALTER TABLE TPROJECT ADD VERSIONSYSTEMFIELD1 VARCHAR (255);
ALTER TABLE TPROJECT ADD VERSIONSYSTEMFIELD2 VARCHAR (255);
ALTER TABLE TPROJECT ADD VERSIONSYSTEMFIELD3 VARCHAR (255);
ALTER TABLE TPROJECT ADD DELETED VARCHAR(1) default 'N' NOT NULL;

-- TPSEVERITY
CREATE TABLE TPSEVERITY
(
            "OBJECTID" INTEGER NOT NULL,
            "SEVERITY" INTEGER,
            "PROJECTTYPE" INTEGER,
            "LISTTYPE" INTEGER,
    PRIMARY KEY(OBJECTID)
);


ALTER TABLE TWORKITEM ADD STARTDATE2   TIMESTAMP;
ALTER TABLE TWORKITEM ADD ENDDATE2     TIMESTAMP;
UPDATE TWORKITEM SET STARTDATE2 = STARTDATE, ENDDATE2 = ENDDATE;
ALTER TABLE TWORKITEM ALTER STARTDATE  TYPE TIMESTAMP;
ALTER TABLE TWORKITEM ALTER ENDDATE    TYPE TIMESTAMP;
UPDATE TWORKITEM SET STARTDATE = STARTDATE2, ENDDATE = ENDDATE2;
ALTER TABLE TWORKITEM DROP STARTDATE2;
ALTER TABLE TWORKITEM DROP ENDDATE2;


/* Table: TREPORTLAYOUT */

CREATE TABLE "TREPORTLAYOUT" 
(
  "OBJECTID"	INTEGER NOT NULL,
  "PROJECTTYPE"	INTEGER,
  "PROJECT"	INTEGER,
  "PERSON"	INTEGER,
  "REPORTFIELD"	INTEGER NOT NULL,
  "FPOSITION"	INTEGER NOT NULL,
  "FWIDTH"	INTEGER NOT NULL,
  "FSORTORDER"	INTEGER,
  "FSORTDIR"	VARCHAR(1),
 PRIMARY KEY ("OBJECTID")
);


/* Table: TSCHEDULER */

CREATE TABLE "TSCHEDULER" 
(
  "OBJECTID"	INTEGER NOT NULL,
  "CRONWHEN"	VARCHAR(64) NOT NULL,
  "JAVACLASSNAME"	VARCHAR(255),
  "EXTERNALEXE"	VARCHAR(255),
  "PERSON"	INTEGER NOT NULL,
 PRIMARY KEY ("OBJECTID")
);


-- TSEVERITY
ALTER TABLE TSEVERITY ADD "WLEVEL" INTEGER;


-- TSITE
ALTER TABLE TSITE ADD EXECUTABLE1 VARCHAR (255);
ALTER TABLE TSITE ADD EXECUTABLE2 VARCHAR (255);
ALTER TABLE TSITE ADD LDAPBINDDN VARCHAR (255);
ALTER TABLE TSITE ADD LDAPBINDPASSW VARCHAR (20);
ALTER TABLE TSITE ADD LICENSEEXT VARCHAR(255);

-- TWORKFLOW
drop table TWORKFLOW;

CREATE TABLE TWORKFLOW
(
            "OBJECTID" INTEGER NOT NULL,
            "STATEFROM" INTEGER,
            "STATETO" INTEGER,
            "PROJECTTYPE" INTEGER,
    PRIMARY KEY(OBJECTID)
);


-- TWORKFLOWROLE

CREATE TABLE TWORKFLOWROLE
(
            "OBJECTID" INTEGER NOT NULL,
            "WORKFLOW" INTEGER,
            "MAYCHANGEROLE" INTEGER,
    PRIMARY KEY(OBJECTID)
);


-- TWORKFLOWCATEGORY

CREATE TABLE TWORKFLOWCATEGORY
(
            "OBJECTID" INTEGER NOT NULL,
            "WORKFLOW" INTEGER,
            "CATEGORY" INTEGER,
    PRIMARY KEY(OBJECTID)
);

-- TWORKITEM
ALTER TABLE TWORKITEM ADD "WLEVEL" VARCHAR(14);


CREATE INDEX TWIINDEX17 ON TWORKITEM(WLEVEL);

ALTER TABLE "TATTRIBUTE" ADD CONSTRAINT "TATTRIBUTE_FK_1" FOREIGN KEY ("REQUIREDOPTION") REFERENCES TATTRIBUTEOPTION ("OBJECTID");
ALTER TABLE "TATTRIBUTEOPTION" ADD CONSTRAINT "TATTRIBUTEOPTION_FK_1" FOREIGN KEY ("ATTRIBUTEID") REFERENCES TATTRIBUTE ("OBJECTID");
ALTER TABLE "TATTRIBUTETYPE" ADD CONSTRAINT "TATTRIBUTETYPE_FK_1" FOREIGN KEY ("ATTRIBUTECLASS") REFERENCES TATTRIBUTECLASS ("OBJECTID");

ALTER TABLE "TISSUEATTRIBUTEVALUE" ADD CONSTRAINT "TISSUEATTRIBUTEVALUE_FK_1" FOREIGN KEY ("PERSON") REFERENCES TPERSON ("PKEY");
ALTER TABLE "TISSUEATTRIBUTEVALUE" ADD CONSTRAINT "TISSUEATTRIBUTEVALUE_FK_2" FOREIGN KEY ("ISSUE") REFERENCES TWORKITEM ("WORKITEMKEY");
ALTER TABLE "TISSUEATTRIBUTEVALUE" ADD CONSTRAINT "TISSUEATTRIBUTEVALUE_FK_3" FOREIGN KEY ("ATTRIBUTEID") REFERENCES TATTRIBUTE ("OBJECTID");
ALTER TABLE "TISSUEATTRIBUTEVALUE" ADD CONSTRAINT "TISSUEATTRIBUTEVALUE_FK_4" FOREIGN KEY ("OPTIONID") REFERENCES TATTRIBUTEOPTION ("OBJECTID");

ALTER TABLE "TPPRIORITY" ADD CONSTRAINT "TPPRIORITY_FK_1" FOREIGN KEY ("PRIORITY") REFERENCES TPRIORITY ("PKEY");
ALTER TABLE "TPPRIORITY" ADD CONSTRAINT "TPPRIORITY_FK_2" FOREIGN KEY ("PROJECTTYPE") REFERENCES TPROJECTTYPE ("OBJECTID");
ALTER TABLE "TPPRIORITY" ADD CONSTRAINT "TPPRIORITY_FK_3" FOREIGN KEY ("LISTTYPE") REFERENCES TCATEGORY ("PKEY");

/* ALTER TABLE "TPRIVATEREPORTREPOSITORY" ADD CONSTRAINT "TPRIVATEREPORTREPOSITORY_FK_1" FOREIGN KEY ("OWNER") REFERENCES TPERSON ("PKEY");
*/


/* ALTER TABLE "TPROJECTREPORTREPOSITORY" ADD CONSTRAINT "TPROJECTREPORTREPOSITORY_FK_1" FOREIGN KEY ("PROJECT") REFERENCES TPROJECT ("PKEY");
*/
ALTER TABLE "TPSEVERITY" ADD CONSTRAINT "TPSEVERITY_FK_1" FOREIGN KEY ("SEVERITY") REFERENCES TSEVERITY ("PKEY");
ALTER TABLE "TPSEVERITY" ADD CONSTRAINT "TPSEVERITY_FK_2" FOREIGN KEY ("PROJECTTYPE") REFERENCES TPROJECTTYPE ("OBJECTID");
ALTER TABLE "TPSEVERITY" ADD CONSTRAINT "TPSEVERITY_FK_3" FOREIGN KEY ("LISTTYPE") REFERENCES TCATEGORY ("PKEY");

/* ALTER TABLE "TPUBLICREPORTREPOSITORY" ADD CONSTRAINT "TPUBLICREPORTREPOSITORY_FK_1" FOREIGN KEY ("OWNER") REFERENCES TPERSON ("PKEY");
*/
ALTER TABLE "TREPORTLAYOUT" ADD CONSTRAINT "TREPORTLAYOUT_FK_1" FOREIGN KEY ("PROJECTTYPE") REFERENCES TPROJECTTYPE ("OBJECTID");
ALTER TABLE "TREPORTLAYOUT" ADD CONSTRAINT "TREPORTLAYOUT_FK_2" FOREIGN KEY ("PROJECT") REFERENCES TPROJECT ("PKEY");
ALTER TABLE "TREPORTLAYOUT" ADD CONSTRAINT "TREPORTLAYOUT_FK_3" FOREIGN KEY ("PERSON") REFERENCES TPERSON ("PKEY");
ALTER TABLE "TREPORTLAYOUT" ADD CONSTRAINT "TREPORTLAYOUT_FK_4" FOREIGN KEY ("REPORTFIELD") REFERENCES TISSUEATTRIBUTEVALUE ("OBJECTID");

ALTER TABLE "TSCHEDULER" ADD CONSTRAINT "TSCHEDULER_FK_1" FOREIGN KEY ("PERSON") REFERENCES TPERSON ("PKEY");

ALTER TABLE "TWORKFLOW" ADD CONSTRAINT "TWORKFLOW_FK_1" FOREIGN KEY ("STATEFROM") REFERENCES TSTATE ("PKEY");
ALTER TABLE "TWORKFLOW" ADD CONSTRAINT "TWORKFLOW_FK_2" FOREIGN KEY ("STATETO") REFERENCES TSTATE ("PKEY");
ALTER TABLE "TWORKFLOW" ADD CONSTRAINT "TWORKFLOW_FK_3" FOREIGN KEY ("PROJECTTYPE") REFERENCES TPROJECTTYPE ("OBJECTID");

ALTER TABLE "TWORKFLOWCATEGORY" ADD CONSTRAINT "TWORKFLOWCATEGORY_FK_1" FOREIGN KEY ("WORKFLOW") REFERENCES TWORKFLOW ("OBJECTID");
ALTER TABLE "TWORKFLOWCATEGORY" ADD CONSTRAINT "TWORKFLOWCATEGORY_FK_2" FOREIGN KEY ("CATEGORY") REFERENCES TCATEGORY ("PKEY");
ALTER TABLE "TWORKFLOWROLE" ADD CONSTRAINT "TWORKFLOWROLE_FK_1" FOREIGN KEY ("WORKFLOW") REFERENCES TWORKFLOW ("OBJECTID");
ALTER TABLE "TWORKFLOWROLE" ADD CONSTRAINT "TWORKFLOWROLE_FK_2" FOREIGN KEY ("MAYCHANGEROLE") REFERENCES TROLE ("PKEY");


INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(36, 'TWORKFLOWCATEGORY', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(37, 'TWORKFLOWROLE', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(38, 'TPPRIORITY', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(39, 'TPSEVERITY', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(40, 'TISSUEATTRIBUTEVALUE', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(41, 'TATTRIBUTEOPTION', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(42, 'TATTRIBUTECLASS', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(43, 'TATTRIBUTETYPE', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(44, 'TATTRIBUTE', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(45, 'TREPORTLAYOUT', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(46, 'TSCHEDULER', 1, 1);

COMMIT;










