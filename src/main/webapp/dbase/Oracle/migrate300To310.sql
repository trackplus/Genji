-- Oracle update script 300 to 310

-- TPERSON
ALTER TABLE TPERSON ADD REMINDMEASORIGINATOR CHAR (1) default 'N';
ALTER TABLE TPERSON ADD REMINDMEASMANAGER CHAR (1) default 'Y';
ALTER TABLE TPERSON ADD REMINDMEASRESPONSIBLE CHAR (1) default 'Y';
ALTER TABLE TPERSON ADD EMAILREMINDPLEVEL NUMBER;
ALTER TABLE TPERSON ADD EMAILREMINDSLEVEL NUMBER;
UPDATE TPERSON SET NOEMAILSPLEASE = 0;


-- TPRIORITY
ALTER TABLE TPRIORITY ADD "WLEVEL" NUMBER;

-----------------------------------------------------------------------------
-- TPPRIORITY
-----------------------------------------------------------------------------
-- DROP TABLE TPPRIORITY CASCADE CONSTRAINTS;

CREATE TABLE TPPRIORITY
(
    OBJECTID NUMBER NOT NULL,
    PRIORITY NUMBER,
    PROJECTTYPE NUMBER,
    LISTTYPE NUMBER
);

ALTER TABLE TPPRIORITY
    ADD CONSTRAINT TPPRIORITY_PK
PRIMARY KEY (OBJECTID);


-- TPROJCAT
ALTER TABLE TPROJCAT MODIFY (LABEL VARCHAR2 (35));

-- TPROJECT
ALTER TABLE TPROJECT ADD VERSIONSYSTEMFIELD0 VARCHAR2 (255);
ALTER TABLE TPROJECT ADD VERSIONSYSTEMFIELD1 VARCHAR2 (255);
ALTER TABLE TPROJECT ADD VERSIONSYSTEMFIELD2 VARCHAR2 (255);
ALTER TABLE TPROJECT ADD VERSIONSYSTEMFIELD3 VARCHAR2 (255);
ALTER TABLE TPROJECT ADD DELETED VARCHAR2 (1) default 'N' NOT NULL;

-- TSEVERITY
ALTER TABLE TSEVERITY ADD WLEVEL NUMBER;

-----------------------------------------------------------------------------
-- TPSEVERITY
-----------------------------------------------------------------------------
-- DROP TABLE TPSEVERITY CASCADE CONSTRAINTS;

CREATE TABLE TPSEVERITY
(
    OBJECTID NUMBER NOT NULL,
    SEVERITY NUMBER,
    PROJECTTYPE NUMBER,
    LISTTYPE NUMBER
);

ALTER TABLE TPSEVERITY
    ADD CONSTRAINT TPSEVERITY_PK
PRIMARY KEY (OBJECTID);


-- TWORKITEM
ALTER TABLE TWORKITEM ADD WLEVEL VARCHAR2 (14);
-- ALTER TABLE TWORKITEM MODIFY (CLASSKEY NUMBER);
CREATE INDEX TWIINDEX17 ON TWORKITEM (WLEVEL);

-- TATTACHMENT
ALTER TABLE TATTACHMENT MODIFY (FILENAME VARCHAR2 (512));
-- TSITE
ALTER TABLE TSITE ADD EXECUTABLE1 VARCHAR2 (255);
ALTER TABLE TSITE ADD EXECUTABLE2 VARCHAR2 (255);
ALTER TABLE TSITE ADD LDAPBINDDN VARCHAR2 (255);
ALTER TABLE TSITE ADD LDAPBINDPASSW VARCHAR2 (20);
ALTER TABLE TSITE ADD LICENSEEXT VARCHAR2 (255);

-- TWORKFLOW
drop table TWORKFLOW;

-----------------------------------------------------------------------------
-- TWORKFLOW
-----------------------------------------------------------------------------
-- DROP TABLE TWORKFLOW CASCADE CONSTRAINTS;

CREATE TABLE TWORKFLOW
(
    OBJECTID NUMBER NOT NULL,
    STATEFROM NUMBER,
    STATETO NUMBER,
    PROJECTTYPE NUMBER
);

ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_PK
PRIMARY KEY (OBJECTID);



-----------------------------------------------------------------------------
-- TWORKFLOWROLE
-----------------------------------------------------------------------------
-- DROP TABLE TWORKFLOWROLE CASCADE CONSTRAINTS;

CREATE TABLE TWORKFLOWROLE
(
    OBJECTID NUMBER NOT NULL,
    WORKFLOW NUMBER,
    MAYCHANGEROLE NUMBER
);

ALTER TABLE TWORKFLOWROLE
    ADD CONSTRAINT TWORKFLOWROLE_PK
PRIMARY KEY (OBJECTID);



-----------------------------------------------------------------------------
-- TWORKFLOWCATEGORY
-----------------------------------------------------------------------------
-- DROP TABLE TWORKFLOWCATEGORY CASCADE CONSTRAINTS;

CREATE TABLE TWORKFLOWCATEGORY
(
    OBJECTID NUMBER NOT NULL,
    WORKFLOW NUMBER,
    CATEGORY NUMBER
);

ALTER TABLE TWORKFLOWCATEGORY
    ADD CONSTRAINT TWORKFLOWCATEGORY_PK
PRIMARY KEY (OBJECTID);



-----------------------------------------------------------------------------
-- TISSUEATTRIBUTEVALUE
-----------------------------------------------------------------------------
-- DROP TABLE TISSUEATTRIBUTEVALUE CASCADE CONSTRAINTS;

CREATE TABLE TISSUEATTRIBUTEVALUE
(
    OBJECTID NUMBER NOT NULL,
    ATTRIBUTEID NUMBER NOT NULL,
    DELETED NUMBER,
    ISSUE NUMBER NOT NULL,
    NUMERICVALUE NUMBER,
    TIMESTAMPVALUE TIMESTAMP,
    LONGTEXTVALUE VARCHAR2(4000),
    OPTIONID NUMBER,
    PERSON NUMBER,
    CHARVALUE VARCHAR2 (255),
    DISPLAYVALUE VARCHAR2 (255)
);

ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_PK
PRIMARY KEY (OBJECTID);



-----------------------------------------------------------------------------
-- TATTRIBUTEOPTION
-----------------------------------------------------------------------------
-- DROP TABLE TATTRIBUTEOPTION CASCADE CONSTRAINTS;

CREATE TABLE TATTRIBUTEOPTION
(
    OBJECTID NUMBER NOT NULL,
    ATTRIBUTEID NUMBER NOT NULL,
    PARENTOPTION NUMBER,
    OPTIONNAME VARCHAR2 (255) NOT NULL,
    DELETED NUMBER
);

ALTER TABLE TATTRIBUTEOPTION
    ADD CONSTRAINT TATTRIBUTEOPTION_PK
PRIMARY KEY (OBJECTID);



-----------------------------------------------------------------------------
-- TATTRIBUTECLASS
-----------------------------------------------------------------------------
-- DROP TABLE TATTRIBUTECLASS CASCADE CONSTRAINTS;

CREATE TABLE TATTRIBUTECLASS
(
    OBJECTID NUMBER NOT NULL,
    ATTRIBUTECLASSNAME VARCHAR2 (255) NOT NULL,
    ATTRIBUTECLASSDESC VARCHAR2 (64),
    JAVACLASSNAME VARCHAR2 (255)
);

ALTER TABLE TATTRIBUTECLASS
    ADD CONSTRAINT TATTRIBUTECLASS_PK
PRIMARY KEY (OBJECTID);



-----------------------------------------------------------------------------
-- TATTRIBUTETYPE
-----------------------------------------------------------------------------
-- DROP TABLE TATTRIBUTETYPE CASCADE CONSTRAINTS;

CREATE TABLE TATTRIBUTETYPE
(
    OBJECTID NUMBER NOT NULL,
    ATTRIBUTETYPENAME VARCHAR2 (255) NOT NULL,
    ATTRIBUTECLASS NUMBER NOT NULL,
    JAVACLASSNAME VARCHAR2 (255),
    VALIDATIONKEY VARCHAR2 (25)
);

ALTER TABLE TATTRIBUTETYPE
    ADD CONSTRAINT TATTRIBUTETYPE_PK
PRIMARY KEY (OBJECTID);



-----------------------------------------------------------------------------
-- TATTRIBUTE
-----------------------------------------------------------------------------
-- DROP TABLE TATTRIBUTE CASCADE CONSTRAINTS;

CREATE TABLE TATTRIBUTE
(
    OBJECTID NUMBER NOT NULL,
    ATTRIBUTENAME VARCHAR2 (255) NOT NULL,
    ATTRIBUTETYPE NUMBER NOT NULL,
    DELETED NUMBER,
    DESCRIPTION VARCHAR2 (64),
    PERMISSION VARCHAR2 (255),
    REQUIREDOPTION NUMBER
);

ALTER TABLE TATTRIBUTE
    ADD CONSTRAINT TATTRIBUTE_PK
PRIMARY KEY (OBJECTID);



-----------------------------------------------------------------------------
-- TREPORTLAYOUT
-----------------------------------------------------------------------------
-- DROP TABLE TREPORTLAYOUT CASCADE CONSTRAINTS;

CREATE TABLE TREPORTLAYOUT
(
    OBJECTID NUMBER NOT NULL,
    PROJECTTYPE NUMBER,
    PROJECT NUMBER,
    PERSON NUMBER,
    REPORTFIELD NUMBER NOT NULL,
    FPOSITION NUMBER NOT NULL,
    FWIDTH NUMBER NOT NULL,
    FSORTORDER NUMBER,
    FSORTDIR VARCHAR2 (1)
);

ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_PK
PRIMARY KEY (OBJECTID);



-----------------------------------------------------------------------------
-- TSCHEDULER
-----------------------------------------------------------------------------
-- DROP TABLE TSCHEDULER CASCADE CONSTRAINTS;

CREATE TABLE TSCHEDULER
(
    OBJECTID NUMBER NOT NULL,
    CRONWHEN VARCHAR2 (64) NOT NULL,
    JAVACLASSNAME VARCHAR2 (255),
    EXTERNALEXE VARCHAR2 (255),
    PERSON NUMBER NOT NULL
);

ALTER TABLE TSCHEDULER
    ADD CONSTRAINT TSCHEDULER_PK
PRIMARY KEY (OBJECTID);

ALTER TABLE TPPRIORITY
    ADD CONSTRAINT TPPRIORITY_FK_1 FOREIGN KEY (PRIORITY)
    REFERENCES TPRIORITY (PKEY)
;

ALTER TABLE TPPRIORITY
    ADD CONSTRAINT TPPRIORITY_FK_2 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TPPRIORITY
    ADD CONSTRAINT TPPRIORITY_FK_3 FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TPSEVERITY
    ADD CONSTRAINT TPSEVERITY_FK_1 FOREIGN KEY (SEVERITY)
    REFERENCES TSEVERITY (PKEY)
;

ALTER TABLE TPSEVERITY
    ADD CONSTRAINT TPSEVERITY_FK_2 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TPSEVERITY
    ADD CONSTRAINT TPSEVERITY_FK_3 FOREIGN KEY (LISTTYPE)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TWORKFLOW
    ADD CONSTRAINT TWORKFLOW_FK_3 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;



ALTER TABLE TWORKFLOWROLE
    ADD CONSTRAINT TWORKFLOWROLE_FK_1 FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOW (OBJECTID)
;

ALTER TABLE TWORKFLOWROLE
    ADD CONSTRAINT TWORKFLOWROLE_FK_2 FOREIGN KEY (MAYCHANGEROLE)
    REFERENCES TROLE (PKEY)
;



ALTER TABLE TWORKFLOWCATEGORY
    ADD CONSTRAINT TWORKFLOWCATEGORY_FK_1 FOREIGN KEY (WORKFLOW)
    REFERENCES TWORKFLOW (OBJECTID)
;

ALTER TABLE TWORKFLOWCATEGORY
    ADD CONSTRAINT TWORKFLOWCATEGORY_FK_2 FOREIGN KEY (CATEGORY)
    REFERENCES TCATEGORY (PKEY)
;

ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_2 FOREIGN KEY (ISSUE)
    REFERENCES TWORKITEM (WORKITEMKEY)
;

ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_3 FOREIGN KEY (ATTRIBUTEID)
    REFERENCES TATTRIBUTE (OBJECTID)
;

ALTER TABLE TISSUEATTRIBUTEVALUE
    ADD CONSTRAINT TISSUEATTRIBUTEVALUE_FK_4 FOREIGN KEY (OPTIONID)
    REFERENCES TATTRIBUTEOPTION (OBJECTID)
;



ALTER TABLE TATTRIBUTEOPTION
    ADD CONSTRAINT TATTRIBUTEOPTION_FK_1 FOREIGN KEY (ATTRIBUTEID)
    REFERENCES TATTRIBUTE (OBJECTID)
;





ALTER TABLE TATTRIBUTETYPE
    ADD CONSTRAINT TATTRIBUTETYPE_FK_1 FOREIGN KEY (ATTRIBUTECLASS)
    REFERENCES TATTRIBUTECLASS (OBJECTID)
;



ALTER TABLE TATTRIBUTE
    ADD CONSTRAINT TATTRIBUTE_FK_1 FOREIGN KEY (REQUIREDOPTION)
    REFERENCES TATTRIBUTEOPTION (OBJECTID)
;



ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_1 FOREIGN KEY (PROJECTTYPE)
    REFERENCES TPROJECTTYPE (OBJECTID)
;

ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_2 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;

ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_3 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
;

ALTER TABLE TREPORTLAYOUT
    ADD CONSTRAINT TREPORTLAYOUT_FK_4 FOREIGN KEY (REPORTFIELD)
    REFERENCES TISSUEATTRIBUTEVALUE (OBJECTID)
;


ALTER TABLE TSCHEDULER
    ADD CONSTRAINT TSCHEDULER_FK_1 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)

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

