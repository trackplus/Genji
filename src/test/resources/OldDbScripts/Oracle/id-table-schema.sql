
-----------------------------------------------------------------------------
-- ID_TABLE
-----------------------------------------------------------------------------
DROP TABLE ID_TABLE CASCADE CONSTRAINTS;

CREATE TABLE ID_TABLE
(
    ID_TABLE_ID NUMBER(10,0) NOT NULL,
    TABLE_NAME VARCHAR2(128) NOT NULL,
    NEXT_ID NUMBER(10,0),
    QUANTITY NUMBER(10,0),
    CONSTRAINT ID_TABLE_U_1 UNIQUE (TABLE_NAME)
);

ALTER TABLE ID_TABLE
    ADD CONSTRAINT ID_TABLE_PK
PRIMARY KEY (ID_TABLE_ID);







