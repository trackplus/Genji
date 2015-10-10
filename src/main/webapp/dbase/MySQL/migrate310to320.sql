-- MySQL update script from 310 to 320
-- $Id: migrate310to320.sql 652 2008-12-30 20:44:26Z friedj $

# Turn it off in case of InnoDB
SET FOREIGN_KEY_CHECKS = 0; 

-- TATTRIBUTEOPTION
ALTER TABLE TATTRIBUTEOPTION ADD SORTORDER INTEGER;

-- TREPORTLAYOUT
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
		            FSORTDIR VARCHAR (1),
    PRIMARY KEY(OBJECTID),
    FOREIGN KEY (PROJECTTYPE) REFERENCES TPROJECTTYPE (OBJECTID)
    ,
    FOREIGN KEY (PROJECT) REFERENCES TPROJECT (PKEY)
    ,
    FOREIGN KEY (PERSON) REFERENCES TPERSON (PKEY)
    ,
    FOREIGN KEY (REPORTFIELD) REFERENCES TATTRIBUTE (OBJECTID)
    
);

ALTER TABLE TSITE ADD PREFERENCES LONGTEXT;

-- =======================================================================
# Changes for upgrade from 3.2.0 B1 to 3.2.0 B2
-- TNOTIFY --
ALTER TABLE TNOTIFY ADD RACIROLE VARCHAR (1);
ALTER TABLE TNOTIFY ADD THEGROUP INTEGER;

-- TPERSON --
ALTER TABLE TPERSON ADD EMPLOYEEID VARCHAR (30);

-- TACCOUNT --
drop table if exists TACCOUNT;

CREATE TABLE TACCOUNT
(
		            OBJECTID INTEGER NOT NULL,
		            ACCOUNTNUMBER VARCHAR (30) NOT NULL,
		            ACCOUNTNAME VARCHAR (80),
		            ACTIVE VARCHAR (1),
		            COSTCENTER VARCHAR (20),
    PRIMARY KEY(OBJECTID)
);

# -----------------------------------------------------------------------
# TPROJECTACCOUNT
# -----------------------------------------------------------------------
drop table if exists TPROJECTACCOUNT;

CREATE TABLE TPROJECTACCOUNT
(
		            ACCOUNT INTEGER NOT NULL,
		            PROJECT INTEGER NOT NULL,
    PRIMARY KEY(ACCOUNT,PROJECT),
    FOREIGN KEY (PROJECT) REFERENCES TPROJECT (PKEY)
    ,
    FOREIGN KEY (ACCOUNT) REFERENCES TACCOUNT (OBJECTID)
    
);

# -----------------------------------------------------------------------
# TGROUP
# -----------------------------------------------------------------------
drop table if exists TGROUP;

CREATE TABLE TGROUP
(
		            ObjectID INTEGER NOT NULL,
		            EMAIL VARCHAR (60),
		            PREFERENCES LONGTEXT,
    PRIMARY KEY(ObjectID)
);

# -----------------------------------------------------------------------
# TGROUPMEMBER
# -----------------------------------------------------------------------
drop table if exists TGROUPMEMBER;

CREATE TABLE TGROUPMEMBER
(
		            OBJECTID INTEGER NOT NULL,
		            THEGROUP INTEGER NOT NULL,
		            PERSON INTEGER NOT NULL,
    PRIMARY KEY(OBJECTID),
    FOREIGN KEY (THEGROUP) REFERENCES TGROUP (OBJECTID)
    ,
    FOREIGN KEY (PERSON) REFERENCES TPERSON (PKEY)
    
);

ALTER TABLE TNOTIFY ADD CONSTRAINT TNOTIFY_FK20 
          FOREIGN KEY (THEGROUP) REFERENCES TGROUP (OBJECTID);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(47, 'TGROUP', 1, 1); 

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(48, 'TGROUPMEMBER', 1, 1);
            
UPDATE ID_TABLE SET NEXT_ID=14 WHERE TABLE_NAME='TCATEGORY';            

# Turn it on in case of InnoDB
SET FOREIGN_KEY_CHECKS = 1; 
COMMIT;










