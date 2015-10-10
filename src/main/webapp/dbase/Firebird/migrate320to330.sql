ALTER TABLE TBASELINE DROP CONSTRAINT TBASELINE_FK_3;            
ALTER TABLE TNOTIFY DROP CONSTRAINT TNOTIFY_FK_5;
ALTER TABLE TWORKITEM DROP CONSTRAINT TWORKITEM_FK_13;
ALTER TABLE TCOST DROP CONSTRAINT TCOST_FK_4;    
ALTER TABLE TPCURRENCY DROP CONSTRAINT TPCURRENCY_FK_1;
ALTER TABLE TPCURRENCY DROP CONSTRAINT TPCURRENCY_FK_2;
ALTER TABLE TGROUPMEMBER DROP CONSTRAINT TGROUPMEMBER_FK_1;            

ALTER TABLE TBASELINE DROP EFFORT;
ALTER TABLE TBASELINE DROP ESTIMATEDHOURS;
ALTER TABLE TBASELINE DROP ESTIMATEDCOST;
ALTER TABLE TBASELINE DROP CURRENCY;

ALTER TABLE TDEPARTMENT ADD COSTCENTER INTEGER;

ALTER TABLE TNOTIFY DROP THEGROUP;

ALTER TABLE TPERSON ADD HOURSPERWORKDAY DOUBLE PRECISION;
ALTER TABLE TPERSON ADD ISGROUP CHAR (1) default 'N';

ALTER TABLE TPRIORITY ADD SYMBOL VARCHAR (255);

ALTER TABLE TPROJECT ADD STATUS INTEGER;
ALTER TABLE TPROJECT ADD CURRENCYNAME VARCHAR (255);
ALTER TABLE TPROJECT ADD CURRENCYSYMBOL VARCHAR (255);
ALTER TABLE TPROJECT ADD HOURSPERWORKDAY DOUBLE PRECISION;
ALTER TABLE TPROJECT ADD MOREPROPS VARCHAR (10000);
ALTER TABLE TPROJECT ADD DESCRIPTION VARCHAR (255);


ALTER TABLE TRELEASE ADD STATUS INTEGER;
ALTER TABLE TRELEASE ADD SORTORDER INTEGER;
ALTER TABLE TRELEASE ADD MOREPROPS VARCHAR (10000);
ALTER TABLE TRELEASE ADD DESCRIPTION VARCHAR (255);
ALTER TABLE TRELEASE ADD DUEDATE TIMESTAMP;

ALTER TABLE TSEVERITY ADD SYMBOL VARCHAR (255);

ALTER TABLE TSTATE ADD SYMBOL VARCHAR (255);

ALTER TABLE TWORKITEM ADD SUBMITTEREMAIL VARCHAR (60);
ALTER TABLE TWORKITEM ADD ACCESSLEVEL INTEGER;
ALTER TABLE TWORKITEM DROP ESTIMATEDHOURS; 
ALTER TABLE TWORKITEM DROP ESTIMATEDCOST;
ALTER TABLE TWORKITEM DROP CURRENCY;

CREATE  INDEX TWIINDEX18 ON TWORKITEM (ACCESSLEVEL);

ALTER TABLE TACCOUNT DROP "ACTIVE";
ALTER TABLE TACCOUNT DROP "COSTCENTER";
ALTER TABLE TACCOUNT ADD COSTCENTER INTEGER;
ALTER TABLE TACCOUNT ADD STATUS INTEGER;
ALTER TABLE TACCOUNT ADD DESCRIPTION VARCHAR (255);
ALTER TABLE TACCOUNT ADD MOREPROPS VARCHAR (10000);

ALTER TABLE TCOST DROP CURRENCY;
ALTER TABLE TCOST ADD SUBJECT VARCHAR (255);
ALTER TABLE TCOST ADD DESCRIPTION VARCHAR (255);
ALTER TABLE TCOST ADD MOREPROPS VARCHAR (10000);
ALTER TABLE TCOST ADD LASTEDIT TIMESTAMP;

DROP TABLE TPCURRENCY;
DROP TABLE TCURRENCY;


ALTER TABLE TPROJECTTYPE ADD HOURSPERWORKDAY DOUBLE PRECISION;
ALTER TABLE TPROJECTTYPE ADD MOREPROPS VARCHAR (10000);

DROP TABLE TGROUP;

CREATE TABLE TBUDGET
(
    OBJECTID INTEGER NOT NULL,
    WORKITEMKEY INTEGER NOT NULL,
    ESTIMATEDHOURS DOUBLE PRECISION,
    TIMEUNIT INTEGER,
    ESTIMATEDCOST DOUBLE PRECISION,
    CHANGEDESCRIPTION VARCHAR (255),
    MOREPROPS VARCHAR (10000),
    CHANGEDBY INTEGER,
    LASTEDIT TIMESTAMP,
    PRIMARY KEY(OBJECTID)
);

CREATE  INDEX TBUDGETINDEX1 ON TBUDGET (WORKITEMKEY);
CREATE  INDEX TBUDGETINDEX2 ON TBUDGET (CHANGEDBY);


CREATE TABLE TACTUALESTIMATEDBUDGET
(
    OBJECTID INTEGER NOT NULL,
    WORKITEMKEY INTEGER NOT NULL,
    ESTIMATEDHOURS DOUBLE PRECISION,
    TIMEUNIT INTEGER,
    ESTIMATEDCOST DOUBLE PRECISION,
    CHANGEDBY INTEGER,
    LASTEDIT TIMESTAMP,
    PRIMARY KEY(OBJECTID)
);

CREATE  INDEX TACTUALESTINDEX1 ON TACTUALESTIMATEDBUDGET (WORKITEMKEY);
CREATE  INDEX TACTUALESTINDEX2 ON TACTUALESTIMATEDBUDGET (CHANGEDBY);


CREATE TABLE TSYSTEMSTATE
(
    OBJECTID INTEGER NOT NULL,
    LABEL VARCHAR (255),
    STATEFLAG INTEGER,
    SYMBOL VARCHAR (255),
    ENTITYFLAG INTEGER,
    SORTORDER INTEGER,
    PRIMARY KEY(OBJECTID)
);


CREATE TABLE TCOSTCENTER
(
    OBJECTID INTEGER NOT NULL,
    COSTCENTERNUMBER VARCHAR (30) NOT NULL,
    COSTCENTERNAME VARCHAR (80),
    MOREPROPS VARCHAR (10000),
    PRIMARY KEY(OBJECTID)
);



CREATE TABLE TMOTD
(
    OBJECTID INTEGER NOT NULL,
    THELOCALE VARCHAR (2),
    THEMESSAGE VARCHAR (32000),
    PRIMARY KEY(OBJECTID)
);

ALTER TABLE TDEPARTMENT ADD CONSTRAINT TDEPARTMENT_FK_1 FOREIGN KEY (COSTCENTER) REFERENCES TCOSTCENTER (OBJECTID);
ALTER TABLE TPROJECT ADD CONSTRAINT TPROJECT_FK_5 FOREIGN KEY (STATUS) REFERENCES TSYSTEMSTATE (OBJECTID);
ALTER TABLE TRELEASE ADD CONSTRAINT TRELEASE_FK_2 FOREIGN KEY (STATUS) REFERENCES TSYSTEMSTATE (OBJECTID);        
ALTER TABLE TACCOUNT ADD CONSTRAINT TACCOUNT_FK_1 FOREIGN KEY (STATUS) REFERENCES TSYSTEMSTATE (OBJECTID);
ALTER TABLE TACCOUNT ADD CONSTRAINT TACCOUNT_FK_2 FOREIGN KEY (COSTCENTER) REFERENCES TCOSTCENTER (OBJECTID);
ALTER TABLE TGROUPMEMBER ADD CONSTRAINT TGROUPMEMBER_FK_1 FOREIGN KEY (THEGROUP) REFERENCES TPERSON (PKEY);    
ALTER TABLE TBUDGET ADD CONSTRAINT TBUDGET_FK_1 FOREIGN KEY (WORKITEMKEY) REFERENCES TWORKITEM (WORKITEMKEY);
ALTER TABLE TBUDGET ADD CONSTRAINT TBUDGET_FK_2 FOREIGN KEY (CHANGEDBY) REFERENCES TPERSON (PKEY);    
ALTER TABLE TACTUALESTIMATEDBUDGET ADD CONSTRAINT TACTUALESTIMATEDBUDGET_FK_1 FOREIGN KEY (WORKITEMKEY) REFERENCES TWORKITEM (WORKITEMKEY);
ALTER TABLE TACTUALESTIMATEDBUDGET ADD CONSTRAINT TACTUALESTIMATEDBUDGET_FK_2 FOREIGN KEY (CHANGEDBY) REFERENCES TPERSON (PKEY);

/*
DML statements
*/

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(49, 'TBUDGET', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(50, 'TACTUALESTIMATEDBUDGET', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(51, 'TSYSTEMSTATE', 100, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(52, 'TCOSTCENTER', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(53, 'TMOTD', 1, 1);

INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER)
       VALUES (1,'proposed', 0, 1, 1);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (2,'in planning', 0, 1, 2);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (3,'in progress', 0, 1, 3);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (4,'on hold', 1, 1, 4);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (5,'released', 1, 1, 5);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (6,'archived', 2, 1, 6);

INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER)
       VALUES (7,'proposed', 0, 2, 1);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (8,'in planning', 0, 2, 2);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (9,'in progress', 0, 2, 3);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (10,'on hold', 1, 2, 4);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (11,'released', 1, 2, 5);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (12,'archived', 2, 2, 6);

INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER)
       VALUES (13,'opened', 0, 3, 1);
INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER) 
       VALUES (14,'closed', 2, 3, 3);

UPDATE TPERSON SET ISGROUP = 'N' WHERE ISGROUP IS NULL;

UPDATE TPROJECT SET STATUS = 3 WHERE STATUS IS NULL AND (DELETED IS NULL OR DELETED = 'N');
UPDATE TPROJECT SET STATUS = 6 WHERE STATUS IS NULL AND DELETED = 'Y';
/*ALTER TABLE TPROJECT DROP DELETED;*/

UPDATE TRELEASE SET STATUS = 9 WHERE STATUS IS NULL;

UPDATE TACCOUNT SET STATUS = 13 WHERE STATUS IS NULL;

COMMIT;