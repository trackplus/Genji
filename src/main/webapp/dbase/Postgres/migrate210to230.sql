
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY)
VALUES (21, 'TPRIVATEREPORTREPOSITORY', 1, 1);


INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY)
VALUES (22, 'TPROJECTREPORTREPOSITORY', 1, 1);


INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY)
VALUES (23, 'TPUBLICREPORTREPOSITORY', 1, 1);

COMMIT;


----------------------------------------------------------------------
-- TPRIVATEREPORTREPOSITORY                                                      
----------------------------------------------------------------------


CREATE TABLE TPRIVATEREPORTREPOSITORY

(

	PKEY integer NOT NULL,	

	OWNER integer NOT NULL,

	NAME varchar(100) NOT NULL,

	QUERY text NOT NULL,

	PRIMARY KEY (PKEY)

);



----------------------------------------------------------------------
-- TPUBLICREPORTREPOSITORY                                                      
----------------------------------------------------------------------


CREATE TABLE TPUBLICREPORTREPOSITORY

(

	PKEY integer NOT NULL,

	OWNER integer NOT NULL,

	NAME varchar(100) NOT NULL,

	QUERY text NOT NULL,

	PRIMARY KEY (PKEY)

);



----------------------------------------------------------------------
-- TPROJECTREPORTREPOSITORY                                                      
----------------------------------------------------------------------


CREATE TABLE TPROJECTREPORTREPOSITORY

(

	PKEY integer NOT NULL,	

	PROJECT integer NOT NULL,

	NAME varchar(100) NOT NULL,

	QUERY text NOT NULL,

	PRIMARY KEY (PKEY)

);

----------------------------------------------------------------------
-- TPRIVATEREPORTREPOSITORY                                                      
----------------------------------------------------------------------

ALTER TABLE TPRIVATEREPORTREPOSITORY
    ADD CONSTRAINT TPRIVATEREPORTREPOSITORY_FK_1 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)

;

----------------------------------------------------------------------
-- TPUBLICREPORTREPOSITORY                                                      
----------------------------------------------------------------------

ALTER TABLE TPUBLICREPORTREPOSITORY
    ADD CONSTRAINT TPUBLICREPORTREPOSITORY_FK_1 FOREIGN KEY (OWNER)
    REFERENCES TPERSON (PKEY)
;

----------------------------------------------------------------------
-- TPROJECTREPORTREPOSITORY                                                      
----------------------------------------------------------------------

ALTER TABLE TPROJECTREPORTREPOSITORY
    ADD CONSTRAINT TPROJECTREPORTREPOSITORY_FK_1 FOREIGN KEY (PROJECT)
    REFERENCES TPROJECT (PKEY)
;
